
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[912. 排序数组 - 力扣（LeetCode）](https://leetcode.cn/problems/sort-an-array/)
```
Given an array of integers nums, sort the array in ascending order and return it.

You must solve the problem without using any built-in functions in O(nlog(n)) time complexity and with the smallest space complexity possible.
```
## 2. 最佳思路

- 快速排序（选择 pivot 要随机，否则 TLE）
- 堆排序（稍难）
- 归并排序（非常简单）

## 3. 最佳代码

### 归并排序

```java
class Solution {
    public int[] sortArray(int[] nums) {
        mergeSort(nums, 0, nums.length - 1);
        return nums;
    }

    void mergeSort(int[] nums, int low, int high) {
        if (low >= high) {
            return;
        }
        int mid = (low + high) >>> 1;
        mergeSort(nums, low, mid);
        mergeSort(nums, mid + 1, high);

        merge(nums, low, mid, high);
    }

    void merge(int[] nums, int low, int mid, int high) {
        int n = (high - low + 1);
        int[] tempNums = new int[n];
        int i = low, j = mid + 1, k = 0;
        while (i <= mid && j <= high) {
            tempNums[k++] = (nums[i] < nums[j]) ? nums[i++] : nums[j++];
        }
        while (i <= mid) {
            tempNums[k++] = nums[i++];
        }
        while (j <= high) {
            tempNums[k++] = nums[j++];
        }
        // copy back
        for (i = low, k = 0; i <= high; i++, k++) {
            nums[i] = tempNums[k];
        }
    }
}
```

### 堆排序

```java
class Solution {
    public int[] sortArray(int[] nums) {
	    // time: O(nlogn). siftDown takes O(log n), build heap takes: O(nlogn). adjustment takes: O(nlogn)	    
        int n = nums.length;
        // build big-root heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(nums, i, n);
        }

        // extract root and adjust heap
        for (int i = 0; i < n - 1; i++) {
            swap(nums, 0, n - i - 1);
            siftDown(nums, 0, n - i - 1);
        }
        return nums;
    }

    void siftDown(int[] nums, int i, int n) {
        while (i < n) {
            int left = 2 * i + 1, right = left + 1;
            int maxIdx = i;
            if (left < n && nums[maxIdx] < nums[left]) {
                maxIdx = left;
            }
            if (right < n && nums[maxIdx] < nums[right]) {
                maxIdx = right;
            }
            if (maxIdx == i) {
                break;
            }
            swap(nums, maxIdx, i);
            i = maxIdx;
        }
    }

    void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
}
```

### 快速排序

```java
class Solution {
    public int[] sortArray(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
        return nums;
    }

    void quickSort(int[] nums, int low, int high) {
        if (low >= high) {
            return;
        }
        int index = partition(nums, low, high);
        quickSort(nums, low, index - 1);
        quickSort(nums, index + 1, high);
    }

    Random random = new Random();
    int partition(int[] nums, int low, int high) {
        int pivot = random.nextInt(high - low + 1) + low;
        swap(nums, pivot, high);
        int small = low - 1;
        for (int i = low; i < high; i++) {
            if (nums[i] < nums[high]) {
                swap(nums, ++small, i);
            }
        }
        swap(nums, ++small, high);
        return small;
    }

    void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：都是 O(n logn)
  heap sort: siftDown 是 O(log n)
  merge sort: merge O(n)
  quickSort: partition: O(n)

## 4. 相关联的题目

- merge 的应用，求逆序对的个数
- partition 函数的应用，求第 k 小的元素




