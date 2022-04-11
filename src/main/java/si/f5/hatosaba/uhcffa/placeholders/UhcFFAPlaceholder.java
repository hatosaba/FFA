package si.f5.hatosaba.uhcffa.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.uhcffa.Uhcffa;

import java.util.concurrent.ExecutionException;

public class UhcFFAPlaceholder extends PlaceholderExpansion {

    private final Uhcffa plugin;

    public UhcFFAPlaceholder(Uhcffa instance) {
        this.plugin = instance;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "uhcffa";
    }

    @Override
    public @NotNull String getAuthor() {
        return "oieii";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        switch (params) {
            case "":
        }

        // Return null as default because it does not seem to be a valid placeholder.
        return null;
    }

    @Override
    public boolean persist() {
        return true;
    }
}
