
该题掌握程度：
- #一般⭕️

Tag: group by some rule. 设计哈希函数很巧妙。

## 1. 题目
题目链接：[249. 移位字符串分组 - 力扣（LeetCode）](https://leetcode.cn/problems/group-shifted-strings/description/)

Perform the following shift operations on a string:

- **Right shift**: Replace every letter with the **successive** letter of the English alphabet, where 'z' is replaced by 'a'. For example, `"abc"` can be right-shifted to `"bcd" `or `"xyz"` can be right-shifted to `"yza"`.
- **Left shift**: Replace every letter with the **preceding** letter of the English alphabet, where 'a' is replaced by 'z'. For example, `"bcd"` can be left-shifted to `"abc" or ``"yza"` can be left-shifted to `"xyz"`.

We can keep shifting the string in both directions to form an **endless** **shifting sequence**.

- For example, shift `"abc"` to form the sequence: `... <-> "abc" <-> "bcd" <-> ... <-> "xyz" <-> "yza" <-> ...`.` <-> "zab" <-> "abc" <-> ...`

You are given an array of strings `strings`, group together all `strings[i]` that belong to the same shifting sequence. You may return the answer in **any order**.

 

**Example 1:**

**Input:** strings = ["abc","bcd","acef","xyz","az","ba","a","z"]

**Output:** [["acef"],["a","z"],["abc","bcd","xyz"],["az","ba"]]

**Example 2:**

**Input:** strings = ["a"]

**Output:** [["a"]]

 

**Constraints:**

- `1 <= strings.length <= 200`
- `1 <= strings[i].length <= 50`
- `strings[i]` consists of lowercase English letters.

## 2. 最佳思路

- In each group, if we transfer the string to start with “a”, there must be some kind of shift strategy to do that.
- So if we could shift the words to start with “a”, we can easily group the strings.

## 3. 最佳代码

### Code 1

```java
class Solution {
    char shiftLetter(char letter, int shift) {
        return (char) ((letter - shift + 26) % 26 + 'a');
    }
    
    // Create a hash value. 关键就是将每个分组的字符串,转换成以 a 开始的字符串(移动相同次)
    String getHash(String s) {
        char[] chars = s.toCharArray();
        
        // Calculate the number of shifts to make the first character to be 'a'
        int shift = chars[0];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = shiftLetter(chars[i], shift);
        }
        
        String hashKey = String.valueOf(chars);
        return hashKey;
    }
    
    // time: O(n*k) n 个字符串,k 是字符串的最大长度
    public List<List<String>> groupStrings(String[] strings) {
        Map<String, List<String>> mapHashToList = new HashMap<>();
        
        // Create a hash_value (hashKey) for each string and append the string
        // to the list of hash values i.e. mapHashToList["abc"] = ["abc", "bcd"]
        for (String str : strings) {
            String hashKey = getHash(str);
            mapHashToList.putIfAbsent(hashKey, new ArrayList<>());
            mapHashToList.get(hashKey).add(str);
        }
        
        // Iterate over the map, and add the values to groups        
        return new ArrayList<>(mapHashToList.values());
    }
}
```



### Code 2 - another hash function

```java
class Solution {
    // Create a hash value
    String getHash(String s) {
        char[] chars = s.toCharArray();
        StringBuilder hashKey = new StringBuilder();
        
        for (int i = 1; i < chars.length; i++) {
            hashKey.append((char) ((chars[i] - chars[i - 1] + 26) % 26 + 'a'));
        }
        
        return hashKey.toString();
    }
    
    // time: O(n*k) n 个字符串,k 是字符串的最大长度
    public List<List<String>> groupStrings(String[] strings) {
        Map<String, List<String>> mapHashToList = new HashMap<>();
        
        // Create a hash_value (hashKey) for each string and append the string
        // to the list of hash values i.e. mapHashToList["abc"] = ["abc", "bcd"]
        for (String str : strings) {
            String hashKey = getHash(str);
            mapHashToList.putIfAbsent(hashKey, new ArrayList<>());
            mapHashToList.get(hashKey).add(str);
        }
        
        // Iterate over the map, and add the values to groups        
        return new ArrayList<>(mapHashToList.values());
    }
}
```



### 3.1 复杂度分析

- **时间复杂度**： O(n*k) n 个字符串,k 是字符串的最大长度
  
- **空间复杂度**：O(n * k)
