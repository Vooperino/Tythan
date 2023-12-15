package com.github.archemedes.tythan.bukkit.utils;

import com.github.archemedes.tythan.bukkit.TythanBukkit;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class PersistentDataUtil {
    protected static <T extends PersistentDataContainer, Z extends PersistentDataType, V extends Object> void setTag(@NonNull T container, String key, Z type, V value){
        container.set(makeKey(key), type, value);
    }

    protected static <T extends PersistentDataContainer, Z extends PersistentDataType> Object getTag(@NonNull T container, String key, Z type){
        return container.get(makeKey(key), type);
    }

    protected static <T extends PersistentDataContainer, Z extends PersistentDataType> boolean hasTag(@NonNull T container, String key, Z type){
        return container.has(makeKey(key), type);
    }

    protected static <T extends PersistentDataContainer> void removeTag(@NonNull T container, String key){
        container.remove(makeKey(key));
    }

    protected static <T extends PersistentDataContainer> List<String> getKeys(@NonNull T container){
        List<String> keys = new ArrayList<>();
        for(NamespacedKey nKey : container.getKeys()){
            if(nKey.getNamespace().equals(TythanBukkit.get())){
                keys.add(nKey.getKey());
            }
        }
        return keys;
    }

    /**
     * Creates a namespaced key with the desired key
     * @param desiredKey the key you want to search for this data with
     * @return NamespacedKey which is how bukkit searches our information in a container
     * */
    protected static NamespacedKey makeKey(String desiredKey){
        return new NamespacedKey(TythanBukkit.get(), desiredKey);
    }

}
