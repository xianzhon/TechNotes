# 动态规划算法总结

Leetcode 练习题单： https://leetcode.cn/problem-list/dynamic-programming/  （截止 2025.5.7 有659 题），按出现频率逆序做。

- **找递推公式的技巧**：思考最后一步有几种做法（例如，计算`dp[i]` 时，最后一步有几种选择）
- **DP 问题的时间复杂度**：状态的个数 X  每个状态所需要的计算

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

```java
- LC516 Longest Palindromic Subsequence
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

## 背包问题相关
### 笔记：[LC416 Partition Equal Subset Sum](leetcode/LC416%20Partition%20Equal%20Subset%20Sum.md)
- 经典的0-1背包问题的简化版本，问恰好装满背包
```java
- 状态表示，dp[i][j]: 表示前 i 件物品，是否能恰好装满容量为 j 的背包
- 递推公式：考虑最后一步，装 i 或不装 i。只要其中一个是 true，dp[i][j] 就是 true
	dp[i][j] = dp[i-1][j]             不装i
			 = dp[i-1][j - nums[i]]   装 i
- base: dp[i][0] = true i >= 0 恰好能装满容量是 0 的背包
- answer: dp[n][sum/2]
```

### 笔记：[LC494. Target Sum](leetcode/LC494.%20Target%20Sum.md)
- 很巧妙，需要做一个变换，才能变成背包问题

```java
1. 状态 dp[i][j] 表示考虑前 i 个物品，组成和为 j 的方法数。
2. 递推公式：
	dp[i][j] = dp[i - 1][j]       +    dp[i-1][j-nums[i]]
		        not use nums[i],    use nums[i]
3. inital: dp[0][0] = 1.   dp = new int[n+1][Capacity + 1];
4. answer: dp[n][Capacity]
```

### 笔记：[LC518. Coin Change II](leetcode/LC518.%20Coin%20Change%20II.md)
- 完全背包问题，恰好装满背包。两层循环，外层枚举 coin，内层枚举amount - 从小到大。

### 笔记：[LC3183. The Number of Ways to Make the Sum](leetcode-vip/LC3183.%20The%20Number%20of%20Ways%20to%20Make%20the%20Sum-vip.md) - 巧妙
- 完全背包问题 + 分组背包问题。