## 题目

链接：[47. 全排列 II - 力扣（LeetCode）](https://leetcode.cn/problems/permutations-ii/)
```
Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.


Example 1:
	Input: nums = [1,1,2]
	Output:
	[[1,1,2],
	 [1,2,1],
	 [2,1,1]]
Example 2:
	Input: nums = [1,2,3]
	Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
	 
Constraints:
	1 <= nums.length <= 8
	-10 <= nums[i] <= 10
```

## 思路

- 主要思路跟 LC46 一样，还是回溯算法，难点在如何去重。我们在做 n 个元素的全排列时，第一步是把每个元素单独拿出来作为第一个元素，再对剩下元素进行全排列。第一步时候，如果发现拿到的元素已经出现在首位时，我们就直接跳过 （怎么知道已经放过了呢？ 可以用一个 Set 来记录）。
- 启发 2：去重的话，还有一种做法是，对数组进行排序，然后一次处理一批（相同的数）。

## 代码1 - 使用 hash 表去重 - ✅

```java
class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) { // time: O(n! n), space: O(n) for stack space, O(n) for dedup.
        List<List<Integer>> ans = new ArrayList<>();
        backtrack(nums, 0, ans);
        return ans;
    }

    void backtrack(int[] nums, int index, List<List<Integer>> ans) {
        if (index == nums.length - 1) {
            ans.add(Arrays.stream(nums).boxed().collect(Collectors.toList()));
            return;
        }

        Set<Integer> visited = new HashSet<>();
        for (int i = index; i < nums.length; i++) {
            if (!visited.contains(nums[i])) {
                visited.add(nums[i]);
                swap(nums, i, index);
                backtrack(nums, index + 1, ans);
                swap(nums, i, index);
            }
        }
    }

    void swap(int[] A, int i, int j) {
        int t = A[i]; A[i] = A[j]; A[j] = t;
    }
}
```

## 代码 2 - 排序去重 - ❌

有bug, 不能这么写，因为 swap 之后，对后续的全排列来说，数组已经乱序了。

```java
class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(nums, 0, ans);
        return ans;
    }

    void backtrack(int[] nums, int index, List<List<Integer>> ans) {
        if (index == nums.length - 1) {
            ans.add(Arrays.stream(nums).boxed().collect(Collectors.toList()));
            return;
        }
        
        for (int i = index; i < nums.length; i++) {
            if (i > index && nums[i] == nums[index]) { // dedup
                continue;
            }
            swap(nums, i, index);
            backtrack(nums, index + 1, ans);
            swap(nums, i, index);        
        }
    }

    void swap(int[] A, int i, int j) {
        int t = A[i]; A[i] = A[j]; A[j] = t;
    }
}
```