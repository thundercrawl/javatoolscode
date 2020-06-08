package scn.index.test;

import java.util.Stack;

class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }
 
public class AddTwo_2 {
	 public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		 ListNode head = new ListNode();
		 ListNode index = head;
		 Integer addOne = 0;
		 while(l1!=null ||l2!=null)
		 {
			 Integer n1 = l1==null?0:l1.val;
			 Integer n2 = l2==null?0:l2.val;
			 
			 if(n1+n2>=10)
			 {
				 index.val = n1+n2 -10+addOne;
				 addOne = 1;
			 }
			 else
			 {
				 index.val = n1+n2+addOne;
				 addOne = 0;
			 }

			 if(l1 !=null)
			 l1=l1.next;
             if(l2 != null)
			 l2=l2.next;
			 if(l1 == null && l2 == null)
			 {
				 if(addOne==1)
				 {
					 index.next = new ListNode();
					 index.next.val = 1;
				 }
			 }
			 else
			 {
				 index.next = new ListNode();
				 index = index.next; 
			 }
		 }
		 return head;
		 
	    }
	 
	 public  static void main(String[] args)
		{
		 ListNode l1 = new ListNode();
		 l1.val = 2;
		 l1.next = new ListNode();
		 l1.next.val = 4;
		 l1.next.next = new ListNode();
		 l1.next.next.val = 3;
		 
		 ListNode l2 = new ListNode();
		 l2.val = 5;
		 l2.next = new ListNode();
		 l2.next.val = 6;
		 l2.next.next = new ListNode();
		 l2.next.next.val = 4;
			new AddTwo_2().addTwoNumbers(l1, l2);
			
		}
}
