package si.f5.hatosaba.uhcffa.user;

import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.cosmetics.block.Blocks;
import si.f5.hatosaba.uhcffa.scoreboard.ScoreboardBuilder;
import si.f5.hatosaba.uhcffa.utils.Convertor;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

public class User {

    private Uhcffa uhcffa = Uhcffa.instance();
    //UUID
    public final UUID uuid;

    private final Map<String, Map<String, ItemStack[]>> kit = new HashMap<>();

    public ItemStack blockItem;

    public final PurchasedKit blocks;

    public final PurchasedEffect purchasedEffect;

    public final PurchasedTrail purchasedTrail;

    public ScoreboardBuilder scoreboardBuilder;

    public User(Yaml yaml){
        //ファイル名に基づきUUIDを生成し代入する
        this.uuid = UUID.fromString(yaml.name);

        this.blocks = new PurchasedKit(this, yaml);
        this.purchasedEffect = new PurchasedEffect(this, yaml);
        this.purchasedTrail = new PurchasedTrail(this, yaml);

        try {
            this.blockItem = Convertor.itemStackFromBase64(yaml.getString("blockItem"));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, null, e);
        }

        if(blockItem == null) this.blockItem = new ItemStack(Material.COBBLESTONE);
        //セクションが存在しなければ戻る
        if(yaml.isConfigurationSection("Inventory.kit") && yaml.isConfigurationSection("Inventory.kit")) {
            ConfigurationSection kitSection = yaml.getConfigurationSection("Inventory.kit");

            for(String kitName : kitSection.getKeys(false)){
                ConfigurationSection typeSection = kitSection.getConfigurationSection(kitName);
                Map<String, ItemStack[]> kit = new HashMap<>();
                for(String type : typeSection.getKeys(false)){
                    try {
                        kit.put(type, Convertor.itemFromBase64(typeSection.getString(type)));
                        this.kit.put(kitName, kit);
                    } catch (IOException ex) {
                        Bukkit.getLogger().log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public Optional<ScoreboardBuilder> scoreboardBuilder(){
        return Optional.of(scoreboardBuilder);
    }

    public void saveKit(Player player) {
        if(KitManager.getInstance().isSelected(player)) {
            Map<String, ItemStack[]> contents = new HashMap<>();
            contents.put("armor", player.getInventory().getArmorContents());
            contents.put("items", player.getInventory().getContents());
            this.kit.put(KitManager.getInstance().getSelectedPlayer().get(player).getName(), contents);
            player.sendMessage("キットをセーブしました");
        }else {
            player.sendMessage("キットを選択した状態でこのコマンドを実行してください");
        }
    }

    public void apply(Kit kit) {
        asBukkitPlayer().getInventory().clear();
        asBukkitPlayer().setHealth(asBukkitPlayer().getMaxHealth());
        ItemStack[] armors = this.kit.get(kit.getName()).get("armor");
        ItemStack[] items = this.kit.get(kit.getName()).get("items");

        if(armors != null)
            asBukkitPlayer().getInventory().setArmorContents(armors);
        if(items != null)
            asBukkitPlayer().getInventory().setContents(items);

        for(int i = 0; i<asBukkitPlayer().getInventory().getSize()-1; ++i) {
            ItemStack item = asBukkitPlayer().getInventory().getItem(i);
            if(item == null) continue;
            if(item.getType().equals(Material.COBBLESTONE)) {
                int finalI = i;
                Blocks.BLOCKS.stream().filter(kit1 -> kit1.item.isSimilar(blockItem)).forEach(kit1 -> {
                    ItemStack block = kit1.item.clone();
                    block.setAmount(64);
                    asBukkitPlayer().getInventory().setItem(finalI, block);
                });
            }
        }
    }

    public boolean isEditedKit(String kitName) {
        return this.kit.containsKey(kitName);
    }

    //指定数だけ所持コイン数を増やす
    public void depositCoins(BigDecimal coins){
        try {
            uhcffa.getEssentials().getUser(uuid).setMoney(uhcffa.getEssentials().getUser(uuid).getMoney().add(coins));
        } catch (final MaxMoneyException ex) {
            uhcffa.getLogger().log(Level.WARNING, "Invalid call to takeMoney, total balance can't be more than the max-money limit.", ex);
        }
    }

    //指定数だけ所持コイン数を減らす
    public void withdrawCoins(BigDecimal coins){
        try {
            uhcffa.getEssentials().getUser(uuid).setMoney(uhcffa.getEssentials().getUser(uuid).getMoney().subtract(coins));
        } catch (final MaxMoneyException ex) {
            uhcffa.getLogger().log(Level.WARNING, "Invalid call to takeMoney, total balance can't be more than the max-money limit.", ex);
        }
    }

    public BigDecimal coins() {
        return uhcffa.getEssentials().getUser(uuid).getMoney();
    }


    //このユーザーに対応したプレイヤーを取得する
    public Player asBukkitPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        Yaml yaml = UserSet.getInstnace().makeYaml(uuid);

        for(Map.Entry<String, Map<String, ItemStack[]>> kitEntry : kit.entrySet()){
            //kit名を取得する
            String kitName = kitEntry.getKey();

            for(Map.Entry<String, ItemStack[]> contentsEntry : kitEntry.getValue().entrySet()){
                //タイプを取得する
                String type = contentsEntry.getKey();

                yaml.set("Inventory.kit." + kitName + "." + type, Convertor.itemToBase64(contentsEntry.getValue()));
            }
        }
        yaml.set("blockItem", Convertor.itemStackToBase64(this.blockItem));

        purchasedTrail.save(yaml);
        purchasedEffect.save(yaml);
        blocks.save(yaml);
        //セーブする
        yaml.save();
    }
}

