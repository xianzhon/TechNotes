
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[505. 迷宫 II - 力扣（LeetCode）](https://leetcode.cn/problems/the-maze-ii/description/)

There is a ball in a `maze` with empty spaces (represented as `0`) and walls (represented as `1`). The ball can go through the empty spaces by rolling **up, down, left or right**, but it won't stop rolling until hitting a wall. When the ball stops, it could choose the next direction.

Given the `m x n` `maze`, the ball's `start` position and the `destination`, where `start = [startrow, startcol]` and `destination = [destinationrow, destinationcol]`, return *the shortest **distance** for the ball to stop at the destination*. If the ball cannot stop at `destination`, return `-1`.

The **distance** is the number of **empty spaces** traveled by the ball from the start position (excluded) to the destination (included).

You may assume that **the borders of the maze are all walls** (see examples).

 

**Example 1:**

![img](https://i.hish.top:8/2025/05/08/151442.jpg)

```
Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]], start = [0,4], destination = [4,4]
Output: 12
Explanation: One possible way is : left -> down -> left -> down -> right -> down -> right.
The length of the path is 1 + 1 + 3 + 1 + 2 + 2 + 2 = 12.
```

**Example 2:**

![img](https://i.hish.top:8/2025/05/08/151443.jpg)

```
Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]], start = [0,4], destination = [3,2]
Output: -1
Explanation: There is no way for the ball to stop at the destination. Notice that you can pass through the destination but you cannot stop there.
```

**Example 3:**

```
Input: maze = [[0,0,0,0,0],[1,1,0,0,1],[0,0,0,0,0],[0,1,0,0,1],[0,1,0,0,0]], start = [4,3], destination = [0,1]
Output: -1
```

 

**Constraints:**

- `m == maze.length`
- `n == maze[i].length`
- `1 <= m, n <= 100`
- `maze[i][j]` is `0` or `1`.
- `start.length == 2`
- `destination.length == 2`
- `0 <= start.row, destination.row < m`
- `0 <= start.col, destination.col < n`
- Both the ball and the destination exist in an empty space, and they will not be in the same position initially.
- The maze contains **at least 2 empty spaces**.

## 2. 最佳思路

## 3. 最佳代码

```java
import java.util.*;

class Solution {
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        int n = maze.length, m = maze[0].length;
        int[][] distance = new int[n][m]; // Tracks min distance to each cell
        for (int[] row : distance) 
            Arrays.fill(row, Integer.MAX_VALUE);
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]); // Min-heap
        pq.offer(new int[] {start[0], start[1], 0});
        distance[start[0]][start[1]] = 0;

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[0], y = cur[1], dist = cur[2];
            
            if (x == destination[0] && y == destination[1]) {
                return dist;
            }
            
            if (dist > distance[x][y]) continue; // Skip if a better path exists
            
            for (int[] dir : dirs) {
                int nx = x, ny = y, steps = 0;
                // Roll until hit wall
                while (isInRange(nx + dir[0], ny + dir[1], n, m) 
                    && maze[nx + dir[0]][ny + dir[1]] == 0) {
                    nx += dir[0];
                    ny += dir[1];
                    steps++;
                }
                // Update distance if shorter path found
                if (dist + steps < distance[nx][ny]) {
                    distance[nx][ny] = dist + steps;
                    pq.offer(new int[] {nx, ny, distance[nx][ny]});
                }
            }
        }
        return -1;
    }
    
    boolean isInRange(int x, int y, int n, int m) {
        return x >= 0 && x < n && y >= 0 && y < m;
    }
}
```


- **Time Complexity**:
    - **Heap Operations**: Each cell can be processed up to 4 times (one per direction), and each heap insertion/extraction takes `O(log(mn))`.
    - **Total**: `O(mn log(mn))` (since in the worst case, all cells enter the heap).
    
- **Space Complexity**:
    - **Distance Matrix**: `O(mn)` to store the shortest distances.
    - **Priority Queue**: `O(mn)` in the worst case.
    - **Total**: `O(mn)`.


## 4. 其他写法

### 4.1 写法 2 - 思路一样 - 最短路径 - 优先队列

```java
class Solution {
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        int n = maze.length, m = maze[0].length;
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        int[][] dist = new int[n][m];
        for (int[] row: dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        minHeap.offer(new int[] {start[0], start[1], 0});
        dist[start[0]][start[1]] = 0;

        // 4 dir
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; //up/down/left/right

        while (!minHeap.isEmpty()) {
            int[] cur = minHeap.poll();
            int x = cur[0], y = cur[1], d = cur[2];
            if (x == destination[0] && y == destination[1]) {
                return d;
            }
            for (int[] dir: dirs) {
                int step = 0;
                int nx = x + dir[0], ny = y + dir[1];
                while (isInRange(nx, ny, n, m) && maze[nx][ny] == 0) {
                    step++;
                    nx += dir[0];
                    ny += dir[1];
                }
                if (step == 0) continue;
                // one step back
                nx -= dir[0];                    
                ny -= dir[1];
                if (d + step < dist[nx][ny]) { // distrala's relax step. go to (nx, ny) from point (x,y) is shorter
                    dist[nx][ny] = d + step;
                    minHeap.offer(new int[] {nx, ny, dist[nx][ny]});
                }                
            }
        }
        return -1;
    }
    boolean isInRange(int x, int y, int rowNum, int colNum) {
        return x >= 0 && x < rowNum && y >= 0 && y < colNum;
    }
}
/*
distrala.
    int[][] dist; // store min-dist from the ball to it.
    source -> other nodes
        {ball_pos} -> {other nodes}
    min{dist} --> {other nodes}    
*/
```

- time: minHeap max number: mn. each que operation log(nm). total is: O(nm log (nm)).
- space: dist array, nm; minHeap max elem number: nm. so totall is: O(nm)