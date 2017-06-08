/**
 * For this problem, finding a general solution that works was easy. We're essentially just counting numbers.
 * 
 * The main feature of this program is to start from 100, then find the next bouncy number, then the next non-bouncy number
 * and repeat.
 * 
 * Since there are clusters of non-bouncy numbers, (ie from 1900 to 1999 for example), rather than simply counting up, we can
 * subtract each bouncy number we find from then next non bouncy number to find out how many there are in that cluster.
 * For example. there are 99 bouncy numbers from 1900 to 1999
 * @author Drew
 *
 */
public class BouncyNumber {

	public BouncyNumber(double target) {
		iterate(target);
	}
	
	/*
	 * Go from each bouncy number to each non-bouncy number and so on until the proportion of bouncy numbers is greater than or
	 * equal to target
	 */
	private void iterate(double target) {
		int start = 100 ;
		int lastBouncy = start;
		int lastNonBouncy = 1;
		double currentCount = 0;
		boolean targetHit = false;
		boolean bouncy = true;
		int current = start;
		
		//While the proportion of bouncy numbers is under .99
		while(!targetHit) {
			
			if(bouncy) {
				//Store the next non-bouncy number as the current number
				current = nextNonBouncy(current);
				lastNonBouncy = current;
				
				//Sum is a temporary storage for currentCount
				double sum = currentCount + lastNonBouncy - lastBouncy;
				
				//Find out of this is the target proportion. Note that the current proportion can be (and usually is)
				//greater than the target proportion. This is because we're only looking for the range in which
				//the correct answer lies
				if((sum) / lastNonBouncy >= target) {
					targetHit = true;
				} else {
					//We only set currentCount to sum if we haven't hit our target because once we've hit our target,
					//the number we add lies between the last two numbers found
					currentCount = sum;
				}
				
				bouncy = false;
			} else {
				current = nextBouncy(current);
				lastBouncy = current;
				bouncy = true;
			}
		}
		
		if(targetHit) {
			System.out.println("The answer is: " + binarySearch(lastBouncy, lastNonBouncy, currentCount, target));
		}
	}
	
	//Simple binary search. This works because all of the numbers between start and end are bouncy, so only one of them
	//can have the target proportion
	private int binarySearch(int start, int end, double count, double target) {
		
		if(end < 0) {
			return 0;
		}
		
		double initialCount = count;
		int diff = end - start;
		int check = start + diff / 2;
		
		double newCount = initialCount + diff / 2;
		
		double proportion = newCount / (check);
		
		if(start == end - 1) {
			return 0;
		} else if(proportion > target) {
			return binarySearch(start, check, count, target);
		} else if(proportion < target) {
			return binarySearch(check, end, newCount, target);
		} else return check;//This is the answer!
		
	}
	
	/*
	 * Given a non-bouncy number, find the next bouncy number. This is done by checking to see if the 
	 * current number is ascending or descending. Numbers whose digits are all the same are treated as ascending
	 */
	private int nextBouncy(int number) {
		int num = number;
		int[] numArray = intToArray(number);
		
		if(!isDescending(numArray)) {//If the number is ascending...
			num += (10 - numArray[0]);//Add the amount to num that will increase the second digit by 1 and decrease the rightmost digit to 0
			int[] finalNumArray = intToArray(num);
			
			if(finalNumArray[finalNumArray.length - 1] > numArray[numArray.length - 1]) {
				num ++;
			}
		} else {//If it is descending, there are a lot of special cases to consider
			int diff = numArray[1] - numArray[0];
			
			//Another special case where a number is 110, 9990, 22220 etc.. In this case, 11 must be added
			boolean sameSameZero = true;
			boolean nineNineZero = true;
			
			int leftMost = numArray[numArray.length - 1];
			for(int i = numArray.length - 1; i > 0; i --) {
				if(numArray[i] != 9) {
					nineNineZero = false;//To deal with a number that is in the form of 9990
				}
				if(numArray[i] != leftMost) {
					sameSameZero = false;//to deal with numbers where the leftmost digit is 0 and the rest are all the same
					break;//once a digit is found that's different, we can stop
				}
			}
			
			if(numArray[0] == 9) {//Special case of a descending number where each digit is 9 (ex 9999)
				num += 2;//adding 1 will make it a descending number (10000 for example) so need to add 2 (10001 for example)
			} else if(nineNineZero) {
				num -= numArray[0];
				num += 11;
			} else if(sameSameZero) {
				num -= numArray[0];
				num += 10;
			} else if(diff == 0) {//If the last two digits are the same in a descending number, (ie 7522) incrementing by 1 makes it bouncy
				num ++;
			} else {
				num += diff + 1;
			}
		}
		
		return num;
	}
	
