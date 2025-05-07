
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[416. 分割等和子集 - 力扣（LeetCode）](https://leetcode.cn/problems/partition-equal-subset-sum/description/)

Given an integer array `nums`, return `true` *if you can partition the array into two subsets such that the sum of the elements in both subsets is equal or* `false` *otherwise*.



**Example 1:**

```
Input: nums = [1,5,11,5]
Output: true
Explanation: The array can be partitioned as [1, 5, 5] and [11].
```

**Example 2:**

```
Input: nums = [1,2,3,5]
Output: false
Explanation: The array cannot be partitioned into equal sum subsets.
```



**Constraints:**

- `1 <= nums.length <= 200`
- `1 <= nums[i] <= 100`



## 2. 思路

native idea - 错误:
```
因为本题不是求子数组，那么可以对数组排序，然后两个指针一个从前往后走，一个从后往前走，累加和。如果相遇了且各自的累加和相等，那么就找到这样的 partition 了。
但是这个思路不对。例如：[1,2,4,6,7] => 答案是 [1,2,7] [4,6]. 但是按照排序，则无解。
```

DP 的思路：
```
- Just needs to find a subset elements, whose sum equals to sum(nums)/2.
- if the sum is odd or element number is 1, just return false. If the maxNum > sum/2, then it means no parition too.
- enumerate each combinations of subsum. (sort it first?) sliding window? knapsack?
- Transform it to a knapsack problems: 0-1 knapsack
- time: O(2^n)

0-1背包问题，n 件物品，每个物品有两种选择，装或者不装到背包里，背包容量 C，要考虑每个物品的重量限制，求最大的价值。
这个题是问是否能恰好装满背包，不求最大价值。

- 状态表示，dp[i][j]: 表示前 i 件物品，是否能恰好装满容量为 j 的背包
- 递推公式：考虑最后一步，装 i 或不装 i。只要其中一个是 true，dp[i][j] 就是 true
	dp[i][j] = dp[i-1][j]             不装i
			 = dp[i-1][j - nums[i]]   装 i
- base: dp[i][0] = true i >= 0 恰好能装满容量是 0 的背包
- answer: dp[n][sum/2]
```




## 3. 最佳代码

```java
class Solution {
    public boolean canPartition(int[] nums) { // time: O(2^n), space: O(n * sum)
        int n = nums.length;
        int sum = Arrays.stream(nums).sum();
        int maxNum = Arrays.stream(nums).max().getAsInt();

        if (sum % 2 != 0 || n <= 1 || maxNum > sum / 2) { // no solution
            return false;
        }
        int capacity = sum / 2;
        boolean[][] dp = new boolean[n][capacity + 1];

        // 初始化
        for (int i = 0; i < n; i++) dp[i][0] = true;
        dp[0][nums[0]] = true; // note: if nums[0] > capacity, then error! So we need to check maxNum.

        // 递推
        for (int i = 1; i < n; i++) { // enumerate each goods
            // enumerate each capacity
            for (int j = 1; j <= capacity; j++) {
                if (j < nums[i])  dp[i][j] = dp[i - 1][j];
                else dp[i][j] = dp[i - 1][j] || dp[i-1][j - nums[i]];
            }
        }
        return dp[n - 1][capacity];
    }
}
```


优化写法 2： 多开一行，然后只初始化 `dp[0][0]`
```java
class Solution {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        // find sum of all array elements
        for (int num : nums) {
            totalSum += num;
        }
        // if totalSum is odd, it cannot be partitioned into equal sum subset
        if (totalSum % 2 != 0) return false;
        int subSetSum = totalSum / 2;
        int n = nums.length;
        boolean dp[][] = new boolean[n + 1][subSetSum + 1];
        dp[0][0] = true;
        for (int i = 1; i <= n; i++) {
            int curr = nums[i - 1];
            for (int j = 0; j <= subSetSum; j++) {
                if (j < curr)
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = dp[i - 1][j] || (dp[i - 1][j - curr]);
            }
        }
        return dp[n][subSetSum];
    }
}
```

## 暴力解法 - dfs
这是一个回溯的问题。
```java
class Solution {
    public boolean canPartition(int[] nums) { // time: O(2^n), space: O(n)
        int totalSum = 0;
        // find sum of all array elements
        for (int num : nums) {
            totalSum += num;
        }
        // if totalSum is odd,it cannot be partitioned into equal sum subset
        if (totalSum % 2 != 0) return false;
        int subSetSum = totalSum / 2;
        int n = nums.length;
        return dfs(nums, n - 1, subSetSum);
    }

    public boolean dfs(int[] nums, int n, int subSetSum) {
        // Base Cases
        if (subSetSum == 0)
            return true;
        if (n == 0 || subSetSum < 0)
            return false;
        //     two options:        select num[n-1]         not select nums[n-1]
        return dfs(nums, n - 1, subSetSum - nums[n - 1]) || dfs(nums, n - 1, subSetSum);
    }
}
```