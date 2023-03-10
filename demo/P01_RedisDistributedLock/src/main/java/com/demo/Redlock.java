package com.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * TODO: Redlock 是一个由 Redis 官方提出的分布式锁算法，可以在多个 Redis 节点之间协同工作，保证分布式锁的可靠性和稳定性。 使用 Redlock 实现分布式锁，需要在多个 Redis 节点之间协作。
 *
 * 我们定义了一个 Redlock 类，它使用了多个 JedisPool 对象，这些对象表示多个 Redis 实例的连接池。acquireLock 方法用于获取锁，它尝试在多个 Redis 实例上获取锁，并在大多数实例上都成功获取锁时返回锁的值。releaseLock 方法用于释放锁，它检查当前持有的锁是否为最新版本的锁，并在所有 Redis 实例上删除锁。
 *
 * 在上述示例中，我们首先定义了一个 QUORUM 值，表示锁被视为获取成功的最小 Redis 实例数。然后在 while 循环中，我们尝试在每个 Redis 实例上获取锁，并将获取锁成功的实例的地址加入到 locks 集合中。如果 locks 集合中的元素数量大于等于 QUORUM 值，表示锁获取成功，我们就返回锁的值。否则，我们需要释放所有获取锁成功的 Redis 实例上的锁，并等待一段时间后重试获取锁。
 *
 * 在释放锁时，我们遍历所有 Redis 实例，检查锁的值是否与当前持有的锁的值相同。如果是，表示当前持有的锁为最新版本的锁，我们就在所有 Redis 实例上删除锁，并返回 true。如果不是，表示当前持有的锁已经过期，我们需要重新获取锁。
 *
 * 需要注意的是，Redlock 算法不是绝对安全的，它可能存在竞争条件和死锁问题。因此，在使用 Redlock 算法时，需要仔细评估系统的需求和容错能力，合理选择 QUORUM 值和获取锁超时时间，避免出现问题。
 */
public class Redlock {

    private static final int QUORUM = 3;

    private List<JedisPool> jedisPools;

    public Redlock(List<JedisPool> jedisPools) {
        this.jedisPools = jedisPools;
    }

    public String acquireLock(String lockName, long lockTimeout, long acquireTimeout) throws InterruptedException {
        byte[] value = UUID.randomUUID().toString().getBytes();
        int lockCount = 0;

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {
            List<String> locks = new ArrayList<>();

            for (JedisPool jedisPool : jedisPools) {
                try (Jedis jedis = jedisPool.getResource()) {
                    SetParams setParams = new SetParams().nx().px(lockTimeout);
                    String result = jedis.set(lockName, new String(value), setParams);
                    if ("OK".equals(result)) {
                        locks.add(jedis.getClient().getHost() + ":" + jedis.getClient().getPort());
                    }
                } catch (JedisException e) {
                    // log
                }
            }

            lockCount = locks.size();
            if (lockCount >= QUORUM) {
                return new String(value);
            }

            for (String lock : locks) {
                String[] parts = lock.split(":");
                try (Jedis jedis = new Jedis(parts[0], Integer.parseInt(parts[1]))) {
                    jedis.del(lockName);
                } catch (JedisException e) {
                    // log
                }
            }

            Thread.sleep(10);
        }

        return null;
    }

    public boolean releaseLock(String lockName, String value) {
        boolean released = false;

        for (JedisPool jedisPool : jedisPools) {
            try (Jedis jedis = jedisPool.getResource()) {
                if (value.equals(jedis.get(lockName))) {
                    jedis.del(lockName);
                    released = true;
                }
            } catch (JedisException e) {
                // log
            }
        }

        return released;
    }
}
