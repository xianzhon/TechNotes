## Item 3. Enforce the singleton property with a private constructor or an enum type

《Effective Java 第二版》第3条 **“使用私有构造器或枚举类型强化单例模式”** 强调了如何正确实现单例模式以避免常见问题。

**文中的观点：**
- 单例的类，比较难测试，因为很难创建一个 mock 子类 【powermock + mockito 可以 mock 单例】
- 使用 `public static final T INSTANCE;` 声明的单例，跟提供一个静态方法相比，后者更加灵活一点，未来更容易改变而不影响接口使用，可能得改变有取消单例模式、每个线程返回一个实例等。
- **枚举是实现单例的最佳实践**：兼顾安全性、简洁性和功能性。

**以下是 Deepseek 给出的扩展，学习下：**
### **重要因素及原因**

1. **防止反射破坏单例**
   - **普通私有构造器的单例**（如 `static final` 实例 + 私有构造器）可能被反射调用 `setAccessible(true)` 强行创建新实例，破坏单例性。
   - **枚举单例**（`Enum`）天然阻止反射攻击，JVM 保证枚举实例的唯一性。

2. **避免序列化/反序列化破坏单例**
   - 普通单例类实现 `Serializable` 后，反序列化时会生成新实例（除非重写 `readResolve` 方法）。
   - **枚举单例** 自动处理序列化，无需额外代码即可保证唯一性。

3. **线程安全性**
   - **静态工厂方法（`getInstance`）的单例** 需额外同步（如双重检查锁），否则多线程下可能生成多个实例。
   - **枚举单例** 或 `static final` 饿汉式单例由 JVM 在类加载时初始化，天然线程安全。

4. **代码简洁性**
   - 枚举单例只需一行代码（如 `public enum Singleton { INSTANCE; }`），无需手动处理构造器、序列化或反射问题。

5. **明确性**
   - 枚举类型直接表明意图（单例），而普通类需依赖文档或约定说明其单例性质。

### **实现方式对比**

| **方式**        | **防反射** | **防序列化破坏**         | **线程安全** | **代码复杂度** |
| ------------- | ------- | ------------------ | -------- | --------- |
| 公有静态 final 实例 | ❌       | ❌（需 `readResolve`） | ✅        | 低         |
| 静态工厂方法（双重检查锁） | ❌       | ❌（需 `readResolve`） | ✅（需同步）   | 高         |
| **枚举单例**      | ✅       | ✅                  | ✅        | 极低        |

### **替代方案建议**
- **优先使用枚举单例**（如 `public enum Singleton { INSTANCE; }`）：简洁、安全且功能完备。
- 若需继承或非枚举实现，需：
  1. 私有化构造器，并在内部抛出异常防止反射调用。
  2. 实现 `readResolve` 方法防止序列化破坏。
  3. 用双重检查锁（DCL）或静态内部类保证线程安全。

### **关键结论**
- **枚举是实现单例的最佳实践**：兼顾安全性、简洁性和功能性。
- 若不能使用枚举，必须额外处理反射、序列化和线程安全问题。

---

## Item 9. Always override hashCode when you override equals

《Effective Java 第二版》第9条 **“覆盖equals时总要覆盖hashCode”** 强调了 `equals` 和 `hashCode` 方法之间的重要契约，如果不遵守会导致对象在哈希集合（如 `HashMap`、`HashSet`）中出现异常行为。

**文中观点：**
- 如果一个类 hashCode 缺失或没有正确实现，那么它就不适合放到基于 hash 的容器（HashMap的 key，HashSet，Hashtable的 key）。
- 实现 hashCode 必须要满足：Equal objects must have equal hashcodes. 【如果 equals 用到了属性 name, age, address 那么在 hashCode 中也要用到相同的属性】
- 好的 hash 函数是，让不相等的 object 尽可能有不同的 hashCode。【如果有相同的 hashCode，那么一定是 hash 冲突了，那么就会放到链表上】
- 使用 unittest 来验证 equals objects 有相同的 hashCode

**以下是 Deepseek 给出的扩展，学习下：**
### **重要因素及原因**
#### **1. 哈希集合的依赖**
- **`hashCode` 的契约要求**：
  - 如果两个对象 `equals` 返回 `true`，它们的 `hashCode` **必须相同**。
  - 如果 `equals` 返回 `false`，`hashCode` **可以不同**（不同可能提升哈希表性能）。
- **违反后果**：
  - 当对象作为 `HashMap` 的 key 或存入 `HashSet` 时，由于 `hashCode` 不一致，可能导致对象“丢失”（无法正确查找或去重）。

#### **2. 性能影响**
- **良好的 `hashCode` 实现**：应尽量让不同对象返回不同的哈希值，减少哈希冲突，提升 `HashMap`/`HashSet` 的查询效率。
- **糟糕的实现**（如所有实例返回相同哈希值）：会导致哈希表退化为链表，时间复杂度从 `O(1)` 降为 `O(n)`。

