
该题掌握程度：
- #熟练✓

Tag: #dp，后续遍历的应用

## 1. 题目
题目链接：[337. 打家劫舍 III - 力扣（LeetCode）](https://leetcode.cn/problems/house-robber-iii/description/)

![image-20250422153641048](https://i.hish.top:8/2025/04/22/153641.png)

## 2. 最佳思路

- 树形 DP。针对每个节点，我们想要返回两个信息，一个是 rob 它获得的收益，一个是不 rob 它获得的收益。

- 如果 rob 一个节点，那么就无法 rob 它的两个儿子节点。
- 如果不 rob 当前的节点，那么它的两个孩子节点（可以 rob，或者也可以不 rob）【重要，容易犯错】


## 3. 最佳代码

```java
class Solution {
    public int rob(TreeNode root) {
        int[] ans = robHelper(root);
        return Math.max(ans[0], ans[1]);
    }

    int[] robHelper(TreeNode root) {
        // return two value: ans[0] rob root, ans[1], not rob root
        if (root == null) {
            return new int[] {0, 0};
        }
        int[] leftAns = robHelper(root.left);
        int[] rightAns = robHelper(root.right);
        int robRoot = root.val + leftAns[1] + rightAns[1];
        int notRobRoot = Math.max(leftAns[0], leftAns[1]) + Math.max(rightAns[0], rightAns[1]); // take care
        return new int[] {robRoot, notRobRoot};
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n)
  we enumerate each tree node once.

- **空间复杂度**： O(log n)
  The stack space for recursive function.

