## 题目 - 对有重复元素的数组求所有子集

[90. 子集 II - 力扣（LeetCode）](https://leetcode.cn/problems/subsets-ii/description/)

```
Given an integer array nums that may contain duplicates, return all possible subsets (the power set).
The solution set must not contain duplicate subsets. Return the solution in any order.

Example 1:
	
	Input: nums = [1,2,2]
	Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]

Example 2:
	
	Input: nums = [0]
	Output: [[],[0]]
 

Constraints:
	
	1 <= nums.length <= 10
	-10 <= nums[i] <= 10
```

## 思路

- 与 [LC78 Subsets](LC78%20Subsets.md) 的区别是，数组中有重复元素了，同样可以用回溯法来写，问题是如何去重？或者怎么转化成无重复的
- 回溯：在求一个子集时，针对数组中每个元素，它有两种选择，选它或不选它，枚举所有的可能
- **转化成子集 1**： 统计 nums 中元素出现的频率，然后对无重复的元素求子集即可（每个重复的元素对应多种选择，选择 0 个，1 个，... K个）
- **排序去重**：需要先排序，当出现重复元素时，需要利用上一次有没有选择这个重复元素，只有上一次选择了，这次才能选择

## 代码1 - 使用 hash 表 （好理解）

```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        // time: O(2^n n). 子集个数2^n, 将每个方案 copy 到结果 List 中 O(n). 
        // space: O(n). 栈的最大深度n, curPath最大的个数n, 哈希表最大的元素个数 n
        Map<Integer, Integer> numFreq = new HashMap<>();
        for (int num : nums) {
            numFreq.put(num, numFreq.getOrDefault(num, 0) + 1);
        }
        List<Integer> uniqNums = new ArrayList<>(numFreq.keySet());
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        backtrack(uniqNums, numFreq, 0, path, ans);
        return ans;
    }

    void backtrack(List<Integer> nums, Map<Integer, Integer> numFreq, int index, List<Integer> path, List<List<Integer>> ans) {
        if (index == nums.size()) {
            ans.add(new ArrayList<>(path));
            return;
        }
        int num = nums.get(index);
        int count = numFreq.get(num); // 跟子集 1 的区别是，每个元素可能不止一种选择了，可以选多个
        for (int i = 0; i <= count; i++) { // i == 0 对应不选择num的情况
            backtrack(nums, numFreq, index + 1, curPath, ans);
            path.add(num);
        }
        
        for (int i = 0; i <= count; i++) { // pop back
            path.remove(curPath.size() - 1);
        }
    }
}
```

## 代码 2 - 排序去重 （不太好理解）

相当于给每个重复的元素都进行独立编号，只有编号小的都选了，才有资格选择大编号的。

```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
	    // time: O(2^n * n) <= 排序 O(n logn), backtrack: O(2^n * n)
	    // space: O(n) <= backtrack 最大深度 n，path 最多保存 n 个元素
	    
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        backtrack(nums, 0, path, false, ans);
        return ans;
    }

    void backtrack(int[] A, int i, List<Integer> path, boolean prevSelected, List<List<Integer>> ans) {
        if (i == A.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        // not select A[i]
        backtrack(A, i + 1, path, false, ans);

        // select A[i]
        if (i == 0 || A[i] != A[i-1] || prevSelected) {
            path.add(A[i]);
            backtrack(A, i + 1, path, true, ans);
            path.remove(path.size() - 1);
        }
    }
}
```