#### **3. 默认 `Object.hashCode()` 的问题**
- 默认实现（通常基于内存地址）不满足 `equals` 的逻辑一致性。例如：
  ```java
  public class Person {
      private String name;
      @Override public boolean equals(Object o) { /* 比较name字段 */ }
      // 未覆盖hashCode
  }
  ```
  - 两个 `name` 相同的 `Person` 对象可能因默认 `hashCode` 不同，导致在 `HashSet` 中被视为两个不同对象。

### **如何正确覆盖 `hashCode`？**
#### **1. 遵循通用规范**
- **一致性**：只要对象未被修改，多次调用 `hashCode()` 应返回相同值。
- **对等性**：`equals` 返回 `true` 时，`hashCode` 必须相同。
- **分散性**：`equals` 返回 `false` 时，尽量让 `hashCode` 不同以减少冲突。

#### **2. 实现方法**
- **手动计算**（Java 7 前）：
  ```java
  @Override public int hashCode() {
      int result = 17;
      result = 31 * result + name.hashCode();
      result = 31 * result + age;
      return result;
  }
  ```
  - 选择质数（如31）减少冲突，且JVM可优化为位运算 `(31 * i == (i << 5) - i)`。

- **使用 `Objects.hash()`（Java 7+）**：
  ```java
  @Override public int hashCode() {
      return Objects.hash(name, age); // 自动处理null和组合哈希
  }
  ```

#### **3. 缓存哈希值（可选）**
- 若类不可变且计算成本高，可缓存哈希值：
  ```java
  private int cachedHashCode; // 延迟初始化
  @Override public int hashCode() {
      if (cachedHashCode == 0) {
          cachedHashCode = Objects.hash(name, age);
      }
      return cachedHashCode;
  }
  ```


### **常见错误**
1. **忽略部分关键字段**：
   - 若 `equals` 比较了 `name` 和 `age`，但 `hashCode` 只用了 `name`，会导致逻辑不一致。
2. **可变对象作为哈希键**：
   - 若对象存入 `HashMap` 后修改了参与 `hashCode` 计算的字段，会导致无法检索到该对象（因其哈希桶位置已变）。

### **关键结论**
- **必须同时覆盖 `equals` 和 `hashCode`**：否则破坏哈希集合的正常行为。
- **保持契约一致性**：`equals` 和 `hashCode` 应基于相同的字段集合。
- **优先使用工具方法**：如 `Objects.hash()` 减少手动实现的错误。

通过遵守这些规则，可以确保对象在哈希集合中正确工作，同时维持高性能。

---

## Item 11. Override clone judiciously

《Effective Java 第二版》第11条 **“谨慎覆盖 clone 方法”** 讨论了 `clone()` 方法的复杂性以及如何正确实现它。


**以下是 Deepseek 给出的扩展，学习下：**
### **重要因素及原因**

#### **1. `Cloneable` 接口的缺陷**
- **`Cloneable` 是一个标记接口**（没有方法），但它的存在决定了 `Object.clone()` 的行为：
  - 如果一个类实现了 `Cloneable`，`Object.clone()` 会返回该对象的**逐字段拷贝**。
  - 如果没有实现 `Cloneable`，调用 `clone()` 会抛出 `CloneNotSupportedException`。
- **问题**：这种设计违反了接口的常规用法（接口应定义行为），而是依赖 `Object.clone()` 的 **protected** 方法。

#### **2. `clone()` 方法的契约**
- **返回一个新对象**，与原对象独立（修改新对象不应影响原对象）。
- **通常应满足**：
  - `x.clone() != x`（必须是新对象）。
  - `x.clone().getClass() == x.getClass()`（除非子类允许改变类型）。
  - `x.clone().equals(x)`（通常成立，但非强制要求）。
- **问题**：这些规则并非强制，依赖开发者自觉遵守。

#### **3. 深拷贝 vs. 浅拷贝**
- **默认的 `Object.clone()` 是浅拷贝**：
  - 基本类型字段直接复制。
  - 引用类型字段仅复制引用（新旧对象共享可变引用）。
- **深拷贝需要手动实现**：
  ```java
  @Override public Stack clone() {
      try {
          Stack result = (Stack) super.clone();
          result.elements = elements.clone(); // 对数组等引用类型单独拷贝
          return result;
      } catch (CloneNotSupportedException e) {
          throw new AssertionError(); // 不可能发生（因为实现了 Cloneable）
      }
  }
  ```
- **风险**：如果类包含可变对象（如数组、集合），浅拷贝可能导致数据意外共享。

#### **4. 线程安全问题**
- 如果 `clone()` 方法未正确同步，多线程环境下可能导致对象状态不一致。

#### **5. 更好的替代方案**
- **拷贝构造器（Copy Constructor）**：
  ```java
  public Stack(Stack s) { ... } // 显式控制拷贝逻辑
  ```
- **拷贝工厂（Copy Factory）**：
  ```java
  public static Stack newInstance(Stack s) { ... }
  ```
