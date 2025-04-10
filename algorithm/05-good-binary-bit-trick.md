# 巧妙的二进制思想

有一些算法题目，联系到二进制的表示后，思路会很简单，实现起来也很容易。

## 1. [78. 子集](https://leetcode.cn/problems/subsets/)

问题描述：给定了n个元素的集合，求它的所有子集。根据数学公式，我们知道，子集的个数是 2^n个。
枚举的时候，可以分别枚举 0个元素的空集，1个元素 n个，2个元素 `C(n,2)`, ... ，n个元素 1个。 这里也涉及到数学公式，二项式系数的概念：
`(1+1)^n = C(n, 0) +C(n, 1) + ... + C(n, n)`.

### 1.1. 常规写法：递归

思路：考虑每一个元素，选择它，或者不选择它。这里其实也可以跟背包问题联系起来，背包也是考虑某个物品选择或者不选择，都是属于选择类的问题。

画出一个递归树，很容易写出来。
![](https://i.hish.top:8/2023/03/16/222357.png)

```java
class Solution {
    public List<List<Integer>> subsets(int[] A) {
        ans = new ArrayList<>();
        dfs(A, 0, new ArrayList<>());
        return ans;
    }

    List<List<Integer>> ans;

    void dfs(int[] A, int index, List<Integer> path) {
        if (index == A.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        // option 1: not select A[index]
        dfs(A, index + 1, path);

        // option 2: select A[index]
        path.add(A[index]);
        dfs(A, index + 1, path);
        path.remove(path.size() - 1);
    }
}
```

### 1.2. 巧妙的二进制思想

思想：对于n个数，求子集时每个数有两种选择，选或者不选。总共有2^n种可能性。联系到n位的二进制，正好能表示 0-2^n -1 这么多个数的，范围内的每一个数，它的二进制表示，都能映射到一种选择方案，当第i位为1表示构成的子集中选择了 A[i]，0表示不选。

```java
class Solution {
    public List<List<Integer>> subsets(int[] A) { // time: O(2^n * n), space: O(n)
        List<List<Integer>> ans = new ArrayList<>();
        int n = A.length;

        // 枚举n位二进制数能表示的范围内的每个数 (0 ~ 2^n - 1)
        for(int i = 0; i < 1 << n; i++) {
            List<Integer> path = new ArrayList<>();

            // 检查当前方案i 的二进制数中每一个二进制位 (总共n位)，获取具体选择的方案
            int mask = 1;
            for(int j = 0; j < n; j++) {
                if ((i & mask) != 0) { // 第j位是 1 ==> 选择A[j]
                    path.add(A[j]);
                }
                mask <<= 1;
            }
            ans.add(path);
        }
        return ans;
    }
}
```