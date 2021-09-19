package si.f5.hatosaba.uhcffa.ranking.workers.reset;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.Simple;
import si.f5.hatosaba.uhcffa.utils.TimeService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RankingReset {
    private final Uhcffa plugin;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ZoneId timeZone;
    private ScheduledFuture<?> resetDailyTask;
    private ScheduledFuture<?> resetWeeklyTask;
    private ZonedDateTime resetTime;

    private final TimeService.SimpleTimeFormat timeFormat;


    public RankingReset(Uhcffa plugin) {
        this.plugin = plugin;
        this.timeFormat = this.createTimeFormat(plugin.config().getFormat());
        this.timeZone = ZoneId.of(Uhcffa.instance().config().getTimeZone());
        this.resetTime = this.parseTime(this.now().withSecond(0), plugin.config().getResetTime());
    }

    public void startDaily() {
        this.resetDailyTask = this.executorService.schedule(() -> {
            this.reset();
            this.startDaily();
            //ここにブロードキャスト
        }, this.timeToDailyResetSeconds(), TimeUnit.SECONDS);
    }

    public void startWeekly() {
        this.resetWeeklyTask = this.executorService.schedule(() -> {
            this.reset();
            this.startWeekly();
            //ここにブロードキャスト
        }, this.timeToWeeklyResetSeconds(), TimeUnit.SECONDS);
    }


    public void stopDaily() {
        this.resetDailyTask.cancel(true);
    }

    public void stopWeekly() {
        this.resetWeeklyTask.cancel(true);
    }

    public void reset() {

        //Bukkit.getPluginManager().callEvent(new DailyQuestsRefreshEvent(this.currentQuests));
    }

    public long timeToDailyResetSeconds() {
        if (this.now().until(this.resetTime, ChronoUnit.SECONDS) <= 0) {
            this.resetTime = this.resetTime.plusDays(1);
        }
        return this.now().until(this.resetTime, ChronoUnit.SECONDS);
    }

    public long timeToWeeklyResetSeconds() {
        if (this.now().until(this.resetTime, ChronoUnit.SECONDS) <= 0) {
            this.resetTime = this.resetTime.plusWeeks(1);
        }
        return this.now().until(this.resetTime, ChronoUnit.SECONDS);
    }

    public String asDailyString() {
        TimeService timeService = Simple.time();
        long timeToReset = this.timeToDailyResetSeconds();
        return this.timeFormat == null ? timeService.format(TimeUnit.SECONDS, timeToReset) : timeService.format(this.timeFormat, TimeUnit.SECONDS, timeToReset);
    }

    public String asWeeklyString() {
        TimeService timeService = Simple.time();
        long timeToReset = this.timeToWeeklyResetSeconds();
        return this.timeFormat == null ? timeService.format(TimeUnit.SECONDS, timeToReset) : timeService.format(this.timeFormat, TimeUnit.SECONDS, timeToReset);
    }

    private ZonedDateTime now() {
        return ZonedDateTime.now().withZoneSameInstant(this.timeZone);
    }

    private ZonedDateTime parseTime(ZonedDateTime date, String time) {
        String[] timeSplit = time.split(":");
        return date.withHour(StringUtils.isNumeric(timeSplit[0]) ? Integer.parseInt(timeSplit[0]) : 0).withMinute(timeSplit.length > 1 ? StringUtils.isNumeric(timeSplit[1]) ? Integer.parseInt(timeSplit[1]) : 0 : 0);
    }

    private TimeService.SimpleTimeFormat createTimeFormat(ConfigurationSection lang) {
        return lang.isConfigurationSection("ranking-time-format") ? new TimeService.SimpleTimeFormat(
                null,
                lang.getString("ranking-time-format.with-months-weeks"),
                lang.getString("ranking-time-format.with-weeks-days"),
                lang.getString("ranking-time-format.with-days-hours"),
                lang.getString("ranking-time-format.with-hours-minutes"),
                lang.getString("ranking-time-format.with-minutes-seconds"),
                lang.getString("ranking-time-format.with-seconds")
        ) : null;
    }
}
