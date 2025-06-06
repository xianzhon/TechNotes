
该题掌握程度：
- #熟练✓
Tag: #二分搜索 

## 1. 题目
题目链接：[153. 寻找旋转排序数组中的最小值 - 力扣（LeetCode）](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/)

Suppose an array of length `n` sorted in ascending order is **rotated** between `1` and `n` times. For example, the array `nums = [0,1,2,4,5,6,7]` might become:

- `[4,5,6,7,0,1,2]` if it was rotated `4` times.
- `[0,1,2,4,5,6,7]` if it was rotated `7` times.

Notice that **rotating** an array `[a[0], a[1], a[2], ..., a[n-1]]` 1 time results in the array `[a[n-1], a[0], a[1], a[2], ..., a[n-2]]`.

Given the sorted rotated array `nums` of **unique** elements, return *the minimum element of this array*.

You must write an algorithm that runs in `O(log n) time`.

 

**Example 1:**

```
Input: nums = [3,4,5,1,2]
Output: 1
Explanation: The original array was [1,2,3,4,5] rotated 3 times.
```

**Example 2:**

```
Input: nums = [4,5,6,7,0,1,2]
Output: 0
Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
```

**Example 3:**

```
Input: nums = [11,13,15,17]
Output: 11
Explanation: The original array was [11,13,15,17] and it was rotated 4 times. 
```

 

**Constraints:**

- `n == nums.length`
- `1 <= n <= 5000`
- `-5000 <= nums[i] <= 5000`
- All the integers of `nums` are **unique**.
- `nums` is sorted and rotated between `1` and `n` times.



## 2. 最佳思路

- Since all the numbers are unique, we could observe that, the array can be splitted into too parts, the left part which has every element larger than `nums[0]`. The right part, starting from the min element, to the end, each element would less than `nums[0]`.
- We could use binary search to find the right-most position which satisfies `nums[pos] >= nums[0]`


## 3. 最佳代码

```java
class Solution {
    public int findMin(int[] nums) {
        int n = nums.length;
        if (n == 1 || nums[0] < nums[n - 1]) {
            return nums[0];
        }
        int low = 0, high = n - 1, maxIdx = 0;
        while (low <= high) {
            // find max pos s.t. nums[pos] >= nums[0]
            int mid = (low + high) >>> 1;
            if (nums[mid] >= nums[0]) {
                maxIdx = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return nums [maxIdx + 1];
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(log n)
  
- **空间复杂度**：O(1)

