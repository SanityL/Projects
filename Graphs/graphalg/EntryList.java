package graphalg;
public class EntryList{


  protected EntryNode head;
  protected int size;

   /**
   *  @param item the item to store in the node.
   *  @param Entry the Entry that owns this node.  (null for sentinels.)
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   **/
  protected EntryNode newNode(Entry item, EntryList list,
                              EntryNode prev, EntryNode next) {
    return new EntryNode(item, list, prev, next);
  }
  public int getSize() {
  	return size;
  }

  public EntryList() {

    head = newNode(null, null, null, null);
    head.next = head;
    head.prev = head;
    size=0;
  }

  /**
   *  @param item is the item to be inserted.
   *
   **/
  public void insertFront(Entry item) {

    EntryNode hw = newNode(item, this, head, head.next);
    head.next.prev = hw;
    head.next = hw;
    size++;
  }

  /**
   *
   *  @param item is the item to be inserted.
   *
   **/
  public void insertBack(Entry item) {

    EntryNode soHard = newNode(item, this, head.prev, head);
    head.prev.next = soHard;
    head.prev = soHard;
    size++;
  }

  /**
   *
   *  DO NOT CHANGE THIS METHOD.
   *
   *  @return a EntryNode at the front of this Entry.
   *
   */
  public EntryNode front() {
    return head.next;
  }

  /**
   *
   *  @return a EntryNode at the back of this Entry.
   *
   */
  public EntryNode back() {
    return head.prev;
  }

  /**
   *
   *  @return a String representation of this Entry.
   */
  public String toString() {
    String result = "{ ";
    EntryNode current = head.next;
    while (current != head) {
      result = result + "( " + current.item.key + ": " + current.item.value + ") ";
      current = current.next;
    }
    return result + "}";
  }
 }