- **优点**：
  - 更灵活（可以接受不同实现类的对象）。
  - 不依赖 `Cloneable` 的奇怪语义。
  - 避免 `clone()` 的深拷贝陷阱。

### **正确实现 `clone()` 的步骤**
1. **实现 `Cloneable` 接口**（否则 `clone()` 抛出异常）。
2. **重写 `clone()` 为 `public`**（因为 `Object.clone()` 是 `protected`）。
3. **调用 `super.clone()` 获取初始拷贝**。
4. **对可变引用字段单独拷贝**（深拷贝）。
5. **处理异常**（`CloneNotSupportedException` 通常可忽略，因为 `Cloneable` 已实现）。

**示例**：
```java
@Override public Stack clone() {
    try {
        Stack result = (Stack) super.clone();
        result.elements = elements.clone(); // 深拷贝数组
        return result;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError(); // 不会发生
    }
}
```

### **关键结论**
- **尽量避免使用 `clone()`**：优先选择**拷贝构造器**或**拷贝工厂**。
- **如果必须使用 `clone()`**：
  - 确保实现 `Cloneable`。
  - 正确处理深拷贝。
  - 文档化 `clone()` 的行为（是否线程安全？是否深拷贝？）。
- **`clone()` 的适用场景**：
  - 主要用于**数组拷贝**（`array.clone()` 是最高效的方式）。
  - 某些框架（如 `ObjectOutputStream`）依赖 `Cloneable`。

### **替代方案推荐**
| **方式**       | **优点**                          | **缺点**                     |
|----------------|----------------------------------|-----------------------------|
| `clone()`      | 某些情况下性能较好（如数组）       | 语义复杂，容易出错            |
| 拷贝构造器      | 更灵活，代码清晰                 | 需要手动实现                 |
| 静态工厂方法    | 可控制返回类型（如不可变对象）     | 需要额外方法                 |

**最终建议**：除非有特殊需求（如兼容旧代码），否则优先使用 **拷贝构造器** 或 **工厂方法**。

---

## Item 74. Implement Serializable judiciously

《Effective Java 第二版》第74条的主题是 **“谨慎实现 Serializable 接口”**，作者 Joshua Bloch 强调了序列化的复杂性及其潜在风险。



**以下是 Deepseek 给出的扩展，学习下：**
### **重要因素及原因**

1. **降低灵活性和维护成本**

   > it decreases the flexibility to change a class's implementation once it has been released.

   - 一旦类实现 `Serializable`，其序列化形式（二进制格式）将成为永久的一部分。未来修改类结构（如删除字段、改变继承关系）时，必须兼容旧版本的序列化数据，否则可能导致反序列化失败。
   - 如果早期未设计好序列化形式，后续可能需要通过 `readObject`/`writeObject` 等方法“修补”，增加代码复杂度。

2. **安全风险**

   > It increases the likelihood of bugs and security holes. There is no explicit constructor associated with deserilization.

   - 反序列化是通过构造对象实现的，绕过常规构造器逻辑，可能被恶意利用（如注入非法数据或触发隐藏代码）。
   - 反序列化攻击的典型案例：通过精心构造的字节流执行非预期操作（如 `HashMap` 的反序列化可能引发哈希碰撞攻击）。

3. **性能开销**

   - 序列化/反序列化需要额外的 CPU 和内存资源，尤其是对象图较深时。
   - 序列化会生成大量临时对象，可能增加 GC 压力。

4. **版本兼容性问题**

   - 每个序列化类都有一个唯一标识符 `serialVersionUID`。若未显式声明，JVM 会根据类结构自动生成，一旦类细节（如方法签名）变化，`UID` 可能改变，导致旧数据无法反序列化。
   - 显式声明 `serialVersionUID` 可强制版本控制，但需自行保证兼容性。

5. **测试负担增加**

   > It  increases the testing burden associated with releasing a new version of a class.

   - 需要额外测试序列化/反序列化的正确性，尤其是跨版本兼容性测试。

6. **继承问题**
   - 父类若实现 `Serializable`，子类会自动序列化，可能导致子类字段意外暴露或反序列化时父类构造逻辑被绕过。
   - 若父类未实现 `Serializable`，子类需手动处理父类状态的序列化（如通过 `readObject`/`writeObject`）。

7. **线程安全隐患**
   - 反序列化后的对象可能需要额外的同步措施，尤其是包含非线程安全字段时。


### **替代方案建议**
- **优先考虑不可变对象**：不可变对象天然更适合序列化，减少状态不一致风险。
- **使用自定义序列化格式**（如 JSON、Protobuf）：更灵活、可读且解耦代码。
- **显式控制序列化过程**：通过 `transient` 忽略敏感字段，或重写 `readObject`/`writeObject` 验证数据合法性。

### **关键结论**
- **不要轻易实现 `Serializable`**：除非确定该类需要跨网络传输或持久化存储，且已评估长期维护成本。
- **设计时明确序列化策略**：包括版本控制、字段过滤和安全措施。

通过理解这些因素，可以避免序列化带来的隐藏陷阱，确保代码的健壮性和安全性。