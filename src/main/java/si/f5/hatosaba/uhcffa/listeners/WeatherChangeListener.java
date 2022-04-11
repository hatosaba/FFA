package si.f5.hatosaba.uhcffa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
        if(e.getWorld().isThundering())
            e.getWorld().setThundering(false);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent e) {
        e.setCancelled(true);
        /*if(e.getWorld().isThundering())
            e.getWorld().setThundering(false);*/
    }
}
