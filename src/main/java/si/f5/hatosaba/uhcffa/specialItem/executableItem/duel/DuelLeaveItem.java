package si.f5.hatosaba.uhcffa.specialItem.executableItem.duel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

public class DuelLeaveItem extends ExecutableItem {

    public DuelLeaveItem(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.BED)
                        .name("&cReturn to Lobby")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            arena.removePlayer(customPlayer);
        }
    }
}

