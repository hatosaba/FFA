package si.f5.hatosaba.uhcffa.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.Translated;

public class LayoutEditorMenu implements InventoryProvider {

    private final Player player;
    private final Kit kit;

    ItemStack x = new ItemStack(Material.AIR);
    ItemStack[] hotbar = {
            x, x, x, x, x, x, x, x, x,
    };
    ItemStack[] inventory = {
            x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x,
            x, x, x, x, x, x, x, x, x,
    };

    public LayoutEditorMenu(Player player, Kit kit) {
        this.player = player;
        this.kit = kit;
        int i;
        for (i = 0; i < 9; i++) {
            hotbar[i] = kit.getItems()[i];
        }
        for (int j = 0; j < 27; j++) {
            inventory[j] = kit.getItems()[i + j];
        }
    }

    public static final SmartInventory INVENTORY(Player player, Kit kit) {


        return SmartInventory.builder()
                .id("layout")
                .manager(Uhcffa.getInstance().getManager())
                .provider(new LayoutEditorMenu(player, kit))
                .size(6, 9)
                .title(Translated.key("gui.layout").get(player))
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(3, ClickableItem.empty(ItemBuilder.of(Material.STAINED_GLASS_PANE).build()));
        contents.set(5, 3, ClickableItem.of(ItemBuilder.of(Material.ARROW).name(Translated.key("gui.go-back").get(player)).build(), e -> {
        }));
        contents.set(5, 4, ClickableItem.of(ItemBuilder.of(Material.CHEST).name(Translated.key("gui.layout.save").get(player)).build(), e -> {
        }));
        contents.set(5, 5, ClickableItem.of(ItemBuilder.of(Material.BARRIER).name(Translated.key("gui.layout.reset").get(player)).build(), e -> {
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int i;
        for (i = 0; i < 9; i++) {
            int slot = i;
            int row = 4;
            int column = slot % 9;
            int finalJ = i;
            contents.set(row, column, ClickableItem.of(ItemBuilder.modify(hotbar[i] == null ? x : hotbar[i]).build(), e -> {
                if (hotbar[finalJ] != x) {
                    contents.set(row, column, ClickableItem.empty(e.getCursor()));
                    hotbar[finalJ] = e.getCursor();
                    e.setCursor(hotbar[finalJ]);
                } else {
                    contents.set(row, column, ClickableItem.empty(ItemBuilder.of(Material.AIR).build()));
                    hotbar[finalJ] = x;
                    e.setCursor(x);
                    player.sendMessage(e.getCurrentItem().getType().name());
                }
            }));
        }
        for (int j = 0; j < 26; j++) {
            int slot = (i + j);
            int row = (slot / 9) - 1;
            int column = slot % 9;
            int finalJ = j;
            contents.set(row, column, ClickableItem.of(ItemBuilder.modify(inventory[j] == null ? x : inventory[j]).build(), e -> {
                if (e.getCursor() != null) {
                    contents.set(row, column, ClickableItem.empty(e.getCursor()));
                    inventory[finalJ] = e.getCursor();
                } else {
                    contents.set(row, column, ClickableItem.empty(ItemBuilder.of(Material.AIR).build()));
                    inventory[finalJ] = x;
                }
                e.setCursor(inventory[finalJ]);
            }));
        }
    }
}
