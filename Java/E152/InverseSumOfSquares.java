import java.util.Hashtable;
import java.math.BigDecimal;
import java.util.Stack;



public class InverseSumOfSquares {
	
	Stack<Integer> inSum;
	double[] sums;
	int[] removed;
	double collectiveSum, target;
	int  max;
	Hashtable<Double, Integer> occurrenceOfSum;
	
	public InverseSumOfSquares(double target, int max, boolean test) {
		int[] removed = {6, 8, 9, 10, 11, 13, 14, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 29, 30, 31, 32, 33, 34};
		this.removed = removed;
		occurrenceOfSum = new Hashtable<Double, Integer>();
		this.max = max;
		this.target = target;
		sums = new double[max + 1];
		inSum = new Stack<Integer>();
		
		collectiveSum = 0;
		for(int i = max; i >= 2; i --) {
			if(i == max) {
				sums[i] = inverseOfSquare(i);
			} else {
				sums[i] = inverseOfSquare(i) + sums[i + 1];
			}
			collectiveSum += inverseOfSquare(i);
		}
	}
	
	int solve() {
		int count = 0;
		
		System.out.println("The collective sum is initially: " + collectiveSum);
		
		return count += solve(target, collectiveSum, 2, 0, true);
	}
	
	public int solve(double target, double collectiveSum, int index, int calls, boolean printCond) {
		int count = 0;
		double key = index - 1 + collectiveSum;
		
		//TODO: This inner loop doesn't seem to be incrementing properly
		for(int i = index; i <= max; i ++) {
			inSum.push(i);
			if(calls == 0) {
				System.out.println("At index: " + i);
			}
			double inverse = inverseOfSquare(i);
			
				printWithSpaces(calls, inSum.toString());
				printWithSpaces(calls, "When removing " + i + ", sum is: " + (collectiveSum - inverse) + " with a lower bound of " + (collectiveSum - sums[i]) + "\n");
				
		
			if(collectiveSum - sums[i] > target) {
				occurrenceOfSum.put(key, 0);
				inSum.pop();
				break;
			} else if(collectiveSum - inverse < target) {
				inSum.pop();
				continue;
			} else if(collectiveSum - inverse == target) {//If we've hit our target, increment the appropriate index in the hash table
//				System.out.println("Point 3");
				occurrenceOfSum.put(key, 1);
			} else if(occurrenceOfSum.get(key) != null) {//If the value is already in the hash table, just return it
				if(printCond)
					printWithSpaces(calls, "Getting from key: " + key + "\n");
				return occurrenceOfSum.get(key);
			} else {
					count += solve(target, collectiveSum - inverse, i + 1, calls + 1, printCond);
			}
			inSum.pop();
		}
		
		occurrenceOfSum.put(key, count);
		if(printCond)
			printWithSpaces(calls, "Put " + count + " to key: " + key + " at the end of the method\n");
		return count;
	}
	
	public void printWithSpaces(int spaces, String message) {
		for(int i = 0; i <= spaces; i ++) {
			System.out.print(" ");
		}
		System.out.print(message);
	}
	
	public double inverseOfSquare(int n) {
		return (double)(1 / (double)(n * n));
	}
	
	public BigDecimal inverseOfSquare(int n, boolean bleh) {
		String zeroes = "00000000000000000000000000000000000000000000000000";
		BigDecimal one = new BigDecimal("1." + zeroes);
		BigDecimal nSquare = new BigDecimal((n*n) + "." + zeroes);
		
		return one.divide(nSquare, BigDecimal.ROUND_HALF_EVEN);
	}
}
