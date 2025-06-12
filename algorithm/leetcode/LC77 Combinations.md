## Problem
[77. 组合 - 力扣（LeetCode）](https://leetcode.cn/problems/combinations/)

Given two integers `n` and `k`, return *all possible combinations of* `k` *numbers chosen from the range* `[1, n]`.

You may return the answer in **any order**.



**Example 1:**

```
Input: n = 4, k = 2
Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
Explanation: There are 4 choose 2 = 6 total combinations.
Note that combinations are unordered, i.e., [1,2] and [2,1] are considered to be the same combination.
```

**Example 2:**

```
Input: n = 1, k = 1
Output: [[1]]
Explanation: There is 1 choose 1 = 1 total combination.
```



**Constraints:**

- `1 <= n <= 20`
- `1 <= k <= n`























## 分析

- 从 1~n 中选择k 个数，不需要考虑顺序问题，这是个选择的问题，方案数是：`C(n, k) = n! / k! (n-k)!`
- 涉及到排列和组合的题目，考虑是否可以用回溯法来做。
- 思路 1：考虑每个数，看它在组合中是否出现，对应两种选择，当我们凑够 k 个数，则就找到了一个组合。【天然没有重复的问题】
- 思路 2：枚举每一个位置，总共 k 个位置，看每个位置可以填入哪些数字。（要注意重复的问题）




```java
// 思路1： 枚举每个元素选或不选，直到选够了 k 个元素
List<List<Integer>> getCombiSum(int n, int k) {
    List<List<Integer>> ans = new ArrayList<>();
	backtrack(1, n, k, new ArrayList<>(), ans);
    return ans;
}

// 依靠 start 来驱动的 (baseCase)
void backtrack(int start, int n, int k, List<Integer> selected, List<List<Integer>> ans) {
    if (selected.size() == k) {
        ans.add(new ArrayList<>(selected));
        return;
    }
    if (start > n) {
        return;
    }
    // not select start
    backtrack(start + 1, n, k, selected, ans);

    // select start
    selected.add(start);
    backtrack(start + 1, n, k, selected, ans);
    selected.remove(selected.size() - 1);
}



// 思路 2: 枚举每个位置，可以选择哪个元素. 要注意去重，第一个位置选了C(n,1). 第二个位置，则只能从第一个位置之后的元素选起，否则有重复
// 这个代码是依靠 len(selected) 来驱动的 （baseCase）
void backtrack(int start, int n, int k, List<Integer> selected, List<List<Integer>> ans) {
    if (selected.size() == k) {
        ans.add(new ArrayList<>(selected));
        return;
    }
    if (start > n) {
        return;
    }
    // cur pos: selected.size() + 1
    for (int i = start; i <= n; i++) {
        selected.add(i);
        backtrack(i + 1, n, k, selected, ans);
        selected.remove（selected.size() - 1);
    }
}
```



## 回溯 - 考虑每个位置

### 版本 2 - AC

```java
class Solution {
    public List<List<Integer>> combine(int n, int k) {
        // time: O(C(n,k) * k) 总共有C(n,k)个组合,需要O(k)去 copy一个组合到答案中去
        // space: O(k), 递归的最大深度是 k, 递归栈空间是 O(k), 保存当前已选的组合的 List,空间是 O(k).
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> curPath = new ArrayList<>();
        backtrack(1, n, k, curPath, ans);
        return ans;
    }

	// 以填空来驱动.  组合枚举的风格的 backtrack。需要注意重复的问题。
    void backtrack(int start, int n, int k, List<Integer> curPath, List<List<Integer>> ans) {
        if (curPath.size() == k) {
            ans.add(new ArrayList<>(curPath));
            return;
        }
        for (int j = start; j <= n; j++) {
            curPath.add(j);
            backtrack(j + 1, n, k, curPath, ans);
            curPath.remove(curPath.size() - 1);
        }
    }
}
```

### 版本 1 - AC 但可改进

```java
class Solution {
    public List<List<Integer>> combine(int n, int k) {
        ans = new ArrayList<>();
        visited = new boolean[n];
        backtrack(1, n, k, new ArrayList<>());
        return ans;
    }

    private boolean[] visited;
    private List<List<Integer>> ans;

    void backtrack(int start, int n, int k, List<Integer> curPath) {
        if (curPath.size() == k) {
            ans.add(new ArrayList<>(curPath));
            return;
        }
        for (int j = start; j <= n; j++) {
            if (!visited[j - 1]) {
                visited[j - 1] = true;
                curPath.add(j);
                backtrack(j + 1, n, k, curPath);
                curPath.remove(curPath.size() - 1);
                visited[j - 1] = false;
            }
        }
    }
}

/*
k pos: enumerate each pos, try to put each number.
n = 4, k = 2

- : 1           2
- : 2   3   4   1
  over over over
nums = 1,2,3,4
visited
*/
```

代码中的问题：
- visited 数组是多余的
- 使用实例变量 ans 来保存递归函数的结果，不是很好，用参数传递更好

## 回溯 - 考虑每个数的两种选择

这种回溯的枚举方式，是二叉树的搜索。比较清晰易懂，推荐使用这个方式。

### 版本 2 - Deepseek改进

```java
class Solution {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        backtrack(1, n, k, new ArrayList<>(), ans);
        return ans;
    }

    private void backtrack(int num, int n, int k, List<Integer> curPath, List<List<Integer>> ans) {
        // If we've collected enough numbers, add to results
        if (curPath.size() == k) {
            ans.add(new ArrayList<>(curPath));
            return;
        }

        // If there aren't enough numbers left to reach k, prune this branch
        if (num > n || (n - num + 1) < (k - curPath.size())) {
            return;
        }

        // Option 1: Include the current number
        curPath.add(num);
        backtrack(num + 1, n, k, curPath, ans);
        curPath.remove(curPath.size() - 1);

        // Option 2: Exclude the current number
        backtrack(num + 1, n, k, curPath, ans);
    }
}
```

### 版本 1

```java
class Solution {
	// time: O(C(n, k) * k). 虽然我们考虑了 n 个数，每个数有两种选择，但是curPath.size() == k 提前结束了，所以时间复杂度不是 2^n. 要考虑 backtrack 函数调用了多少次
    public List<List<Integer>> combine(int n, int k) {
        backtrack(1, n, k);
        return ans;
    }

    List<List<Integer>> ans = new ArrayList<>();
    List<Integer> curPath = new ArrayList<>();

	// 考虑1-n 中的每个数，每个数都有两种选择，放到组合中，或者不放
    void backtrack(int num, int n, int k) {
        if (curPath.size() == k) { // find one answer
            ans.add(new ArrayList<>(curPath));
            return;
        }
        if (num > n) {
            return;
        }

        // choose num
        curPath.add(num);
        backtrack(num + 1, n, k);
        curPath.remove(curPath.size() - 1);

        // do not choose num
        backtrack(num + 1, n, k);
    }
}
```

## 二进制思想

比较巧妙，但是就这题来说，它的复杂度比较高。没有优化空间，因为它是暴力枚举每一个组合。

### 代码 1

说明：代码中 i 的二进制表示中最低的 n 位，代表 1~n中的每个数，是否选择。（1 是选择了，0 是不选择）

```java
class Solution { // time: O(2^n * k), space: O(k)
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) {
            if (Integer.bitCount(i) != k) continue; // 优化
            List<Integer> path = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    path.add(j + 1);
                }
            }
            if (path.size() == k) { // 有了上面的优化，则不需要这里的 if 条件了
                ans.add(path);
            }
        }
        return ans;
    }
}
```

