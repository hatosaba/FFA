package si.f5.hatosaba.uhcffa.schedule.task;

import com.lielamar.lielsutils.bukkit.scoreboard.ScoreboardManager;
import com.lielamar.lielsutils.bukkit.scoreboard.ScoreboardUtils;
import com.lielamar.lielsutils.groups.Pair;
import org.bukkit.Bukkit;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.TextUtil;
import si.f5.hatosaba.uhcffa.utils.TextUtils;
import si.f5.hatosaba.uhcffa.utils.Translated;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class UpdateSidebar extends AsyncTask {

    private final Uhcffa uhcffa = Uhcffa.getInstance();

    public UpdateSidebar() {
        super(1);
    }

    @Override
    public void run() {
        UserSet.getInstnace().getOnlineUsers().forEach(user -> {
            ScoreboardManager.Line[] lines;
            final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(PlayerConverter.getID(user.asBukkitPlayer()));

            if (KitManager.getInstance().isSelected(user.asBukkitPlayer())) {
                lines = ScoreboardUtils.assembleScoreboard(TextUtil.getByPlaceholders(Arrays.asList(
                        "&7%date%",
                        "",
                        "Players",
                        "&a%online%&7/200",
                        "",
                        "Kills: &a%pvplevels_kills%",
                        "Streaks: &a%pvplevels_killstreak%",
                        "",
                        "Coins: &6%vault_eco_balance_formatted%",
                        "",
                        "&ehatosaba.f5.si"
                        ), user.asBukkitPlayer()),
                        new Pair<>("%date%", TextUtils.getDate()),
                        new Pair<>("%online%", Bukkit.getOnlinePlayers().size())
                );
            } else if (customPlayer.inArena()) {
                Arena arena = customPlayer.getArena();
                lines = ScoreboardUtils.assembleScoreboard(
                        Arrays.asList(
                                "",
                                Translated.key("duel.opponent").args(arena.getEnemy(customPlayer).getPlayer().getName()).get(customPlayer.getPlayer()),
                                ""
                        )
                );
            } else {
                lines = ScoreboardUtils.assembleScoreboard(TextUtil.getByPlaceholders(Arrays.asList(
                        "&7%date%",
                        "&bYou can challenge",
                        "&bother players by",
                        "&btyping &e/duel <name>",
                        "",
                        "Star: &e%pvplevels_level_prefix%",
                        "",
                        "Killstreak: &a%pvplevels_killstreak%",
                        "Best Killstreak: &a%pvplevels_killstreak_top%",
                        "",
                        "Kills: &a%pvplevels_kills%",
                        "Coins: &6%vault_eco_balance_formatted%",
                        "",
                        "&ehatosaba.f5.si"
                        ), user.asBukkitPlayer()),
                        new Pair<>("%date%", TextUtils.getDate()));
            }

            if (uhcffa.getScoreboardManager().getScoreboard(user.asBukkitPlayer()) == null) return;

            Objects.requireNonNull(uhcffa.getScoreboardManager().getScoreboard(user.asBukkitPlayer())).updateLines(lines);

        });
    }

}
