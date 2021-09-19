package si.f5.hatosaba.uhcffa.cosmetics.trail.effect;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;

public abstract class TrailEffect {

    protected String id;

    public TrailEffect(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void play(Entity target);

}
