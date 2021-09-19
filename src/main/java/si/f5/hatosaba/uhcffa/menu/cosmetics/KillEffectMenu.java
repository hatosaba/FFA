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
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.user.PurchasedEffect;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ItemStackBuilder;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class KillEffectMenu implements InventoryProvider {

    private final UserSet userSet = UserSet.getInstnace();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("killEffect")
            .manager(Uhcffa.instance().getManager())
            .provider(new KillEffectMenu())
            .size(6, 9)
            .title(DARK_GRAY + "KillEffect")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        User user = userSet.getUser(player);
        PurchasedEffect purchasedEffect = user.purchasedEffect;
        List<Effect> effects = Effects.EFFECTS;
        ClickableItem[] items = new ClickableItem[effects.size()];

        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.AIR)));

        for (int i = 0; i < items.length; i++) {
            Effect item = effects.get(i);

            items[i] = ClickableItem.of(
                    ItemStackBuilder.createItem(item.item.clone(),GREEN + item.name,
                            Arrays.asList(
                                    DARK_GRAY + "エフェクト",
                                    purchasedEffect.has(item) ? "" : GRAY + "コスト: "+ GOLD + item.value,
                                    purchasedEffect.has(item) ? (user.purchasedEffect.isSelected(item) ?  GREEN + "選択済み" : YELLOW + "クリックして選択") : RED + "購入する"
                            ))
                    , e -> {
                        if(!user.purchasedEffect.isSelected(item) ) {
                            if (purchasedEffect.has(item)) {
                                user.purchasedEffect.select(item);
                                SoundEffects.SUCCEEDED.play(player);
                            } else {
                                if (purchasedEffect.canBuy(item)) {
                                    purchasedEffect.buy(item);
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
