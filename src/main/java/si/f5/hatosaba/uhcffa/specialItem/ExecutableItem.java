package si.f5.hatosaba.uhcffa.specialItem;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;

public abstract class ExecutableItem {

    public final String id;

    public ExecutableItem(String id) {
        this.id = id;
    }

    public abstract ItemStack buildItemStack();

    protected ItemStack define(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("id", id);
        return nbtItem.getItem();
    }

    public void onClick(PlayerInteractEvent event) {

    }

    public void onDrop(PlayerDropItemEvent event) {

    }

    public void onConsume(PlayerItemConsumeEvent event) {

    }

    public void onDamageEntity(PlayerDamageEntityEvent event) {

    }

    public void  onPlayerKill(PlayerDeathEvent event) {

    }
}
