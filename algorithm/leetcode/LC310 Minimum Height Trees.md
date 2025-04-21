
该题掌握程度：
- #一般⭕️

Tag: #拓扑排序

## 1. 题目
题目链接： [310. 最小高度树 - 力扣（LeetCode）](https://leetcode.cn/problems/minimum-height-trees/description/)
```
A tree is an undirected graph in which any two vertices are connected by exactly one path. In other words, any connected graph without simple cycles is a tree.

Given a tree of n nodes labelled from 0 to n - 1, and an array of n - 1 edges where edges[i] = [ai, bi] indicates that there is an undirected edge between the two nodes ai and bi in the tree, you can choose any node of the tree as the root. When you select a node x as the root, the result tree has height h. Among all possible rooted trees, those with minimum height (i.e. min(h))  are called minimum height trees (MHTs).

Return a list of all MHTs' root labels. You can return the answer in any order.

The height of a rooted tree is the number of edges on the longest downward path between the root and a leaf.


Input: n = 6, edges = [[3,0],[3,1],[3,2],[3,4],[5,4]]
Output: [3,4]
 
Constraints:
	1 <= n <= 2 * 10^4
	edges.length == n - 1
	0 <= ai, bi < n
	ai != bi
	All the pairs (ai, bi) are distinct.
	The given input is guaranteed to be a tree and there will be no repeated edges.
```
![image-20250420172057042](https://i.hish.top:8/2025/04/20/172057.png)

## 2. 最佳思路

- 创建图，在图上采用拓扑排序，最后剩下的顶点就是答案。

## 3. 最佳代码

```java
class Solution {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (edges.length == 0) {
            return new ArrayList(Arrays.asList(0));
        }

        // build a graph
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] indegree = new int[n];
        for (int[] edge : edges) {
            int a = edge[0], b = edge[1]; // a->b
            indegree[b]++;
            indegree[a]++;
            graph.putIfAbsent(a, new ArrayList<>());
            graph.putIfAbsent(b, new ArrayList<>());
            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        boolean[] visited = new boolean[n];
        Queue<Integer> que = new LinkedList<>();
        // add all nodes whose indegree == 1 to the que
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 1) {
                que.offer(i);
                visited[i] = true;
            }
        }
        
        List<Integer> lastLevelNums = new ArrayList<>();
        while (!que.isEmpty()) {
            int size = que.size();
            lastLevelNums = new ArrayList<>(que);
            for (int i = 0; i < size; i++) {
                int node = que.poll();
                for (int next : graph.get(node)) {
                    indegree[next]--;
                    if (indegree[next] == 1) {
                        que.offer(next);
                    }
                }
            }
        }
        return lastLevelNums;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n), n是顶点数
  创建图的时间是: O(n)，因为边数是n - 1，所以也是和 n 有关了。 BFS 遍历整个图，每个节点进出 Queue 各一次。访问边的次数是 O(n)

- **空间复杂度**： O(n)
  使用了 Queue 和 visited 数组，最大长度都是 n
### 3.2 特别注意

- 算法思路：mid
- 实现细节：easy
- 算法证明：easy
- 时空复杂度分析：easy