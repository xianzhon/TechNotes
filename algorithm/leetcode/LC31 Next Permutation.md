
该题掌握程度：
- #熟练✓
- #一般⭕️

Tag: #全排列

## 1. 题目
题目链接：[31. 下一个排列 - 力扣（LeetCode）](https://leetcode.cn/problems/next-permutation/description/)
```
A permutation of an array of integers is an arrangement of its members into a sequence or linear order.

For example, for arr = [1,2,3], the following are all the permutations of arr: [1,2,3], [1,3,2], [2, 1, 3], [2, 3, 1], [3,1,2], [3,2,1].
The next permutation of an array of integers is the next lexicographically greater permutation of its integer. More formally, if all the permutations of the array are sorted in one container according to their lexicographical order, then the next permutation of that array is the permutation that follows it in the sorted container. If such arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).

For example, the next permutation of arr = [1,2,3] is [1,3,2].
Similarly, the next permutation of arr = [2,3,1] is [3,1,2].
While the next permutation of arr = [3,2,1] is [1,2,3] because [3,2,1] does not have a lexicographical larger rearrangement.
Given an array of integers nums, find the next permutation of nums.

The replacement must be in place and use only constant extra memory.
 

Example 1:
	Input: nums = [1,2,3]
	Output: [1,3,2]
	
Example 2:
	Input: nums = [3,2,1]
	Output: [1,2,3]

Example 3:	
	Input: nums = [1,1,5]
	Output: [1,5,1]
		
Constraints:	
	1 <= nums.length <= 100
	0 <= nums[i] <= 100
```
## 2. 最佳思路

- 我们希望下一个数 **比当前数大**，这样才满足 “下一个排列” 的定义。因此只需要 将后面的「大数」与前面的「小数」交换，就能得到一个更大的数。比如 `123456`，将 5 和 6 交换就能得到一个更大的数 `123465`。
- 我们还希望下一个数 **增加的幅度尽可能的小**，这样才满足“下一个排列与当前排列紧邻“的要求。为了满足这个要求，我们需要：
	- 在 **尽可能靠右的低位** 进行交换，需要 **从后向前** 查找 （逆序对）
	- 将一个 尽可能小的「大数」 与前面的「小数」交换。比如 123465，下一个排列应该把 5 和 4 交换而不是把 6 和 4 交换
	- 将「大数」换到前面后，需要将「大数」后面的所有数 重置为升序，升序排列就是最小的排列。以 123465 为例：首先按照上一步，交换 5 和 4，得到 123564；然后需要将 5 之后的数重置为升序，得到 123546。显然 123546 比 123564 更小，123546 就是 123465 的下一个排列

参考链接： https://leetcode.cn/problems/next-permutation/solutions/80560/xia-yi-ge-pai-lie-suan-fa-xiang-jie-si-lu-tui-dao-/ 

## 3. 最佳代码

```java
class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        // 1. find reverse pair from right to left: nums[i] > nums[i - 1]
        // only when we swap a large number to i - 1, we get a bigger permutation
        int i = n - 1;
        for(; i > 0 && nums[i] <= nums[i-1]; i--) {}
        if (i == 0) { // case: [3,2,1], no reverse pair
            reverse(nums, 0, n - 1);
            return;
        }

        // 2. find swap pos, just find a bigger number to replace nums[i-1]
        int j = i;
        for(; j < n && nums[j] > nums[i - 1]; j++) {}
        swap(nums, i - 1, j - 1);

        // 3. reverse
        reverse(nums, i, n - 1);
    }

    void reverse(int[] nums, int i, int j) {
        for(; i < j; i++, j--) {
            swap(nums, i, j);
        }
    }

    void swap(int[] nums, int i, int j) {
        int t = nums[i]; nums[i] = nums[j]; nums[j] = t;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n)

- **空间复杂度**：O(1)
  
### 3.2 特别注意

- 算法思路：mid
- 实现细节：mid
- 算法证明：easy
- 时空复杂度分析：easy

## 4. 相关联的题目


## 5. 可能的 follow-up


