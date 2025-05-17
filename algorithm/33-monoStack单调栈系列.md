单调栈的题目，往往能用 O(n) 的时间解决原本需要 O(n^2)时间的题目。比较难以理解，但是写法比较固定。

题单： [分享｜【算法题单】单调栈（矩形面积/贡献法/最小字典序）- 讨论 - 力扣（LeetCode）](https://leetcode.cn/discuss/post/3579480/ti-dan-dan-diao-zhan-ju-xing-xi-lie-zi-d-u4hk/)

技巧：
- 判断栈是递增还是递减，就看要求的是左边或者右边，比当前元素大的（栈顶到栈顶是递增），还是比当前元素小的（递减栈）。

## 练习题目
### 笔记：[LC456. 132 Pattern](leetcode/LC456.%20132%20Pattern.md)

```java
import java.util.Stack;

class Solution {
    public boolean find132pattern(int[] nums) {  // time: O(n), space: O(n)
        int n = nums.length;
        if (n < 3) return false;

        int[] minLeft = new int[n];
        minLeft[0] = nums[0];
        for (int i = 1; i < n; i++) {
            minLeft[i] = Math.min(minLeft[i - 1], nums[i]);
        }

        Stack<Integer> stack = new Stack<>();
        for (int j = n - 1; j >= 0; j--) {
            if (nums[j] > minLeft[j]) {
                while (!stack.isEmpty() && stack.peek() <= minLeft[j]) {
                    stack.pop();
                }
                if (!stack.isEmpty() && stack.peek() < nums[j]) {
                    return true;
                }
                stack.push(nums[j]);
            }
        }
        return false;
    }
}
```

### 笔记：[LC496. Next Greater Element I](leetcode/LC496.%20Next%20Greater%20Element%20I.md)
求每个元素右边第一个大于它的元素。栈中元素，从栈顶到栈底是递增的。

### 笔记：[LC503. Next Greater Element II](leetcode/LC503.%20Next%20Greater%20Element%20II.md)
循环数组。