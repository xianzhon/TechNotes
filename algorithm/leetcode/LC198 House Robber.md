
该题掌握程度：
- #熟练✓

Tag: #dp

## 1. 题目
题目链接：[198. 打家劫舍 - 力扣（LeetCode）](https://leetcode.cn/problems/house-robber/description/)

You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and **it will automatically contact the police if two adjacent houses were broken into on the same night**.

Given an integer array `nums` representing the amount of money of each house, return *the maximum amount of money you can rob tonight **without alerting the police***.

 

**Example 1:**

```
Input: nums = [1,2,3,1]
Output: 4
Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
Total amount you can rob = 1 + 3 = 4.
```

**Example 2:**

```
Input: nums = [2,7,9,3,1]
Output: 12
Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
Total amount you can rob = 2 + 9 + 1 = 12.
```

 

**Constraints:**

- `1 <= nums.length <= 100`
- `0 <= nums[i] <= 400`

## 2. 最佳思路

Since we need to calculate the maximum amount of the money, it’s natually to think about DP. We just need to think about:

- what does the `dp[i]` mean, and the formular
- the initial value and the answer

```
dp[i]: rob first i houses to get the max amount
dp[0] = nums[0]
dp[1] = max(nums[0], nums[1])
dp[i] = max(dp[i-1], dp[i-2]+nums[i])   <=== do not rob house[i], or rob house[i]
ans = dp[n-1]
```




## 3. 最佳代码

```java
    public int rob(int[] nums) { // time: O(n), space: O(n)
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i-1], dp[i - 2] + nums[i]);
        }
        return dp[n-1];
    }
```

## 4. Variants

### Rob II
- 题目：[213. 打家劫舍 II - 力扣（LeetCode）](https://leetcode.cn/problems/house-robber-ii/)
- 要点 All houses at this place are **arranged in a circle.**  第 1 家和第 n 家连起来了，围成了一个圈。不能同时抢第 1 家和最后一家。

思路：
1. 能否转化成问题 1 来做？ 对第 1 家和最后一家分开考虑。房子编号: 0~n-1. rob house 0 时，必然不能 rob house n - 1. 实际上是要求rob `house[0 ~ n-2]` 的最大值，同理，rob house n - 1时，是求rob `house[1, n - 1]`的最大值

```java
class Solution {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return nums[0];            
        }
        int ans1 = rob(nums, 0, n - 2);
        int ans2 = rob(nums, 1, n - 1);
        return Math.max(ans1, ans2);
    }

    int rob(int[] nums, int low, int high) {
        int n = high - low + 1;
        if (n == 1) {
            return nums[low];
        }
        int[] dp = new int[n];    
        dp[0] = nums[low];
        dp[1] = Math.max(nums[low], nums[low + 1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[low + i]);
        }
        return dp[n - 1];
    }
}
```

### Rob III
- 题目： [337. 打家劫舍 III - 力扣（LeetCode）](https://leetcode.cn/problems/house-robber-iii/)
- 笔记：[LC337 House Robber III](LC337%20House%20Robber%20III.md)