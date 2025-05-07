
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[438. 找到字符串中所有字母异位词 - 力扣（LeetCode）](https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/)


Given two strings `s` and `p`, return an array of all the start indices of `p`'s anagrams in `s`. You may return the answer in **any order**.



**Example 1:**

```
Input: s = "cbaebabacd", p = "abc"
Output: [0,6]
Explanation:
The substring with start index = 0 is "cba", which is an anagram of "abc".
The substring with start index = 6 is "bac", which is an anagram of "abc".
```

**Example 2:**

```
Input: s = "abab", p = "ab"
Output: [0,1,2]
Explanation:
The substring with start index = 0 is "ab", which is an anagram of "ab".
The substring with start index = 1 is "ba", which is an anagram of "ab".
The substring with start index = 2 is "ab", which is an anagram of "ab".
```



**Constraints:**

- `1 <= s.length, p.length <= 3 * 10^4`
- `s` and `p` consist of lowercase English letters.

## 2. 思路

- how to know two strings are anagrams? char frequency or sort.
- native idea:
	- enumerate each substring, and check anagram. O(n^3).
- better idea: sliding window
	- why can use sliding window? this is a fixed window
		- maintain the window:  charFreq and matchedCount.




## 3. 最佳代码

```java
class Solution {
    public List<Integer> findAnagrams(String s, String p) { // time: O(n+m), space: O(1)
        List<Integer> ans = new ArrayList<>();
        int[] freqPat = new int[128];
        for (int i = 0; i < p.length(); i++) {
            freqPat[p.charAt(i)]++;
        }
        int[] freqWin = new int[128];
        int matchedNum = 0, lenPat = p.length();
        for (int i = 0, j = 0; j < s.length(); j++) {
            // add j to window
            char ch = s.charAt(j);
            freqWin[ch]++;
            if (freqWin[ch] <= freqPat[ch]) {
                matchedNum++;
            }
            // move i forward to maintain fixed window
            if (j - i + 1 > lenPat) {
                char chDel = s.charAt(i++); // 总是忘记 i++
                freqWin[chDel]--;
                if (freqWin[chDel] < freqPat[chDel]) {
                    matchedNum--;
                }
            }
            // add answer if window meet
            if (matchedNum == lenPat) {
                ans.add(i);
            }
        }
        return ans;
    }
}
```



## 写法 2

枚举每个固定窗口的起点。固定大小的滑动窗口比较好写。

```java
class Solution {
    public List<Integer> findAnagrams(String str, String pattern) {
                List<Integer> ans = new ArrayList<>();

        char[] chsStr = str.toCharArray();
        char[] chsPat = pattern.toCharArray();

        int[] freqPat = calculateFreq(chsPat);
        int[] freqWin = new int[128];
        int validCharCount = 0;
        for (int i = 0; i < str.length(); i++) { // fixed sliding window
            char ch = chsStr[i];
            // add ch to window
            if (++freqWin[ch] <= freqPat[ch]) {
                validCharCount++;
            }
            if (i >= pattern.length()) {
                char chRemove = chsStr[i - pattern.length()];
                if (--freqWin[chRemove] < freqPat[chRemove]) {
                    validCharCount--;
                }
            }

            if (validCharCount == pattern.length()) {
                ans.add(i - pattern.length() + 1);
            }
        }

        return ans;
    }

    int[] calculateFreq(char[] letters) {
        int[] freq = new int[128];
        for (char letter : letters) {
            freq[letter]++;
        }
        return freq;
    }

}
```

