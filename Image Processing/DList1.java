/* DList1.java */

/**
 *  A DList1 is a mutable doubly-linked list.  (No sentinel, not
 *  circularly linked.)
 */

public class DList1 {


  protected DListNode1 head;
  protected DListNode1 tail;
  protected int size;

  public DList1() {
    head = null;
    tail = null;
    size = 0;
  }

  public DList1(int r, int g, int b, int rep) {
    head = new DListNode1(r, g, b, rep);
    tail = head;
    size++;
  }

  public void insertLast(int r, int g, int b, int rep) {
    if (head==null) {
      head = new DListNode1(r, g, b, rep);
      tail = head;
      size++;
    } else if (tail.red==r && tail.green==g && tail.blue==b) {
      tail.repeat++;
    } else {
      tail.next = new DListNode1(r, g, b, rep);
      tail.next.prev = tail;
      tail.next.next = null;
      tail = tail.next;
      size++;
    }
  }
}