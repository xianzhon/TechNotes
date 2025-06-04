Link: [163. 缺失的区间 - 力扣（LeetCode）](https://leetcode.cn/problems/missing-ranges/)

## Problem

You are given an inclusive range `[lower, upper]` and a **sorted unique** integer array `nums`, where all elements are within the inclusive range.

A number `x` is considered **missing** if `x` is in the range `[lower, upper]` and `x` is not in `nums`.

Return *the **shortest sorted** list of ranges that **exactly covers all the missing numbers***. That is, no element of `nums` is included in any of the ranges, and each missing number is covered by one of the ranges.

**Example 1:**

```
Input: nums = [0,1,3,50,75], lower = 0, upper = 99
Output: [[2,2],[4,49],[51,74],[76,99]]
Explanation: The ranges are:
[2,2]
[4,49]
[51,74]
[76,99]
```

**Example 2:**

```
Input: nums = [-1], lower = -1, upper = -1
Output: []
Explanation: There are no missing ranges since there are no missing numbers.
```

 

**Constraints:**

- `-109 <= lower <= upper <= 109`
- `0 <= nums.length <= 100`
- `lower <= nums[i] <= upper`
- All the values of `nums` are **unique**.

## Solution

- Idea: very easy, just enumerate each pair, [lower, nums[0]], [nums[i], nums[i+1]], … [nums[n-1], upper] to find whether there is any number missing.
- For implementation, there is a skill to make the code cleaner and shorter by observing the patten of the checking the pair.

```
first pair: [lower, nums[0]] --> add {lower, nums[0] - 1}
middle pairs: [nums[i], nums[i+1]] --> add {nums[i] + 1, nums[i + 1] - 1}
last pair: [nums[n-1], upper] --> add {nums[n-1] + 1, upper}
```



### Native solution

```java
class Solution {
    public List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {
        int n = nums.length;
        List<List<Integer>> ans = new ArrayList<>();
        if (n == 0) {
            ans.add(getRange(lower, upper));
            return ans;
        }
        // check 1st pair [lower, nums[0] - 1]
        if (lower != nums[0]) {
            ans.add(getRange(lower, nums[0] - 1));
        }
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] != nums[i + 1] - 1) {
                ans.add(getRange(nums[i] + 1, nums[i + 1] - 1));
            }
        }
        // check last pair [nums[n-1] + 1, upper]
        if (nums[n - 1] != upper) {
            ans.add(getRange(nums[n-1] + 1, upper));
        }
        return ans;
    }

    List<Integer> getRange(int left, int right) {
        return Arrays.asList(left, right);
    }
}
```



### Improved one

```java
    public List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i <= nums.length; i++) { // enumerate each pair end
            int start = (i == 0) ? lower - 1 : nums[i - 1];
            int end = (i == nums.length) ? upper + 1 : nums[i];
            if (start != end - 1) {
                ans.add(Arrays.asList(start + 1, end - 1));
            }
        }
        return ans;
    }
```

### 写法 3 - best

```java
class Solution {
    public List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {
        List<List<Integer>> ans = new ArrayList<>();
        for (int num: nums) { // enumerate each pair end
            if (lower < num) {
	            ans.add(Arrays.asList(lower, num - 1));
            }
            lower = num + 1;
        }
        if (lower <= upper) {
	        ans.add(Arrays.asList(lower, upper));
        }
        return ans;
    }
}
```