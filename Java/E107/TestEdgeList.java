
public class TestEdgeList {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SpanningTree el = new SpanningTree("src/p107_network.txt", 40);
		//SpanningTree el = new SpanningTree(true);
		
		System.out.println("The answer is: " + el.solveMinSpanningTree());
	}

}
