
public class E4Number {
	private int[] number;//The number as an array
	private int num;//The number as an int
	private int length;//The length of the number
	
	public E4Number(int n){
		makeNumber(n);
		length = number.length;
		num = n;
	}
	
	public boolean isPalindrome(){
		int pos = length - 1;
		boolean ret = true;
		
		while(pos > length - 1 - pos){
			//System.out.println("Working...");
			if(number[pos] != number[length - 1 - pos]){
				ret = false;
				break;
			}
			pos --;
		}
		return ret;
	}
	
	public void nextPalindrome(){
		
		//Must first check to see if it is another palindrome.  If it is, subtract 1 from it.
		//Otherwise, it simply say that the current number is the next palindrome
		if(isPalindrome()){
			num --;
			makeNumber(num);
		}
		
		int pos = length - 1;
		int temp;
		
		System.out.println("\nCurrent Palindrome: " + getNum());
		
		while(pos > length - 1 - pos){
			int first = number[pos];
			int last = number[length - 1 - pos];
			//Find out what must be subtracted from num to make the numbers
			//at pos and length-pos equal
			if(last < first){
				temp = last + 10 - first;
				System.out.println("Subtracting " + first + " from " + (last + 10) + " to get " + temp);
			}
			else
				temp = number[pos] - number[length - 1 - pos];
			System.out.print("Num is: " + getNum() + " and ");
			System.out.println("Temp is: " + temp);
			System.out.println("Should be " + (10 - Math.abs(temp)) + "?");
			num -= Math.abs(temp) * Math.pow(10, length - 1 - pos);
			makeNumber(num);
			
			pos --;
		}
		
		//It is possible that matching two digits will change a digit that has already been
		//checked.  Must check here to see if the number is actually a palindrome
		if(!isPalindrome()){//If this number is not a palindrome
			System.err.println("Try again...");
			nextPalindrome();//Search for the next palindrome
		}
		else
			System.out.println(getNum());
			
	}
	
	public int getNum(){
		return num;
	}

	private void makeNumber(int n){
		int i = 0;
		int num = n;
		
		//Get the number of digits in n
		while(num != 0){
			i ++;
			num /= 10;
		}
		
		int[] temp = new int[i];
		num = n;
		
		//Put the digits of n into an array
		for(i = 0; i < temp.length; i ++){
			temp[i] = num % 10;
			num /= 10;
		}
		
		number = temp;
		length = number.length;
	}
}
