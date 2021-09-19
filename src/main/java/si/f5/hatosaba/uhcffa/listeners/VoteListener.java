package si.f5.hatosaba.uhcffa.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import si.f5.hatosaba.uhcffa.user.User;
import si.f5.hatosaba.uhcffa.user.UserSet;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class VoteListener implements Listener {

    private final UserSet users = UserSet.getInstnace();

    @EventHandler
    public void onVote(VotifierEvent event) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        Vote vote = event.getVote();
        String serviceName = vote.getServiceName();
        String playerName = vote.getUsername();
        @SuppressWarnings("deprecation")
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

        UserSet.getInstnace().getOnlineUsers().forEach(user -> {
            user.depositCoins(BigDecimal.valueOf(150));
            user.asBukkitPlayer().sendMessage(new String[]{
                    DARK_GREEN + "------------------------------------",
                    GREEN + playerName + GRAY + "さんが" + GREEN + serviceName + GRAY + "で投票したことによりコインが" + YELLOW + "150 " + GRAY + "枚もらえました！",
                    GRAY + "投票はこちらから",
                    WHITE + "https://minecraft.jp/servers/hatosaba.f5.si/vote",
                    DARK_GREEN + "------------------------------------"
            });
        });

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + playerName + "permission settemp ffa.vote true 1d");

        //サーバーに接続した事のないプレイヤーであれば戻る
        if (player == null || !player.hasPlayedBefore()) return;

        UUID uuid = player.getUniqueId();

        //ユーザーデータが存在しなければ戻る
        if (!users.containsUser(uuid)) return;

        //ユーザーデータを取得する
        User user = users.getUser(uuid);

        user.depositCoins(BigDecimal.valueOf(350));

        user.asBukkitPlayer().sendMessage(new String[]{
                GREEN + "投票ありがとうございます。",
                GRAY + "コイン" + YELLOW + "500" + GRAY + "枚を与えました！"
        });
    }
}
