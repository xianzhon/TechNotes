## 题目
[393. UTF-8 编码验证 - 力扣（LeetCode）](https://leetcode.cn/problems/utf-8-validation/description/)

##  思路

1. get number of bytes from the first byte
2. verify each continus bytes (10xxx)

## Code

- 技巧：java 中可以用 0b 开头，表示二进制树，配合移位操作，可读性更好一点
```java
class Solution {
    public boolean validUtf8(int[] data) { // time: O(n), space: O(1)
        final int CONTINUS_MASK = 0b11 << 6;
        final int CONTINUS_VALUE = 1 << 7;
        for(int i  = 0; i < data.length; i++) {
            int byteNum = getNumberOfBytes(data[i]);
            if (byteNum == -1 || i + byteNum - 1 >= data.length) { // detail1: 没有足够长度也是 invalid
                return false;
            }
            for (int j = 1; j < byteNum; j++) {
                // check continus byte
                int val = data[i + j];
                if ((val & CONTINUS_MASK) != CONTINUS_VALUE) {
                    return false;
                }
            }
            i += byteNum - 1;
        }
        return true;
    }

    int getNumberOfBytes(int num) {
        final int ONE_BYTE_MASK = (1 << 7);
        final int TWO_BYTE_MAST = (0b11 << 6);
        final int THREE_BYTE_MAST = (0b111 << 5);
        final int FOUR_BYTE_MAST = (0b1111 << 4);

        if ((num & FOUR_BYTE_MAST) == FOUR_BYTE_MAST) {
            return (num & (1<<3)) == 0 ? 4 : -1;
        }
        if ((num & THREE_BYTE_MAST) == THREE_BYTE_MAST) {
            return 3;
        }
        if ((num & TWO_BYTE_MAST) == TWO_BYTE_MAST) {
            return 2;
        }
        if ((num & ONE_BYTE_MASK) == 0) {
            return 1;
        }
        return -1;
    }
}
```

## Code 2 - 换种思路求 getNumberOfBytes

```java
class Solution {
    public boolean validUtf8(int[] data) {
        int i = 0;
        final int CONTINUS_MASK = 0b11 << 6;
        final int CONTINUS_VALUE = 1 << 7;
        while (i < data.length) {
            int byteNum = getNumberOfBytes(data[i]);
            if (byteNum == -1 || i + byteNum - 1 >= data.length) {
                return false;
            }
            for (int j = 1; j < byteNum; j++) {
                // check continus byte
                int val = data[i + j];
                if ((val & CONTINUS_MASK) != CONTINUS_VALUE) {
                    return false;
                }
            }
            i += byteNum;
        }
        return true;
    }

    int getNumberOfBytes(int num) {  // 判断 num 左边有多少个 1
        int i = 7;
        while (i >= 0 && ((num >> i) & 1) == 1) {
            i--;
        }
        int count = 7 - i;
        if (count == 1 || count > 4) {
            return -1;
        }
        return count == 0 ? 1 : count;
    }
}
```