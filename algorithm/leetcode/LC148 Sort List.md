
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[148. 排序链表 - 力扣（LeetCode）](https://leetcode.cn/problems/sort-list/description/)
```
Given the head of a linked list, return the list after sorting it in ascending order.

Follow up: Can you sort the linked list in O(n logn) time and O(1) memory (i.e. constant space)?
```
## 2. 最佳思路

```java
链表的排序：
	数组的O(nlogn)三个排序，快排，归并，堆排序
- 快排（以第一个作为pivot，分成两个） - OK，对于数组大部分已经升序的情况，这题会 TLE
- 归并：split，然后merge，也可以
- 堆排序
```
## 3. 最佳代码 - 归并排序

### 写法 2 - mergesort
```java
class Solution {
    public ListNode sortList(ListNode head) { // merge sort.
        if (head == null || head.next == null) { // base: at most 1 element
            return head;
        }
        ListNode middle = findMiddle(head);
        ListNode rightHead = middle.next;
        middle.next = null;
        head = sortList(head);
        rightHead = sortList(rightHead);
        return merge(head, rightHead);
    }

    ListNode findMiddle(ListNode head) {
        ListNode fast = head.next, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode pre = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                pre.next = l1;
                l1 = l1.next;
            } else {
                pre.next = l2;
                l2 = l2.next;
            }
            pre = pre.next;
        }
        pre.next = l1 != null ? l1 : l2;
        return dummy.next;
    }
}
/*
1 2 3 4 5
    |s
          |f
split: 1-n/2, n/2~n
recursively handle each part
merge together
*/
```

### 写法 1 - merge sort

```java
class Solution {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;

        // split in half
        ListNode secondHalf = splitList(head);
        ListNode firstHead = sortList(head);
        ListNode secondHead = sortList(secondHalf);
        return mergeList(firstHead, secondHead);
    }

    ListNode splitList(ListNode head) {
        ListNode fast = head, slow = head, pre = null;
        for(; fast != null && fast.next != null;) {
            fast = fast.next.next;
            pre = slow;
            slow = slow.next;
        }
        pre.next = null;  // 断开的情况，一定要是中点的前一半（偶数节点时，中间前一个）
        return slow;
    }

    ListNode mergeList(ListNode headA, ListNode headB) {
        ListNode dummy = new ListNode();
        ListNode pre = dummy;
        while (headA != null && headB != null) {
            if (headA.val < headB.val) {
                pre.next = headA;
                headA = headA.next;                
            } else {
                pre.next = headB;
                headB = headB.next;
            }
            pre = pre.next;
        }
        pre.next = headA != null ? headA : headB;
        return dummy.next;
    }
}
```

复杂度分析
- **时间复杂度**：O(n logn)
- **空间复杂度**：O(log n) 递归用的栈空间

## 4. 其他解法

### 4.1 解法2 - 自底向上的归并排序
可以做到空间复杂度： O(1). 很巧妙。TODO。
