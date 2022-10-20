package net.artelnatif.nicko.mojang;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class MojangAPI {
    public static final String URL_NAME = "https://api.mojang.com/users/profiles/minecraft/{name}";
    public static final String URL_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";

    private final CacheLoader<String, Optional<MojangSkin>> loader = new CacheLoader<>() {
        @Nonnull
        public Optional<MojangSkin> load(@Nonnull String uuid) throws Exception {
            return getSkinFromMojang(uuid);
        }
    };
    private final LoadingCache<String, Optional<MojangSkin>> cache = CacheBuilder
            .newBuilder()
            .build(loader);

    public Optional<MojangSkin> getSkin(String uuid) throws IOException, ExecutionException {
        return cache.get(uuid);
    }

    public Optional<String> getUUID(String name) throws IOException {
        final String parametrizedUrl = URL_NAME.replace("{name}", name);
        final JsonObject object = getRequestToUrl(parametrizedUrl);
        if (hasNoError(object)) {
            return Optional.of(object.get("id").getAsString());
        }
        return Optional.empty();
    }

    private Optional<MojangSkin> getSkinFromMojang(String uuid) throws IOException {
        final String parametrizedUrl = URL_SKIN.replace("{uuid}", uuid);
        final JsonObject object = getRequestToUrl(parametrizedUrl);
        if (hasNoError(object)) {
            final MojangSkin skin = MojangSkin.buildFromJson(object);
            return Optional.of(skin);
        }

        return Optional.empty();
    }

    private JsonObject getRequestToUrl(String parametrizedUrl) throws IOException {
        final URL url = new URL(parametrizedUrl);
        final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setRequestMethod("GET");

        final BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            builder.append(line);
        }

        try {
            final JsonElement jsonElt = JsonParser.parseString(builder.toString());
            return jsonElt.getAsJsonObject();
        } catch (JsonParseException | IllegalStateException exception) {
            System.out.println("Failed to parse request (" + parametrizedUrl + ")! Does the username exists?");
            return getErrorObject();
        }
    }

    private JsonObject getErrorObject() {
        final JsonObject errorObject = new JsonObject();
        errorObject.addProperty("error", "An error occurred.");
        return errorObject;
    }

    private boolean hasNoError(JsonObject object) {
        return object.get("error") == null;
    }
}
