
该题掌握程度：
- #熟练✓

考察的知识点：

- 前缀和；滑动窗口算法

## 1. 题目

题目链接：[2302. 统计得分小于 K 的子数组数目 - 力扣（LeetCode）](https://leetcode.cn/problems/count-subarrays-with-score-less-than-k/description/?envType=daily-question&envId=2025-04-28)

The **score** of an array is defined as the **product** of its sum and its length.

- For example, the score of `[1, 2, 3, 4, 5]` is `(1 + 2 + 3 + 4 + 5) * 5 = 75`.

Given a positive integer array `nums` and an integer `k`, return *the **number of non-empty subarrays** of* `nums` *whose score is **strictly less** than* `k`.

A **subarray** is a contiguous sequence of elements within an array.

 

**Example 1:**

```
Input: nums = [2,1,4,3,5], k = 10
Output: 6
Explanation:
The 6 subarrays having scores less than 10 are:
- [2] with score 2 * 1 = 2.
- [1] with score 1 * 1 = 1.
- [4] with score 4 * 1 = 4.
- [3] with score 3 * 1 = 3. 
- [5] with score 5 * 1 = 5.
- [2,1] with score (2 + 1) * 2 = 6.
Note that subarrays such as [1,4] and [4,3,5] are not considered because their scores are 10 and 36 respectively, while we need scores strictly less than 10.
```

**Example 2:**

```
Input: nums = [1,1,1], k = 5
Output: 5
Explanation:
Every subarray except [1,1,1] has a score less than 5.
[1,1,1] has a score (1 + 1 + 1) * 3 = 9, which is greater than 5.
Thus, there are 5 subarrays having scores less than 5.
```

 

**Constraints:**

- `1 <= nums.length <= 10^5`
- `1 <= nums[i] <= 10^5`
- `1 <= k <= 10^15`



## 2. 思路

### 暴力做法 / naive solution

1. 枚举每一个子数组（通过枚举区间起点和终点），计算子数组的sum 然后乘以子数组长度，得到 product。如果利用前缀和数组进行优化，那么时间复杂度是 O(n^2).
2. 根据数据范围，n 是10^5, 所以这种解法会超时。所以更优的解法应该是 O(n) 或者 O(nlogn). 因为这题是关于子数组的，对数组排序没有任何作用。所以重点还是思考 O(n)的解法。

### 滑动窗口

1. 为什么可以用滑动窗口呢？i, j的移动具有单调性，只能往前不能往后移动。

```
why we can use sliding window in this problem?
    ----------i------------j-----------
                           |
for each j, we can find a left-most i before it, to satisify sum(a[i..j]) * (j - i + 1) < k
    so when j move forward, i also needs to move forward. That means i moves along with j, and i can't move back.
```

2. 怎么实现呢？

计算累计和，很好写，移动 i、j 时，对 sum 加减一下即可，但是注意数据的范围，要使用 long 来保存 sum；
计算满足条件的子数组的个数？枚举以 j 结尾的子树组的个数是: j - i + 1. 累加起来就是答案。




## 3. 最佳代码

```java
class Solution {
    public long countSubarrays(int[] nums, long k) {
        int n = nums.length;
        long sum = 0, count = 0;
        for (int i = 0, j = 0; j < n; j++) {
            // add j to the window
            sum += nums[j];

            while (sum * (j - i + 1) >= k) {
                sum -= nums[i++];                
            }
            // add answer for [i, j].  how many sub-array? sub-array ends with nums[j]: j - i +1
            count = count + (j - i + 1);
        }
        return count;
    }
}
```

- **时间复杂度**：O(n）
- **空间复杂度**：O(1)


## 5. 相关联的题目

- [53. 最大子数组和 - 力扣（LeetCode）](https://leetcode.cn/problems/maximum-subarray/description/)
- [713. 乘积小于 K 的子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/subarray-product-less-than-k/description/) ，笔记：[LC713 Subarray Product Less Than K](LC713%20Subarray%20Product%20Less%20Than%20K.md)
- [930. 和相同的二元子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/binary-subarrays-with-sum/)





