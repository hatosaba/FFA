package si.f5.hatosaba.uhcffa.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import si.f5.hatosaba.uhcffa.Uhcffa;
import si.f5.hatosaba.uhcffa.sound.SoundEffects;
import si.f5.hatosaba.uhcffa.specialItem.ExecutableItemType;
import si.f5.hatosaba.uhcffa.utils.ItemBuilder;
import si.f5.hatosaba.uhcffa.utils.Skull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class FFAShopMenu implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("ffashop")
            .manager(Uhcffa.getInstance().getManager())
            .provider(new FFAShopMenu())
            .size(6, 9)
            .title(GREEN + "ショップ")
            .closeable(true)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        final PlayerInventory inventory = player.getInventory();

        contents.set(1, 2, ClickableItem.of(
                ItemBuilder.modify(Skull.createFrom("9f1eb4369447a2fb692693ea18a88b5b2fe7621b5d29a59a2860415d52e6cbeb"))
                        .name(GREEN + "Golden Head")
                        .lore(Arrays.asList(
                                GRAY + "Material 16",
                                GRAY + "Head 1",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 16) && inventory.contains(Material.SKULL_ITEM, 1)) {
                        removeInventoryItems(player, Material.REDSTONE, 16);
                        removeInventoryItems(player, Material.SKULL_ITEM, 1);
                        ExecutableItemType.GOLDEN_HEAD.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(1, 3, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name(GREEN + "Excalibur")
                        .lore(Arrays.asList(
                                GRAY + "Material 16",
                                GRAY + "Head 1",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 16) && inventory.contains(Material.SKULL_ITEM, 1) && canBuy(player)) {
                        removeInventoryItems(player, Material.REDSTONE, 16);
                        removeInventoryItems(player, Material.SKULL_ITEM, 1);
                        ExecutableItemType.EXCALIBUR.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(1, 4, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_AXE)
                        .name(GREEN + "Axe of Perun")
                        .lore(Arrays.asList(
                                GRAY + "Material 32",
                                GRAY + "Head 1",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 32) && inventory.contains(Material.SKULL_ITEM, 1) && canBuy(player)) {
                        removeInventoryItems(player, Material.REDSTONE, 32);
                        removeInventoryItems(player, Material.SKULL_ITEM, 1);
                        ExecutableItemType.AXE_PERUN.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(1, 5, ClickableItem.of(
                ItemBuilder.of(Material.IRON_SWORD)
                        .name(GREEN + "Anduril")
                        .lore(Arrays.asList(
                                GRAY + "Material 24",
                                GRAY + "Head 1",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 24) && inventory.contains(Material.SKULL_ITEM, 1) && canBuy(player)) {
                        removeInventoryItems(player, Material.REDSTONE, 24);
                        removeInventoryItems(player, Material.SKULL_ITEM, 1);
                        ExecutableItemType.ANDURIL.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(1, 6, ClickableItem.of(
                ItemBuilder.modify(ExecutableItemType.BLOODLUST.getItem())
                        .lore(Arrays.asList(
                                GRAY + "Material 48",
                                GRAY + "Head 2",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 32) && inventory.contains(Material.SKULL_ITEM, 2) && canBuy(player)) {
                        removeInventoryItems(player, Material.REDSTONE, 32);
                        removeInventoryItems(player, Material.SKULL_ITEM, 2);
                        ExecutableItemType.BLOODLUST.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(2, 2, ClickableItem.of(
                ItemBuilder.of(Material.IRON_HOE)
                        .name(GREEN + "&aDeath's Scythe")
                        .lore(Arrays.asList(
                                GRAY + "Material 48",
                                GRAY + "Head 2",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 48) && inventory.contains(Material.SKULL_ITEM, 2) && canBuy(player)) {
                        removeInventoryItems(player, Material.REDSTONE, 48);
                        removeInventoryItems(player, Material.SKULL_ITEM, 2);
                        ExecutableItemType.DEATHS_SYTHE.addItem(player);

                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(2, 3, ClickableItem.of(
                ItemBuilder.of(Material.GOLDEN_APPLE)
                        .name(GREEN + "Light Apple")
                        .lore(Arrays.asList(
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        ExecutableItemType.LIGHT_APPLE.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(2, 4, ClickableItem.of(
                ItemBuilder.of(Material.DIAMOND_SWORD)
                        .name(GREEN + "Dragon Sword")
                        .lore(Arrays.asList(
                                GRAY + "Material 48",
                                GRAY + "Head 1",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 48) && inventory.contains(Material.SKULL_ITEM, 1) && canBuy(player)) {
                        removeInventoryItems(player, Material.SKULL_ITEM, 1);
                        removeInventoryItems(player, Material.REDSTONE, 48);
                        ExecutableItemType.DRAGON_SWORD.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(2, 5, ClickableItem.of(
                ItemBuilder.of(Material.ARROW)
                        .name(GREEN + "ARROW")
                        .amount(16)
                        .lore(Arrays.asList(
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        player.getInventory().addItem(ItemBuilder.of(Material.ARROW).amount(16).build());
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(2, 6, ClickableItem.of(
                ItemBuilder.of(Material.LEATHER_BOOTS)
                        .name("&aHermes' Boots")
                        .lore(Arrays.asList(
                                GRAY + "Material 48",
                                GRAY + "Head 2",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 48) && inventory.contains(Material.SKULL_ITEM, 2)) {
                        removeInventoryItems(player, Material.REDSTONE, 48);
                        removeInventoryItems(player, Material.SKULL_ITEM, 2);
                        ExecutableItemType.HERMESBOOTS.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(3, 2, ClickableItem.of(
                ItemBuilder.modify(ExecutableItemType.SKELTON_HORSE.getItem())
                        .lore(Arrays.asList(
                                "",
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        ExecutableItemType.SKELTON_HORSE.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(3, 3, ClickableItem.of(
                ItemBuilder.modify(ExecutableItemType.FLASK_OF_ICHOR.getItem())
                        .name("&cFlask of Ichor")
                        .lore(Arrays.asList(
                                "",
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        ExecutableItemType.FLASK_OF_ICHOR.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(3, 4, ClickableItem.of(
                ItemBuilder.of(Material.GOLD_HELMET)
                        .name("&cExodus")
                        .lore(Arrays.asList(
                                "",
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        ExecutableItemType.EXODUS.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));
        contents.set(3, 5, ClickableItem.of(
                ItemBuilder.modify(ExecutableItemType.FLASK_OF_CLEANSING.getItem())
                        .lore(Arrays.asList(
                                "",
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        ExecutableItemType.FLASK_OF_CLEANSING.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(4, 2, ClickableItem.of(
                ItemBuilder.modify(ExecutableItemType.MODULAR_BOW.getItem())
                        .lore(Arrays.asList(
                                "",
                                GRAY + "Material 8",
                                "",
                                YELLOW + "Click to buy"
                        ))
                        .build(),
                e -> {
                    if (inventory.contains(Material.REDSTONE, 8)) {
                        removeInventoryItems(player, Material.REDSTONE, 8);
                        ExecutableItemType.MODULAR_BOW.addItem(player);
                    } else {
                        SoundEffects.FAILED.play(player);
                    }
                }));

        contents.set(4, 3, ClickableItem.of(
                ItemBuilder.of(Material.BARRIER)
                        .name("&cEmpty Slot")
                        .lore(GRAY + "提案があれば追加します")
                        .build(),
                e -> {}));

        contents.set(4, 4, ClickableItem.of(
                ItemBuilder.of(Material.BARRIER)
                        .name("&cEmpty Slot")
                        .lore(GRAY + "提案があれば追加します")
                        .build(),
                e -> { }));

        contents.set(5, 8, ClickableItem.of(
                ItemBuilder.of(Material.HOPPER)
                        .name(GREEN + "Trash")
                        .lore(Arrays.asList(
                                GRAY + "アイテムを捨てる",
                                "",
                                YELLOW + "Click to trash"
                        ))
                        .build(),
                e -> {
                    for (ExecutableItemType type : ExecutableItemType.values()) {
                        if (e.getCursor().isSimilar(type.getItem())) {
                            e.setCursor(ItemBuilder.of(Material.AIR).build());
                            player.sendMessage("アイテムを削除しました");
                        }
                    }
                }));
    }

    final List<ItemStack> arrayList = Arrays.asList(
            ExecutableItemType.DRAGON_SWORD.getItem(),
            ExecutableItemType.AXE_PERUN.getItem(),
            ExecutableItemType.DEATHS_SYTHE.getItem(),
            ExecutableItemType.ANDURIL.getItem(),
            ExecutableItemType.BLOODLUST.getItem(),
            ExecutableItemType.MODULAR_BOW.getItem(),
            ExecutableItemType.EXCALIBUR.getItem());

    public boolean canBuy(Player player) {
        int amount = 0;
        for (ItemStack item : arrayList) {
            if (player.getInventory().contains(item)) {
                amount += item.getAmount();
            }
        }

        return amount < 1;
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void removeInventoryItems(Player player, Material type, int amount) {
        final PlayerInventory playerInventory = player.getInventory();
        for (ItemStack is : playerInventory.getContents()) {
            if (is != null && is.getType() == type) {
                if (is.getItemMeta().hasDisplayName()) {
                    if (is.isSimilar(ExecutableItemType.GOLDEN_HEAD.getItem())) break;
                }
                int newamount = is.getAmount() - amount;
                if (newamount > 0) {
                    is.setAmount(newamount);
                    break;
                } else {
                    playerInventory.remove(is);
                    amount = -newamount;
                    if (amount == 0) break;
                }
            }
        }
    }
}