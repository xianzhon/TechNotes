
该题掌握程度：
- #很差❌

Tag: #backtrack #二进制思想

## 1. 题目
题目链接：[320. 列举单词的全部缩写 - 力扣（LeetCode）](https://leetcode.cn/problems/generalized-abbreviation/description/)

A word's **generalized abbreviation** can be constructed by taking any number of **non-overlapping** and **non-adjacent** substrings and replacing them with their respective lengths.

- For example, `abcde` can be abbreviated into:
	- `"a3e"` (`"bcd"` turned into `"3"`)
	- `"1bcd1"` (`"a"` and `"e"` both turned into `"1"`)
	- `"5"` (`"abcde"` turned into `"5"`)
	- `"abcde"` (no substrings replaced)
- However, these abbreviations are invalid
	- `"23"` (`"ab"` turned into `"2"` and `"cde"` turned into `"3"`) is invalid as the substrings chosen are adjacent.
	- `"22de"` (`"ab"` turned into `"2"` and `"bc"` turned into `"2"`) is invalid as the substring chosen overlap.

Given a string `word`, return *a list of all the possible **generalized abbreviations** of* `word`. Return the answer in **any order**.



**Example 1:**

```
Input: word = "word"
Output: ["4","3d","2r1","2rd","1o2","1o1d","1or1","1ord","w3","w2d","w1r1","w1rd","wo2","wo1d","wor1","word"]
```

**Example 2:**

```
Input: word = "a"
Output: ["1","a"]
```



**Constraints:**

- `1 <= word.length <= 15`
- `word` consists of only lowercase English letters.

## 2. 思路1 - backtrack

```java
backtrack, each letter has two choice:
    abbr
    not abbr

void backtrack(String word, int i, int abbrCount, StringBuilder path)
    keep word[i]:
        if (abbrCount > 0) path.add(abbrCount);
        path.add(word[i]);
        backtrack(word, i + 1, 0, path);

    not keep it:
        backtrack(word, i + 1, abbrCount + 1, path);
```

### code 1 - backtrack

```java
class Solution {
    public List<String> generateAbbreviations(String word) {
        ans = new ArrayList<>();
        backtrack(word, 0, 0, new StringBuilder());
        return ans;
    }

    List<String> ans;

    void backtrack(String word, int i, int abbrCount, StringBuilder path) {
        int len = path.length();
        if (i == word.length()) {
            if (abbrCount > 0) { // take care of this!! Otherwise may miss: '5' for 'abcde'
                path.append(abbrCount);
            }
            ans.add(path.toString());
            path.setLength(len); // must restore the path too!!
            return;
        }

        // abbreviate the letter word[i]
        backtrack(word, i + 1, abbrCount + 1, path);

        // keep letter
        if (abbrCount > 0) {
            path.append(abbrCount);
        }
        path.append(word.charAt(i));
        backtrack(word, i + 1, 0, path);

        // restore the path
        path.setLength(len);
    }
}
```

- **时间复杂度**：O(2^n * n) 总共有 n 个字符，每个字符两种选择，最后 copy String 到结果 List 中，需要 O(n)的时间。

- **空间复杂度**：O(n) 保存 path 的 StringBuilder 最多保存 n 个字符。
  .

## 思路 2  二进制的思想

###  写法 1 - binary trick

```java
class Solution {
    public List<String> generateAbbreviations(String word) {
        int n = word.length();
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < (1<<n); i++) {
            // construct the word abbr from binary i
            StringBuilder sb = new StringBuilder();
            int count = 0, x = i; // 0100 -> word:0100 => 1o2;  1010->word:0101 =>1o1d 不需要 reverse 了
            for (int j = 0; j < n; j++, x >>= 1) {
                if ((x & 1) == 1) { // bit 1
                    if (count > 0) {
                        sb.append(count);
                    }
                    sb.append(word.charAt(j));
                    count = 0;
                } else {
                    count++;
                }
            }
            if (count > 0) {
                sb.append(count);
            }
            ans.add(sb.toString());
        }
        return ans;
    }
}
/*
binary trick:
1. word has n chars, n <= 15, use lowest n bit in int.
2. the total count of n chars is 2^n. each letter has two choice, abbreviation or not.
3. e.g. 1000 -> w3.  1100 -> wo2
4. there is a trick, when iterate 0100, we can reverse it and encode the word as word:0010, which is: 2r1
*/
```


### 写法 2 - binary trick
```java
class Solution {
    public List<String> generateAbbreviations(String word) {
        int n = word.length();
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < (1<<n); i++) {
            // construct the word abbr
            StringBuilder sb = new StringBuilder();
            int count = 0;  // encoding 1000:word -> w3.  1100:word -> wo2
            for (int j = 0; j < n; j++) {
                int mask = 1 << (n - j - 1);
                if ((i & mask) == mask) { // bit 1.
                    if (count > 0) {
                        sb.append(count);
                    }
                    sb.append(word.charAt(j));
                    count = 0;
                } else {
                    count++;
                }
            }
            if (count > 0) {
                sb.append(count);
            }
            ans.add(sb.toString());
        }
        return ans;
    }
}
```
