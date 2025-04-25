
该题掌握程度：
- #熟练✓

Tag: #dfs

## 1. 题目
题目链接： https://leetcode.cn/problems/number-of-islands/

Given an `m x n` 2D binary grid `grid` which represents a map of `'1'`s (land) and `'0'`s (water), return *the number of islands*.

An **island** is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.

**Example 1:**

```
Input: grid = [
  ["1","1","1","1","0"],
  ["1","1","0","1","0"],
  ["1","1","0","0","0"],
  ["0","0","0","0","0"]
]
Output: 1
```

**Example 2:**

```
Input: grid = [
  ["1","1","0","0","0"],
  ["1","1","0","0","0"],
  ["0","0","1","0","0"],
  ["0","0","0","1","1"]
]
Output: 3
```

**Constraints:**

- `m == grid.length`

- `n == grid[i].length`

- `1 <= m, n <= 300`

- `grid[i][j]` is `'0'` or `'1'`.

	

## 2. 最佳思路

- Use DFS on each un-visited land, one dfs can mark all connected islands as visited.
- Count how many DFS calls on the initial loop.

## 3. 最佳代码

Note: Other than using a boolean array for checking visibility, we can also update the input grid to achieve that (mark `grid[i][j] to '0'` after finished traversal of one island)

### Code 3 - almost same

```java
class Solution {
    public int numIslands(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        int ans = 0;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    dfsFindIsland(grid, visited, i, j);
                    ans++;
                }
            }
        }
        return ans;
    }

    private int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // up, down, left, right

    private void dfsFindIsland(char[][] grid, boolean[][] visited, int x, int y) {
        int m = grid.length, n = grid[0].length;
        if (x < 0 || x >= m || y < 0 || y >= n || visited[x][y] || grid[x][y] == '0') {
            return;
        }
        visited[x][y] = true;
        for (int[] dir : dirs) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            dfsFindIsland(grid, visited, nx, ny);
        }
    }
}
```

### Code 2 - change the way to iterate dirs

```java
class Solution {
    public int numIslands(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        int ans = 0;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    dfsFindIsland(grid, visited, i, j);
                    ans++;
                }
            }
        }
        return ans;
    }

    private void dfsFindIsland(char[][] grid, boolean[][] visited, int x, int y) {
        int m = grid.length, n = grid[0].length;
        if (x < 0 || x >= m || y < 0 || y >= n || visited[x][y] || grid[x][y] == '0') {
            return;
        }
        visited[x][y] = true;
        dfsFindIsland(grid, visited, x - 1, y);
        dfsFindIsland(grid, visited, x + 1, y);
        dfsFindIsland(grid, visited, x, y - 1);
        dfsFindIsland(grid, visited, x, y + 1);
    }
}
```

### Basic DFS
```java
class Solution {
    public int numIslands(char[][] grid) {
        int n = grid.length, m = grid[0].length;
        boolean[][] visited = new boolean[n][m];
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    dfs(grid, visited, i, j);
                    ans++;
                }
            }
        }
        return ans;
    }

    final int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    void dfs(char[][] grid, boolean[][] visited, int i, int j) {
        int n = grid.length, m = grid[0].length;
        visited[i][j] = true;

        // check neigbhours
        for (int[] dir : dirs) {
            int nx = i + dir[0], ny = j + dir[1];
            if (nx >= 0 && nx < n && ny >= 0 && ny < m && grid[i][j] == '1' && !visited[nx][ny]) {
                dfs(grid, visited, nx, ny);
            }
        }
    }
}
```

- **时间复杂度**：O(nm)
  每个点最多被 dfs 遍历一遍，总共 `n*m` 个点。

- **空间复杂度**：O(nm) - 主要是 visited 数组，以及 DFS 的栈空间调用， 栈的最大深度是 `n*m`。