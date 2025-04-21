
该题掌握程度：
- #一般⭕️

Tag: #拓扑排序 

## 1. 题目
题目链接：[207. 课程表 - 力扣（LeetCode）](https://leetcode.cn/problems/course-schedule/description/)
```
There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return true if you can finish all courses. Otherwise, return false.
 

Example 1:
	Input: numCourses = 2, prerequisites = [[1,0]]
	Output: true
	Explanation: There are a total of 2 courses to take. 
	To take course 1 you should have finished course 0. So it is possible.

Example 2:	
	Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
	Output: false
	Explanation: There are a total of 2 courses to take. 
	To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.
	 

Constraints:
	1 <= numCourses <= 2000
	0 <= prerequisites.length <= 5000
	prerequisites[i].length == 2
	0 <= ai, bi < numCourses
	All the pairs prerequisites[i] are unique.
```
## 2. 最佳思路

### BFS - 拓扑排序
- 根据课程的依赖关系，创建有向图（couse 0 依赖课程 1，那么创建一条边： 1 -> 0，表示只有 1 完成了，才能进行课程 0）
- 拓扑排序，例如入度为 0 的那些课程，放到 BFS 的 queue 里，逐层去遍历 queue，需要用 boolean 数组去检测是否存在环，进 queue 之前标记 true
- 最后如果拓扑结束，还有课程没有访问，则说明存在环，return false，否则就是 true

### DFS - 标记
- 从每个顶点出发做一次 DFS，用一个 int 数组标记，0 为未访问，1 表示正在遍历中，2 表示遍历结束
- 如果存在环，则在遍历某个连通分量时，再次碰到标记为 1 的顶点。使用全局变量，记录这种有环的状态

## 3. 最佳代码 - BFS

```java
class Solution {
    List<List<Integer>> edges;

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        edges = new ArrayList<>();
        for(int i = 0; i < numCourses; i++) {
            edges.add(new ArrayList<>());
        }
        int[] indegree = new int[numCourses];

        // construct map
        for(int[] edge: prerequisites) {
            int a = edge[0], b = edge[1];
            edges.get(b).add(a);
            indegree[a]++;
        }

        // detect circle in graph
        Deque<Integer> que = new ArrayDeque<>();
        for(int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                que.offer(i);
            }
        }
        int count = 0;
        while (!que.isEmpty()) {
            int cur = que.poll();
            count++;
            for(int next : edges.get(cur)) {
                if (--indegree[next] == 0) {
                    que.offer(next);
                }
            }            
        }

        return count == numCourses;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n + e)
  创建图：O(n+e); 拓扑排序：O(n+e) 每个顶点进出 queue 最多一次，queue 最大长度是 n，遍历所有边，时间是 e。

- **空间复杂度**：O(n+e)
  保存图（顶点+边），boolean 数组最大长度 n。

### 3.2 特别注意

- 算法思路：easy
- 实现细节：mid
- 算法证明：easy
- 时空复杂度分析：easy

## 4. 其他解法

### 4.1 解法2 - DFS - 很好

```java
class Solution {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    int[] seen;  // seen[i], 0: not searched yet, 1: searching, 2: all child and itself done
    boolean valid;

// DFS: time O(n + m)
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // build graph
        seen = new int[numCourses];
        for (int[] edge: prerequisites) {
            int from = edge[1], to = edge[0];
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }
        
        valid = true;
        for (int i = 0; i < numCourses; i++) {
            if (seen[i] == 0) {
                dfs(i);
            }
        }
        return valid;
    }

    void dfs(int node) {
        seen[node] = 1;
        if (graph.containsKey(node)) {
            for (int next : graph.get(node)) {
                if (seen[next] == 0) {
                    dfs(next);                    
                } else if (seen[next] == 1) {
                    valid = false;
                    return;
                }
            }
        }
        seen[node] = 2;
    }
}
/**
如果是DAG，那么遍历时，不需要用visited数组来标记，就像二叉树的DFS一样。
否则，如果是有环图，那么会遍历到自己。
 */
```


### 4.2 写法3 - 拓扑排序 - 写法不好

```java
class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // build graph        
        List<Integer>[] graph = new ArrayList[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        int[] indegree = new int[numCourses];
        for (int[] edge : prerequisites) {
            int a = edge[0], b = edge[1]; // course a relies on course b, b -> a
            graph[b].add(a);
            indegree[a]++;
        }

        // topology sort
        Queue<Integer> que = new LinkedList<>();
        boolean[] visited = new boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                que.offer(i);
                visited[i] = true;
            }
        }

        while (!que.isEmpty()) {
            int size = que.size();
            for (int i = 0; i < size; i++) {
                int cur = que.poll();
                for (int next : graph[cur]) {        
                    if (!visited[next] && --indegree[next] == 0) {
                        visited[next] = true;
                        que.offer(next);
                    }
                }
            }
        }
        for (boolean node : visited) {
            if (!node) { // means has cycle
                return false;
            }
        }
        return true;
    }
}
```
- visited 数组是多余。直接统计拓扑排序得到的节点 count，最后判断 count == 顶点数即可
- 不需要按层遍历



