package si.f5.hatosaba.uhcffa.arena;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.kit.Kit;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.utils.LocationUtil;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.TextUtil;
import si.f5.hatosaba.uhcffa.utils.yaml.Yaml;

import java.io.File;
import java.util.*;

public class ArenaManager {

    private final Uhcffa plugin = Uhcffa.getInstance();
    //アリーナデータを保存するフォルダー
    public final File folder = new File(plugin.getDataFolder() + File.separator + "Arenas");

    private final Map<String, Arena> arenas = new HashMap<>();

    public ArenaManager(){
        //フォルダーが存在しなければ作成する
        if(!folder.exists()) folder.mkdirs();

        for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
            //ファイルをコンフィグとして読み込む
            Yaml yaml = new Yaml(plugin, file, "arena.yml");

            String fileName = file.getName();
            //拡張子を削除してアリーナ名を取得する
            String arenaName = fileName.substring(0, fileName.length() - 4);
            //コンフィグを基にアリーナを生成する
            Arena arena = new Arena(yaml);
            //登録する
            arenas.put(arenaName, arena);
        }
    }

    public void registerArena(String arenaName){
        File file = new File(folder, arenaName.replace('§', '&') + ".yml");

        //コンフィグが存在しなければ戻る
        if(!file.exists()) return;

        //コンフィグを取得する
        Yaml yaml = makeYaml(arenaName.replace('§', '&'));

        //コンフィグに基づきアリーナを生成する
        Arena arena = new Arena(yaml);

        //登録する
        arenas.put(arenaName, arena);
    }

    public void registerArena(Arena arena){
        final String arenaName = arena.getName();
        //既にアリーナデータが存在するのであれば戻る
        if(arenas.containsKey(arenaName)) return;
        //アリーナデータコンフィグ作成する
        Yaml yaml = makeYaml(arenaName);
        yaml.set("spawn1", LocationUtil.locationToString(arena.getSpawn1()));
        yaml.set("spawn2", LocationUtil.locationToString(arena.getSpawn2()));
        yaml.set("maxBuildY", arena.getMaxBuildY());
        yaml.save();
        //登録する
        arenas.put(arena.getName(), arena);
    }


    public Yaml makeYaml(String arenaName){
        return new Yaml(plugin, new File(folder, arenaName + ".yml"), "arena.yml");
    }

    public void joinMatch(CustomPlayer customPlayer, Kit kit) {
        List<Arena> availableGames = new LinkedList<>();
        for (Arena arena : arenas.values()) {
            if (arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS && !arena.isFull()) {
                availableGames.add(arena);
            }
        }
        if (availableGames.isEmpty()) {
            customPlayer.sendTranslated("arena.not.use.arena");
            //player.sendMessage("使用できるアリーナがありません");
            return;
        }
        Collections.shuffle(availableGames);
        Arena arena = availableGames.stream()
                .filter(arena1 -> {
                    if (arena1.getKit() == null) {
                        arena1.setKit(kit);
                        return true;
                    } else {
                        return arena1.getKit() == kit;
                    }
                })
                .min((arena1, arena2) -> arena2.getPlayers().size() - arena1.getPlayers().size())
                .get();
        arena.addPlayer(customPlayer);
    }

    public void createMatch(Request request) {
        List<Arena> availableGames = new LinkedList<>();
        for (Arena arena : arenas.values()) {
            if (arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS && !arena.isFull()) {
                availableGames.add(arena);
            }
        }
        if (availableGames.isEmpty()) {
            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(request.getPlayerID());
            customPlayer.sendTranslated("arena.not.use.arena");
            return;
        }
        Collections.shuffle(availableGames);
        Arena arena = availableGames.stream()
                .min((arena1, arena2) -> arena2.getPlayers().size() - arena1.getPlayers().size())
                .get();

        arena.setKit(request.getKit());
        arena.addPlayer(Uhcffa.getCustomPlayer(request.playerID));
        arena.addPlayer(Uhcffa.getCustomPlayer(request.toPlayerID));
    }

    public void removeArena(String arenaName) {
        makeYaml(arenaName).file.delete();
        arenas.remove(arenaName);
    }

    public Arena getArena(String arenaName) {
        return arenas.get(arenaName);
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    public void requestDuel(String playerID, String toPlayerID, Kit kit) {
        final Player player = PlayerConverter.getPlayer(playerID);
        final Player toPlayer = PlayerConverter.getPlayer(toPlayerID);
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(toPlayerID);

        player.sendMessage(TextUtil.colorize("&7You invited &9" + toPlayer.getName() + " &7to &9" + kit.getName() + " Duel!"));

        toPlayer.sendMessage(TextUtil.colorize("&9" + kit.getName() + " &7Duel Request From &9" + player.getName()));
        toPlayer.spigot().sendMessage(
                new ComponentBuilder(TextUtil.colorize( "&9&l&nCLICK HERE "))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + player.getName()))
                        .append(TextUtil.colorize("&r&7to accept!"))
                        .create());

        customPlayer.addRequest(new Request(playerID, toPlayerID, kit));

        SoundEffects.SUCCEEDED.play(player, toPlayer);
    }

    public boolean canRequest() {
        return true;
    }

    public class Request {

        private final String playerID;
        private final String toPlayerID;
        private final Kit kit;

        public Request(String playerID, String toPlayerID, Kit kit) {
            this.playerID = playerID;
            this.toPlayerID = toPlayerID;
            this.kit = kit;
        }

        public String getPlayerID() {
            return playerID;
        }

        public String getToPlayerID() {
            return toPlayerID;
        }

        public Kit getKit() {
            return kit;
        }
    }
}

