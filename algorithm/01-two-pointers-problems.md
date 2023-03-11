# 使用双指针算法解决两个 Leetcode 难题



双指针是一种常用的算法思想，通常用于解决数组或链表相关的问题。它的基本思路是使用两个指针在数据结构中扫描或移动，以达到解决问题的目的。根据两个指针移动的方向，可以分成同向双指针和反向双指针。

- 滑动窗口算法（同向双指针）：用两个指针维护一个滑动窗口，通过移动左右指针，可以在线性时间内解决一些字符串或数组的问题
- 双指针查找算法（反向双指针）：使用两个指针分别指向数组或链表的开头和结尾，通过不断移动指针来查找目标元素。
- 快慢指针算法（同向双指针）：使用两个指针分别以不同的速度移动，通常用于解决链表相关的问题，例如判断链表是否有环、查找链表中的中间节点等。



## 题目1： [42. 接雨水 - 力扣（LeetCode）](https://leetcode.cn/problems/trapping-rain-water/)

这个题目的基本思路是，对于每个柱子，假设它的左侧和右侧（可以不相邻）都有比它高的柱子 （leftMax, rightMax），那么雨水一定是可以覆盖当前柱子的，计算柱子A[i] 能接到的雨水，可以通过公式： min (leftMax, rightMax) - A[i]。那如果两侧有一个比当前A[i] 小会怎样，这个公式的结果也是 0，因为leftMax 和 rightMax容纳了A[i]，不会比 A[i] 自身更小 （当然也可以定义leftMax 和 rightMax 不包含 A[i]，严格的左侧和右侧最大值，计算公式加个 if 判断即可）。对于每个柱子，我们都可以这样求，然后累加就能知道所有柱子能接的雨水量了。

怎么求每个柱子左边和右边比它高的柱子呢？有两种思路，一种是 DP，分别从左右两侧遍历一遍，记录到数组中。一种是使用反向双指针，这种思路稍微不是很好想。

### 解法1：DP 时间-O(n), 空间-O(n)

对数组从两边分别遍历一次，记录每个元素左侧和右侧的最大值。然后再对每个柱子遍历一遍，累加总的雨水量。

```java
public int trap(int[] A) { // time: O(n), space: O(n)
    int n = A.length;
    int[] left = new int[n];
    int[] right = new int[n];

    left[0] = A[0]; // 统计每个A[i]左边最高的柱子
    for(int i = 1; i < n; i++) {
        left[i] = Math.max(left[i-1], A[i]);
    }

    right[n - 1] = A[n-1]; // 统计每个A[i]右边最高的柱子
    for(int i = n - 2; i >= 0; i--) {
        right[i] = Math.max(right[i+1], A[i]);
    }

    int ans = 0;   // 统计每个柱子能接到的雨水
    for(int i = 0; i < n; i++) {
        ans += Math.min(left[i], right[i]) - A[i];
    }
    return ans;
}
```

### 解法2：双指针 时间-O(n), 空间-O(1)

双指针，使用 left, right 反向双指针，然后使用leftMax, rightMax 两个变量记录两个指针在移动过程中访问的最大值（包括当前指针自身所在位置）。

- 如果A[left] < A[right] 则移动 left (++), 否则移动 right (--)，通过这种策略，可以得到每个柱子左右侧的最大值。
- 如果 A[left] < A[right]，那么一定有 leftMax < rightMax（①），因此 left 所在的柱子能接收的雨水，就是 leftMax - A[left]. 
  难点：如何证明①，虽然直觉上一定是对的。我们可以采用反证法，如果 leftMax 是在 left 的左边，且leftMax > rightMax的话，那么 left 就不应该移动，然后 right指针会一直往左边移动，越过当前的right所在位置，这跟当前的这个情形矛盾（right 不动，left 往右移动了）

```java
public int trap(int[] A) {
    // 初始化对撞指针 / 方向双指针
    int left = 0, right = A.length - 1;
    // 记录两个指针在移动过程中，访问的最大值
    int leftMax = 0, rightMax = 0;
    int ans = 0;
    
    while (left < right) {
         // 更新A[left]左侧 A[0 ~ left] 的最大值 (包括 left 位置)
        leftMax = Math.max(leftMax, A[left]);
        // 更新A[right]右侧 A[right ~ n-1] 的最大值 (包括 right 位置)
        rightMax = Math.max(rightMax, A[right]); 
        
        if (A[left] < A[right]) {  // 一定能推出：leftMax < rightMax
            ans += leftMax - A[left++];
        } else {
            ans += rightMax - A[right--];
        }
    }
    return ans;
}
```

说明：这个题目虽然也可以用单调栈来做，但是描述以及思维难度相当于上升了一个级别，而且空间复杂度是 O(n) 并不比双指针算法好。然后评论区，还看到过一种更短一点的代码，时空复杂度和双指针算法一样，但思维非常有跳跃性，难度比双指针更大，也不建议去看了。



## 题目2：[11. 盛最多水的容器 - 力扣（LeetCode）](https://leetcode.cn/problems/container-with-most-water/)

双指针的思路很简单，每次移动较小的那个柱子，计算当前盛水量并更新答案。

怎么证明这种思路能取得最优解呢？同样可以采用反正法。

```java
public int maxArea(int[] A) {
    // 初始化两个反向的双指针
    int i = 0, j = A.length - 1;
    int ans = 0;

    while (i < j) {
        // 对于每一种（i,j）组成的容器，计算当前盛水量并更新答案
        ans = Math.max(ans, (j-i) * Math.min(A[i], A[j]));
        if (A[i] < A[j]) {
            i++;
        } else {
            j--;
        }
    }
    return ans;
}
```





参考资料：

- [Array题型: 双指针Two Pointers套路【LeetCode刷题套路教程2】 – TuringPlanet](https://turingplanet.org/2020/05/20/array-two-pointers%e5%a5%97%e8%b7%af%e3%80%90leetcode%e5%88%b7%e9%a2%98%e5%a5%97%e8%b7%af%e6%95%99%e7%a8%8b2%e3%80%91/):  用视频和图文的形式详细介绍了双指针的类型和解题模板。
