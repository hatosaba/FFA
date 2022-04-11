package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class HermesBoots extends ExecutableItem {

    public HermesBoots(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.LEATHER_BOOTS)
                        .name("&eHermes' Boots")
                        .lore("&7Provides the wearer with 10% increase walk speed!")
                        .build()
        );
    }
}
