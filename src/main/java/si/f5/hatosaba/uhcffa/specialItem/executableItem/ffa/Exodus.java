package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.HashSet;

public class Exodus extends ExecutableItem {

    public Exodus(String id) {
        super(id);
    }

    private final HashSet<Player> cooldownPlayers = new HashSet<>();

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.GOLD_HELMET)
                        .name("&dExodus")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    //エンティティに攻撃した時のアクションを設定する
    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;

        if (cooldownPlayers.contains(event.damager())) return;

        event.damager().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));

        cooldownPlayers.add(event.damager());

        //2秒後にクールダウンを完了させる
        Sync.define(() -> cooldownPlayers.remove(event.damager())).executeLater(2 * 20L);
    }
}