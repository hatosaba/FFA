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
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.Skull;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

public class InvSeeMenu implements InventoryProvider {

    private final KitManager kitManager = KitManager.getInstance();
    private final ArenaManager arenaManager = Uhcffa.instance().getArenaManager();

    private final ItemStack[] contents;
    private final ItemStack[] armorContetns;


    public static SmartInventory INVENTORY(ItemStack[] contents, ItemStack[] armorContetns) {
        return SmartInventory.builder()
                .id("inv")
                .manager(Uhcffa.instance().getManager())
                .provider(new InvSeeMenu(contents, armorContetns))
                .size(6, 9)
                .title("")
                .build();
    }

    public InvSeeMenu(ItemStack[] contents, ItemStack[] armorContetns) {
        this.contents = contents;
        this.armorContetns = armorContetns;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for (int i = 0; i < (player.getInventory().getContents()).length; i++) {
            contents.add(ClickableItem.empty(this.contents.clone()[i]));
        }

        ItemStack helmet = armorContetns[0];
        ItemStack chestplate = armorContetns[1];
        ItemStack leggings = armorContetns[2];
        ItemStack boots = armorContetns[3];

        ItemStack NONE = ItemBuilder.of(Material.BARRIER).name("empty slot").build();

        contents.set(5, 0, ClickableItem.empty(helmet != null ? helmet : NONE));
        contents.set(5, 1, ClickableItem.empty(chestplate != null ? chestplate : NONE));
        contents.set(5, 2, ClickableItem.empty(leggings != null ? leggings : NONE));
        contents.set(5, 3, ClickableItem.empty(boots != null ? boots : NONE));

        contents.set(5, 6, ClickableItem.empty(ItemBuilder.of(Material.APPLE).name("&c" +  " Health").build()));
        contents.set(5, 7, ClickableItem.empty(ItemBuilder.of(Material.GOLDEN_APPLE).name("&e" ).build()));
        contents.set(5, 8, ClickableItem.empty(ItemBuilder.of(Material.COOKED_MUTTON).name("&a" + " Food").build()));

        contents.fillRow(4, ClickableItem.empty(ItemBuilder.of(Material.STAINED_GLASS_PANE).name("").build()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}

