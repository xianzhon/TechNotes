# 滑动窗口算法的套路

Leetcode 题单： https://leetcode.cn/problem-list/sliding-window/  （截止 2025.05.06 有163 题）

### [3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/) - 满足条件的最长的窗口

#### 思路1：枚举右边界

思路：
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

代码实现：
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

#### 思路 2: 枚举左边界

```java
- sliding window: O(n)
	- why can use?
	    -----------i---------j-------
	                         |
	    for each i, there is a right-most j, to make the window s[i:j] have no repeat chars.
	    when i move forward, j must stay there or move forward, it can not move backward.

    how to use:
    - check no repeat char in window: use a Set<Character> or int[]
    - move j forward: set.add(s[j])
    - move i forward: set.remove(s[i])
```

代码实现：
```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        var n = s.length();
        var seen = new HashSet<>();
        var ans = 0;
        for (int i = 0, j = 0; i < n; i++) {  // window: [i, j)
            // move j until window not met
            while (j < n && !seen.contains(s.charAt(j))) {
                seen.add(s.charAt(j++));
            }

            // update answer for window [i, j)
            ans = Math.max(ans, j - i);

            // move i forward
            seen.remove(s.charAt(i));
        }
        return ans;
    }
}
```


### [76. 最小覆盖子串](https://leetcode.cn/problems/minimum-window-substring/) - 满足条件的最小的窗口

- 笔记： [LC76 Minimum Window Substring](leetcode/LC76%20Minimum%20Window%20Substring.md)
- 思路：枚举右边界（更好一点），或者枚举左边界。
```java
class Solution {
    public String minWindow(String s, String t) { // time: O(m + n)
        char[] sourceChars = s.toCharArray();
        char[] targetChars = t.toCharArray();

        // Frequency map for characters in target string t
        int[] targetFreq = new int[128];
        for (char ch : targetChars) {
            targetFreq[ch]++;
        }

        // Sliding window variables
        int[] windowFreq = new int[128];
        int matchedChars = 0;          // Count of matched characters in current window
        int windowStart = 0;           // Start of the current window
        int minWindowLength = Integer.MAX_VALUE;
        int resultStart = 0;           // Start position of the minimum window found

        for (int left = 0, right = 0; right < sourceChars.length; right++) {
            char currentChar = sourceChars[right];

            // Expand the window by including current character
            windowFreq[currentChar]++;

            // If this character contributes to matching the target
            if (windowFreq[currentChar] <= targetFreq[currentChar]) {
                matchedChars++;
            }

            // Try to shrink the window from the left while maintaining the condition
            while (left <= right && windowFreq[sourceChars[left]] > targetFreq[sourceChars[left]]) {
                windowFreq[sourceChars[left]]--;
                left++;
            }

            // If we've matched all characters and found a smaller window
            if (matchedChars == t.length() && (right - left + 1) < minWindowLength) {
                minWindowLength = right - left + 1;
                resultStart = left;
            }
        }

        return minWindowLength != Integer.MAX_VALUE
               ? s.substring(resultStart, resultStart + minWindowLength)
               : "";
    }
}
```

### [992. K 个不同整数的子数组 - 力扣（LeetCode）](https://leetcode.cn/problems/subarrays-with-k-different-integers/)
- 笔记：[LC992. Subarrays with K Different Integers](leetcode/LC992.%20Subarrays%20with%20K%20Different%20Integers.md)

### [159. 至多包含两个不同字符的最长子串 - 力扣（LeetCode）](https://leetcode.cn/problems/longest-substring-with-at-most-two-distinct-characters/description/)
- 笔记：[LC159. Longest Substring with At Most Two Distinct Characters - vip](leetcode-vip/LC159.%20Longest%20Substring%20with%20At%20Most%20Two%20Distinct%20Characters%20-%20vip.md)
- 两种写法都行，枚举右边界（推荐），或者枚举左边界。

### [424. 替换后的最长重复字符 - 力扣（LeetCode）](https://leetcode.cn/problems/longest-repeating-character-replacement/description/)
- 笔记：[LC424. Longest Repeating Character Replacement](leetcode/LC424.%20Longest%20Repeating%20Character%20Replacement.md)
- 非常类似的题目，简化版本 [1004. 最大连续1的个数 III - 力扣（LeetCode）](https://leetcode.cn/problems/max-consecutive-ones-iii/description/?envType=problem-list-v2&envId=sliding-window) 笔记：[LC1004. Max Consecutive Ones III](leetcode/LC1004.%20Max%20Consecutive%20Ones%20III.md)

### [30. 串联所有单词的子串 - 力扣（LeetCode）](https://leetcode.cn/problems/substring-with-concatenation-of-all-words/description/?envType=problem-list-v2&envId=sliding-window)
- 笔记：[LC30 Substring with Concatenation of All Words](leetcode/LC30%20Substring%20with%20Concatenation%20of%20All%20Words.md)
- 很难想，取模分组，然后再用 sliding window。

### 笔记：[LC438 Find All Anagrams in a String](leetcode/LC438%20Find%20All%20Anagrams%20in%20a%20String.md)
- 固定大小的滑动窗口，相对容易一点

### 笔记：[LC187 Repeated DNA Sequences](leetcode/LC187%20Repeated%20DNA%20Sequences.md)
- 固定大小的滑动窗口，难点是对子串进行编码（求 hash 值）