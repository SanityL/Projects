/* HashTableChained.java */

package dict;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  private EntryList[] defTable;
  private int bucketsLength;


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
     defTable = new EntryList[sizeEstimate];
     bucketsLength = sizeEstimate;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
    defTable = new EntryList[103];
    bucketsLength = 103;
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
    int hash;
    int a = 727;
    int b = 337;
    int p = 3499;
    hash = ((a*code+b) % p) % bucketsLength;
    if (hash < 0) {
      hash += bucketsLength;
    }
    return hash;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    int tot = 0;
    for (int i = 0; i < bucketsLength; i++){
      if (defTable[i] != null){
        tot += defTable[i].getSize();
      } 
    }
    return tot;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/
  public boolean isEmpty() {
    // Replace the following line with your solution.
    for (int i = 0; i < bucketsLength; i++){
      if (defTable[i] != null){
        return false;
      }
    }
    return true;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
    if (((double) size()) / ((double) bucketsLength) > 0.75) {
      System.out.println("here");
      reSize(true);
    }
    int index = compFunction(key.hashCode());
    Entry temp = new Entry();
    temp.key = key;
    temp.value = value;
    if (defTable[index] != null){
      defTable[index].insertBack(temp); 
    }else {
      EntryList newList = new EntryList();
      newList.insertFront(temp);
      defTable[index] = newList;
    }
    return temp;
  }


  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/


  public Entry find(Object key) {
    // Replace the following line with your solution.
    int index = compFunction(key.hashCode());
    if (defTable[index] != null) {
      EntryNode happy = defTable[index].front();
      while (happy.isValidNode()){
        if (happy.item.key().equals(key)){
          return happy.item;
        }
        happy = happy.next();
      }
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    if (((double) size()) / ((double) bucketsLength) < 0.25 && bucketsLength > 17) {
      reSize(false);
    }
    int index = compFunction(key.hashCode());
    if (defTable[index] != null) {
      EntryNode happy = defTable[index].front();
      while (happy.isValidNode()){
        if (happy.item.key().equals(key)){
          happy.remove();
          return happy.item;
        }
        happy = happy.next();
      }
    }
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    // Your solution here.
    for (int i = 0; i < bucketsLength; i++){
      defTable[i] = null;
    }
  }

  public void reSize(boolean enlarge) {
    int newLength = 0;
    if (enlarge) {
      newLength = bucketsLength * 2;
    }else{
      newLength = bucketsLength / 2;
    }

    EntryList[] newTable = new EntryList[newLength];
    EntryNode happy;
    for (int index=0; index<bucketsLength; index++) {
      if (defTable[index] != null) {
        happy = defTable[index].front();
        while (happy.isValidNode()){
          int idx = compFunction(happy.item.key().hashCode());
          Entry temp = new Entry();
          temp.key = happy.item.key();
          temp.value = happy.item.value();
          if (newTable[idx] != null){
            newTable[idx].insertBack(temp); 
          }else {
            EntryList newList = new EntryList();
            newList.insertFront(temp);
            newTable[idx] = newList;
          }
          happy = happy.next();
        }
      }
    }
    defTable = newTable;
    bucketsLength = newLength;
  }
}
