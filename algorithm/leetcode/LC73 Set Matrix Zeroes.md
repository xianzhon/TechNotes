
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[73. 矩阵置零 - 力扣（LeetCode）](https://leetcode.cn/problems/set-matrix-zeroes/)

Given an `m x n` integer matrix `matrix`, if an element is `0`, set its entire row and column to `0`'s.

You must do it [in place](https://en.wikipedia.org/wiki/In-place_algorithm).



**Example 1:**

![img](https://i.hish.top:8/2025/05/08/145521.jpg)

```
Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
Output: [[1,0,1],[0,0,0],[1,0,1]]
```

**Example 2:**

![img](https://i.hish.top:8/2025/05/08/145521.jpg)

```
Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
```



**Constraints:**

- `m == matrix.length`
- `n == matrix[0].length`
- `1 <= m, n <= 200`
- `-2^31 <= matrix[i][j] <= 23^1 - 1`



**Follow up:**

- A straightforward solution using `O(mn)` space is probably a bad idea.
- A simple improvement uses `O(m + n)` space, but still not the best solution.
- Could you devise a constant space solution?



## 解法 1 - O(m+n) space

开辟两个 boolean 数组，分别记录原始的 grid 中哪些行和列出现过 0；先标记一遍，然后再根据 boolean 数组的值去设置 0

```java
class Solution {
    public void setZeroes(int[][] A) { // time: O(nm), space: O(n + m)
        int n = A.length, m = A[0].length;
        boolean[] zeroRows = new boolean[n];
        boolean[] zeroCols = new boolean[m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                if (A[i][j] == 0) {
                    zeroRows[i] = true;
                    zeroCols[j] = true;
                }
            }
        // set zeros according to tag
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                if (zeroRows[i] || zeroCols[j]) {
                    A[i][j] = 0;
                }
            }
    }
}
```



## 解法 2 - O(1) space

很巧妙，利用第一行和第一列去标记其余行和列的状态，然后用两个 boolean 变量来标记第一行和第一列是否要清 0.

```java
class Solution {
    public void setZeroes(int[][] A) { // time: O(nm), space: O(1)
        int m = A.length, n = A[0].length;
        boolean rowZero = false, colZero = false;

        for(int j = 0; j < n; j++) {
            if (A[0][j] == 0) {
                rowZero = true;
                break;
            }
        }
        for(int i = 0; i < m; i++) {
            if (A[i][0] == 0) {
                colZero = true;
                break;
            }
        }
        // 用第一行、第一列标记子矩阵 (1-m * 1-n)
        for(int i = 1; i < m; i++)
            for(int j = 1; j < n; j++)
                if (A[i][j] == 0) {
                    A[0][j] = 0;
                    A[i][0] = 0;
                }

        // 设置0
        for(int i = 1; i < m; i++)
            for(int j = 1; j < n; j++)
                if (A[0][j] == 0 || A[i][0] == 0) {
                    A[i][j] = 0;
                }
        // check first row & column
        if (rowZero) {
            for(int j = 0; j < n; j++) A[0][j] = 0;
        }
        if (colZero) {
            for(int i = 0; i < m; i++) A[i][0] = 0;
        }
    }
}
```

