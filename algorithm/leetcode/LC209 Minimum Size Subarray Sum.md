
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[209. 长度最小的子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/minimum-size-subarray-sum/description/)

Given an array of positive integers `nums` and a positive integer `target`, return *the **minimal length** of a* *subarray* *whose sum is greater than or equal to* `target`. If there is no such subarray, return `0` instead.


**Example 1:**

```
Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: The subarray [4,3] has the minimal length under the problem constraint.
```

**Example 2:**

```
Input: target = 4, nums = [1,4,4]
Output: 1
```

**Example 3:**

```
Input: target = 11, nums = [1,1,1,1,1,1,1,1]
Output: 0
```

 

**Constraints:**

- `1 <= target <= 10^9`
- `1 <= nums.length <= 10^5`
- `1 <= nums[i] <= 10^4`

 

**Follow up:** If you have figured out the `O(n)` solution, try coding another solution of which the time complexity is `O(n log(n))`.



## 2. 写法1 - 枚举右边界

- `—----—–i——----——j——---—` 对于每个 j，可以从左边找到离它最近的 i，使得`sum(nums[i,j]) >= target`. 当右移 j 时，i 必然跟着右移或者保持不动，绝对不会左移，因此 i 是由 j 牵引着往前走的。
- 窗口包含的元素是 `[i, j]`，利用 curSum 维护窗口中元素的和，移动 j，`curSum += nums[j];` 移动i， `curSum -= nums[i];`

```java
class Solution {
    public int minSubArrayLen(int target, int[] nums) { // time: O(n)
        int n = nums.length;
        int ans = Integer.MAX_VALUE, curSum = 0;
        for (int i = 0, j = 0; j < n; j++) {
            // consider window [i,j], add j to the window
            curSum += nums[j];

            // move i until condition not met
            while (curSum >= target) {
                // update answer
                ans = Math.min(ans, j - i + 1);
                curSum -= nums[i++];
            }
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }
}
```







## 3. 写法2 - 枚举左边界

思路：

- `—----—–i——----——j——---—` 对于每个 i，可以从右边找到离它最近的 j，使得`sum(nums[i,j]) >= target`. 当右移 i 时，j 必然跟着右移或者保持不动，绝对不会左移，因此 j 是由 i 驱动往前走的。
- 窗口包含的元素是 `[i, j)`，利用 curSum 维护窗口中元素的和，移动 j，`curSum += nums[j];` 移动i， `curSum -= nums[i];`

```java
class Solution {
    public int minSubArrayLen(int target, int[] nums) {// time: O(n)
        int n = nums.length;
        int ans = Integer.MAX_VALUE, curSum = 0;
        for (int i = 0, j = 0; i < n; i++) {
            // nums[j] is not included in the curSum
            while (j < n && curSum < target) {
                curSum += nums[j++];
            }

            if (curSum >= target) {
                ans = Math.min(ans, j - i); // window: [i, j), because nums[j] is not included in the curSum.
            }

            // move i forward
            curSum -= nums[i];
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }
}
```



