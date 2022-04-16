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
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.user.User;
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

        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(player);
        User user = customPlayer.getUser();

        for (i = 0; i < 9; i++) {
            if (user.isEditedKit(kit.getName()))
                hotbar[i] = user.getKit(kit)[i] == null ? x : user.getKit(kit)[i];
            else
                hotbar[i] = kit.getItems()[i] == null ? x : kit.getItems()[i];

        }
        for (int j = 0; j < 27; j++) {
            if (user.isEditedKit(kit.getName()))
                inventory[j] = user.getKit(kit)[i + j] == null ? x : user.getKit(kit)[i + j];
            else
                inventory[j] = kit.getItems()[i + j] == null ? x : kit.getItems()[i + j];
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
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(player);
        User user =  customPlayer.getUser();

        contents.fillRow(3, ClickableItem.empty(ItemBuilder.of(Material.STAINED_GLASS_PANE).build()));
        contents.set(5, 3, ClickableItem.of(ItemBuilder.of(Material.ARROW).name(Translated.key("gui.go-back").get(player)).build(), e -> {
            NormalKitSelectorMenu.INVENTORY().open(player);
        }));
        contents.set(5, 4, ClickableItem.of(ItemBuilder.of(Material.CHEST).name(Translated.key("gui.layout.save").get(player)).build(), e -> {
            user.saveKit(kit, hotbar, inventory);
            NormalKitSelectorMenu.INVENTORY().open(player);
        }));
        contents.set(5, 5, ClickableItem.of(ItemBuilder.of(Material.BARRIER).name(Translated.key("gui.layout.reset").get(player)).build(), e -> {
            user.resetKit(kit);
            INVENTORY(player, kit).open(player);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int i;
        for (i = 0; i < 9; i++) {
            int slot = i;
            int row = 4;
            int column = slot % 9;
            contents.set(row, column, ClickableItem.of(ItemBuilder.modify(hotbar[slot]).build(), e -> {
                if (e.getCurrentItem().getType() != null) {
                    if (e.getCursor() != null) {
                        hotbar[slot] = e.getCursor();
                        e.setCursor(e.getCurrentItem());
                        contents.set(row, column, ClickableItem.empty(ItemBuilder.modify(e.getCursor()).build()));
                    } else {
                        hotbar[slot] = x;
                        e.setCursor(e.getCurrentItem());
                        contents.set(row, column, ClickableItem.empty(ItemBuilder.modify(x).build()));
                    }
                } else {
                    if (e.getCursor() != null) {
                        hotbar[slot] = e.getCursor();
                        e.setCursor(new ItemStack(Material.AIR));
                        contents.set(row, column, ClickableItem.empty(ItemBuilder.modify(e.getCursor()).build()));
                    }
                }
            }));
        }
        for (int j = 0; j < 27; j++) {
            int slot = (i + j);
            int row = (slot / 9) - 1;
            int column = slot % 9;
            int finalJ = j;
            contents.set(row, column, ClickableItem.of(ItemBuilder.modify(inventory[j]).build(), e -> {
                if (e.getCurrentItem().getType() != null) {
                    if (e.getCursor() != null) {
                        inventory[finalJ] = e.getCursor();
                        e.setCursor(e.getCurrentItem());
                        contents.set(row, column, ClickableItem.empty(ItemBuilder.modify(e.getCursor()).build()));
                    } else {
                        inventory[finalJ] = x;
                        e.setCursor(e.getCurrentItem());
                        contents.set(row, column, ClickableItem.empty(ItemBuilder.modify(x).build()));
                    }
                } else {
                    if (e.getCursor() != null) {
                        inventory[finalJ] = e.getCursor();
                        e.setCursor(new ItemStack(Material.AIR));
                        contents.set(row, column, ClickableItem.empty(ItemBuilder.modify(e.getCursor()).build()));
                    }
                }
            }));
        }
    }
}
