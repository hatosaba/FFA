package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.Arrays;
import java.util.HashSet;

public class Excalibur extends ExecutableItem {

    private final HashSet<Player> cooldownPlayers = new HashSet<>();

    public Excalibur(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name("&eExcalibur")
                        .lore(Arrays.asList(
                                "&cExplosive (!)",
                                "&9Chaos"
                        ))
                        //.enchantment(Enchantment.DURABILITY, 1)
                        .build()
        );
    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        event.setCancelled(true);
    }

    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;
        Player damager = event.damager();
        Player damagee = (Player) event.damagee();

        //クールダウン中なら戻る
        if (cooldownPlayers.contains(damager)) return;

        damagee.getWorld().createExplosion(damagee.getLocation(), 0.0F);

        double damage = 4;
        double health = Math.max(0, damagee.getHealth() - damage);

        damagee.setHealth(health);

        //クールダウンさせる
        cooldownPlayers.add(damager);

        //5秒後にクールダウンを完了させる
        Sync.define(() -> cooldownPlayers.remove(damager)).executeLater(5 * 20L);

    }
}
