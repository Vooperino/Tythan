package com.github.archemedes.tythan.bukkit.data;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public interface TythanDataTypes {
    PersistentDataType<byte[], ItemStack> ITEMSTACK = new TythanDataType<>(ItemStack.class);
    PersistentDataType<byte[], Location> LOCATION = new TythanDataType<>(Location.class);
    PersistentDataType<byte[], ItemStack[]> ITEMSTACK_ARRAY = new TythanDataTypeArray<>(ItemStack.class, ItemStack[].class);

}
