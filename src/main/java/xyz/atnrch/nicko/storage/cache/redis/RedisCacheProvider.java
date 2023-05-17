package xyz.atnrch.nicko.storage.cache.redis;

import xyz.atnrch.nicko.storage.cache.CacheProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCacheProvider implements CacheProvider {
    private JedisPool pool;

    @Override
    public boolean init() {
        // TODO: 3/12/23 Get port from configuration
        pool = new JedisPool("localhost", 6379);
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
