
该题掌握程度：
- #熟练✓
tags: #二分搜索
## 1. 题目
题目链接：[274. H 指数 - 力扣（LeetCode）](https://leetcode.cn/problems/h-index/)

Given an array of integers `citations` where `citations[i]` is the number of citations a researcher received for their `ith` paper, return *the researcher's h-index*.

According to the [definition of h-index on Wikipedia](https://en.wikipedia.org/wiki/H-index): The h-index is defined as the maximum value of `h` such that the given researcher has published at least `h` papers that have each been cited at least `h` times.

**Example 1:**

```
Input: citations = [3,0,6,1,5]
Output: 3
Explanation: [3,0,6,1,5] means the researcher has 5 papers in total and each of them had received 3, 0, 6, 1, 5 citations respectively.
Since the researcher has 3 papers with at least 3 citations each and the remaining two with no more than 3 citations each, their h-index is 3.
```

**Example 2:**

```
Input: citations = [1,3,1]
Output: 1
```


**Constraints:**

- `n == citations.length`
- `1 <= n <= 5000`
- `0 <= citations[i] <= 1000`

## 2. 最佳思路

### 2.1 sort + binary search

- after sorted the array, we only need to find the left most pos to satisfy:  `A[pos] >= n - pos.` 

### 2.2 binary search without sort

- enumerate each possible answer, h-index: `[0, n]`. For each mid, we iterate the list to check whether it satisfies the definition of h-index. If we got more higher citations, we search answer in `[mid + 1, n]`. otherwise in `[0, mid - 1]`

### 2.3 count sort
- 这个答案有个特点，h-index 的范围是`[0, n]`. 我们可以采用计数排序的思路，统计每个值出现的次数
- counter 数组，长度n+1. 当 `A[i] >= n` 算到n 这一类，其他的就放到对应的下标处。
- 最后答案是从高到低遍历，`累加和 >= 下标`时，这个下标就是我们的答案。也可以从思路 1 的排序想到，那个复杂度完全取决于排序，因此换成计数排序会更快一点。


## 3. 最佳代码

### 3.1 sort + binary search

```java
class Solution {
    public int hIndex(int[] citations) {
        Arrays.sort(citations);
        int n = citations.length;

        // binary search to find left most pos s.t. A[mid] >= n - mid
        int low = 0, high = n - 1, ans = 0;       
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (citations[mid] >= n - mid) {
                ans = n - mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```

- **time**: O(n logn). sort the array takes O(nlogn), binary search takes O(log n).
- **space**: O(1). Should we count the space used in sort algorithm? If it is using quickSort, then it needs O(log n) stack space. If we use heap sort, the space is O(1).

Note: this approach would update the input array.

### 3.2 binary search directly

```java
class Solution {
    public int hIndex(int[] citations) {    
        int low = 0, high = citations.length; // h-index: [0, n]
        int ans = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1; // 逻辑右移，溢出时符号位不会跟着右移（用 0 填充，所以能处理和溢出的情况）
            if (checkHIndex(citations, mid)) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }

    boolean checkHIndex(int[] nums, int k) {
        int count = 0;
        for (int num : nums) {
            if (num >= k) {
                count++;
            }
        }
        return count >= k;
    }
}
```
- time: O(n logn). 
- space: O(1)
Note: better than approach 1.

### 3.3 count sort
```java
class Solution {
    public int hIndex(int[] nums) {
        int n = nums.length;
        int[] counter = new int[n + 1];
        for (int num : nums) {
            if (num >= n) {
                counter[n]++;
            } else {
                counter[num]++;
            }
        }
        // iterate from high to low
        int sum = 0;
        for (int i = n; i >= 0; i--) {
            sum += counter[i];
            if (sum >= i) {
                return i;
            }
        }
        return 0;
    }
}
/*
[3,0,6,1,5]

n = 5
0: 0
1: 1
2:
3: 1
4:
5: 2
*/
```

- **time**: O(n)
- **space**: O(n)

Note: The time is better than approach 1 & approach 2. 


## 关联的题

- [275. H 指数 II - 力扣（LeetCode）](https://leetcode.cn/problems/h-index-ii/) 给出的数组已经有序了，所以直接二分即可。

```java
class Solution {
    public int hIndex(int[] citations) {
        int low = 0, high = citations.length - 1, ans = 0;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            // find left-most pos s.t. citations[mid] >= n - mid
            if (citations[mid] >= citations.length - mid) {
                ans = citations.length - mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}
```