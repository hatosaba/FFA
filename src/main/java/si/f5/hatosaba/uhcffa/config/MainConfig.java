package si.f5.hatosaba.uhcffa.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

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
            String[] split = spawnPoint.split(":");
            Location location = new Location(
                    Bukkit.getWorld(split[0]),
                    Double.valueOf(split[1]),
                    Double.valueOf(split[2]),
                    Double.valueOf(split[3]),
                    Float.valueOf(split[4]),
                    Float.valueOf(split[5])
            );
            spawnPoints.add(location);
        }

        String locText = config.getString("editroom");
        String[] split = locText.split(":");
        Location location = new Location(
                Bukkit.getWorld(split[0]),
                Double.valueOf(split[1]),
                Double.valueOf(split[2]),
                Double.valueOf(split[3]),
                Float.valueOf(split[4]),
                Float.valueOf(split[5])
        );
        this.location = location;

        String lobbyLocText = config.getString("lobby");
        String[] splitLobby = lobbyLocText.split(":");
        Location lobbyLoc = new Location(
                Bukkit.getWorld(splitLobby[0]),
                Double.valueOf(splitLobby[1]),
                Double.valueOf(splitLobby[2]),
                Double.valueOf(splitLobby[3]),
                Float.valueOf(splitLobby[4]),
                Float.valueOf(splitLobby[5])
        );
        this.lobby = lobbyLoc;

        this.timeZone = config.getString("current-season.time-zone");
        this.resetTime = config.getString("current-season.reset-time");
        this.format = config.getConfigurationSection("ranking-time-format");

        this.startTime = config.getString("automatic-event.start-time");
    }

    public void saveAll() {
        FileConfiguration config = config();
        config.set("spawnpoints", spawnPoints.stream()
                .map(loc ->
                        loc.getWorld().getName() +
                                ":" +
                                loc.getX() +
                                ":" +
                                loc.getY() +
                                ":" +
                                loc.getZ() +
                                ":" +
                                loc.getYaw() +
                                ":" +
                                loc.getPitch()
                        )
                .collect(Collectors.toList()));

        config.set("editroom",
                location.getWorld().getName() +
                ":" +
                location.getX() +
                ":" +
                location.getY() +
                ":" +
                location.getZ() +
                ":" +
                location.getYaw() +
                ":" +
                location.getPitch()
        );

        config.set("lobby",
                lobby.getWorld().getName() +
                        ":" +
                        lobby.getX() +
                        ":" +
                        lobby.getY() +
                        ":" +
                        lobby.getZ() +
                        ":" +
                        lobby.getYaw() +
                        ":" +
                        lobby.getPitch()
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
