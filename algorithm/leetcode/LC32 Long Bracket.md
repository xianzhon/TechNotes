
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[32. 最长有效括号 - 力扣（LeetCode）](https://leetcode.cn/problems/longest-valid-parentheses/description/)

Given a string containing just the characters `'('` and `')'`, return *the length of the longest valid (well-formed) parentheses* *substring*.

 

**Example 1:**

```
Input: s = "(()"
Output: 2
Explanation: The longest valid parentheses substring is "()".
```

**Example 2:**

```
Input: s = ")()())"
Output: 4
Explanation: The longest valid parentheses substring is "()()".
```

**Example 3:**

```
Input: s = ""
Output: 0
```

 

**Constraints:**

- `0 <= s.length <= 3 * 104`
- `s[i]` is `'('`, or `')'`.

## 2. 最佳思路

```
- Using DP
- state: dp[i]表示的是以右括号i结尾的最长的有效括号的个数
- formula: 
    if s[i] != ')': dp[i]= 0
    if s[i] == ')'
        s[i-1] == '(':
            dp[i] = 2 + (dp[i-2] ?)
        s[i-1] == ')':
                 s[i-dp[i-1]-1] == '('
            dp[i]: && prev == )
                dp[i-1] +2 + (dp[i - dp[i-1] - 2]?)
            ()(())
              |  |
- base: 
    int[] dp = new int[n];
    dp[0] = 0
- answer: max(dp[i])
```
## 3. 最佳代码

```java
class Solution {
    public int longestValidParentheses(String s) { // time: O(n), space: O(n)
        int n = s.length();
        char[] chs = s.toCharArray();
        int[] dp = new int[n];
        int ans = 0;
        for (int i = 1; i < n; i++) {
            if (chs[i] == ')') {
                if (chs[i-1] == '(') { 
                    // )()   (()
                    //   i     i
                    dp[i] = 2 + (i > 2 ? dp[i - 2]: 0);                    
                } else { 
                    // ()(())
                    //      i
                    if (i - dp[i-1] >= 1 && chs[i - dp[i-1] - 1] == '(') {
                        dp[i] = dp[i - 1] + 2 + (i - dp[i-1] >= 2 ? dp[i - dp[i-1] - 2]: 0);
                    }                    
                }
                ans = Math.max(ans, dp[i]);
            }
        }
        return ans;
    }
}
```

## 4. 其他解法

### 4.1 解法2 - use stack 不太好理解

这里的 stack 用来找匹配的括号的序列的, start 记录一段有效括号的前一个位置。
```java
class Solution {
    public int longestValidParentheses(String s) { // time: O(n), space: O(n)
        Deque<Integer> stack = new ArrayDeque<>(); // store ('s position
        int start = -1; // store the position right before the valid paranthese ()()
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                stack.push(i);
            } else { // )
                if (!stack.isEmpty()) { // has a pair
                    stack.pop();
                    if (stack.isEmpty()) {
                        ans = Math.max(ans, i - start); //|()()
                    } else {
                        ans = Math.max(ans, i - stack.peek()); // (|()
                    }                
                } else { // single )
                    start = i; // start 指向的是一段合法的括号匹配的前一个位置
                }
            }
        }
        return ans;
    }
}
```
