/**
 * 
 * @author Drew
 * The objective in this problem was to find the number which began the longest collatz sequence
 * below one million.
 * 
 * I decided not to do a brute force solution for this one.  What I noticed was that every time
 * the sequence is computed for a number, it is computed for another one as well.
 * 
 * For example, the sequence starting with 5 is
 * 5->16->8->4->2->1
 * 
 * I see that the length of this sequence is 6, but I also see the lengths of the sequences beginning
 * with 16, 8, 4, 2 and 1.  I store these lengths in the array so they can be looked up later. This
 * saves a great deal of computations by eliminating the redundancy.
 * 
 * One difficulty of this method is the amount of space it takes to store EVERY number that is computed.
 * The second number in the sequence starting with 837797.  The next number in this sequence is 2513392,
 * which is over 1000000.  If I were to store this number, I'd need a array with a size of at least
 * 2513392, with much of that space not being used.  I decided not to store any number over 1000000 to
 * save space.
 * 
 * An interesting problem I encountered was the appearance of sequences starting with negative numbers.
 * I realised that n was exceeding the highest 32-big number of 2147483648 and wrapping around to negative
 * numbers.  The solution to this is simple: change "int" to "long"
 *
 */
public class E14 {
	
	static int[] numbers;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		numbers = new int[1000000];
		
		for(int i = 0; i < numbers.length; i ++){
			numbers[i] = -1;
		}
		
		
		for(int i = 1; i < 1000000; i ++){
			//System.out.println("i = " + i);
			if(numbers[i] == -1){//If that number has not been computed, compute it
				sequence(i, 0);
			}
			//System.out.println("Count: " + numbers[i]);
		}
		
		//System.out.println(sequence(15, 0));
		
		int max = 0;
		int best = 0;
		for(int i = 1; i < 1000000; i ++){
			if(max < numbers[i]){
				max = numbers[i];
				best = i;
			}
		}
		System.out.println("Max: " + best + " with " + max);

	}
	
	//Returns a sequence
	public static int sequence(long n, int count){
		//System.out.println("n = " + n);
		
		try{
			//base case
			if(n == 1){
				numbers[(int) n] = 1;
				return numbers[(int) n];
			}
		
			//If this one has already been computed, no need to compute it again
			else if(numbers[(int) n] != -1)
				return numbers[(int) n];
		
			else if((n%2) == 0){
				numbers[(int) n] = 1 + sequence(n/2, count + 1);
				return numbers[(int) n];
			}
		
			else{
				numbers[(int) n] = 1 + sequence(n*3 + 1, count + 1);
				return numbers[(int) n];
			}
		}catch (ArrayIndexOutOfBoundsException e){//If n is over 1000000
			//It cannot have been computed because there is no space for it in the array.
			if(n%2 == 0)
				return 1 + sequence(n/2, count + 1);
			else
				return 1 + sequence(n*3 + 1, count + 1);
		}

	}
}
