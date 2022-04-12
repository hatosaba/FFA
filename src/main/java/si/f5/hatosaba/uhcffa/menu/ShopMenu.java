package si.f5.hatosaba.uhcffa.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.menu.cosmetics.MyCosmeticsMenu;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.Translated;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class ShopMenu implements InventoryProvider {

    private final Player player;

    public ShopMenu(Player player) {
        this.player = player;
    }

    public static final SmartInventory INVENTORY(Player player) {
        return SmartInventory.builder()
                .id("shop")
                .manager(Uhcffa.getInstance().getManager())
                .provider(new ShopMenu(player))
                .size(3, 9)
                .title(Translated.key("gui.cosmetics").get(player))
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
/*        contents.set(1, 2, ClickableItem.of(
                ItemStackBuilder.builder(Material.ANVIL)
                        .setDisplayName(GREEN + "FFA Kit編集")
                        .setLore(Arrays.asList(
                                GRAY + "KITに合わせたインベントリレイアウトの変更",
                                "",
                                YELLOW + "クリックして編集"
                        ))
                        .build(),
                e -> EditKitSelectorMenu.INVENTORY.open(player)));*/

        contents.set(1, 2, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name(Translated.key("gui.cosmetics").get(player))
                        .lore(Arrays.asList(
                                Translated.key("gui.cosmetics.1").get(player),
                                Translated.key("gui.cosmetics.2").get(player),
                                "",
                                Translated.key("gui.cosmetics.click").get(player)
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> MyCosmeticsMenu.INVENTORY.open(player)));

        contents.set(1, 6, ClickableItem.of(
                ItemBuilder.of(Material.EMERALD)
                        .name(Translated.key("gui.cosmetics").get(player))
                        .lore(Arrays.asList(
                                Translated.key("gui.vote.1").get(player),
                                Translated.key("gui.vote.2").get(player)
                        ))
                        .build(),
                e -> {}));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
