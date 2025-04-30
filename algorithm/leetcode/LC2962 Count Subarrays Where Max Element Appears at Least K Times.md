
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[2962. 统计最大元素出现至少 K 次的子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/count-subarrays-where-max-element-appears-at-least-k-times/description/?envType=daily-question&envId=2025-04-29)

You are given an integer array `nums` and a **positive** integer `k`.

Return *the number of subarrays where the **maximum** element of* `nums` *appears **at least*** `k` *times in that subarray.*

A **subarray** is a contiguous sequence of elements within an array.



**Example 1:**

```
Input: nums = [1,3,2,3,3], k = 2
Output: 6
Explanation: The subarrays that contain the element 3 at least 2 times are: [1,3,2,3], [1,3,2,3,3], [3,2,3], [3,2,3,3], [2,3,3] and [3,3].
```

**Example 2:**

```
Input: nums = [1,4,2,1], k = 3
Output: 0
Explanation: No subarray contains the element 4 at least 3 times.
```



**Constraints:**

- `1 <= nums.length <= 10^5`
- `1 <= nums[i] <= 10^6`
- `1 <= k <= 10^5`



note：题目有点难懂，最大值是整个数组的最大值，而不是每个子数组的最大值。



## 2. 思路

native solution：

- 找出最大值，O(n). 枚举每个子数组，O(n^2). 判断子树组是否满足要求（最大值的个数 >= k个），总的时间复杂度是：O(n^2).

更好的做法：

- O(n^2) 会超时，更低的复杂度只能是 O(n), 或 O(nlog n). 这题排序没用，所以只能往 O(n)的方向找
- 滑动窗口算法，如何用？找到所有的最大值的位置，放到 List，然后对这个 List 做固定大小的滑动窗口算法。答案是：固定左边界或右边界后得到的子树组的个数，利用乘积法则。




## 3. 最佳代码

```java
class Solution {
    public long countSubarrays(int[] nums, int k) { // time: O(n), space: O(n)
        int n = nums.length;

        // 1. find max
        int max = nums[0];
        for (int i = 1; i < n; i++) {
            max = Math.max(max, nums[i]);
        }

        // 2. find max pos array
        List<Integer> posList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (nums[i] == max) {
                posList.add(i);
            }
        }

        // 3. sliding window (fix size)
        long ans = 0l;
        for (int right = k - 1; right < posList.size(); right++) {
            int left = right - k + 1; // [left, right] contains k elements
            int leftPos = posList.get(left);
            int rightPos = posList.get(right);
            // [leftPos, rightPos] contains k max. total subarray count? 需要不重不漏地枚举所有子树组
            // 枚举方式：所有子数组以 nums[leftPos] 作为第一个最大元素
            // 或：所有子数组以 nums[rightPos] 作为最后一个最大元素
            int leftCount = left > 0 ? leftPos - posList.get(left - 1): leftPos + 1;
            int rightCount = n - rightPos;
            ans += (long) leftCount * rightCount;
        }
        return ans;
    }
}
```

## 4. 其他解法

### 4.1 解法2

直接在原始数组上用滑动窗口。有点难理解这个答案的更新。

```java
class Solution {
    public long countSubarrays(int[] nums, int k) {
        int n = nums.length;
        int max = Arrays.stream(nums).max().getAsInt();
        int count = 0;
        long ans = 0l;
        for (int i = 0, j = 0; j < n; j++) {
            // add j to window
            if (nums[j] == max) {
                count++;
            }
            // move i until window not met?
            while (count == k) {
                if (nums[i] == max) {
                    count--;
                }
                i++;
            }
            // 右端点为 j 且左端点小于 i 的子数组，都包含至少 k 个 max，因此我们可以把答案增加 i
            // when i > 0, it means the window met condition and move i to break the condition
            ans += i;
        }
        return ans;
    }
}
```



