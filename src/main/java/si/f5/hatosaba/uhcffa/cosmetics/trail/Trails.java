package si.f5.hatosaba.uhcffa.cosmetics.trail;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.cosmetics.killeffect.Effect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trails {

    public final static List<Trail> TRAILS = new ArrayList<>();

    static {
        initialize(
                "0,0,Vanilla Trail,SAND",
                "1,1000,Black Smoke Trail,INK_SACK:8",
                "2,1000,White Smoke Trail,INK_SACK:8",
                "3,1000,Fire Trail,INK_SACK:8",
                "4,1000,Blood Trail,INK_SACK:8",
                "5,1000,Magic Trail,INK_SACK:8",
                "6,1000,Slime Trail,INK_SACK:8",
                "7,1000,Ender Trail,INK_SACK:8",
                "8,3000,Green Star Trail,EMERALD",
                "9,5500,Hearts Trail,GOLDEN_APPLE",
                "10,6000,Notes Trail,NOTE_BLOCK"
        );}

    private static void initialize(String... texts){
        Arrays.stream(texts)
                .map(text -> text.split(","))
                .map(data -> new Trail(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3].contains(":") ? new ItemStack(Material.valueOf(data[3].split(":")[0]), 1, Short.parseShort(data[3].split(":")[1])) : new ItemStack(Material.valueOf(data[3]))))
                .forEach(TRAILS::add);
    }
}
