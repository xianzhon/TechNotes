
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[400. 第 N 位数字 - 力扣（LeetCode）](https://leetcode.cn/problems/nth-digit/)

Given an integer `n`, return the `nth` digit of the infinite integer sequence `[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...]`.

**Example 1:**

```
Input: n = 3
Output: 3
```

**Example 2:**

```
Input: n = 11
Output: 0
Explanation: The 11th digit of the sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... is a 0, which is part of the number 10.
```

**Constraints:**

- `1 <= n <= 2^31 - 1`



























## 2. 最佳思路

- 分类：1~9为一类，10~99为一类，依此类推。我们要找到目标digit所属的数字在哪一类，同类里的数digitCnt一样（这样方便下一步找数）
- 确定哪个自然数：n所对应的数是这类里第几个？每个数有digitCnt位数，起始数字是该类的最小数字（1，10，100，...），结果不言而喻了吧
- 找到这个数字的第k位是什么digit

## 3. 最佳代码

```java
public class Solution {
    public int findNthDigit(int n) {
        int len = 1;
        long count = 9; // note: 这里要用 long，否则 len * count 会溢出
        int start = 1;

        while (n > len * count) {
            n -= len * count;
            len += 1;
            count *= 10;
            start *= 10;
        }

        n--; // change n to 0-based index
        return findNthDigit(start + n / len, n % len);
    }

    int findNthDigit(int num, int k) {
        return String.valueOf(num).charAt(k) - '0';
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(log n)
- **空间复杂度**：O(1)

### 3.2 特别注意

- 算法思路：easy
- 实现细节：hard

