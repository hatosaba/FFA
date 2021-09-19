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
import si.f5.hatosaba.uhcffa.cosmetics.killeffect.Effect;
import si.f5.hatosaba.uhcffa.cosmetics.killeffect.Effects;
import si.f5.hatosaba.uhcffa.cosmetics.trail.Trail;
import si.f5.hatosaba.uhcffa.cosmetics.trail.Trails;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.user.PurchasedEffect;
import si.f5.hatosaba.uhcffa.user.PurchasedTrail;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ItemStackBuilder;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class TrailMenu implements InventoryProvider {

    private final UserSet userSet = UserSet.getInstnace();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("trail")
            .manager(Uhcffa.instance().getManager())
            .provider(new TrailMenu())
            .size(6, 9)
            .title(DARK_GRAY + "Trail")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        User user = userSet.getUser(player);
        PurchasedTrail purchasedTrail = user.purchasedTrail;
        List<Trail> trails = Trails.TRAILS;
        ClickableItem[] items = new ClickableItem[trails.size()];

        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.AIR)));

        for (int i = 0; i < items.length; i++) {
            Trail item = trails.get(i);

            items[i] = ClickableItem.of(
                    ItemStackBuilder.createItem(item.item.clone(),GREEN + item.name,
                            Arrays.asList(
                                    DARK_GRAY + "エフェクト",
                                    purchasedTrail.has(item) ? "" : GRAY + "コスト: "+ GOLD + item.value,
                                    purchasedTrail.has(item) ? (user.purchasedTrail.isSelected(item) ?  GREEN + "選択済み" : YELLOW + "クリックして選択") : RED + "購入する"
                            ))
                    , e -> {
                        if(!user.purchasedTrail.isSelected(item) ) {
                            if (purchasedTrail.has(item)) {
                                user.purchasedTrail.select(item);
                                SoundEffects.SUCCEEDED.play(player);
                            } else {
                                if (purchasedTrail.canBuy(item)) {
                                    purchasedTrail.buy(item);
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
                    ItemStackBuilder.builder(Material.ARROW)
                            .setDisplayName(GREEN + "前のページへ")
                            .build(),
                    e -> INVENTORY.open(player, pagination.previous().getPage())));
        }

        if(!pagination.isLast()) {
            contents.set(5, 8, ClickableItem.of(
                    ItemStackBuilder.builder(Material.ARROW)
                            .setDisplayName(GREEN + "次のページへ")
                            .build(),
                    e -> INVENTORY.open(player, pagination.next().getPage())));
        }

        contents.set(5, 3, ClickableItem.of(
                ItemStackBuilder.builder(Material.ARROW)
                        .setDisplayName(GRAY + "戻る")
                        .setLore(Arrays.asList(
                                GRAY + "化粧品へ"
                        ))
                        .build(),
                e -> MyCosmeticsMenu.INVENTORY.open(player)));

        contents.set(5, 4, ClickableItem.of(
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
