# 从多个递增的生成序列中找第n个数


## 题目1：丑数 - 《剑指offer》

**题目**：

我们把只包含质因子 2、3 和 5 的数称作丑数（Ugly Number）。求按从小到大的顺序的第 n 个丑数。其中1是第一个丑数。

在线OJ：[剑指 Offer 49. 丑数 - 力扣（LeetCode）](https://leetcode.cn/problems/chou-shu-lcof/)

**思路分析**：

将2为因子的数，看成一个递增的序列： 2x1, 2x2, 2x3, 2x4, ...  
将3为因子的数，也看成一个递增的序列，3x1, 3x2, 3x3, 3x4, ....  
同理，5为因子的数，看成递增序列：5x1, 5x2, 5x3, ...  

原题转化成求三个序列合并且去重之后的第n个数，数组第一个数是1。可以借鉴合并两个有序数组的思路，这题使用3个指针，分别记录3个递增序列的遍历的位置，每次取三者最小的，然后移动指针，直到数组中包含了第n个数。每一轮可能有相等的元素，去重的关键是，每次找到最小值后，将序列中等于最小值的序列的指针都往后移动1步。【当然，去重也可以使用Set，虽然空间复杂度都是O(n), 但是没必要】

```java
    public int nthUglyNumber(int n) { // time: O(n), space: O(n)
        int[] A = new int[n];   // 使用长度为n的数组 A 记录前n个丑数，第一个是1
        A[0] = 1;
        for(int i = 1, x = 0, y = 0, z = 0; i < n; i++) { // 每一轮比较都可以确定一个丑数 A[i]
            A[i] = min(A[x] * 2, A[y] * 3, A[z] * 5);
            if (A[i] == A[x] * 2) x++;
            if (A[i] == A[y] * 3) y++; // 去重的关键， 这里没有用 else if
            if (A[i] == A[z] * 5) z++;
        }
        return A[n-1];
    }

    int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
```


## 题目2：某论坛看到的面试题

**题目：**

给定一个数组，只有一个初始数字1，对这个数组的每个数字k，做2k+1 和 3k+1，然后加入数组，要求这个数组是sorted并且没有重复元素，返回第n个。 这个数组应该是 [1,3,4,7,9,10,13, ....]

**思路：**

跟题目1的丑数，一模一样，这题是2k+1 和 3k+1 生成的两个递增序列，原问题转化成，求两个递增序列合并且去重之后的第n个数。

```java
int findNthNum(int n) { // time: O(n), space: O(n)
    int[] nums = new int[n];
    nums[0] = 1;
    for (int i = 1, p1 = 0, p2 = 0; i < n; i++) { // 生成后面的n-1个数， nums[1]...nums[n-1]
        int x = nums[p1] * 2 + 1;
        int y = nums[p2] * 3 + 1;
        nums[i] = Math.min(x, y);
        if (nums[i] == x) p1++;
        if (nums[i] == y) p2++; // 去重的关键：两个if并列，而不是if-else
    }
    return nums[n - 1];
}
```
