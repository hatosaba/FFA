package si.f5.hatosaba.uhcffa.cosmetics.killeffect.effect;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.ParticleEffect;

public class SlimeExplosion extends KillEffect {

    public SlimeExplosion() {
        super( "1000");
    }

    @Override
    public void play(Player p) {
        Location loc = p.getLocation();
        float offsetX = (float) 0.7;
        float offsetY = (float) 0.5;
        float offsetZ = (float) 0.7;
        float addX = 0;
        float addY = 1;
        float addZ = 0;
        float speed = (float) 0.1;
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 10;

            @Override
            public void run() {
                if (i > 0) {
                    ParticleEffect.SLIME.display(offsetX, offsetY, offsetZ, speed, 10, loc.clone().add(addX, addY, addZ), 256f);
                    i--;
                    if (i == 0) {
                        cancel();
                    }
                }
            }

        };
        runnable.runTaskTimer(Uhcffa.instance(), 5, 5);
    }

}
