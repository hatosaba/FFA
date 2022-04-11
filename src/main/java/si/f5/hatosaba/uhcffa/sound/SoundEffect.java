package si.f5.hatosaba.uhcffa.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundEffect {

    private final Sound type;
    private final float volume, pitch;

    public SoundEffect(Sound type, float volume, float pitch) {
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), type, volume, pitch);
    }

    public void play(Player... player) {
        for (Player p : player) {
            p.playSound(p.getLocation(), type, volume, pitch);
        }
    }

}
