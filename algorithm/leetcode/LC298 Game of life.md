
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[289. 生命游戏 - 力扣（LeetCode）](https://leetcode.cn/problems/game-of-life/)

The board is made up of an `m x n` grid of cells, where each cell has an initial state: **live** (represented by a `1`) or **dead** (represented by a `0`). Each cell interacts with its [eight neighbors](https://en.wikipedia.org/wiki/Moore_neighborhood) (horizontal, vertical, diagonal) using the following four rules (taken from the above Wikipedia article):

1. Any live cell with fewer than two live neighbors dies as if caused by under-population.
2. Any live cell with two or three live neighbors lives on to the next generation.
3. Any live cell with more than three live neighbors dies, as if by over-population.
4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

The next state of the board is determined by applying the above rules simultaneously to every cell in the current state of the `m x n` grid `board`. In this process, births and deaths occur **simultaneously**.

Given the current state of the `board`, **update** the `board` to reflect its next state.

**Note** that you do not need to return anything.



```
Input: board = [[0,1,0],[0,0,1],[1,1,1],[0,0,0]]
Output: [[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
```

**Constraints:**

- `m == board.length`
- `n == board[i].length`
- `1 <= m, n <= 25`
- `board[i][j]` is `0` or `1`.

 

**Follow up:**

- Could you solve it in-place? Remember that the board needs to be updated simultaneously: You cannot update some cells first and then use their updated values to update other cells.
- In this question, we represent the board using a 2D array. In principle, the board is infinite, which would cause problems when the active area encroaches upon the border of the array (i.e., live cells reach the border). How would you address these problems?



## 2. 最佳代码

思路：直接模拟。空间 O(1) 的技巧就是充分利用 board 给的数据类型 int，除了 0 和 1 两个数之外，我们可以做一个状态的映射，从而知道一个 cell 过去是 dead，现在是 live 状态，或者是过去 live 现在 dead；最后恢复成 0 / 1 即可。


### 代码 1 - space O(1)
- change the cell status to a combined status (which can indicate old status, also new status)
    old dead -> new live: 2
    old live -> new dead: -1
    old live -> new live: 1
```java
class Solution {
    public void gameOfLife(int[][] board) {
        int n = board.length, m = board[0].length;
        int[][] dirs = {{-1, -1}, {-1, 0}, {-1, 1}, // up
                        {0, -1}, {0, 1}, // middle
                        {1, -1}, {1, 0}, {1, 1}}; // down
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int liveCount = 0;
                for (int[] dir : dirs) {
                    int nx = i + dir[0], ny = j + dir[1];
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m && Math.abs(board[nx][ny]) == 1) {
                        liveCount++;
                    }
                }
                if (board[i][j] == 1) {
                    if (liveCount < 2 || liveCount > 3) {
                        board[i][j] = -1;
                    }
                } else {
                    if (liveCount == 3) {
                        board[i][j] = 2;
                    }
                }
            }
        }
        // reset status (2 -> 1, -1: 0)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 2) {
                    board[i][j] = 1;
                }
                if (board[i][j] == -1) {
                    board[i][j] = 0;
                }
            }
        }
    }
}
```
### 代码 2 - space O(nm)
native solution.
```java
class Solution {
    public void gameOfLife(int[][] board) {
        int m = board.length, n = board[0].length;
        int[][] next = new int[m][n];

        int[][] dirs = {{-1, -1}, {-1, 0}, {-1, 1}, // 上：左中右
						{0, -1}, {0, 1},  // 中：左右
					    {1, -1}, {1, 0}, {1, 1}}; // 下：左中右
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // count live neigbhours
                int liveCount = 0;
                for (int[] dir : dirs) {
                    int nx = i + dir[0], ny = j + dir[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && board[nx][ny] == 1) {
                        liveCount++;
                    }
                }
                if (board[i][j] == 1) {
                    next[i][j] = (liveCount < 2 || liveCount > 3) ? 0 : 1;
                } else {
                    next[i][j] = (liveCount == 3) ? 1 : 0;
                }
            }
        }
        // copy back
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = next[i][j];
            }
        }
    }
}
```
