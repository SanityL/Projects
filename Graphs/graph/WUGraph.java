/* WUGraph.java */

package graph;
import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  private int countVertex, countEdge;
  private DList vertexList;
  private HashTableChained vertexHashTable, edgeHashTable;
  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
    vertexHashTable = new HashTableChained(17);
    edgeHashTable = new HashTableChained(17);
    vertexList = new DList(); // A doubly-linked list with vertices
    countVertex = 0;
    countEdge = 0;
  }
  
  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount() {
    return countVertex;
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
    return countEdge;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
    Object[] arrayObject = new Object[countVertex];
    ListNode first = vertexList.front();
    int i = 0;
    try {
      while (first.isValidNode()) {
        arrayObject[i] = first.item();
        first = first.next();
        i++;
      }
    } catch (InvalidNodeException e) {
      System.out.println("Exception in getVertices()");
    }
    return arrayObject;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
    if (!isVertex(vertex)) { //check if the vertext is in the graph
      vertexList.insertBack(vertex);
      countVertex++;
      Entry entry = vertexHashTable.insert(vertex, vertexList.back());
    }
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    if (isVertex(vertex)) {
      ListNode removedV = (ListNode)vertexHashTable.find(vertex).value();
      List list = ((List)removedV.edgeList()); //Edge's List
      try {
        while (list.front().isValidNode()) {
          Edge e = (Edge)list.front().item();
          removeEdge(e.getV1(), e.getV2());
        }
        removedV.remove();
        Entry deletedV = vertexHashTable.remove(vertex);
        countVertex--;
      } catch (InvalidNodeException e) {
        System.out.println("Exception in removeVertex()");
      }
    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    if (vertexHashTable.find(vertex)==null) {
      return false;
    }
    return true;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
    if (isVertex(vertex)) {
      return ((ListNode)vertexHashTable.find(vertex).value()).edgeList().length();
    }
    return 0;
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
    if (isVertex(vertex) && degree(vertex)!=0) {
      Neighbors neighbor = new Neighbors();
      List eList = ((ListNode)vertexHashTable.find(vertex).value()).edgeList();
      ListNode firstEdge = eList.front();
      neighbor.neighborList = new Object[eList.length()];
      neighbor.weightList = new int[eList.length()];
      int i = 0;
      try {
        while (firstEdge.isValidNode()) {
          neighbor.neighborList[i] = ((Edge)firstEdge.item()).getV2();
          neighbor.weightList[i] = ((Edge)firstEdge.item()).getWeight();
          firstEdge = firstEdge.next();
          i++;
        }
      } catch (InvalidNodeException e) {
        System.out.println("Exception in getNeighbors()");
      }
      return neighbor;
    }
    return null;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
    if (isVertex(u) && isVertex(v)) {
      ListNode vertexU = (ListNode)vertexHashTable.find(u).value(); // A reference to the vertext DListNode
      ListNode vertexV = (ListNode)vertexHashTable.find(v).value();
      VertexPair vPair = new VertexPair(u, v);
      if (edgeHashTable.find(vPair)==null) { // Add the edge to the hashTable
        if (!u.equals(v)) { // They are not the same vertex
          Edge edge = new Edge(u, v, weight);
          Edge edge2 = new Edge(v, u, weight);
          vertexU.edgeList().insertBack(edge);
          vertexV.edgeList().insertBack(edge2);
          vertexU.edgeList().back().duplicate = vertexV.edgeList().back();
          vertexV.edgeList().back().duplicate = vertexU.edgeList().back();
          vertexU.edgeList().back().hasDuplicate = true;
          vertexV.edgeList().back().hasDuplicate = true;
          Entry entry = edgeHashTable.insert(edge.getPair(), vertexU.edgeList().back());
          countEdge++;
        } else { // They are the same vertex, so just need to add one edge to the edgeList
          Edge edge = new Edge(u, v, weight); 
          vertexU.edgeList().insertBack(edge);
          Entry entry = edgeHashTable.insert(edge.getPair(), vertexU.edgeList().back());
          countEdge++;
        }
      } else { // Increment the weight if found
        try {
          ((Edge)((ListNode)edgeHashTable.find(vPair).value()).item()).updateWeight(weight);
        } catch (InvalidNodeException e) {
          System.out.println("Exception in addEdge()");
        } 
      }
    }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    if (isEdge(u, v)) {
      VertexPair checkPair = new VertexPair(u, v);
      try {
        if (((ListNode)edgeHashTable.find(checkPair).value()).hasDuplicate) {
          ((ListNode)edgeHashTable.find(checkPair).value()).duplicate.remove();
          ((ListNode)edgeHashTable.find(checkPair).value()).remove();
        } else {
          ((ListNode)edgeHashTable.find(checkPair).value()).remove();
        }
        Entry removed = edgeHashTable.remove(checkPair);
      } catch (InvalidNodeException e) {
        System.out.println("Exception in removeEdge()");
      }
      countEdge--;
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
    if (isVertex(u) && isVertex(v)) {
      VertexPair checkPair = new VertexPair(u, v);
      if (edgeHashTable.find(checkPair)!=null) {
        return true;
      }
    }
    return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
    int w = 0;
    if (isEdge(u, v)) {
      VertexPair checkPair = new VertexPair(u, v);
      try {
        w = ((Edge)((ListNode)edgeHashTable.find(checkPair).value()).item()).getWeight();
      } catch (InvalidNodeException e) {
        System.out.println("Exception in weight()");
      }
    }
    return w;
  }
}