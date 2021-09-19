package si.f5.hatosaba.uhcffa.cosmetics.killeffect.effect;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BloodExplosion extends KillEffect {

    public BloodExplosion() {
        super("4");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void play(Player p) {
        Location loc = p.getLocation();
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.playEffect(loc.clone().add(0, 0.5, 0), Effect.STEP_SOUND, 152);
            all.playEffect(loc.clone().add(0, 1, 0), Effect.STEP_SOUND, 152);
        }
    }

}