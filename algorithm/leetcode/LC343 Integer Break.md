
该题掌握程度：
- #很差❌

## 1. 题目
题目链接：[343. 整数拆分 - 力扣（LeetCode）](https://leetcode.cn/problems/integer-break/)

Given an integer `n`, break it into the sum of `k` **positive integers**, where `k >= 2`, and maximize the product of those integers.

Return *the maximum product you can get*.

**Example 1:**

```
Input: n = 2
Output: 1
Explanation: 2 = 1 + 1, 1 × 1 = 1.
```

**Example 2:**

```
Input: n = 10
Output: 36
Explanation: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36.
```

**Constraints:**

- `2 <= n <= 58`

## 2. 最佳思路

结论：拆分的结果，尽可能是 2 和 3,且 2 的个数不超过 2 个。

n = a1 + a2 + ... + an

分析整数拆分的构成：
1. 当 n > 5时，拆分的结果不可能包含 5，反证法：如果包含 5，可以拆成 2 + 3. 结果乘积是更大的
2. n 只能去 1-4，也不可能是 1，因为拆开后对乘积没贡献，所以只能是 2,3,4. 对于 4 来说，可以拆成 2+2，乘积不变。
3. 为何最多只能有 2 个2，假设有多余 2 个 2，任取 3 个 2,   prod(2+2+2)  < prod(3 + 3)，所以2 的个数不超过 2 个

实际上就是分析，当 n >= 5的时候，一定要 split，因为 split 后他们的乘积是大于不 split 的。
## 3. 最佳代码

### Code 1 - O(n) 利用了数学知识
```java
class Solution {
    public int integerBreak(int n) {
        if (n <= 3) {
            return n - 1;
        }
        int ans = 1;
        while (n >= 5) {
            ans *= 3;
            n -= 3;
        }
        // n: 2 3 4
        ans *= n;
        return ans;
    }
}
```

- time: O(n). The while loop will execute: O(n/3) times.
- space: O(1)


### code 2 - O(n) - DP
```java
class Solution {
    public int integerBreak(int n) {
        if (n <= 3) {
            return n - 1;
        }
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            dp[i] = Math.max(Math.max(2 * (i - 2), 2 * dp[i - 2]), Math.max(3 * (i - 3), 3 * dp[i - 3]));
        }
        return dp[n];
    }
}
// From：https://leetcode.cn/problems/integer-break/solutions/352875/zheng-shu-chai-fen-by-leetcode-solution/
```
### code 1 - O(n^2) 不是最佳
```java
class Solution {
    public int integerBreak(int n) {
        int[] dp = new int[n+1];
        dp[1] = 0;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                int curMax = Math.max(j * (i - j), j * dp[i-j]);
                dp[i] = Math.max(dp[i], curMax);
            }
        }
        return dp[n];
    }
}
/*

f(n) = f(n-k) * k {k=1...n-1}
f(1) = 1
f(2) = 1
f(3) = f(1)*2 = 2
        f(2)*1 = 1
f(4)=f(1)*3 f(2)*2  f(3)*1 = 4


f(6)=3*3 = 9
f(9) = f(6)*3 = 27
f(10) = f(6)*4 = 36
dp[i] = max(j * (i-j), j * dp[i-j]) j=1..i-1
dp[0] = dp[1] = 0
dp[2] = 1*1, 1*dp[1] = 1
*/
```
复杂度分析
- **时间复杂度**：O(n^2)
- **空间复杂度**：O(n)
  .
