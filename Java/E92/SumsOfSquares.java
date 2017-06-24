/*
 * The key to this problem is the fact that numbers in chains will always be revisited
 * when examining other numbers and there's no point in re-treading a chain that's already been looked at.
 * 
 * For example, the chain starting with 2 goes 2 -> 4 -> 16 -> 37 -> 58 -> 89
 * So when examining the chain that starts with 2, we can simply take note of the fact that it and all the numbers
 * in its chain will converge on 89.
 *
 */
public class SumsOfSquares {
	private int[] oneOr89;
	int maxSumOfSquares;
	
	public SumsOfSquares() {
		maxSumOfSquares = 567;//Biggest possible sum of squares under 10 000 000
		
		oneOr89 = new int[maxSumOfSquares + 1];
		
		for(int i = 0; i < maxSumOfSquares; i ++ ) {
			oneOr89[i] = 0;
		}
	}

	public int solve() {
		int sum = 0;
		
		//Loop through all numbers from 1 to 10 million
		for(int i = 2; i < 10000000; i ++) {
			
			int end = getEnd(i);
			//Count up the ones that end at 89
			if(end == 89) {
				sum ++;
			}
		}
		return sum;
	}
	
	//Get the final Sum of Squares, which is either 1 or 89
	private int getEnd(int number) {
		int sumOfSquares;
		
		if(number > maxSumOfSquares || oneOr89[number] == 0) {
			sumOfSquares = getSumOfSquares(number);
			
			if(sumOfSquares == 1 || sumOfSquares == 89) {//If we've reached 1 or 89, return sumOfSquares(which is either 1 or 89)
				return sumOfSquares;
			} else {
				int end = getEnd(sumOfSquares);//Recursively call the method on the same one
				
				if(number < maxSumOfSquares) {
					oneOr89[number] = end;//Store the result in the array so it can be looked up later
				}
				
				return end;//Finally, return the result
			}
		} else {//If the number has been found, just return the value it ends at
			return oneOr89[number];
		}
	}
	
	//Get the sum of squares of a number
	//If 49 is entered, the method will return 4^2 + 9^2 = 16 + 81 = 97
	private int getSumOfSquares(int n) {
		int sum = 0;
		int number = n;
		int current;
		
		while(number != 0) {
			current = number % 10;
			sum += Math.pow(current, 2);
			number /= 10;
		}
		return sum;
	}
}
