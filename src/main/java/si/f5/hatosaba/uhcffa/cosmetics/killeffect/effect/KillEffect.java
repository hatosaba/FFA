package si.f5.hatosaba.uhcffa.cosmetics.killeffect.effect;

import org.bukkit.entity.Player;

public abstract class KillEffect {

    protected String id;

    public KillEffect(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void play(Player target);
}
