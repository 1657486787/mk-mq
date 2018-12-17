package com.suns.leetcode;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class Solution002 {
    public static class ListNode {
         int val;
         ListNode next;
         ListNode(int x) { val = x; }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return null;
    }

    /**
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     * Explanation: 342 + 465 = 807.
     * @param args
     */
    public static void main(String[] args) {
        int [] array1 = {2,4,3};
        ListNode l1 = null;
        for(int i=0;i<array1.length;i++){
            ListNode listNode = new ListNode(array1[i]);

        }
        new Solution002().addTwoNumbers(null,null);
    }
}