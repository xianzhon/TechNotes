package com.demo;

import java.util.concurrent.TimeUnit;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Demo2_Redission {
    public static void main(String[] args) {

        // 同时开启20个线程模拟20个分布式节点并发去获取 redis实现的分布式锁，最终只有1个获取成功
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                String currentThread = Thread.currentThread().getName();

                // 初始化 Redission 客户端
                Config config = new Config();
                config.useSingleServer().setAddress("redis://localhost:6379");
                RedissonClient client = Redisson.create(config);

                // 获取分布式锁对象
                RLock lock = client.getLock("myLock");

                // 尝试加锁，最多等待 10 秒钟
                boolean locked = false;
                try {
                    locked = lock.tryLock(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (locked) {
                    try {
                        // 如果加锁成功，执行对应的操作
                        System.out.println(currentThread + " acquired lock");
                        // 模拟获取到锁后执行操作
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        // 释放锁
                        lock.unlock();
                        System.out.println(currentThread + " released the lock");
                    }
                } else {
                    // 如果加锁失败，执行对应的处理逻辑
                    System.out.println(currentThread + " failed to acquire lock");
                }

            }).start();
        }
    }
}
