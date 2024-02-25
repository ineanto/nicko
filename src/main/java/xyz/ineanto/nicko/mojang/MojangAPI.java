package xyz.ineanto.nicko.mojang;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MojangAPI {
    public static final String URL_NAME = "https://api.mojang.com/users/profiles/minecraft/{name}";
    public static final String URL_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";

    private final Logger logger = Logger.getLogger("MojangAPI");
    private final HashMap<String, String> uuidToName = new HashMap<>();
    private final ExecutorService worker = Executors.newFixedThreadPool(6);

    private final CacheLoader<String, Optional<MojangSkin>> skinLoader = new CacheLoader<>() {
        @Nonnull
        public Optional<MojangSkin> load(@Nonnull String uuid) throws Exception {
            return getSkinFromMojang(uuid);
        }
    };

    private final LoadingCache<String, Optional<MojangSkin>> skinCache = CacheBuilder
            .newBuilder()
            .recordStats()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build(skinLoader);

    private final CacheLoader<String, Optional<String>> uuidLoader = new CacheLoader<>() {
        @Nonnull
        public Optional<String> load(@Nonnull String name) throws Exception {
            return getUUIDFromMojang(name);
        }
    };

    private final LoadingCache<String, Optional<String>> uuidCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(2, TimeUnit.DAYS)
            .build(uuidLoader);

    public Optional<MojangSkin> getSkin(String uuid) throws IOException, ExecutionException {
        return skinCache.get(uuid);
    }

    public Optional<MojangSkin> getSkinWithoutCaching(String uuid) throws IOException, ExecutionException, InterruptedException {
        return getSkinFromMojang(uuid);
    }

    public Optional<String> getUUID(String name) throws IOException, ExecutionException {
        return uuidCache.get(name);
    }

    private Optional<String> getUUIDFromMojang(String name) throws ExecutionException, InterruptedException {
        final String parametrizedUrl = URL_NAME.replace("{name}", name);
        final JsonObject object = getRequestToUrl(parametrizedUrl);
        if (hasNoError(object)) {
            final JsonElement idObject = object.get("id");
            final String uuid = idObject.getAsString();
            final Optional<String> uuidOptional = Optional.of(uuid);
            uuidCache.put(name, uuidOptional);
            uuidToName.put(uuid, name);
            return uuidOptional;
        }
        return Optional.empty();
    }

    public void eraseFromCache(String uuid) {
        skinCache.invalidate(uuid);
        uuidToName.remove(uuid);
        uuidCache.invalidate(uuid);
    }

    private Optional<MojangSkin> getSkinFromMojang(String uuid) throws ExecutionException, InterruptedException {
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

    private JsonObject getRequestToUrl(String parametrizedUrl) throws ExecutionException, InterruptedException {
        return worker.submit(() -> {
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
        }).get();
    }

    private JsonObject getErrorObject() {
        final JsonObject errorObject = new JsonObject();
        errorObject.addProperty("error", "An error occurred.");
        return errorObject;
    }

    private boolean hasNoError(JsonObject object) {
        return object.get("error") == null;
    }

    public LoadingCache<String, Optional<MojangSkin>> getSkinCache() {
        return skinCache;
    }
}
