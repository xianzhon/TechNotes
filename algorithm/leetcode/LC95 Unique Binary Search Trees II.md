## 题目

[95. 不同的二叉搜索树 II - 力扣（LeetCode）](https://leetcode.cn/problems/unique-binary-search-trees-ii/)

![image-20250420094934126](https://i.hish.top:8/2025/04/20/094934.png)

## 思路

- enumerate the root node:1, 2, 3 for example n = 3
- recursively generate left tree & right tree, then combine each left with each right subtree.

## 代码1 - AC

```java
class Solution {
    public List<TreeNode> generateTrees(int n) {
        return dfs(1, n);
    }

    List<TreeNode> dfs(int low, int high) {
        List<TreeNode> ans = new ArrayList<>();
        if (low > high) { // base case
            ans.add(null);
            return ans;
        }
        // enumerate each root
        for (int i = low; i <= high; i++) {
            List<TreeNode> leftAns = dfs(low, i - 1);
            List<TreeNode> rightAns = dfs(i + 1, high);
            for (TreeNode left : leftAns) {
                for (TreeNode right: rightAns) {
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    ans.add(root);
                }
            }
        }
        return ans;
    }
}
```

- 时间复杂度：整个时间复杂度取决于 n 个节点有多少个不同的二叉搜索树，答案是 第 n 个卡特兰数，`C（n） = 1/(n+1) * C(2n, n)`。每个dfs调用，组合数量是左右子树数量的乘积，因此总的时间复杂度是 `O(C(n))`。由于 `C(n) =1/(n+1) * C(2n, n) = O(4^n / n^{1.5})`，因此时间复杂度是 `O(4^{n/2}) = O(2^n)`。
- 空间复杂度：我们使用 List 保存左右子树的结果，总的复杂度也是 `O(2^n)`

## 代码 2 - 参考他人的 - 记忆化搜索

避免解决重复的子问题。

```java
class Solution {
    public List<TreeNode> generateTrees(int n) {
        memo = new ArrayList[n + 1][n + 1];
        return dfs(1, n);
    }

    private List<TreeNode>[][] memo;

    List<TreeNode> dfs(int low, int high) {
        List<TreeNode> ans = new ArrayList<>();
        if (low > high) {
            ans.add(null);
            return ans;
        }
        if (memo[low][high] != null) {
            return memo[low][high];
        }

        // enumerate each root
        for (int i = low; i <= high; i++) {
            List<TreeNode> leftAns = dfs(low, i - 1);
            List<TreeNode> rightAns = dfs(i + 1, high);
            for (TreeNode left : leftAns) {
                for (TreeNode right: rightAns) {
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    ans.add(root);
                }
            }
        }
        memo[low][high] = ans;
        return ans;
    }
}
```


