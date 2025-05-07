# 动态规划算法总结

Leetcode 练习题单： https://leetcode.cn/problem-list/dynamic-programming/  （截止 2025.5.7 有659 题），按出现频率逆序做。

- 找递推公式的技巧：思考最后一步有几种做法，或者考虑`dp[i]` 时，考虑第 i 步怎么做。
- DP 问题的时间复杂度：状态的个数 x  每个状态所需要的计算

## 基础知识 - 递归三要素

### 1. **重叠子问题（Overlapping Subproblems）**

- 问题可以分解为若干子问题，且这些子问题会被**重复计算多次**。
- 动态规划通过**记忆化存储（Memoization）**或**填表（Tabulation）**避免重复计算，提高效率。
- **示例**：斐波那契数列 `F(n) = F(n-1) + F(n-2)`，其中 `F(n-1)` 和 `F(n-2)` 会被多次计算。

### 2. **最优子结构（Optimal Substructure）**

- 问题的最优解可以通过其子问题的最优解**组合**得到。
- **示例**：最短路径问题中，`A→B→C` 的最短路径 = `A→B` 的最短路径 + `B→C` 的最短路径。

### 3. **状态转移方程（State Transition Equation）**

- 定义如何从子问题的解推导出当前问题的解，通常是一个递推公式。
- **示例**：
	- 斐波那契数列：`dp[i] = dp[i-1] + dp[i-2]`
	- 最长回文子串：`dp[i][j] = (s[i] == s[j]) && dp[i+1][j-1]`



## 笔记 [LC05. Longest Palindromic Substring](leetcode/LC05.%20Longest%20Palindromic%20Substring.md) - Mid

```java
	- (boolean) dp[i][j] means substring s[i,j] is panlindrome or not.
	- formula: dp[i][j] = dp[i+1][j-1] if s[i] == s[j], else: false
	- base: dp[i][i] = true. single char is palindrome
	- answer: max(j - i + 1) if dp[i][j] = true
	- time: O(n^2), space: O(n^2)
```


## [70. 爬楼梯 - 力扣（LeetCode）](https://leetcode.cn/problems/climbing-stairs/description/?envType=problem-list-v2&envId=dynamic-programming) - Easy

You are climbing a staircase. It takes `n` steps to reach the top.
Each time you can either climb `1` or `2` steps. In how many distinct ways can you climb to the top?

思考最后一步：可以从n - 1 走一步，或者 n - 2走两步到达 n，从而写出递推公式。

## [LC72. Edit Distance](leetcode/LC72.%20Edit%20Distance.md) - Easy
- 经典的动态规划问题，三种操作。

```java
- dp[i][j]: means how many operations required to convert word1[:i] to word2[:j]
- formula
	dp[i][j] = dp[i-1][j-1]                                          if word1[i] == word2[j]
	         = min (dp[i-1][j-1], dp[i][j-1], dp[i-1][j]) + 1        otherwise (three operations)
- base/initial
	var dp = new int[n + 1][m + 1]
	dp[0][j] = j, dp[i][0] = i
- answer: dp[n][m]
```



## [LC53. Maximum Subarray](leetcode/LC53.%20Maximum%20Subarray.md) - Easy
- 经典的动态规划问题
- 相关联的题目，最大子序列的和，最长递增子序列，最长公共子序列，最长公共子数组，最长回文子序列。
```java
- dp[i][j]: means how many operations required to convert word1[:i] to word2[:j]
- formula
	dp[i][j] = dp[i-1][j-1]                                          if word1[i] == word2[j]
	         = min (dp[i-1][j-1], dp[i][j-1], dp[i-1][j]) + 1        otherwise (three operations)
- base/initial
	var dp = new int[n + 1][m + 1]
	dp[0][j] = j, dp[i][0] = i
- answer: dp[n][m]
```
- 最长回文子序列，笔记：[LC516 Longest Palindromic Subsequence](leetcode/LC516%20Longest%20Palindromic%20Subsequence.md)
- 笔记：[LC22. Generate Parentheses](leetcode/LC22.%20Generate%20Parentheses.md) 不太好想，而且 DP 解法空间太大，不是最优解法
- 笔记：[LC32 Long Bracket](leetcode/LC32%20Long%20Bracket.md)


