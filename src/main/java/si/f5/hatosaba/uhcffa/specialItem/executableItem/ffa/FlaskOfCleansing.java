package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import si.f5.hatosaba.uhcffa.event.PlayerDamageEntityEvent;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.HashSet;

public class FlaskOfCleansing extends ExecutableItem {

    public FlaskOfCleansing(String id) {
        super(id);
    }

    public static final HashSet<Player> cooldownPlayers = new HashSet<>();

    @Override
    public ItemStack buildItemStack () {
        ItemStack item = new ItemStack(Material.POTION,1);
        Potion potion = new Potion(PotionType.WEAKNESS, 2);

        potion.setSplash(true);
        potion.apply(item);

        return define(ItemBuilder.modify(item).name("Flask of Cleansing").build());
    }


    @Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        if (!(event.damagee() instanceof Player)) return;
        Player damagee = (Player) event.damagee();

        // 相手の付与されているエフェクトを全て削除
        for (PotionEffect potionEffect : damagee.getActivePotionEffects()) {
            damagee.removePotionEffect(potionEffect.getType());
        }

        cooldownPlayers.add(damagee);

        Sync.define(() -> cooldownPlayers.remove(damagee)).executeLater(20 * 20L);
    }
}
