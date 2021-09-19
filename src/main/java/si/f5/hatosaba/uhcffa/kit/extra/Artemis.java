package si.f5.hatosaba.uhcffa.kit.extra;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.schedule.task.HomingTask;

import java.util.ArrayList;
import java.util.Random;

public class Artemis implements Listener {

    @EventHandler
    public void eventArrowFired(EntityShootBowEvent e) {
        ArrayList<Integer> chancelist = new ArrayList<>();
        chancelist.add(Integer.valueOf(1));
        chancelist.add(Integer.valueOf(2));
        chancelist.add(Integer.valueOf(3));
        chancelist.add(Integer.valueOf(4));
        Random random = new Random();
        int choice = random.nextInt(chancelist.size());
        int finalchoice = chancelist.get(choice).intValue();
        LivingEntity player = e.getEntity();
        if (finalchoice == 4 &&
                e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow && Constants.ARTEMIS.isSimilar(player.getEquipment().getItemInHand())) {
            {
                double minAngle = 6.283185307179586D;
                Entity minEntity = null;
                for (Entity entity : player.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
                    if (player.hasLineOfSight(entity) && entity instanceof LivingEntity &&
                            !entity.isDead()) {
                        Vector toTarget = entity.getLocation().toVector().clone()
                                .subtract(player.getLocation().toVector());
                        double angle = e.getProjectile().getVelocity().angle(toTarget);
                        if (angle < minAngle) {
                            minAngle = angle;
                            minEntity = entity;
                        }
                    }
                }
                if (minEntity != null) {
                    new HomingTask((Arrow)e.getProjectile(), (LivingEntity) minEntity, Uhcffa.instance());
                }
            }
        }
    }
}