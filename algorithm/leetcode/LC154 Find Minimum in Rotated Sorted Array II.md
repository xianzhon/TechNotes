
该题掌握程度：
- #熟练✓


## 1. 题目
题目链接：[154. 寻找旋转排序数组中的最小值 II - 力扣（LeetCode）](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array-ii/)

Suppose an array of length `n` sorted in ascending order is **rotated** between `1` and `n` times. For example, the array `nums = [0,1,4,4,5,6,7]` might become:

- `[4,5,6,7,0,1,4]` if it was rotated `4` times.
- `[0,1,4,4,5,6,7]` if it was rotated `7` times.

Notice that **rotating** an array `[a[0], a[1], a[2], ..., a[n-1]]` 1 time results in the array `[a[n-1], a[0], a[1], a[2], ..., a[n-2]]`.

Given the sorted rotated array `nums` that may contain **duplicates**, return *the minimum element of this array*.

You must decrease the overall operation steps as much as possible.

 

**Example 1:**

```
Input: nums = [1,3,5]
Output: 1
```

**Example 2:**

```
Input: nums = [2,2,2,0,1]
Output: 0
```

 

**Constraints:**

- `n == nums.length`
- `1 <= n <= 5000`
- `-5000 <= nums[i] <= 5000`
- `nums` is sorted and rotated between `1` and `n` times.

 

**Follow up:** This problem is similar to [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/description/), but `nums` may contain **duplicates**. Would this affect the runtime complexity? How and why?



## 2. 最佳思路

- because the array has duplicates number, so we can’t use `nums[0]` directly to do binary search, as in the end of right sequence, there might be same value as `nums[0]`. 
- So we could eliminate same value as `nums[0]` in right side, so that we can sure the right elements are all **less than** first number. so that we can use binary search on the shortened array.


## 3. 最佳代码

```java
class Solution {
    public int findMin(int[] nums) {
        int n = nums.length;
        int low = 0, high = n - 1;
        
        while (high > 0 && nums[high] == nums[0]) { // remove same number as nums[0] in the right side
            high--;
        }
        if (nums[0] <= nums[high]) { // if only 1 element remains, we also need to return here.
            return nums[low];
        }
        // find right most pos s.t. nums[pos] >= nums[0]
        int maxIdx = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (nums[mid] >= nums[0]) {
                maxIdx = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return nums[maxIdx + 1];
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n), the worst case is that all elements are the same. 
  
- **空间复杂度**：O(1)

### 3.2 特别注意

- 算法思路：easy.
- 实现细节：mid.
