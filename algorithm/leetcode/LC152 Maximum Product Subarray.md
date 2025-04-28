
该题掌握程度：
- #一般⭕️

考点：一维 DP
## 1. 题目
题目链接：[152. 乘积最大子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/maximum-product-subarray/description/)

Given an integer array `nums`, find a subarray that has the largest product, and return *the product*.

The test cases are generated so that the answer will fit in a **32-bit** integer.

 

**Example 1:**

```
Input: nums = [2,3,-2,4]
Output: 6
Explanation: [2,3] has the largest product 6.
```

**Example 2:**

```
Input: nums = [-2,0,-1]
Output: 0
Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
```

 

**Constraints:**

- `1 <= nums.length <= 2 * 10^4`
- `-10 <= nums[i] <= 10`
- The product of any subarray of `nums` is **guaranteed** to fit in a **32-bit** integer.



## 2. 最佳思路

```java
native solution:
- enumerate each sub-array, and calculate the product, record the max value
- time: O(n^2) with optimize logic of calculating product while enumerating the endpoint

Better solution: 
1. sliding window? No. there is no increasing condition of i moves along with j.
-----------i---------j---------                     
                     |
2. dp?
dp[i]: max product ends with nums[i].
    dp[i] = max (dp[i-1] * nums[i], nums[i])
dp[0] = nums[0]
ans = max{dp[i]}
No, this will miss the case: [-1, 2, -3].

3. dp with two state array
maxVal[i]: 以nums[i]结尾的最大的乘积子数组
minVal[i]: 以nums[i]结尾的最小的乘积子数组 (可能是负数)

formula:
	maxVal[i] = max (nums[i], nums[i] * maxVal[i-1], nums[i] * minVal[i-1]);
	minVal[i] = min (nums[i], nums[i] * maxVal[i-1], nums[i] * minVal[i-1]);
init: maxVal[0] = minVal[0] = nums[0]
answer: max {maxVal[i]}, i = 0, .. n-1
```

## 3. 最佳代码

```java
class Solution {
    public int maxProduct(int[] A) {
        int n = A.length;
        if (n == 0) {
            return 0;
        }
        int[] maxVal = new int[n]; // 以A[i]结尾的最大的乘积子数组
        int[] minVal = new int[n]; // 以A[i]结尾的最小的乘积子数组 (可能是负数)
        maxVal[0] = minVal[0] = A[0];

        int ans = A[0];
        for (int i = 1; i < n; i++) {
            int cur = A[i];
            int curMax = cur * maxVal[i - 1];
            int curMin = cur * minVal[i - 1];
            maxVal[i] = Math.max(cur, Math.max(curMax, curMin));
            minVal[i] = Math.min(cur, Math.min(curMax, curMin));

            ans = Math.max(ans, maxVal[i]);
        }
        return ans;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
