
该题掌握程度：
- #很差❌

Tag: #全排列

## 1. 题目
题目链接：[60. 排列序列 - 力扣（LeetCode）](https://leetcode.cn/problems/permutation-sequence/description/)
```
The set [1, 2, 3, ..., n] contains a total of n! unique permutations.

By listing and labeling all of the permutations in order, we get the following sequence for n = 3:

"123"
"132"
"213"
"231"
"312"
"321"

Given n and k, return the kth permutation sequence.

Example 1:
	Input: n = 3, k = 3
	Output: "213"

Example 2:
	Input: n = 4, k = 9
	Output: "2314"

Example 3:	
	Input: n = 3, k = 1
	Output: "123"
 
Constraints:

	1 <= n <= 9
	1 <= k <= n!
```


## 2. 最佳思路

![image-20250420155519302](https://i.hish.top:8/2025/04/20/155548.png)

- 画图分析，枚举每个位置可以放哪些数，比如放 1 之后，后面n - 1位会产生 (n-1)!个排列。根据 k 的落在哪个区间，确定第 1 位填几。后面每一位同理做法。


## 3. 最佳代码

```java
class Solution {
    public String getPermutation(int n, int k) {
        boolean[] used = new boolean[n];
        char[] ans = new char[n];
        for (int i = 0; i < n; i++) { // check each position, find out the digit to put in
            // we have n-i-1 position left, so one digit in i'th position has (n-i-1)! permutations
            int fact = 1;
            for (int j = 1; j <= n - i - 1; j++) {
                fact *= j;
            }
            
            // check which group k belongs to
            for (int j = 0; j < n; j++) {
                if (!used[j]) {
                    if (k > fact) { // skip: 1xx,2xx,3xx, etc
                        k -= fact;
                    } else {
                        used[j] = true;
                        ans[i] = (char)('1' + j);
                        break;
                    }
                }
            }
        }
        return new String(ans);
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n^2)
  外层循环遍历每个位置，总共 n 个位置，内层循环首先计算n - i -1 的阶乘，其次遍历每个可以选择的数。

- **空间复杂度**：O(n)
  使用了 boolean 数组去标记每个数是否被使用过。

### 3.2 特别注意

- 算法思路：easy
- 实现细节：有点难写。求阶乘，直接 for 循环每次求也可以，也不需要用数组去预处理。
- 算法证明： 在每个位置上，枚举可用的数字时，是从小到大放的，所以最后得到的排列一定是满足sequence 要求的。
- 时空复杂度分析：easy

## 4. 相关联的题目

- [31. 下一个排列 - 力扣（LeetCode）](https://leetcode.cn/problems/next-permutation/)

