
public class Edge implements Comparable<Edge> {

	private int weight;//Weight of the edge
	private int vertex1, vertex2;//The two vertices that the edge connects
	
	public Edge(int v1, int v2, int w) {
		vertex1 = v1;
		vertex2 = v2;
		weight = w;
	}
	
	public int getWeight() {
		return weight;
	}
	
	//Removing an item from the array list takes time. Simply setting it to zero works just as well and is way faster
	public void setWeightZero() {
		weight = 0;
	}
	
	/**
	 * 
	 * @param Either 1 for v1 or 2 for v2, depending on which should be returned
	 * @return The vertex that should be returned, or -1 if a bad parameter was given
	 */
	public int getVertex(int v) {
		if(v == 1) return vertex1;
		else if(v ==2) return vertex2;
		else return -1;//Something went wrong
	}

	public int compareTo(Edge e) {
		// TODO Auto-generated method stub
		return e.getWeight() - weight;
	}
}
