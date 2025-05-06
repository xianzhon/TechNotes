
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[76. 最小覆盖子串 - 力扣（LeetCode）](https://leetcode.cn/problems/minimum-window-substring/description/?envType=study-plan-v2&envId=top-interview-150)

Given two strings `s` and `t` of lengths `m` and `n` respectively, return *the **minimum window*** ***substring\*** *of* `s` *such that every character in* `t` *(**including duplicates**) is included in the window*. If there is no such substring, return *the empty string* `""`.

The testcases will be generated such that the answer is **unique**.



**Example 1:**

```
Input: s = "ADOBECODEBANC", t = "ABC"
Output: "BANC"
Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
```

**Example 2:**

```
Input: s = "a", t = "a"
Output: "a"
Explanation: The entire string s is the minimum window.
```

**Example 3:**

```
Input: s = "a", t = "aa"
Output: ""
Explanation: Both 'a's from t must be included in the window.
Since the largest window of s only has one 'a', return empty string.
```



**Constraints:**

- `m == s.length`
- `n == t.length`
- `1 <= m, n <= 10^5`
- `s` and `t` consist of uppercase and lowercase English letters.


**Follow up:** Could you find an algorithm that runs in `O(m + n)` time?

## 2. 最佳思路
```java
- native idea: enumerate each substring, check condition. O(n^3). Timeout.
- better idea: sliding window
    - why?
       ---------i-------j--------
                        |
        for each j, there is a nearst i, to make s[i:j] covers chars in t.
        when move j forward, i must stay or move forward. It can't go back.
    - how:
        window: maintain a charFreq map and matched char count (chars that make contribution to t)
        move j++, update char freq, and if it makes contribution, then matchedCharNum++
        move i++, update charFreq, also matchedCharNum-- if it is not redundant.
        update answer when matchedCharNum == t's length.
```

## 3. 最佳代码

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

## 4. 写法 2 - 枚举左边界
思路：
```java
- sliding window
    - why?
       ---------i-------j--------
                        |
        for each i, there is a nearst j, to make s[i:j] covers chars in t.
        when move i forward, j must stay or move forward. It can't go back.
    - how:
        window: maintain a charFreq map and matched char count (chars that make contribution to t)
        move j++, update char freq, and if it makes contribution, then matchedCharNum++
        move i++, update charFreq, also matchedCharNum-- if it is not redundant.
        update answer when matchedCharNum == t's length.
```

代码：
```java
class Solution {
    public String minWindow(String s, String t) {
        var sourceChars = s.toCharArray();
        var targetChars = t.toCharArray();

        var freqTarget = new int[128];
        for (int i = 0; i < targetChars.length; i++) {
            freqTarget[targetChars[i]]++;
        }

        // sliding window
        var freqWindow = new int[128];
        int matchedCharNum = 0, n = sourceChars.length;
        int startPos = 0, minLength = Integer.MAX_VALUE;

        for (int i = 0, j = 0; i < n; i++) {
            // move j forward until window met
            while (j < n && matchedCharNum < targetChars.length) {
                freqWindow[sourceChars[j]]++;
                if (freqWindow[sourceChars[j]] <= freqTarget[sourceChars[j]]) {
                    matchedCharNum++;
                }
                j++;
            }

            if (matchedCharNum == targetChars.length && j - i < minLength) {
                minLength = j - i;
                startPos = i;
            }

            // shrink i
            freqWindow[sourceChars[i]]--;
            if (freqWindow[sourceChars[i]] < freqTarget[sourceChars[i]]) {
                matchedCharNum--;
            }
        }
        return minLength != Integer.MAX_VALUE ? s.substring(startPos, startPos + minLength) : "";
    }
}
```