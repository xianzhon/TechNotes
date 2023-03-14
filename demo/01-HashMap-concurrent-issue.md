# 演示HashMap 1.8 并发安全问题

HashMap 是 JDK 中的一个保存键值对的容器，JDK 官方文档说它不是线程安全的，如果要在多线程下使用的话，会有线程安全问题。那么会有什么样的线程安全问题呢？


## 并发问题1 - size不对

当多个线程同时对同一个 `HashMap` 进行修改操作时，可能会导致数据不一致的问题。下面是一个简单的例子，演示了在 JDK 1.8 版本下 `HashMap` 并发写操作的问题：

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapDemo {

    public static void main(String[] args) throws InterruptedException {
        final Map<String, String> map = new HashMap<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                map.put("key" + i, "value" + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                map.put("key" + i, "value" + i + "_updated");
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(map.size());
    }
}
// 程序每次运行的结果不一样, 例如3次运行结果
// 1416  1034  1087
```

在上面的例子中，创建了一个 `HashMap` 对象，然后启动了两个线程 `t1` 和 `t2`，分别向 `HashMap` 中添加 1000 个键值对，并且键相同但是值不同。最后输出 `HashMap` 的大小。

由于 `HashMap` 不是线程安全的，因此当多个线程同时对 `HashMap` 进行修改时，会导致数据不一致的问题。在上面的例子中，当两个线程同时向 `HashMap` 中添加键值对时，可能会**出现数据覆盖** 的情况。如果两个线程同时修改同一个键的值，那么最后存储在 `HashMap` 中的值就是不确定的。这样就会导致最后输出的 `HashMap` 大小与预期不一致，从而引发错误。

上面这个例子中，如果把循环的次数由1000改成100，甚至改成10也会出现size的大小大于期望值的情况。


**原因分析**：

在多线程环境下，如果多个线程同时对 `HashMap` 进行插入操作，那么就有可能导致 `HashMap` 中的 `size` 大于预期的值。这是因为 `HashMap` 中的 `size` 计算是基于链表和红黑树的节点数进行统计的，而在多线程环境下，如果同时对同一个链表或红黑树进行插入操作，就会出现节点重复计数的情况，从而导致 `size` 计算不准确。

因为多线程可见性问题，以及原子性问题，导致size的更新不是原子的。如果两个线程都判断没有keyX，然后都执行了插入，那么size就增加了两次。

## 并发问题2 - 数据覆盖

多个线程同时对同一个键进行修改时，由于线程的执行顺序是不确定的，因此最终存储在 `HashMap` 中的值可能是不确定的。这样就可能导致数据覆盖的问题。

下面的例子，演示了在多线程环境下使用 `HashMap` 可能导致的数据覆盖问题：

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapDemo {

    public static void main(String[] args) throws InterruptedException {
        final Map<String, String> map = new HashMap<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                map.put("key", "value1");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                map.put("key", "value2");
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(map.get("key"));
    }
}
```

在上面的例子中，我们创建了一个 `HashMap` 对象，然后启动了两个线程 `t1` 和 `t2`，分别向 `HashMap` 中添加键为 `"key"`，值分别为 `"value1"` 和 `"value2"` 的键值对。最后输出键为 `"key"` 的值。

由于两个线程同时对键为 `"key"` 的值进行修改，因此最终存储在 `HashMap` 中的值是不确定的，有可能是 `"value1"`，也有可能是 `"value2"`。这样就可能导致程序出现逻辑错误，输出的值可能与预期不一致。


因此，在多线程环境下应该使用线程安全的数据结构，如 `ConcurrentHashMap`，来避免这种问题的发生。
