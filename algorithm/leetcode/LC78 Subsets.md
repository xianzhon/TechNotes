## 题目：对没有重复元素的集合求子集

链接：[78. 子集 - 力扣（LeetCode）](https://leetcode.cn/problems/subsets/)

```
Given an integer array nums of unique elements, return all possible subsets (the power set).
The solution set must not contain duplicate subsets. Return the solution in any order.

Example 1:
	Input: nums = [1,2,3]
	Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]

Example 2:
	Input: nums = [0]
	Output: [[],[0]]

Constraints:
	1 <= nums.length <= 10
	-10 <= nums[i] <= 10
	All the numbers of nums are unique.
```

## 思路

- 考虑每个子集,它实际是对 n 个元素进行的选择(取,或不取). 所以可以用回溯的算法去考察每个元素是否放到子集中.
- 当所有元素都考虑过一遍,那么就得到了一个子集.遍历所有的解空间,就得到全部的子集. 
- 在数学上,子集的个数:2^n

## 代码

```java
class Solution {
    public List<List<Integer>> subsets(int[] nums) { 
        // time: O(2^n n) 子集个数是2^n, backtrack 调用次数是这么多,每得到一个子集,需要 copy 到结果 List 里, O(n).
        // space: O(n) 递归的最大深度 n, curPath保存选中元素,最多 n
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> curPath = new ArrayList<>();
        backtrack(nums, 0, curPath, ans);
        return ans;
    }

	/*
	index: 记录当前要考虑的元素，两种方案，放或者不放
	curPath: 记录已经选择放的元素
	ans: 记录结果
	*/
    void backtrack(int[] nums, int index, List<Integer> curPath, List<List<Integer>> ans) {
        if (index == nums.length) {
            ans.add(new ArrayList<>(curPath));
            return;
        }
        // choose nums[index]
        curPath.add(nums[index]);
        backtrack(nums, index + 1, curPath, ans);
        curPath.remove(curPath.size() - 1); // 回溯

        // not choose
        backtrack(nums, index + 1, curPath, ans);
    }
}
```

## 关联题目

- [LC90 Subsets II](LC90%20Subsets%20II.md)