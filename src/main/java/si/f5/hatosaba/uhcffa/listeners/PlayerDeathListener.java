package si.f5.hatosaba.uhcffa.listeners;

import me.MathiasMC.PvPLevels.api.PvPLevelsAPI;
import me.MathiasMC.PvPLevels.data.PlayerConnect;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.cosmetics.manager.KillManager;
import si.f5.hatosaba.uhcffa.cosmetics.manager.TrailManager;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa.Daredevil;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.math.BigDecimal;
import java.util.Objects;

public class PlayerDeathListener implements Listener {

    private final KitManager kitManager = KitManager.getInstance();
    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(player));
        if (customPlayer.inArena()) return;

        if (!player.getWorld().getName().equals("kitpvp")) return;

        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (player.getKiller() != null && player.getKiller().getType().equals(EntityType.PLAYER)) {
            Player killer = player.getKiller();
            killer.setHealth(Math.min(40, killer.getHealth() + (40 * .6)));
        }

        customPlayer.setPreset();

        event.getDrops().clear();
        event.setDeathMessage(null);

        clearInv(player);

        deathEffect(player);

        giveMaterial(player);

        player.setHealth(player.getMaxHealth());

        player.teleport(player.getLocation());

        spectetorSet.applyHideMode(player);

      //  ..PvPLevelsAPI.getInstance().getPlayerConnect(player.).setXp(PvPLevelsAPI.getInstance().getPlayerConnect(uuid).getKills());

        Sync.define(() -> Uhcffa.getInstance().setSpectatorItem(player)).executeLater(20L);

        // プレイヤーヘッド
        dropSkull(player);

        broadcastDeathMessage(player);
    }

    private void broadcastDeathMessage(Player player) {
        Player killer = player.getKiller();
        PvPLevelsAPI api = PvPLevelsAPI.getInstance();
        ItemStack item = killer.getItemInHand();

        long deathPlayerKills = api.getPlayerConnect(player.getUniqueId().toString()).getKills();
        long killerKills = api.getPlayerConnect(killer.getUniqueId().toString()).getKills();

        Bukkit.getOnlinePlayers().stream().filter(online -> online.getWorld().getName().equals("kitpvp")).forEach(online -> {
            String deathMessage = ChatColor.RED + player.getName() + ChatColor.DARK_RED + "[" + deathPlayerKills + "]" + ChatColor.RED + " was slain by ";
            if (killer.getName().equals(player.getName())) {
                deathMessage = deathMessage + ChatColor.GREEN + killer.getName() + ChatColor.DARK_RED + "[" + killerKills + "]";
            } else {
                deathMessage = deathMessage + ChatColor.RED + killer.getName() + ChatColor.DARK_RED + "[" + killerKills + "]";
            }
            if (player.getLastDamageCause() == null || player.getLastDamageCause().getCause() == null) {
                if (Objects.requireNonNull(item).getItemMeta().hasDisplayName())
                    deathMessage += " using " + ChatColor.GREEN + "[" + ChatColor.stripColor(item.getItemMeta().getDisplayName()) + "]";
            } else {
                switch (player.getLastDamageCause().getCause()) {
                    case PROJECTILE:
                        deathMessage = player.getName() + " was shot by " + killer.getName();
                        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                            deathMessage += " using " + ChatColor.GREEN + "[" + ChatColor.stripColor(item.getItemMeta().getDisplayName()) + "]";
                        }
                        double distance = player.getLocation().distance(killer.getLocation());
                        BigDecimal distancebi = new BigDecimal(distance);
                        double newdistance = distancebi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        deathMessage += " (" + newdistance + " blocks)";
                        break;
                    case FALL:
                        deathMessage = player.getName() + " knocked off a cliff by " + player.getName() + "!";
                        break;
                    case LAVA:
                        deathMessage = player.getName() + " tried to swim in lava to escape " + player.getName();
                        break;
                    case FIRE:
                        deathMessage = player.getName() + " walked into fire whilst fighting " + player.getName();
                        break;
                    default:
                        break;
                }
            }
            online.sendMessage(deathMessage);
        });
    }

    private void giveMaterial(Player player) {
        final Player killer = player.getKiller();
        if (killer != null) {
            ItemStack material = ItemBuilder.of(Material.REDSTONE).name("&cCrafting Material").amount(8).build();
            killer.getInventory().addItem(material);
        }
    }

    private void deathEffect(Player player) {
        final Player killer = player.getKiller();
        if (killer != null) {
            User user = UserSet.getInstnace().getUser(player.getKiller());
            if (user.purchasedEffect.getSelectedEffect().id == 0) {
                // 雷を落とす
                player.getWorld().strikeLightningEffect(player.getLocation());
            } else if (KillManager.getInstance().getKillEffect(user) != null) {
                KillManager.getInstance().getKillEffect(user).play(player);
            }
        } else {
            ItemStack material = ItemBuilder.of(Material.REDSTONE).name("&cCrafting Material").amount(8).build();
            // 雷を落とす
            player.getWorld().strikeLightningEffect(player.getLocation());
            player.getWorld().dropItemNaturally(player.getLocation(), material);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (Daredevil.horses.contains(event.getEntity())) {
            Daredevil.horses.remove(event.getEntity());
            event.setDroppedExp(0);
            event.getDrops().clear();
        }

        if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getEntity());
            OfflinePlayer player = Bukkit.getOfflinePlayer(event.getEntity().getName());
            PlayerConnect victim = PvPLevelsAPI.getInstance().getPlayerConnect(player.getUniqueId().toString());
            PlayerConnect killer = PvPLevelsAPI.getInstance().getPlayerConnect(event.getEntity().getKiller().getUniqueId().toString());
            if (victim != null) victim.setDeaths(victim.getDeaths() + 1);
            if (killer != null) killer.setKills(killer.getKills() + 1);
            dropSkull(event.getEntity());
            ItemStack material = ItemBuilder.of(Material.REDSTONE).name("&cCrafting Material").amount(8).build();
            event.getEntity().getKiller().getInventory().addItem(material);
            event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
            PlayerJoinQuitListener.combatPlayers.remove(player.getUniqueId());
            npc.destroy();
            event.setDroppedExp(0);
        }
    }

    private void dropSkull(Entity entity) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta metaSkull = (SkullMeta) skull.getItemMeta();
        metaSkull.setDisplayName("§c§l" + entity.getName() + "'s Head");
        metaSkull.setOwner(entity.getName());
        skull.setItemMeta(metaSkull);
        entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), skull);
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
            if (pro.getShooter() instanceof Player && event.getEntity() instanceof Player) {
                Player p = (Player) pro.getShooter();
                Player e = (Player) event.getEntity();
                if (pro.getType().equals(EntityType.ARROW)) {
                    if (ExecutableItemType.MODULAR_BOW.getItem().isSimilar(p.getItemInHand())) {
                        e.setHealth(Math.max(0, e.getHealth() - 1.5));
                        e.getWorld().strikeLightningEffect(e.getLocation());
                        p.getInventory().addItem(ItemBuilder.of(Material.ARROW).build());
                    }
                    double health1 = e.getHealth();
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Uhcffa.getInstance(), () -> {
                        double distance = p.getLocation().distance(e.getLocation());
                        BigDecimal distancebi = new BigDecimal(distance);
                        double newdistance = distancebi.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        if (distance >= 50) {
                            Bukkit.broadcastMessage("§f" + p.getName() + " §eshot §f" + e.getName() + " §efrom §c" + newdistance
                                    + " §eblocks away!");
                        }
                        event.getEntity().setLastDamageCause(event);
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
}
