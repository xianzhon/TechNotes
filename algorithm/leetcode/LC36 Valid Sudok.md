
该题掌握程度：
- #熟练✓

**技巧**：利用 int 的若干位，作为集合实现去重的功能。

## 1. 题目
题目链接：[36. 有效的数独 - 力扣（LeetCode）](https://leetcode.cn/problems/valid-sudoku/description/)

## 2. 最佳思路
- 难点是判断数字在这一行，一列，这个小方格里是否重复
- 因为只有 1-9，所以可以用 int 的低 9 位来作为一个 set 保存哪些数出现过
- 那么一行需要一个 int，一列也需要一个 int，一个小方格对应一个 int
- 遍历每个元素，如果是数组，判断它在这一行，这一列，这个小方格是否出现过，若出现过，则重复；否则用位运算加到集合(int)中去

## 3. 最佳代码 - 位运算+状态压缩

```java
class Solution {
    public boolean isValidSudoku(char[][] board) {
        int[] rowSeen = new int[9];
        int[] colSeen = new int[9];
        int[] boxSeen = new int[9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;
                int digit = Character.getNumericValue(board[i][j]);
                int mask = (1 << digit);
                int boxId = (i / 3) * 3 + (j / 3); // take care!
                // check dup
                if ((rowSeen[i] & mask) != 0) return false;
                if ((colSeen[j] & mask) != 0) return false;
                if ((boxSeen[boxId] & mask) != 0) return false;

                // add to set
                rowSeen[i] |= mask;
                colSeen[j] |= mask;
                boxSeen[boxId] |= mask;
            }
        }
        return true;
    }
}
/*
idea:
check each row/col/box whether there are duplicates number put in.
since we only have 1-9 numbers, we can consider using 9 bit for dup check.
each row/col/box uses one int to help to check dup.
    add to int:   x |= 1<<digit
    check dup: x & (1 << digit) != 0
*/
```