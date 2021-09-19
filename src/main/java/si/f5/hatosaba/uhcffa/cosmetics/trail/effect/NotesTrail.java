package si.f5.hatosaba.uhcffa.cosmetics.trail.effect;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesTrail extends TrailEffect {

    private List<Color> colors;

    public NotesTrail() {
        super("10");
        colors = new ArrayList<>();
        colors.add(Color.AQUA);
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.FUCHSIA);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.LIME);
        colors.add(Color.MAROON);
        colors.add(Color.NAVY);
        colors.add(Color.OLIVE);
        colors.add(Color.ORANGE);
        colors.add(Color.PURPLE);
        colors.add(Color.RED);
        colors.add(Color.SILVER);
        colors.add(Color.TEAL);
        colors.add(Color.WHITE);
        colors.add(Color.YELLOW);
    }

    @Override
    public void play(Entity target) {
        BukkitRunnable runnable = new BukkitRunnable() {
            Random rnd = new Random();

            @Override
            public void run() {
                if (target == null || target.isOnGround() || target.isDead()) {
                    cancel();
                }
                double random = Math.random();
                if (random >= 0.5) {
                    for (int i = 0; i < 2; i++) {
                        ParticleEffect.NOTE.display(new ParticleEffect.NoteColor(rnd.nextInt(25)),
                                target.getLocation().clone().add(Math.random() + 0.2, Math.random(), Math.random() + 0.2), 256);
                    }
                } else {
                    for (int i = 0; i < 2; i++) {
                        ParticleEffect.NOTE.display(new ParticleEffect.NoteColor(rnd.nextInt(25)),
                                target.getLocation().clone().add(Math.random() - 0.2, Math.random(), Math.random() - 0.2), 256);
                    }
                }
            }

        };
        runnable.runTaskTimer(Uhcffa.instance(), 2, 2);
    }

}