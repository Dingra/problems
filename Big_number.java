public class Big_number {
	private int[] big_number;
	
	public Big_number(int size){
		big_number = new int[size];
		
		big_number[0] = 1;
		
		for(int i = 1; i < size; i ++){
			big_number[i] = 0;
		}
	}
	
	public void multiply(int mult){
		int carryOver = 0;
		
		for(int i = 0; i < big_number.length; i ++){
			int temp = (big_number[i] * mult) + carryOver;
			//System.out.println("big_number at " + i + " is " + big_number[i]);
			//System.out.println("Temp is " + temp);
			
 			if(temp >= 100000000){
				big_number[i] = temp % 100000000;
				carryOver = temp / 100000000;
				//System.out.println("CarryOver is " + carryOver);
			}
 			else{
 				big_number[i] = temp;
 				carryOver = 0;
 			}
		}
	}
	
	public String getFullNumber(){
		String full = "";
		
		for(int i = big_number.length - 1; i >= 0; i --){
			full = full + big_number[i];
		}		
		return full;
	}
	
	public int sumOfDigits(){
		int sum = 0;
		for(int i = 0; i < big_number.length; i ++){
			int temp = big_number[i];
			for(int j = 0; j < 9; j ++){
				sum += temp % 10;
				temp = temp / 10;
			}
		}
		return sum;
	}
}
