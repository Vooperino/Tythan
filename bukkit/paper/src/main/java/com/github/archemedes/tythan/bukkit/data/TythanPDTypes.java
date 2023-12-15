package com.github.archemedes.tythan.bukkit.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.persistence.PersistentDataType;

@Getter
@RequiredArgsConstructor
public enum TythanPDTypes {
    BYTE(PersistentDataType.BYTE),
    BYTE_ARRAY(PersistentDataType.BYTE_ARRAY),
    DOUBLE(PersistentDataType.DOUBLE),
    FLOAT(PersistentDataType.FLOAT),
    INTEGER(PersistentDataType.INTEGER),
    INTEGER_ARRAY(PersistentDataType.INTEGER_ARRAY),
    LONG(PersistentDataType.LONG),
    LONG_ARRAY(PersistentDataType.LONG_ARRAY),
    SHORT(PersistentDataType.SHORT),
    STRING(PersistentDataType.STRING),
    PERSISTENT_DATA_CONTAINER(PersistentDataType.TAG_CONTAINER),
    PERSISTENT_DATA_CONTAINER_ARRAY(PersistentDataType.TAG_CONTAINER_ARRAY),
    ITEMSTACK(TythanDataTypes.ITEMSTACK),
    ITEMSTACK_ARRAY(TythanDataTypes.ITEMSTACK_ARRAY),
    LOCATION(TythanDataTypes.LOCATION),
            ;

    private final PersistentDataType dataType;

}
