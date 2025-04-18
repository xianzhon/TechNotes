
## 题目

[250. 统计同值子树 - 力扣（LeetCode）](https://leetcode.cn/problems/count-univalue-subtrees/description/)

![image-20250418162837909](https://i.hish.top:8/2025/04/18/162838.png)


## 思路

1. use a global counter to track the uni-value tree number
2. use postOrder to traverse the tree, when handle each tree, check whether all sub-tree is uni-value, 
   if both sub-trees are uni-value tree, and the node.value same as two child, then current tree is uni-tree, increment the counter by 1,  otherwise, return false.
3. base condition: if tree is null, return true but do not increment the counter.

## 代码1

```java
class Solution {
    public int countUnivalSubtrees(TreeNode root) { // time: O(n), space: O(log n)
        AtomicInteger counter = new AtomicInteger(0); // 不想使用实例变量，所以就用了原子类
        isUnivalSubstree(root, counter);
        return counter.get();
    }

    private boolean isUnivalSubstree(TreeNode root, AtomicInteger counter) {
        // postOrder traversal
        if (root == null) {
            return true;
        }
        boolean leftAns = isUnivalSubstree(root.left, counter);
        boolean rightAns = isUnivalSubstree(root.right, counter);
        boolean sameValAsLeft = root.left == null || root.left.val == root.val;
        boolean sameValAsRight = root.right == null || root.right.val == root.val;

        if (leftAns && rightAns && sameValAsLeft && sameValAsRight) {
            counter.getAndIncrement();
            return true;
        }
        return false;
    }
}
```

## 代码 2 - 判断 node 的值是否相等，在儿子节点上判断

不太推荐这个写法，不容易理解这个递归函数返回值的含义。

```java
class Solution {
    public int countUnivalSubtrees(TreeNode root) {
        if (root == null) {
            return 0;
        }
        isUnivalSubtree(root, root.val);
        return counter;
    }

    int counter = 0;

    boolean isUnivalSubtree(TreeNode root, int val) {
        // 返回值表示的是父亲这科树下,当前root这个孩子是否满足条件
        if (root == null) {
            return true;
        }
        boolean leftAns = isUnivalSubtree(root.left, root.val);
        boolean rightAns = isUnivalSubtree(root.right, root.val);
        if (!leftAns || !rightAns) {
            return false;
        }
        counter++;  // 当前 root 这个子树是满足条件的
        return root.val == val;
    }
}
```