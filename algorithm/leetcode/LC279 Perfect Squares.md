
该题掌握程度：
- #一般⭕️

Tag: #dp 

## 1. 题目
题目链接：[279. 完全平方数 - 力扣（LeetCode）](https://leetcode.cn/problems/perfect-squares/)

Given an integer `n`, return *the least number of perfect square numbers that sum to* `n`.

A **perfect square** is an integer that is the square of an integer; in other words, it is the product of some integer with itself. For example, `1`, `4`, `9`, and `16` are perfect squares while `3` and `11` are not.

 

**Example 1:**

```
Input: n = 12
Output: 3
Explanation: 12 = 4 + 4 + 4.
```

**Example 2:**

```
Input: n = 13
Output: 2
Explanation: 13 = 4 + 9.
```


**Constraints:**

- `1 <= n <= 10^4`

## 2. 最佳思路

Since the problem asks for min number, we can consider to use DP.
```
- dp[i] means the min number of square sum for integer i.
- formular: dp[i] = min (dp[i - k] + 1), where k is the square numbers less than or equal to i
- initial: dp[0] = 0
- answer: dp[n]
```

## 3. 最佳代码

### DP from the answer

```java
class Solution {

  public int numSquares(int n) {
    int dp[] = new int[n + 1];    
    dp[0] = 0; // bottom case

    // pre-calculate the square numbers.
    int max_square_index = (int) Math.sqrt(n) + 1;
    int[] square_nums = new int[max_square_index];
    for (int i = 1; i < max_square_index; ++i) {
      square_nums[i] = i * i;
    }

    for (int i = 1; i <= n; ++i) {
      dp[i] = i;
      for (int s = 1; s < max_square_index; ++s) {
        if (i < square_nums[s])
          break;
        dp[i] = Math.min(dp[i], dp[i - square_nums[s]] + 1);
      }
    }
    return dp[n];
  }
}
```


### DP - without optimism 

```java
class Solution {
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = n; // n = 1 + 1 + ... + 1
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：`O(n * sqrt(n))`
  For each number, we check each possible square under it. There are about `sqrt(n)` times.

- **空间复杂度**：O(n)

## 4. 其他解法

英文答案上给了很多其他解法。

### 4.1 解法2 - 利用数学知识

```java
class Solution {

  protected boolean isSquare(int n) {
    int sq = (int) Math.sqrt(n);
    return n == sq * sq;
  }

  public int numSquares(int n) { // time: O(sqrt(n)), space: O(1)
    // four-square and three-square theorems.
    while (n % 4 == 0)
      n /= 4;
    if (n % 8 == 7)
      return 4;

    if (this.isSquare(n))
      return 1;
    // enumeration to check if the number can be decomposed into sum of two squares.
    for (int i = 1; i * i <= n; ++i) {
      if (this.isSquare(n - i * i))
        return 2;
    }
    // bottom case of three-square theorem.
    return 3;
  }
}
```


