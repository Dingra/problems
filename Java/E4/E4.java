/**
 * 
 * @author Drew Ingram
 * 
 *	This is a challenge to return the largest palindrome which is a multiple of
 *	two three-digit numbers.  I initially started doing this problem in Scheme, but I decided
 *	to switch to Java because storing the digits in an array makes it far easier to compare
 *	its digits.
 */
import java.util.ArrayList;
public class E4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 3;
		n = largestMultiple(n);
		
		E4Number num = new E4Number(n);
		
		while(true){
			System.out.println(num.getNum());
			if(num.isPalindrome() && hasNDigitFactors(3, num.getNum())){
				break;
			}
			
			num.nextPalindrome();
		}
		
		
		System.err.println(num.getNum());//print the answer
		
	}
	
	
	//Return the largest multiple of any two n-digit numbers
	public static int largestMultiple(int n){
		int num = makeNines(n);
		return num*num;
	}
	
	//Make an n-digit number of all nines
	public static int makeNines(int n){
		int ret = 0;
		for(int i = 0; i < n; i ++){
			ret += (9 * Math.pow(10, i));
		}
		return ret;
	}
	
	//Returns true if n has two m-digit factors and false otherwise
	public static boolean hasNDigitFactors(int n, int num){
		boolean ret = false;
		
		int i = (1 * (int)Math.pow(10, n-1));//Start with the lowest n-digit number
		
		while(i <= Math.sqrt(num)){
			if(num % i == 0){//If i evenly divides num
				if(sameNumberOfDigits(i, num/i)){
					ret = true;
					break;
				}
			}
			i++;
		}
		return ret;
	}

	//Check to see if a and b have the same number of digits
	public static boolean sameNumberOfDigits(int a, int b){
		boolean ret = true;
		while(a/10 != 0){//While there is still some left of a
			if(b/10 == 0){//If there is none left of b...
				ret = false;
				break;
			}
			a /= 10;
			b /= 10;
		}
		if(b/10 != 0)//If there is still some left of b
			ret = false;//There is not still some left of a
		
		return ret;
	}
	
	//Used for debugging.  Prints out the factors of a number n
	public static void factor(int n){
		for(int i = 1; i < (int)Math.sqrt(n); i ++){
			if(n % i == 0){
				System.out.println(i + ", " + n/i);
			}
		}
			
	}

}