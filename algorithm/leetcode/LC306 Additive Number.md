
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[306. 累加数 - 力扣（LeetCode）](https://leetcode.cn/problems/additive-number/description/)

An **additive number** is a string whose digits can form an **additive sequence**.

A valid **additive sequence** should contain **at least** three numbers. Except for the first two numbers, each subsequent number in the sequence must be the sum of the preceding two.

Given a string containing only digits, return `true` if it is an **additive number** or `false` otherwise.

**Note:** Numbers in the additive sequence **cannot** have leading zeros, so sequence `1, 2, 03` or `1, 02, 3` is invalid.

 **Example 1:**

```
Input: "112358"
Output: true
Explanation:
The digits can form an additive sequence: 1, 1, 2, 3, 5, 8.
1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
```

**Example 2:**

```
Input: "199100199"
Output: true
Explanation:
The additive sequence is: 1, 99, 100, 199.
1 + 99 = 100, 99 + 100 = 199
```

**Constraints:**

- `1 <= num.length <= 35`
- `num` consists only of digits.

**Follow up:** How would you handle overflow for very large input integers?

## 2. 最佳思路

The main idea is to recursively try all possible splits for the first two numbers and then check whether the remaining string can continue the sequence. Leading zeros should be handled carefully (e.g., "01" is invalid).

- Time complexity:O(2^n)
- Space complexity:O(n)

### How to enumerate two numbers?

**Method 1:** 枚举第二个数的开始和结尾

记字符串 num 的长度为 n，序列最新确定的两个数中，位于前面的数字为 first，first 的最高位在 num 中的下标为 firstStart，first 的最低位在 num 中的下标为 firstEnd。记序列最新确定的两个数中，位于后面的数字为 second，second 的最高位在 num 中的下标为 secondStart，second 的最低位在 num 中的下标为 secondEnd。在穷举第一个数字和第二个数字的过程中，容易得到以下两个结论：firstStart=0，firstEnd+1=secondStart。因此，我们只需要用两个循环来遍历 secondStart 和 secondEnd 所有可能性的组合即可。

链接： https://leetcode.cn/problems/additive-number/solutions/1200446/lei-jia-shu-by-leetcode-solution-cadc/

```java
class Solution {
    public boolean isAdditiveNumber(String num) {
        int n = num.length();
        for (int secondStart = 1; secondStart < n - 1; ++secondStart) {
            if (num.charAt(0) == '0' && secondStart != 1) {
                break;
            }
            for (int secondEnd = secondStart; secondEnd < n - 1; ++secondEnd) {
                if (num.charAt(secondStart) == '0' && secondStart != secondEnd) {
                    break;
                }
                if (valid(secondStart, secondEnd, num)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean valid(int secondStart, int secondEnd, String num) {
       ...
    }
}

// 链接：https://leetcode.cn/problems/additive-number/solutions/1200446/lei-jia-shu-by-leetcode-solution-cadc/
```


**Method 2** - 枚举第一个数和第二个数的的长度

第一个数的长度是 1 到n/2区间内（包含）。第二个数的长度氛围是 1 ~ (n-i)/2 范围内（剩下的数n-i的一半，因为要给第3 个数留空间）

```java
class Solution {
    public boolean isAdditiveNumber(String num) {
        int n = num.length();
        if (n < 3) return false;

        for (int i = 1; i <= n/2; i++) { // 枚举第一个数的长度: num[0:i-1]
            if (i > 1 && num.charAt(0) == '0') break;
            long num1 = Long.valueOf(num.substring(0, i));

            for (int j = 1; j <= (n-i)/2; j++) { // 枚举第二个数的长度: num[i:i+j-1]
                if (j > 1 && num.charAt(i) == '0') break;

                long num2 = Long.valueOf(num.substring(i, i + j));
                if (isValid(num1, num2, num.substring(i + j))) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isValid(long num1, long num2, String remainStr) {
        if (remainStr.isEmpty()) {
            return true;
        }
        long sum = num1 + num2;
        String sumStr = String.valueOf(sum);
        if (!remainStr.startsWith(sumStr)) {
            return false;
        }
        return isValid(num2, sum, remainStr.substring(sumStr.length()));
    }
}
/*
idea:
parse num1 -> long.  len:1 -> n/2
parse num2 -> long.  len: 1 -> (n-len(num1))/2

check (num1, num2, remainStr);
    sum = num1 + num2
    remainStr.startswith(sum)
        check(num2, sum, remainStr.substring(len(sum)))

Edge Cases Handled:
"112358" → true (1+1=2, 1+2=3, 2+3=5, 3+5=8)
"199100199" → true (19+91=110, 91+110=199)
"101" → true (1+0=1)
"011" → true (0+1=1)
"000" → true (0+0=0)
"1023" → false (1+0=1, but next should be 0+1=1, mismatch)
"123" → true (1+2=3)
*/
```

**Followup:** 如何处理溢出的问题？ Use BigInteger. 非常简单，直接替换即可。算法的框架都不用变。

```java
import java.math.*;

class Solution {
    public boolean isAdditiveNumber(String num) {
        int n = num.length();
        if (n < 3) return false;

        for (int i = 1; i <= n/2; i++) {
            if (i > 1 && num.charAt(0) == '0') break;
            BigInteger num1 = new BigInteger(num.substring(0, i));

            for (int j = 1; j <= (n-i)/2; j++) {
                if (j > 1 && num.charAt(i) == '0') break;

                BigInteger num2 = new BigInteger(num.substring(i, i + j));
                if (isValid(num1, num2, num.substring(i + j))) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isValid(BigInteger num1, BigInteger num2, String remainStr) {
        if (remainStr.isEmpty()) {
            return true;
        }
        BigInteger sum = num1.add(num2);
        String sumStr = String.valueOf(sum);
        if (!remainStr.startsWith(sumStr)) {
            return false;
        }
        return isValid(num2, sum, remainStr.substring(sumStr.length()));
    }
}
/*
idea:
parse num1 -> long.  len:1 -> n/2
parse num2 -> long.  len: 1 -> (n-len(num1))/2

check (num1, num2, remainStr);
    sum = num1 + num2
    remainStr.startswith(sum)
        check(num2, sum, remainStr.substring(len(sum)))
*/
```

- 时间复杂度：O(n^3)
  两种循环遍历前两个数的位置，然后验证是否有效也需要 O(n).
- 空间复杂度：O(n)
  验证是否有效时，递归用到的栈空间，最大深度是n。