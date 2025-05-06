
该题掌握程度：
- #一般⭕️

## 1. 题目

题目链接：[30. 串联所有单词的子串 - 力扣（LeetCode）](https://leetcode.cn/problems/substring-with-concatenation-of-all-words/description/?envType=problem-list-v2&envId=sliding-window)

You are given a string `s` and an array of strings `words`. All the strings of `words` are of **the same length**.

A **concatenated string** is a string that exactly contains all the strings of any permutation of `words` concatenated.

- For example, if `words = ["ab","cd","ef"]`, then `"abcdef"`, `"abefcd"`, `"cdabef"`, `"cdefab"`, `"efabcd"`, and `"efcdab"` are all concatenated strings. `"acdbef"` is not a concatenated string because it is not the concatenation of any permutation of `words`.

Return an array of *the starting indices* of all the concatenated substrings in `s`. You can return the answer in **any order**.

 

**Example 1:**

**Input:** s = "barfoothefoobarman", words = ["foo","bar"]

**Output:** [0,9]

**Explanation:**

The substring starting at 0 is `"barfoo"`. It is the concatenation of `["bar","foo"]` which is a permutation of `words`.
The substring starting at 9 is `"foobar"`. It is the concatenation of `["foo","bar"]` which is a permutation of `words`.

**Example 2:**

**Input:** s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"]

**Output:** []

**Explanation:**

There is no concatenated substring.

**Example 3:**

**Input:** s = "barfoofoobarthefoobarman", words = ["bar","foo","the"]

**Output:** [6,9,12]

**Explanation:**

The substring starting at 6 is `"foobarthe"`. It is the concatenation of `["foo","bar","the"]`.
The substring starting at 9 is `"barthefoo"`. It is the concatenation of `["bar","the","foo"]`.
The substring starting at 12 is `"thefoobar"`. It is the concatenation of `["the","foo","bar"]`.

 **Constraints:**

- `1 <= s.length <= 10^4`
- `1 <= words.length <= 5000`
- `1 <= words[i].length <= 30`
- `s` and `words[i]` consist of lowercase English letters.

## 2. 思路

- native idea: enumerate each substring with length of all words combined, then check whether it is the combination of words. Timeout.
- better idea:
	- 我们需要做的是，枚举每一个子串的起点，总共有 O(n)个。然后比较每个子串是否由所有单词组成
	- 对起点进行分类，按照 i % w 的余数进行分类
		- 第一类的起点有：0, w, 2w, 3w, 4w, ...
		- 第二类的起点有：1，w+1，2w+1, 3w+1, ...
		- 第w类的起点是：w-1, 2w-1, 3w-1, ....
	- 分类的好处是，对与每一类的起点来说，所有单词都是落在两个起点之间的，不会跨起点。然后把单词看成一个整体（一个 int），那么就转换成了一个固定窗口的滑动窗口问题
		- 拿第一类举例，对与0, w, 2w, 3w, ....来说，我们要找 m 个连续的点，使得它的内容与 words 里一样。
		- 如何判断两个内容一样？使用两个 hash 表，外加一个 count 来统计有效的 word 个数，当 count = words的长度时，就找到了一个子串，统计到答案中去


## 3. 最佳代码

```java
public List<Integer> findSubstring(String s, String[] words) {
        // 分组使用滑动窗口算法
        int n = s.length(), w = words[0].length(), m = words.length;
        Map<String, Integer> freq = new HashMap<>();
        for (String word : words) {
            freq.put(word, freq.getOrDefault(word, 0) + 1);
        }
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < w; i++) { // enumerate each group
            Map<String, Integer> curFreq = new HashMap<>();
            int activeCount = 0;
            // 这题的窗口有个特点，固定长度，所以写法更简单一点
            for (int j = i; j + w <= n; j += w) {
                // window begin: j - m*w
                // window end: j
                if (j >= i + m*w) {
                    // move one "group" out
                    String leftWord = s.substring(j - m * w, j - (m-1)*w);
                    if (curFreq.get(leftWord) <= freq.getOrDefault(leftWord, 0)) {
                        activeCount--;
                    }
                    curFreq.put(leftWord, curFreq.get(leftWord) - 1);                    
                }

                // add one in
                String rightWord = s.substring(j, j + w);
                curFreq.put(rightWord, curFreq.getOrDefault(rightWord, 0) + 1);
                if (curFreq.get(rightWord) <= freq.getOrDefault(rightWord, 0)) {
                    activeCount++;
                }

                if (activeCount == m) {
                    ans.add(j - (m - 1) * w);
                }
            }
        }
        return ans;
    }
```

时间复杂度：
- 对于每一组来说，总共有 n/w个起点位置，滑动窗口的算法是 O(n/w) 但是比较 word是否在 hash 表中，add/remove 需要O(w)的时间复杂度，所以一组的时间复杂度是: O(n)
- 总共有 w 组，所以总复杂度是：O(nw)
## 知识点

滑动窗口。取模进行分组的技巧。