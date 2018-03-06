import java.math.BigInteger;
import java.math.MathContext;
import java.util.Iterator;
import java.util.LinkedList;
import java.math.BigDecimal;

/**
 * 
 * @author Drew
 * 
 * This class stores a fraction as a numerator (as a long or BigIntger depending on the size) and a denominator
 * as a custom class FactoredInteger. The purpose of the FactoredInteger class for the denominator is to allow
 * us to easily check the prime factors in the denominator which gives us easy access to information we need.
 * 
 * This class also uses a variable called Vector which is a BigInteger which represents which inverse squares are
 * part of this fractions sum. For example, if a fraction was obtained by adding 1/4^2 to 1/7^2, the (bitwise)
 * version of the vector would be 01001000 meaning that the 4th and 7th inverse squares are part of the sum.
 * Using this bit vector allows us to see which fractions have been added to this sum in constant time.
 *
 */

public class FactoredFraction {
	private final BigInteger ONE = new BigInteger("1");
	private final BigInteger ZERO = new BigInteger("0");
	private BigInteger vector;
	private BigInteger numeratorBigInt;
	private long numeratorLong;
	private FactoredInteger denominator;
	private String asString;
	private double doubleValue;
	private int lowestFractionDenominator;
	
	public FactoredFraction(long n, FactoredInteger d, BigInteger v) {
		this.numeratorLong = n;
		this.denominator = d;
		this.vector = v;
		asString= null;
		numeratorBigInt = null;
		lowestFractionDenominator = -1;
		
		reduce();
	}
	
	public FactoredFraction(BigInteger n, FactoredInteger d, BigInteger v) {
		this.numeratorBigInt = n;
		this.denominator = d;
		vector = v;
		asString = null;
		numeratorLong = 0;
		lowestFractionDenominator = -1;
		
		reduce();
	}
	
	public FactoredFraction(FactoredInteger d, int v) {
		this.numeratorLong = 1;
		this.numeratorBigInt  = null;
		this.denominator = d;
		lowestFractionDenominator = -1;
		vector = ONE.shiftLeft(v);
	}
	
	public FactoredFraction(FactoredInteger d, BigInteger v) {
		this.numeratorBigInt = null;
		this.numeratorLong = 1;
		this.denominator = d;
		this.vector = v;
		lowestFractionDenominator = -1;
	}
	
	public FactoredFraction(BigInteger n, FactoredInteger d) {
		this.numeratorBigInt = n;
		this.numeratorLong = 0;
		this.denominator = d;
		lowestFractionDenominator = -1;
		vector = ONE;
	}
	
	public FactoredFraction(BigInteger n, FactoredInteger d, BigInteger v, boolean reduced) {
		this.numeratorBigInt = n;
		this.denominator = d;
		vector = v;
		lowestFractionDenominator = -1;
		asString = null;
	}
	
	/**
	 * Reduces this fraction, meaning the numerator and denominator do not share any prime factors.
	 * This is necessary when adding factors as the prime factorization of the numerator changes
	 */
	private void reduce() {
		LinkedList<PrimeFactor> dFactors = denominator.getFactors();
		Iterator<PrimeFactor> it = dFactors.iterator();
		LinkedList<PrimeFactor> gcf = new LinkedList<PrimeFactor>();
		
		//Find out whether the numerator is being stored as a long or BigInteger and call the appropriate method
		if(numeratorLong > 0) {
			reduceLong(dFactors, gcf, it);
		} else {
			reduceBigInt(dFactors, gcf, it);
		}
	}
	
	/**
	 * Since the numerator can be one of two different data types, we need a different method for each
	 * one.
	 */
	public void reduceLong(LinkedList<PrimeFactor> dFactors, LinkedList<PrimeFactor> gcf, Iterator<PrimeFactor> it) {
		PrimeFactor current;
		
		while(it.hasNext()) {
			current = it.next();
			long mod;
			long base = current.getBase();
			int exp = 0;
			for(int i = 0; i < current.getExponent(); i ++) {
				mod = numeratorLong % base;
				if(mod == 0) {
					exp ++;
					numeratorLong = numeratorLong / base;
				} else {
					break;
				}
			}
			if(exp > 0) {
				gcf.add(new PrimeFactor(current.getBase(), exp));
			}
		}
		if(gcf.size() > 0) {
			FactoredInteger gcfFi = new FactoredInteger(gcf);
			denominator = denominator.integerDivide(gcfFi);
		}
	}
	
