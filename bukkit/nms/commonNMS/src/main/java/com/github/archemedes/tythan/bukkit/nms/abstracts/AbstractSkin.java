package com.github.archemedes.tythan.bukkit.nms.abstracts;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.utils.mojang.SkinData;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;

public abstract class AbstractSkin implements SkinData {
    @NotNull @Unmodifiable @Override public abstract String getValue();
    @NotNull @Unmodifiable @Override public abstract String getSignature();

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    @NotNull @Unmodifiable public ItemStack toPlayerHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(),"Player");
        profile.getProperties().put("textures", this.getProperty());
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile");
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(meta, profile);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            try {
                if (metaProfileField==null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, profile);
            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                Tythan.get().getLogger().log(Level.SEVERE,"Failed to mutate the given meta object.", ex);
            }
        }
        head.setItemMeta(meta);
        return head;
    }

    public JsonObject toJSON() {
        JsonObject object = new JsonObject();
        JsonObject properties = new JsonObject();
        properties.addProperty("value", this.getValue());
        properties.addProperty("signature", this.getSignature());
        object.add("properties", properties);
        return object;
    }

    @NotNull public Property getProperty(){return new Property("textures", this.getValue(),this.getSignature());}
}
