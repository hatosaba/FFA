package si.f5.hatosaba.uhcffa.user;

import si.f5.hatosaba.uhcffa.cosmetics.trail.Trail;
import si.f5.hatosaba.uhcffa.cosmetics.trail.Trails;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PurchasedTrail {

    private final User user;
    private final Set<Integer> hatIds;
    private Trail selectedTrail;

    public PurchasedTrail(User user, Yaml yaml){
        this.user = user;
        this.hatIds = new HashSet<>(yaml.getIntegerList("Purchased trails ids"));
        this.selectedTrail = Trails.TRAILS.get(yaml.getInt("selectedTrail"));
    }

    public boolean canBuy(Trail effect){
        return effect.value  <= user.coins().intValue();
    }

    public void buy(Trail effect){
        user.withdrawCoins(BigDecimal.valueOf(effect.value));
        hatIds.add(effect.id);
    }

    public void select(Trail trail) {
        selectedTrail = trail;
    }

    public boolean isSelected(Trail trail) {
        return selectedTrail.equals(trail);
    }

    public Trail getSelectedTrail() {
        return selectedTrail;
    }

    public boolean has(Trail trail){
        return hatIds.contains(trail.id);
    }

    public void save(Yaml yaml){
        yaml.set("Purchased trails ids", new ArrayList<>(hatIds));
        yaml.set("selectedTrail", selectedTrail.id);
    }

}