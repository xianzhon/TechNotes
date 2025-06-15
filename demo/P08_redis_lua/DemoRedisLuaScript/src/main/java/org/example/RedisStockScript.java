package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisStockScript {
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int REDIS_PORT = 6379;
    private static JedisPool jedisPool;

    // Lua 脚本：检查库存并扣减（原子性操作）
    private static final String STOCK_DEDUCTION_SCRIPT =
                    "local stock_key = KEYS[1] " +
                    "local stock = tonumber(redis.call('GET', stock_key)) " +
                    "if stock > 0 then " +
                    "    redis.call('DECR', stock_key) " +
                    "    return 1 " +  // 扣减成功
                    "end " +
                    "    return 0 ";  // 库存不足

    static {
        // 初始化 Redis 连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);  // 最大连接数
        jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT);
    }

    /**
     * 扣减库存（原子性操作）
     * @param key Redis 中的库存键（如 "product:1001:stock"）
     * @return true 扣减成功，false 库存不足或失败
     */
    public static boolean deductStock(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            // 执行 Lua 脚本
            Object result = jedis.eval(
                    STOCK_DEDUCTION_SCRIPT,
                    1,  // KEYS 的数量
                    key  // KEYS[1]
            );

            // 脚本返回 1 表示扣减成功
            return Long.valueOf(1).equals(result);
        } catch (Exception e) {
            System.err.println("Redis 操作异常: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        testSingleThreadDeduct();
        testMultipleThreadsDeduct();
    }

    static void testSingleThreadDeduct() {
        String stockKey = "product:1001:stock";

        // 初始化库存（测试用）
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(stockKey, "10");  // 初始库存为 10
        }

        // 模拟并发扣减
        for (int i = 0; i < 15; i++) {
            boolean success = deductStock(stockKey);
            System.out.printf("扣减尝试 %d: %s%n", i + 1, success ? "成功" : "失败");
        }
    }

    static void testMultipleThreadsDeduct() throws InterruptedException {
        String stockKey = "product:1002:stock";

        // 初始化库存（测试用）
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(stockKey, "10");  // 初始库存为 10
        }

        // 模拟并发扣减
        final int threadNum = 30;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            final int id = i + 1;
            threadPool.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                boolean success = deductStock(stockKey);
                long time = System.currentTimeMillis();
                System.out.printf(time + " 扣减尝试 %d: %s%n", id, success ? "成功" : "失败");
            });
        }
        Thread.sleep(3000);
        threadPool.shutdown();
    }
}

/*
Sample output for test1:
扣减尝试 1: 成功
扣减尝试 2: 成功
扣减尝试 3: 成功
扣减尝试 4: 成功
扣减尝试 5: 成功
扣减尝试 6: 成功
扣减尝试 7: 成功
扣减尝试 8: 成功
扣减尝试 9: 成功
扣减尝试 10: 成功
扣减尝试 11: 失败
扣减尝试 12: 失败
扣减尝试 13: 失败
扣减尝试 14: 失败
扣减尝试 15: 失败


Sample output for test2: (totally 10 success)
1749956479027 扣减尝试 27: 失败
1749956479044 扣减尝试 17: 失败
1749956479044 扣减尝试 8: 失败
1749956479041 扣减尝试 30: 失败
1749956479041 扣减尝试 29: 失败
1749956479041 扣减尝试 28: 失败
1749956479026 扣减尝试 14: 失败
1749956479026 扣减尝试 5: 成功
1749956479040 扣减尝试 20: 失败
1749956479026 扣减尝试 10: 成功
1749956479040 扣减尝试 25: 失败
1749956479026 扣减尝试 13: 成功
1749956479026 扣减尝试 15: 成功
1749956479027 扣减尝试 6: 成功
1749956479026 扣减尝试 18: 成功
1749956479030 扣减尝试 21: 失败
1749956479025 扣减尝试 3: 成功
1749956479029 扣减尝试 16: 失败
1749956479027 扣减尝试 11: 失败
1749956479029 扣减尝试 23: 失败
1749956479027 扣减尝试 1: 失败
1749956479029 扣减尝试 26: 失败
1749956479029 扣减尝试 22: 失败
1749956479027 扣减尝试 9: 失败
1749956479027 扣减尝试 4: 失败
1749956479029 扣减尝试 24: 失败
1749956479027 扣减尝试 12: 成功
1749956479026 扣减尝试 19: 成功
1749956479026 扣减尝试 2: 成功
1749956479027 扣减尝试 7: 失败
 */