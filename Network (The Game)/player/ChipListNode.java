/* ChipListNode.java */

package player;

/**
 *  A ChipListNode is a mutable node in a ChipList (doubly-linked list).
 **/

public class ChipListNode{

  /**
   *  (inherited)  item references the item stored in the current node.
   *  (inherited)  myList references the List that contains this node.
   *  prev references the previous node in the ChipList.
   *  next references the next node in the ChipList.
   **/
  public int color;
  public int x;
  public int y;
  public Move move;
  public int score;
  protected ChipListNode prev;
  protected ChipListNode next;
  protected ChipList myList;

  /**
   *  ChipListNode() constructor.
   *  @param i the item to store in the node.
   *  @param l the list this node is in.
   *  @param p the node previous to this node.
   *  @param n the node following this node.
   */

  public ChipListNode() {
    prev = null;
    next = null;
    myList = null;

  }
  public ChipListNode(int color, int x, int y, ChipList l, ChipListNode p, ChipListNode n) {
    this.color = color;
    this.x = x;
    this.y = y;
    myList = l;
    prev = p;
    next = n;
  }
  public ChipListNode(int color, Move m, ChipList l, ChipListNode p, ChipListNode n) {
    this.color = color;
    this.move = m;
    myList = l;
    prev = p;
    next = n;
  }

  public ChipListNode(ChipListNode from, ChipListNode to) {
    move = new Move(from.x, from.y, to.x, to.y);
  }

  /**
   *  isValidNode returns true if this node is valid; false otherwise.
   *  An invalid node is represented by a `myList' field with the value null.
   *  Sentinel nodes are invalid, and nodes that don't belong to a list are
   *  also invalid.
   *
   *  @return true if this node is valid; false otherwise.
   *
   *  Performance:  runs in O(1) time.
   */
  public boolean isValidNode() {
    return myList != null;
  }

  /**
   *  next() returns the node following this node.  If this node is invalid,
   *  throws an exception.
   *
   *  @return the node following this node.
   *  @exception InvalidNodeException if this node is not valid.
   *
   *  Performance:  runs in O(1) time.
   */
  public ChipListNode next() throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException("next() called on invalid node");
    }
    return next;
  }

  /**
   *  prev() returns the node preceding this node.  If this node is invalid,
   *  throws an exception.
   *
   *  @param node the node whose predecessor is sought.
   *  @return the node preceding this node.
   *  @exception InvalidNodeException if this node is not valid.
   *
   *  Performance:  runs in O(1) time.
   */
  public ChipListNode prev() throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException("prev() called on invalid node");
    }
    return prev;
  }

  /**
   *  insertAfter() inserts an item immediately following this node.  If this
   *  node is invalid, throws an exception.
   *
   *  @param item the item to be inserted.
   *  @exception InvalidNodeException if this node is not valid.
   *
   *  Performance:  runs in O(1) time.
   */
  public void insertAfter(int color, int x, int y) throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException("insertAfter() called on invalid node");
    }
    // Your solution here.  Will look something like your Homework 4 solution,
    //   but changes are necessary.  For instance, there is no need to check if
    //   "this" is null.  Remember that this node's "myList" field tells you
    //   what ChipList it's in.  You should use myList.newNode() to create the
    //   new node.
      ChipListNode newafter = ((ChipList) myList).newNode(color, x, y, ((ChipList) myList), this, this.next);
      this.next.prev = newafter;
      this.next = newafter;
      myList.size++;
  }

  /**
   *  insertBefore() inserts an item immediately preceding this node.  If this
   *  node is invalid, throws an exception.
   *
   *  @param item the item to be inserted.
   *  @exception InvalidNodeException if this node is not valid.
   *
   *  Performance:  runs in O(1) time.
   */
  public void insertBefore(int color, int x, int y) throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException("insertBefore() called on invalid node");
    }
    // Your solution here.  Will look something like your Homework 4 solution,
    //   but changes are necessary.  For instance, there is no need to check if
    //   "this" is null.  Remember that this node's "myList" field tells you
    //   what ChipList it's in.  You should use myList.newNode() to create the
    //   new node.
      ChipListNode newbefore = ((ChipList) myList).newNode(color, x, y, ((ChipList) myList), this.prev, this);
      this.prev.next = newbefore;
      this.prev = newbefore;
      myList.size++;
  }

  /**
   *  remove() removes this node from its ChipList.  If this node is invalid,
   *  throws an exception.
   *
   *  @exception InvalidNodeException if this node is not valid.
   *
   *  Performance:  runs in O(1) time.
   */
  public void remove() throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException("remove() called on invalid node");
    }
    // Your solution here.  Will look something like your Homework 4 solution,
    //   but changes are necessary.  For instance, there is no need to check if
    //   "this" is null.  Remember that this node's "myList" field tells you
    //   what ChipList it's in.
      this.prev.next = this.next;
      this.next.prev = this.prev;

    // Make this node an invalid node, so it cannot be used to corrupt myList.
    myList.size--;
    myList = null;
    // Set other references to null to improve garbage collection.
    this.next = null;
    this.prev = null;
  }

}
