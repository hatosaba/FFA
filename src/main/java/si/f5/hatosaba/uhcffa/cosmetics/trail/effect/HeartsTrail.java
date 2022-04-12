package si.f5.hatosaba.uhcffa.cosmetics.trail.effect;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.ParticleEffect;

public class HeartsTrail extends TrailEffect {

    public HeartsTrail() {
        super("9");
    }

    @Override
    public void play(Entity target) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (target == null || target.isOnGround() || target.isDead()) {
                    cancel();
                }
                ParticleEffect.HEART.display(0, 0, 0, 0, 1, target.getLocation(), 256);
            }

        };
        runnable.runTaskTimer(Uhcffa.getInstance(), 2, 2);
    }

}
