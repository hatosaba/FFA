package si.f5.hatosaba.uhcffa.cosmetics.manager;

import si.f5.hatosaba.uhcffa.cosmetics.killeffect.effect.*;
import si.f5.hatosaba.uhcffa.user.User;

import java.util.HashMap;
import java.util.Map;

public class KillManager {

    private static KillManager instance;

    public static void load() {
        instance = new KillManager();
    }

    public static KillManager getInstance() {
        return instance;
    }

    private final Map<String, KillEffect> effects = new HashMap<>();

    public KillManager() {
        registerKillEffect(
                new BloodExplosion(),
                new HeartExplosion(),
                new FinalSmash(),
                new HeadRocket(),
                new SlimeExplosion()
        );
    }

    public void registerKillEffect (KillEffect...killEffects){
        for (KillEffect killEffect : killEffects)
            this.effects.put(killEffect.getId(), killEffect);
    }

    public KillEffect getKillEffect(User user) {
        return effects.get(String.valueOf(user.purchasedEffect.getSelectedEffect().id));
    }

    public KillEffect getKillEffect(int id) {
        return effects.get(String.valueOf(id));
    }
}
