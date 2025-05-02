
该题掌握程度：
- #熟练✓
## 1. 题目
题目链接：[280. 摆动排序 - 力扣（LeetCode）](https://leetcode.cn/problems/wiggle-sort/description/)

Given an integer array `nums`, reorder it such that `nums[0] <= nums[1] >= nums[2] <= nums[3]...`.

You may assume the input array always has a valid answer.

**Example 1:**

```
Input: nums = [3,5,2,1,6,4]
Output: [3,5,1,6,2,4]
Explanation: [1,6,2,5,3,4] is also accepted.
```

**Example 2:**

```
Input: nums = [6,6,5,6,3,8]
Output: [6,6,5,6,3,8]
```



**Constraints:**

- `1 <= nums.length <= 5 * 104`
- `0 <= nums[i] <= 104`
- It is guaranteed that there will be an answer for the given input `nums`.



**Follow up:** Could you solve the problem in `O(n)` time complexity?



## 2. 思路

- nature solution: 观察发现需要排序之后的数组满足条件，奇数下标的值要大于左右两个相邻的元素。那么我们可以先整体排序，然后从后往前每次取一个大的元素，插入到前面的为止，以满足wiggle排序要求。这个时间复杂度是 O(nlogn)
- 更好的做法是：贪心。先拿一个例子，观察一下，如果不满足条件，交换两个数，看看有什么规律。比如拿前 3 个数，a,b,c来举例，要求 b >= a && b <= c. 那么如果 a，b 不满足，即a > b. 那么交换 a、b 的值即可。然后如果 b、c 不满足，交换 b、c。然后发现，这时候 a、b 的值不受后面的元素的影响了，即使 c > d，导致 c、d 交换，但是 d 依然是 < b，所以 a、b 已经确定了。因此，可以每两个元素为一组一起考虑。

```java
[3,5,2,1,6,4]  => 第一种情况， 3 < 5 && 5 > 2 所以前 3 个元素都满足，但是这一次只能确定 3 5 不会再交换了，所以 i += 2
 i j k
[3,5,2,1,6,4]   => 这一次 2 > 1 不满足，要交换。同理，交换后的 2 和 6 也不满足，所以要交换 2 和 6
     i j k
[3,5,1,6,2,4]  交换后1,6满足了，不需要动
     i j k
[3,5,1,6,2,4]  2,4 满足条件，不需要交换， i += 2 后结束
         i j k
```



## 3. 最佳代码

```java
class Solution {
    public void wiggleSort(int[] nums) {
        int n = nums.length;
        if (n == 1) return;
        for (int i = 0; i < n - 1; i += 2) {
            if (nums[i] > nums[i + 1]) {
                swap(nums, i, i + 1);
            }
            if (i + 2 < n && nums[i + 1] < nums[i + 2]) {
                swap(nums, i + 1, i + 2);
            }
        }
    }

    void swap(int[] nums, int i, int j) { int t = nums[i]; nums[i] = nums[j]; nums[j] = t; }
}
```

- **时间复杂度**：O(n)

- **空间复杂度**：O(1)



## 总结

学习到的是如何用具体的例子去分析，然后找到解决方案的过程。注意这题中相邻的三个数，如果相等也是满足条件的，否则的话，就变成 题目 [324. 摆动排序 II - 力扣（LeetCode）](https://leetcode.cn/problems/wiggle-sort-ii/description/) 了，那个更难构造一点。