	//Given a bouncy number, find the next non-bouncy number
	public int nextNonBouncy(int number) {
		int[] numArray = intToArray(number);
		
		int focalDigit = focalDigitBouncyNumber(numArray);
		
		if(!isDescending(numArray)) {//If the number is ascending
			int diff = numArray[focalDigit + 1] - numArray[focalDigit]; 
			numArray[focalDigit] = numArray[focalDigit] + diff;
			
			for(int i = 0; i < focalDigit; i ++) {
				numArray[i] = numArray[focalDigit];
			}
		} else {
			
			//If the focal digit is 9, then the digit to its left must be incremented by 1
			//The focal digit can only be a 9 in the case of a descending number because 9 is the higest possible digit
			//ie, it is not possible for the digit left of the focal digit to be greater than 9
			if(numArray[focalDigit] == 9) {
				numArray[focalDigit + 1] += 1;
				numArray[focalDigit] = 0;
			}
			
			numArray[focalDigit] = numArray[focalDigit] + 1;
			
			for(int i = 0; i < focalDigit; i ++) {
				numArray[i] = 0;
			}
		}
		
		return arrayToInt(numArray);
	}
	
	public int[] copyArray(int[] number) {
		int[] retArray = new int[number.length];
		
		for(int i = 0; i < number.length; i ++) {
			retArray[i] = number[i];
		}
		
		return retArray;
	}
	
	//Convert an integer to an array to make it easier to reference each digit
	public int[] intToArray(int number) {		
		int count = 0;
		
		int numCopy = number;
		
		while(numCopy != 0) {
			count ++;
			numCopy = numCopy / 10;
		}
		
		int[] ret = new int[count];
		
		numCopy = number;
		for(int i = 0; i < count; i ++) {
			ret[i] = numCopy % 10;
			numCopy = numCopy / 10;
		}
		
		return ret;
	}
	
	//Convert an int array back to an int
	public int arrayToInt(int[] numArray) {
		int number = 0;
		
		for(int i = 0; i < numArray.length; i ++) {
			int digit = numArray[i];
			number += digit * Math.pow(10, i);
		}
		
		return number;
	}
	
	/*
	 * Find the "Focal" digit in a bouncy number. The focal digit is the next one that will have to change
	 * in order for the number to stop being bouncy. For example in the number 17418, the focal number is 4 because, going from
	 * left to right, it is the first digit that is not part of the ascension (1, 7 then 4) so it needs to be changed to a 7 in order
	 * for number not to be considered bouncy
	 * 
	 * @param number: a bouncy number in the form of an array
	 */
	public int focalDigitBouncyNumber(int[] number) {
		
		int previousDiff = 10;//A diff can never be more than 9 because the greatest diff is 9 - 0 since we're dealing in single digits
		int currentDiff = 0;
		int lastUniqueDigit = number.length - 1;
		
		for(int i = number.length -1; i > 0; i --) {
			
			//If the current diff is 0, there is no way to tell if the number was ascending or descending up to
			//this point. Use the previous diff instead
			if(number[i] - number[i - 1] != 0)
				currentDiff = number[i] - number[i - 1];
		
			if(previousDiff == 10) {//Essentially if this is the first pass of the loop
				previousDiff = currentDiff;
				
				//We've reached the focal point if all of the previous digits were ascending (negative diff)
				//or ascending (positive diff)
			} else if ((currentDiff > 0 && previousDiff < 0)) {
				return i - 1;//Simply return the position of the digit that is the focal point
				
			} else if (currentDiff < 0 && previousDiff > 0) {//Descending numbers are more complicated
				return lastUniqueDigit;
			}
			
			if(number[lastUniqueDigit] != number[i - 1]) {
				lastUniqueDigit = i - 1;
			} 
			previousDiff = currentDiff;
		}
		
		//This case should only be reached for a number whose digits are all the same, in which case the second to last
		//digit is the one that must change
		return 1;
	}
	
	//Determine if a number is descending. Numbers whose digits are all the same are not considered descending
	private boolean isDescending(int[] number) {
		int diff;
		boolean ret = false;
		
		//Basically just look at the two left-most digits. If they aren't the same, return true or false based on which is larger
		for(int i = number.length - 1; i >= 1; i --) {
			diff = number[i] - number[i - 1];
			
			if(diff < 0) {
				ret = false;
				break;
			} else if(diff > 0) {
				ret = true;
				break;
			}
		}
		
		//All digits are the same, so return false
		return ret;
	}
	
	//State whether a number is bouncy or not. Probably just for debugging
	public boolean isBouncy(int number) {
		boolean positiveChange = false;
		boolean negativeChange = false;
	
		while(number / 10 > 0) {
			int n1 = number % 10;
			int n2 = (number /10) % 10;
			
			int diff = n1 - n2;
			
			if(diff < 0) {
				negativeChange = true;
			} else if (diff > 0) {
				positiveChange = true;
			}
			number = number / 10;
			if(negativeChange && positiveChange) {
				break;
			}
		}
		
		return negativeChange && positiveChange;
	}
}
