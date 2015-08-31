package graphalg;


public class Edge {
	public Object v1, v2;
	public int weight;

	public Edge(Object v1, Object v2, int w) {
		this.v1 = v1;
		this.v2 = v2;
		weight = w;
	}

	public String toString() {
		String re = "[" + weight + "]";
		return re;
	}
}