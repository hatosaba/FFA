package si.f5.hatosaba.uhcffa.user;

import si.f5.hatosaba.uhcffa.cosmetics.block.Block;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PurchasedKit {

    private final User user;
    private final Set<Integer> hatIds;

    public PurchasedKit(User user, Yaml yaml){
        this.user = user;
        this.hatIds = new HashSet<>(yaml.getIntegerList("Purchased blocks ids"));
    }

    public boolean canBuy(Block block){
        return block.value  <= user.coins().intValue();
    }

    public void buy(Block block){
        user.withdrawCoins(BigDecimal.valueOf(block.value));
        hatIds.add(block.id);
    }

    public boolean has(Block block){
        return hatIds.contains(block.id);
    }

    public void save(Yaml yaml){
        yaml.set("Purchased blocks ids", new ArrayList<>(hatIds));
    }

}
