package si.f5.hatosaba.uhcffa.cosmetics.block;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blocks {

    public final static List<Block> BLOCKS = new ArrayList<>();

    static {
        initialize(
                "0,0,COBBLESTONE",
                "1,1000,STONE",
                "2,1000,WOOL",
                "3,1000,WOOL:1",
                "4,1000,WOOL:2",
                "5,1000,WOOL:3",
                "6,1000,WOOL:4",
                "7,1000,WOOL:5",
                "8,1000,WOOL:6",
                "9,1000,WOOL:7",
                "10,1000,WOOL:8",
                "11,1000,WOOL:9",
                "12,1000,WOOL:10",
                "13,1000,WOOL:11",
                "14,1000,WOOL:12",
                "15,1000,WOOL:13",
                "16,1000,WOOL:14",
                "17,1000,WOOL:15",
                "18,1000,WOOD",
                "19,1000,WOOD:1",
                "20,1000,WOOD:2",
                "21,1000,WOOD:3",
                "22,1000,WOOD:4",
                "23,1000,WOOD:5",
                "24,1000,LOG",
                "25,1000,LOG:1",
                "26,1000,LOG:2",
                "27,1000,LOG:3",
                "30,1000,COAL_ORE",
                "31,1000,IRON_ORE",
                "32,1000,LAPIS_ORE",
                "33,1000,REDSTONE_ORE",
                "34,1000,GOLD_ORE",
                "35,1000,DIAMOND_ORE",
                "36,1000,EMERALD_ORE",
                "37,1000,COAL_BLOCK",
                "38,1000,IRON_BLOCK",
                "39,1000,LAPIS_BLOCK",
                "40,1000,REDSTONE_BLOCK",
                "41,1000,GOLD_BLOCK",
                "42,1000,DIAMOND_BLOCK",
                "43,1000,EMERALD_BLOCK",
                "44,1000,SNOW_BLOCK",
                "45,1000,NETHERRACK",
                "46,1000,ENDER_STONE",
                "47,1000,BRICK",
                "48,1000,QUARTZ_BLOCK",
                "49,1000,STAINED_CLAY",
                "50,1000,STAINED_CLAY:1",
                "51,1000,STAINED_CLAY:2",
                "52,1000,STAINED_CLAY:3",
                "53,1000,STAINED_CLAY:4",
                "54,1000,STAINED_CLAY:5",
                "55,1000,STAINED_CLAY:6",
                "56,1000,STAINED_CLAY:7",
                "57,1000,STAINED_CLAY:8",
                "58,1000,STAINED_CLAY:9",
                "59,1000,STAINED_CLAY:10",
                "60,1000,STAINED_CLAY:11",
                "61,1000,STAINED_CLAY:12",
                "62,1000,STAINED_CLAY:13",
                "63,1000,STAINED_CLAY:14",
                "64,1000,STAINED_CLAY:15",
                "65,1000,LEAVES",
                "66,1000,LEAVES:1",
                "67,1000,LEAVES:2",
                "68,1000,LEAVES:3",
                "69,1000,LEAVES",
                "70,1000,LEAVES:1"
        );}

    private static void initialize(String... texts){
        Arrays.stream(texts)
                .map(text -> text.split(","))
                .map(data -> new Block(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2].contains(":") ? new ItemStack(Material.valueOf(data[2].split(":")[0]), 1, Short.parseShort(data[2].split(":")[1])) : new ItemStack(Material.valueOf(data[2]))))
                .forEach(BLOCKS::add);
    }
}
