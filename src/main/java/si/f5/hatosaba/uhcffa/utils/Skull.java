package si.f5.hatosaba.uhcffa.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class Skull {

    public static ItemStack createFrom(UUID uuid){
        return createFrom(Bukkit.getOfflinePlayer(uuid));
    }

    public static ItemStack createFrom(OfflinePlayer player){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        skull.setItemMeta(meta);
        return skull;
    }

    /*public static ItemStack createFrom(String base64){

        int hash = base64.hashCode();
        UUID uuid = new UUID(hash, hash);
        String data = "{SkullOwner:{Id:\"" + uuid.toString() + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}";
        return Bukkit.getUnsafe().modifyItemStack(new ItemStack(Material.SKULL_ITEM), data);
    }*/

    public static ItemStack createFrom(String url) {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        assert skullMeta != null;

        if (url.length() < 16) {

            skullMeta.setOwner(url);
            skull.setItemMeta(skullMeta);
            return skull;
        }

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null); // We create a GameProfile

        // We get the bytes from the texture in Base64 encoded that comes from the Minecraft-URL.
        byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "https://textures.minecraft.net/texture/" + url // We get the texture link. // We create a GameProfile
                // We get the bytes from the texture in Base64 encoded that comes from the Minecraft-URL.
        ).getBytes());

        // We set the texture property in the GameProfile.
        gameProfile.getProperties().put("textures", new Property("textures", new String(data)));

        try {
            Field field = skullMeta.getClass().getDeclaredField("profile"); // We get the field profile.

            field.setAccessible(true); // We set as accessible to modify.
            field.set(skullMeta, gameProfile); // We set in the skullMeta the modified GameProfile that we created.

        } catch (Exception e) {
            e.printStackTrace();
        }

        skull.setItemMeta(skullMeta);

        return skull; //Finally, you have the custom head!

    }

}