	//Reduce the numerator if it's a big integer
	public void reduceBigInt(LinkedList<PrimeFactor> dFactors, LinkedList<PrimeFactor> gcf, Iterator<PrimeFactor> it) {
		PrimeFactor current;
		while(it.hasNext()) {
			current = it.next();
			BigInteger mod;
			BigInteger base = new BigInteger("" + current.getBase());
			int exp = 0;
			for(int i = 0; i < current.getExponent(); i ++) {
				mod = numeratorBigInt.mod(base);
				if(mod.toString() == "0") {
					exp ++;
					numeratorBigInt = numeratorBigInt.divide(new BigInteger("" + current.getBase()));
				} else {
					break;
				}
			}
			if(exp > 0) {
				gcf.add(new PrimeFactor(current.getBase(), exp));
			}
		}
		if(gcf.size() > 0) {
			FactoredInteger gcfFi = new FactoredInteger(gcf);
			denominator = denominator.integerDivide(gcfFi);
		}
	}
	
	/**
	 * Adds this fraction to another fraction
	 * @param f the fraction being added to
	 * @return The result of adding the two fractions together
	 */
	public FactoredFraction add(FactoredFraction f) {
		FactoredFraction sum;
		long fNumLong = f.getNumeratorLong();
		
		//ORing the two vectors gives us the vector for the sum
		BigInteger newVector = vector.or(f.getVector());
		
		//Multiply the denominators together
		FactoredInteger newDenom = denominator.multiply(f.getDenominator());
		
		//If the numerator of both fractions is a long, we can simply add them together normally
		if(numeratorLong > 0 && fNumLong > 0 && fNumLong <= Long.MAX_VALUE / numeratorLong) {
			long numLeft = numeratorLong * f.getDenominator().longValue();
			long numRight = f.getNumeratorLong() * denominator.longValue();
			long newNum = numRight + numLeft;
			
			sum = new FactoredFraction(newNum, newDenom, newVector);
		} else {//If one of them is a big integer, we have to use the BigInteger method for adding
			BigInteger numLeft, numRight;
			
			//We need big integers for the numerators for both fractions so if one is stored as a long, convert
			//it to a BigInteger
			if(numeratorLong > 0) {
				numLeft = new BigInteger("" + numeratorLong);
			} else {
				numLeft = numeratorBigInt;
			}
			
			if(fNumLong > 0) {
				numRight = new BigInteger("" + fNumLong);
			} else {
				numRight = f.getNumeratorBigInt();
			}
			
			BigInteger newNum = numLeft.add(numRight);
			sum = new FactoredFraction(newNum, newDenom, newVector);
		}
		return sum;
		
	}
	
	public FactoredFraction subtract(FactoredFraction f) {
		FactoredFraction difference;
		long fNumLong = f.getNumeratorLong();
		BigInteger newVector = vector.and(f.getVector().not());
		
		FactoredInteger newDenom = denominator.multiply(f.getDenominator());
		if(numeratorLong > 0 && fNumLong > 0) {
			long numLeft = numeratorLong * f.getDenominator().longValue();
			long numRight = f.getNumeratorLong() * denominator.longValue();
			long newNum = numRight - numLeft;
		
			difference = new FactoredFraction(newNum, newDenom, newVector);
		} else {
			BigInteger numLeft, numRight;
			
			if(numeratorLong > 0) {
				numLeft = new BigInteger("" + numeratorLong);
			} else {
				numLeft = numeratorBigInt;
			}
			
			if(fNumLong > 0) {
				numRight = new BigInteger("" + fNumLong);
			} else {
				numRight = f.getNumeratorBigInt();
			}
			
			BigInteger newNum = numLeft.subtract(numRight);
			difference = new FactoredFraction(newNum, newDenom, newVector);
		}
		return difference;
	}
	
