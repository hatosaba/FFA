package si.f5.hatosaba.uhcffa.schedule.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;

public class ItemTask extends AsyncTask {

    public ItemTask() {
        super(0);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getItemInHand().isSimilar(ExecutableItemType.ANDURIL.getItem())) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
            }
            if (p.getInventory().getBoots() != null) {
                if (p.getInventory().getBoots().isSimilar(ExecutableItemType.HERMESBOOTS.getItem())) {
                    float speed = 0.225F;
                    p.setWalkSpeed(speed);
                } else {
                    p.setWalkSpeed(0.2F);
                }
            } else {
                p.setWalkSpeed(0.2F);
            }
        }
    }

}
