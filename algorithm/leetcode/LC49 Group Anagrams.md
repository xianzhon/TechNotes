
该题掌握程度：
- #一般⭕️

Tag: #hash

## 1. 题目
题目链接：[49. 字母异位词分组 - 力扣（LeetCode）](https://leetcode.cn/problems/group-anagrams/description/)

Given an array of strings `strs`, group the anagrams together. You can return the answer in **any order**.

 

**Example 1:**

**Input:** strs = ["eat","tea","tan","ate","nat","bat"]

**Output:** [["bat"],["nat","tan"],["ate","eat","tea"]]

**Explanation:**

- There is no string in strs that can be rearranged to form `"bat"`.
- The strings `"nat"` and `"tan"` are anagrams as they can be rearranged to form each other.
- The strings `"ate"`, `"eat"`, and `"tea"` are anagrams as they can be rearranged to form each other.

**Example 2:**

**Input:** strs = [""]

**Output:** [[""]]

**Example 3:**

**Input:** strs = ["a"]

**Output:** [["a"]]

 

**Constraints:**

- `1 <= strs.length <= 104`
- `0 <= strs[i].length <= 100`
- `strs[i]` consists of lowercase English letters.



## 2. 最佳思路

- 针对每个 string，找一个 hash 函数，让每一组（anagram）得到的 hash 一样。这样就能放到同一组里去
- 问题转化成，如何设计一个 hash 函数。anagram的特点是，两个string 用到的字符种类和个数一模一样。
	- hash 1： 排序
	- hash 2：统计字符出现频率，构造 hash 字符串

## 3. 最佳代码

### Code 1 - 利用 char freq 构造 hash - 非常巧妙
```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) { // time: O(nklog(k)), space: O(nk)
        Map<String, List<String>> map = new HashMap<>();
        for(String str : strs) {
            String hash = getFreqHash(str);
            map.putIfAbsent(hash, new ArrayList<>());
            map.get(hash).add(str);
        }
        return new ArrayList<>(map.values());
    }

    String getFreqHash(String str) { // since it only contains lowercase letter        
        char[] freq = new char[26];  // and strs[i].length <= 100
        for (int i = 0; i < str.length(); i++) {
            freq[str.charAt(i) - 'a']++;
        }
        return new String(freq);
    }
}
```
- **时间复杂度**： O(n k)
  k is the max-length of the string. n is the number of string.
- **空间复杂度**：O(n k)

### Code 2 - 排序

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) { // time: O(nklog(k)), space: O(nk)
        Map<String, List<String>> map = new HashMap<>();
        for(String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String hash = new String(chars);
            map.putIfAbsent(hash, new ArrayList<>());
            map.get(hash).add(str);
        }
        return new ArrayList<>(map.values());
    }
}
```

- **时间复杂度**： O(n klog(k))
  k is the max-length of the string. n is the number of string.
- **空间复杂度**：O(nk)

## Similar

[LC249 Group Shifted Strings](../leetcode-vip/LC249%20Group%20Shifted%20Strings.md)