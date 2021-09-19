package si.f5.hatosaba.uhcffa;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

import static org.bukkit.ChatColor.*;

public class Constants {

    public static final Random RANDOM = new Random();

    public static final ItemStack KIT_SELECTOR = new ItemStack(Material.NETHER_STAR);
    public static final ItemStack SHOP = new ItemStack(Material.EMERALD);
    public static final ItemStack EXCALIBUR = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack ANDURIL = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack PERUN = new ItemStack(Material.DIAMOND_AXE);
    public static final ItemStack ARTEMIS = new ItemStack(Material.BOW);
    public static final ItemStack RESPAWN_ITEM = new ItemStack(Material.BONE);
    public static final ItemStack INSTANT_RESPAWN_ITEM = new ItemStack(Material.PAPER);
    public static final ItemStack BACK_TO_LOBBY = new ItemStack(Material.BED);

    public static final String[] UPDATE_MESSAGE = new String[]{
            "",
            YELLOW + "[アップデート] 21/09/07/22:24",
            YELLOW + "uhcmeetupが実装されました！",
            YELLOW + "イベント開催されている際に参加できます！！",
            "",
            GREEN + "提案はこちらから: https://forms.gle/QuY2UfrBxQpgKzPKA",
            ""
    };

    static {
        ItemMeta kitSelectorMeta = KIT_SELECTOR.getItemMeta();
        kitSelectorMeta.setDisplayName(GREEN + "Kitを選択する");
        KIT_SELECTOR.setItemMeta(kitSelectorMeta);

        ItemMeta shopMeta = SHOP.getItemMeta();
        shopMeta.setDisplayName(GREEN + "ショップ");
        SHOP.setItemMeta(shopMeta);

        ItemMeta instantRespawnItemMeta = INSTANT_RESPAWN_ITEM.getItemMeta();
        instantRespawnItemMeta.setDisplayName(RED + "インスタント リスポーン");
        INSTANT_RESPAWN_ITEM.setItemMeta(instantRespawnItemMeta);

        ItemMeta respawnItemMeta = RESPAWN_ITEM.getItemMeta();
        respawnItemMeta.setDisplayName(DARK_GRAY + "リスポーン");
        RESPAWN_ITEM.setItemMeta(respawnItemMeta);

        ItemMeta lobbyItemMeta = BACK_TO_LOBBY.getItemMeta();
        lobbyItemMeta.setDisplayName(RED + "ロビーに戻る");
        BACK_TO_LOBBY.setItemMeta(lobbyItemMeta);

        ItemMeta excaliburMeta = EXCALIBUR.getItemMeta();
        excaliburMeta.setDisplayName(YELLOW + "Excalibur");
        excaliburMeta.setLore(Arrays.asList(
                RED + "Explosive (!)",
                BLUE + "Chaos"
        ));
        excaliburMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        EXCALIBUR.setItemMeta(excaliburMeta);

        ItemMeta andurilMeta = ANDURIL.getItemMeta();
        andurilMeta.setDisplayName(GREEN + "Anduril");
        andurilMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false);
        andurilMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ANDURIL.setItemMeta(andurilMeta);

        ItemMeta perunItemMeta = PERUN.getItemMeta();
        perunItemMeta.setDisplayName(GOLD + "Axe of Perun");
        perunItemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        perunItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PERUN.setItemMeta(perunItemMeta);

        ItemMeta artemisItemMeta = ARTEMIS.getItemMeta();
        artemisItemMeta.setDisplayName(RED + "Artemis' Bow");
        artemisItemMeta.setLore(Arrays.asList(
                BLUE + "Hunting"
        ));
        artemisItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        artemisItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        ARTEMIS.setItemMeta(artemisItemMeta);

    }

}
