package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

public class FlaskOfIchor extends ExecutableItem {

    public FlaskOfIchor(String id) {
        super(id);
    }

    @Override
    public ItemStack buildItemStack () {
        ItemStack item = new ItemStack(Material.POTION,1);
        Potion potion = new Potion(PotionType.INSTANT_DAMAGE);
        PotionMeta meta = (PotionMeta) item.getItemMeta();

        potion.setSplash(true);
        potion.setLevel(2);
        potion.apply(item);

        meta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 2), true);
        item.setItemMeta(meta);

        return define(ItemBuilder.modify(item).name("Flask of Ichor").build());
    }
}
