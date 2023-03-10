package com.demo;

import redis.clients.jedis.Jedis;

public class Demo1_setnx {
    public static void main(String[] args) {
        // 同时开启20个线程模拟20个分布式节点并发去获取 redis实现的分布式锁，最终只有1个获取成功
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                Jedis jedis = new Jedis();
                String currentThread = Thread.currentThread().getName();

                // 支持超时释放
                RedisDistributedLock lock = new RedisDistributedLock(jedis, "lockKey", "lockValue", 60);
                boolean lockSuccess = false;
                try {
                    if (lockSuccess = lock.acquire()) {
                        System.out.println(currentThread + " acquired lock");
                        // 模拟获取到锁后执行操作
                        Thread.sleep(8000);
                    } else {
                        // 未获取到锁的处理
                        System.out.println(currentThread + " failed to acquire lock");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (lockSuccess) {
                        lock.release();
                        System.out.println(currentThread + " released the lock");
                    }
                }
            }).start();
        }
    }
}
