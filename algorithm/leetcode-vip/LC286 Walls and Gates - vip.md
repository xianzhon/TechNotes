
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[286. 墙与门 - 力扣（LeetCode）](https://leetcode.cn/problems/walls-and-gates/description/)

You are given an `m x n` grid `rooms` initialized with these three possible values.

- `-1` A wall or an obstacle.
- `0` A gate.
- `INF` Infinity means an empty room. We use the value `2^31 - 1 = 2147483647` to represent `INF` as you may assume that the distance to a gate is less than `2147483647`.

Fill each empty room with the distance to *its nearest gate*. If it is impossible to reach a gate, it should be filled with `INF`.

 

**Example 1:**

![img](https://i.hish.top:8/2025/04/26/210638.jpg)

```
Input: rooms = [[2147483647,-1,0,2147483647],[2147483647,2147483647,2147483647,-1],[2147483647,-1,2147483647,-1],[0,-1,2147483647,2147483647]]
Output: [[3,-1,0,1],[2,2,1,-1],[1,-1,2,-1],[0,-1,3,4]]
```

**Example 2:**

```
Input: rooms = [[-1]]
Output: [[-1]]
```

 

**Constraints:**

- `m == rooms.length`
- `n == rooms[i].length`
- `1 <= m, n <= 250`
- `rooms[i][j]` is `-1`, `0`, or `2^31 - 1`.



## 2. 最佳思路

Easy BFS. Start from all the doors, enqueue as level 0, expanding to each empty rooms around the enqueued nodes, the nearst distance for a room to any nearst door is the level count which accessing the empty room.

## 3. 最佳代码

```java
class Solution {
    public void wallsAndGates(int[][] rooms) { // time: O(nm), space: O(nm)
        int n = rooms.length, m = rooms[0].length;
        Queue<int[]> que = new LinkedList<>();

        // enqueue all the doors
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (rooms[i][j] == 0) {
                    que.offer(new int[] {i, j});
                }
            }
        }

        // BFS
        int level = 0;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // up/down, left/right
        while (!que.isEmpty()) {
            ++level;
            int size = que.size();
            for (int i = 0; i < size; i++) {
                int[] cur = que.poll();
                int x = cur[0], y = cur[1];
                for (int[] dir: dirs) {
                    int nx = x + dir[0], ny = y + dir[1];
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m && rooms[nx][ny] == Integer.MAX_VALUE) {
                        rooms[nx][ny] = level;
                        que.offer(new int[] {nx, ny});
                    }
                }
            }
        }  
    }
}
```

一旦我们到达了一个房间，并记录下它的距离时，这意味着我们也标记了这个房间已经被访问过了，这意味着每个房间最多会被访问一次。因此，时间复杂度与门的数量无关，所以时间复杂度为 O(mn) 。