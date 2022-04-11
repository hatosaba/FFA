package si.f5.hatosaba.uhcffa.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEntityEvent {

    private final EntityDamageByEntityEvent event;

    public PlayerDamageEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) throw new IllegalArgumentException("The damager must be an instance of Player");

        this.event = event;
    }

    public Player damager() {
        return (Player) event.getDamager();
    }

    public Entity damagee() {
        return event.getEntity();
    }

    public double damage() {
        return event.getDamage();
    }

    public void setDamage(double damage) {
        event.setDamage(damage);
    }

    public double damage(EntityDamageEvent.DamageModifier type) {
        return event.getDamage(type);
    }

    public void setDamage(EntityDamageEvent.DamageModifier type, double damage) {
        event.setDamage(type, damage);
    }

    public EntityDamageEvent.DamageCause cause() {
        return event.getCause();
    }

    public double finalDamage() {
        return event.getFinalDamage();
    }

    public double originalDamage(EntityDamageEvent.DamageModifier type) {
        return event.getOriginalDamage(type);
    }

    public boolean isApplicable(EntityDamageEvent.DamageModifier type) {
        return event.isApplicable(type);
    }

    public boolean isCancelled() {
        return event.isCancelled();
    }

    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }


}
