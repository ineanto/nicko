package xyz.atnrch.nicko.mojang;

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
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MojangAPI {
    public static final String URL_NAME = "https://api.mojang.com/users/profiles/minecraft/{name}";
    public static final String URL_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";

    private final Logger logger = Logger.getLogger("MojangAPI");

    private final HashMap<String, String> uuidToName = new HashMap<>();

    private final CacheLoader<String, Optional<MojangSkin>> loader = new CacheLoader<String, Optional<MojangSkin>>() {
        @Nonnull
        public Optional<MojangSkin> load(@Nonnull String uuid) throws Exception {
            return getSkinFromMojang(uuid);
        }
    };

    private final LoadingCache<String, Optional<MojangSkin>> cache = CacheBuilder
            .newBuilder()
            .recordStats()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build(loader);

    public Optional<MojangSkin> getSkin(String uuid) throws IOException, ExecutionException {
        return cache.get(uuid);
    }

    public Optional<MojangSkin> getSkinWithoutCaching(String uuid) throws IOException {
        return getSkinFromMojang(uuid);
    }

    public Optional<String> getUUID(String name) throws IOException {
        final String parametrizedUrl = URL_NAME.replace("{name}", name);
        final JsonObject object = getRequestToUrl(parametrizedUrl);
        if (hasNoError(object)) {
            final JsonElement idObject = object.get("id");
            final String id = idObject.getAsString();
            uuidToName.put(id, name);
            return Optional.of(id);
        }
        return Optional.empty();
    }

    public void eraseFromCache(String uuid) {
        cache.invalidate(uuid);
        uuidToName.remove(uuid);
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

    public String getUUIDName(String uuid) {
        return uuidToName.get(uuid);
    }

    private JsonObject getRequestToUrl(String parametrizedUrl) throws IOException {
        final URL url = new URL(parametrizedUrl);
        final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setRequestMethod("GET");

        switch (con.getResponseCode()) {
            case 404:
            case 400:
                logger.warning("Failed to parse request: Invalid Name");
                return getErrorObject();
            case 429:
                logger.warning("Failed to parse request: The connection is throttled.");
                return getErrorObject();
            case 200:
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
                    logger.warning("Failed to parse request (" + parametrizedUrl + ")!");
                    return getErrorObject();
                }
            default:
                logger.warning("Unhandled response code from Mojang: " + con.getResponseCode());
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

    public LoadingCache<String, Optional<MojangSkin>> getCache() {
        return cache;
    }
}
