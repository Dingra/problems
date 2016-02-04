
public class E16 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Big_number bn = new Big_number(50);
		
		for(int i = 0; i < 1000; i ++){
			bn.multiply(2);
			System.out.println("The number is " + bn.getFullNumber());
		}
		
		System.out.println("The sum of the digits is " + bn.sumOfDigits());

	}

}
