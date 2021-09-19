package si.f5.hatosaba.uhcffa.kit;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.File;
import java.util.*;

public class KitManager {

    private static KitManager instance;

    public static void load(){
        instance = new KitManager();
    }

    public static KitManager getInstance(){
        return instance;
    }

    private final HashSet<Player> cooldownPlayers = new HashSet<>();

    private final Uhcffa plugin = Uhcffa.instance();

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    private final UserSet userSet = UserSet.getInstnace();

    //キットデータを保存するフォルダー
    public final File folder = new File(plugin.getDataFolder() + File.separator + "kits");

    private final Map<String, Kit> kits = new HashMap<>();

    private final HashMap<Player, Kit> selectedPlayer = new HashMap<>();

    private KitManager() {
        //フォルダーが存在しなければ作成する
        if(!folder.exists()) folder.mkdirs();
        //各コンフィグ毎に処理をする
        for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
            if(file.isFile()) {
                String fileName = file.getName();
                //拡張子を削除してキット名を取得する
                String gameName = fileName.substring(0, fileName.length() - 4);

                //キットを登録する
                registerKit(gameName);
            }
        }
    }

    public void registerKit(String kitName){
        File file = new File(folder, kitName + ".yml");

        //コンフィグが存在しなければ戻る
        if(!file.exists()) return;

        //コンフィグを取得する
        Yaml yaml = makeYaml(kitName);

        //コンフィグに基づきアスレを生成する
        Kit kit =  new Kit(yaml);
        kits.put(kitName, kit);

    }

    public void saveAll(){
        kits.values().forEach(Kit::save);
    }

    public void reload(){
        kits.values().forEach(Kit::save);
        kits.clear();
        load();
    }

    public void selectToKit(Player player, Kit kit, boolean isEdit) {
        User user = userSet.getUser(player);

        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setHealth(player.getMaxHealth());

        if(!isEdit) {
            if (user.isEditedKit(kit.getName())) {
                Sync.define(() -> user.apply(kit)).executeLater(1 * 20);
            } else {
                Sync.define(() -> kit.apply(player)).executeLater(1 * 20);
            }
        }
        selectedPlayer.put(player, kit);

        if(isEdit) {
            Sync.define(() -> kit.apply(player)).executeLater(1 * 20);
            player.teleport(Uhcffa.instance().config().getLocation());
            player.sendMessage("/kitsaveでキットを保存できます");
        } else {
            player.setAllowFlight(false);
//            ScoreboardBuilder statusBoard = user.scoreboardBuilder != null ? user.scoreboardBuilder : new ScoreboardBuilder(user);
//            statusBoard.loadScoreboard();
            Sync.define(() -> {
                player.teleport(Uhcffa.instance().config().getSpawnPoints().stream().skip((new Random()).nextInt(Uhcffa.instance().config().getSpawnPoints().size())).findFirst().get());
                spectetorSet.applyShowMode(player);
                tryGivingRespawnItemsTo(player);
            }).executeLater(2*20);
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(kit.getName() + "を選択しました");
    }

    private void tryGivingRespawnItemsTo(Player player) {
        tryGivingItemTo(player, Constants.RESPAWN_ITEM);
    }

    private void tryGivingItemTo(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();
        if (!inventory.contains(item)) inventory.addItem(item);
    }

    public Collection<Kit> getKits(){
        return kits.values();
    }

    public Kit getKit(String kitName){
        return kits.get(kitName);
    }

    public HashMap<Player, Kit> getSelectedPlayer() {
        return selectedPlayer;
    }

    public HashSet<Player> getCooldownPlayers() {
        return cooldownPlayers;
    }

    public boolean isSelected(Player player) {
        return selectedPlayer.containsKey(player);
    }

    public boolean containsKit(Kit kit){
        return containsKit(kit.getName());
    }

    public boolean containsKit(String kitName){
        return kits.containsKey(kitName);
    }

    public Yaml makeYaml(String kitName){
        return new Yaml(plugin, new File(folder, kitName + ".yml"), "kit.yml");
    }
}

