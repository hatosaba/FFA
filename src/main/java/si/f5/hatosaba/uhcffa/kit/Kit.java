package si.f5.hatosaba.uhcffa.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.cosmetics.block.Blocks;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.Convertor;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Kit {

    private ItemStack icon;
    private final String name;
    private ItemStack[] armor;
    private ItemStack[] items;

    public Kit(Yaml yaml) {

        this.name = yaml.name;
        this.icon = new ItemStack(Material.valueOf(yaml.getString("Kit.Icon")));

        //セクションが存在しなければ戻る
        if(yaml.isConfigurationSection("Inventory")) {
            try {
                this.armor = Convertor.itemFromBase64(yaml.getString("Inventory.armor"));
                this.items = Convertor.itemFromBase64(yaml.getString("Inventory.items"));
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }

    public Kit(String kitName, ItemStack[] armor, ItemStack[] contents) {
        this.name = kitName;
        this.armor = armor;
        this.items = contents;
    }

    public void saveKit(Player player) {
        this.armor = player.getInventory().getArmorContents();
        this.items = player.getInventory().getContents();
        player.sendMessage("キットを設定しました");
    }

    public void apply(Player player) {

        player.setHealth(player.getMaxHealth());
        User user  = UserSet.getInstnace().getUser(player);

        if(armor != null)
            player.getInventory().setArmorContents(armor);
        if(items != null)
            player.getInventory().setContents(items);

        /*CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(PlayerConverter.getID(player));

        final CustomPlayer.Preset preset = customPlayer.getPreset();
        if (preset == null) {
            final PlayerInventory inv = player.getInventory();
            ItemStack[] armor = new ItemStack[4];

            armor[0] = ItemBuilder.of(Material.IRON_BOOTS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
            armor[1] = ItemBuilder.of(Material.IRON_LEGGINGS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
            armor[2] = ItemBuilder.of(Material.DIAMOND_CHESTPLATE).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
            armor[3] = ItemBuilder.of(Material.DIAMOND_HELMET).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();

            inv.setArmorContents(armor);

            inv.setItem(0, ItemBuilder.of(Material.DIAMOND_SWORD).build());
            inv.setItem(1, ItemBuilder.of(Material.FISHING_ROD).build());
            inv.setItem(2, ItemBuilder.of(Material.BOW).build());
            inv.setItem(3, ItemBuilder.of(Material.ARROW).amount(8).build());
            ExecutableItemType.LIGHT_APPLE.setItem(4, player);
            inv.setItem(8, ItemBuilder.of(Material.COBBLESTONE).amount(64).build());
            ExecutableItemType.FFA_SHOP_ITEM.setItem(5,player);
            ExecutableItemType.RESPAWN_ITEM.setItem(6,player);

            customPlayer.setPreset();
        } else {
            preset.applyContent();
        }*/

        for(int i = 0; i< player.getInventory().getSize()-1; ++i) {
            ItemStack item = player.getInventory().getItem(i);
            if(item == null) continue;
            if(item.getType().equals(Material.COBBLESTONE)) {
                int finalI = i;
                Blocks.BLOCKS.stream().filter(kit1 -> kit1.item == user.blockItem).forEach(kit1 -> {
                    ItemStack block = kit1.item.clone();
                    if (block.hasItemMeta()) {
                        ItemMeta im = block.getItemMeta();
                        if (im.hasLore())
                            im.setLore(new ArrayList<>());
                        if (im.hasDisplayName())
                            im.setDisplayName("");
                        block.setItemMeta(im);
                    }
                    block.setAmount(64);
                    player.getInventory().setItem(finalI, block);
                });
            }
        }

/*        ItemStack[] armor = new ItemStack[4];
        armor[0] = ItemBuilder.of(Material.IRON_BOOTS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
        armor[1] = ItemBuilder.of(Material.IRON_LEGGINGS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
        armor[2] = ItemBuilder.of(Material.DIAMOND_CHESTPLATE).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
        armor[3] = ItemBuilder.of(Material.DIAMOND_HELMET).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();

        player.getInventory().setArmorContents(armor);

        final PlayerInventory inv = player.getInventory();

        inv.setItem(0, ItemBuilder.of(Material.DIAMOND_SWORD).build());
        inv.setItem(1, ItemBuilder.of(Material.FISHING_ROD).build());
        inv.setItem(2, ItemBuilder.of(Material.BOW).build());
        inv.setItem(3, ItemBuilder.of(Material.ARROW).amount(8).build());
        inv.setItem(4, ExecutableItemType.LIGHT_APPLE.getItem());
        inv.setItem(8, ItemBuilder.of(Material.COBBLESTONE).amount(64).build());

        for(int i = 0; i< player.getInventory().getSize()-1; ++i) {
            ItemStack item = player.getInventory().getItem(i);
            if(item == null) continue;
            if(item.getType().equals(Material.COBBLESTONE)) {
                int finalI = i;
                Blocks.BLOCKS.stream().filter(kit1 -> kit1.item == user.blockItem).forEach(kit1 -> {
                    ItemStack block = kit1.item.clone();
                    if (block.hasItemMeta()) {
                        ItemMeta im = block.getItemMeta();
                        if (im.hasLore())
                            im.setLore(new ArrayList<>());
                        if (im.hasDisplayName())
                            im.setDisplayName("");
                        block.setItemMeta(im);
                    }
                    block.setAmount(64);
                    player.getInventory().setItem(finalI, block);
                });
            }
        }*/
    }

    public String getName() {
        return name;
    }

    public void save() {
        Yaml yaml = KitManager.getInstance().makeYaml(name);

        if(armor != null)
            yaml.set("Inventory.armor", Convertor.itemToBase64(armor));
        if(items != null)
            yaml.set("Inventory.items", Convertor.itemToBase64(items));

        //セーブする
        yaml.save();
    }
}
