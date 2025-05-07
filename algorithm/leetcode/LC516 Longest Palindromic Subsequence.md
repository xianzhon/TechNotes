
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[516. 最长回文子序列 - 力扣（LeetCode）](https://leetcode.cn/problems/longest-palindromic-subsequence/)

Given a string `s`, find *the longest palindromic **subsequence**'s length in* `s`.

A **subsequence** is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.

 

**Example 1:**

```
Input: s = "bbbab"
Output: 4
Explanation: One possible longest palindromic subsequence is "bbbb".
```

**Example 2:**

```
Input: s = "cbbd"
Output: 2
Explanation: One possible longest palindromic subsequence is "bb".
```

 

**Constraints:**

- `1 <= s.length <= 1000`
- `s` consists only of lowercase English letters.



## 2. 最佳思路

```
- 转换成求s 与 s' 的最长公共子序列，用 DP 来做
- dp[i][j] 表示 s[:i] 与 s'[:j]的最长公共子序列的长度
- 递推公式
	dp[i][j] = dp[i - 1][j - 1] + 1          if s[i] == s'[j]
			 = max(dp[i][j-1], dp[i-1][j])   else  [因为不相等，所以删除一个字符再去比较]
- init/base: 
	dp = new int[n+1][m+1]  # 字符串有空串，所以要加+1去包含空串
	dp[0][j] = 0, dp[i][0] = 0
- answer: dp[n][m]
```




## 3. 最佳代码


### 3.1 解法2 - LCS - 不容易出错的写法

```java
class Solution {
    public int longestPalindromeSubseq(String s) { // time: O(n^2), space: O(n^2)
        int n = s.length();
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == s.charAt(n - j)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j-1], dp[i-1][j]);
                }
            }
        }
        return dp[n][n];
    }
}
/*
bbbab
subsequence: char not required to be continuous.
max length: dp? two string's common subsequence.

time: O(n^2), space: O(n^2)

dp[i][j]: s[:i] with t[:j]'s longest common subsequence
    dp[i][j] = max{dp[i-1][j], dp[i][j-1]}  if s[i] != t[j]
        dp[i-1][j-1] + 1                      else
base: dp[0][j] = 0, dp[i][0] = 0
answer: dp[n][n]
*/
```

空间可以优化到 O(n)，例如使用滚动数组。

### 3.2 解法3 - top-down DFS + memo

DP's another implementation version:
```java
class Solution {
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] memo = new int[n][n];
        return lps(s, 0, n - 1, memo);
    }

    private int lps(String s, int i, int j, int[][] memo) {
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }

        if (s.charAt(i) == s.charAt(j)) {
            memo[i][j] = lps(s, i + 1, j - 1, memo) + 2;
        } else {
            memo[i][j] = Math.max(lps(s, i + 1, j, memo), lps(s, i, j - 1, memo));
        }
        return memo[i][j];
    }
}
```

- time: O(n^2)
- space: O(n^2)


### 空间优化的 DP

```java
class Solution {
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[] dp = new int[n];
        int[] dpPrev = new int[n];

        for (int i = n - 1; i >= 0; --i) {
            dp[i] = 1;
            for (int j = i + 1; j < n; ++j) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = dpPrev[j - 1] + 2;
                } else {
                    dp[j] = Math.max(dpPrev[j], dp[j - 1]);
                }
            }
            dpPrev = dp.clone();
        }

        return dp[n - 1];
    }
}
```
- time: O(n^2)
- space: O(n)



## DP - 使用滚动数组优化空间 

```java
class Solution {
    public int longestPalindromeSubseq(String s) { // time: O(n^2), space: O(n)
        int n = s.length();
        int[][] dp = new int[2][n + 1];
        int idx = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == s.charAt(n - j)) {
                    dp[idx][j] = dp[1 - idx][j - 1] + 1;
                } else {
                    dp[idx][j] = Math.max(dp[idx][j-1], dp[1 - idx][j]);
                }
            }
            idx = 1 - idx;
        }
        return dp[1 - idx][n];
    }
}
```

- 技巧：开辟两行的数组，用 idx 来回滚动。初始要更新的 idx = 1，利用 0行。之后每次切换时，使用 `idx = 1 - idx`