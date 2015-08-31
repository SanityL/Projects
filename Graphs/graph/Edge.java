package graph;

public class Edge {

	private VertexPair pair;
	private int weight;
	private Object vertex1;
	private Object vertex2;

	public Edge(Object v1, Object v2, int w) {
		pair = new VertexPair(v1, v2);
		weight = w;
		vertex1 = v1;
		vertex2 = v2;
	}

	public VertexPair getPair() { //Get the vertex pair
		return pair;
	}

	public int getWeight() {
		return weight;
	}

	public void updateWeight(int w) { //Update the weight
		weight = w;
	}

	public Object getV1() {
		return vertex1;
	}

	public Object getV2() {
		return vertex2;
	}
}
