
该题掌握程度：
- #一般⭕️

Tag: #hash #位运算

## 1. 题目
题目链接：[318. 最大单词长度乘积 - 力扣（LeetCode）](https://leetcode.cn/problems/maximum-product-of-word-lengths/)

Given a string array `words`, return *the maximum value of* `length(word[i]) * length(word[j])` *where the two words do not share common letters*. If no such two words exist, return `0`.

 

**Example 1:**

```
Input: words = ["abcw","baz","foo","bar","xtfn","abcdef"]
Output: 16
Explanation: The two words can be "abcw", "xtfn".
```

**Example 2:**

```
Input: words = ["a","ab","abc","d","cd","bcd","abcd"]
Output: 4
Explanation: The two words can be "ab", "cd".
```

**Example 3:**

```
Input: words = ["a","aa","aaa","aaaa"]
Output: 0
Explanation: No such pair of words.
```

 

**Constraints:**

- `2 <= words.length <= 1000`
- `1 <= words[i].length <= 1000`
- `words[i]` consists only of lowercase English letters.

## 2. 最佳思路

- 难点在于如何快速判断两个 word 是否包含相同的字母
- 因为 word 中都是小写字母，长度最长有 1000 个，因此可以使用 int 的 26 位来判断某个字母是否存在
- 判断相交（包含相同字母）时，可以用与操作，判断结果是否为 0，等于 0 说明没有相同字母
## 3. 最佳代码

```java
class Solution {
    public int maxProduct(String[] words) { // time: O(n^2)
        int n = words.length;
        int[] bitMasks = new int[n];
        for (int i = 0; i < n; i++) {
            bitMasks[i] = calculateBitMask(words[i]);
        }

        int maxAns = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((bitMasks[i] & bitMasks[j]) == 0) {
                    maxAns = Math.max(maxAns, words[i].length() * words[j].length());
                }
            }
        }
        return maxAns;
    }

    int calculateBitMask(String word) {
        int ans = 0;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            ans |= (1 << index);
        }
        return ans;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n^2)
  Preprocess the mask array takes O(nL). And iterate each pair of the words takes O(n^2). Since L <= n, the time is O(n^2).

- **空间复杂度**：O(n)
  

