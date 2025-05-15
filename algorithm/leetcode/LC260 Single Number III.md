
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[260. 只出现一次的数字 III - 力扣（LeetCode）](https://leetcode.cn/problems/single-number-iii/description/)

Given an integer array `nums`, in which exactly two elements appear only once and all the other elements appear exactly twice. Find the two elements that appear only once. You can return the answer in **any order**.

You must write an algorithm that runs in linear runtime complexity and uses only constant extra space.

 

**Example 1:**

```
Input: nums = [1,2,1,3,2,5]
Output: [3,5]
Explanation:  [5, 3] is also a valid answer.
```

**Example 2:**

```
Input: nums = [-1,0]
Output: [-1,0]
```

**Example 3:**

```
Input: nums = [0,1]
Output: [1,0]
```

 

**Constraints:**

- `2 <= nums.length <= 3 * 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`
- Each integer in `nums` will appear twice, only two integers will appear once.

## 2. 最佳思路

- 一个数出现两次，那么它们异或的结果是 0，如果一个数组里，只有一个数出现一次，其他数出现两次，那么我们可以异或直接得到答案
- 现在有两个不一样的数，都只出现一次。那我们可以根据这两个数异或之后的其中一位（结果必然不是 0，所以必然有一个二进制位不一样），将数组分成两组。然后在两组，各自就只有一个数不一样，各自异或就得到答案了。
- 问题 1：若两个不同的数异或结果 x，如何找到某一个bit 它是 1 呢？笨办法是枚举 32 次。
	更巧妙的做法：异或的技巧，一个数 n，  a = (n & (n-1)) 会把最低位的 1 去掉。然后 n ^ (a) 得到的数就是一个 mask（只有最低位的 1 保留了）


## 3. 最佳代码

```java
class Solution {
    public int[] singleNumber(int[] nums) {
        int x = 0;
        for (int num : nums) {
            x ^= num;
        }

        // x & (x - 1) 会把x中最低位的 1 化为 0,然后再跟 x 异或,就只有最低位的 1 保留,其他的都是 0,就得到一个 mask
        int mask = x ^ (x & (x - 1));

        // split the array to to group according to bit i        
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & mask) == mask) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[] {a, b};
    }
}
```



## 考点

二进制的 trick。

- 异或的性质：两个相同的数异或结果是 0
- 如何将最低位的 1 去掉：`x & (x - 1)`
- 如何只保留最低位的 1 呢： `x ^(x & (x - 1))`.   更简单的做法： `x & (-x)`

```
x & (-x) 举例：
x = 0b01100
-x= 0b10100

x&(-x)= 0b00100 只保留最低位的 1
```