# 我的二分查找模板

基本的二分查找算法，比较容易懂，但是二分查找有很多变形，用于解决不同的算法问题。因此，针对每个问题，采用合适的二分查找算法才不容易出错。这里列出了我对二分查找算法的一些理解。

## 避免死循环

- 每次划分完，一定是要缩小解空间的，这样才不会造成死循环
- 需要注意：循环条件的写法 (start <= end, start + 1 < end)
- 取中间元素的写法： 统一:  mid = start + ((end - start) >> 1);
    - java中可以这么写: mid = (start + end) >>> 1;   使用逻辑右移运算符。
    - 为了可读性以及兼容其他语言通用写法： `mid = start + (end - start)/2`;

## 根据循环条件划分三种模板

### 模板1: low <= high

这种循环条件，保证二分的数组至少有一个元素，循环体中一定要缩短空间，改变low和high指针时，不要包含mid。

```java
int ans = -1;
while (low <= high) {
    int mid = low + (high - low) / 2;
    if (target <= A[mid]) {  // find first match
        ans = mid;
        high = mid - 1;
    } else {
        low = mid + 1;
    }
}
return ans;
```

### 模板2: low + 1 < high 

始终保证二分时，至少有三个元素，这样不管怎样缩减空间（例如：给low和high赋值为mid），都不会造成死循环。
缺点是精确查找时，循环结束，还会剩下两个元素，需要单独判断。

```java
while (low + 1 < high) {
   int mid = low + (high - low) / 2;
   if (A[mid] < A[mid+1]) {
       low = mid;
   } else {
       high = mid;
   }
}
```

### 模板3: low < high

至少有两个元素才进行二分，结束条件就是只有一个元素了 low==high，最后还剩下一个元素，精确查找需要单独判断一次。

```java
// 一定需要注意缩短空间的写法：(例如数组[1,2]，low=1,high=2, 取中点时采用了下取整，会倾向于左边，所以得到1)
while (low < high) {
    int mid = low + (high - low) / 2;
    if (xxx) { // remove left
        low = mid + 1;  // 注意这里，如果low=mid，加上下取整，可能造成死循环
    } else { // remove right part
        high = mid;
    }
}

// 或者： 例如数组[1,2]，low=1,high=2, 取中点时采用了上取整，会倾向于右边，所以得到2
while (low < high) {
    int mid = low + (high - low + 1) / 2;
    if (xxx) { // remove left
        low = mid;
    } else { // remove right part
        high = mid - 1;  // 注意这里，如果high=mid，加上上取整，可能造成死循环
    }
}
```

## 运用模板解决问题

