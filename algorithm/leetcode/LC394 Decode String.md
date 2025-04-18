## 题目

[394. 字符串解码 - 力扣（LeetCode）](https://leetcode.cn/problems/decode-string/description/)
挺难写的，需要多练习几次。

```
Given an encoded string, return its decoded string.

The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times. Note that k is guaranteed to be a positive integer.

You may assume that the input string is always valid; there are no extra white spaces, square brackets are well-formed, etc. Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k. For example, there will not be input like 3a or 2[4].

The test cases are generated so that the length of the output will never exceed 10^5.


Example 1:

	Input: s = "3[a]2[bc]"
	Output: "aaabcbc"

Example 2:

	Input: s = "3[a2[c]]"
	Output: "accaccacc"

Example 3:

	Input: s = "2[abc]3[cd]ef"
	Output: "abcabccdcdcdef"
 

Constraints:
	1 <= s.length <= 30
	s consists of lowercase English letters, digits, and square brackets '[]'.
	s is guaranteed to be a valid input.
	All the integers in s are in the range [1, 300].
```

## 思路

- 不是很难，逐层去decode，甚至可以用递归来做，难的是写法上，又是解析数字，又是处理中括号，容易犯错
- 我们使用两个栈，一个保存左括号之前出现的字符，一个保存 repeat 的counter。两个变量（currentCount，currentString）在遍历字符串的时候，去更新
- 遇到左中括号，意味着需要进栈一次，把（currentCount，currentString）进栈，然后重新初始化这两个变量
- 遇到右括号，需要出栈，currentString 表示需要重复的字符，栈顶的 counter 是需要重复的次数。栈顶的 string 是重复字符左边的部分
- 最后返回 currentString 即可

官方题解： https://leetcode.cn/problems/decode-string/solutions/19447/decode-string-fu-zhu-zhan-fa-di-gui-fa-by-jyd 这里的动画演示的很好。

## 代码

```java
class Solution {
    public String decodeString(String s) { // time: O(n), space: O(n)
        Deque<String> strStack = new ArrayDeque<>();
        Deque<Integer> countStack = new ArrayDeque<>();
        StringBuilder currentStr = new StringBuilder();
        int currentCount = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isDigit(ch)) {
                currentCount = currentCount * 10 + Character.getNumericValue(ch);                
            } else if (Character.isLetter(ch)) {
                currentStr.append(ch);
            } else if (ch == '[') {
                strStack.push(currentStr.toString()); // letters before repeat
                countStack.push(currentCount);

                currentCount = 0;
                // is there a clear method? currentStr.setLength(0);
                // https://stackoverflow.com/questions/5192512/how-can-i-clear-or-empty-a-stringbuilder
                currentStr = new StringBuilder(); 
            } else if (ch == ']') {
                int count = countStack.pop();
                StringBuilder sb = new StringBuilder();
                while (count-- > 0) {
                    sb.append(currentStr);
                }
                // currentStr.insert(0, strStack.pop());
                currentStr = new StringBuilder(strStack.pop() + sb.toString());
            }
        }
        return currentStr.toString();
    }
}
```

## 变形 1：如果数字在右边

来自评论区的讨论：
> 2024.8.30 猿辅导笔试第二题。不过那个题的数字在后面

```
Example:
[a[c]2]3  => accaccacc
```


### 代码 - 参考Deepseek 给的答案

```java
    public String decodeString(String s) {
        Stack<StringBuilder> stringStack = new Stack<>();
        Stack<Integer> countStack = new Stack<>();
        StringBuilder currentStr = new StringBuilder();
        int currentCount = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '[') {
                // Push the current string and count to the stacks
                stringStack.push(currentStr);
                countStack.push(currentCount);
                // Reset for the new substring inside brackets
                currentStr = new StringBuilder();
                currentCount = 0;
            } else if (ch == ']') {
                // Read the following digits to get the count
                int j = i + 1;
                while (j < s.length() && Character.isDigit(s.charAt(j))) {
                    j++;
                }
                int repeatCount = Integer.parseInt(s.substring(i + 1, j));
                i = j - 1; // Move i to the end of the digits

                // Pop the previous string and repeat the current string
                StringBuilder temp = stringStack.pop();
                for (int k = 0; k < repeatCount; k++) {
                    temp.append(currentStr);
                }
                currentStr = temp;
            } else if (Character.isDigit(ch)) {
                // Build the multi-digit number
                currentCount = currentCount * 10 + Character.getNumericValue(ch);
            } else {
                // Append the character to the current string
                currentStr.append(ch);
            }
        }

        return currentStr.toString();
    }
```
