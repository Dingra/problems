import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
public class InverseSumOfSquares {
	
	/**
	 * The question asks how many ways there are to add sums of inverse squares of integers from 2 to 80 to get 1/2.
	 * 
	 * They key to this problem is recognizing that in order to get to 1/2, all prime factors in the denominator
	 * of a fraction must be cancelled out except for a single factor of 2.
	 * 
	 * A fraction with a prime factor f in its denominator must be added to another fraction with f in its denominator
	 * for f to be cancelled out.
	 * 
	 * This solution works in two major steps.
	 * First, all fractions with prime factors greater than 2 are summed together to find which combinations cancel
	 * out those factors.
	 * 
	 * Second, those fractions with only 2 as a prime factor are added to the sums in the previous step to find which ones
	 * add up to 1/2.
	 * 
	 * Fractions are stored using a custom FactoredFraction class which represents a fraction as a numerator as a BigInteger
	 * to prevent overflow and a denominator as a FactoredInteger.
	 * 
	 * FactoredInteger is another custom class which represents an integer as a set of prime factors along with their
	 * exponents. These prime factors are represented by yet another class called PrimeFactor which stores a base
	 * and an exponent. Details for how these work can be found in each one's class.
	 * 
	 * More changes to come:
	 * I might come back to this at some point to try to optimize a bit more. My code runs in about a minute on my computer
	 * which I'm happy but there's definitely room for improvement. This will most likely come down to a deeper understanding of
	 * the problem rather than making tweaks to the code to make things more efficient but we'll see.
	 */
	
	private int max;
	private int[] primes;
	private FactoredFraction target;
	
	public InverseSumOfSquares(int max, FactoredFraction target) {
		
		this.target = target;
		this.max = max;
		LinkedList<Integer> primeList = new LinkedList<Integer>();
		
		primeList.add(2);
		/**
		 * Come up with a list of prime numbers <= max
		 */
		for(int i = 3; i <= max; i ++) {
			Iterator<Integer> it = primeList.iterator();
			int current;
			int sqrt = (int)Math.sqrt(i);
			while(it.hasNext()) {
				current = it.next();
				if(current > sqrt) {
					primeList.add(i);
					break;
				} else if(i % current == 0) {
					break;
				}
			}
		}
		
		//Stick the primes into an array
		primes = new int[primeList.size()];
		Iterator<Integer> it = primeList.iterator();
		int i = 0;
		int current;
		while(it.hasNext()) {
			current = it.next();
			primes[i] = current;
			i ++;
		}
	}
	
	public int actuallySolve() {
		ArrayList<FactoredFraction> allFractions = new ArrayList<FactoredFraction>();
		ArrayList<FactoredFraction> powersOfTwo = new ArrayList<FactoredFraction>();
		
		//Make an arraylist containing all inverse square fractions (1/2^2, 1/3^2 etc.)
		BigInteger vector = new BigInteger("2");
		for(int i = 2; i <= max; i ++) {
			vector = vector.shiftLeft(1);
			FactoredInteger denominator = new FactoredInteger(i, primes);
			allFractions.add(new FactoredFraction(denominator.square(), vector));
		}
		//Place all fractions whose denominators are powers of 2 into an arraylist
		for(int i = 2; i < max; i *= 2) {
			powersOfTwo.add(allFractions.get(i - 2));
		}
		
		//For all fractions which have denominators that aren't powers of 2, find all sums of these
		//which only have powers of 2 in the denominator
		powersOfTwo.addAll(reduceToPowersOfTwo(allFractions, null));
		
		FractionGroup[] groupedPowersOfTwo = groupSumByLowestFraction(powersOfTwo);
		
		ArrayList<FactoredFraction> validSums = new ArrayList<FactoredFraction>();
		numberOfInverseSquareSums(null, target, groupedPowersOfTwo, 0, validSums);
		
		return validSums.size();
	}
	