- ① 在有序数组中，**精确查找**某个数，返回下标，不存在返回-1 【模板1】
- ② 在有序数组中，寻找**第一个满足条件**的位置，不存在返回-1 【模板1】
    - 例如：在有序数组中，找到第一个 >= target 的元素位置 (例如：查找插入位置）
    - 查找第一个等于target的元素位置
- ③ 在有序数组中，寻找**最后一个满足**条件的位置，不存在返回-1 【模板1】
    - 在有序数组中，找到最后一个 < target 的元素位置
    - 寻找最后一个等于 target的元素位置
- ④ 在有序数组中，寻找**最接近target**的元素位置 【模板2】
    - e.g. find peak

对于写法3，比较容易出错，考虑时，要多思考一步，因此尽量不要使用它。

## 练习题

### [34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/) - 模板1

```java
class Solution { // time: O(log n), space: O(1)
    int searchFirst(int[] nums, int target) {
        int left = 0, right = nums.length - 1, ans = -1;        
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (nums[mid] == target) {
                ans = mid;
                right = mid - 1; // 相等之后，仍然往左继续找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    int searchLast(int[] nums, int target) {
        int left = 0, right = nums.length - 1, ans = -1;        
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (nums[mid] == target) {
                ans = mid;
                left = mid + 1; // 相等之后，仍然往右继续找
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    public int[] searchRange(int[] nums, int target) {
        int left = searchFirst(nums, target);
        int right = searchLast(nums, target);
        return new int[] {left, right};
    }
}
```
### [35. 搜索插入位置](https://leetcode.cn/problems/search-insert-position/) -  模板1更好

思路：需要注意下ans的初始化。不过跟上面找最后一个元素是一样的。

```java
class Solution { // time: O(log n), space: O(1)
    public int searchInsert(int[] nums, int target) {
        if (nums == null) {
            return -1;
        }
        int start = 0, end = nums.length - 1;
        // 找到最后一个比 target 小的元素位置: ans
        int ans = -1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] < target) { // 找到一个满足条件的，不要停。继续move
                ans = mid;
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return ans + 1;
    }
}
```

### [28 · 搜索二维矩阵 - LintCode](https://www.lintcode.com/problem/28/) - 模板1

思路：这个题目，只能先按照第1列二分，找最后一个 <= target的位置，然后再在这一行进行二分。

```
     public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int m = matrix.length, n = matrix[0].length;
        // find last index <= target in first column
        int low = 0, high = m - 1;
        int ans = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (matrix[mid][0] == target) {
                return true;
            } else if (matrix[mid][0] < target) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (ans == -1) {
            return false;
        }

        low = 0;
        high = n - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (matrix[ans][mid] == target) {
                return true;
            } else if (matrix[ans][mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

```

### [38 · 搜索二维矩阵（二） - LintCode](https://www.lintcode.com/problem/38)

思路：这个题就是从右上角或者左下角开始，一次排除一行或一列。简单。思维有点急转弯。


### [74 · 第一个错误的代码版本 - LintCode](https://www.lintcode.com/problem/74/)

```java
    int firstBadVersion(int n) {
        int low = 1, high = n;
        int ans = 1;
        // find first bad version in range [1, n] inclusive, answer must be exist
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (isBadVersion(mid)) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
```

### [162. 寻找峰值](https://leetcode.cn/problems/find-peak-element/) - 模板2

思路：二分最后还剩下2个元素，只要数组中还剩下三个元素就继续二分。比较条件，就是判断mid 和 mid + 1的大小。

```java
   public int findPeakElement(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int low = 0, high = nums.length - 1;
        while (low + 1 < high) { // at least 3 elements between [low, high]
            int mid = low + (high - low) / 2;
            if (nums[mid] < nums[mid + 1]) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return nums[low] > nums[high] ? low : high;
    }
```


### [248 · Count of Smaller Number - LintCode](https://www.lintcode.com/problem/248/) - 模板1

思路：从有序数组中找第一个大于等于target的元素，需要注意ans的初始值。

```java
    public List<Integer> countOfSmallerNumber(int[] a, int[] queries) {
        List<Integer> ans = new ArrayList<>();
        if (a == null || a.length == 0) {
            for (int x : queries) {
                ans.add(0);
            }
            return ans;
        }
        Arrays.sort(a);
        for(int x : queries) {
            int count = findFirstGreatOrEqual(a, x);
            ans.add(count);
        }
        return ans;
    }

    private int findFirstGreatOrEqual(int[] a, int x) {
        int left = 0, right = a.length - 1;
        int ans = a.length; // 这里需要注意：表示while循环里没有任何匹配，也就是ans没有被赋值过。 ==> a[i] < x
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid] >= x) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
```


### [33. 搜索旋转排序数组](https://leetcode.cn/problems/search-in-rotated-sorted-array/) - 模板1

旋转数组，精确查找，返回下标。  

```java
输入：nums = [4,5,6,7,0,1,2], target = 0
输出：4

示例 2：
输入：nums = [4,5,6,7,0,1,2], target = 3
输出：-1
  
示例 3：
输入：nums = [1], target = 0
输出：-1
```

代码：
```java
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = ((left + right) >>> 1);
            if (target == nums[mid]) {
                return mid;
            }
            // check [left, mid] is sorted
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else { // [mid, right] is sorted
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }
```

