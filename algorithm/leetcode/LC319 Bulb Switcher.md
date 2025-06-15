
该题掌握程度：
- #很差❌

## 1. 题目

题目链接：[319. 灯泡开关 - 力扣（LeetCode）](https://leetcode.cn/problems/bulb-switcher/description/)

题目意思: n 个灯， 按 n 轮，第 i 轮，编号是 i 的整数倍的全按一遍。求最后亮灯的数量。



There are `n` bulbs that are initially off. You first turn on all the bulbs, then you turn off every second bulb.

On the third round, you toggle every third bulb (turning on if it's off or turning off if it's on). For the `ith` round, you toggle every `i` bulb. For the `nth` round, you only toggle the last bulb.

Return *the number of bulbs that are on after `n` rounds*.



**Example 1:**

![img](https://i.hish.top:8/2025/06/07/230223.jpg)

```
Input: n = 3
Output: 1
Explanation: At first, the three bulbs are [off, off, off].
After the first round, the three bulbs are [on, on, on].
After the second round, the three bulbs are [on, off, on].
After the third round, the three bulbs are [on, off, off].
So you should return 1 because there is only one bulb is on.
```

**Example 2:**

```
Input: n = 0
Output: 0
```

**Example 3:**

```
Input: n = 1
Output: 1
```



**Constraints:**

- `0 <= n <= 10^9`





































## 2. 最佳思路

- 问题：什么样的灯最后是亮的? 答案:被按了奇数次.
- 哪些灯被按了奇数次? 一个编号 x 的灯,它被按的次数 = 它的约数的个数
- 如果 编号 x 有一个约数 p,那么 x/p 也一定是它的约数. 因此,约数通常是成对出现, 除非p = x/p.
- 那么可以得到,只有完全平方数的约数个数才是奇数个.
- 问题: n 以内,有多少个完全平方数? sqrt(n) 下取整.
## 3. 最佳代码

```java
class Solution {
    public int bulbSwitch(int n) { // time: O(1)
        return (int)Math.sqrt(n);
    }
}
```


## 背景知识

- 约数 = 因数，两个数 a、b，如果 a 能够被 b 整除，则称 a 是 b 的倍数，b 是 a 的约数。
	- 约数和倍数是二元关系，只能说某个数是另外一个数的约数，或者倍数。
	- 一个数的约数，必然包含1 和它本身
- 公因数：如果一个数既是 a 的因数，又是 b 的因数，那么它是这两个数的公因数。
	- 最大公因数，两个数的公因数中最大的一个。算法求解：`辗转相除法。`
- 合数和质数
	- 质数：**质数指那些大于1的，且除了1和它自身之外再没有其它约数的自然数。**
	- 合数：**合数是指除了1和它自身之外还有其它约数的自然数**
	- 规律，自然数 1 既不是质数也不是合数，2 是第一个质数，且是质数中唯一的偶数。
- 质因数分解（分解质因数）