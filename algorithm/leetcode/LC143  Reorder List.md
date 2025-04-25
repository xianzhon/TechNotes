
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[143. 重排链表 - 力扣（LeetCode）](https://leetcode.cn/problems/reorder-list/)
```
You are given the head of a singly linked-list. The list can be represented as:

L0 → L1 → … → Ln - 1 → Ln
Reorder the list to be on the following form:

L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
You may not modify the values in the list's nodes. Only nodes themselves may be changed.
```
## 2. 最佳思路

1. basic case: len <= 1, return
2. split list
3. reverse 2nd half
4. merge together
## 3. 最佳代码

```java
class Solution {
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode middle = findMiddle(head);
        ListNode rightHead = middle.next;
        middle.next = null;
        rightHead = reverseList(rightHead);

        // merge two list (head, rightHead)
        ListNode dummy = new ListNode(0);
        ListNode pre = dummy;
        while (head != null && rightHead != null) {
            pre.next = head;
            head = head.next;
            pre = pre.next;

            pre.next = rightHead;
            rightHead = rightHead.next;
            pre = pre.next;
        }
        pre.next = head != null ? head : rightHead;    
    }

    ListNode findMiddle(ListNode head) {
        // len >= 2
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    ListNode reverseList(ListNode head) {
        ListNode dummy = new ListNode(0);
        while (head != null) {
            ListNode next = head.next;
            head.next = dummy.next;
            dummy.next = head;
            head = next;
        }
        return dummy.next;
    }
}
```

### 3.1 复杂度分析

- **时间复杂度**：O(n)

- **空间复杂度**：O(1)

### 3.2 特别注意

- 算法思路：easy
- 实现细节：easy
- 算法证明：easy
- 时空复杂度分析：easy

