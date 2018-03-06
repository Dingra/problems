import java.math.BigInteger;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class TestInverseSumOfSquares {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigInteger one = new BigInteger("1");
		FactoredInteger two = new FactoredInteger(2, 1);
		FactoredFraction target = new FactoredFraction(one, two, null);
		
		long startTime = System.nanoTime();
		InverseSumOfSquares fs = new InverseSumOfSquares(80, target);
		System.out.println("The answer is " + fs.actuallySolve());
		long endTime = System.nanoTime();
		
		System.out.println("That took " + (endTime - startTime) + "ns");
		
	}

}
