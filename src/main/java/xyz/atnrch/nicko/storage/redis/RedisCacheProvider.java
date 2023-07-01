package xyz.atnrch.nicko.storage.redis;

import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.storage.CacheProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCacheProvider implements CacheProvider {
    private final Configuration configuration;
    private JedisPool pool;

    public RedisCacheProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean init() {
        final DataSourceConfiguration redisConfiguration = configuration.getRedisConfiguration();
        pool = new JedisPool(redisConfiguration.getAddress(), redisConfiguration.getPort());
        return !pool.isClosed() && pool.getResource() != null;
    }

    @Override
    public boolean close() {
        pool.close();
        return pool.isClosed();
    }

    public Jedis getJedis() {
        return pool.getResource();
    }
}