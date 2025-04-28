
该题掌握程度：
- #熟练✓

考点：滑动窗口算法。

## 1. 题目
题目链接：[713. 乘积小于 K 的子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/subarray-product-less-than-k/description/)

Given an array of integers `nums` and an integer `k`, return *the number of contiguous subarrays where the product of all the elements in the subarray is strictly less than* `k`.
 

**Example 1:**

```
Input: nums = [10,5,2,6], k = 100
Output: 8
Explanation: The 8 subarrays that have product less than 100 are:
[10], [5], [2], [6], [10, 5], [5, 2], [2, 6], [5, 2, 6]
Note that [10, 5, 2] is not included as the product of 100 is not strictly less than k.
```

**Example 2:**

```
Input: nums = [1,2,3], k = 0
Output: 0
```

 

**Constraints:**

- `1 <= nums.length <= 3 * 10^4`
- `1 <= nums[i] <= 1000`
- `0 <= k <= 10^6`

## 2. 思路

```python
native solution:
- enumerate each sub-array, and calculate the product, compare with k, count one if less than k.
- time: O(n^2)


We must find a better solution, how to reduce it to O(n)? sliding window
all numbers are positive. 
----------i-----------j-----------
                      |
for each j, find left-most i which subjects to product(nums[i...j]) < k.
when move j forward, i must stay around or move forward. So we can move j around, and i will move along with it.
```
## 3. 最佳代码

```java
class Solution {
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int n = nums.length;
        long product = 1L;
        int count = 0;
        for (int i = 0, j = 0; j < n; j++) {
            // add j to the window
            product *= nums[j];

            while (i <= j && product >= k) { // NOTE: take care of i <= j, because k might be 0.
                product /= nums[i++];
            }

            // add answer, window: [i, j]
            count += (j - i + 1); // count subarray ends with nums[j].
        }
        return count;
    }
}
```

- **时间复杂度**：O(n)
- **空间复杂度**：O(1)

## Related Problems

- [152. 乘积最大子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/maximum-product-subarray/description/), 笔记：[LC152 Maximum Product Subarray](LC152%20Maximum%20Product%20Subarray.md)  - 不是特别相关，无法使用滑动窗口算法。LC713 的数组都是正数，所以可以用滑动窗口，LC152 包含负数，所以不能使用滑动窗口来做，因为如果都是正数的话，那么 LC152 就直接返回所有数的乘积即可。