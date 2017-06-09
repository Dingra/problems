
public class testSumOfSquares {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SumsOfSquares sos = new SumsOfSquares();
		
		long startTime = System.nanoTime();
		System.out.println("The answer is: " + sos.solve());
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + " ns"); 
	}

}
