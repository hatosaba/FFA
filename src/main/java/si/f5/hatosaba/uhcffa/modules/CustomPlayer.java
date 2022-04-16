package si.f5.hatosaba.uhcffa.modules;

import com.rexcantor64.triton.Triton;
import com.rexcantor64.triton.player.LanguagePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaManager;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableManager;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomPlayer {

    private final String playerID;

    private User user;

    private final LanguagePlayer languagePlayer;

    private final List<Kit> editedKits = new CopyOnWriteArrayList<>();
    private Preset preset;

    private SetupData setupData;

    private final Map<String, ArenaManager.Request> requests = new HashMap<>();
    private final Map<String, BukkitRunnable> unrequests = new HashMap<>();

    private Arena arena = null;

    private String lang; // the player's language

    public CustomPlayer(final String playerID) {
        this.playerID = playerID;
        this.user = UserSet.getInstnace().getUser(PlayerConverter.getPlayer(playerID));
        this.languagePlayer = Triton.get().getPlayerManager().get(UUID.fromString(playerID));
        loadAllPlayerData();
    }

    public final void loadAllPlayerData() {
    }

    public void sendTranslated(String key) {
        PlayerConverter.getPlayer(playerID).sendMessage(Triton.get().getLanguageManager().getText(languagePlayer, key));
    }

    public void sendTranslated(String key, String... args) {
        PlayerConverter.getPlayer(playerID).sendMessage(Triton.get().getLanguageManager().getText(languagePlayer, key, args));
    }

    public boolean inArena() {
        return this.arena != null;
    }

    public void addRequest(ArenaManager.Request request) {
        final String playerID = request.getPlayerID();
        final String toPlayerID = request.getToPlayerID();

        requests.put(playerID, request);
        final BukkitRunnable run = unrequests.get(playerID);
        if (run != null) {
            run.cancel();
        }
        unrequests.put(playerID, new BukkitRunnable() {
            @Override
            public void run() {
                final Player player = PlayerConverter.getPlayer(playerID);
                final Player toPlayer = PlayerConverter.getPlayer(toPlayerID);
                removeRequest(playerID);
                SoundEffects.FAILED.play(player);
                player.sendMessage("リクエストが無効になりました");
                toPlayer.sendMessage(player.getName() + "からのリクエストが無効になりました");
            }
        });
        unrequests.get(playerID).runTaskLater(Uhcffa.getInstance(), 30 * 20);
    }

    public void cancelRequest() {
        sendTranslated("request.cancel");
        requests.forEach((s, request) -> {
            CustomPlayer requester = Uhcffa.getCustomPlayer(s);
            requester.sendTranslated("request.cancel");
        });
        unrequests.forEach((s, bukkitRunnable) -> {
            if (bukkitRunnable != null) {
                bukkitRunnable.cancel();
            }
        });
        requests.clear();
    }

    public void removeRequest(String playerID) {
        requests.remove(playerID);
        final BukkitRunnable runnable = unrequests.remove(playerID);
        if (runnable != null) {
            runnable.cancel();
        }
    }

    public String getPlayerID() {
        return playerID;
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return PlayerConverter.getPlayer(playerID);
    }

    public ArenaManager.Request getRequest(String playerID) {
        return requests.get(playerID);
    }

    public Map<String, ArenaManager.Request> getRequests() {
        return requests;
    }

    public boolean isBusy() {
        return arena != null;
    }

    public void setSetupData() {
        setupData = new SetupData();
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setPreset() {
        preset = new Preset(playerID);
    }

    public Preset getPreset() {
        return preset;
    }

    public String getLanguage() {
        return lang;
    }

    public SetupData getSetupData() {
        return setupData;
    }

    public boolean isAlreadyRequested(String playerID) {
        return requests.containsKey(playerID);
    }

    public class Preset {

        private final String playerID;

        private final ItemStack[] armor;
        private final ItemStack[] inv;


        public Preset(final String playerID) {
            final Player player = PlayerConverter.getPlayer(playerID);

            for (ExecutableItemType type : ExecutableItemType.values()) {
                for (ItemStack item : ExecutableManager.withoutDefaultKit) {
                    if (type.getItem() != item) continue;
                    player.getInventory().remove(item);
                }
            }
            player.getInventory().remove(Material.SKULL_ITEM);
            player.getInventory().remove(Material.REDSTONE);

            this.playerID = playerID;
            this.armor = player.getEquipment().getArmorContents();
            this.inv = player.getInventory().getContents();
        }

        public void applyContent() {
            final PlayerInventory inv = PlayerConverter.getPlayer(playerID).getInventory();

            inv.clear();
            inv.setArmorContents(this.armor);
            inv.setContents(this.inv);
        }

        public ItemStack[] getArmor() {
            return armor;
        }

        public ItemStack[] getInv() {
            return inv;
        }
    }

    public class SetupData {
        private String name;

        private double maxBuildY = 0;

        private Location spawn1;
        private Location spawn2;

        public String getName() {
            return name;
        }

        public double getMaxBuildY() {
            return maxBuildY;
        }

        public Location getSpawn1() {
            return spawn1;
        }

        public Location getSpawn2() {
            return spawn2;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMaxBuildY(double maxBuildY) {
            this.maxBuildY = maxBuildY;
        }

        public void setSpawn1(Location spawn1) {
            this.spawn1 = spawn1;
        }

        public void setSpawn2(Location spawn2) {
            this.spawn2 = spawn2;
        }

        public boolean compile() {
            return this.maxBuildY != 0 && this.name != null && this.spawn1 != null && this.spawn2 != null;
        }
    }
}
