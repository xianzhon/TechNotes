
该题掌握程度：
- #很差❌

## 1. 题目
题目链接：[548. 将数组分割成和相等的子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/split-array-with-equal-sum/)

Given an integer array `nums` of length `n`, return `true` if there is a triplet `(i, j, k)` which satisfies the following conditions:

- `0 < i, i + 1 < j, j + 1 < k < n - 1`
- The sum of subarrays `(0, i - 1)`, `(i + 1, j - 1)`, `(j + 1, k - 1)` and `(k + 1, n - 1)` is equal.

A subarray `(l, r)` represents a slice of the original array starting from the element indexed `l` to the element indexed `r`.

**Example 1:**

```
Input: nums = [1,2,1,2,1,2,1]
Output: true
Explanation:
i = 1, j = 3, k = 5.
sum(0, i - 1) = sum(0, 0) = 1
sum(i + 1, j - 1) = sum(2, 2) = 1
sum(j + 1, k - 1) = sum(4, 4) = 1
sum(k + 1, n - 1) = sum(6, 6) = 1
```

**Example 2:**

```
Input: nums = [1,2,1,2,1,2,1,2]
Output: false
```

**Constraints:**

- `n == nums.length`
- `1 <= n <= 2000`
- `-106 <= nums[i] <= 10^6`

Note: 题目很绕，比较难理解，大致意思就是给你一个数组，然后从中间选择 3 个元素，去掉他们，从而将原始的数组分成了 4 个子数组。现在要求划分后的每个子数组的和都一样，找出是否存在这样的划分方法。

## 2. 思路

- 从数组中删除 3 个元素，还要保证有 4 个子数组，因此总的数组元素最少是 7 个。所以，如果数组小于 7，直接返回 false 即可。
- native solution：直接枚举3 个元素的位置，然后利用前缀和，求每个子数组，看是否满足条件。这个时间复杂度是：O(n^3)，该题会超时。
- 更好的做法，是怎么优化复杂度。思考这 3 个元素的依赖关系，当 j 固定之后，i 和 k 的枚举范围就缩小了将近“一半”，它们两个合到一起才需要枚举 O(n)。所以可以用这点进行优化。
	- 我们可以把左边两个子数组枚举出来（固定 j 的情况下，枚举 i），找出和相等的划分方案，然后在右边枚举的时候，判断是否有这样的和出现即可。

Good analysis:  https://leetcode.cn/problems/split-array-with-equal-sum/solutions/1/python-ha-xi-biao-qian-zhui-he-by-yi-zhi-ednt

## 3. 最佳代码 - 前缀和 + 哈希表

```java
class Solution {
    public boolean splitArray(int[] A) { // time: O(n^2)
        int n = A.length;
        if (n < 7) return false;
        int[] pre = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            pre[i] = pre[i - 1] + A[i - 1];
        }
        // sum(A[i..j]) = pre[j + 1] - pre[j]
        // enumerate each j
        for (int j = 3; j < n - 3; j++) {
            // left: [0, j - 1], right: [j + 1, n - 1]
            Set<Integer> leftSums = new HashSet<>();
            for (int i = 1; i < j - 1; i++) {
                // two parts, [0, i -1], [i+1, j - 1]
                int sum1 = pre[i], sum2 = pre[j] - pre[i+1];
                if (sum1 == sum2) {
                    leftSums.add(sum1);
                }
            }
            for (int k = j + 2; k < n - 1; k++) {
                // two parts, [j+1, k-1], [k+1, n-1]
                int sum1 = pre[k] - pre[j+1], sum2 = pre[n] - pre[k+1];
                if (sum1 == sum2 && leftSums.contains(sum1)) {
                    return true;
                }
            }
        }
        return false;
    }
}

/*
[1,2,1,2,1,2,1]
remove 3 elem, it becomes 4 parts, and all sum equals.
prefix sum?

- n < 7 false
- i: 0 < i < n - 5
- j: i + 1 < j < n - 3
- k: j + 1 < k < n - 1

1. preSum
2. enumerate j, then find i,k in each part.
    hash to store the possible sum in left part
*/
```
- **时间复杂度**：O(n^2)
- **空间复杂度**：O(n)