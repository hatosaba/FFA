package si.f5.hatosaba.uhcffa.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class UserSet {

    private static UserSet instance;

    public static void load(){
        instance = new UserSet();
    }

    public static UserSet getInstnace(){
        return instance;
    }

    private final Uhcffa plugin = Uhcffa.getInstance();

    //ユーザーデータを保存するフォルダー
    public final File folder = new File(plugin.getDataFolder() + File.separator + "Users");

    private final Map<UUID, User> users = new HashMap<>();

    private UserSet(){
        //フォルダーが存在しなければ作成する
        if(!folder.exists()) folder.mkdirs();

        for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
            //ファイルをコンフィグとして読み込む
            Yaml yaml = new Yaml(plugin, file, "user.yml");

            //コンフィグを基にユーザーを生成する
            User user = new User(yaml);

            //登録する
            users.put(user.uuid, user);
        }
    }

    public List<User> getOnlineUsers(){
        return Bukkit.getOnlinePlayers().stream().map(this::getUser).collect(Collectors.toList());
    }

    public User getUser(Player player){
        return users.get(player.getUniqueId());
    }

    public User getUser(UUID uuid){
        return users.get(uuid);
    }

    public boolean containsUser(Player player){
        return containsUser(player.getUniqueId());
    }

    public boolean containsUser(UUID uuid){
        return users.containsKey(uuid);
    }

    public Yaml makeYaml(UUID uuid){
        return new Yaml(plugin, new File(folder, uuid.toString() + ".yml"), "user.yml");
    }

    public void saveAll(){
        users.values().forEach(User::save);
    }

    public void onJoin(PlayerJoinEvent event){
        registerUser(event.getPlayer());
    }

    public void  registerUser(Player player) {
        UUID uuid = player.getPlayer().getUniqueId();

        //既にユーザーデータが存在するのであれば戻る
        if(users.containsKey(uuid)) return;

        //ユーザーデータコンフィグ作成する
        Yaml yaml = makeYaml(uuid);

        //コンフィグを基にユーザーを生成する
        User user = new User(yaml);

        //登録する
        users.put(uuid, user);
    }
}

