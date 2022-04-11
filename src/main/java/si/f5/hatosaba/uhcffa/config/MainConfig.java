package si.f5.hatosaba.uhcffa.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import si.f5.hatosaba.uhcffa.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainConfig extends Config {

    private final List<Location> spawnPoints = new ArrayList<>();
    private Location location;
    private Location lobby;
    private ConfigurationSection format;
    private String resetTime;
    private String timeZone;
    private String startTime;

    public MainConfig(){
        super("config.yml");
        load();
    }

    @Override
    public void load() {
        FileConfiguration config = config();

        spawnPoints.clear();

        for (String spawnPoint : config.getStringList("spawnpoints")) {
            Location location = LocationUtil.stringToLocation(spawnPoint);
            spawnPoints.add(location);
        }

        String locText = config.getString("editroom");
        String[] split = locText.split(":");
        this.location = LocationUtil.stringToLocation(locText);

        String lobbyLocText = config.getString("lobby");
        this.lobby = LocationUtil.stringToLocation(lobbyLocText);

        this.timeZone = config.getString("current-season.time-zone");
        this.resetTime = config.getString("current-season.reset-time");
        this.format = config.getConfigurationSection("ranking-time-format");

        this.startTime = config.getString("automatic-event.start-time");
    }

    public void saveAll() {
        FileConfiguration config = config();
        config.set("spawnpoints", spawnPoints.stream()
                .map(LocationUtil::locationToString
                        )
                .collect(Collectors.toList()));

        config.set("editroom",
                LocationUtil.locationToString(location)
        );

        config.set("lobby",
                LocationUtil.locationToString(lobby)
        );

        update();
    }

    public Location getLocation() {
        return location;
    }

    public Location getLobby() {
        return lobby;
    }

    public ConfigurationSection getFormat() {
        return format;
    }

    public String getResetTime() {
        return resetTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setFormat(ConfigurationSection format) {
        this.format = format;
    }

    public void setResetTime(String resetTime) {
        this.resetTime = resetTime;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

}
