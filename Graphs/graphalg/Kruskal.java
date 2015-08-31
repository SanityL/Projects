/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;


/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  	public static WUGraph minSpanTree(WUGraph g){
	  	WUGraph t = new WUGraph();
	  	Object[] vArray = g.getVertices();
	  	KruskalHashTable vHash = new KruskalHashTable(17);
	  	EdgeList eList = new EdgeList();
	  	Edge e;
	  	Neighbors n;
	  	int w = 0;

		for (Object v: vArray) {	//hash all vertices into index {0,1,2,3,4,5,6,7,8,9,10..., v}
			vHash.insert(v, w);
			w++;
		}
		w = 0;
		for (Object v: vArray) {	//walk through all vertices
			t.addVertex(v);
			n = g.getNeighbors(v);	//get neighbors of v
			for (int idx=0; idx <n.neighborList.length; idx++) {	//walk through v's neighbors
				if(((int) vHash.find(v).value()) <= ((int) vHash.find((n.neighborList)[idx]).value())){		//if the pairs has not already been added, add it now!
					eList.enlist(new Edge(v, (n.neighborList)[idx], (n.weightList)[idx]));
				}
			}
		}

		quickSort(eList);	//sort the edges

		DisjointSets vSet = new DisjointSets(g.edgeCount());  //create disjoint
		Entry first, second;
		while (!eList.isEmpty()){
			e = eList.delist();		//e is an Edge which has all the info. of an edge (ex: vertex1, vertex2, weight)
			first = vHash.find(e.v1);		//first and second are entries which has the hash value of vertex1 and vertex2
			second = vHash.find(e.v2);
			if (vSet.find(((int) first.value())) != vSet.find(((int) second.value()))){			//only do the union when the two indices have different roots 
				vSet.union(vSet.find(((int) first.value())), vSet.find(((int) second.value())));
				t.addEdge(e.v1, e.v2, e.weight);
			}
		}
		return t;
	}


	public static void partition(EdgeList qIn, Comparable pivot, 
                               EdgeList qSmall, EdgeList qEquals, 
                               EdgeList qLarge) {
    // Your solution here.
	    while (qIn.size() !=0) {
	        Edge item1 = qIn.delist();
	        if (((Comparable) item1.weight).compareTo(pivot) < 0) {
	          	qSmall.enlist(item1);
	        } else if (((Comparable) item1.weight).compareTo(pivot) == 0) {
	          	qEquals.enlist(item1);
	        } else {
	          	qLarge.enlist(item1);
	        }
	    }
    }

    public static void quickSort(EdgeList q) {
    // Your solution here.
	    if (q.size() != 0 && q.size() != 1) {
		    int random = ((int) (q.size() * Math.random()));
		    EdgeList small = new EdgeList();
		    EdgeList equals = new EdgeList();
		    EdgeList large = new EdgeList();
		    partition(q, (Comparable) q.nth(random).weight, small, equals, large);
		    quickSort(small);
		    quickSort(large);
		    q.append(small);
		    q.append(equals);
		    q.append(large);
	    }
	}

}
