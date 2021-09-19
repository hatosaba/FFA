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

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class ExtraKitSelectorMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("extra")
            .manager(Uhcffa.instance().getManager())
            .provider(new ExtraKitSelectorMenu())
            .size(3, 9)
            .title(DARK_GRAY + "Extra Kit選択メニュー")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        contents.set(1, 2, ClickableItem.of(
                ItemStackBuilder.builder(Material.IRON_SWORD)
                        .setDisplayName(GREEN + "Anduril Kit")
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("anduril"), false);
                    player.closeInventory();
                }));

        contents.set(1, 3, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_SWORD)
                        .setDisplayName(YELLOW + "Excalibur Kit")
                        .setLore(Arrays.asList(
                                RED + "Explosive (!)",
                                BLUE + "Chaos"
                        ))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("excalibur"), false);
                    player.closeInventory();
                }));

        contents.set(1, 4, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_AXE)
                        .setDisplayName(GOLD + "Axe of Perun Kit")
                        .build(),
                e -> {

                    kitManager.selectToKit(player, kitManager.getKit("perun"), false);
                    player.closeInventory();
                }));

        contents.set(1, 5, ClickableItem.of(
                ItemStackBuilder.builder(Material.BOW)
                        .setDisplayName(RED + "Artemis's Bow Kit")
                        .setLore(Arrays.asList(
                                BLUE + "Hunting"
                        ))
                        .build(),
                e -> {
//                    e
                }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}