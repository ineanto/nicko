package xyz.atnrch.nicko.storage.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.storage.Cache;
import xyz.atnrch.nicko.storage.CacheProvider;

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
        System.out.println("Loaded REDIS CACHE");
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
        final Jedis jedis = provider.getJedis();
        jedis.set("nicko:" + uuid.toString(), gson.toJson(profile));
        return ActionResult.ok();
    }

    @Override
    public boolean isCached(UUID uuid) {
        final Jedis jedis = provider.getJedis();
        return jedis.exists("nicko:" + uuid.toString());
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        final Jedis jedis = provider.getJedis();
        // TODO (Ineanto, 5/20/23): Check if cached before because Jedis returns a bulk reply so this is unsafe
        final String data = jedis.get("nicko:" + uuid.toString());
        final NickoProfile profile = gson.fromJson(data, NickoProfile.class);
        return Optional.of(profile);
    }
}
