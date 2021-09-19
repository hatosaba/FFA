package si.f5.hatosaba.uhcffa.listeners;

import me.MathiasMC.PvPLevels.api.PvPLevelsAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.cosmetics.manager.KillManager;
import si.f5.hatosaba.uhcffa.cosmetics.manager.TrailManager;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;

import java.math.BigDecimal;

import static org.bukkit.ChatColor.*;

public class PlayerDeathListener implements Listener {

    private final KitManager kitManager = KitManager.getInstance();
    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    @EventHandler (priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        if (!player.getWorld().getName().equals("kitpvp"))
            return;

        if(player.getGameMode() != GameMode.SURVIVAL) return;
        if(player.getKiller() != null && player.getKiller().getType().equals(EntityType.PLAYER)) {
            Player killer = player.getKiller();
            killer.setHealth(killer.getMaxHealth());
            User user = UserSet.getInstnace().getUser(killer);
            if(user.purchasedEffect.getSelectedEffect().id == 0) {
                if(user.asBukkitPlayer().hasPermission("ffa.vote")){
                    KillManager.getInstance().getKillEffect(1000).play(player);
                }
            }else if (KillManager.getInstance().getKillEffect(user) != null) {
                KillManager.getInstance().getKillEffect(user).play(player);
            }
        }

        dropSkull(player);
        player.setHealth(player.getMaxHealth());
        //Sync.define(() -> )).execute();
       // Sync.define(() -> ((CraftPlayer) player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN))).execute();

        player.setAllowFlight(true);
        player.setFlying(true);
        spectetorSet.applyHideMode(player);

        event.setDeathMessage(null);

        String uuid = player.getUniqueId().toString();
        PvPLevelsAPI.getInstance().getPlayerConnect(uuid).setXp(PvPLevelsAPI.getInstance().getPlayerConnect(uuid).getKills());

        if(UserSet.getInstnace().getUser(player).scoreboardBuilder != null)
            UserSet.getInstnace().getUser(player).scoreboardBuilder.clearScoreboard();
        clearInv(player);
        Sync.define(() -> tryGivingDefaultItemsTo(player)).executeLater(1*20);
        broadcastDeath(player, player.getKiller());
    }

    private void broadcastDeath(Player deathPlayer, Player killer) {
        PvPLevelsAPI api = PvPLevelsAPI.getInstance();
        long deathPlayerKills = api.getPlayerConnect(deathPlayer.getUniqueId().toString()).getKills();
        long killerKills = api.getPlayerConnect(killer.getUniqueId().toString()).getKills();
        String uuid = killer.getUniqueId().toString();
        PvPLevelsAPI.getInstance().getPlayerConnect(uuid).setXp(PvPLevelsAPI.getInstance().getPlayerConnect(uuid).getKills());
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getWorld().getName().equals("kitpvp")).forEach(player -> {
                    if (killer.getName() == player.getName()) {
                        player.sendMessage(
                                RED + deathPlayer.getName() + DARK_RED + "[" + deathPlayerKills  + "]" + RED + " was slain by " + GREEN + killer.getName() + DARK_RED + "[" + killerKills + "]"
                        );
                    } else {
                        player.sendMessage(
                                RED + deathPlayer.getName() + DARK_RED + "[" + deathPlayerKills + "]" + RED + " was slain by " + RED + killer.getName() + DARK_RED + "[" + killerKills + "]"
                        );
                    }
                }
        );
    }

    private void dropSkull(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta metaSkull = (SkullMeta) skull.getItemMeta();
        metaSkull.setDisplayName("§c§l" + player.getName() + "'s Head");
        metaSkull.setOwner(player.getName());
        skull.setItemMeta(metaSkull);
        player.getWorld().dropItemNaturally(player.getLocation(), skull);
    }

    private void clearInv(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    @EventHandler
    public void onProjectileDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Projectile && !event.isCancelled()) {
            Projectile pro = (Projectile) event.getDamager();
            if (pro.getShooter() instanceof Player) {
                Player p = (Player) pro.getShooter();
                Player e = (Player) event.getEntity();
                if (pro.getType().equals(EntityType.ARROW)) {
                    double health1 = e.getHealth();
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Uhcffa.instance(), () -> {
                        double distance = p.getLocation().distance(e.getLocation());
                        BigDecimal distancebi = new BigDecimal(distance);
                        double newdistance = distancebi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        if (distance >= 50) {
                            Bukkit.broadcastMessage("§f" + p.getName() + " §eshot §f" + e.getName() + " §efrom §c" + newdistance
                                    + " §eblocks away!");
                        }
                        double damage = health1 - e.getHealth();
                        BigDecimal bi = new BigDecimal(damage);
                        double d = bi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        double health = e.getHealth();
                        BigDecimal di = new BigDecimal(health);
                        double h = di.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        p.sendMessage("§bHit §c" + e.getName() + " §bfor " + d + " §bdamage (" + h + " remaining).");
                        p.sendMessage("§c" + e.getDisplayName() + " §eis on §c" + (int) e.getHealth() + " §eHP!");
                        //if(p.getInventory().contains(new ItemStack(Material.BOW)))
                    }, 1L);
                }
                if (pro.getType().equals(EntityType.FISHING_HOOK)) {
                    p.sendMessage("§c" + e.getDisplayName() + " §eis on §c" + (int) e.getHealth() + " §eHP!");
                }
            }
        }

    }

    @EventHandler
    public void onProjectileShoot(ProjectileLaunchEvent event) {
        Projectile e = event.getEntity();
        if (e.getShooter() instanceof Player) {
            Player p = (Player) e.getShooter();
            User user = UserSet.getInstnace().getUser(p);
            if(user.purchasedTrail.getSelectedTrail().id == 0) {
                if(user.asBukkitPlayer().hasPermission("ffa.vote")){
                    TrailManager.getInstance().getTrailEffect(1000).play(e);
                }
            }else if (TrailManager.getInstance().getTrailEffect(user) != null) {
                TrailManager.getInstance().getTrailEffect(user).play(e);
            }
        }
    }


    private void tryGivingDefaultItemsTo(Player player) {
        if (KitManager.getInstance().isSelected(player)) {
            tryGivingItemTo(player, Constants.INSTANT_RESPAWN_ITEM, 0);
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 2);
            tryGivingItemTo(player, Constants.SHOP, 4);
            tryGivingItemTo(player, Constants.BACK_TO_LOBBY, 8);
        } else {
            tryGivingItemTo(player, Constants.KIT_SELECTOR, 0);
            tryGivingItemTo(player, Constants.SHOP, 4);
        }
    }

    private void tryGivingItemTo(Player player, ItemStack item, int slot) {
        Inventory inventory = player.getInventory();
        if (!inventory.contains(item)) inventory.setItem(slot, item);
    }
}
