
## 该题掌握程度：
- #很差❌

## 1. 题目
题目链接：[247. 中心对称数 II - 力扣（LeetCode）](https://leetcode.cn/problems/strobogrammatic-number-ii/description/)

Given an integer `n`, return all the **strobogrammatic numbers** that are of length `n`. You may return the answer in **any order**.

A **strobogrammatic number** is a number that looks the same when rotated `180` degrees (looked at upside down).



**Example 1:**

```
Input: n = 2
Output: ["11","69","88","96"]
```

**Example 2:**

```
Input: n = 1
Output: ["0","1","8"]
```



**Constraints:**

- `1 <= n <= 14`

## 2. 最佳思路

- 通过中心点逐步往外扩展，每次添加一对字符（相同的 0、1、8，或成对的 6-9, 9-6）
- 需要根据 n 是奇数还是偶数分开讨论，如果是奇数，最中间必须要有一个字符，且只能是 0、1、8 的一种
- 要处理边界情况，0 不能作为开始，考虑最外层的一对时，要排除0

这个视频解说非常好： [贾考博 LeetCode 247. Strobogrammatic Number II 他还是不会念 - YouTube](https://www.youtube.com/watch?v=hM2WMOfht_g)
## 3. 最佳代码

```java
class Solution {
    char[][] pairMap = {{'0', '0'}, {'1', '1'}, {'8', '8'}, {'6', '9'}, {'9', '6'}};

    public List<String> findStrobogrammatic(int n) { // time: O(n * 5^(n/2)), space: O(n)
        List<String> ans = new ArrayList<>();
        if (n == 0) {
            return ans;
        }

        if (n % 2 == 0) {
            backtrack(ans, "", n);
        } else {
            backtrack(ans, "0", n - 1);
            backtrack(ans, "1", n - 1);
            backtrack(ans, "8", n - 1);
        }
        return ans;
    }

    void backtrack(List<String> ans, String current, int remain) {
        if (remain == 0) {
            ans.add(current);
            return;
        }
        for (char[] pair : pairMap) {
            if (remain == 2 && pair[0] == '0') {
                continue;
            }
            String newCurrent = String.valueOf(pair[0]) + current + String.valueOf(pair[1]);
            backtrack(ans, newCurrent, remain - 2);
        }
    }
}
/*
backtrack(current, remain)
    if (remain == 0) {
        add current to the result
        return;
    }
    // add pair around current
    5 cases: 0 0 / 1 1/ 8 8 / 6 9 / 9 6
        ignore illegal case: 0 0 && remain = 2.
*/
```

## 4. 总结

回溯法。但是与其他回溯的写法有区别，这里是要成对考虑，且是从中间往两边扩展。