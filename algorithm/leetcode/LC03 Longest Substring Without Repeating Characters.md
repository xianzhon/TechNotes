
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[3. 无重复字符的最长子串 - 力扣（LeetCode）](https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/)

Given a string `s`, find the length of the **longest** **substring** without duplicate characters.

 **Example 1:**

```
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.
```

**Example 2:**

```
Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.
```

**Example 3:**

```
Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
```

 

**Constraints:**

- `0 <= s.length <= 5 * 10^4`
- `s` consists of English letters, digits, symbols and spaces.

## 2. 最佳思路

```java
- native idea: enumerate each substring, check duplicates. O(n^3)
- sliding window: O(n)
	- why can use?
	    -----------i---------j-------                         
	                         |
	    for each j, there is a left-most i, to make the window s[i:j] have no repeat chars.
	    when j move forward, i must stay there or move forward.

    how to use:
    - check no repeat char in window: use a Set<Character> or int[]
    - move j forward: set.add(s[j])
    - move i forward: set.remove(s[i])
```
## 3. 最佳代码

```java
class Solution {
    public int lengthOfLongestSubstring(String s) { // time: O(n), space: O(n)
        int n = s.length();
        var seen = new HashSet<>();
        var ans = 0;
        for (int i = 0, j = 0; j < n; j++) {
            // move i until j can add to the window
            while (seen.contains(s.charAt(j))) {
                seen.remove(s.charAt(i++));
            }
            // add j to window
            seen.add(s.charAt(j));

            // update answer (intially the window met the condition, every move of j & i, the window [i, j-1] still meet the condition)
            ans = Math.max(ans, j - i + 1);
        }
        return ans;
    }
}
```


## 总结

- [10-sliding-window-pattern](../10-sliding-window-pattern.md)