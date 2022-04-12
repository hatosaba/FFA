package si.f5.hatosaba.uhcffa.menu.cosmetics;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.cosmetics.block.Block;
import si.f5.hatosaba.uhcffa.cosmetics.block.Blocks;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.user.PurchasedKit;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class CosmeticsBlockMenu implements InventoryProvider {

    private final UserSet userSet = UserSet.getInstnace();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("blocks")
            .manager(Uhcffa.getInstance().getManager())
            .provider(new CosmeticsBlockMenu())
            .size(6, 9)
            .title(DARK_GRAY + "ブロック")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        User user = userSet.getUser(player);
        PurchasedKit purchasedKit = user.blocks;
        List<Block> games = Blocks.BLOCKS;
        ClickableItem[] items = new ClickableItem[games.size()];

        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.AIR)));

        for (int i = 0; i < items.length; i++) {
            Block block = games.get(i);

            items[i] = ClickableItem.of(
                    ItemBuilder.modify(block.item)
                            .lore(Arrays.asList(
                                    DARK_GRAY + "ブロック",
                                    purchasedKit.has(block) ? "" : GRAY + "コスト: "+ GOLD + block.value,
                                    purchasedKit.has(block) ? (user.blockItem.isSimilar(block.item) ?  GREEN + "選択済み" : YELLOW + "クリックして選択") : RED + "購入する"
                            ))
                            .build()
                    , e -> {
                        if(!user.blockItem.isSimilar(block.item)) {
                            if (purchasedKit.has(block)) {
                                user.blockItem = block.item;
                                SoundEffects.SUCCEEDED.play(player);
                            } else {
                                if (purchasedKit.canBuy(block)) {
                                    purchasedKit.buy(block);
                                    SoundEffects.SUCCEEDED.play(player);
                                    player.sendMessage(GREEN + "購入しました");
                                } else {
                                    SoundEffects.FAILED.play(player);
                                    player.sendMessage(RED + "coinが足りません");
                                }
                            }
                            INVENTORY.open(player, pagination.getPage());
                        }
                    });
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(21);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1).allowOverride(false));

        if(!pagination.isFirst()) {
            contents.set(5, 0, ClickableItem.of(
                    ItemBuilder.of(Material.ARROW)
                            .name(GREEN + "前のページへ")
                            .build(),
                    e -> INVENTORY.open(player, pagination.previous().getPage())));
        }

        if(!pagination.isLast()) {
            contents.set(5, 8, ClickableItem.of(
                    ItemBuilder.of(Material.ARROW)
                            .name(GREEN + "次のページへ")
                            .build(),
                    e -> INVENTORY.open(player, pagination.next().getPage())));
        }

        contents.set(5, 3, ClickableItem.of(
                ItemBuilder.of(Material.ARROW)
                        .name(GRAY + "戻る")
                        .lore(Arrays.asList(
                                GRAY + "化粧品へ"
                        ))
                        .build(),
                e -> MyCosmeticsMenu.INVENTORY.open(player)));

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
