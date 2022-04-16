package si.f5.hatosaba.uhcffa.arena;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import com.lielamar.lielsutils.bukkit.color.ColorUtils;
import com.rexcantor64.triton.Triton;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.utils.*;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.util.*;

public class Arena {

    private UUID currentUUID = UUID.randomUUID();
    private final LinkedHashMap<UUID, Integer> kills = new LinkedHashMap<>();
    private final List<CustomPlayer> players = new LinkedList<>();
    private final List<CustomPlayer> spectators = new LinkedList<>();
    private final List<CustomPlayer> APlayers = new LinkedList<>();
    private final List<CustomPlayer> BPlayers = new LinkedList<>();
    private final List<Item> droppedItem = new LinkedList<>();
    private final List<Block> placed = new LinkedList<>();
    private final List<Block> bucketPlaced = new LinkedList<>();
    private final String name;
    private final String displayName;
    private final Location waitingLocation;
    private final Location spectatorLocation;
    private final Location spawn1;
    private final Location spawn2;
    private final double maxBuildY;
    private boolean isOffline = false;
    private int offlineCount = 0;
    private int maxOfflineCount = 5;
    private boolean counting = false;
    private int maxCount = 6;
    private int count = 0;
    private int maxPlayerSize = 2;
    private ArenaState arenaState;

    private BukkitTask startTask;
    private BukkitTask runTask;
    private BukkitTask stopTask;

    private Kit kit;

    public Arena(Yaml yaml) {
        this.name = ChatColor.translateAlternateColorCodes('&', yaml.name);
        this.maxBuildY = yaml.getDouble("maxBuildY");
        this.displayName = name;
        this.waitingLocation = LocationUtil.stringToLocation(yaml.getString("spawn1"));
        this.spectatorLocation = LocationUtil.stringToLocation(yaml.getString("spawn1"));
        this.spawn1 = LocationUtil.stringToLocation(yaml.getString("spawn1"));
        this.spawn2 = LocationUtil.stringToLocation(yaml.getString("spawn2"));
        this.arenaState = ArenaState.WAITING_FOR_PLAYERS;
    }

    public Arena(String name, double maxBuildY, Location spawn1, Location spawn2) {
        this.name = name;
        this.maxBuildY = maxBuildY;
        this.displayName = name;
        this.waitingLocation = spawn1;
        this.spectatorLocation = spawn1;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.arenaState = ArenaState.WAITING_FOR_PLAYERS;
    }

    public void addPlayer(CustomPlayer customPlayer) {
        if (players.contains(customPlayer)) {
            return;
        }

        customPlayer.setArena(this);

        Player player = customPlayer.getPlayer();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
        player.setLevel(0);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setFallDistance(0.0f);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.teleport(waitingLocation.clone());

        ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, player);

        SpectetorSet.getInstance().applyShowMode(player);

        players.add(customPlayer);

        String name = player.getName();
        String playerSize = String.valueOf(players.size());
        String playerMaxSize = String.valueOf(maxPlayerSize);
        sendGameMessage("duel.join",name, playerSize ,playerMaxSize );

