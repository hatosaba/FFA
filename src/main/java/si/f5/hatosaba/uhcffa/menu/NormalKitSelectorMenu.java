package si.f5.hatosaba.uhcffa.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.ItemStackBuilder;

import static org.bukkit.ChatColor.*;

public class NormalKitSelectorMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("normal")
            .manager(Uhcffa.instance().getManager())
            .provider(new NormalKitSelectorMenu())
            .size(3, 9)
            .title(DARK_GRAY + "Normal Kit選択メニュー")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        contents.set(1, 3, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_SWORD)
                        .setDisplayName(GREEN + "Standard Kit")
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {

                    kitManager.selectToKit(player, kitManager.getKit("standard"), false);
                    player.closeInventory();
                }));

        /*contents.set(1, 5, ClickableItem.of(
                ItemStackBuilder.builder(Material.BOW)
                        .setDisplayName(GREEN + "Archer Kit")
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("archer"), false);

                    player.closeInventory();
                }));*/
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
