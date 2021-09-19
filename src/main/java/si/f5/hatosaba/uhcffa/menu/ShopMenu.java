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
import si.f5.hatosaba.uhcffa.utils.ItemStackBuilder;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class ShopMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("shop")
            .manager(Uhcffa.instance().getManager())
            .provider(new ShopMenu())
            .size(3, 9)
            .title(GREEN + "ショップ")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 2, ClickableItem.of(
                ItemStackBuilder.builder(Material.ANVIL)
                        .setDisplayName(GREEN + "FFA Kit編集")
                        .setLore(Arrays.asList(
                                GRAY + "KITに合わせたインベントリレイアウトの変更",
                                "",
                                YELLOW + "クリックして編集"
                        ))
                        .build(),
                e -> EditKitSelectorMenu.INVENTORY.open(player)));

        contents.set(1, 4, ClickableItem.of(
                ItemStackBuilder.builder(Material.DIAMOND_SWORD)
                        .setDisplayName(GREEN + "化粧品")
                        .setLore(Arrays.asList(
                                GRAY + "ゲーム内で入手可能なFFA用化粧品を",
                                GRAY + "すべて閲覧し、装備することができます。",
                                "",
                                YELLOW + "クリックして化粧品を閲覧"
                        ))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> MyCosmeticsMenu.INVENTORY.open(player)));

        contents.set(1, 6, ClickableItem.of(
                ItemStackBuilder.builder(Material.EMERALD)
                        .setDisplayName(GRAY + "統計コイン:" + YELLOW + UserSet.getInstnace().getUser(player).coins())
                        .setLore(Arrays.asList(
                                GRAY + "投票してコインを多く受け取る",
                                GOLD + "https://hatosaba.f5.si/jms"
                        ))
                        .build(),
                e -> {}));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
