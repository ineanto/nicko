package xyz.ineanto.nicko.storage.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.Cache;
import xyz.ineanto.nicko.storage.CacheProvider;

import java.util.Optional;
import java.util.UUID;

public class RedisCache extends Cache {
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    private final Configuration configuration;
    private RedisCacheProvider provider;

    public RedisCache(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public CacheProvider getProvider() {
        if (provider == null) {
            provider = new RedisCacheProvider(configuration);
        }
        return provider;
    }

    @Override
    public ActionResult cache(UUID uuid, NickoProfile profile) {
        try (Jedis jedis = provider.getJedis()) {
            jedis.set("nicko:" + uuid.toString(), gson.toJson(profile));
            return ActionResult.ok();
        } catch (JedisException exception) {
            return ActionResult.error(LanguageKey.Error.CACHE);
        }
    }

    @Override
    public boolean isCached(UUID uuid) {
        try (Jedis jedis = provider.getJedis()) {
            return jedis.exists("nicko:" + uuid.toString());
        } catch (JedisException exception) {
            return false;
        }
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        try (Jedis jedis = provider.getJedis()) {
            // 06/07/25: like, for real. what the f is that comment about...?
            // 08/29/23: what the fuck was I talking about?
            // old_todo (Ineanto, 05/20/23): Check if cached before because Jedis returns a bulk reply so this is unsafe
            final String data = jedis.get("nicko:" + uuid.toString());
            final NickoProfile profile = gson.fromJson(data, NickoProfile.class);
            return Optional.of(profile);
        } catch (JedisException exception) {
            return Optional.empty();
        }
    }

    @Override
    public ActionResult delete(UUID uuid) {
        try (Jedis jedis = provider.getJedis()) {
            jedis.del("nicko:" + uuid.toString());
            return ActionResult.ok();
        } catch (JedisException exception) {
            return ActionResult.error(LanguageKey.Error.CACHE);
        }
    }
}
