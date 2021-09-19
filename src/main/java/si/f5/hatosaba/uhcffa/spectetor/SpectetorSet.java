package si.f5.hatosaba.uhcffa.spectetor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import si.f5.hatosaba.uhcffa.kit.KitManager;

import java.util.HashSet;
import java.util.function.Consumer;

public class SpectetorSet {

    private static SpectetorSet instance;
    private final KitManager kitManager = KitManager.getInstance();

    public static SpectetorSet getInstance(){
        return instance != null ? instance : (instance = new SpectetorSet());
    }

    private final HashSet<Player> hideModePlayers = new HashSet<>();

    //プレイヤーがログインした時
    public void onPlayerJoin(Player player){
        //スペクターモードにする
        applyHideMode(player);
    }

    //プレイヤーがログアウトした時
    public void onPlayerQuit(Player player){
        //非表示モードの使用者リストから削除する
        hideModePlayers.remove(player);
    }

    public boolean isHideMode(Player player){
        return hideModePlayers.contains(player);
    }

    //targetをplayerから非表示にする
    private void show(Player player, Player target){
        player.showPlayer(target);
    }

    //targetをplayerに表示する
    private void hide(Player player, Player target){
        player.hidePlayer(target);
    }

    public void applyShowMode(Player player){
        //全プレイヤーを表示する
        forEachOnlinePlayer((target) -> show(target, player)); // playerをtargetに表示
        forEachHideModeUser(user -> hide(player, user)); // userをplayerから非表示にする

        hideModePlayers.remove(player);
    }

    public void applyHideMode(Player player){
        //スペクターモードのものを非表示にする
        forEachOnlinePlayer(user -> hide(user, player)); // userをplayerから非表示にする
        forEachHideModeUser(user -> show(player, user));

        hideModePlayers.add(player);
    }

    //スペクターモードではない者に対して処理をする
    private void forEachOnlinePlayer(Consumer<Player> processing){
        Bukkit.getOnlinePlayers().stream().filter(player -> !isHideMode(player)).forEach(processing::accept);
    }

    //全スペクターモード使用者に対して処理をする
    private void forEachHideModeUser(Consumer<Player> processing){
        hideModePlayers.stream().forEach(processing::accept);
    }

}
