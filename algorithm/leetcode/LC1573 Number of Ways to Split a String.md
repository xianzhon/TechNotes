
该题掌握程度：
- #一般⭕️

## 1. 题目
题目链接：[1573. 分割字符串的方案数 - 力扣（LeetCode）](https://leetcode.cn/problems/number-of-ways-to-split-a-string/description/)

Given a binary string `s`, you can split `s` into 3 **non-empty** strings `s1`, `s2`, and `s3` where `s1 + s2 + s3 = s`.

Return the number of ways `s` can be split such that the number of ones is the same in `s1`, `s2`, and `s3`. Since the answer may be too large, return it **modulo** `10^9 + 7`.

**Example 1:**

```
Input: s = "10101"
Output: 4
Explanation: There are four ways to split s in 3 parts where each part contain the same number of letters '1'.
"1|010|1"
"1|01|01"
"10|10|1"
"10|1|01"
```

**Example 2:**

```
Input: s = "1001"
Output: 0
```

**Example 3:**

```
Input: s = "0000"
Output: 3
Explanation: There are three ways to split s in 3 parts.
"0|0|00"
"0|00|0"
"00|0|0"
```

**Constraints:**

- `3 <= s.length <= 10^5`
- `s[i]` is either `'0'` or `'1'`.

## 2. 思路

- 答案之所以要对这个数`10^9 + 7`求模，表示结果可能超过 int 的范围，所以要考虑用 long 来保存临时结果
- native solution：枚举两个划分的位置，然后利用前缀和判断是否满足条件，复杂度：O(n^2), 会超时。
- 更好的做法：总共有多少 1，可以遍历一遍，记作 count 个 1，如果count 不是 3 的倍数，那么答案是 0.
- 如果 count = 0，那么答案是：从 n 个 0 中划分，总共有 n-1 个位置，任选两个位置都能得到一个划分，所以答案是： C(n-1, 2) 
- 否则的话，前缀包含 count/3个 1，后缀包含count/3，那么找到这两个位置，然后扩展前缀或后缀，使得 1 的个数不变，那么总共的方案数就是：前缀的个数 * 后缀的个数。
- 写法1：为了方便找到 count/3所对应的 1 的位置，我们可以把 1 对应的下标全部保存下来。那么划分的位置就是: ones.size() / 3 - 1 (prefix包含这个位置)，和 `ones.size() * 2/3 - 1` （suffix包含这个位置）
- 写法2：知道需要多少 1 之后，直接扫描一遍，当第 1 部分满足条件后，统计第一个划分有多少种可能，当第 2 部分满足条件后，看第 2 个划分有多少种可能。

## 3. 最佳代码

### 写法 2 - 更好，空间是O(1)
参考其他答案。
trick: 统计 1 的个数。
```java
class Solution {
    public int numWays(String s) {
        final int MOD = 1000000007;
        int countOfOne = 0, n = s.length();
        for (int i = 0; i < n; i++) {
            countOfOne += s.charAt(i) - '0';
        }
        if (countOfOne % 3 != 0) {
            return 0;
        }
        if (countOfOne == 0) {
            return (int) ((n - 1L) * (n - 2L) / 2 % MOD);
        }
        int firstCutWays = 0, secondCutWays = 0;
        int oneCountRequired = countOfOne / 3, curCountOfOne = 0;
        for (int i = 0; i < n; i++) {
            curCountOfOne += s.charAt(i) - '0';
            if (curCountOfOne == oneCountRequired) {
                firstCutWays++;
            }
            if (curCountOfOne == oneCountRequired * 2) {
                secondCutWays++;
            }
        }
        long ans = ((long) firstCutWays * secondCutWays) % MOD;
        return (int) ans;
    }
}
```
- time: O(n), space: O(1)
### 写法 1

```java
class Solution {
    public int numWays(String s) {
        List<Integer> ones = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                ones.add(i);
            }
        }
        int count = ones.size();
        if (count % 3 != 0) {
            return 0;
        }
        final int MOD = 1000000007;
        if (count == 0) {
            long ans = (long) (s.length() - 1) * (s.length() - 2) / 2; // C(n - 1, 2).
            return (int) (ans % MOD);
        }
        int split1 = count / 3, split2 = count * 2 / 3; // split1/split2 是划分的后一个位置
        int leftPosCount = ones.get(split1) - ones.get(split1 - 1);
        int rightPosCount = ones.get(split2) - ones.get(split2 - 1);
        long ans = (long) leftPosCount * rightPosCount;
        return (int) (ans % MOD);
    }
}
```
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)



## 4. Follow up

Follow up: Can you generalize the problem with numbers between [-10^9, 10^9] such the sum between subarrays s1, s2, s3 are the same?

