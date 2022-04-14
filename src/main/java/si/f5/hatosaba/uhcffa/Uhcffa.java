package si.f5.hatosaba.uhcffa;

import com.earth2me.essentials.Essentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lielamar.lielsutils.bukkit.scoreboard.ScoreboardManager;
import fr.minuskube.inv.InventoryManager;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.commands.InventoryCommand;
import si.f5.hatosaba.uhcffa.commands.admin.AdminCommand;
import si.f5.hatosaba.uhcffa.commands.duel.AcceptCommand;
import si.f5.hatosaba.uhcffa.commands.duel.DuelCommand;
import si.f5.hatosaba.uhcffa.commands.ffa.FFACommand;
import si.f5.hatosaba.uhcffa.config.MainConfig;
import si.f5.hatosaba.uhcffa.cosmetics.manager.KillManager;
import si.f5.hatosaba.uhcffa.cosmetics.manager.TrailManager;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.listeners.*;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.placeholders.UhcFFAPlaceholder;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.schedule.task.ItemTask;
import si.f5.hatosaba.uhcffa.schedule.task.AsyncTask;
import si.f5.hatosaba.uhcffa.schedule.task.UpdateSidebar;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableManager;
import si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa.Daredevil;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class Uhcffa extends JavaPlugin {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static Uhcffa instance;
    private InventoryManager manager;
    private Essentials essentials;
    private ArenaManager arenaManager;
    private final ArrayList<AsyncTask> activeTasks = new ArrayList<>(2);
    private MainConfig config;
    private final ExecutableManager executableManager = new ExecutableManager();

    private ScoreboardManager scoreboardManager;

    private static final ConcurrentHashMap<String, CustomPlayer> customPlayerMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        config = new MainConfig();
        UserSet.load();
        KitManager.load();
        KillManager.load();
        TrailManager.load();

        arenaManager = new ArenaManager();

        executableManager.init();

        this.manager = new InventoryManager(this);
        this.manager.init();

        this.scoreboardManager = new ScoreboardManager(this, "HATOSABA FFA");

        Sync.define(() -> Bukkit.getOnlinePlayers().forEach(player -> {
            SpectetorSet.getInstance().onPlayerJoin(player);
            UserSet.getInstnace().registerUser(player);
            player.teleport(config().getLobby());
            player.setMaxHealth(40);
            player.setHealth(player.getMaxHealth());
            player.sendMessage(new String[]{
                    "",
                    ChatColor.RED +"アップデートのためシステムを再起動しました。",
                    ChatColor.GREEN + "こちらのリンクから",
                    ChatColor.YELLOW + "https://forms.gle/ADnT8SHDBDzm6fTB7",
                    ChatColor.GREEN + "要望・提案・バグなどを募集しています。",
                    "",
            });
        })).executeLater(TimeUnit.SECONDS.toSeconds(2));

        registerEventListeners(
                new PlayerListener(),
                new PlayerJoinQuitListener(),
                new EntitySpawnListener(),
                new WeatherChangeListener(),
                new PlayerPlaceBlock(),
                new PlayerDeathListener(),
                new PlayerRespawnListener(),
                new VoteListener(),
                new CombatTagger(),
                new ItemListener(),
                new ArenaListener()
        );

        startTasks(
                new ItemTask(),
                new UpdateSidebar()
        );

        /*registerCommandHandler(
                new UhcffaCommands(),
                new KitSaveCommand(),
                new AutomaticEventCommand(),
                new DuelCommand()
        );*/

        if (essentials == null) {
            Essentials essential = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
            if (essential != null) {
                if (essential.isEnabled()) {
                    essentials = essential;
                }
            }
        }

        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            // Register placeholder extension
            new UhcFFAPlaceholder(this).register();
            getLogger().info("Registered placeholders, so you can use them!");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                final String playerID = PlayerConverter.getID(player);
                final CustomPlayer customPlayer = new CustomPlayer(playerID);
                customPlayerMap.put(playerID, customPlayer);
            }
        });

        initiate();

        registerCommands();

    }

    private void registerCommands() {
        new DuelCommand(this).registerCommand(this);
        new AcceptCommand(this).registerCommand(this);
        new AdminCommand(this).registerCommand(this);
        new FFACommand(this).registerCommand(this);
        new InventoryCommand(this).registerCommand(this);
    }


    public void initiate() {
        Bukkit.getOnlinePlayers().forEach(pl -> scoreboardManager.injectPlayer(pl));
    }

    @Override
    public void onDisable() {
        UserSet.getInstnace().getOnlineUsers().forEach(user -> {
            Player player = user.asBukkitPlayer();
            SpectetorSet.getInstance().onPlayerQuit(player);
            resetInv(player);
            if(KitManager.getInstance().isSelected(player))
                KitManager.getInstance().getSelectedPlayer().remove(player);
        });

        this.scoreboardManager = null;

        // Plugin shutdown logic
        KitManager.getInstance().saveAll();
        UserSet.getInstnace().saveAll();
        config().saveAll();
        cancelTasks();

        for (NPC npc : PlayerJoinQuitListener.combatPlayers.values()){
            npc.destroy();
        }

        for (Horse horse : Daredevil.horses) {
            horse.remove();
        }

        for (Arena arena : arenaManager.getArenas().values()) {
            arena.endGame();
        }

        PlayerPlaceBlock.removeBlocks();
    }

    public static Uhcffa getInstance() {
        return instance;
    }

    public InventoryManager getManager() {
        return manager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public Gson getGson() {
        return gson;
    }

    public MainConfig config(){
        return config;
    }



    private void registerEventListeners(Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

    private void startTasks(AsyncTask... tasks){
        for(AsyncTask task : tasks){
            activeTasks.add(task);
            task.start();
        }
    }

    public ExecutableManager getExecutableManager() {
        return executableManager;
    }

    private void cancelTasks(){
        for(AsyncTask task : activeTasks) task.cancel();
        activeTasks.clear();
    }

    public static void putCustomPlayer(final String playerID, final CustomPlayer customPlayer) {
        Bukkit.getLogger().info("Inserting data for " + PlayerConverter.getName(playerID));
        customPlayerMap.put(playerID, customPlayer);
    }

    public static CustomPlayer getCustomPlayer(final String playerID) {
        CustomPlayer customPlayer = customPlayerMap.get(playerID);
        if (customPlayer == null && PlayerConverter.getPlayer(playerID) != null) {
            customPlayer = new CustomPlayer(playerID);
            putCustomPlayer(playerID, customPlayer);
        }
        return customPlayer;
    }

    public static CustomPlayer getCustomPlayer(Player player) {
        return getCustomPlayer(PlayerConverter.getID(player));
    }

    public static CustomPlayer getCustomPlayer(CommandSender sender) {
        return getCustomPlayer(PlayerConverter.getID((Player) sender));
    }

    public static void removePlayerData(final String playerID) {
        customPlayerMap.remove(playerID);
    }


    private void resetInv(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setAllowFlight(true);
        player.setFlying(true);
        setLobbyItem(player);
        SpectetorSet.getInstance().applyHideMode(player);
    }

    public void setLobbyItem(Player player) {
        player.getInventory().clear();
        ExecutableItemType.PLAY_ITEM.setItem(0, player);
        ExecutableItemType.SPECTATOR.setItem(2, player);
        ExecutableItemType.SHOP_ITEM.setItem(4, player);
        ExecutableItemType.JOIN_ITEM.setItem(6, player);
    }

    public void setSpectatorItem(Player player) {
        player.getInventory().clear();
        ExecutableItemType.JOIN_ITEM.setItem(0, player);
        ExecutableItemType.SHOP_ITEM.setItem(4, player);
        ExecutableItemType.LEAVE_ITEM.setItem(8, player);
    }

    public ScoreboardManager getScoreboardManager() { return this.scoreboardManager; }

    public Essentials getEssentials() {
        return essentials;
    }

    public boolean isPlaceholderAPI() {
        return true;
    }
}
