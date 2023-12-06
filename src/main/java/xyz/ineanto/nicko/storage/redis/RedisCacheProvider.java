package xyz.ineanto.nicko.storage.redis;

import redis.clients.jedis.exceptions.JedisConnectionException;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DataSourceConfiguration;
import xyz.ineanto.nicko.storage.CacheProvider;
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
        try {
            return !pool.isClosed() && pool.getResource() != null;
        } catch (JedisConnectionException exception) {
            return false;
        }
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
