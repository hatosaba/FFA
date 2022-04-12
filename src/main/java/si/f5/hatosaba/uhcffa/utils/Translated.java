package si.f5.hatosaba.uhcffa.utils;

import com.rexcantor64.triton.Triton;
import com.rexcantor64.triton.language.LanguageManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Translated {

    private Triton triton = Triton.get();
    private LanguageManager languageManager = triton.getLanguageManager();

    private final String key;
    private String[] args;

    public Translated(String key) {
        this.key = key;
    }

    public static Translated key(String key) {
        return new Translated(key);
    }

    public Translated args(String... args) {
        this.args = args;
        return this;
    }

    public String get(Player player) {
        if (args == null)
            return languageManager.getText(triton.getPlayerManager().get(player.getUniqueId()), key);
        else
            return languageManager.getText(triton.getPlayerManager().get(player.getUniqueId()), key, args);

    }

    public String get(String playerID) {
        final Player player = PlayerConverter.getPlayer(playerID);
        if (args == null)
            return languageManager.getText(triton.getPlayerManager().get(player.getUniqueId()), key);
        else
            return languageManager.getText(triton.getPlayerManager().get(player.getUniqueId()), key, args);
    }
}
