package si.f5.hatosaba.uhcffa.menu;

import com.avaje.ebeaninternal.server.cache.CachedBeanDataUpdate;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.Translated;

import java.util.ArrayList;

public class NormalKitSelectorMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();
    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();

    public static SmartInventory INVENTORY() {
        return SmartInventory.builder()
                .id("normal")
                .manager(Uhcffa.getInstance().getManager())
                .provider(new NormalKitSelectorMenu())
                .size(6, 9)
                .title("")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.AIR)));
        ClickableItem[] items = new ClickableItem[kitManager.getKits().size()];

        for (int i = 0; i < kitManager.getKits().size(); i++) {
            Kit kit = new ArrayList<>(kitManager.getKits()).get(i);

            items[i] = ClickableItem.of(
                    ItemBuilder.of(kit.getIcon().getType()).name(Translated.key("duel." + kit.getName()).get(player))
                            .lore(
                                    "",
                                    "&eLeft Click to play",
                                    "&7" + arenaManager.getArenas().values().stream()
                                            .filter(arena1 -> {
                                                if (arena1.getKit() == null) return false;
                                                if (arena1.getKit() != kit) return false;
                                                if (arena1.getArenaState() != ArenaState.IN_GAME) return false;
                                                return true;
                                            }).count()
                                            + " currently playing!")
                            .flags(ItemFlag.HIDE_ATTRIBUTES)
                            .build()
                    , e -> {
                        if (e.isLeftClick()) {
                            player.closeInventory();
                            arenaManager.joinMatch(Uhcffa.getCustomPlayer(player), kit);
                        }
                    });
        }

        contents.set(5,4, ClickableItem.of(ItemBuilder.of(Material.BARRIER).build(), e -> player.closeInventory()));
        contents.set(5,7, ClickableItem.of(ItemBuilder.of(Material.BARRIER).build(), e -> LayoutEditorMenu.INVENTORY(player, kitManager.getKits().stream().findFirst().get()).open(player)));


        pagination.setItems(items);
        pagination.setItemsPerPage(6);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1).allowOverride(false));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
