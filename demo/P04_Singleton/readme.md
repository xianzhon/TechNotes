# Java 实现单例模式的几种方式


## 一、引言

### 1. 设计模式简介

设计模式是软件设计中常见问题的典型解决方案，是前人在软件开发过程中总结出的最佳实践。它们类似于建筑领域的蓝图，提供了`可重用的设计方案`，而不是具体的代码实现。设计模式主要分为三大类：

- **创建型模式**：处理对象创建机制（如单例、工厂、建造者模式）
- **结构型模式**：处理对象组合（如适配器、装饰器、代理模式）
- **行为型模式**：处理对象间的交互和职责分配（如观察者、策略、命令模式）

### 2. 单例模式的定义

单例模式(Singleton Pattern)是一种`创建型`设计模式，它确保一个类**只有一个实例**，并提供一个**全局访问点**。其核心特点包括：

- 私有化构造函数：防止外部通过new创建实例
- 静态私有成员变量：保存类的唯一实例
- 静态公有方法：提供全局访问入口

单例模式的UML类图非常简单，只包含一个类，这个类拥有一个指向自身实例的静态引用和一个获取该实例的静态方法。

![image-20250427172218697](https://i.hish.top:8/2025/04/27/172218.png)

### 3. 单例模式的应用场景

单例模式适用于`需要全局唯一对象`的场景，特别是那些`资源密集或需要严格控制`的场景：

**典型应用场景包括**：

- **配置管理**：全局配置对象（如数据库配置、应用设置）
```java
// 例如：全局配置管理器
ConfigManager config = ConfigManager.getInstance();
String dbUrl = config.get("database.url");
```

- **日志系统**：保证所有日志都通过同一个日志对象记录
```java
public final class DAOFactory {
    private static final Logger logger = Logger.getLogger(DAOFactory.class);

    void someMethod() {
        logger.info("bala bala");
    }
}
```

- **资源访问**：数据库连接池、线程池等
```java
// 数据库连接池使用单例
ConnectionPool pool = ConnectionPool.getInstance();
Connection conn = pool.getConnection();
```

- **缓存系统**：全局缓存管理
```java
CacheManager cache = CacheManager.getInstance();
cache.put("user:123", userData);
```

- **硬件访问**：如打印机、显卡等设备控制
```java
PrintSpooler spooler = PrintSpooler.getInstance();
spooler.print(document);
```

**使用单例模式的优势**：

- 严格控制实例数量，节约系统资源
- 避免对资源的多重占用
- 提供统一的访问入口，便于管理

**需要注意的场合**：

- 不适合有扩展需求的情况（需要考虑使用工厂模式）
- 在分布式系统中，单机单例可能不够
- 过度使用会导致代码耦合度高，难以测试



## 二、单例模式的基本实现

### 1. 饿汉式单例 (Eager Initialization)

#### 实现原理
饿汉式单例在类加载时就立即创建实例，是一种`急切加载的方式`。它利用`JVM类加载机制`保证线程安全（类加载过程是线程安全的）。

#### 代码示例
```java
public class Singleton {
    // 在类加载时就初始化实例
    private static final Singleton instance = new Singleton();

    // 私有化构造函数
    private Singleton() {
        // 防止通过反射创建实例
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    // 提供全局访问点
    public static Singleton getInstance() {
        return instance;
    }
}
```

#### 优缺点分析
**优点：**

- 实现简单，代码直观
- 线程安全（由JVM类加载机制保证）
- 没有同步开销，性能好

**缺点：**

- 类加载时就创建实例，如果实例很大且从未使用，会浪费内存
- 不能延迟加载，如果`单例的初始化依赖外部参数`，这种方式不适用

**适用场景：**

- 单例对象较小且一定会被使用
- 对性能要求较高的场景

### 2. 懒汉式单例 (Lazy Initialization)

#### 基础实现（线程不安全）
懒汉式单例延迟了实例的创建，在第一次调用时才初始化实例。

#### 代码示例（线程不安全版本）
```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {
        // 防止反射攻击
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

#### 优缺点分析
**优点：**
- 延迟加载，节省系统资源
- 实现相对简单

**缺点（基础版本的问题）：**

- 线程不安全，在多线程环境下可能创建多个实例
- 需要额外的同步机制来保证线程安全

**线程不安全示例：**

```java
// 两个线程同时执行以下代码可能创建两个实例
if (instance == null) {  // 线程A和线程B同时判断为true
    instance = new Singleton();  // 都会执行创建
}
```

**适用场景：**
- 单线程环境
- 对性能要求不高且可以接受偶尔创建多个实例的场景（不推荐）

#### 线程安全改进方案（简单同步方法）
```java
public synchronized static Singleton getInstance() {
    if (instance == null) {
        instance = new Singleton();
    }
    return instance;
}
```
这种改进虽然解决了线程安全问题，但每次获取实例都需要同步，性能较差。



## 三、线程安全的单例实现

### 1. 同步方法懒汉式 (Synchronized Method)

#### 实现方式
通过在getInstance()方法上添加`synchronized`关键字来实现线程安全：

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    // 同步方法保证线程安全
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

#### 性能问题
- **锁粒度大**：每次调用getInstance()都会同步，即使实例已经创建
- **性能瓶颈**：在高并发场景下会成为系统瓶颈
- **测试数据**：比非同步方法慢约100倍（基于简单基准测试）

**适用场景**：

- 对性能要求不高的简单应用
- 单例初始化后很少被调用的场景

### 2. 双重检查锁定 (Double-Checked Locking)

#### 实现原理
结合了延迟加载和线程安全，通过两次检查减少同步块的使用：

1. 第一次检查不加锁（提高性能）
2. 第二次检查在同步块内（保证线程安全）
3. 使用`volatile`防止指令重排序、保证变量可见性

#### volatile关键字的作用【重要！！】
- **可见性**：保证多线程环境下变量的可见性
- **禁止重排序**：防止new操作被JVM重排序导致其他线程获取未初始化完全的实例（下面 b 和 c 指令重排了）
	- new 一个对象的三步操作：a. 申请内存空间，b. 调用构造方法，c. 返回对象的引用

- **内存屏障**：确保写操作先于读操作

#### 代码示例
```java
public class Singleton {
    // volatile保证可见性和禁止指令重排序
    private volatile static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        // 第一次检查（无锁）
        if (instance == null) {
            synchronized (Singleton.class) {
                // 第二次检查（有锁）
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### 3. 静态内部类实现 (Initialization-on-demand Holder / Static Holder)

#### 实现原理
利用JVM类加载机制：
1. 静态内部类在`首次被引用`时才会加载
2. JVM保证类加载过程的线程安全
3. 实现延迟加载且无同步开销

#### 代码示例
```java
public class Singleton {
    private Singleton() {}

    // 静态内部类
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

#### 优点分析
- **线程安全**：由`JVM类加载机制`保证
- **延迟加载**：只有调用getInstance()时才会加载内部类
- **无同步开销**：不需要任何同步机制
- **代码简洁**：实现简单直观

**与双重检查锁定的比较**：

- 性能相当（都只有第一次访问需要同步）
- 静态内部类实现更简洁，不易出错



#### 静态内部类实现单例的缺点

##### 1. 无法防止反射攻击（严重缺陷）

**问题本质**：静态内部类无法阻止通过反射机制调用私有构造方法创建新实例

**示例代码**：

```java
public static void breakSingleton() throws Exception {
    InnerClassSingleton instance1 = InnerClassSingleton.getInstance();

    // 通过反射获取构造方法
    Constructor<InnerClassSingleton> constructor =
        InnerClassSingleton.class.getDeclaredConstructor();
    constructor.setAccessible(true);  // 突破私有限制

    // 创建第二个实例
    InnerClassSingleton instance2 = constructor.newInstance();

    System.out.println(instance1 == instance2);  // 输出false，单例被破坏
}
```

**解决方案**：可以在构造方法中添加防御代码：

```java
private InnerClassSingleton() {
    if (SingletonHolder.INSTANCE != null) {
        throw new RuntimeException("不允许通过反射创建实例");
    }
}
```

Demo code: [DemoBreakInnerClassSingleton.java](./DemoBreakInnerClassSingleton.java) and [InnerClassSingleton.java](./InnerClassSingleton.java)



##### 2. 序列化/反序列化问题

**问题表现**：当单例类实现 Serializable 接口时，反序列化会创建新实例

**测试案例**：

```java
// 序列化
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("singleton.ser"));
oos.writeObject(InnerClassSingleton.getInstance());

// 反序列化
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("singleton.ser"));
InnerClassSingleton newInstance = (InnerClassSingleton) ois.readObject();

System.out.println(instance == newInstance);  // 输出false
```

**解决方案**：需要实现readResolve方法：

当单例类实现Serializable接口时，默认的反序列化机制会创建新的对象实例，从而破坏单例模式。readResolve()方法可以解决这个问题，返回已有的单例实例，替代反序列化创建的新对象：

```java
private Object readResolve() {
    return SingletonHolder.INSTANCE;
}
```

Demo code: [DemoBreakInnerClassSingleton.java](./DemoBreakInnerClassSingleton.java) and [InnerClassSingleton.java](./InnerClassSingleton.java)

工作原理：

- 反序列化过程中，ObjectInputStream会检查类是否定义了readResolve()方法

- 如果存在，则调用该方法并使用其返回值替代新创建的对象

- 新创建的对象会被垃圾回收


注意事项：

- 所有实例字段都应该声明为transient，因为反序列化时会忽略这些字段的值

- 如果使用枚举单例，则自动具备序列化安全性，无需额外处理【对比可见，枚举实现单例的好处之一】



##### 3. 初始化参数传递受限【重要！】

**局限性**：静态内部类的初始化时机由JVM控制，无法在运行时传递初始化参数

**错误示例**：

```java
// 这种需求无法用静态内部类实现
public static InnerClassSingleton getInstance(String config) {
    // 无法将config传递给内部类的INSTANCE
}
```

##### 4. 类加载器问题（特殊场景）

**多类加载器环境**：当存在多个类加载器时，每个类加载器都会加载自己的静态内部类实例

**产生场景**：

- Web容器（如Tomcat）的多级类加载机制
- OSGi等模块化框架
- 自定义类加载器场景

##### 5. 内存泄漏风险（长期存在）

**问题原因**：静态内部类会持有外部类的引用，如果单例对象很大且生命周期长，可能导致内存无法释放

**典型场景**：

- Android开发中Activity的静态内部类单例
- 长期运行的服务器程序

##### 6. 初始化异常处理困难

**问题表现**：静态内部类的初始化失败会导致`NoClassDefFoundError`，难以优雅处理

**示例情况**：
```java
private static class SingletonHolder {
    // 如果初始化抛出异常，后续调用都会失败
    private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();

    static {
        if(someCondition) {
            throw new RuntimeException("初始化失败");
        }
    }
}
```

demo code: [DemoInnerClassSingletonNoClassDefFoundError.java](./DemoInnerClassSingletonNoClassDefFoundError.java)

##### 各场景下的替代方案建议

| 问题场景           | 推荐替代方案           |
| ------------------ | ---------------------- |
| 需要防反射         | 枚举单例               |
| 需要序列化         | 枚举或实现readResolve  |
| 需要传递初始化参数 | 双重检查锁定           |
| 多类加载器环境     | 使用特定类加载器或枚举 |
| 内存敏感场景       | 弱引用或非静态实现     |

**总结建议**：
静态内部类实现适合大多数常规场景，但在需要防御性编程（如安全敏感系统）或特殊环境（如需要传递初始化参数）时，应考虑双重检查锁定或枚举实现。在框架开发或库设计时，应特别注意类加载器问题。


### 4. 枚举单例 (Enum Singleton)

#### 实现方式
利用枚举类型的语言特性实现单例：

```java
public enum EnumSingleton {
    INSTANCE;

    // 可以添加方法
    public void doSomething() {
        System.out.println("Singleton method");
    }
}
```

#### 防止反射攻击的优势
- **天生防反射**：JVM保证枚举实例只会被初始化一次
- **自动序列化安全**：枚举的序列化由JVM特殊处理
- **防止克隆**：枚举类型天然禁止克隆

#### 代码示例（完整版）
```java
public enum EnumSingleton {
    INSTANCE;

    private String connection;

    private EnumSingleton() {
        // 初始化工作
        connection = "Database Connection";
    }

    public String getConnection() {
        return connection;
    }

    // 使用方式
    public static void main(String[] args) {
        String conn = EnumSingleton.INSTANCE.getConnection();
    }
}
```

#### 枚举单例的独特优势
1. **绝对的单例**：JVM层面保证
2. **线程安全**：枚举的初始化是线程安全的
3. **序列化安全**：自动处理序列化和反序列化
4. **代码极简**：一行代码实现单例

**Joshua Bloch（《Effective Java》作者）推荐**：

> "单元素的枚举类型已经成为实现Singleton的最佳方法"

### 各实现方式对比表

| 实现方式       | 线程安全 | 延迟加载 | 防止反射 | 序列化安全 | 性能 |
| -------------- | -------- | -------- | -------- | ---------- | ---- |
| 同步方法懒汉式 | ✓        | ✓        | ✗        | ✗          | 差   |
| 双重检查锁定   | ✓        | ✓        | ✗        | ✗          | 优   |
| 静态内部类     | ✓        | ✓        | ✗        | ✗          | 优   |
| 枚举           | ✓        | ✗        | ✓        | ✓          | 最优 |



### JDK 源码中出现的 Singleton

##### math.random() - static holder

```java
    public static double random() {
        return RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
    }

    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();
    }
```

##### java.lang.Runtime - 饿汉式单例

```java
public class Runtime {
    private static final Runtime currentRuntime = new Runtime();
    public static Runtime getRuntime() {
        return currentRuntime;
    }

    private Runtime() {}
}
```

##### java.util.Collections - 饿汉式单例

```java
public class Collections {
    // Suppresses default constructor, ensuring non-instantiability.
    private Collections() {
    }

	public static final List EMPTY_LIST = new EmptyList<>();
    public static final <T> List<T> emptyList() {
        return (List<T>) EMPTY_LIST;
    }
}
```



## 四、单例模式的进阶话题

### 1. 单例模式的序列化问题

#### readResolve()方法的作用

当单例类实现Serializable接口时，默认的反序列化机制会创建新的对象实例，从而破坏单例模式。readResolve()方法可以解决这个问题：

**解决方案**：

```java
private Object readResolve() throws ObjectStreamException {
    return instance; // 返回已有的单例实例，替代反序列化创建的新对象
}
```

**工作原理**：

1. 反序列化过程中，ObjectInputStream会检查类是否定义了readResolve()方法
2. 如果存在，则调用该方法并使用其返回值替代新创建的对象
3. 新创建的对象会被垃圾回收

**注意事项**：

- 所有实例字段都应该声明为transient，因为反序列化时会忽略这些字段的值
- 如果使用枚举单例，则自动具备序列化安全性，无需额外处理

### 2. 防止反射攻击

#### 私有构造函数中的防御代码

即使构造函数是私有的，反射API仍然可以突破访问限制创建新实例：

**攻击示例**：

```java
public class ReflectionAttack {
    public static void main(String[] args) throws Exception {
        EagerSingleton instance1 = EagerSingleton.getInstance();

        Constructor<EagerSingleton> constructor = EagerSingleton.class.getDeclaredConstructor();
        constructor.setAccessible(true); // 突破私有限制
        EagerSingleton instance2 = constructor.newInstance();

        System.out.println(instance1 == instance2); // 输出false
    }
}
```

**防御方案**：

**方案一：标志位检查**

```java
public class ReflectionSafeSingleton {
    private static volatile boolean initialized = false;
    private static final ReflectionSafeSingleton instance = new ReflectionSafeSingleton();

    private ReflectionSafeSingleton() {
        synchronized (ReflectionSafeSingleton.class) {
            if (initialized) {
                throw new RuntimeException("禁止通过反射创建实例");
            }
            initialized = true;
        }
    }
}
```

**方案二：运行时计数检查（更严格）**

```java
private static int instanceCount = 0;
private ReflectionSafeSingleton() {
    if (++instanceCount > 1) {
        throw new RuntimeException("单例模式禁止创建多个实例");
    }
}
```

**方案三：枚举防御（最佳方案）**
```java
public enum EnumSingleton {
    INSTANCE;
    // 枚举天生防反射攻击
}
```

**防御要点**：
1. 在第一次实例化时设置标志位
2. 后续构造调用检查标志位并抛出异常
3. 注意多线程环境下的竞态条件（需要同步）
4. 对于饿汉式，需要在静态初始化后立即设置标志

### 3. 单例模式与类加载器

#### 多个类加载器环境下的单例

在复杂的类加载器体系中，单例可能在不同类加载器空间中存在多个实例：

**问题场景**：

```java
// 使用不同类加载器加载同一个类
ClassLoader classLoader1 = new URLClassLoader(urls);
ClassLoader classLoader2 = new URLClassLoader(urls);

Class<?> clazz1 = classLoader1.loadClass("com.example.Singleton");
Class<?> clazz2 = classLoader2.loadClass("com.example.Singleton");

Object instance1 = clazz1.getMethod("getInstance").invoke(null);
Object instance2 = clazz2.getMethod("getInstance").invoke(null);

System.out.println(instance1 == instance2); // 输出false
```

**解决方案**：

**方案一：指定类加载器**

```java
public class ClassLoaderAwareSingleton {
    private static ClassLoaderAwareSingleton instance;
    private static ClassLoader preferredClassLoader;

    public static void setPreferredClassLoader(ClassLoader loader) {
        preferredClassLoader = loader;
    }

    public synchronized static ClassLoaderAwareSingleton getInstance() {
        if (instance == null) {
            ClassLoader loader = preferredClassLoader != null ?
                preferredClassLoader : Thread.currentThread().getContextClassLoader();

            try {
                Class<?> clazz = loader.loadClass(ClassLoaderAwareSingleton.class.getName());
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                instance = (ClassLoaderAwareSingleton) constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("初始化失败", e);
            }
        }
        return instance;
    }
}
```

**方案二：使用上下文类加载器**
```java
public static synchronized Singleton getInstance() {
    if (instance == null) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> clazz = cl.loadClass(Singleton.class.getName());
            Field instanceField = clazz.getDeclaredField("instance");
            instance = (Singleton) instanceField.get(null);
        } catch (Exception e) {
            instance = new Singleton(); // 回退方案
        }
    }
    return instance;
}
```

**方案三：使用OSGi服务（适用于模块化系统）**

```java
// 在OSGi环境中注册服务
BundleContext context = FrameworkUtil.getBundle(Singleton.class).getBundleContext();
context.registerService(Singleton.class.getName(), new Singleton(), null);