	/**
	 * Takes an arraylist of powers of 2 and sorts them into groups according to the largest fraction in its sum.
	 * Because we're dealing with sums of fractions, we need to avoid adding two fractions that have a base fraction
	 * in common. This can be done by going through a list of fractions linearly and simply skipping over one which
	 * shares a base fraction but we can actually save time by sorting them into groups
	 * @param powersOfTwo an arraylist of FactoredFractions for which the only prime factor in the denominator is 2
	 * @return An array containing groups of fractions for which the largest fraction in the sum is the same
	 */
	public FractionGroup[] groupSumByLowestFraction(ArrayList<FactoredFraction> powersOfTwo) {
		FractionGroup[] groupedPowersOfTwo = new FractionGroup[max + 1];
		
		for(int i = 0; i < powersOfTwo.size(); i ++) {
			FactoredFraction currentFraction = powersOfTwo.get(i);
			int lowestFractionDenominator = currentFraction.getLowestFractionDenominator(max);
			
			if(groupedPowersOfTwo[lowestFractionDenominator] == null) {
				groupedPowersOfTwo[lowestFractionDenominator] = new FractionGroup();
			}
			
			groupedPowersOfTwo[lowestFractionDenominator].add(currentFraction);
		}
		
		return groupedPowersOfTwo;
	}
	
	/**
	 * Find the number of ways to add inverse squares of distinct integers from 2 to max to get a sum of target
	 * @param sum The current sum
	 * @param target The sum we're trying to find
	 * @param groups The fractions that we'll be adding together
	 * @param groupIndex The index of the current group
	 * @param answers An array list containing a set of base fractions which add to target
	 */
	public void numberOfInverseSquareSums(FactoredFraction sum, FactoredFraction target, FractionGroup[] groups, int groupIndex, ArrayList<FactoredFraction> answers) {
		FractionGroup currentGroup = groups[groupIndex];
		
		//Go to the next one without adding the current one to the sum
		if(groupIndex < max) {
			numberOfInverseSquareSums(sum, target, groups, groupIndex + 1, answers);
		}
		
		for(int i = 0; currentGroup != null && i < currentGroup.size(); i ++) {
			FactoredFraction newSum;
			FactoredFraction currentFraction = currentGroup.get(i);
			
			//We need to check if the current fraction has already been added to the sum
			if(sum == null || !sum.hasInSum(currentFraction)) {
				
				//If sum is null it hasn't been instantiated yet which essentially means it's zero
				if(sum == null) {
					newSum = currentFraction;
				} else {
					newSum = sum.add(currentFraction);
				}
				
				//We check if lowestFractionDenominator(max) != to the index of the next group because if it is, that means
				//that the fractions in the next group ALL have at least one fraction in common with the sum
				if(groupIndex < max && currentGroup.lowestFractionDenominator(max) != groupIndex + 1) {
					if(newSum.doubleValue() < target.doubleValue()) {
						numberOfInverseSquareSums(newSum, target, groups, groupIndex + 1, answers);
					} else if(newSum.doubleValue() == target.doubleValue()) {
						if(!arrayListContainsFraction(answers, newSum)){
							answers.add(newSum);
						}
					}
				}
			}
		}
	}
	
	/**
	 * This function takes an array list containing all base fractions (those which are the inverse squares of integers from 2 to max)
	 * and finds sums of those fractions which only contain powers of 2. The base fractions which are already powers of 2 are skipped over.
	 * This process looks at a fraction whose HDPF is greater than 2 and adds it to others with the same HDPF starting with the highest
	 * prime number under max/2. The sum of a group of fractions with the same HDPF is defined to be "valid" if the sum does not
	 * have that HDPF as a prime factor. When one of these is found, it is added to back the allFractions Array List. That sum, having a
	 * new HDPF may be added to another fraction with the same HDPF
	 * @param allFractions an arrayList initially containing all base fractions
	 * @param debug a fraction which may be used for debugging
	 * @return an array list of factored fractions which are sums of base fractions and only have 2 as a prime factor in the denominator
	 */
	public ArrayList<FactoredFraction> reduceToPowersOfTwo(ArrayList<FactoredFraction> allFractions, FactoredFraction debug) {
		ArrayList<FactoredFraction> powersOfTwo = new ArrayList<FactoredFraction>();
		ArrayList<FactoredFraction> viableFractions = new ArrayList<FactoredFraction>();
		
		//Check all of the primes to find if fractions with that as the HDPF add to ones that don't
		for(int primeIndex = primes.length / 2; primeIndex >= 1; primeIndex --) {
			int currentPrime = primes[primeIndex];
			ArrayList<FactoredFraction> forCurrentPrime = new ArrayList<FactoredFraction>();
			
			//Look through all fractions to find one whose HDPF is equal to the current prime
			for(int fractionIndex = allFractions.size() - 1; fractionIndex >= 0; fractionIndex --) {

				//We need to place all fractions with their HDPF == the current prime in an arraylist
				FactoredFraction currentFraction = allFractions.get(fractionIndex);
				if(currentFraction.highestDenominatorFactor() == currentPrime) {
					forCurrentPrime.add(currentFraction);
				}
			}
			
			//We also need to loop through the viable fractions found when iterating through previous primes since
			//those might have HDPFs equal to the current prime
			//When we find one, we can remove it from the allFractions arraylist because no other primes will use it
			for(int i = 0; i < viableFractions.size(); i ++) {
				FactoredFraction current = viableFractions.get(i);
				if(current.highestDenominatorFactor() == currentPrime) {
					forCurrentPrime.add(current);
					viableFractions.remove(i);
					i --;
				}
			}
			
			ArrayList<FactoredFraction> viableForPrime = new ArrayList<FactoredFraction>();
			if(forCurrentPrime.size() > 0) {
				viableForPrime(null, currentPrime, 0, forCurrentPrime, viableForPrime, debug);
			}
			
			for(int i = 0; i < viableForPrime.size(); i ++) {
				FactoredFraction current = viableForPrime.get(i);
				
				if(current.highestDenominatorFactor() == 2) {
					if(!arrayListContainsFraction(powersOfTwo, current)){
						powersOfTwo.add(current);
					}
				} else {
					viableFractions.add(current);
				} 
			}
		}

		return powersOfTwo;
	}
	
