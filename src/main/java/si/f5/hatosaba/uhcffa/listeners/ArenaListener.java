package si.f5.hatosaba.uhcffa.listeners;

import com.cryptomorin.xseries.XMaterial;
import me.MathiasMC.PvPLevels.api.events.PlayerKillEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.arena.Arena;
import si.f5.hatosaba.uhcffa.arena.ArenaState;
import si.f5.hatosaba.uhcffa.modules.CustomPlayer;
import si.f5.hatosaba.uhcffa.spectetor.SpectetorSet;
import si.f5.hatosaba.uhcffa.utils.BucketUtils;
import si.f5.hatosaba.uhcffa.utils.PlayerConverter;

import java.util.LinkedList;
import java.util.List;

public class ArenaListener implements Listener {

    /*@EventHandler
    public void onQuit(PlayerQuitEvent event) {

    }*/

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);
        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (player.getGameMode() == GameMode.CREATIVE) return;

            if (arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS || arena.getArenaState() == ArenaState.IN_GAME) {
                if (event.getTo().getY() <= 0) {
                    player.teleport(arena.getSpawn1());
                }
            }
        } else {
            if (player.getGameMode() == GameMode.CREATIVE) return;

            if (event.getTo().getY() <= 0) {
                player.teleport(Uhcffa.getInstance().config().getLobby());
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() == ArenaState.IN_GAME) {
                arena.onDeath(customPlayer);
                event.setDeathMessage(null);
                player.setHealth(player.getMaxHealth());
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void pvpLevels(PlayerKillEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                event.setCancelled(true);
            } else {
                Block block = event.getBlock();
                arena.getPlaced().add(block);
            }
            if (arena.getArenaState() == ArenaState.IN_GAME) {
                double max_build_y = arena.getMaxBuildY();
                if (event.getBlockPlaced().getLocation().getBlockY() >= max_build_y) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                event.setCancelled(true);
            } else {
                Block block = event.getBlock();
                if (!arena.getPlaced().contains(block)) {
                    event.setCancelled(true);
                    return;
                }
                arena.getPlaced().remove(block);
            }
        } else {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event) {
        if (!event.getBlock().isLiquid()) {
            return;
        }
        Block to = event.getToBlock();
        if (event.getBlock().getType() == Material.COBBLESTONE) {
            event.setCancelled(true);
        }
        if (BucketUtils.generates(event.getBlock(), to)) {
            event.setCancelled(true);
        } else {
            BlockFace[] faces = {BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            BlockFace[] array;
            for (int length = (array = faces).length, i = 0; i < length; ++i) {
                BlockFace face = array[i];
                if (BucketUtils.generates(event.getBlock(), to.getRelative(face))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                event.setCancelled(true);
            } else {
                Block clickedBlock = event.getBlockClicked();
                if (arena.getBucketPlaced().contains(clickedBlock)) {
                    if (clickedBlock.getType() == XMaterial.LAVA.parseMaterial()
                            || clickedBlock.getType() == XMaterial.LAVA.parseMaterial()) {
                        List<Block> trackBlock = new LinkedList<>();
                        BucketUtils.trackLava(clickedBlock, trackBlock);
                        trackBlock.forEach(block -> block.setType(Material.AIR));
                    }
                    arena.getBucketPlaced().remove(clickedBlock);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                event.setCancelled(true);
            } else {
                Block clickedBlock = event.getBlockClicked();
                Block block = null;
                if (event.getBlockFace() == BlockFace.UP) {
                    block = clickedBlock.getLocation().add(0.0, 1.0, 0.0).getBlock();
                } else if (event.getBlockFace() == BlockFace.DOWN) {
                    block = clickedBlock.getLocation().add(0.0, -1.0, 0.0).getBlock();
                } else if (event.getBlockFace() == BlockFace.EAST) {
                    block = clickedBlock.getLocation().add(1.0, 0.0, 0.0).getBlock();
                } else if (event.getBlockFace() == BlockFace.SOUTH) {
                    block = clickedBlock.getLocation().add(0.0, 0.0, 1.0).getBlock();
                } else if (event.getBlockFace() == BlockFace.WEST) {
                    block = clickedBlock.getLocation().add(-1.0, 0.0, 0.0).getBlock();
                } else if (event.getBlockFace() == BlockFace.NORTH) {
                    block = clickedBlock.getLocation().add(0.0, 0.0, -1.0).getBlock();
                }
                if (block != null) {
                    arena.getBucketPlaced().add(block);
                } else if (player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.getSetupData() != null) {
            ItemStack itemStack = event.getItem().getItemStack();
            if (itemStack == null) {
                return;
            }
        } else if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            ItemStack itemStack2 = event.getItem().getItemStack();
            if (itemStack2 == null) {
                return;
            }
            if (arena.getArenaState() == ArenaState.IN_GAME) {
                arena.getDroppedItem().remove(event.getItem());
            }
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                event.setCancelled(true);
            } else if (itemStack2.getType() != XMaterial.OAK_PLANKS.parseMaterial()) {
                event.setCancelled(true);
            }
        } else if (player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        final String playerID = PlayerConverter.getID(player);
        final CustomPlayer customPlayer = Uhcffa.getInstance().getCustomPlayer(playerID);

        if (customPlayer.getSetupData() != null) {
            ItemStack itemStack = event.getItemDrop().getItemStack();
            if (itemStack == null) {
                return;
            }
        } else if (customPlayer.inArena()) {
            Arena arena = customPlayer.getArena();
            ItemStack itemStack2 = event.getItemDrop().getItemStack();
            if (itemStack2 == null) {
                return;
            }
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                event.setCancelled(true);
            } else if (itemStack2.getType() != XMaterial.OAK_PLANKS.parseMaterial()) {
                event.setCancelled(true);
            } else {
                arena.getDroppedItem().add(event.getItemDrop());
            }
        } else if (player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(player);
            if (!customPlayer.inArena()) {
                if (!SpectetorSet.getInstance().isHideMode(player))
                    return;
                event.setCancelled(true);
            }
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() == ArenaState.COUNTING_DOWN || arena.getArenaState() == ArenaState.GAME_END) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            CustomPlayer customPlayer = Uhcffa.getCustomPlayer(player);
            if (!customPlayer.inArena()) {
                if (!SpectetorSet.getInstance().isHideMode(player))
                    return;
                event.setCancelled(true);
            }
            Arena arena = customPlayer.getArena();
            if (arena.getArenaState() != ArenaState.IN_GAME) {
                return;
            }
            if (event.getEntity() instanceof Player) {
                CustomPlayer target = Uhcffa.getCustomPlayer((Player) event.getEntity());
                Arena targetArena = customPlayer.getArena();
                if (targetArena.getArenaState() != ArenaState.IN_GAME) {
                    return;
                }
                        /*if (arena.getCurrentUUID().equals(targetArena.getCurrentUUID())) {
                            ActionBar.sendActionBar(player,
                                    Duel.getMessageConfig().getString("arenas.ingame.damage-actionbar").replace(
                                            "%%damage%%",
                                            String.valueOf(Math.round(event.getFinalDamage() * 100.0) / 100.0)));
                        }*/

            }
        }
    }
}
