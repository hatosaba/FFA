package si.f5.hatosaba.uhcffa.specialItem;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;

public class PlayerExecuteItemListener implements Listener {

    private final ExecutableItemRegistry registry;

    public PlayerExecuteItemListener(ExecutableItemRegistry registry) {
        Bukkit.getPluginManager().registerEvents(this, Uhcffa.getInstance());
        this.registry = registry;
    }

    @EventHandler()
    public void on(PlayerInteractEvent event) {
        ExecutableItem item = toExecutableItem(event.getItem());
        if (item != null) item.onClick(event);
    }

    @EventHandler()
    public void on(PlayerItemConsumeEvent event) {
        ExecutableItem item = toExecutableItem(event.getItem());
        if (item != null) item.onConsume(event);
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        ExecutableItem item = toExecutableItem(event.getItemDrop().getItemStack());
        if (item != null) item.onDrop(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void on(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        ExecutableItem item = toExecutableItem(damager.getInventory().getItemInHand());
        ExecutableItem helmet = toExecutableItem(damager.getInventory().getHelmet());
        if (helmet != null) helmet.onDamageEntity(new PlayerDamageEntityEvent(event));
        if (item != null) item.onDamageEntity(new PlayerDamageEntityEvent(event));

        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player){
                    ExecutableItem bow = toExecutableItem(player.getInventory().getItemInHand());
                    if (bow != null) bow.onDamageEntity(new PlayerDamageEntityEvent(event));
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        ExecutableItem item = toExecutableItem(event.getEntity().getKiller().getItemInHand());
        if (item != null) item.onPlayerKill(event);
    }

    private ExecutableItem toExecutableItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;

        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey("id")) return null;

        String id = nbtItem.getString("id");

        return registry.get(id);
    }
}