// 获取服务
ServiceReference<Singleton> ref = context.getServiceReference(Singleton.class);
Singleton singleton = context.getService(ref);
```

**最佳实践建议**：

1. 在Web容器中，考虑将单例存储在ServletContext属性中
2. 在OSGi环境中，使用服务注册机制替代传统单例
3. 对于框架开发，明确文档说明类加载器要求
4. 考虑使用依赖注入框架（如Spring）管理单例生命周期

**类加载器影响总结**：

| 场景              | 表现               | 解决方案               |
| ----------------- | ------------------ | ---------------------- |
| 相同类加载器      | 正常工作           | 无需特殊处理           |
| 不同类加载器      | 多个实例           | 指定统一类加载器       |
| Web容器(如Tomcat) | 每个WebApp一个实例 | 使用ServletContext共享 |
| OSGi环境          | 每个Bundle一个实例 | 使用OSGi服务注册       |
| 热部署环境        | 重新加载后新实例   | 实现重新加载感知逻辑   |



## 五、单例模式的最佳实践

### 1. 各种实现方式的比较

以下是主流单例实现方式的详细对比表格：

| 实现方式        | 线程安全 | 延迟加载 | 防反射 | 序列化安全 | 性能 | 代码复杂度 | 参数传递 | 类加载器安全 |
| --------------- | -------- | -------- | ------ | ---------- | ---- | ---------- | -------- | ------------ |
| 饿汉式          | ✓        | ✗        | ✗      | ✗          | 优   | 低         | ✗        | ✗            |
| 同步方法懒汉式  | ✓        | ✓        | ✗      | ✗          | 差   | 低         | ✓        | ✗            |
| 双重检查锁定    | ✓        | ✓        | ✗      | ✗          | 优   | 中         | ✓        | ✗            |
| 静态内部类      | ✓        | ✓        | ✗      | ✗          | 优   | 低         | ✗        | ✗            |
| 枚举            | ✓        | ✗        | ✓      | ✓          | 最优 | 最低       | ✗        | ✗            |
| ThreadLocal单例 | 线程隔离 | ✓        | ✗      | ✗          | 良   | 中         | ✓        | ✗            |

**关键指标说明**：
- **性能**：枚举 > 静态内部类 ≈ 双重检查锁定 > 饿汉式 > ThreadLocal > 同步方法懒汉式
- **安全性**：枚举 > 双重检查锁定(加volatile) > 静态内部类 > 其他
- **灵活性**：双重检查锁定 > 同步方法懒汉式 > 其他



### 2. 不同场景下的选择建议

#### (1) 常规Java应用场景选择
- **安全优先**：首选枚举方式（Effective Java推荐）
```java
public enum SafeSingleton {
    INSTANCE;
    // 业务方法
    public void service() { ... }
}
```

- **性能敏感**：静态内部类或双重检查锁定
```java
// 静态内部类方案
public class PerformanceSingleton {
    private static class Holder {
        static final PerformanceSingleton INSTANCE = new PerformanceSingleton();
    }
    public static PerformanceSingleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

- **需要参数初始化**：双重检查锁定变体
```java
public class ConfigurableSingleton {
    private volatile static ConfigurableSingleton instance;
    private final String config;

    private ConfigurableSingleton(String config) {
        this.config = config;
    }

    public static void init(String config) {
        if (instance == null) {
            synchronized (ConfigurableSingleton.class) {
                if (instance == null) {
                    instance = new ConfigurableSingleton(config);
                }
            }
        }
    }
}
```

#### (2) 特殊环境选择
- **Web容器环境**：结合ServletContext
```java
public class WebAppSingleton {
    private static final String KEY = "com.example.WebAppSingleton";

    public static WebAppSingleton getInstance(ServletContext context) {
        WebAppSingleton instance = (WebAppSingleton) context.getAttribute(KEY);
        if (instance == null) {
            synchronized (WebAppSingleton.class) {
                instance = (WebAppSingleton) context.getAttribute(KEY);
                if (instance == null) {
                    instance = new WebAppSingleton();
                    context.setAttribute(KEY, instance);
                }
            }
        }
        return instance;
    }
}
```

- **Android开发**：使用Application上下文
```java
public class AppSingleton {
    private static AppSingleton instance;

    public static void init(Application app) {
        instance = new AppSingleton(app);
    }

    public static AppSingleton getInstance() {
        if (instance == null) {
            throw new IllegalStateException("请先在Application中初始化");
        }
        return instance;
    }
}
```

- **分布式系统**：改用分布式缓存（Redis等）
```java
public class DistributedSingleton {
    private static final String LOCK_KEY = "global:singleton:lock";

    public static void doSingletonTask(Jedis jedis) {
        try {
            // 获取分布式锁
            while (!"OK".equals(jedis.set(LOCK_KEY, "1", "NX", "EX", 30))) {
                Thread.sleep(100);
            }
            // 执行单例任务
            // ...
        } finally {
            jedis.del(LOCK_KEY);
        }
    }
}
```

### 3. Spring框架中的单例模式

#### (1) Spring单例的特点
- **容器级单例**：每个Spring容器内保证唯一
- **不同于传统单例**：Spring单例是相对于IoC容器而言，不是JVM级
- **默认作用域**：Spring Bean默认就是单例作用域

```java
@Service // 默认就是单例
public class OrderService {
    // 服务实现
}
```

#### (2) Spring单例的实现机制
- **三级缓存解决循环依赖**：

