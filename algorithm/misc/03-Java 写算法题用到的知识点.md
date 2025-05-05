#  Java 写算法题可以利用的语言特性和类库

## **1. 基本数据结构**
### **(1) 线性表**
- **数组**  
  - `int[] arr = new int[n];`  
  - `Arrays` 工具类（`sort`, `binarySearch`, `copyOf`, `fill` 等）  
- **动态数组**  
  - `ArrayList<E>`（`add`, `remove`, `get`, `set`, `subList`）  
- **链表**  
  - `LinkedList<E>`（`addFirst`, `addLast`, `pollFirst`, `pollLast`）  

### **(2) 栈（Stack）**
- `Stack<E>`（遗留类，不推荐）  
- **推荐使用 `Deque` 实现栈**：  
  ```java
  Deque<Integer> stack = new ArrayDeque<>();
  stack.push(1);  // 入栈
  stack.pop();    // 出栈
  stack.peek();   // 查看栈顶
  ```

### **(3) 队列（Queue）**
- **普通队列**  
  ```java
  Queue<Integer> queue = new LinkedList<>();
  queue.offer(1);  // 入队
  queue.poll();    // 出队
  queue.peek();    // 查看队首
  ```
- **双端队列（Deque）**  
  ```java
  Deque<Integer> deque = new ArrayDeque<>();
  deque.offerFirst(1);  // 队首入队
  deque.offerLast(2);   // 队尾入队
  deque.pollFirst();    // 队首出队
  deque.pollLast();     // 队尾出队
  ```
- **优先队列（堆）**  
  ```java
  // 默认最小堆
  PriorityQueue<Integer> minHeap = new PriorityQueue<>();
  // 最大堆
  PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
  minHeap.offer(3);  // 插入
  minHeap.poll();    // 弹出最小值
  minHeap.peek();   // 查看最小值
  ```


## **2. 集合类（Set / Map）**
### **(1) 集合（Set）**
- **哈希集合（无序）**  
  ```java
  Set<Integer> set = new HashSet<>();
  set.add(1);      // 添加
  set.remove(1);   // 删除
  set.contains(1); // 查询
  ```
- **有序集合（基于红黑树）**  
  ```java
  Set<Integer> sortedSet = new TreeSet<>();
  sortedSet.add(3);
  sortedSet.first();  // 最小元素
  sortedSet.last();   // 最大元素
  ```

### **(2) 映射（Map）**
- **哈希表（无序）**  
  ```java
  Map<String, Integer> map = new HashMap<>();
  map.put("a", 1);      // 插入
  map.get("a");         // 查询
  map.containsKey("a"); // 检查键
  map.remove("a");      // 删除
  ```
- **有序映射（基于红黑树）**  
  ```java
  Map<String, Integer> sortedMap = new TreeMap<>();
  sortedMap.put("b", 2);
  sortedMap.firstKey();  // 最小键
  sortedMap.lastKey();   // 最大键
  ```


## **3. 字符串处理**
- **`String` 类**  
  ```java
  String s = "abc";
  s.charAt(0);      // 'a'
  s.substring(1);   // "bc"
  s.indexOf("b");   // 1
  s.split(",");     // 分割字符串
  ```
- **`StringBuilder`（高效字符串拼接）**  
  ```java
  StringBuilder sb = new StringBuilder();
  sb.append("a");
  sb.append("b");
  String result = sb.toString();  // "ab"
  sb.reverse();  // 反转字符串
  ```

## **4. 数学计算**
- **`Math` 工具类**  
  ```java
  Math.max(a, b);
  Math.min(a, b);
  Math.abs(x);
  Math.pow(a, b);  // a^b
  Math.sqrt(x);    // 平方根
  Math.log(x);     // 自然对数
  ```
- **大整数计算（`BigInteger`）**  
  ```java
  BigInteger a = new BigInteger("123456789");
  BigInteger b = new BigInteger("987654321");
  BigInteger sum = a.add(b);  // 加法
  BigInteger product = a.multiply(b);  // 乘法
  ```
- **大浮点数计算（`BigDecimal`）**  
  ```java
  BigDecimal a = new BigDecimal("123.456");
  BigDecimal b = new BigDecimal("789.123");
  BigDecimal sum = a.add(b);
  ```


## **5. 位运算**
- **基本位运算**  
  ```java
  int a = 5;  // 0101
  int b = 3;  // 0011
  a & b;  // 按位与 (1)
  a | b;  // 按位或 (7)
  a ^ b;  // 异或 (6)
  ~a;     // 取反
  a << 1; // 左移 (10)
  a >> 1; // 右移 (2)
  ```
