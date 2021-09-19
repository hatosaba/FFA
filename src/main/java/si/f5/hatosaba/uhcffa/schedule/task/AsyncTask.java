package si.f5.hatosaba.uhcffa.schedule.task;

import org.bukkit.scheduler.BukkitTask;
import si.f5.hatosaba.uhcffa.schedule.Async;

public abstract class AsyncTask {

    private BukkitTask task;
    private final long interval;

    public AsyncTask(long interval){
        this.interval = interval;
    }

    public void start(){
        cancel();
        task = Async.define(() -> run()).executeTimer(interval);
    }

    public void cancel(){
        if(task != null) task.cancel();

        task = null;
    }

    public abstract void run();

}

