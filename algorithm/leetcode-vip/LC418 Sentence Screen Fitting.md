
该题掌握程度：
- #很差❌

## 1. 题目
题目链接：[418. 屏幕可显示句子的数量 - 力扣（LeetCode）](https://leetcode.cn/problems/sentence-screen-fitting/description/)

Given a `rows x cols` screen and a `sentence` represented as a list of strings, return *the number of times the given sentence can be fitted on the screen*.

The order of words in the sentence must remain unchanged, and a word cannot be split into two lines. A single space must separate two consecutive words in a line.

 **Example 1:**

```
Input: sentence = ["hello","world"], rows = 2, cols = 8
Output: 1
Explanation:
hello---
world---
The character '-' signifies an empty space on the screen.
```

**Example 2:**

```
Input: sentence = ["a", "bcd", "e"], rows = 3, cols = 6
Output: 2
Explanation:
a-bcd- 
e-a---
bcd-e-
The character '-' signifies an empty space on the screen.
```

**Example 3:**

```
Input: sentence = ["i","had","apple","pie"], rows = 4, cols = 5
Output: 1
Explanation:
i-had
apple
pie-i
had--
The character '-' signifies an empty space on the screen.
```


**Constraints:**

- `1 <= sentence.length <= 100`
- `1 <= sentence[i].length <= 10`
- `sentence[i]` consists of lowercase English letters.
- `1 <= rows, cols <= 2 * 10^4`

## 2. 思路

1. **将句子视为环形字符串**：把句子拼接成一个循环字符串（末尾加空格作为分隔符），这样当排列到字符串末尾时可以自然回到开头继续排列。
2. **指针位置模拟排列**：
	- 用`start`指针表示当前排列到的位置（总是指向单词首字母）
	- 每行直接"消耗"`cols`个位置（`start += cols`）
	- 然后检查这个位置是否刚好落在单词分隔处：
		- 如果落在空格：说明正好完整排列，下一行从下一个单词开始
		- 如果落在单词中间：需要回退到当前单词的开头，把这个单词移到下一行
3. **最终计算完整循环次数**：总位移`start`除以句子字符串长度，得到完整重复的次数。

这种方法避免了逐词排列的低效，通过数学计算和位置调整快速确定能完整显示句子的次数。



拿一个例子来走一遍
```java
cols=5

012345678901234567890  before: start=0
I had apple pie I had apple pie I had apple pie 
     |
     start (1st line will fit: I had, so next line, it should start with apple, so start++)

012345678901234567890  before: start=6, after: start=12
I had apple pie I had apple pie I had apple pie 
           |
           start (2ed line will fit: apple, so next line, it should start with pie, so start++)     

012345678901234567890  before: start=12, after: 18
I had apple pie I had apple pie I had apple pie 
                 |
                 start (3rd line will fit: pie I, so next line, it should start with had, so start++)     

012345678901234567890123456789    before: start=18, after: 22
I had apple pie I had apple pie I had apple pie 
                       |
                       start (4th line will fit: had, so next line, it should start with apple, so move start to 22, begin of apple)    

repeat until all rows settled, then the start is the next word begin in next line.
*/
```

## 3. 最佳代码

```java
class Solution {
    public int wordsTyping(String[] sentence, int rows, int cols) {
        String words = String.join(" ", sentence) + " "; // end space: repeating whole strings need it
        int len = words.length();
        int start = 0; // 表示的含义是：每一行开头的单词的位置. start 总是指向一个 word 的首字母
        for (int i = 0; i < rows; i++) { // simulate each row, we put some words there
            start += cols;
            // check begin word for next row
            if (words.charAt(start % len) == ' ') { // 如果 start 是指向空格，则它下一个必然是字母。
                start++;
            } else { // 否则，说明 start 指向了字母，那么我们需要找到这个单词的首个字母，让后把它作为下面一行的第一个单词
                // the last word in current line can not fit in row: i
                while (start > 0 && words.charAt((start - 1) % len) != ' ') {
                    start--; // we need to find the word begin, and put it to next line
                }
            }
        }
        return start / len;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**： `O(L + rows × avg_word_length)`
	concate string takes O(L) where L = total length of all words + (n-1) spaces + 1 extra space (for the concatenated string).
	最坏情况下，`start` 回退的次数不超过 `cols`（因为每行最多回退 `cols` 次）。
- **空间复杂度**：O(L)
	`words` 字符串占用的空间为 `O(L)`，其中 `L` 是所有单词拼接后的长度（包括空格）。

## 4. 其他解法

### 4.1 解法2

```java
public class Solution {
    public int wordsTyping(String[] sentence, int rows, int cols) {
        String s = String.join(" ", sentence) + " ";
        int start = 0, l = s.length();
        for (int i = 0; i < rows; i++) {
            start += cols;
            if (s.charAt(start % l) == ' ') {
                start++;
            } else {
                while (start > 0 && s.charAt((start-1) % l) != ' ') {
                    start--;
                }
            }
        }
        
        return start / s.length();
    }
}
```

- time:  O(n x L + rows)



