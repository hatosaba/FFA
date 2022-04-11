package si.f5.hatosaba.uhcffa.specialItem.executableItem.ffa;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.hatosaba.uhcffa.schedule.Sync;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItem;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.Skull;
import si.f5.hatosaba.uhcffa.utils.TextUtil;

import java.util.Arrays;
import java.util.HashSet;

public class GoldenHead extends ExecutableItem {

    public GoldenHead(String id) {
        super(id);
    }

    private HashSet<Player> cooldown = new HashSet<>();

    @Override
    public ItemStack buildItemStack() {
        return define(
                ItemBuilder.modify(Skull.createFrom("9f1eb4369447a2fb692693ea18a88b5b2fe7621b5d29a59a2860415d52e6cbeb"))
                        .name("&6Golden Head")
                        .lore("&7Absorption (2:00)",
                                "&7Regeneration II (0:05)",
                                "&a*Given to all team",
                                "&amembers*")
                        .build()
        );
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        event.setCancelled(true);

        if (cooldown.contains(player)) return;

        if (item.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
        } else if (item.getAmount() > 1) {
            ItemStack itemClone = item.clone();
            itemClone.setAmount(item.getAmount() - 1);
            player.setItemInHand(itemClone);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9 * 20, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 2, 0));
        player.sendMessage(TextUtil.colorize(new String[]{
                "&aYou ate a &6Golden Head &aand gained 5 seconds to Regeneration IIII and 2 minutes of Absorption!",
                "&aYou gained 21 seconds of Speed II"
        }));
        player.playSound(player.getLocation(), Sound.BURP, 1F, 1F);
        cooldown.add(player);
        Sync.define(() -> cooldown.remove(player)).executeLater(3 * 20);

    }

    @Override
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.getPlayer().getLevel() < 50) event.setCancelled(true);
    }

    //エンティティに攻撃した時のアクションを設定する
    /*@Override
    public void onDamageEntity(PlayerDamageEntityEvent event) {
        event.setDamage(120);
    }*/

}
