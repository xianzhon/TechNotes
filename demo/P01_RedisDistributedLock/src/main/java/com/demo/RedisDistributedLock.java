package com.demo;

import redis.clients.jedis.Jedis;

/**
 * 使用 setnx 指令实现了 Redis 分布式锁的获取和释放:
 * <p>
 * acquire() 方法尝试获取锁，如果成功则返回 true，否则返回 false；
 * release() 方法尝试释放锁，如果成功则返回 true，否则返回 false。
 */
public class RedisDistributedLock {
    private Jedis jedis;
    private String lockKey;
    private String lockValue;
    private long expireAfterSeconds;

    public RedisDistributedLock(Jedis jedis, String lockKey, String lockValue, long expireAfterSeconds) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.expireAfterSeconds = expireAfterSeconds;
    }

    public boolean acquire() {
        // 获取分布式锁的核心指令, setnx means SET if Not eXists
        Long result = jedis.setnx(lockKey, lockValue);
        if (result == 1) {
            jedis.expire(lockKey, expireAfterSeconds);
            return true;
        } else {
            return false;
        }
    }

    public boolean release() {
        String result = jedis.get(lockKey);
        if (result != null && result.equals(lockValue)) {
            jedis.del(lockKey);
            return true;
        } else {
            return false;
        }
    }
}
