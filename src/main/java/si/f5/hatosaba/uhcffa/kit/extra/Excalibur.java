package si.f5.hatosaba.uhcffa.kit.extra;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import si.f5.hatosaba.uhcffa.Constants;
import si.f5.hatosaba.uhcffa.schedule.Sync;

import java.util.HashSet;

public class Excalibur implements Listener {

    private final HashSet<Player> cooldownPlayers = new HashSet<>();

    @EventHandler
    public void onAttackExcalibur(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player attacked = (Player) e.getEntity();

            if (Constants.EXCALIBUR.isSimilar(attacker.getItemInHand())) {

                //クールダウン中なら戻る
                if(cooldownPlayers.contains(attacker)) return;

                attacked.getWorld().createExplosion(attacked.getLocation(), 0.0F);

                if(attacked.getHealth() - 4.0D <= 4.0D) {
                    attacked.setHealth(attacked.getHealth() - attacked.getHealth() + 1);
                }else {
                    attacked.setHealth(attacked.getHealth() - 4.0D);
                }

                //クールダウンさせる
                cooldownPlayers.add(attacker);

                //5秒後にクールダウンを完了させる
                Sync.define(() -> cooldownPlayers.remove(attacker)).executeLater(5*20L);
            }
        }
    }
}
