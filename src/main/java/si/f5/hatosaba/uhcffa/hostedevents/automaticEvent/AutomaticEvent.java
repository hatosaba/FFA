package si.f5.hatosaba.uhcffa.hostedevents.automaticEvent;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.socket.ArenaSocket;
import si.f5.hatosaba.uhcffa.utils.Simple;
import si.f5.hatosaba.uhcffa.utils.TimeService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AutomaticEvent {
    private final Uhcffa plugin;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ZoneId timeZone;
    private ScheduledFuture<?> resetEventTask;
    private ZonedDateTime resetTime;

    private final TimeService.SimpleTimeFormat timeFormat;

    public AutomaticEvent(Uhcffa plugin) {
        this.plugin = plugin;
        this.timeFormat = this.createTimeFormat(plugin.config().getFormat());
        this.timeZone = ZoneId.of(Uhcffa.instance().config().getTimeZone());
        this.resetTime = this.parseTime(this.now().withSecond(0), plugin.config().getStartTime());
    }

    public void startEvent() {
        this.resetEventTask = this.executorService.schedule(() -> {
            this.reset();
            this.startEvent();
            if(Bukkit.getOnlinePlayers().size() >= 5) {
                Bukkit.broadcastMessage("uhcmeetupが開催されています\n/server uhcmeetupで参加しましょう\n次の開催時間は、" + asHourString());
                ArenaSocket.sendMessage("open");
            }
        }, this.timeToHourResetSeconds(), TimeUnit.SECONDS);
    }

    public void stopEvent() {
        this.resetEventTask.cancel(true);
    }

    public void reset() {
        //Bukkit.getPluginManager().callEvent(new DailyQuestsRefreshEvent(this.currentQuests));
    }

    public long timeToHourResetSeconds() {
        if (this.now().until(this.resetTime, ChronoUnit.SECONDS) <= 0) {
            this.resetTime = this.now().plusHours(1);
        }
        return this.now().until(this.resetTime, ChronoUnit.SECONDS);
    }

    public String asHourString() {
        TimeService timeService = Simple.time();
        long timeToReset = this.timeToHourResetSeconds();
        return this.timeFormat == null ? timeService.format(TimeUnit.SECONDS, timeToReset) : timeService.format(this.timeFormat, TimeUnit.SECONDS, timeToReset);
    }


    private ZonedDateTime now() {
        return ZonedDateTime.now().withZoneSameInstant(this.timeZone);
    }

    private ZonedDateTime parseTime(ZonedDateTime date, String time) {
        String[] timeSplit = time.split(":");
        return date.withHour(StringUtils.isNumeric(timeSplit[0]) ? Integer.parseInt(timeSplit[0]) : 0).withMinute(timeSplit.length > 1 ? StringUtils.isNumeric(timeSplit[1]) ? Integer.parseInt(timeSplit[1]) : 0 : 0);
    }

    protected enum AutomaticEventType {

        BRACKETS,
        KOTH,
        LMS,
        JUGGERNAUT,
        SUMO;

    }

    private TimeService.SimpleTimeFormat createTimeFormat(ConfigurationSection lang) {
        return lang.isConfigurationSection("event-time-format") ? new TimeService.SimpleTimeFormat(
                null,
                lang.getString("event-time-format.with-months-weeks"),
                lang.getString("event-time-format.with-weeks-days"),
                lang.getString("event-time-format.with-days-hours"),
                lang.getString("event-time-format.with-hours-minutes"),
                lang.getString("event-time-format.with-minutes-seconds"),
                lang.getString("event-time-format.with-seconds")
        ) : null;
    }
}
