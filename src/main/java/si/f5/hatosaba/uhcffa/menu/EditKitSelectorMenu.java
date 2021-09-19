package si.f5.hatosaba.uhcffa.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.ItemStackBuilder;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class EditKitSelectorMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("edit")
            .manager(Uhcffa.instance().getManager())
            .provider(new EditKitSelectorMenu())
            .size(3, 9)
            .title(GREEN + "ここからキットが編集できます")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_SWORD)
                        .setDisplayName(GREEN + "Standard Kit")
                        .setLore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("standard"), true);
                    player.closeInventory();
                }));

        contents.set(0, 1, ClickableItem.of(
                ItemStackBuilder.builder(Material.BOW)
                        .setDisplayName(GREEN + "Archer Kit")
                        .setLore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .build(),
                e -> {
                    /*kitManager.selectToKit(player, kitManager.getKit("archer"), true);
                    player.closeInventory();*/
                }));

        contents.set(0, 2, ClickableItem.of(
                ItemStackBuilder.builder(Material.IRON_SWORD)
                        .setDisplayName(GREEN + "Anduril Kit")
                        .setLore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("anduril"), true);
                    player.closeInventory();
                }));

        contents.set(0, 3, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_SWORD)
                        .setDisplayName(YELLOW + "Excalibur Kit")
                        .setLore(Arrays.asList(
                                RED + "Explosive (!)",
                                BLUE + "Chaos",
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("excalibur"), true);
                    player.closeInventory();
                }));

        contents.set(0, 4, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_AXE)
                        .setDisplayName(GOLD + "Axe of Perun Kit")
                        .setLore(Arrays.asList(
                                "",
                                YELLOW + "クリックして編集する"
                        ))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> {
                    kitManager.selectToKit(player, kitManager.getKit("perun"), true);
                    player.closeInventory();
                }));

        contents.set(0, 5, ClickableItem.of(
                ItemStackBuilder.builder(Material.BOW)
                        .setDisplayName(RED + "Artemis's Bow Kit")
                        .setLore(Arrays.asList(
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