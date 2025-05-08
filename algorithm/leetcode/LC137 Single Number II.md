
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[137. 只出现一次的数字 II - 力扣（LeetCode）](https://leetcode.cn/problems/single-number-ii/description/)

Given an integer array `nums` where every element appears **three times** except for one, which appears **exactly once**. *Find the single element and return it*.

You must implement a solution with a linear runtime complexity and use only constant extra space.

 

**Example 1:**

```
Input: nums = [2,2,3,2]
Output: 3
```

**Example 2:**

```
Input: nums = [0,1,0,1,0,1,99]
Output: 99
```

 

**Constraints:**

- `1 <= nums.length <= 3 * 10^4`
- `-231 <= nums[i] <= 2^31 - 1`
- Each element in `nums` appears exactly **three times** except for one element which appears **once**.

## 2. 最佳思路

- 按照二进制的思想，分析每一位，如果所有数字都是出现 3 次，那么二进制的每一位出现的 0 | 1 的次数，应该是 3 的整数倍。如果不是 3 的整数倍，则说明是只出现一次的那个数影响的

```
1 1 1 3 3 3 5

 01
 01
 01
 11
 11
 11
101
---
101
```


## 3. 最佳代码

```java
class Solution {
    public int singleNumber(int[] nums) { // time: O(n logC) = O(32n) = O(n)
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int count = 0;
            for (int num : nums) {
                count += ((num >> i) & 1);
            }
            int bit = count % 3; // 0,1
            ans |= (bit << i);
        }
        return ans;
    }
}
```
