package si.f5.hatosaba.uhcffa.schedule.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.Constants;

public class AndurilTask extends AsyncTask {

    public AndurilTask() {
        super(1);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Constants.ANDURIL.isSimilar(p.getItemInHand())) {

                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
            }
        }
    }

}
