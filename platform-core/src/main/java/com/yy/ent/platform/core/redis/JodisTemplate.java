package com.yy.ent.platform.core.redis;

import com.wandoulabs.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JodisTemplate
 *
 * @author suzhihua
 * @date 2015/9/22.
 */
public class JodisTemplate extends RedisTemplate {
    private RoundRobinJedisPool jedisPool;

    public JodisTemplate() {
    }

    public JodisTemplate(String zkAddr, int zkSessionTimeoutMs, String zkPath,
                         JedisPoolConfig poolConfig) {
        jedisPool = new RoundRobinJedisPool(zkAddr, zkSessionTimeoutMs, zkPath, poolConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Assert.notNull(jedisPool, "jedisPool can't be null");
    }

    @Override
    protected Jedis getJedis() {
        return jedisPool.getResource();
    }
}
