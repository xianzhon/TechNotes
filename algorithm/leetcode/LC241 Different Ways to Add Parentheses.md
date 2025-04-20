## 题目

```
Given a string expression of numbers and operators, return all possible results from computing all the different possible ways to group numbers and operators. You may return the answer in any order.

The test cases are generated such that the output values fit in a 32-bit integer and the number of different results does not exceed 104.


Example 1:
	Input: expression = "2-1-1"
	Output: [0,2]
	Explanation:
	((2-1)-1) = 0
	(2-(1-1)) = 2

Example 2:
	Input: expression = "2*3-4*5"
	Output: [-34,-14,-10,-10,10]
	Explanation:
	(2*(3-(4*5))) = -34
	((2*3)-(4*5)) = -14
	((2*(3-4))*5) = -10
	(2*((3-4)*5)) = -10
	(((2*3)-4)*5) = 10


Constraints:
	1 <= expression.length <= 20
	expression consists of digits and the operator '+', '-', and '*'.
	All the integer values in the input expression are in the range [0, 99].
	The integer values in the input expression do not have a leading '-' or '+' denoting the sign.
```
[241. 为运算表达式设计优先级 - 力扣（LeetCode）](https://leetcode.cn/problems/different-ways-to-add-parentheses/)

## 思路

![image-20250420093009939](https://i.hish.top:8/2025/04/20/093010.png)

- 预处理：将 expression 解析成 token String数组，运算符和运算数都解析出来。
- 递归处理，在递归里枚举最后一次的运算符，然后将左右两部分分别递归去处理，得到结果，根据乘法原理，将左右两个部分的结果遍历，然后根据运算符得到计算结果，保存到当前递归的结果 List 里。递归的出口是，只有一个运算数，没有运算符，那么直接返回这个运算数即可。
- 相似的题：LC95.



## 代码1

知道思路之后，代码实现，也是需要一些技巧的，比如把整个 expression 分隔成 token，遍历运算符的时候，从第 2 个 token 开始，每次累加 2

```java
class Solution {
    public List<Integer> diffWaysToCompute(String expression) {
        List<String> tokens = new ArrayList<>();
        int n = expression.length();

        for(int i = 0; i < n; i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) { // parse the number
                int j = i;
                while (j < n && Character.isDigit(expression.charAt(j))) {
                    j++;
                }
                tokens.add(expression.substring(i, j));
                i = j - 1;
            } else { // operators
                tokens.add(String.valueOf(ch));
            }
        }

        // tokens must be in the form of: a,OP,b,OP,c,OP,d
        return dfs(tokens, 0, tokens.size() - 1);
    }

    List<Integer> dfs(List<String> tokens, int low, int high) {
        if (low == high) {
            return Arrays.asList(Integer.valueOf(tokens.get(low)));
        }
        // low < high, enumerate each operator
        List<Integer> ans = new ArrayList<>();
        for (int i = low + 1; i < high; i += 2) {
            // calculate left & right
            List<Integer> left = dfs(tokens, low, i - 1);
            List<Integer> right = dfs(tokens, i + 1, high);
            for (int x : left) {
                for (int y : right) {
                    String op = tokens.get(i);
                    if (op.equals("+")) {
                        ans.add(x + y);
                    } else if (op.equals("-")) {
                        ans.add(x - y);
                    } else {
                        ans.add(x * y);
                    }
                }
            }
        }
        return ans;
    }
}
```

- 时间复杂度：`O(2^n)`
  对于 m 个运算符，递归树的分支数是卡特兰数 `C(m)` ,每个分解点的组合数量是左右子问题的乘积，因此总的时间复杂度是 `O(C(m))`。
  由于 `C(m) =1/(m+1) * C(2m, m) = O(4^m / m^{1.5})`，且 `m = O(n)`，因此时间复杂度是 `O(4^{n/2}) = O(2^n)`。
  Leetcode AI 给出的分析结果是 `O(4^n * n)`
- 空间复杂度：`O(2^n)`
  需要 `O(n)` 的空间来存储分解后的 tokens。递归的深度最多为 `m`（运算符的数量），即 `O(m) = O(n)`。每次递归调用会生成一些中间列表，最坏情况下可能需要存储 `O(C(m))` 个结果，即 `O(4^m / m^{1.5})`。因此，空间复杂度也是 `O(4^m / m^{1.5})`，即 `O(2^n)`

## 代码 2 - 记忆化搜索 - 避免解决重复子问题

```java
class Solution {
    public List<Integer> diffWaysToCompute(String expression) {
        List<String> tokens = new ArrayList<>();
        int n = expression.length();

        for(int i = 0; i < n; i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) { // parse the number
                int j = i;
                while (j < n && Character.isDigit(expression.charAt(j))) {
                    j++;
                }
                tokens.add(expression.substring(i, j));
                i = j - 1;
            } else { // operators
                tokens.add(String.valueOf(ch));
            }
        }

        // tokens must be in the form of: a,OP,b,OP,c,OP,d
        memo = new ArrayList[tokens.size() + 1][ tokens.size() + 1];
        return dfs(tokens, 0, tokens.size() - 1);
    }

    private List<Integer>[][] memo;

    List<Integer> dfs(List<String> tokens, int low, int high) {
        if (low == high) {
            return Arrays.asList(Integer.valueOf(tokens.get(low)));
        }
        if (memo[low][high] != null) {
            return memo[low][high];
        }
        // low < high, enumerate each operator
        List<Integer> ans = new ArrayList<>();
        for (int i = low + 1; i < high; i += 2) {
            // calculate left & right
            List<Integer> left = dfs(tokens, low, i - 1);
            List<Integer> right = dfs(tokens, i + 1, high);
            for (int x : left) {
                for (int y : right) {
                    String op = tokens.get(i);
                    if (op.equals("+")) {
                        ans.add(x + y);
                    } else if (op.equals("-")) {
                        ans.add(x - y);
                    } else {
                        ans.add(x * y);
                    }
                }
            }
        }
        memo[low][high] = ans;
        return ans;
    }
}
```

- 时间复杂度：Leetcode AI 给出的分析结果是 `O(4^n)`

