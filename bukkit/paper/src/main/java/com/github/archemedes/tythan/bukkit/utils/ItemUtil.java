package com.github.archemedes.tythan.bukkit.utils;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import com.github.archemedes.tythan.bukkit.data.TythanDataTypes;
import com.github.archemedes.tythan.bukkit.data.TythanPDTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemUtil extends PersistentDataUtil {
    public static @NotNull ItemStack make(Material baseMaterial, Component displayName){
        return make(baseMaterial, 1, displayName, new ArrayList<>());
    }

    public static @NotNull ItemStack make(Material baseMaterial, Component displayName, Component... lore){
        return make(baseMaterial, 1, displayName, Arrays.stream(lore).toList());
    }

    public static @NotNull ItemStack make(Material baseMaterial, Component displayName, List<Component> lore){
        return make(baseMaterial, 1, displayName, lore);
    }

    public static @NotNull ItemStack make(Material baseMaterial, int amount, Component displayName){
        return make(baseMaterial, amount, displayName, new ArrayList<>());
    }

    public static @NotNull ItemStack make(Material baseMaterial, int amount, Component displayName, Component... lore){
        return make(baseMaterial, amount, displayName, Arrays.stream(lore).toList());
    }

    public static @NotNull ItemStack make(Material baseMaterial, int amount, Component displayName, List<Component> lore){
        ItemStack i = new ItemStack(baseMaterial, amount);
        return decorate(i, displayName, lore);
    }

    public static @NotNull ItemStack makePotion(Color potionColor, Component displayName, List<Component> lore){
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta pMeta = (PotionMeta) potion.getItemMeta();
        pMeta.setColor(potionColor);
        return decorate(potion, displayName, lore);
    }

    @Contract("_, _, _ -> param1")
    public static @NotNull ItemStack decorate(@NotNull ItemStack item, @NotNull Component displayName, List<Component> lore){
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @SuppressWarnings("deprecation")
    public static boolean exists(ItemStack is) {
        return is != null
                && is.getType() != Material.AIR
                && is.getType() != Material.CAVE_AIR
                && is.getType() != Material.VOID_AIR
                && is.getType() != Material.LEGACY_AIR;
    }

    public static void setTag(@NotNull ItemMeta meta, String key, String value){
        setTag(meta.getPersistentDataContainer(), key, PersistentDataType.STRING, value);
    }

    public static <T extends PersistentDataContainer> boolean hasTag(ItemMeta meta, String key){
        for(TythanPDTypes dataTypes : TythanPDTypes.values()){
            if(hasTag((T)meta.getPersistentDataContainer(), key, dataTypes.getDataType())){
                return true;
            }
        }
        return false;
    }

    public static <T extends PersistentDataContainer> String getStringTag(@NotNull ItemMeta meta, String key){
        return (String) getTag((T) meta.getPersistentDataContainer(), key, PersistentDataType.STRING);
    }

    private static void setTythanTag(@NotNull ItemStack itemStack, String key, @NotNull TythanPDTypes type, Object value){
        ItemMeta meta = itemStack.getItemMeta();
        switch (type){
            case BYTE -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.BYTE, value);
            case BYTE_ARRAY -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.BYTE_ARRAY, value);
            case DOUBLE -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.DOUBLE, value);
            case LONG -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.LONG, value);
            case FLOAT -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.FLOAT, value);
            case SHORT -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.SHORT, value);
            case STRING -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.STRING, value);
            case INTEGER -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.INTEGER, value);
            case LOCATION -> setTag(meta.getPersistentDataContainer(), key, TythanPDTypes.LOCATION.getDataType(), value);
            case ITEMSTACK -> setTag(meta.getPersistentDataContainer(), key, TythanPDTypes.ITEMSTACK.getDataType(), value);
            case ITEMSTACK_ARRAY -> setTag(meta.getPersistentDataContainer(), key, TythanPDTypes.ITEMSTACK_ARRAY.getDataType(), value);
            case LONG_ARRAY -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.LONG_ARRAY, value);
            case INTEGER_ARRAY -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.INTEGER_ARRAY, value);
            case PERSISTENT_DATA_CONTAINER -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.TAG_CONTAINER, value);
            case PERSISTENT_DATA_CONTAINER_ARRAY -> setTag(meta.getPersistentDataContainer(), key, PersistentDataType.TAG_CONTAINER_ARRAY, value);
        }
        itemStack.setItemMeta(meta);
    }

    /**
     * Adds an NBT tag storing a type to an itemstack which is referencable by a key
     * @param itemStack item you want to add a tag to
     * @param key reference key for the stored data
     * @param value value you would like to store
     * */
    public static void setTag(ItemStack itemStack, String key, byte value){
        setTythanTag(itemStack, key, TythanPDTypes.BYTE, value);
    }

    public static void setTag(ItemStack itemStack, String key, byte[] value){
        setTythanTag(itemStack, key, TythanPDTypes.BYTE_ARRAY, value);
    }

    public static void setTag(ItemStack itemStack, String key, double value){
        setTythanTag(itemStack, key, TythanPDTypes.DOUBLE, value);
    }

    public static void setTag(ItemStack itemStack, String key, float value){
        setTythanTag(itemStack, key, TythanPDTypes.FLOAT, value);
    }

    public static void setTag(ItemStack itemStack, String key, int value){
        setTythanTag(itemStack, key, TythanPDTypes.INTEGER, value);
    }

    public static void setTag(ItemStack itemStack, String key, int[] value){
        setTythanTag(itemStack, key, TythanPDTypes.INTEGER_ARRAY, value);
    }

    public static void setTag(ItemStack itemStack, String key, long value){
        setTythanTag(itemStack, key, TythanPDTypes.LONG, value);
    }

    public static void setTag(ItemStack itemStack, String key, long[] value){
        setTythanTag(itemStack, key, TythanPDTypes.LONG_ARRAY, value);
    }

    public static void setTag(ItemStack itemStack, String key, short value){
        setTythanTag(itemStack, key, TythanPDTypes.SHORT, value);
    }

    public static void setTag(ItemStack itemStack, String key, String value){
        setTythanTag(itemStack, key, TythanPDTypes.STRING, value);
    }

    public static void setTag(ItemStack itemStack, String key, PersistentDataContainer value){
        setTythanTag(itemStack, key, TythanPDTypes.PERSISTENT_DATA_CONTAINER, value);
    }

    public static void setTag(ItemStack itemStack, String key, PersistentDataContainer[] value){
        setTythanTag(itemStack, key, TythanPDTypes.PERSISTENT_DATA_CONTAINER_ARRAY, value);
    }

    public static void setTag(ItemStack itemStack, String key, ItemStack value){
        setTythanTag(itemStack, key, TythanPDTypes.ITEMSTACK, value);
    }

    public static void setTag(ItemStack itemStack, String key, ItemStack[] value){
        setTythanTag(itemStack, key, TythanPDTypes.ITEMSTACK_ARRAY, value);
    }

    public static void setTag(ItemStack itemStack, String key, Location value){
        setTythanTag(itemStack, key, TythanPDTypes.LOCATION, value);
    }

    public static <T extends PersistentDataContainer> byte getByteTag(@NotNull ItemStack itemStack, String key){
        return (byte) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.BYTE);
    }

    public static <T extends PersistentDataContainer> byte[] getByteArrTag(@NotNull ItemStack itemStack, String key){
        return (byte[]) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.BYTE_ARRAY);
    }

    public static <T extends PersistentDataContainer> double getDoubleTag(@NotNull ItemStack itemStack, String key){
        return (double) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.DOUBLE);
    }

    public static <T extends PersistentDataContainer> float getFloatTag(@NotNull ItemStack itemStack, String key){
        return (float) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.FLOAT);
    }

    public static <T extends PersistentDataContainer> int getIntTag(@NotNull ItemStack itemStack, String key){
        return (int) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.INTEGER);
    }

    public static <T extends PersistentDataContainer> int[] getIntArrTag(@NotNull ItemStack itemStack, String key){
        return (int[]) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.INTEGER_ARRAY);
    }

    public static <T extends PersistentDataContainer> long getLongTag(@NotNull ItemStack itemStack, String key){
        return (long) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.LONG);
    }

    public static <T extends PersistentDataContainer> long[] getLongArrTag(@NotNull ItemStack itemStack, String key){
        return (long[]) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.LONG_ARRAY);
    }

    public static <T extends PersistentDataContainer> short getCharTag(@NotNull ItemStack itemStack, String key){
        return (short) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.SHORT);
    }

    public static <T extends PersistentDataContainer> String getStringTag(@NotNull ItemStack itemStack, String key){
        return (String) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.STRING);
    }

    public static <T extends PersistentDataContainer> PersistentDataContainer getPersistentDataContainerTag(@NotNull ItemStack itemStack, String key){
        return (PersistentDataContainer) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.TAG_CONTAINER);
    }

    public static <T extends PersistentDataContainer> PersistentDataContainer[] getPersistentDataContainerArrTag(@NotNull ItemStack itemStack, String key){
        return (PersistentDataContainer[]) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, PersistentDataType.TAG_CONTAINER_ARRAY);
    }

    public static <T extends PersistentDataContainer> Location getLocationTag(@NotNull ItemStack itemStack, String key){
        return (Location) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, TythanDataTypes.LOCATION);
    }

    public static <T extends PersistentDataContainer> ItemStack getItemStackTag(@NotNull ItemStack itemStack, String key){
        return (ItemStack) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, TythanDataTypes.ITEMSTACK);
    }

    public static <T extends PersistentDataContainer> ItemStack[] getItemStackArrTag(@NotNull ItemStack itemStack, String key){
        return (ItemStack[]) getTag((T) itemStack.getItemMeta().getPersistentDataContainer(), key, TythanDataTypes.ITEMSTACK_ARRAY);
    }

    public static <T extends PersistentDataContainer> boolean hasTag(ItemStack itemStack, String key){
        if(!exists(itemStack)) return false;
        for(TythanPDTypes dataTypes : TythanPDTypes.values()){
            if(hasTag((T)itemStack.getItemMeta().getPersistentDataContainer(), key, dataTypes.getDataType())){
                return true;
            }
        }
        return false;
    }

    public static <T extends PersistentDataContainer> void removeTag(ItemStack itemStack, String key){
        removeTag((T) itemStack, key);
    }

    public static ItemStack getSkullFromTexture(@NotNull String theTexture) {
        NbtCompound tag = NbtFactory.ofCompound("tag");
        NbtCompound skullOwner = NbtFactory.ofCompound("SkullOwner");
        NbtCompound properties = NbtFactory.ofCompound("Properties");
        NbtCompound property = NbtFactory.ofCompound("");

        char[] uid = UUID.nameUUIDFromBytes(theTexture.getBytes()).toString().toCharArray();
        uid[14] = '4';
        uid[19] = 'a';

        property.put("Value", theTexture);
        skullOwner.put("Id", String.valueOf(uid));
        NbtList<NbtCompound> list = NbtFactory.ofList("textures", property);
        properties.put(list);
        skullOwner.put(properties);
        tag.put(skullOwner);

        ItemStack is = MinecraftReflection.getBukkitItemStack(new ItemStack(Material.PLAYER_HEAD, 1));
        NbtFactory.setItemTag(is, tag);
        return is;
    }

    /**
     * Method to easily make Minecraft skulls from arbitrary skin files
     * @param profile The profile to get a skin from
     * @return a textured Minecraft SKULL_ITEM
     */
    public static ItemStack getSkullFromProfile(@NotNull WrappedGameProfile profile) {
        String value = profile.getProperties().get("textures").iterator().next().getValue();
        return getSkullFromTexture(value);
    }

}