	/**
	 * Adds a list of viable fractions to the viableForPrime array list. A fraction is said to be viable for a prime currentPrime if
	 * currentPrime is the Highest Prime Denominator Factor (HDPF) in a group of fractions and the sum of those fractions does not have
	 * currentPrime as a prime factor in the denominator
	 * 
	 * @param sum The sum of a group of fractions, initially null (zero)
	 * @param currentPrime The prime number that we're checking for
	 * @param index The index of the prime we're checking in forCurrentPrime
	 * @param forCurrentPrime An Array List of fractions for which the HDPF(currentPrime) is the same
	 * @param viableForPrime An Array List of fractions which do not have currentPrime as a prime factor in the denominator
	 * @param debug A fraction which may be used for debugging
	 */
	public void viableForPrime(FactoredFraction sum, int currentPrime, int index, ArrayList<FactoredFraction> forCurrentPrime, ArrayList<FactoredFraction> viableForPrime, FactoredFraction debug) {
		if(index < forCurrentPrime.size()) {
			FactoredFraction newSum;
			FactoredFraction current = forCurrentPrime.get(index);
			
			//Don't include the current one in the sum
			viableForPrime(sum, currentPrime, index + 1, forCurrentPrime, viableForPrime, debug);
			
			if(sum == null || !sum.hasInSum(current)) {
				
				//If the sum object is null, the sum is essentially zero as nothing has been added to it
				if(sum == null) {
					newSum = current;
				} else {
					newSum = sum.add(current);
				}
				
				//If the sum after adding the current fraction still has currentPrime, we need to add more to it to see if it will go away
				if(newSum.hasFactor(currentPrime)) {
					viableForPrime(newSum, currentPrime, index + 1, forCurrentPrime, viableForPrime, debug);
				} else {//If not, we add this one to the Array List of viable primes
					viableForPrime.add(newSum);
				} 
			}
		}
	}
	
	/**
	 * Check to see if a fraction targetFraction exists in an arrayList fractionList
	 * @param fractionList an arrayList of FactoredFractions
	 * @param targetFraction a FactoredFraction
	 * @return True if targetFractoin is in fractoinList and false otherwise
	 */
	public boolean arrayListContainsFraction(ArrayList<FactoredFraction> fractionList, FactoredFraction targetFraction) {
		for(int i = 0; i < fractionList.size(); i ++) {
			FactoredFraction currentFraction = fractionList.get(i);
			if(currentFraction.compareTo(targetFraction) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Takes an array of integers and creates a Factored Fraction which is the sum of all inverse squares
	 * in that array
	 * @param allFractions a list of all base fractions
	 * @param index An array of indices of the fractions you want to add
	 * @return A factored fraction which is the sum of the inverse squares of index
	 */
	public FactoredFraction debugF(ArrayList<FactoredFraction> allFractions, int ... index) {
		FactoredFraction debug = null;
		for(int i = 0; i < index.length; i ++) {
			FactoredFraction current = allFractions.get(index[i] - 2);
			
			if(debug == null) {
				debug = current;
			} else {
				debug = debug.add(current);
			}
		}
		return debug;
	}

}
