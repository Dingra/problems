/**
 * 
 * @author Drew Ingram
 * 
 *	This is a challenge to return the largest palindrome which is a multiple of
 *	two three-digit numbers.  This is the first challenge I am not doing in Scheme, but
 *	I am still in the Scheme mindset, which is why there are so many functions.
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
		//System.out.println(hasNDigitFactors(3, 10000));
		//System.out.println("SameNumber? " + sameNumberOfDigits(20, 1));
		
		//System.err.println((new E4Number(899998)).isPalindrome());
		//(new E4Number(998001)).nextPalindrome();
		
		
		while(true){
			System.out.println(num.getNum());
			if(num.isPalindrome() && hasNDigitFactors(3, num.getNum())){
				break;
			}
			
			num.nextPalindrome();
		}
		
		
		System.err.println(num.getNum());//print the answer
		
	}
	
	
	/**
	 * Return the largest multiple of any n digits
	 * @return the largst multiple
	 * @param n the number of digits
	 */
	public static int largestMultiple(int n){
		int num = makeNines(n);
		return num*num;
	}
	
	/**
	 * Create a number that is n nines
	 * @param n the number of digits
	 * @return a number containing n nines
	 */
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
			//	System.out.println("Checking " + i + " and " + num/i);
				if(sameNumberOfDigits(i, num/i)){
			//		System.err.println("TRUE FOR " + i + " AND " + num/i);
					ret = true;
					break;
				}
			}
			i++;
		}
		return ret;
	}

	
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
	
	public static void factor(int n){
		for(int i = 1; i < (int)Math.sqrt(n); i ++){
			if(n % i == 0){
				System.out.println(i + ", " + n/i);
			}
		}
			
	}

}
