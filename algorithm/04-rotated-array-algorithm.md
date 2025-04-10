# 旋转数组的二分搜索

旋转数组上的搜索，有时候很难想清楚，有的解法思路是一层if嵌套一层if的。本文总结了一下，找了一个比较容易理解，且不容易出错的方式来解决。

## 1. 二分搜索精确值

对于旋转数组来说，精确查找，需要分成两步。首先去找分界点，然后根据target在哪一段（通过判断 target >= A[0]）进行精确的二分查找。

### 1.1. [33. 搜索旋转排序数组 - 力扣（LeetCode）](https://leetcode.cn/problems/search-in-rotated-sorted-array/)

题目说明：一定是一个旋转数组，没有重复元素。

注意：如果不是旋转数组的话，有时需要特殊判断一下 A[0] < A[n-1]，本题的解法不需要特判，能覆盖这种情况。

```java
class Solution {
    public int search(int[] A, int target) { // time: O(log n), space: O(1)
        int n = A.length;
        // 第1步，找分界点（等价于找最大值所在的位置）
        int maxPos = findMaxElemPos(A);
        // 第2步，判断target在哪一段上升区间，使用标准的二分查找即可
        if (target >= A[0]) {
            return search(A, 0, maxPos, target);
        } else {
            return search(A, maxPos + 1, n - 1, target);
        }
    }

    int findMaxElemPos(int[] A) {
        int low = 0, high = A.length - 1;
        int ans = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (A[mid] >= A[0]) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    int search(int[] A, int low, int high, int target) {
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (A[mid] == target) return mid;
            else if (A[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }
}
```

### 1.2. [81. 搜索旋转排序数组 II - 力扣（LeetCode）](https://leetcode.cn/problems/search-in-rotated-sorted-array-ii/)

题目说明：与问题I相比，包含重复元素了。

思路：既然有重复的元素，画图之后，发现只有一种情况需要处理，起始时，左端和右端有相同的元素，这时候二分，不知道往那边移动。因此，我们可以人为去掉右边跟A[0]重复的元素，但是要注意保留1个，这样能方便处理，当所有元素相等时，至少还有一个元素的搜索空间。

```java
class Solution {
    public boolean search(int[] nums, int target) { // avg time: O(log n), worst: O(n), space: O(1)
        int high = nums.length - 1;
        
        // 注意这里使用 high > 0 是为了处理nums全相等的情况，保留1个元素。【否则要对nums[0] == target特判一下】
        while (high > 0 && nums[high] == nums[0]) high--;   // 与问题I的唯一区别
        
        int maxPos = findMaxElemPos(nums, high + 1);
        if (target >= nums[0]) {
            return search(nums, 0, maxPos, target);
        } else {
            return search(nums, maxPos + 1, high, target);
        }
    }

    int findMaxElemPos(int[] A, int n) {
        int low = 0, high = n - 1;
        int ans = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (A[mid] >= A[0]) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    boolean search(int[] A, int low, int high, int x) {
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (A[mid] == x) return true;
            else if (A[mid] < x) low = mid + 1;
            else high = mid - 1;
        }
        return false;
    }
}
```

## 2. 二分求最小值

这里本质是找分界点，除了找最小值，还可以找最大值。

### 2.1. [153. 寻找旋转排序数组中的最小值 - 力扣（LeetCode）](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/)

题目说明：元素各不相同，但是不保证旋转。因为旋转n次等于没有旋转。所以需要特判一下。

```java
class Solution {
    public int findMin(int[] A) {
        int n = A.length;
        if (n == 1 || A[0] < A[n-1]) return A[0]; // 特判: 只有一个元素，或者没有旋转的情况
        
        int low = 0, high = n - 1;
        int ans = -1;
        // 找满足A[i] >= A[0] 的最后一个i, 也就是分界点左边 (数组的最大值)
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (A[mid] >= A[0]) {
                // 已经找到一个满足 A[i] >= A[0]的位置mid, 先记下
                ans = mid;
                low = mid + 1;  // 继续往后找，看有没有更好的解
            } else {
                high = mid - 1;
            }
        }
        return A[ans + 1]; // 最小一定存在
    }
}
```

### 2.2. [154. 寻找旋转排序数组中的最小值 II - 力扣（LeetCode）](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array-ii/)

问题描述：跟问题I的区别在于，可能有重复的元素，也不保证一定旋转。

思路：去掉右边跟A[0]重复的元素，转换成问题I.

```java
class Solution {
    public int findMin(int[] A) {
        int n = A.length;        
        int low = 0, high = n - 1;
        
        // 去掉右边等于 A[0]的元素，注意保留1个，这样数组元素全部一样时，high指向下标0
        while (high > 0 && A[high] == A[0]) high--; 

        if (A[0] <= A[high]) { // 两种情况: high == 0, or A[0] < A[high] (没有旋转)
            return A[0];
        }

        // 因为 A[0] > A[high], 所以A[0~high] 一定有旋转
        int ans = -1;
        // 找满足A[i] >= A[0] 的最后一个i, 也就是分界点左边 (数组的最大值)
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (A[mid] >= A[0]) {
                // 已经找到一个满足 A[i] >= A[0]的位置mid, 先记下
                ans = mid;
                low = mid + 1;  // 继续往后找，看有没有更好的解
            } else {
                high = mid - 1;
            }
        }
        return A[ans + 1]; // 最小一定存在
    }
}
```