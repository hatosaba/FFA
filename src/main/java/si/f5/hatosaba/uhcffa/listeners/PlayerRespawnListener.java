package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.UserSet;

import java.util.HashSet;

public class PlayerRespawnListener implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();
    private final HashSet<Player> cooldownPlayers = new HashSet<>();
    private final HashSet<Player> respawnPlayers = new HashSet<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("kitpvp"))
            return;
        if (Constants.RESPAWN_ITEM.isSimilar(event.getItem())) {

            //クールダウン中なら戻る
            if(cooldownPlayers.contains(player)) return;

            cooldownPlayers.add(player);
            respawnPlayers.add(player);

            //5秒後にクールダウンを完了させる
            Sync.define(() -> cooldownPlayers.remove(player)).executeLater(5*20L);

            //10秒後にリスポーンを完了させる
            Sync.define(() -> {
                if(respawnPlayers.contains(player)) {
                    PlayerInventory inv = player.getInventory();
                    inv.clear();
                    inv.setHelmet(new ItemStack(Material.AIR));
                    inv.setChestplate(new ItemStack(Material.AIR));
                    inv.setLeggings(new ItemStack(Material.AIR));
                    inv.setBoots(new ItemStack(Material.AIR));
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }
                    if(UserSet.getInstnace().getUser(player).scoreboardBuilder != null)
                        UserSet.getInstnace().getUser(player).scoreboardBuilder.clearScoreboard();
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    tryGivingDefaultItemsTo(player);
                    spectetorSet.applyHideMode(player);
                    respawnPlayers.remove(player);
                }
            }).executeLater(8*20L);

            player.sendMessage("§e8秒間§7その場から動かないでください...");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(respawnPlayers.contains(player)) {
            Block from = event.getFrom().getBlock();
            Block to = event.getTo().getBlock();

            if (from.getLocation().distance(to.getLocation()) < 1) {
                return;
            }
            respawnPlayers.remove(player);
            player.sendMessage("§eその場から動いたためリスポーンをキャンセルしました");
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
