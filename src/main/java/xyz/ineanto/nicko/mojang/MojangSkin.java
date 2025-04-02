package xyz.ineanto.nicko.mojang;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Collections;

public record MojangSkin(String value, String signature) {
    public static MojangSkin buildFromJson(JsonObject object) {
        final JsonObject properties = object.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        final String value = properties.get("value").getAsString();
        final String signature = properties.get("signature").getAsString();
        return new MojangSkin(value, signature);
    }

    public Collection<ProfileProperty> asProfileProperties() {
        return Collections.singleton(new ProfileProperty("textures", value, signature));
    }
}
