package si.f5.hatosaba.uhcffa.menu;

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
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.stream.Collectors;

public class KitSelectorMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();
    private final ArenaManager arenaManager = Uhcffa.getInstance().getArenaManager();

    private final String playerID;
    private final String toPlayerID;


    public static final SmartInventory INVENTORY(String playerID, String toPlayerID) {
        return SmartInventory.builder()
                .id("selector")
                .manager(Uhcffa.getInstance().getManager())
                .provider(new KitSelectorMenu(playerID, toPlayerID))
                .size(3, 9)
                .title(PlayerConverter.getName(toPlayerID) + "にリクエストを送信")
                .build();
    }

    public KitSelectorMenu(String playerID, String toPlayerID) {
        this.playerID = playerID;
        this.toPlayerID = toPlayerID;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));
        ClickableItem[] items = new ClickableItem[kitManager.getKits().size()];

        for (int i = 0; i < kitManager.getKits().size(); i++) {
            Kit kit = kitManager.getKits().stream().collect(Collectors.toList()).get(i);

            items[i] = ClickableItem.of(
                    ItemBuilder.of(Material.BUCKET).name("&a" + kit.getName().toUpperCase())
                            .lore(
                                    "&7Click to invite " + PlayerConverter.getName(toPlayerID) + " to",
                                    "&7duel",
                                    "",
                                    "&eClick to play!",
                                    "&7" + arenaManager.getArenas().values().stream()
                                            .filter(arena1 -> {
                                                if (arena1.getKit() == null) return false;
                                                if (arena1.getArenaState() != ArenaState.IN_GAME) return false;
                                                return true;
                                            }).count()
                                            + " currently playing!")
                            .build()
                    , e -> {
                        player.closeInventory();
                        arenaManager.requestDuel(this.playerID, this.toPlayerID, kit);
                    });
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(6);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1).allowOverride(false));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