  ```mermaid
  graph LR
    A[创建Bean] --> B[放入三级缓存]
    B --> C[属性注入]
    C --> D[初始化]
    D --> E[放入一级缓存]
  ```

- **关键源码片段**（简化版）：

  ```java
  public class DefaultSingletonBeanRegistry {
      // 一级缓存：完整Bean
      private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
      // 二级缓存：早期引用（解决循环依赖）
      private final Map<String, Object> earlySingletonObjects = new HashMap<>();
      // 三级缓存：ObjectFactory
      private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();
  }
  ```

#### (3) Spring单例的最佳实践
- **无状态设计**：推荐将单例Bean设计为无状态的

  ```java
  @Service
  public class StatelessService {
      // 使用参数传递状态，而不是成员变量
      public Result process(Request request) {
          // ...
      }
  }
  ```

- **需要状态时的线程安全处理**：

  ```java
  @Service
  @Scope("singleton")
  public class CounterService {
      private final AtomicLong counter = new AtomicLong();

      public long increment() {
          return counter.incrementAndGet();
      }
  }
  ```

- **与@Configuration的特殊配合**：

  ```java
  @Configuration
  public class AppConfig {
      @Bean
      @Scope("singleton")
      public DataSource dataSource() {
          // 只会被调用一次
          return new HikariDataSource();
      }
  }
  ```

#### (4) Spring单例的局限与解决方案
- **问题**：多个Spring容器会有多个实例
- **解决方案**：使用父容器或静态变量

