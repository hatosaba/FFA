package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class ItemListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent event) {
        ItemStack material = ItemBuilder.of(Material.REDSTONE).name("&cCrafting Material").build();

        if (material.isSimilar(event.getItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onEatHead(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getItemInHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }
        if (!p.getWorld().getName().equals("kitpvp"))
            return;
        String name = item.getItemMeta().getDisplayName();
        if (name.startsWith("§c§l") && name.endsWith("'s Head")) {
            event.setCancelled(true);
            if (item.getAmount() == 1) {
                p.setItemInHand(new ItemStack(Material.AIR));
            } else if (item.getAmount() > 1) {
                ItemStack itemClone = item.clone();
                itemClone.setAmount(item.getAmount() - 1);
                p.setItemInHand(itemClone);
            }
            boolean giveSpeed = true;
            boolean giveRegen = true;
            int speedduration = 12 * 20;
            int regenduration = 7 * 20;
            if (p.getActivePotionEffects() != null) {
                for (PotionEffect pot : p.getActivePotionEffects()) {
                    if (giveSpeed && pot.getType().equals(PotionEffectType.SPEED) && pot.getDuration() * 20 >= speedduration) {
                        giveSpeed = false;
                        continue;
                    }
                    if (giveRegen && pot.getType().equals(PotionEffectType.REGENERATION) && pot.getDuration() * 20 >= regenduration) {
                        giveRegen = false;
                    }
                }
            }
            if (giveSpeed)
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
            if (giveRegen)
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 1));
            p.sendMessage("§aYou ate a player head and gained 7 seconds of Regeneration II!");
        }
    }

/*
    private HashMap<UUID, Double> jumps = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.isSneaking()) {
            if (player.isOnGround()) {
                if(!jumps.containsKey(player.getUniqueId()))
                    jumps.put(player.getUniqueId(), 1.0);

                double multiply = jumps.get(player.getUniqueId());
                if(multiply == 3.0) {
                    jumps.remove(player.getUniqueId());
                    return;
                }
                player.setVelocity(player.getLocation().getDirection().multiply(multiply).setY(0.4));
                jumps.put(player.getUniqueId(), jumps.get(player.getUniqueId()) + 0.25);
            }
        }else {
            jumps.remove(player.getUniqueId());
        }
    }
*/
}
