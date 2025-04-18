## 题目
链接： [523. 连续的子数组和 - 力扣（LeetCode）](https://leetcode.cn/problems/continuous-subarray-sum/)

```
Given an integer array nums and an integer k, return true if nums has a good subarray or false otherwise.

A good subarray is a subarray where:

its length is at least two, and
the sum of the elements of the subarray is a multiple of k.
Note that:

A subarray is a contiguous part of the array.
An integer x is a multiple of k if there exists an integer n such that x = n * k. 0 is always a multiple of k.
 

Example 1:
	Input: nums = [23,2,4,6,7], k = 6
	Output: true
	Explanation: [2, 4] is a continuous subarray of size 2 whose elements sum up to 6.

Example 2:
	Input: nums = [23,2,6,4,7], k = 6
	Output: true
	Explanation: [23, 2, 6, 4, 7] is an continuous subarray of size 5 whose elements sum up to 42.
	42 is a multiple of 6 because 42 = 7 * 6 and 7 is an integer.

Example 3:
	Input: nums = [23,2,6,4,7], k = 13
	Output: false
 

Constraints:
	1 <= nums.length <= 10^5
	0 <= nums[i] <= 10^9
	0 <= sum(nums[i]) <= 2^31 - 1
	1 <= k <= 2^31 - 1
```

- 非常好的题目，很巧妙的解法。

## 思路

- 直接暴力解法，需要遍历每个子数组，然后求和，那么是 O(n^3), 如果利用前缀和优化，可以降到 O(n^2)，依然会超时。
- 前缀和优化，两个数模 k 的得到余数相同，然后相减就能被 k 整除。

tag: #前缀和 #取模 #hash表 

## 前缀和 + Hash 表
### 写法 1 - 使用 HashMap

```java
class Solution {
    public boolean checkSubarraySum(int[] nums, int k) {  // time: O(n), space: O(n)
        int n = nums.length;
        int[] accSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            accSum[i] = accSum[i - 1] + nums[i - 1];
        }

        // 记录每个前缀和模 k的余数出现的下标        
        Map<Integer, Integer> indexMap = new HashMap<>();
        for(int i = 0; i <= n; i++) {
            int t = accSum[i] % k;
            if (indexMap.containsKey(t)) {
                // 说明找到连续的 2 个以上的数,他们的前缀和有相同是的余数,因此这一段的和,可以被 k 整除
                if (i - indexMap.get(t) > 1) {
                    return true;
                }
            } else {
                indexMap.put(t, i); // 只记录最先出现的余数所在的位置
            }            
        }
        return false;
    }
}
```

### 写法 2 - 使用 HashSet

非常巧妙的写法。这个 hash 表的利用，有点类似于 TwoSum 里的 hash 表。
```java
class Solution {
    public boolean checkSubarraySum(int[] nums, int k) { // time: O(n), space: O(n)
        int n = nums.length;
        int[] accSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            accSum[i] = accSum[i - 1] + nums[i - 1];
        }
              
        Set<Integer> hash = new HashSet<>();
        for(int i = 2; i <= n; i++) {
            hash.add(accSum[i - 2] % k);
            if (hash.contains(accSum[i] % k)) {
                return true;
            }
        }
        return false;
    }
}
```


## 暴力解法 - 前缀和 - TLE

```java
class Solution {
    public boolean checkSubarraySum(int[] nums, int k) {        
        int n = nums.length;
        int[] accSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            accSum[i] = accSum[i - 1] + nums[i - 1];
        }

        // enumerate each subarray
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int curSum = accSum[j + 1] - accSum[i];
                if (curSum % k == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
```
