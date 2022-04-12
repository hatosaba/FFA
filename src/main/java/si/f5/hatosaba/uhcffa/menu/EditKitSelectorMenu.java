package si.f5.hatosaba.uhcffa.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class EditKitSelectorMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("edit")
            .manager(Uhcffa.getInstance().getManager())
            .provider(new EditKitSelectorMenu())
            .size(3, 9)
            .title(GREEN + "ここからキットが編集できます")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name(GREEN + "Standard Kit")
                        .lore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("standard"), true);
                    player.closeInventory();
                }));

        contents.set(0, 1, ClickableItem.of(
                ItemBuilder.of(Material.BOW)
                        .name(GREEN + "Archer Kit")
                        .lore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .build(),
                e -> {
                    /*kitManager.selectToKit(player, kitManager.getKit("archer"), true);
                    player.closeInventory();*/
                }));

        contents.set(0, 2, ClickableItem.of(
                ItemBuilder.of(Material.IRON_SWORD)
                        .name(GREEN + "Anduril Kit")
                        .lore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("anduril"), true);
                    player.closeInventory();
                }));

        contents.set(0, 3, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name(YELLOW + "Excalibur Kit")
                        .lore(Arrays.asList(
                                RED + "Explosive (!)",
                                BLUE + "Chaos",
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("excalibur"), true);
                    player.closeInventory();
                }));

        contents.set(0, 4, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_AXE)
                        .name(GOLD + "Axe of Perun Kit")
                        .lore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("perun"), true);
                    player.closeInventory();
                }));

        contents.set(0, 5, ClickableItem.of(
                ItemBuilder.of(Material.BOW)
                        .name(RED + "Artemis's Bow Kit")
                        .lore(Arrays.asList(
                                BLUE + "Hunting",
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .build(),
                e -> {
//                    kitManager.selectToKit(player, kitManager.getKit("artemis"), true);
//                    player.closeInventory();
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}