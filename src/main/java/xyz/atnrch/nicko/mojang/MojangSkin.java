package xyz.atnrch.nicko.mojang;

import com.google.gson.JsonObject;

public class MojangSkin {
    private final String value;
    private final String signature;

    public MojangSkin(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    public static MojangSkin buildFromJson(JsonObject object) {
        final JsonObject properties = object.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        final String value = properties.get("value").getAsString();
        final String signature = properties.get("signature").getAsString();
        return new MojangSkin(value, signature);
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }
}