	/**
	 * Check to see if a fraction f is part of this
	 * sum
	 * @param f the fraction we're checking for
	 * @return true if f is in the sum of this fraction and false otherwise
	 */
	public boolean hasInSum(FactoredFraction f) {
		BigInteger and = vector.and(f.getVector());
		
		if(and.compareTo(ZERO) == 0) {
			return false;
		}
		return true;
	}
	
	public BigInteger getVector() {
		return vector;
	}
	
	public FactoredInteger getDenominator() {
		return denominator;
	}
	
	/**
	 * Find the highest prime factor in the denominator of this fraction
	 * @return the highest prime factor
	 */
	public int highestDenominatorFactor() {
		//The prime factors are always in ascending order so we just take the last one
		LinkedList<PrimeFactor> factors = denominator.getFactors();
		return factors.getLast().getBase();
	}
	
	public BigInteger getNumeratorBigInt() {
		return numeratorBigInt;
	}
	
	public long getNumeratorLong() {
		return numeratorLong;
	}

	/**
	 * Get the value of this fraction as a double
	 * @return The result of dividing the numerator by the denominator
	 */
	public double doubleValue() {
		if(doubleValue == 0) {
			BigDecimal quotient;
			if(numeratorLong == 0) {
				quotient = new BigDecimal(numeratorBigInt).divide(new BigDecimal(denominator.getValue()), MathContext.DECIMAL128);
			} else {
				BigInteger tempBigInt = new BigInteger("" + numeratorLong);
				quotient = new BigDecimal(tempBigInt).divide(new BigDecimal(denominator.getValue()), MathContext.DECIMAL128);
			}
			doubleValue = quotient.doubleValue();
		}
		return doubleValue;
	}
	
	/**
	 * Find out if the denominator has a factor n
	 * @param n the factor being checked
	 * @return true if n is a factor in the denominator, false otherwise
	 */
	public boolean hasFactor(int n) {
		return denominator.hasFactor(n);
	}
	
	/**
	 * Checks through all fractions that have been added to the current one
	 * and returns the denominator of the lowest one
	 * @param max the highest integer for which we find the inverse square
	 * @return an integer from 2 to max
	 */
	public int getLowestFractionDenominator(int max) {
		if(lowestFractionDenominator == -1) {
			BigInteger mask = ONE;
			for(int i = 1; i <= max; i ++) {
				mask = mask.shiftLeft(1);
				BigInteger result = vector.and(mask);
				if(result.compareTo(ZERO) != 0) {
					lowestFractionDenominator = i;
					break;
				}
			}
		}
		
		return lowestFractionDenominator;
	}
	
	/**
	 * Returns a string representation of the fractions in this sum, for debugging purposes
	 * @param max the highest integer for which we find the inverse square
	 * @return a string representing what's in this fraction
	 */
	public String inSum(int max) {
		String inSum = "";
		BigInteger mask = ONE;
		boolean printPlus = false;
		for(int i = 0; i <= max; i ++) {
			mask = mask.shiftLeft(1);
			BigInteger result = vector.and(mask);
			if(result.compareTo(ZERO) != 0) {
				if(printPlus) {
					inSum += " + ";
				}
				inSum += ("1/" + (i + 1) + "^2");
				printPlus = true;
			}
		}
		
		return inSum;
	}
	
	/**
	 * Returns a string representation of this fractions vector
	 * @param max the highest integer for which we find the inverse square
	 * @return a string representation of this fractions vector
	 */
	public String bitwiseVector(int max) {
		String bitwise = "";
		
		BigInteger mask = ONE;
		
		for(int i = 0; i <= max; i ++) {
			mask = mask.shiftLeft(1);
			if(vector.and(mask).compareTo(ZERO) == 0) {
				bitwise = 0 + bitwise;
			} else bitwise = 1 + bitwise;
		}
		
		return bitwise;
	}
	
	public int compareTo(FactoredFraction f) {
		return this.vector.compareTo(f.getVector());
	}

	public String toString() {
		if(asString == null) {
			if(numeratorLong == 0) {
				asString = numeratorBigInt + "/(" + denominator + ")";
			} else {
				asString = numeratorLong + "/(" + denominator + ")";
			}
		}
		return asString;
	}
}
