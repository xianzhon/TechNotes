
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[445. 两数相加 II - 力扣（LeetCode）](https://leetcode.cn/problems/add-two-numbers-ii/)
```
You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

Example 1:
	Input: l1 = [7,2,4,3], l2 = [5,6,4]
	Output: [7,8,0,7]    《==  3243 + 564 = 7807

Example 2:
	
	Input: l1 = [2,4,3], l2 = [5,6,4]
	Output: [8,0,7]
	Example 3:
	
	Input: l1 = [0], l2 = [0]
	Output: [0]
 

Constraints:

	The number of nodes in each linked list is in the range [1, 100].
	0 <= Node.val <= 9
	It is guaranteed that the list represents a number that does not have leading zeros.
 
Follow up: Could you solve it without reversing the input lists? 
```
## 2. 最佳思路

1. reverse list first, to handle least significant digit first.
2. simulate add digit by digit, take care of carry. unless all list is null and carry is 0
3. reverse the sum list and return

## 3. 最佳代码

### version 2
```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        
        l1 = reverseList(l1);
        l2 = reverseList(l2);
        int carry = 0;
        ListNode p = l1, q = l2;
        ListNode dummy = new ListNode(0);

        while (p != null || q != null || carry != 0) {
            int sum = carry;
            if (p != null) {
                sum += p.val;
                p = p.next;
            }
            if (q != null) {
                sum += q.val;
                q = q.next;
            }

            // insert sum in reverse order, so no need to reverse again
            ListNode node = new ListNode(sum % 10);
            node.next = dummy.next;
            dummy.next = node;

            carry = sum / 10;
        }

        reverseList(l1);
        reverseList(l2);

        return dummy.next;
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


### version 1 - 还能优化

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        
        l1 = reverseList(l1);
        l2 = reverseList(l2);
        int carry = 0;
        ListNode p = l1, q = l2;
        ListNode dummy = new ListNode(0);
        ListNode pre = dummy;

        while (p != null || q != null || carry != 0) {
            int sum = carry;
            if (p != null) {
                sum += p.val;
                p = p.next;
            }
            if (q != null) {
                sum += q.val;
                q = q.next;
            }
            pre.next = new ListNode(sum % 10);
            carry = sum / 10;
            pre = pre.next;
        }

        reverseList(l1);
        reverseList(l2);

        return reverseList(dummy.next);
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
1. the answer list, we can just insert in reversed order.

### 3.1 复杂度分析

- **时间复杂度**：O(n)

- **空间复杂度**：O(1)

### 3.2 特别注意

- 算法思路：easy
- 实现细节：easy
- 算法证明：easy
- 时空复杂度分析：easy

## 4. 其他解法

### 4.1 解法2 - use stack to avoid mutate input list

```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Deque<Integer> numStack1 = new ArrayDeque<>();
        Deque<Integer> numStack2 = new ArrayDeque<>();

        for (ListNode cur = l1; cur != null; cur = cur.next) {
            numStack1.push(cur.val);
        }
        for (ListNode cur = l2; cur != null; cur = cur.next) {
            numStack2.push(cur.val);
        }

        // simulate addition
        int carry = 0;
        ListNode dummy = new ListNode(0);
        while (!numStack1.isEmpty() || !numStack2.isEmpty() || carry != 0) {
            int sum = carry;
            if (!numStack1.isEmpty()) {
                sum += numStack1.pop();
            }
            if (!numStack2.isEmpty()) {
                sum += numStack2.pop();
            }
            ListNode cur = new ListNode(sum % 10);
            cur.next = dummy.next;
            dummy.next = cur;

            carry = sum / 10;            
        }
        return dummy.next;
    }
}
```

Follow up: Could you solve it without reversing the input lists? 
使用 Stack 的方式，可以避免修改输入的数组。好处是多线程环境下，这种代码更加安全一点。

## 5. 相关联的题目

basic version:  [2. 两数相加 - 力扣（LeetCode）](https://leetcode.cn/problems/add-two-numbers/description/)




