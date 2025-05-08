
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[439. 三元表达式解析器 - 力扣（LeetCode）](https://leetcode.cn/problems/ternary-expression-parser/description/)

Given a string `expression` representing arbitrarily nested ternary expressions, evaluate the expression, and return *the result of it*.

You can always assume that the given expression is valid and only contains digits, `'?'`, `':'`, `'T'`, and `'F'` where `'T'` is true and `'F'` is false. All the numbers in the expression are **one-digit** numbers (i.e., in the range `[0, 9]`).

The conditional expressions group right-to-left (as usual in most languages), and the result of the expression will always evaluate to either a digit, `'T'` or `'F'`.



**Example 1:**

```
Input: expression = "T?2:3"
Output: "2"
Explanation: If true, then result is 2; otherwise result is 3.
```

**Example 2:**

```
Input: expression = "F?1:T?4:5"
Output: "4"
Explanation: The conditional expressions group right-to-left. Using parenthesis, it is read/evaluated as:
"(F ? 1 : (T ? 4 : 5))" --> "(F ? 1 : 4)" --> "4"
or "(F ? 1 : (T ? 4 : 5))" --> "(T ? 4 : 5)" --> "4"
```

**Example 3:**

```
Input: expression = "T?T?F:5:3"
Output: "F"
Explanation: The conditional expressions group right-to-left. Using parenthesis, it is read/evaluated as:
"(T ? (T ? F : 5) : 3)" --> "(T ? F : 3)" --> "F"
"(T ? (T ? F : 5) : 3)" --> "(T ? F : 5)" --> "F"
```



**Constraints:**

- `5 <= expression.length <= 10^4`
- `expression` consists of digits, `'T'`, `'F'`, `'?'`, and `':'`.
- It is **guaranteed** that `expression` is a valid ternary expression and that each number is a **one-digit number**.

## 2. 最佳思路

- 从右往左去 parse，使用 stack 去辅助。
- why iterate from right to left?  `a ? b : c`. 因为有嵌套，我们求值的时候只能从后往前看，逐层去求。

## 3. 最佳代码

```java
class Solution {
    public String parseTernary(String expression) { // time: O(n), space: O(n)
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = expression.length() - 1; i >= 0; i--) {
            char ch = expression.charAt(i);
            if (ch == '?') {
                char cond = expression.charAt(i - 1);
                i--;
                char trueVal = stack.pop();
                char falseVal = stack.pop();
                stack.push(cond == 'T' ? trueVal : falseVal);
            } else if (ch != ':') {
                stack.push(ch);
            }
        }
        return String.valueOf(stack.pop());
    }
}
```

