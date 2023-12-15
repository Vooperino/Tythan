package com.github.archemedes.tythan.utils.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.nio.charset.StandardCharsets;

public interface SkinData {
    @NotNull @Unmodifiable String getValue();
    @NotNull @Unmodifiable String getSignature();
    default @NotNull @Unmodifiable boolean isSlim() {
        byte[] decodedSkinData = Base64.decodeBase64(this.getValue());
        String decodedSkinDataString = new String(decodedSkinData, StandardCharsets.UTF_8);
        JsonObject skinDataObject = new Gson().fromJson(decodedSkinDataString, JsonObject.class);
        if (skinDataObject.has("metadata")) {
            JsonObject metadataObject = skinDataObject.get("metadata").getAsJsonObject();
            String model = metadataObject.get("model").getAsString();
            return model.equals("slim");
        } else return false;
    }

}
