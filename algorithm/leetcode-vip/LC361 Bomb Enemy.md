
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接： https://leetcode.cn/problems/bomb-enemy/

Given an `m x n` matrix `grid` where each cell is either a wall `'W'`, an enemy `'E'` or empty `'0'`, return *the maximum enemies you can kill using one bomb*. You can only place the bomb in an empty cell.

The bomb kills all the enemies in the same row and column from the planted point until it hits the wall since it is too strong to be destroyed.

 

**Example 1:**

![img](https://i.hish.top:8/2025/05/08/113551.jpg)

```
Input: grid = [["0","E","0","0"],["E","0","W","E"],["0","E","0","0"]]
Output: 3
```

**Example 2:**

![img](https://i.hish.top:8/2025/05/08/113551.jpg)

```
Input: grid = [["W","W","W"],["0","0","0"],["E","E","E"]]
Output: 1
```

 

**Constraints:**

- `m == grid.length`
- `n == grid[i].length`
- `1 <= m, n <= 500`
- `grid[i][j]` is either `'W'`, `'E'`, or `'0'`.

## 2. 最佳思路

- 预处理，记录每个单元格，在 4 个方向上能 kill 的最大的敌人数量

## 3. 最佳代码

```java
class Solution {
    public int maxKilledEnemies(char[][] grid) { // time: O(nm), space: O(nm)
        int n = grid.length, m = grid[0].length;
        int[][][] enemyInDir = countEnemyInAllDir(grid);
        int ans = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '0') {
                    // try to put a bomb here
                    int killCount = 0;
                    for (int k = 0; k < 4; k++) {
                        killCount += enemyInDir[i][j][k];
                    }
                    ans = Math.max(ans, killCount);
                }
            }
        }
        return ans;
    }

    private int[][][] countEnemyInAllDir(char[][] grid) {
        int n = grid.length, m = grid[0].length;
        int ans = 0;
        int[][][] enemyInDir = new int[n][m][4]; // 0: up, 1, down, 2: left, 3: right
        for (int i = 0; i < n; i++) {// calculate from top to down, left to right                    
            // left to right
            int cur = 0;
            for (int j = 0; j < m; j++) {
                enemyInDir[i][j][2] = cur;
                cur = accumulateEnemy(grid[i][j], cur);
            }
            // right to left
            cur = 0;
            for (int j = m - 1; j >= 0; j--) {
                enemyInDir[i][j][3] = cur;
                cur = accumulateEnemy(grid[i][j], cur);
            }
        }
        for (int j = 0; j < m; j++) {
            // top to down
            int cur = 0;
            for (int i = 0; i < n; i++) {
                enemyInDir[i][j][0] = cur;
                cur = accumulateEnemy(grid[i][j], cur);
            }
            // down to top
            cur = 0;
            for (int i = n - 1; i >= 0; i--) {
                enemyInDir[i][j][1] = cur;
                cur = accumulateEnemy(grid[i][j], cur);
            }
        }
        return enemyInDir;
    }

    private int accumulateEnemy(char ch, int count) {
        if (ch == 'W') {
            return 0;
        } else if (ch == 'E') {
            return count + 1;
        }
        return count;
    }
}


/*
idea:
1. enumerate each empty cell, count enemy in the row and col.
    O(n*m* (n + m))

missing case:
[["W","W","W","W","E"],
 ["W","E","E","E","E"],
 ["W","E","0","E","0"],
 ["W","E","E","E","E"],
 ["W","W","W","W","W"]]

2. 优化的方案
没有利用遍历过的信息。  consider one row:  ["W","E","0","E","0"],
                        enemy in left:   0    0   1   1   1
                        enemy in right:  2    1   1   0  0
这样预处理之后，统计敌人数量时，就直接把四个数相加即可，不用遍历了。时间复杂度是: O(mn)。
*/
```
