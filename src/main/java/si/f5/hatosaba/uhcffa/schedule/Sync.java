package si.f5.hatosaba.uhcffa.schedule;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import si.f5.hatosaba.uhcffa.Uhcffa;

public interface Sync extends Runnable {

    public static Sync define(Sync sync){
        return sync;
    }

    public default void execute(){
        Bukkit.getScheduler().runTask(Uhcffa.instance(), this);
    }

    public default void executeLater(long delay){
        Bukkit.getScheduler().runTaskLater(Uhcffa.instance(), this, delay);
    }

    public default BukkitTask executeTimer(long period, long delay){
        return Bukkit.getScheduler().runTaskTimer(Uhcffa.instance(), this, period, delay);
    }

    public default BukkitTask executeTimer(long interval){
        return executeTimer(interval, interval);
    }

}
