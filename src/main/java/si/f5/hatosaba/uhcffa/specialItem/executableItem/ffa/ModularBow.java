package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class ModularBow extends ExecutableItem {

    public ModularBow(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.BOW)
                        .name("&aModular Bow")
                        .build()
        );
    }

    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;
        Player damager = event.damager();
        Player damagee = (Player) event.damagee();

        /*damagee.setHealth(Math.min(0, damagee.getHealth() - 1));
        damagee.getWorld().strikeLightningEffect(damagee.getLocation());
        damager.getInventory().addItem(ItemBuilder.of(Material.ARROW).build());*/
    }
}
