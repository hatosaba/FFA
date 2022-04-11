package si.f5.hatosaba.uhcffa.listeners;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.kit.KitManager;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.user.UserSet;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;
import si.f5.hatosaba.uhcffa.utils.Skull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJoinQuitListener implements Listener {

    private final SpectetorSet spectetorSet = SpectetorSet.getInstance();
    private final UserSet userSet = UserSet.getInstnace();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerPreLogin(final AsyncPlayerPreLoginEvent event) {
        // if player was kicked, don't load the data
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        final String playerID = event.getUniqueId().toString();
        final Uhcffa plugin = Uhcffa.instance();
        Uhcffa.putCustomPlayer(playerID, new CustomPlayer(playerID));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final String playerID = PlayerConverter.getID(event.getPlayer());
        Player player = event.getPlayer();

        CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);

        if (customPlayer == null) {
            customPlayer = new CustomPlayer(playerID);
            Uhcffa.putCustomPlayer(playerID, customPlayer);
        }

        userSet.onJoin(event);
        spectetorSet.onPlayerJoin(player);

        if (PlayerJoinQuitListener.combatPlayers.containsKey(player.getUniqueId())) {
            NPC npc = PlayerJoinQuitListener.combatPlayers.get(player.getUniqueId());
            PlayerJoinQuitListener.combatPlayers.remove(player.getUniqueId());
            if (npc.getEntity() != null) {
                player.teleport(npc.getEntity().getLocation().clone());
                player.setHealth(((LivingEntity) npc.getEntity()).getHealth());
            }
            npc.destroy();
            player.sendMessage("途中で抜けたので戦場にスポーンしました");
            spectetorSet.applyShowMode(player);
        } else {
            player.teleport(Uhcffa.instance().config().getLobby());

            PlayerInventory inv = player.getInventory();
            inv.clear();
            inv.setHelmet(new ItemStack(Material.AIR));
            inv.setChestplate(new ItemStack(Material.AIR));
            inv.setLeggings(new ItemStack(Material.AIR));
            inv.setBoots(new ItemStack(Material.AIR));

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            player.setMaxHealth(40);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);

            Uhcffa.instance().setLobbyItem(player);

            if (player.getGameMode() == GameMode.SURVIVAL) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }

        player.sendMessage(new String[]{
                "",
                ChatColor.GREEN + "こちらのリンクから",
                ChatColor.YELLOW + "https://forms.gle/ADnT8SHDBDzm6fTB7",
                ChatColor.GREEN + "要望・提案・バグなどを募集しています。",
                "",
        });

        // Injecting to the scoreboard
        Uhcffa.instance().getScoreboardManager().injectPlayer(player);
    }

    private final KitManager kitManager = KitManager.getInstance();
    public static final Map<UUID, NPC> combatPlayers = new HashMap<>();

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(kitManager.isSelected(player))
            kitManager.getSelectedPlayer().remove(player);

        if (CombatTagger.isTagged(player.getUniqueId())) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, event.getPlayer().getName());
            npc.spawn(player.getLocation());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.IRON_BOOTS, 1));
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.IRON_LEGGINGS, 1));
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, Skull.createFrom(player.getUniqueId()));

            npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);

            LivingEntity livingEntity = (LivingEntity) npc.getEntity();
            livingEntity.setMaxHealth(40);
            livingEntity.setHealth(event.getPlayer().getHealth());

            combatPlayers.put(event.getPlayer().getUniqueId(), npc);
        }

        spectetorSet.onPlayerQuit(player);

        Uhcffa.instance().getScoreboardManager().ejectPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();

            if (arena.getState() == ArenaState.IN_GAME)
                arena.onDeath(playerID);
            else
                arena.removePlayer(playerID);
            return;
        }

        Uhcffa.removePlayerData(playerID);
    }

}
