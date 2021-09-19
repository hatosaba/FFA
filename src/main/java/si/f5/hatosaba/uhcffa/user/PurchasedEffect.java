package si.f5.hatosaba.uhcffa.user;

import si.f5.hatosaba.uhcffa.cosmetics.killeffect.Effect;
import si.f5.hatosaba.uhcffa.cosmetics.killeffect.Effects;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PurchasedEffect {

    private final User user;
    private final Set<Integer> hatIds;
    private Effect selectedEffect;

    public PurchasedEffect(User user, Yaml yaml){
        this.user = user;
        this.hatIds = new HashSet<>(yaml.getIntegerList("Purchased effects ids"));
        this.selectedEffect = Effects.EFFECTS.get(yaml.getInt("selectedEffect"));
    }

    public boolean canBuy(Effect effect){
        return effect.value  <= user.coins().intValue();
    }

    public void buy(Effect effect){
        user.withdrawCoins(BigDecimal.valueOf(effect.value));
        hatIds.add(effect.id);
    }

    public void select(Effect effect) {
        selectedEffect = effect;
    }

    public boolean isSelected(Effect effect) {
        return selectedEffect.equals(effect);
    }

    public Effect getSelectedEffect() {
        return selectedEffect;
    }

    public boolean has(Effect effect){
        return hatIds.contains(effect.id);
    }

    public void save(Yaml yaml){
        yaml.set("Purchased effects ids", new ArrayList<>(hatIds));
        yaml.set("selectedEffect", selectedEffect.id);
    }

}
