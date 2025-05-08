
该题掌握程度：
- #一般⭕️

Tag:

## 1. 题目
题目链接：[187. 重复的DNA序列 - 力扣（LeetCode）](https://leetcode.cn/problems/repeated-dna-sequences/description/)

The **DNA sequence** is composed of a series of nucleotides abbreviated as `'A'`, `'C'`, `'G'`, and `'T'`.

- For example, `"ACGAATTCCG"` is a **DNA sequence**.

When studying **DNA**, it is useful to identify repeated sequences within the DNA.

Given a string `s` that represents a **DNA sequence**, return all the **`10`-letter-long** sequences (substrings) that occur more than once in a DNA molecule. You may return the answer in **any order**.



**Example 1:**

```
Input: s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
Output: ["AAAAACCCCC","CCCCCAAAAA"]
```

**Example 2:**

```
Input: s = "AAAAAAAAAAAAA"
Output: ["AAAAAAAAAA"]
```



**Constraints:**

- `1 <= s.length <= 10^5`
- `s[i]` is either `'A'`, `'C'`, `'G'`, or `'T'`.

## 2. 最佳思路

- 字符重新进行二进制编码 + 固定大小的滑动窗口。
- 因为子串是 10 个字符，且字符集只有 4 个，所以可以映射成一个 int（相当于 hash 了一下）




## 3. 最佳代码

```java
class Solution {
    static final int L = 10;

    Map<Character, Integer> bin = new HashMap<>() {{
        put('A', 0b00);
        put('C', 0b01);
        put('G', 0b10);
        put('T', 0b11);
    }};

    public List<String> findRepeatedDnaSequences(String s) {
        List<String> ans = new ArrayList<>();
        int n = s.length();
        if (n <= L) {
            return ans;
        }
        int x = 0; // first window, enter L-1 char
        for (int i = 0; i < L - 1; i++) {
            x = (x << 2) | bin.get(s.charAt(i));
        }

        final int mask = ((1 << (L * 2)) - 1);
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int i = 0; i <= n - L; i++) { // enumerate each substring entry point
            char rightChar = s.charAt(i + L - 1);
            x = ((x << 2) | bin.get(rightChar)) & mask;
            freqMap.put(x, freqMap.getOrDefault(x, 0) + 1);
            if (freqMap.get(x) == 2) { // 技巧，刚超过 1 个时加到答案里去
                ans.add(s.substring(i, i + L));
            }
        }
        return ans;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：`O(n-L)`
- **空间复杂度**：`O(n-L)`

## 4. 其他解法

### 4.1 解法2 -- native solution

```java
class Solution {
    public List<String> findRepeatedDnaSequences(String s) {
        // time: O(n*L) L=10
        Map<String, Integer> freq = new HashMap<>();
        List<String> ans = new ArrayList<>();
        for (int i = 10; i <= s.length(); i++) { // 枚举每个串的终点（下一个位置）
            String subStr = s.substring(i - 10, i);
            int count = freq.getOrDefault(subStr, 0);
            freq.put(subStr, count + 1);
            if (count + 1 == 2) { // 为了不重复记录答案，我们只统计当前出现次数为 2 的子串. 很巧妙!
                ans.add(subStr);
            }
        }
        return ans;
    }
}
```

Or this way:
```java
class Solution {
    static final int L = 10;

    public List<String> findRepeatedDnaSequences(String s) {
        List<String> ans = new ArrayList<String>();
        Map<String, Integer> cnt = new HashMap<String, Integer>();
        int n = s.length();
        for (int i = 0; i <= n - L; ++i) {  // 枚举每个子串的起点
            String sub = s.substring(i, i + L);
            cnt.put(sub, cnt.getOrDefault(sub, 0) + 1);
            if (cnt.get(sub) == 2) {
                ans.add(sub);
            }
        }
        return ans;
    }
}
// 链接：https://leetcode.cn/problems/repeated-dna-sequences/solutions/1035568/zhong-fu-de-dnaxu-lie-by-leetcode-soluti-z8zn/
```
或者，更简短的写法：
```java
class Solution {

    public List<String> findRepeatedDnaSequences(String s) {
        int L = 10, n = s.length();
        HashSet<String> seen = new HashSet(), output = new HashSet();

        // iterate over all sequences of length L
        for (int start = 0; start < n - L + 1; ++start) {
            String tmp = s.substring(start, start + L);
            if (seen.add(tmp)) {
                output.add(tmp);
            }
        }
        return new ArrayList<String>(output);
    }
}
```
- time: O((n - L) * L).
- space: O((n-L) * L)



