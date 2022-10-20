package net.artelnatif.nicko.mojang;

import com.google.gson.JsonObject;

public record MojangSkin(String name, String value, String signature) {
    public static MojangSkin buildFromJson(JsonObject object) {
        final String name = object.get("name").getAsString();
        final JsonObject properties = object.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        final String value = properties.get("value").getAsString();
        final String signature = properties.get("signature").getAsString();
        return new MojangSkin(name, value, signature);
    }
}
