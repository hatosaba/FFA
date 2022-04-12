package si.f5.hatosaba.uhcffa.schedule;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import si.f5.hatosaba.uhcffa.Uhcffa;

public interface Async extends Runnable {

    public static Async define(Async async){
        return async;
    }

    public default BukkitTask execute(){
        return Bukkit.getScheduler().runTaskAsynchronously(Uhcffa.getInstance(), this);
    }

    public default BukkitTask executeLater(long delay){
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Uhcffa.getInstance(), this, delay);
    }

    public default BukkitTask executeTimer(long period, long delay) {
        return Bukkit.getScheduler().runTaskTimer(Uhcffa.getInstance(), this, period, delay);
    }

    public default BukkitTask executeTimer(long interval){
        return executeTimer(interval, interval);
    }

    /*public default BukkitTask executeTimer(long period, long delay){
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Uhcffa.getInstance(), this, period, delay);
    }

    public default BukkitTask executeTimer(long interval){
        return executeTimer(interval, interval);
    }*/

}
