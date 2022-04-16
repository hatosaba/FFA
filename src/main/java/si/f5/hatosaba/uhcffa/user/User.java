package si.f5.hatosaba.uhcffa.user;

import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.cosmetics.block.Blocks;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.utils.Convertor;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

public class User {

    private Uhcffa uhcffa = Uhcffa.getInstance();
    //UUID
    public final UUID uuid;

    private final Map<String, Map<String, ItemStack[]>> kit = new HashMap<>();

    public ItemStack blockItem;

    public final PurchasedKit blocks;

    private ItemStack[] items;

    public final PurchasedEffect purchasedEffect;

    public final PurchasedTrail purchasedTrail;

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

    public void saveKit(Kit kit, ItemStack[] hotbar, ItemStack[] items) {
        ItemStack[] contents = new ItemStack[36];
        int i;
        for (i = 0; i < 9; i++) {
            contents[i] = hotbar[i];
        }
        for (int j = 0; j < 27; j++) {
            contents[i + j] = items[j];
        }
        Map<String, ItemStack[]> test = new HashMap<>();
        test.put("items", contents);
        test.put("armor", kit.getArmor());
        this.kit.put(kit.getName(), test);
    }

    public void resetKit(Kit kit) {
        Map<String, ItemStack[]> contents = new HashMap<>();
        contents.put("armor", kit.getArmor());
        contents.put("items", kit.getItems());
        this.kit.put(kit.getName(), contents);
    }

    public void apply(Kit kit) {
        asBukkitPlayer().getInventory().clear();
        asBukkitPlayer().setHealth(asBukkitPlayer().getMaxHealth());
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(asBukkitPlayer()));

 /*       final CustomPlayer.Preset preset = customPlayer.getPreset();
        if (preset == null) {
            ItemStack[] armor = new ItemStack[4];
            armor[0] = ItemBuilder.of(Material.IRON_BOOTS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
            armor[1] = ItemBuilder.of(Material.IRON_LEGGINGS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
            armor[2] = ItemBuilder.of(Material.DIAMOND_CHESTPLATE).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
            armor[3] = ItemBuilder.of(Material.DIAMOND_HELMET).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();

            asBukkitPlayer().getInventory().setArmorContents(armor);

            final PlayerInventory inv = asBukkitPlayer().getInventory();

            inv.setItem(0, ItemBuilder.of(Material.DIAMOND_SWORD).build());
            inv.setItem(1, ItemBuilder.of(Material.FISHING_ROD).build());
            inv.setItem(2, ItemBuilder.of(Material.BOW).build());
            inv.setItem(3, ItemBuilder.of(Material.ARROW).amount(8).build());
            ExecutableItemType.LIGHT_APPLE.setItem(4, asBukkitPlayer());
            inv.setItem(8, ItemBuilder.of(Material.COBBLESTONE).amount(64).build());
            ExecutableItemType.FFA_SHOP_ITEM.setItem(5, asBukkitPlayer());
            ExecutableItemType.RESPAWN_ITEM.setItem(6, asBukkitPlayer());


            customPlayer.setPreset();
        } else {
            preset.applyContent();
        }*/

/*        for(int i = 0; i<asBukkitPlayer().getInventory().getSize()-1; ++i) {
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
        }*/

        ItemStack[] armors = this.kit.get(kit.getName()).get("armor");
        ItemStack[] items = this.kit.get(kit.getName()).get("items");

        if(armors != null)
            asBukkitPlayer().getInventory().setArmorContents(armors);
        if(items != null)
            asBukkitPlayer().getInventory().setContents(items);

        /*final PlayerInventory inv = asBukkitPlayer().getInventory();

        inv.setItem(0, ItemBuilder.of(Material.DIAMOND_SWORD).build());
        inv.setItem(1, ItemBuilder.of(Material.FISHING_ROD).build());
        inv.setItem(2, ItemBuilder.of(Material.BOW).build());
        inv.setItem(3, ItemBuilder.of(Material.ARROW).amount(8).build());
        inv.setItem(4, ExecutableItemType.LIGHT_APPLE.getItem());
        inv.setItem(8, ItemBuilder.of(Material.COBBLESTONE).amount(64).build());*/

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

    public ItemStack[] getKit(Kit kit) {
        return this.kit.get(kit.getName()).get("items");
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

