/* ChipList.java */

package player;

/**
 *  A ChipList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel node at the head of the list.
 *
 *  DO NOT CHANGE ANY METHOD PROTOTYPES IN THIS FILE.
 **/

public class ChipList {
  
  protected int size;

  /**
   *  (ChipList)  size is the number of items in the list.
   *  head references the sentinel node.
   *  Note that the sentinel node does not store an item, and is not included
   *  in the count stored by the "size" field.
   *
   *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATION.
   **/

  protected ChipListNode head;

  /* ChipList invariants:
   *  1)  head != null.
   *  2)  For every ChipListNode x in a ChipList, x.next != null.
   *  3)  For every ChipListNode x in a ChipList, x.prev != null.
   *  4)  For every ChipListNode x in a ChipList, if x.next == y, then y.prev == x.
   *  5)  For every ChipListNode x in a ChipList, if x.prev == y, then y.next == x.
   *  6)  For every ChipList l, l.head.myList = null.  (Note that l.head is the
   *      sentinel.)
   *  7)  For every ChipListNode x in a ChipList l EXCEPT l.head (the sentinel),
   *      x.myList = l.
   *  8)  size is the number of ChipListNodes, NOT COUNTING the sentinel,
   *      that can be accessed from the sentinel (head) by a sequence of
   *      "next" references.
   **/

  /**
   *  newNode() calls the ChipListNode constructor.  Use this method to allocate
   *  new ChipListNodes rather than calling the ChipListNode constructor directly.
   *  That way, only this method need be overridden if a subclass of ChipList
   *  wants to use a different kind of node.
   *
   *  @param item the item to store in the node.
   *  @param list the list that owns this node.  (null for sentinels.)
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   **/
  protected ChipListNode newNode(int color, int x, int y, ChipList list,
                              ChipListNode prev, ChipListNode next) {
    return new ChipListNode(color, x, y, list, prev, next);
  }
  protected ChipListNode newNode(int color, Move m, ChipList list,
                              ChipListNode prev, ChipListNode next) {
    return new ChipListNode(color, m, list, prev, next);
  }

  /**
   *  ChipList() constructs for an empty ChipList.
   **/
  public ChipList() {
    head = newNode(0, 0, 0, null, null, null);
    head.next = head;
    head.prev = head;
  }

  /**
   *  insertFront() inserts an item at the front of this ChipList.
   *
   *  @param item is the item to be inserted.
   *
   *  Performance:  runs in O(1) time.
   **/
  public boolean hasNode(int color, int x, int y) throws InvalidNodeException {
    ChipListNode iter = front();
      while(iter.isValidNode()){
        if (iter.color == color && iter.x == x && iter.y == y){
          return true;
      }
      iter.next();
    }
    return false;
  }

  public void insertFront(int color, int x, int y) {

      ChipListNode newhead = newNode(color, x, y, this, head, head.next);
        head.next.prev = newhead;
        head.next = newhead;
        size++;
  }

  public void insertFront(int color, Move m) {

    ChipListNode newhead = newNode(color, m, this, head, head.next);
      head.next.prev = newhead;
      head.next = newhead;
      size++;
  }

  public boolean isEmpty(){
    return size == 0;
  }
   public int length(){
    return size;
   }

  /**
   *  insertBack() inserts an item at the back of this ChipList.
   **/
  public void insertBack(int color, int x, int y) {
      ChipListNode newback = newNode(color, x, y, this, head.prev, head);
      head.prev.next = newback;
      head.prev = newback;
      size++;
  }

  /**
   *  front() returns the node at the front of this ChipList.
   *
   *  @return a ListNode at the front of this ChipList.
   *
   *  Performance:  runs in O(1) time.
   */
  public ChipListNode front() {
    return head.next;
  }

  /**
   *  back() returns the node at the back of this ChipList.  
   *
   *  @return a ListNode at the back of this ChipList.
   *
   *  Performance:  runs in O(1) time.
   */
  public ChipListNode back() {
    return head.prev;
  }

  /**
   *  toString() returns a String representation of this ChipList.
   *
   *  @return a String representation of this ChipList.
   *
   *  Performance:  runs in O(n) time, where n is the length of the list.
   */
  public String toString() {
    ChipListNode current = head.next;
    String result = "[  " + current.color;
    while (current != head) {
      result = result +" (" + current.x + ", " + current.y + ") ";
      current = current.next;
    }
    return result + "]";
  }

  private static void testEmpty() {
    ChipList l = new ChipList();
    System.out.println("An empty list should be [  ]: " + l);
    System.out.println("l.isEmpty() should be true: " + l.isEmpty());
    System.out.println("l.length() should be 0: " + l.length());
    System.out.println("Finding front node p of l.");
    ChipListNode p = l.front();

    System.out.println("Finding back node p of l.");
    p = l.back();
    
    l.insertFront(new Integer(0),new Integer(8),new Integer(5));
    System.out.println("l after insertFront(10) should be [  10  ]: " + l);
  }

  public static void main(String[] argv) {
    testEmpty();
    ChipList l = new ChipList();
    l.insertFront(new Integer(0), new Integer(1),new Integer(2));
    l.insertFront(new Integer(0), new Integer(5),new Integer(4));
    l.insertFront(new Integer(0), new Integer(1),new Integer(6));
    System.out.println("l is a list of 3 chip: " + l);
    
  }
}
