package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.menu.LayoutEditorMenu;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa.FlaskOfCleansing;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        event.setExpToDrop(0);

        if (event.getState().name().equalsIgnoreCase("CAUGHT_FISH")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void Event(PlayerMoveEvent e) {
            Player player = e.getPlayer();
            Material bottomBlock = Material.DIAMOND_BLOCK;
            Material topBlock = Material.IRON_PLATE;
            double angle = 1.0;
            Sound sound = Sound.GHAST_FIREBALL;
            if ((player.getLocation().subtract(0, 1, 0).getBlock().getType().equals(bottomBlock)
                    && (player.getLocation().getBlock().getType().equals(topBlock)))) {
                Vector v = player.getLocation().getDirection().multiply(4).setY(angle);
                player.setVelocity(v);
                player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
            }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onDropItem(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(player.getGameMode() != GameMode.SURVIVAL) return;

        if (player.getWorld().getName().equals("kitpvp") || player.getWorld().getName().equals("lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(player);

        if (customPlayer.inArena()) return;

        if (!player.getWorld().getName().equals("kitpvp"))
            return;

        if(spectetorSet.isHideMode(player)) event.setCancelled(true);

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof TNTPrimed) {
            event.setCancelled(false);
        }
        if (event.getEntity() instanceof Fireball) {
            event.setCancelled(false);
        }
        if (event.getEntity() instanceof Wither) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof Ghast) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        event.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("kitpvp"))
            return;
        event.getItemDrop().setItemStack(new ItemStack(Material.AIR));
        if(player.getGameMode() == GameMode.SURVIVAL) event.setCancelled(true);
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null)
            return;

        if (!player.getWorld().getName().equals("kitpvp"))
            return;

        event.setCancelled(true);
        player.updateInventory();
    }

    @EventHandler
    public void onItemFrame(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (event.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameDamage(EntityDamageByEntityEvent event) {
        UUID uuid = event.getDamager().getUniqueId();
        if (event.getEntity() instanceof ItemFrame && event.getDamager() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile instanceof Arrow))
            return;

        Arrow arrow = (Arrow) projectile;

        World world = arrow.getWorld();
        if (!arrow.getWorld().getName().equals("kitpvp"))
            return;

        BlockIterator bi = new BlockIterator(world, arrow.getLocation().toVector(),
                arrow.getVelocity().normalize(), 0, 4);
        Block hit;

        while (bi.hasNext()) {
            hit = bi.next();
            if (hit.getLocation().getBlock() != null) {
                arrow.remove();
                break;
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) return;

        Player player = ((Player) entity).getPlayer();
        String playerId = PlayerConverter.getID(player);
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerId);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() != ArenaState.IN_GAME)
                event.setCancelled(true);
        }

        if (player.getGameMode() == GameMode.CREATIVE) return;

        if(spectetorSet.isHideMode(player))
            event.setCancelled(true);

        switch (event.getCause()){
            case FALL:
                if (customPlayer.inArena()) {
                    Arena arena = customPlayer.getArena();
                    if  (arena.getArenaState() == ArenaState.IN_GAME) return;
                }
                event.setCancelled(true);
            case VOID:
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Player player = (Player) event.getEntered();

        if (spectetorSet.isHideMode(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent e) {
        Vehicle v = e.getVehicle();
        if (v.getPassenger() instanceof Player) {
            Player p = (Player) v.getPassenger();
            if (spectetorSet.isHideMode(p)) {
                v.teleport(e.getFrom());
            }
        }
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        Player player = (Player) event.getAttacker();
        if (spectetorSet.isHideMode(player)) {
            event.setCancelled(true);
        }
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            if (FlaskOfCleansing.cooldownPlayers.contains(event.getEntity())) event.setCancelled(true);
        }
        // Disable auto-healing from a filled hunger bar.
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity defender = event.getEntity();
        Entity damager = event.getDamager();

        if (defender.getType() == EntityType.HORSE) {
            if(damager instanceof Player) {
                if (spectetorSet.isHideMode((Player) damager))
                    event.setCancelled(true);
            }
        }

        if (damager instanceof Projectile) {
            damager = (Entity) ((Projectile)damager).getShooter();
        }

        if (defender instanceof Player && damager != null) {
            Player p = (Player)damager;
            String playerId = PlayerConverter.getID(p);
            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerId);

            if (customPlayer.inArena()) return;

            if(spectetorSet.isHideMode(p))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHorseInventory(InventoryClickEvent event) {
        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(event.getWhoClicked());

        if (event.getInventory() instanceof HorseInventory) {
            if(event.getWhoClicked().getGameMode() != GameMode.SURVIVAL) return;

            if (event.getView().getTopInventory() == event.getClickedInventory()) {
                event.setCancelled(true);
            }
        }

        if (customPlayer.inArena()) return;

        if (KitManager.getInstance().isSelected(customPlayer.getPlayer())) return;

        if(event.getWhoClicked().getGameMode() != GameMode.ADVENTURE) return;
        if(event.getAction().equals(InventoryAction.DROP_ALL_CURSOR) || event.getAction().equals(InventoryAction.DROP_ONE_CURSOR)) {
            event.setCancelled(true);
        }
        if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            event.setCancelled(true);
        }
        if (event.getView().getBottomInventory() == event.getClickedInventory()) {
                event.setCancelled(true);
            }
    }

    /*@EventHandler
    public void onSoilChangeEntity(EntityInteractEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID uuid = player.getUniqueId();
            PlayerObject playerObject = Duel.getPlayerController().getPlayer(uuid);

            if (playerObject == null) {
                return;
            }

            if (playerObject.isSpectator()) {
                event.setCancelled(true);
            }
            if (playerObject.inArena()) {
                Arena arena = playerObject.getArena();
                if (arena.getArenaState() == Arena.ArenaState.WAIT || arena.getArenaState() == Arena.ArenaState.PLAY || arena.getArenaState() == Arena.ArenaState.END || arena.getArenaState() == Arena.ArenaState.RESET) {
                    if (event.getEntityType() != EntityType.PLAYER && event.getBlock().getType() == XMaterial.FARMLAND.parseMaterial()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }*/

}
