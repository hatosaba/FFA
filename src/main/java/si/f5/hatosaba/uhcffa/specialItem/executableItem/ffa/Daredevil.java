package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Daredevil extends ExecutableItem {

    public Daredevil(String id) {
        super(id);
    }

    public static List<Horse> horses = new LinkedList<>();

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.of(Material.MONSTER_EGG)
                        .name("&aDaredevil")
                        .lore(Arrays.asList(
                                "&aType: Skeleton Horse",
                                "&aHealth: 25 Hearts",
                                "&aSpeed: &7Max",
                                "&aJump: &73blocks",
                                "&eComes with free saddle"
                        ))
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        final Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);

        horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999 * 20, 2));
        horse.setAdult();
        horse.setAgeLock(true);
        horse.setTamed(true);
        horse.setJumpStrength(0.7);
        horse.setMaxHealth(50);
        horse.setHealth(horse.getMaxHealth());
        horse.setVariant(Horse.Variant.SKELETON_HORSE);
        horse.setOwner(player);
        horse.getInventory().setSaddle(ItemBuilder.of(Material.SADDLE).build());

        ((EntityLiving)((CraftEntity)horse).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.699999988079071D / 2);

        horses.add(horse);

        player.setItemInHand(ItemBuilder.of(Material.AIR).build());
        event.setCancelled(true);
    }
}