  ```java
  public class GlobalSingleton {
      private static volatile GlobalSingleton instance;

      @Bean
      public GlobalSingleton globalSingleton() {
          if (instance == null) {
              synchronized (GlobalSingleton.class) {
                  if (instance == null) {
                      instance = new GlobalSingleton();
                  }
              }
          }
          return instance;
      }
  }
  ```

**Spring环境选择建议**：
1. 常规Bean直接使用`@Service`/`@Component`等注解
2. 需要复杂初始化时使用`@Bean`方法
3. 与框架整合时考虑`FactoryBean`接口
4. 分布式环境考虑`@Scope("singleton", proxyMode=ScopedProxyMode.TARGET_CLASS)`



## 六、单例模式的测试

### 1. 如何测试单例类

#### 基础测试方法

**单元测试单例的基本属性**：

```java
public class SingletonTest {
    @Test
    public void testSingletonInstance() {
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();

        // 测试唯一性
        assertSame("两个实例应该相同", instance1, instance2);

        // 测试非空
        assertNotNull("实例不应为null", instance1);
    }
}
```

**测试序列化安全性**：
```java
@Test
public void testSerializationSafety() throws Exception {
    Singleton instance1 = Singleton.getInstance();

    // 序列化
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(instance1);

    // 反序列化
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bis);
    Singleton instance2 = (Singleton) ois.readObject();

    assertSame("反序列化后实例应相同", instance1, instance2);
}
```

**测试反射攻击防御**：
```java
@Test(expected = RuntimeException.class)
public void testReflectionAttack() throws Exception {
    Singleton instance1 = Singleton.getInstance();

    Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Singleton instance2 = constructor.newInstance(); // 应该抛出异常

    fail("反射攻击应该被阻止");
}
```



### 2. 多线程环境下的测试方法

#### 基础多线程测试

**使用线程池测试并发访问**：

```java
@Test
public void testConcurrentAccess() throws InterruptedException {
    final int THREAD_COUNT = 100;
    final Set<Singleton> instances = Collections.synchronizedSet(new HashSet<>());
    final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    for (int i = 0; i < THREAD_COUNT; i++) {
        executor.execute(() -> {
            instances.add(Singleton.getInstance());
            latch.countDown();
        });
    }

    latch.await(5, TimeUnit.SECONDS);
    executor.shutdown();

    assertEquals("所有线程应获取同一个实例", 1, instances.size());
}
```

#### 测试单例初始化性能

**测试初始化延迟**：

```java
@Test
public void testInitializationPerformance() {
    long startTime = System.nanoTime();
    Singleton.getInstance();
    long duration = System.nanoTime() - startTime;

    assertTrue("初始化时间应小于1ms", duration < 1_000_000);

    // 测试后续访问性能
    startTime = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
        Singleton.getInstance();
    }
    duration = System.nanoTime() - startTime;
    double avgTime = duration / 1000.0;

    assertTrue("平均访问时间应小于100ns", avgTime < 100);
}
```

**最佳实践建议**：

1. 对单例类至少进行单线程正确性和多线程安全性测试
2. 性能敏感的单例需要JMH基准测试
3. 使用反射测试防御性代码（如防反射攻击）
4. 对于有状态的单例，需要测试状态隔离性
5. 考虑使用代码覆盖率工具（如JaCoCo）确保测试完整性



## 七、总结

### 1. 单例模式的优缺点总结

#### 优点

**资源管理优势**：
- 严格控制实例数量，节省系统资源（如数据库连接）
- 减少频繁创建销毁对象的开销（性能敏感场景）
- 避免对共享资源的重复占用（如配置文件访问）

**访问控制优势**：
- 提供全局唯一访问点，便于集中管理
- 可以轻松扩展为有限数量的实例（多例模式）
- 比全局变量更安全（可以延迟初始化）

**设计优势**：
- 符合单一职责原则（控制实例化+业务逻辑）
- 替代全局变量，避免命名空间污染
- 某些场景下可以简化系统设计（如服务定位器）

#### 缺点

**设计问题**：
- 不适用于变化频繁的对象
- 也正是因为系统中只有一个实例，这样就导致了单例类的职责过重，违背了“单一职责原则”
- 过度使用会导致代码耦合度高（隐藏依赖关系）
- 不利于面向接口编程（通常直接访问具体类），不方便扩展

**技术限制**：

- 多线程环境需要额外同步处理（除枚举和静态内部类外）
- 单元测试困难（难以mock和重置状态）
- 序列化/反序列化可能破坏单例性（需额外处理）

**架构问题**：
- 在分布式系统中效果有限（每JVM一个实例）
- 不适合需要扩展的场景（违反开闭原则）
- 可能成为系统瓶颈（高并发访问时）

### 2. 使用单例模式的注意事项

#### 设计阶段注意事项

**必要性评估**：
```java
// 在决定使用单例前先考虑：
// 1. 是否真的需要全局唯一实例？
// 2. 是否可能未来需要多个实例？
// 3. 是否会导致不合理的耦合？
```

**实现选择**：

- 线程安全需求 → 枚举或静态内部类
- 序列化需求 → 实现readResolve或直接使用枚举
- 性能要求 → 避免同步方法懒汉式
- 依赖注入 → 考虑框架管理生命周期

#### 编码实践注意事项

**资源管理**：
```java
public class DatabasePool {
    private static DatabasePool instance;
    private Connection[] pool;

    // 添加关闭方法处理资源
    public synchronized void shutdown() {
        // 关闭所有连接
        // 重置instance为null（需getInstance支持重新初始化）
    }
}
```

**状态管理**：
```java
public class AppConfig {
    private static AppConfig instance;
    // 使用不可变对象保存状态
    private final ImmutableMap<String, String> config;

    private AppConfig() {
        this.config = loadConfig(); // 只初始化一次
    }

    // 提供无状态访问方法
    public String getConfig(String key) {
        return config.get(key);
    }
}
```

**测试友好设计**：
```java
public class PaymentService {
    private static PaymentService instance;
    // 允许测试时重置实例
    static void resetInstance() {
        instance = null;
    }

    // 允许注入mock依赖
    public void setExternalService(ExternalService service) {
        this.externalService = service;
    }
}
```

#### 架构级注意事项

**分布式系统**：
- 考虑使用分布式缓存（Redis）替代JVM单例
- 或者使用集群范围内的同步机制（ZooKeeper）

**微服务架构**：
```java
// 将单例服务拆分为独立微服务
@RestController
public class InventoryService {
    // 通过服务注册中心保证唯一实例
    @GetMapping("/stock/{id}")
    public int getStock(@PathVariable String id) {
        // ...
    }
}
```

**依赖注入框架**：
```java
// 使用Spring管理单例更灵活
@Configuration
public class AppConfig {
    @Bean
    @Scope("singleton") // 默认就是singleton
    public OrderService orderService() {
        return new OrderService();
    }
}
```

### 3. 单例模式的替代方案

#### 1. 依赖注入（推荐方案）

**Spring管理单例**：
```java
@Service // 默认单例作用域
public class UserService {
    // 由Spring容器管理生命周期
    // 比传统单例更灵活、更易测试
}
```

**优势**：
- 解耦对象创建和使用
- 便于单元测试（可轻松注入mock对象）
- 支持AOP等高级特性

#### 2. 静态工具类（无状态场景）

**替代方案**：
```java
public final class MathUtils {
    private MathUtils() {} // 防止实例化

    public static double calculate(double input) {
        // 纯函数实现
    }
}
```

**适用场景**：
- 只有静态方法的工具类
- 不需要维护状态的场景

#### 3. 单例接口+多实现（扩展性方案）

**灵活实现**：
```java
public interface CacheProvider {
    void put(String key, Object value);
    Object get(String key);
}

// 测试用mock实现
public class MockCache implements CacheProvider { /*...*/ }

// 生产环境实现
public class RedisCache implements CacheProvider { /*...*/ }
```

#### 4. 上下文对象（替代全局状态）

**替代方案**：
```java
public class RequestContext {
    private static final ThreadLocal<RequestContext> context =
        ThreadLocal.withInitial(RequestContext::new);

    public static RequestContext getCurrent() {
        return context.get();
    }

    // 请求相关数据
    private User currentUser;
    // ...
}
```

#### 5. 对象池模式（资源管理场景）

**数据库连接池示例**：
```java
public class ConnectionPool {
    private static final int MAX_POOL = 10;
    private Queue<Connection> pool = new ArrayBlockingQueue<>(MAX_POOL);

    // 不是严格单例，但控制实例数量
    public Connection getConnection() {
        return pool.poll();
    }

    public void release(Connection conn) {
        pool.offer(conn);
    }
}
```

#### 6. 服务定位器模式（企业级方案）

**实现示例**：

```java
public class ServiceLocator {
    private static final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    public static <T> void register(Class<T> type, T instance) {
        services.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        return (T) services.get(type);
    }
}
```



#### 7. 单例的现代替代方案对比表

| 方案       | 优点             | 缺点             | 适用场景         |
| ---------- | ---------------- | ---------------- | ---------------- |
| 传统单例   | 简单直接         | 难以测试、扩展   | 小型应用、工具类 |
| 依赖注入   | 解耦、易测试     | 需要框架支持     | 企业级应用       |
| 静态工具类 | 无状态、线程安全 | 不能继承、无状态 | 纯函数工具       |
| 上下文对象 | 线程隔离、灵活   | 需要显式传递     | Web请求处理      |
| 对象池     | 控制实例数量     | 管理复杂         | 数据库连接等资源 |
| 服务定位器 | 动态注册         | 类型不安全       | 插件架构         |

**迁移建议路径**：
1. 新项目直接使用依赖注入框架
2. 旧项目逐步重构：
   ```mermaid
   graph LR
    传统单例 --> 服务定位器
    服务定位器 --> 依赖注入
   ```
3. 对于全局配置类，可以考虑转为不可变对象+静态方法