- **`Integer` 位运算工具**  
  ```java
  Integer.bitCount(n);  // 统计二进制1的个数
  Integer.highestOneBit(n);  // 最高位1的值
  Integer.lowestOneBit(n);   // 最低位1的值
  Integer.numberOfLeadingZeros(n);  // 前导0的个数
  ```


## **6. 排序与查找**
- **`Arrays.sort()`**  
  ```java
  int[] arr = {3, 1, 2};
  Arrays.sort(arr);  // 升序排序
  Arrays.sort(arr, (a, b) -> b - a);  // 降序排序
  ```
- **`Collections.sort()`**  
  
  ```java
  List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 2));
  Collections.sort(list);  // 升序
  Collections.sort(list, (a, b) -> b - a);  // 降序
  ```
- **二分查找**  
  
  ```java
  int[] arr = {1, 2, 3, 4, 5};
  int idx = Arrays.binarySearch(arr, 3);  // 返回索引
  ```


## **7. 其他实用工具**

1. **位运算工具类**：
	- `Integer.bitCount(n)` - 计算整数的二进制表示中1的个数
	- `Integer.highestOneBit(n)` - 获取最高位的1
	- `Integer.lowestOneBit(n)` - 获取最低位的1
	- `Integer.numberOfLeadingZeros(n)` - 前导零的数目
	- `Integer.numberOfTrailingZeros(n)` - 后缀零的数目
2. **数学工具**：
	- `Math` 类中的各种方法（如 `max`, `min`, `abs`, `pow`, `sqrt` 等）
	- `BigDecimal` - 高精度浮点数运算
3. **数组工具**：
	- `Arrays.copyOf()` 和 `Arrays.copyOfRange()` - 数组拷贝
	- `Arrays.fill()` - 填充数组
	- `Arrays.sort()` - 排序（可以自定义比较器）
	- `Arrays.binarySearch()` - 二分查找
4. **字符串处理**：
	- `String.substring()`
	- `String.indexOf()`
	- `String.charAt()`
	- `String.toCharArray()`
	- `String.split()`
	- `String.format()`
5. **集合工具类**：
	- `Collections.sort()` - 排序集合
	- `Collections.reverse()` - 反转集合
	- `Collections.binarySearch()` - 二分查找
	- `Collections.swap()` - 交换元素
	- `Collections.fill()` - 填充集合
6. **比较器和lambda表达式**：
	- 使用 `Comparator` 和 lambda 表达式简化比较逻辑
	- 例如：`PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);`
7. **随机数生成**：
	- `Random` 类
	- `ThreadLocalRandom` 类（多线程环境下更高效）
8. **正则表达式**：
	- `Pattern` 和 `Matcher` 类处理复杂字符串匹配

9. **位集合**：
	- `BitSet` - 处理位操作的类
10. **Java 8+ 特性**：
	- `Optional` - 避免空指针异常
	- 函数式接口和 Stream API 的更多高级用法
	- `var` 关键字（Java 10+，可以简化局部变量声明）
11. **输入输出优化**：
	- `BufferedReader` 和 `BufferedWriter` 处理大量输入输出时更高效
12. **并发工具**（少数并发题目可能用到）：
	- `AtomicInteger`, `AtomicLong` 等原子类
	- `CountDownLatch`, `Semaphore` 等同步工具
13. **枚举**：
	- 有时可以用枚举简化状态表示



## **总结**
| 类别        | 主要类/方法                            | 用途         |
| --------- | --------------------------------- | ---------- |
| **数组**    | `int[]`, `Arrays`                 | 存储和操作数组    |
| **动态数组**  | `ArrayList`                       | 可变长数组      |
| **栈**     | `Deque`（`ArrayDeque`）             | 后进先出（LIFO） |
| **队列**    | `Queue`（`LinkedList`）             | 先进先出（FIFO） |
| **优先队列**  | `PriorityQueue`                   | 堆结构        |
| **集合**    | `HashSet`, `TreeSet`              | 去重、快速查找    |
| **映射**    | `HashMap`, `TreeMap`              | 键值对存储      |
| **字符串**   | `String`, `StringBuilder`         | 字符串操作      |
| **数学**    | `Math`, `BigInteger`              | 数学计算       |
| **位运算**   | `Integer.bitCount`                | 位操作        |
| **排序/查找** | `Arrays.sort`, `Collections.sort` | 排序和二分查找    |
| **工具类**   | `Collections`, `Stream`           | 集合操作       |

这些特性和类库覆盖了 LeetCode 算法题的绝大部分需求，合理使用它们可以大幅提升解题效率！ 🚀