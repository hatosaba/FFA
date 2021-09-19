package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.menu.KitSelectorMenu;
import si.f5.hatosaba.uhcffa.menu.ShopMenu;

public class ItemListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (Constants.KIT_SELECTOR.isSimilar(event.getItem())) {
            KitSelectorMenu.INVENTORY.open(player);
        }

        if (Constants.INSTANT_RESPAWN_ITEM.isSimilar(event.getItem())) {
            if(KitManager.getInstance().isSelected(player))
                KitManager.getInstance().selectToKit(player, KitManager.getInstance().getSelectedPlayer().get(player), false);
        }

        if (Constants.BACK_TO_LOBBY.isSimilar(event.getItem())) {
            player.teleport(Uhcffa.instance().config().getLobby());
            tryGivingDefaultItemsTo(player);
            if(player.getGameMode() == GameMode.SURVIVAL) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }

        if (Constants.SHOP.isSimilar(event.getItem())) {
            ShopMenu.INVENTORY.open(player);
        }
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
            int speedduration = 5 * 20;
            int regenduration = 7 * 20;
            if (p.getActivePotionEffects() != null) {
                for (PotionEffect pot : p.getActivePotionEffects()) {
                    if (giveSpeed && pot.getType().equals(PotionEffectType.SPEED) && pot.getDuration() * 20 >= speedduration) {
                        giveSpeed = false;
                        continue;
                    }
                    if (giveRegen && pot.getType().equals(PotionEffectType.REGENERATION) && pot.getDuration() * 20 >= regenduration) {
                        giveRegen = false;
                        continue;
                    }
                }
            }
            if (giveSpeed)
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
            if (giveRegen)
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 1));
            p.sendMessage("§aYou ate a player head and gained 7 seconds of Regeneration II!");
        }
        if (name.equalsIgnoreCase("§6Golden Head")) {
            event.setCancelled(true);
            if (item.getAmount() == 1) {
                p.setItemInHand(new ItemStack(Material.AIR));
            } else if (item.getAmount() > 1) {
                ItemStack itemClone = item.clone();
                itemClone.setAmount(item.getAmount() - 1);
                p.setItemInHand(itemClone);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9 * 20, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 2, 0));
            p.sendMessage("§aYou ate a §6Golden Head §aand gained 9 seconds of Regeneration III and 2 minutes of Absorption!");

        }
    }

    private void tryGivingDefaultItemsTo(Player player) {
            player.getInventory().clear();
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 0);
            tryGivingItemTo(player, Constants.SHOP, 4);
    }

    private void tryGivingItemTo(Player player, ItemStack item, int slot) {
        Inventory inventory = player.getInventory();
        if (!inventory.contains(item)) inventory.setItem(slot, item);
    }
}
