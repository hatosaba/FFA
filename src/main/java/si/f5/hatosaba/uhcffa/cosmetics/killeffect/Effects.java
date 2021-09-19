package si.f5.hatosaba.uhcffa.cosmetics.killeffect;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Effects {

    public final static List<Effect> EFFECTS = new ArrayList<>();

    static {
        initialize(
                "0,0,NONE,BARRIER",
                "1,6000,Head Rocket Kill Effect,SKULL_ITEM",
                "2,6000,Final Smash Kill Effect,ARMOR_STAND",
                "3,15000,Heart Explosion Kill Effect,GOLDEN_APPLE",
                "4,15000,Blood Explosion Kill Effect,REDSTONE"
        );}

    private static void initialize(String... texts){
        Arrays.stream(texts)
                .map(text -> text.split(","))
                .map(data -> new Effect(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3].contains(":") ? new ItemStack(Material.valueOf(data[3].split(":")[0]), 1, Short.parseShort(data[3].split(":")[1])) : new ItemStack(Material.valueOf(data[3]))))
                .forEach(EFFECTS::add);
    }
}

