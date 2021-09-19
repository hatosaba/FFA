package si.f5.hatosaba.uhcffa.cosmetics.manager;

import si.f5.hatosaba.uhcffa.cosmetics.trail.effect.*;
import si.f5.hatosaba.uhcffa.user.User;

import java.util.HashMap;
import java.util.Map;

public class TrailManager {

    private static TrailManager instance;

    public static void load() {
        instance = new TrailManager();
    }

    public static TrailManager getInstance() {
        return instance;
    }

    private final Map<String, TrailEffect> effects = new HashMap<>();

    public TrailManager() {
        registerTrailEffect(
                new GreenStarTrail(),
                new HeartsTrail(),
                new NotesTrail(),
                new VanillaTrail(),
                new SlashTrail()
        );
    }

    public void registerTrailEffect (TrailEffect...trailEffects){
        for (TrailEffect trailEffect : trailEffects)
            this.effects.put(trailEffect.getId(), trailEffect);
    }

    public TrailEffect getTrailEffect(User user) {
        return effects.get(String.valueOf(user.purchasedTrail.getSelectedTrail().id));
    }

    public TrailEffect getTrailEffect(int id) {
        return effects.get(String.valueOf(id));
    }
}