        if (isFull()) {
            arenaState = ArenaState.COUNTING_DOWN;
            startTask = new BukkitRunnable() {
                int i = 5; // start time
                @Override
                public void run() {
                    if(i == 0) {
                        this.cancel();
                        startGame();
                        sendGameTitle("&cGo", "");
                        playSound(Sound.ENDERDRAGON_GROWL, 1, 1);
                        startTask = null;
                        return;
                    }

                    if(i <= 5) {
                        playSound(Sound.WOOD_CLICK, 1F, 1F);
                        sendGameTitle("&e" + i, "");
                        sendGameMessage("duel.countdown", String.valueOf(i));
                    }
                    i--;
                }
            }.runTaskTimer(Uhcffa.getInstance(), 0L, 20L);
        }
    }

    public void removePlayer(CustomPlayer customPlayer) {
        if (!players.contains(customPlayer)) {
            return;
        }

        Player player = customPlayer.getPlayer();

        customPlayer.setArena(null);
        player.setGameMode(GameMode.ADVENTURE);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setPlayerListName(player.getPlayerListName());
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setExp(0.0f);
        player.setLevel(0);
        player.setFallDistance(0.0f);
        player.setFireTicks(0);
        player.getInventory().setArmorContents(null);

        if (players.size() == 1)
            setKit(null);

        if (arenaState == ArenaState.COUNTING_DOWN) {
            if (this.startTask != null) {
                this.startTask.cancel();
                arenaState = ArenaState.WAITING_FOR_PLAYERS;
                players.forEach(s -> {
                    //fixPlayer(s);
                    ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, s.getPlayer());

                });
                sendGameMessage("duel.cancel");
            }
        }

        Uhcffa.getInstance().setLobbyItem(player);
        player.teleport(Uhcffa.getInstance().config().getLobby());
        SpectetorSet.getInstance().applyHideMode(player);
        players.remove(customPlayer);
        //endGame();
    }

    public void addSpectator(CustomPlayer customPlayer) {
        if (this.spectators.contains(customPlayer)) {
            return;
        }
        customPlayer.setArena(this);
        //customPlayer.setSpectator(true);

        Player player = customPlayer.getPlayer();
        player.setLevel(0);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setFallDistance(0.0f);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setExp(0.0f);
        player.hidePlayer(customPlayer.getPlayer());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHeldItemSlot(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0), true);
        player.teleport(this.spectatorLocation.clone());

        ExecutableItemType.DUEL_PLAY_AGAIN.setItem(4,player);
        ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, player);
        this.spectators.add(customPlayer);
        for (CustomPlayer po : getPlayers()) {
            po.getPlayer().hidePlayer(player);
        }
    }

    public void removeSpectator(CustomPlayer customPlayer) {
        if (!this.spectators.contains(customPlayer)) {
            return;
        }
        this.spectators.remove(customPlayer);
        customPlayer.setArena(null);
        //customPlayer.setSpectator(false);
        Player player = customPlayer.getPlayer();
        player.setLevel(0);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFoodLevel(20);
        player.setFallDistance(0.0f);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setExp(0.0f);
        player.showPlayer(customPlayer.getPlayer());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHeldItemSlot(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        for (CustomPlayer po : getPlayers()) {
            po.getPlayer().showPlayer(player);
        }
    }

    public void startGame() {
        int minute = 500;
        int second = 0;

        this.maxCount = 60 * minute + second;
        this.count = 0;
        this.counting = true;
        this.arenaState = ArenaState.IN_GAME;

        Collections.shuffle(this.players);
        for (CustomPlayer customPlayer : this.players) {
            if (this.APlayers.size() < this.BPlayers.size()) {
                this.APlayers.add(customPlayer);
            } else {
                this.BPlayers.add(customPlayer);
            }
        }

        for (CustomPlayer customPlayer : this.APlayers) {
            customPlayer.getPlayer().teleport(this.spawn1.clone());
        }

        for (CustomPlayer customPlayer : this.BPlayers) {
            customPlayer.getPlayer().teleport(this.spawn2.clone());
        }

        for (CustomPlayer customPlayer : this.players) {
            Player player = customPlayer.getPlayer();

            player.setGameMode(GameMode.SURVIVAL);
            player.setLevel(0);
            player.setMaxHealth(20.0);
            player.setHealth(player.getMaxHealth());
            player.setFireTicks(0);
            player.setFoodLevel(20);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setExp(0.0f);
            player.setFallDistance(0.0f);
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.getInventory().clear();
            player.getInventory().setHeldItemSlot(0);
            player.getInventory().setArmorContents(null);
            applyKit(customPlayer.getPlayerID());
        }
    }

    public void applyKit(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);
        kit.apply(player);
    }

    public void endGame() {
        arenaState = ArenaState.GAME_END;

        removeDropItem();

        if (players.size() <= 0) {
            return;
        }

        CustomPlayer spectator = this.spectators.isEmpty() ? null : this.spectators.get(0);
        CustomPlayer winner = (spectator != null)
                ? (this.APlayers.contains(spectator) ? (this.BPlayers.isEmpty() ? spectator : this.BPlayers.get(0))
                : (this.APlayers.isEmpty() ? spectator : this.APlayers.get(0)))
                : this.players.get(0);

        winner.getPlayer().spigot().sendMessage(new ComponentBuilder(Translated.key("duel.inventories").args(spectator.getPlayer().getName()).get(spectator.getPlayer()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + spectator.getPlayer().getName()))
                .create());
        winner.sendTranslated("duel.result-win", spectator.getPlayer().getName());
        Titles.sendTitle(winner.getPlayer(), Translated.key("duel.you-win").get(winner.getPlayer()), "");

        spectator.getPlayer().spigot().sendMessage(new ComponentBuilder(Translated.key("duel.inventories").args(spectator.getPlayer().getName()).get(spectator.getPlayer()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + winner.getPlayer().getName()))
                .create());
        spectator.sendTranslated("duel.result-loser", winner.getPlayer().getName());
        Titles.sendTitle(spectator.getPlayer(), Translated.key("duel.you-loser").get(spectator.getPlayer()), "");

        final Player player = winner.getPlayer();
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setExp(0.0f);
        player.setLevel(0);
        player.setFallDistance(0.0f);
        player.setFireTicks(0);
        player.getInventory().clear();

        ExecutableItemType.DUEL_PLAY_AGAIN.setItem(4,player);
        ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, player);


        List<CustomPlayer> targetList = new LinkedList<>(this.players);
        for (CustomPlayer playerID : targetList) {

            //invs.put(playerID, PlayerConverter.getPlayer(playerID).getInventory().getContents());
            //armors.put(playerID, PlayerConverter.getPlayer(playerID).getEquipment().getArmorContents());
            //String enemyName = PlayerConverter.getName(getEnemy(playerID));
            //sendGameMessage(playerID ,ColorUtils.translateAlternateColorCodes('&',"&7&m&l============================================"));
            //ChatUtils.sendCenteredMessage(playerID, message);
            /*PlayerConverter.getPlayer(playerID).spigot().sendMessage(new ComponentBuilder(TextUtil.colorize( "&eClick to view " + enemyName + "'s Inventory"))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + enemyName))
                    .create());*/
            //sendMessage(playerID ,ColorUtils.translateAlternateColorCodes('&',"&7&m&l============================================"));
        }
        stopMinigame();
    }


    public void stopMinigame() {
        if(this.startTask != null) this.startTask.cancel();
        if(this.runTask != null) this.runTask.cancel();

        this.arenaState = ArenaState.GAME_END;
        stopTask = new BukkitRunnable() {
            int i = 10; // end time
            @Override
            public void run() {
                if(i == 0) {
                    this.cancel();
                    sendGameTitle("&cGAME OVER", "");
                    nextJoinGame();
                    reset();
                    stopTask = null;
                    return;
                }
                //elapsedTime++;
                i--;
            }
        }.runTaskTimer(Uhcffa.getInstance(), 0L, 20L);
    }
    public void nextJoinGame() {
        List<CustomPlayer> targetList = new LinkedList<>(this.players);
        for (CustomPlayer customPlayer : targetList) {
            nextJoinGame(customPlayer);
        }
    }

    public void nextJoinGame(CustomPlayer customPlayer) {
        players.remove(customPlayer);
        Uhcffa.getInstance().getArenaManager().joinMatch(customPlayer, kit);
    }

    public void onDeath(CustomPlayer customPlayer) {
        CustomPlayer killer = null;
        Player player = customPlayer.getPlayer();
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) player.getLastDamageCause();
            if (event.getDamager() instanceof Player) {
                Player damagePlayer = (Player) event.getDamager();
                killer = Uhcffa.getCustomPlayer(damagePlayer);
            }
        }
        if (killer != null) {
            //this.kills.put(killer.getUniqueId(), this.kills.getOrDefault(killer.getUniqueId(), 0) + 1);
        }
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setExp(0.0f);
        player.setLevel(0);
        player.setFallDistance(0.0f);
        player.setFireTicks(0);
        player.getInventory().clear();

        Sync.define(() -> {
            ExecutableItemType.DUEL_PLAY_AGAIN.setItem(4,player);
            ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, player);
        }).execute();

        spectators.add(customPlayer);

        /*customPlayer.setBestStreak(0);
        customPlayer.setDeaths(playerObject.getDeaths() + 1);*/
        //this.getPlayers().remove(customPlayer);
        //this.addSpectator(customPlayer);
        endGame();
    }

    public void playSound(String key) {
    }

    public void sendGameMessage(String text) {
        this.players.stream().forEach(playerObject -> playerObject.sendTranslated(text));
    }

    public void sendGameMessage(String key, String... args) {
        this.players.stream().forEach(playerObject -> playerObject.sendTranslated(key, args));
    }

    public void sendGameTitle(String title, String subtitle) {
        this.players.stream().forEach(playerObject -> Titles.sendTitle(playerObject.getPlayer(), 5, 20, 5,
                TextUtil.colorize(title), TextUtil.colorize(subtitle)));
    }

    public void sendCenterGameMessage(String text) {
       /* this.players.stream()
                .forEach(playerObject -> ChatUtils.sendCenteredMessage(playerObject.getPlayer(), text));*/
    }

    public boolean isFull() {
        return this.players.size() >= this.maxPlayerSize;
    }

    public void reset() {
        this.replaceBlock();
        this.removeDropItem();

        this.currentUUID = UUID.randomUUID();
        //this.arenaState = ArenaState.GAME_END;
        this.maxPlayerSize = 2;
        this.players.clear();
        this.spectators.clear();
        this.APlayers.clear();
        this.BPlayers.clear();
        this.counting = false;
        this.maxCount = 6;
        this.count = 0;
        this.placed.clear();
        this.bucketPlaced.clear();
        this.droppedItem.clear();
        this.kills.clear();
        this.arenaState = ArenaState.WAITING_FOR_PLAYERS;
    }

    public void shutdown() {
        this.replaceBlock();
        this.removeDropItem();
    }

    private void replaceBlock() {
        for (Block block : this.placed) {
            block.setType(Material.AIR);
        }
        for (Block block : this.bucketPlaced) {
            List<Block> trackBlocks = new LinkedList<>();
            BucketUtils.trackWater(block, trackBlocks);
            BucketUtils.trackLava(block, trackBlocks);
            for (Block targetBlock : trackBlocks) {
                targetBlock.setType(Material.AIR);
            }
        }
    }

    private void removeDropItem() {
        for (Item item : this.droppedItem) {
            if (item != null && !item.isDead()) {
                item.remove();
            }
        }
    }

    public List<CustomPlayer> getMyTeam(CustomPlayer playerObject) {
        if (this.getAPlayers().contains(playerObject)) {
            return this.getAPlayers();
        }
        return this.getBPlayers();
    }

    public List<CustomPlayer> getOtherTeam(CustomPlayer playerObject) {
        if (this.getAPlayers().contains(playerObject)) {
            return this.getBPlayers();
        }
        return this.getAPlayers();
    }

    public CustomPlayer getEnemy(CustomPlayer playerObject) {
        if (this.APlayers.contains(playerObject)) {
            if (this.BPlayers.size() == 0) {
                return null;
            }
            return this.BPlayers.get(0);
        } else {
            if (this.APlayers.size() == 0) {
                return null;
            }
            return this.APlayers.get(0);
        }
    }

    public String ingameTime() {
        int second = this.isOffline ? (this.maxOfflineCount - this.offlineCount) : (this.maxCount - this.count);
        int minute = second / 60;
        second -= minute * 60;
        return "0" + minute + ":" + ((second <= 9) ? "0" : "") + second;
    }

    public UUID getCurrentUUID() {
        return this.currentUUID;
    }

    public String getName() {
        return this.name;
    }

    public Location getWaitingLocation() {
        return this.waitingLocation;
    }

    public Location getSpectatorLocation() {
        return this.spectatorLocation;
    }

    public Location getSpawn1() {
        return this.spawn1;
    }

    public Location getSpawn2() {
        return this.spawn2;
    }

    public double getMaxBuildY() {
        return this.maxBuildY;
    }

    public HashMap<UUID, Integer> getKills() {
        return this.kills;
    }

    public List<CustomPlayer> getPlayers() {
        return this.players;
    }

    public List<CustomPlayer> getSpectators() {
        return this.spectators;
    }

    public List<CustomPlayer> getAPlayers() {
        return this.APlayers;
    }

    public List<CustomPlayer> getBPlayers() {
        return this.BPlayers;
    }

    public List<Item> getDroppedItem() {
        return this.droppedItem;
    }

    public List<Block> getPlaced() {
        return this.placed;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public List<Block> getBucketPlaced() {
        return this.bucketPlaced;
    }

    public void setOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public boolean isOffline() {
        return this.isOffline;
    }

    public void setOfflineCount(int offlineCount) {
        this.offlineCount = offlineCount;
    }

    public int getOfflineCount() {
        return this.offlineCount;
    }

    public void setMaxOfflineCount(int maxOfflineCount) {
        this.maxOfflineCount = maxOfflineCount;
    }

    public int getMaxOfflineCount() {
        return this.maxOfflineCount;
    }

    public void setCounting(boolean counting) {
        this.counting = counting;
    }

    public boolean isCounting() {
        return this.counting;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setMaxPlayerSize(int maxPlayerSize) {
        this.maxPlayerSize = maxPlayerSize;
    }

    public int getMaxPlayerSize() {
        return this.maxPlayerSize;
    }

    public ArenaState getArenaState() {
        return this.arenaState;
    }

    public void playSound(Sound sound, float pitch, float volume) {
        for(CustomPlayer customPlayer : players) {
            final Player player = customPlayer.getPlayer();
            if (player != null)
                player.getPlayer().playSound(player.getLocation(), sound, pitch, volume);
        }
    }

    public enum TextMode {
        CENTER, REPLACE;
    }
}