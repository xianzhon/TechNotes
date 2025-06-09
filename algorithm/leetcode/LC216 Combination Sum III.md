
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[216. 组合总和 III - 力扣（LeetCode）](https://leetcode.cn/problems/combination-sum-iii/description/)

Find all valid combinations of `k` numbers that sum up to `n` such that the following conditions are true:

- Only numbers `1` through `9` are used.
- Each number is used **at most once**.

Return *a list of all possible valid combinations*. The list must not contain the same combination twice, and the combinations may be returned in any order.



**Example 1:**

```
Input: k = 3, n = 7
Output: [[1,2,4]]
Explanation:
1 + 2 + 4 = 7
There are no other valid combinations.
```

**Example 2:**

```
Input: k = 3, n = 9
Output: [[1,2,6],[1,3,5],[2,3,4]]
Explanation:
1 + 2 + 6 = 9
1 + 3 + 5 = 9
2 + 3 + 4 = 9
There are no other valid combinations.
```

**Example 3:**

```
Input: k = 4, n = 1
Output: []
Explanation: There are no valid combinations.
Using 4 different numbers in the range [1,9], the smallest sum we can get is 1+2+3+4 = 10 and since 10 > 1, there are no valid combination.
```



**Constraints:**

- `2 <= k <= 9`
- `1 <= n <= 60`
















## 2. 最佳思路

- 总共有 1-9 9 个元素,那么每个元素只有两种选择,选或者不选.
- 回溯, 枚举每个元素的选择方案, 当已经选了 k 个, 且remain = 0, 那么就找到了一个答案
## 3. 最佳代码

```
class Solution {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<Integer> selected = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();
        backtrack(1, n, k, selected, ans);
        return ans;
    }

    void backtrack(int num, int remain, int k, List<Integer> selected, List<List<Integer>> ans) {
        if (k == 0 && remain == 0) {
            ans.add(new ArrayList<>(selected));
            return;
        }
        if (k == 0 || remain < 0 || num > 9) {
            return;
        }
        // not select num
        backtrack(num + 1, remain, k, selected, ans);

        // select num
        selected.add(num);
        backtrack(num + 1, remain - num, k - 1, selected, ans);
        selected.remove(selected.size() - 1);
    }
}
/*
总共有 1-9 9 个元素,那么每个元素只有两种选择,选或者不选.
回溯, 枚举每个元素的选择方案,当已经选了 k 个,且remain = 0,那么就找到了一个答案
*/
```


有点啰嗦的代码：（不好）
```java
class Solution {
    public List<List<Integer>> combinationSum3(int k, int n) {
        Set<Integer> selected = new HashSet<>();
        List<List<Integer>> ans = new ArrayList<>();
        backtrack(0, k, 1, n, selected, ans);
        return ans;
    }

    void backtrack(int pos, int k, int start, int remain, Set<Integer> selected, List<List<Integer>> ans) {
        if (pos == k) {
            if (remain == 0) {
                ans.add(new ArrayList<>(selected));
            }
            return;
        }
        if (remain < 0) {
            return;
        }
        // select each num in pos
        for (int i = start; i <= 9; i++) {
            if (!selected.contains(i)) {
                selected.add(i);
                backtrack(pos + 1, k, i + 1, remain - i, selected, ans);
                selected.remove(i);
            }
        }
    }
}
/*
backtrack(i, k, selected, ans)
枚举每个位置选哪些元素(记录已经选过的)
枚举到最后一个位置后,结果等于 n 则加入答案。  注意：这里有 bug，结果会包含很多重复，因为枚举每个位置时，每个位置上的元素可以从 1-9 中任意选择。这是一个排列问题了
*/
```


## 关联题目

- [LC40. Combination Sum II](todo-top300/LC40.%20Combination%20Sum%20II.md)
- [LC39. Combination Sum](todo-top300/LC39.%20Combination%20Sum.md)