/* EdgeList.java */
package graphalg;




public class EdgeList implements Queue {

  private SListNode head;
  private SListNode tail;
  private int size;

  /**
   *  EdgeList() constructs an empty list.
   **/
  public EdgeList() {
    size = 0;
    head = null;
    tail = null;
  }

  /** 
   *  size() returns the size of this list.
   *  @return the size of this list.
   *  Performance:  runs in O(1) time.
   **/
  public int size() {
    return size;
  }

  /**
   *  isEmpty() returns true if this list is empty, false otherwise.
   *  @return true if this list is empty, false otherwise. 
   *  Performance:  runs in O(1) time.
   **/
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   *  enlist() inserts an object at the end of the list.
   *  @param item the item to be enlistd.
   **/
  public void enlist(Edge item) {
    if (head == null) {
      head = new SListNode(item);
      tail = head;
    } else {
      tail.next = new SListNode(item);
      tail = tail.next;
    }
    size++;
  }

  /**
   *  delist() removes and returns the object at the front of the list.
   *  @return the item delisted.
   *  @throws a listEmptyException if the list is empty.
   **/
  public Edge delist() { 
    if (head == null) {
      System.out.println("Head is null.");
      System.exit(0);
    }
    Edge o = head.item;
    head = head.next;
    size--;
    if (size == 0) {
      tail = null;
    }
    return o;
  }

  /**
   *  front() returns the object at the front of the list.
   *  @return the item at the front of the list.
   *  @throws a listEmptyException if the list is empty.
   **/
  public Edge front() {
    if (head == null) {
      System.out.println("Head is null.");
      System.exit(0);
    } 
    return head.item;
  }

  /**
   *
   *  nth() returns the nth item in this EdgeList.
   *    Items in the list are numbered from 1.
   *  @param n the number of the item to return.
   */
  public Edge nth(int n) {
    SListNode node = head;
    for (; n > 1; n--) {
      node = node.next;
    }
    return node.item;
  }

  /**
   *  append() appends the contents of q onto the end of this EdgeList.
   *    On completion, q is empty.
   *  @param q the EdgeList whose contents should be appended onto this
   *    EdgeList.
   **/
  public void append(EdgeList q) {
    if (head == null) {
      head = q.head;
    } else {
      tail.next = q.head;
    }
    if (q.head != null) {
      tail = q.tail;
    }
    size = size + q.size;
    q.head = null;
    q.tail = null;
    q.size = 0;
  }

  /**
   *  toString() converts this list to a String.
   **/
  public String toString() {
    String out = "[ ";
      for (int i = 0; i < size(); i++) {
      	out = out + front() + " ";
      	enlist(delist());
      }
    return out + "]";
  }
}
