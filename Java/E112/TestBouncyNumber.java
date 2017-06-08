
public class TestBouncyNumber {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long startTime = System.nanoTime();
		BouncyNumber bn = new BouncyNumber(.99);
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + " ns"); 

	}

}
