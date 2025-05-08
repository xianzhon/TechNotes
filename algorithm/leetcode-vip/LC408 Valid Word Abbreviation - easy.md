
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[408. 有效单词缩写 - 力扣（LeetCode）](https://leetcode.cn/problems/valid-word-abbreviation/)

A string can be **abbreviated** by replacing any number of **non-adjacent**, **non-empty** substrings with their lengths. The lengths **should not** have leading zeros.

For example, a string such as `"substitution"` could be abbreviated as (but not limited to):

- `"s10n"` (`"s ubstitutio n"`)
- `"sub4u4"` (`"sub stit u tion"`)
- `"12"` (`"substitution"`)
- `"su3i1u2on"` (`"su bst i t u ti on"`)
- `"substitution"` (no substrings replaced)

The following are **not valid** abbreviations:

- `"s55n"` (`"s ubsti tutio n"`, the replaced substrings are adjacent)
- `"s010n"` (has leading zeros)
- `"s0ubstitution"` (replaces an empty substring)

Given a string `word` and an abbreviation `abbr`, return *whether the string **matches** the given abbreviation*.

A **substring** is a contiguous **non-empty** sequence of characters within a string.

 

**Example 1:**

```
Input: word = "internationalization", abbr = "i12iz4n"
Output: true
Explanation: The word "internationalization" can be abbreviated as "i12iz4n" ("i nternational iz atio n").
```

**Example 2:**

```
Input: word = "apple", abbr = "a2e"
Output: false
Explanation: The word "apple" cannot be abbreviated as "a2e".
```

 

**Constraints:**

- `1 <= word.length <= 20`
- `word` consists of only lowercase English letters.
- `1 <= abbr.length <= 10`
- `abbr` consists of lowercase English letters and digits.
- All the integers in `abbr` will fit in a 32-bit integer.

## 2. 最佳思路

两个指针，直接模拟。

## 3. 最佳代码
有一个 corner case，非常难想：`a1b01e`  (01 这种不满足要求的)

```java
class Solution {
    public boolean validWordAbbreviation(String word, String abbr) {
        int i = 0, j = 0;
        while (i < word.length() && j < abbr.length()) {
            char ch = abbr.charAt(j);
            if (Character.isDigit(ch)) { // parse the number                
                int num = 0;                
                while (j < abbr.length() && Character.isDigit(abbr.charAt(j))) {
                    if (num == 0 && abbr.charAt(j) == '0') { // handle corner case: a1b01e
                        return false;
                    }
                    num = num * 10 + Character.getNumericValue(abbr.charAt(j));
                    j++;
                }
                i += num;
            } else {
                if (ch != word.charAt(i)) {
                    return false;
                }
                i++;
                j++;
            }
        }
        return i == word.length() && j == abbr.length();
    }
}
```
