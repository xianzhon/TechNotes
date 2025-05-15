
# Java 位运算进阶技巧

## 1. 位操作高级技巧

### 1.1 快速乘除
- **乘以2**：`x << 1`
- **除以2**：`x >> 1` (算术右移，保留符号位)
- **乘以2^n**：`x << n`
- **除以2^n**：`x >> n`
- **取模2^n**：`x & ((1 << n) - 1)`

### 1.2 位计数技巧
- **统计1的个数（Brian Kernighan算法）**：
  ```java
  int count = 0;
  while (x != 0) {
      x &= (x - 1);  // 清除最低位的1
      count++;
  }
  ```

### 1.3 位反转
- **反转所有位**：`~x`
- **反转最低n位**：`x ^ ((1 << n) - 1)`

## 2. 掩码与位集合操作

### 2.1 位集合表示
- **设置多个位**：`x |= mask`
- **清除多个位**：`x &= ~mask`
- **切换多个位**：`x ^= mask`
- **检查所有位是否设置**：`(x & mask) == mask`
- **检查任一位置位**：`(x & mask) != 0`

### 2.2 位集合运算
- **并集**：`a | b`
- **交集**：`a & b`
- **差集**：`a & ~b`
- **对称差集（异或）**：`a ^ b`

## 3. 特殊数值检测

### 3.1 检测2的幂
```java
boolean isPowerOfTwo = (x != 0) && ((x & (x - 1)) == 0);
```

### 3.2 检测4的幂
```java
boolean isPowerOfFour = (x != 0) && ((x & (x - 1)) == 0) && ((x & 0xAAAAAAAA) == 0);
```

### 3.3 检测是否有相邻的1
```java
boolean hasAdjacentOnes = (x & (x >> 1)) != 0;
```

## 4. 位操作实用函数

### 4.1 获取最高有效位
```java
int highestOneBit(int x) {
    x |= (x >> 1);
    x |= (x >> 2);
    x |= (x >> 4);
    x |= (x >> 8);
    x |= (x >> 16);
    return x - (x >>> 1);
}
```

### 4.2 循环移位
```java
// 循环左移
int rotateLeft(int x, int n) {
    return (x << n) | (x >>> (32 - n));
}

// 循环右移
int rotateRight(int x, int n) {
    return (x >>> n) | (x << (32 - n));
}
```

### 4.3 交换奇偶位
```java
int swapOddEvenBits(int x) {
    return ((x & 0xAAAAAAAA) >>> 1) | ((x & 0x55555555) << 1);
}
```

## 5. 位操作在算法中的应用

### 5.1 寻找唯一出现一次的数字
```java
// 数组中所有数字都出现两次，只有一个出现一次
int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
        result ^= num;
    }
    return result;
}
```

### 5.2 寻找两个唯一出现一次的数字
```java
int[] singleNumberTwo(int[] nums) {
    int diff = 0;
    for (int num : nums) {
        diff ^= num;
    }
    diff &= -diff;  // 获取最低位的1
    
    int[] result = new int[2];
    for (int num : nums) {
        if ((num & diff) == 0) {
            result[0] ^= num;
        } else {
            result[1] ^= num;
        }
    }
    return result;
}
```

### 5.3 格雷码生成
```java
int binaryToGray(int num) {
    return num ^ (num >> 1);
}

int grayToBinary(int gray) {
    int binary = gray;
    while ((gray >>= 1) != 0) {
        binary ^= gray;
    }
    return binary;
}
```

这些高级位操作技巧在算法优化、密码学、图形学等领域有广泛应用，合理使用可以显著提高程序性能。