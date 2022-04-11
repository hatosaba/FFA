package si.f5.hatosaba.uhcffa.specialItem;

import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.specialItem.executableItem.duel.DuelLeaveItem;
import si.f5.hatosaba.uhcffa.specialItem.executableItem.duel.DuelPlayAgainItem;
import si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa.*;

import java.util.Arrays;
import java.util.List;

public class ExecutableManager {

    private final ExecutableItemRegistry executableItemRegistry = new ExecutableItemRegistry();

    public static List<ItemStack> withoutDefaultKit;


    public void init() {
        new PlayerExecuteItemListener(executableItemRegistry);
        register(new GoldenHead(ExecutableItemType.GOLDEN_HEAD.id()));
        register(new DeathsScythe(ExecutableItemType.DEATHS_SYTHE.id()));
        register(new Anduril(ExecutableItemType.ANDURIL.id()));
        register(new Excalibur(ExecutableItemType.EXCALIBUR.id()));
        register(new LightApple(ExecutableItemType.LIGHT_APPLE.id()));
        register(new AxeOfPerun(ExecutableItemType.AXE_PERUN.id()));
        register(new DragonSword(ExecutableItemType.DRAGON_SWORD.id()));
        register(new SpectetorItem(ExecutableItemType.SPECTATOR.id()));
        register(new KitSelectorItem(ExecutableItemType.JOIN_ITEM.id()));
        register(new LeaveItem(ExecutableItemType.LEAVE_ITEM.id()));
        register(new ShopItem(ExecutableItemType.SHOP_ITEM.id()));
        register(new FFAShopItem(ExecutableItemType.FFA_SHOP_ITEM.id()));
        register(new SpectetorItem(ExecutableItemType.SPECTATOR.id()));
        register(new RespawnItem(ExecutableItemType.RESPAWN_ITEM.id()));
        register(new BloodLust(ExecutableItemType.BLOODLUST.id()));
        register(new HermesBoots(ExecutableItemType.HERMESBOOTS.id()));
        register(new Daredevil(ExecutableItemType.SKELTON_HORSE.id()));
        register(new FlaskOfIchor(ExecutableItemType.FLASK_OF_ICHOR.id()));
        register(new Exodus(ExecutableItemType.EXODUS.id()));
        register(new FlaskOfCleansing(ExecutableItemType.FLASK_OF_CLEANSING.id()));
        register(new ModularBow(ExecutableItemType.MODULAR_BOW.id()));
        register(new PlayItem(ExecutableItemType.PLAY_ITEM.id()));
        register(new DuelLeaveItem(ExecutableItemType.DUEL_LEAVE_ITEM.id()));
        register(new DuelPlayAgainItem(ExecutableItemType.DUEL_PLAY_AGAIN.id()));

        withoutDefaultKit = Arrays.asList(
                ExecutableItemType.FFA_SHOP_ITEM.getItem(),
                ExecutableItemType.RESPAWN_ITEM.getItem(),
                ExecutableItemType.LIGHT_APPLE.getItem());
    }

    public void register(ExecutableItem item) {
        executableItemRegistry.register(item);
    }

    public void unregister(ExecutableItem item) {

        executableItemRegistry.unregister(item);
    }

    public boolean isRegistered(ExecutableItem item) {
        return executableItemRegistry.isRegistered(item);
    }

    public ItemStack getItem(String id) {
        return executableItemRegistry.get(id).buildItemStack();
    }
}
