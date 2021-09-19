package si.f5.hatosaba.uhcffa.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import si.f5.hatosaba.uhcffa.cosmetics.block.Blocks;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.Convertor;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Kit {

    private final String name;
    private ItemStack[] armor;
    private ItemStack[] items;

    public Kit(Yaml yaml) {

        this.name = yaml.name;

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
