
该题掌握程度：
- #熟练✓

Tag: #dp

## 1. 题目
题目链接： https://leetcode.cn/problems/longest-line-of-consecutive-one-in-matrix/

Given an `m x n` binary matrix `mat`, return *the length of the longest line of consecutive one in the matrix*.

The line could be horizontal, vertical, diagonal, or anti-diagonal.



**Example 1:**

![img](https://i.hish.top:8/2025/04/22/155112.jpg)

```
Input: mat = [[0,1,1,0],[0,1,1,0],[0,0,0,1]]
Output: 3
```

**Example 2:**

![img](https://i.hish.top:8/2025/04/22/155112.jpg)

```
Input: mat = [[1,1,1,1],[0,1,1,0],[0,0,0,1]]
Output: 4
```



**Constraints:**

- `m == mat.length`
- `n == mat[i].length`
- `1 <= m, n <= 104`
- `1 <= m * n <= 104`
- `mat[i][j]` is either `0` or `1`.



## 2. 最佳思路

- 简单的二维 DP 问题，比较直接，记录 4 个方向（左上、上、右上、左）上连续 1 的个数。


## 3. 最佳代码

```java
class Solution {
    public int longestLine(int[][] mat) {
        int n = mat.length, m = mat[0].length;
        int[][][] dp = new int[n][m][4];

        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 1) {
                    // 0: top, 1: left-top, 2: right-top, 3: left
                    dp[i][j][0] = (i > 0 ? dp[i - 1][j][0] : 0) + 1;
                    dp[i][j][1] = (i > 0 && j > 0 ? dp[i - 1][j - 1][1] : 0) + 1;
                    dp[i][j][2] = (i > 0 && j < m - 1 ? dp[i-1][j+1][2] : 0) + 1;
                    dp[i][j][3] = (j > 0 ? dp[i][j - 1][3] : 0) + 1;

                    for (int k = 0; k < 4; k++) {
                        ans = Math.max(ans, dp[i][j][k]);
                    }
                }
            }
        }
        return ans;
    }
}
/*
grid dp:
dp[i][j][4] to store the longest consecutive one in each dir.
*/
```

使用条件运算符来处理边界的情况，代码量会少一点。



### 3.1 复杂度分析

- **时间复杂度**：O(nm)

- **空间复杂度**：O(nm)

