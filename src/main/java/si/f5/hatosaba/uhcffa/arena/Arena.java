package si.f5.hatosaba.uhcffa.arena;

import com.cryptomorin.xseries.messages.Titles;
import com.lielamar.lielsutils.bukkit.color.ColorUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.menu.InvSeeMenu;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.utils.*;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Arena {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    private final String name;

    private BukkitTask startTask;
    private BukkitTask runTask;
    private BukkitTask stopTask;

    private ArenaState state;

    private int maxPlayerSize = 2;
    private final double maxBuildY;

    private final List<String> players = new LinkedList<>();
    private final List<String> APlayers = new LinkedList<>();
    private final List<String> BPlayers = new LinkedList<>();
    private final List<String> spectators = new LinkedList<>();
    private final List<Item> droppedItem = new LinkedList<>();
    private final List<Block> placed = new LinkedList<>();
    private final List<Block> bucketPlaced = new LinkedList<>();
    private final LinkedHashMap<String, Integer> kills = new LinkedHashMap<>();

    private final LinkedHashMap<String, ItemStack[]> armors = new LinkedHashMap<>();
    private final LinkedHashMap<String, ItemStack[]> invs = new LinkedHashMap<>();


    private final Location spawn1;
    private final Location spawn2;

    private Kit kit;

    private int elapsedTime;

    public Arena(Yaml yaml) {
        this.name = ChatColor.translateAlternateColorCodes('&', yaml.name);
        this.maxBuildY = yaml.getDouble("maxBuildY");
        this.spawn1 = LocationUtil.stringToLocation(yaml.getString("spawn1"));
        this.spawn2 = LocationUtil.stringToLocation(yaml.getString("spawn2"));
        this.state = ArenaState.WAITING_FOR_PLAYERS;
    }

    public Arena(String name, double maxBuildY,  Location spawn1, Location spawn2) {
        this.name = name;
        this.maxBuildY = maxBuildY;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.state = ArenaState.WAITING_FOR_PLAYERS;
    }

    public boolean isFull() {
        return this.players.size() >= this.maxPlayerSize;
    }

    public void startMinigame() {
        if(stopTask != null) return;

        this.state = ArenaState.COUNTING_DOWN;

        Collections.shuffle(players);
        for (String playerID : players) {
            if (APlayers.size() < BPlayers.size()) {
                APlayers.add(playerID);
            } else {
                BPlayers.add(playerID);
            }
            applyKit(playerID);
        }
        for (String playerID : APlayers) {
            final Player player = PlayerConverter.getPlayer(playerID);
            player.getPlayer().teleport(this.spawn1.clone());
        }

        for (String playerID : BPlayers) {
            final Player player = PlayerConverter.getPlayer(playerID);
            player.teleport(this.spawn2.clone());
        }

        this.startTask = new BukkitRunnable() {
            int i = 5; // start time

            @Override
            public void run() {
                if(i == 0) {
                    this.cancel();
                    //(playerID);
                    //startGame();
                    runMinigame();
                    sendTitle("&cGo", "");
                    playSound(Sound.ENDERDRAGON_GROWL, 1, 1);
                    startTask = null;
                    return;
                }

                if(i <= 5) {
                    playSound(Sound.WOOD_CLICK, 1F, 1F);
                    sendMessage("&e" + i);
                }
                elapsedTime++;
                i--;
            }
        }.runTaskTimer(Uhcffa.getInstance(), 0L, 20L);
    }

    public void startGame() {

    }

    public void onDeath(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);

        /*spectetorSet.applyHideMode(player);
        spectators.add(playerID);*/
        endGame();
        Sync.define(() -> players.forEach(s -> {
            fixPlayer(s);
            ExecutableItemType.DUEL_PLAY_AGAIN.setItem(4, s);
            ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, s);
        })).execute();
    }

    public void fixPlayer(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFoodLevel(20);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setLevel(0);
        player.setExp(0);
        player.setFireTicks(0);

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
        player.updateInventory();

        for(PotionEffect pe : player.getActivePotionEffects())
            player.removePotionEffect(pe.getType());
    }

    public void applyKit(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);
        kit.apply(player);
    }

    /**
     * Starts the minigame timer
     */
    public void runMinigame() {
        if(this.startTask != null)
            this.startTask.cancel();

        this.state = ArenaState.IN_GAME;
        this.runTask = new BukkitRunnable() {
            @Override
            public void run() {
                elapsedTime++;
            }
        }.runTaskTimer(Uhcffa.getInstance(), 0L, 20L);
    }

    public void addPlayer(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);
        /*if (players.contains(playerID)) {
            player.sendMessage("既に参加しています");
            return;
        }*/

        fixPlayer(playerID);

        sendMessage(playerID, Translated.key("duel.join").args(player.getName(), String.valueOf(1) , String.valueOf(maxPlayerSize)).get(player));
        SoundEffects.OPERATED.play(player);

        player.getPlayer().teleport(this.spawn1.clone());

        ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, player);

        spectetorSet.applyShowMode(player);
        customPlayer.setArena(this);
        players.add(playerID);

        if (isFull()) {
            startMinigame();
        }
    }

    public void removePlayer(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);

        customPlayer.setArena(null);
        player.setGameMode(GameMode.ADVENTURE);
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

        if (players.size() <= 1) {
            setKit(null);
        }

        if (state == ArenaState.COUNTING_DOWN) {
            if (this.startTask != null) {
                this.startTask.cancel();
                state = ArenaState.WAITING_FOR_PLAYERS;
                players.forEach(s -> {
                    fixPlayer(s);
                    ExecutableItemType.DUEL_LEAVE_ITEM.setItem(8, s);

                });
                sendMessage("duel.cancel");
            }
        }

        Uhcffa.getInstance().setLobbyItem(player);
        player.teleport(Uhcffa.getInstance().config().getLobby());

        spectetorSet.applyShowMode(player);

        players.remove(playerID);
        kills.remove(playerID);
    }

    public void endGame() {
        state = ArenaState.GAME_END;

        removeDropItem();

        String spectator = this.spectators.isEmpty() ? null : this.spectators.get(0);
        String winner = (spectator != null)
                ? (this.APlayers.contains(spectator) ? (this.BPlayers.isEmpty() ? spectator : this.BPlayers.get(0))
                : (this.APlayers.isEmpty() ? spectator : this.APlayers.get(0)))
                : this.players.get(0);

        String message = ColorUtils.translateAlternateColorCodes('&',
                "&a"
                        + PlayerConverter.getName(winner) +
                        " &eWinner! &r- &c" +
                        PlayerConverter.getName(spectator));


        sendTitle(winner, "&6VICTORY", PlayerConverter.getName(winner) + " won the game");



        List<String> targetList = new LinkedList<>(this.players);
        for (String playerID : targetList) {
            invs.put(playerID, PlayerConverter.getPlayer(playerID).getInventory().getContents());
            armors.put(playerID, PlayerConverter.getPlayer(playerID).getEquipment().getArmorContents());
            String enemyName = PlayerConverter.getName(getEnemy(playerID));
            sendMessage(playerID ,ColorUtils.translateAlternateColorCodes('&',"&7&m&l============================================"));
            //ChatUtils.sendCenteredMessage(playerID, message);
            PlayerConverter.getPlayer(playerID).spigot().sendMessage(new ComponentBuilder(TextUtil.colorize( "&eClick to view " + enemyName + "'s Inventory"))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + enemyName))
                    .create());
            sendMessage(playerID ,ColorUtils.translateAlternateColorCodes('&',"&7&m&l============================================"));
        }
        stopMinigame();
    }


    public void stopMinigame() {
        if(this.startTask != null) this.startTask.cancel();
        if(this.runTask != null) this.runTask.cancel();

        this.state = ArenaState.GAME_END;
        stopTask = new BukkitRunnable() {
            int i = 10; // end time
            @Override
            public void run() {
                if(i == 0) {
                    this.cancel();
                    sendTitle("&cGAME OVER", "");
                    nextJoinGame();
                    destroyGame();
                    reset();
                    stopTask = null;
                    return;
                }
                elapsedTime++;
                i--;
            }
        }.runTaskTimer(Uhcffa.getInstance(), 0L, 20L);
    }

    public void nextJoinGame() {
        for (String playerID : players) {
            nextJoinGame(playerID);
        }
    }

    public void nextJoinGame(String playerID) {
        players.remove(playerID);
        Uhcffa.getInstance().getArenaManager().joinMatch(playerID, kit);
    }

    public void reset() {
        this.replaceBlock();
        this.removeDropItem();
        //this.arenaState = ArenaState.R;
        this.setKit(null);
        this.maxPlayerSize = 2;
        this.players.clear();
        this.APlayers.clear();
        this.BPlayers.clear();
        this.placed.clear();
        this.bucketPlaced.clear();
        this.droppedItem.clear();
        this.kills.clear();
        this.spectators.clear();
        this.state = ArenaState.WAITING_FOR_PLAYERS;
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

    public void destroyGame() {
        /*this.locations = null;*/
    }

    public void invSee(String playerID, String targetPlayerID) {
        Player player = PlayerConverter.getPlayer(playerID);

        ItemStack[] armors = this.armors.get(targetPlayerID);
        ItemStack[] contents = this.invs.get(targetPlayerID);

        InvSeeMenu.INVENTORY(contents, armors).open(player);
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayerSize() {
        return maxPlayerSize;
    }

    public List<Block> getPlaced() {
        return placed;
    }

    public List<Block> getBucketPlaced() {
        return bucketPlaced;
    }

    public double getMaxBuildY() {
        return maxBuildY;
    }

    public List<Item> getDroppedItem() {
        return droppedItem;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getEnemy(String playerID) {
        if (this.APlayers.contains(playerID)) {
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

    public ArenaState getState() {
        return state;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void playSound(Sound sound, float pitch, float volume) {
        for(String p : players) {
            final Player player = PlayerConverter.getPlayer(p);
            if(p != null)
                player.getPlayer().playSound(player.getLocation(), sound, pitch, volume);
        }
    }

    public void sendTitle(String title, String subTitle) {
        for(String p : players) {
            if(p != null)
                sendTitle(p, title, subTitle);
        }
    }

    public void sendTitle(String playerID ,String title, String subTitle) {
        final Player player = PlayerConverter.getPlayer(playerID);
        title = ColorUtils.translateAlternateColorCodes('&', title);
        subTitle = ColorUtils.translateAlternateColorCodes('&', subTitle);
        Titles.sendTitle(player, 5, 20, 5, title, subTitle);
    }

    public void sendMessage(String playerID, String message) {
            final Player player = PlayerConverter.getPlayer(playerID);
            player.sendMessage(ColorUtils.translateAlternateColorCodes('&', message));
    }

    public void sendMessage(String key) {
        for(String p : players) Uhcffa.getCustomPlayer(p).sendTranslated(key);
    }
}
