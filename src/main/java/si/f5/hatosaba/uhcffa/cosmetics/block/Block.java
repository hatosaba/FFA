package si.f5.hatosaba.uhcffa.cosmetics.block;

import org.bukkit.inventory.ItemStack;

public class Block {

    public final int id;
    public final int value;
    public final ItemStack item;

    public Block(int id, int value, ItemStack item){
        this.id = id;
        this.value = value;
        this.item = item;
    }

    @Override
    public int hashCode(){
        return id;
    }

}
