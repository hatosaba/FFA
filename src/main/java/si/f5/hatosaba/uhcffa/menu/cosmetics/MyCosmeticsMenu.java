package si.f5.hatosaba.uhcffa.menu.cosmetics;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.menu.ShopMenu;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class MyCosmeticsMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("cosmetics")
            .manager(Uhcffa.getInstance().getManager())
            .provider(new MyCosmeticsMenu())
            .size(6, 9)
            .title(GREEN + "化粧品")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 1, ClickableItem.of(
                ItemBuilder.of(Material.ARROW)
                        .name(GREEN + "ProjectileTrail")
                        .lore(Arrays.asList(
                                GRAY + "投射物のパーティクルの",
                                GRAY + "軌跡効果を変更します。",
                                "",
                                YELLOW + "クリックして閲覧"
                        ))
                        .build(),
                e -> TrailMenu.INVENTORY.open(player)));

        contents.set(1, 3, ClickableItem.of(
                ItemBuilder.of(Material.IRON_SWORD)
                        .name(GREEN + "Kill Effect")
                        .lore(Arrays.asList(
                                GRAY + "敵を倒した時に発動する",
                                GRAY + "様々なエフェクトを選択可能。",
                                "",
                                YELLOW + "クリックして閲覧"
                        ))
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .build(),
                e -> KillEffectMenu.INVENTORY.open(player)));

        contents.set(1, 5, ClickableItem.of(
                ItemBuilder.of(Material.COBBLESTONE)
                        .name(GREEN + "Block Skin")
                        .lore(Arrays.asList(
                                GRAY + "ブロックのスキンを変更します。",
                                "",
                                YELLOW + "クリックして閲覧"
                        ))
                        .build(),
                e -> CosmeticsBlockMenu.INVENTORY.open(player)));

        contents.set(1, 7, ClickableItem.of(
                ItemBuilder.of(Material.SIGN)
                        .name(GREEN + "Kill Message")
                        .lore(Arrays.asList(
                                GRAY + "Kill Messageパッケージを選択すると、",
                                GRAY + "プレイヤーを殺害したときのチャットメッセージ",
                                GRAY + "を置き換えることができます。",
                                "",
                                YELLOW + "クリックして閲覧"
                        ))
                        .build(),
                e -> {}));

        contents.set(3, 2, ClickableItem.of(
                ItemBuilder.of(Material.SKULL_ITEM)
                        .name(GREEN + "Death Cries")
                        .lore(Arrays.asList(
                                GRAY + "このデス・クライを使って、",
                                GRAY + "死ぬたびに涙がどれだけ塩辛いかを",
                                GRAY + "他の人に伝えましょう。",
                                "",
                                YELLOW + "クリックして閲覧"
                        ))
                        .build(),
                e -> {}));

        contents.set(5, 3, ClickableItem.of(
                ItemBuilder.of(Material.ARROW)
                        .name(GRAY + "戻る")
                        .lore(Arrays.asList(
                                GRAY + "FFA SHOPへ"
                        ))
                        .build(),
                e -> ShopMenu.INVENTORY(player).open(player)));

        contents.set(5, 4, ClickableItem.of(
                ItemBuilder.of(Material.EMERALD)
                        .name(GRAY + "統計コイン:" + YELLOW + UserSet.getInstnace().getUser(player).coins())
                        .lore(Arrays.asList(
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
