
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接： https://leetcode.cn/problems/restore-ip-addresses/description/

## 2. 最佳思路
回溯。

## 3. 最佳代码

```java
class Solution {
    public List<String> restoreIpAddresses(String s) {
        ans = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>());
        return ans;
    }

    List<String> ans;

    void backtrack(String s, int index, List<String> chunks) {
        int n = s.length();
        if (index == n) {
            if (chunks.size() == 4) {
                ans.add(chunks.stream().collect(Collectors.joining(".")));
            }
            return;
        }
        if (chunks.size() > 4) {
            return;
        }
        for (int i = index + 1; i <= n; i++) {
            String chunk = s.substring(index, i);
            if (i > index + 1 && s.charAt(index) == '0') { // illegal, leading 0
                break;
            }
            int val = Integer.valueOf(chunk);
            if (val > 255) break;

            chunks.add(chunk);
            backtrack(s, i, chunks);
            chunks.remove(chunks.size() - 1);
        }
    }
}
/*
clarify:
- only digits
- if not valid, return empty list

backtrack
    check firstPart (1 digit, 2 digit, 3 digit)
    maintain a String list for existing chunks
    if meet the end, check chunks number, if valid, add to answer
*/
```

### 3.1 复杂度分析

- **时间复杂度**：O(3^SEG_COUNT * |s|)。 |s|是构造结果，并加入到结果 List 中的时间。SEG_COUNT = 4. 递归最大深度是 4.
- **空间复杂度**：O(SEG_COUNY) or O(s)

## 4. 其他解法

### 4.1 解法2 - 对前导 0 的小优化

```java
class Solution {
    public List<String> restoreIpAddresses(String s) {
        ans = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>());
        return ans;
    }
    List<String> ans;

    void backtrack(String s, int index, List<String> chunks) {
        int n = s.length();
        if (index == n) {
            if (chunks.size() == 4) {
                ans.add(chunks.stream().collect(Collectors.joining(".")));
            }
            return;
        }
        if (chunks.size() > 4) {
            return;
        }
        for (int i = index + 1; i <= n; i++) {
            String chunk = s.substring(index, i);
            int val = Integer.valueOf(chunk);
            if (val > 255) break;

            chunks.add(chunk);
            backtrack(s, i, chunks);
            chunks.remove(chunks.size() - 1);

		    if (s.charAt(index) == '0') { // illegal, leading 0
			    break;
		    }
        }
    }
}
```
