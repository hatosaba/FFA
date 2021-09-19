package si.f5.hatosaba.uhcffa;

import com.earth2me.essentials.Essentials;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.minuskube.inv.InventoryManager;
import io.github.mrblobman.spigotcommandlib.CommandHandler;
import io.github.mrblobman.spigotcommandlib.registry.CommandLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.command.*;
import si.f5.hatosaba.uhcffa.config.MainConfig;
import si.f5.hatosaba.uhcffa.cosmetics.manager.KillManager;
import si.f5.hatosaba.uhcffa.cosmetics.manager.TrailManager;
import si.f5.hatosaba.uhcffa.hostedevents.automaticEvent.AutomaticEvent;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.kit.extra.Artemis;
import si.f5.hatosaba.uhcffa.kit.extra.AxeOfPerun;
import si.f5.hatosaba.uhcffa.kit.extra.Excalibur;
import si.f5.hatosaba.uhcffa.listeners.*;
import si.f5.hatosaba.uhcffa.placeholders.UhcFFAPlaceholder;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.schedule.task.AndurilTask;
import si.f5.hatosaba.uhcffa.schedule.task.AsyncTask;
import si.f5.hatosaba.uhcffa.schedule.task.UpdateHealth;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.UpdateChecker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public final class Uhcffa extends JavaPlugin {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static Uhcffa instance;
    private InventoryManager manager;
    private Essentials essentials;
    private CommandLib lib;
    private final ArrayList<AsyncTask> activeTasks = new ArrayList<>(2);
    private MainConfig config;
    private AutomaticEvent automaticEvent;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        config = new MainConfig();
        UserSet.load();
        KitManager.load();
        KillManager.load();
        TrailManager.load();

        new UpdateChecker().checkForUpdates();

        this.automaticEvent = this.automaticEvent == null ? new AutomaticEvent(this) : this.automaticEvent;
        this.automaticEvent.startEvent();

        this.manager = new InventoryManager(this);
        this.manager.init();

        Sync.define(() -> Bukkit.getOnlinePlayers().forEach(player -> {
            SpectetorSet.getInstance().onPlayerJoin(player);
            UserSet.getInstnace().registerUser(player);
            player.teleport(Uhcffa.instance().config().getLobby());
            player.setMaxHealth(40);
            player.setHealth(player.getMaxHealth());
            player.sendMessage(ChatColor.RED +"致命的なバグ修正のためシステムを再起動しました。もう一度キットを選択してから遊ぶようお願いします");
            Sync.define(() -> player.sendMessage(Constants.UPDATE_MESSAGE)).executeLater(5*20L);
        })).executeLater(TimeUnit.SECONDS.toSeconds(2));

        registerEventListeners(
                new PlayerPlaceBlock(),
                new PlayerDamageListener(),
                new FoodLevelChangeListener(),
                new BlockBreakListener(),
                new ProjectileHitListener(),
                new PlayerItemDamageListener(),
                new PlayerJoinListener(),
                new ItemListener(),
                new PlayerDeathListener(),
                new ItemDropListener(),
                new PlayerLeaveListener(),
                new PlayerRespawnListener(),
                new PlayerPickUpListener(),
                new VoteListener(),

                new Excalibur(),
                new AxeOfPerun(),
                new Artemis()
        );

        startTasks(
                new AndurilTask(),
                new UpdateHealth()
        );

        registerCommandHandler(
                new UhcffaCommands(),
                new KitSaveCommand(),
                new AutomaticEventCommand()
        );

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

        this.automaticEvent.stopEvent();

        // Plugin shutdown logic
        KitManager.getInstance().saveAll();
        UserSet.getInstnace().saveAll();
        config().saveAll();
        cancelTasks();
    }

    public static Uhcffa instance() {
        return instance;
    }

    public InventoryManager getManager() {
        return manager;
    }

    public AutomaticEvent getAutomaticEvent() {
        return automaticEvent;
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

    private void registerCommandHandler(CommandHandler... handlers) {
        this.lib = new CommandLib(this);
        for (CommandHandler handler : handlers)
            this.lib.registerCommandHandler(handler);
    }

    private void startTasks(AsyncTask... tasks){
        for(AsyncTask task : tasks){
            activeTasks.add(task);
            task.start();
        }
    }

    private void cancelTasks(){
        for(AsyncTask task : activeTasks) task.cancel();
        activeTasks.clear();
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
        tryGivingDefaultItemsTo(player);
        SpectetorSet.getInstance().applyHideMode(player);
    }

    private void tryGivingDefaultItemsTo(Player player) {
        tryGivingItemTo(player, Constants.KIT_SELECTOR, 0);
        tryGivingItemTo(player, Constants.SHOP, 4);
    }

    private void tryGivingItemTo(Player player, ItemStack item, int slot) {
        Inventory inventory = player.getInventory();
        if (!inventory.contains(item)) inventory.setItem(slot, item);
    }

    public Essentials getEssentials() {
        return essentials;
    }
}
