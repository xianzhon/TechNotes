# 区间相关的问题

对于区间相关的问题，因为涉及到两个端点，一段区间，在和循环关联在一起的时候，判断两个区间相交容易迷糊。这个笔记总结一下常见的区间的问题的思考方式。要多画图，然后把条件想清楚，再去用代码实现。

判断区间 A和 B 是否有交集： `!(A.end < B.start || A.start > B.end)  <==> A.end >= B.start && A.start <= B.end`

![](https://i.hish.top:8/2023/03/25/094302.png)

## 有序区间的插入

题目：[57. 插入区间 - 力扣（LeetCode）](https://leetcode.cn/problems/insert-interval/)

大概意思：就是给了一串区间，按照开始端点排好序了。现在需要插入一段新区间，如何有交集的话，则合并一下区间。  

思路：将新区间插入原来的区间，总共有3种情况，新区间左侧和右侧完全没有交集，中间有交集，需要特殊处理。  
那么算法就可以直接分成3步，先copy左边的，然后合并中间的，最后copy右边的。

![](https://i.hish.top:8/2023/03/25/085333.png)

```java
public int[][] insert(int[][] A, int[] newInterval) { // time: O(n), space: O(n)
    List<int[]> ans = new ArrayList<>();

    int start = newInterval[0], end = newInterval[1];
    int i = 0, n = A.length;

    // 左侧完全没有交集的部分  cur.end < start
    for(; i < n && A[i][1] < start; i++) {            
        ans.add(new int[] {A[i][0], A[i][1]});            
    }

    // 中间有交集的部分，需要合并 cur.start <= end
    if (i < n) {
        start = Math.min(start, A[i][0]);
        for(; i < n && A[i][0] <= end; i++) {
            end = Math.max(end, A[i][1]);
        }
    }
    ans.add(new int[] {start, end});

    // 右侧完全没有交集的部分 (cur.start > end)
    for(; i < n; i++) {
        ans.add(new int[] {A[i][0], A[i][1]});
    }
    return ans.toArray(new int[ans.size()][2]);
}
```

这个题，如果换成是 `List<int[]>`  然后要求原地插入，怎么做呢？ 一种方式是使用O(n)空间，使用上面的方式，时间是 O(n)。  
或者，直接遍历和操作List，这样时间复杂度是 O(n^2)。

```java
public void insertInterval(List<int[]> intervals, int[] newInterval) {     
    int i = 0, n = intervals.size();
    
    // 找到左侧边界小于新区间左侧边界的区间（一定没有交集的部分直接跳过）
    while (i < n && intervals.get(i)[1] < newInterval[0]) { // cur.end < start
        i++;
    }
    
    // 处理交集区间  (cur.end >= start && cur.start <= end)
    while (i < n && intervals.get(i)[0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals.get(i)[0]);
        newInterval[1] = Math.max(newInterval[1], intervals.get(i)[1]);
        intervals.remove(i); // 将已处理的区间从List中移除
        n--; // 更新List大小
    }
    
    // 将新区间加入List
    intervals.add(i, newInterval);
}
```

## 合并区间

题目： [56. 合并区间 - 力扣（LeetCode）](https://leetcode.cn/problems/merge-intervals/)

思路：

![](https://i.hish.top:8/2023/03/25/093237.png)

在具体写法上，因为要获取last.end，因此使用了ArrayDeque，它底层就是一个循环数组实现的双向队列，支持自动扩容。  
它提供的接口比较清晰, getLast()/ addLast()/ removeLast() 是在队尾操作，getFirst() / addFirst() / removeFirst()是在队头操作。

```java
    public int[][] merge(int[][] A) {
        Deque<int[]> deque = new ArrayDeque<>();
        Arrays.sort(A, (a, b) -> a[0] - b[0]); // Sort intervals by start
        for(int i = 0; i < A.length; i++) {
            if (i == 0 || deque.getLast()[1] < A[i][0]) { // no overlap, last.end < cur.start
                deque.addLast(new int[] {A[i][0], A[i][1]});
            } else {  // overlap: last.end >= cur.start
                int[] last = deque.getLast();
                last[1] = Math.max(last[1], A[i][1]);
            }
        }
        return deque.toArray(new int[deque.size()][2]);
    }
```

## 删除区间

题目：[435. 无重叠区间 - 力扣（LeetCode）](https://leetcode.cn/problems/non-overlapping-intervals/)

思路：

使用贪心来做，按区间结尾排序。贪心策略：优先保留结尾小且不相交的区间。 选择的区间结尾越小，留给剩余区间的空间就越大，就越能保留更多的区间。

```java
public int eraseOverlapIntervals(int[][] A) {  // time: O(nlogn), space: O(1)
    // assert A.length >= 1
    // 将区间按结束时间从小到大排序
    Arrays.sort(A, (a, b) -> a[1] - b[1]);

    int kept = 1, lastEnd = A[0][1]; // first one obviously be kept
    for(int i = 1; i < A.length; i++) {
        if (lastEnd <= A[i][0]) { // no overlap
            lastEnd = A[i][1];
            kept++;
        } // otherwise, overlap, abandom
    }
    return A.length - kept;
}
```