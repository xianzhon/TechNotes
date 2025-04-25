
该题掌握程度：
- #很差❌

Tag:

## 1. 题目
题目链接：[486. 预测赢家 - 力扣（LeetCode）](https://leetcode.cn/problems/predict-the-winner/)

You are given an integer array `nums`. Two players are playing a game with this array: player 1 and player 2.

Player 1 and player 2 take turns, with player 1 starting first. Both players start the game with a score of `0`. At each turn, the player takes one of the numbers from either end of the array (i.e., `nums[0]` or `nums[nums.length - 1]`) which reduces the size of the array by `1`. The player adds the chosen number to their score. The game ends when there are no more elements in the array.

Return `true` if Player 1 can win the game. If the scores of both players are equal, then player 1 is still the winner, and you should also return `true`. You may assume that both players are playing optimally.

 

**Example 1:**

```
Input: nums = [1,5,2]
Output: false
Explanation: Initially, player 1 can choose between 1 and 2. 
If he chooses 2 (or 1), then player 2 can choose from 1 (or 2) and 5. If player 2 chooses 5, then player 1 will be left with 1 (or 2). 
So, final score of player 1 is 1 + 2 = 3, and player 2 is 5. 
Hence, player 1 will never be the winner and you need to return false.
```

**Example 2:**

```
Input: nums = [1,5,233,7]
Output: true
Explanation: Player 1 first chooses 1. Then player 2 has to choose between 5 and 7. No matter which number player 2 choose, player 1 can choose 233.
Finally, player 1 has more score (234) than player 2 (12), so you need to return True representing player1 can win.
```

 

**Constraints:**

- `1 <= nums.length <= 20`
- `0 <= nums[i] <= 107`

## 2. 最佳思路

- 还是很难想到这个DP的定义的

```
- dp[i][j] (i <= j) 表示针对nums[i..j] 这一段的玩法中,当前玩家与对手得分之差的最大值. 
- 递推公式：dp[i][j] = max{nums[i] - dp[i+1][j], nums[j] - dp[i][j-1]}
- 初始值：dp[i][i] = nums[i];  i = 0 ~ n-1
- answer：dp[0][n-1] >= 0
```



## 3. 最佳代码

```java
class Solution {
    public boolean predictTheWinner(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n]; // dp[i][j] (i <= j) 表示针对nums[i..j] 这一段的玩法中,当前玩家与对手得分之差的最大值. 
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                int scoreTakeStart = nums[i] - dp[i + 1][j];
                int scoreTakeEnd = nums[j] - dp[i][j-1];
                dp[i][j] = Math.max(scoreTakeStart, scoreTakeEnd);
            }
        }
        return dp[0][n - 1] >= 0;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n^2)
  总共有n^2个状态，每个状态计算之需要 O(1). 总的时间是 O(n^2)

- **空间复杂度**：O(n)



TODO: use top-down DFS + memo to achieve that?

## 其他解法



```java
class Solution {
    private int maxDiff(int[] nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }
        
        int scoreByLeft = nums[left] - maxDiff(nums, left + 1, right);
        int scoreByRight = nums[right] - maxDiff(nums, left, right - 1);
        
        return Math.max(scoreByLeft, scoreByRight);
    }
    
    public boolean predictTheWinner(int[] nums) {
        int n = nums.length;
        
        return maxDiff(nums, 0, n - 1) >= 0;
    }
}
```

- time: O(2^n). 
- space: O(n )
