package si.f5.hatosaba.uhcffa.cosmetics.killeffect.effect;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.ParticleEffect;

public class HeadRocket extends KillEffect {

    public HeadRocket() {
        super("1");
    }

    @Override
    public void play(Player p) {
        Location loc = p.getLocation();
        World world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityArmorStand entityArmorStand = new EntityArmorStand(world);
        entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(),
                MathHelper.d((entityArmorStand.pitch * 256F) / 360F),
                MathHelper.d((entityArmorStand.yaw * 256F) / 360F));
        entityArmorStand.setInvisible(true);
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(p.getName());
        skull.setItemMeta(skullMeta);
        net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(skull);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
        PacketPlayOutEntityEquipment packetPlayOutEntityEquipment = new PacketPlayOutEntityEquipment(entityArmorStand.getId(), 4,
                nmsItemStack);
        for (Player all : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityEquipment);
        }
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 20;
            Location lastPos = new Location(loc.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if (i > 0) {
                    entityArmorStand.locY += 0.5;
                    Location pos = new Location(loc.getWorld(), entityArmorStand.locX, entityArmorStand.locY, entityArmorStand.locZ);
                    if (pos.getBlock().getType() == Material.AIR) {
                        PacketPlayOutEntityHeadRotation packetPlayOutEntityHeadRotation = new PacketPlayOutEntityHeadRotation(
                                entityArmorStand,
                                (byte) MathHelper.floor(((entityArmorStand.yaw += 10) * 256.0F) / 360.0F));
                        PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = new PacketPlayOutEntityTeleport(entityArmorStand);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityTeleport);
                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityHeadRotation);
                        }
                        ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, pos, 256f);
                        lastPos = pos;
                        i--;
                    } else {
                        i = 0;
                    }
                    if (i == 0) {
                        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(entityArmorStand.getId());
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                            all.playEffect(lastPos, Effect.STEP_SOUND, 152);
                        }
                        cancel();
                    }
                }
            }

        };
        runnable.runTaskTimer(Uhcffa.instance(), 1, 1);
    }
}
