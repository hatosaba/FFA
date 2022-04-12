package si.f5.hatosaba.uhcffa.specialItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.Arrays;
import java.util.List;

public enum ExecutableItemType {

    GOLDEN_HEAD("golden"),
    LIGHT_APPLE("light"),
    AXE_PERUN("perun"),
    DEATHS_SYTHE("death"),
    EXCALIBUR("excalibur"),
    ANDURIL("anduril"),
    DRAGON_SWORD("dragon"),
    JOIN_ITEM("join"),
    LEAVE_ITEM("leave"),
    RESPAWN_ITEM("respawn"),
    SHOP_ITEM("shop"),
    FFA_SHOP_ITEM("ffa_shop"),
    BLOODLUST("bloodlust"),
    HERMESBOOTS("hermes"),
    SKELTON_HORSE("skelton"),
    FLASK_OF_ICHOR("ichor"),
    EXODUS("exodus"),
    MODULAR_BOW("ModularBow"),
    FLASK_OF_CLEANSING("cleansing"),
    PLAY_ITEM("play"),
    DUEL_LEAVE_ITEM("duel_leave"),
    DUEL_PLAY_AGAIN("again"),
    SPECTATOR("spec");

    private  ExecutableManager manager = Uhcffa.getInstance().getExecutableManager();

    private final String id;

    ExecutableItemType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void addItem(Player player) {
        player.getInventory().addItem(manager.getItem(id()));
    }

    public void setItem(int slot, Player player) {
        player.getInventory().setItem(slot, manager.getItem(id()));
    }

    public void setItem(int slot, String playerID) {
        setItem(slot, PlayerConverter.getPlayer(playerID));
    }

    public ItemStack getItem() {
        return manager.getItem(id());
    }

}
