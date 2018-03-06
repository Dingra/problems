import java.util.Iterator;
import java.util.LinkedList;
import java.math.BigInteger;


/* 
 * This class represents an integer as a list of prime factors including a base and exponent.	
 *
 */

public class FactoredInteger {
	LinkedList<PrimeFactor> primeFactors;
	
	private BigInteger value;
	private String asFactoredString, asString;
	
	public FactoredInteger(LinkedList<PrimeFactor> pf) {
		value = null;
		primeFactors = pf;
		asFactoredString = null;
	}
	
	//Assumes that base is a prime number
	public FactoredInteger(int base, int exponent) {
		primeFactors = new LinkedList<PrimeFactor>();
		primeFactors.add(new PrimeFactor(base, exponent));
		value = null;
		asFactoredString = null;
	}
	
	public FactoredInteger(int base) {
		primeFactors = new LinkedList<PrimeFactor>();
		primeFactors.add(new PrimeFactor(base, 1));
		value = null;
		asFactoredString = null;
	}
	
	/**
	 * Allows the function calling it to pass an integer and a list of
	 * prime numbers instead of needing to worry about providing the prime factoriziation
	 * @param compositeValue
	 * @param primes
	 */
	public FactoredInteger(int compositeValue, int[] primes) {
		primeFactors = new LinkedList<PrimeFactor>();
		
		int i = 0;
		int exponent = 0;
		while(i < primes.length) {
			int base = primes[i];
			if(compositeValue % base == 0) {
				compositeValue /= base;
				exponent ++;
			} else {
				if(exponent > 0) {
					primeFactors.add(new PrimeFactor(base, exponent));
					exponent = 0;
				}
				if(compositeValue <= 1) {
					break;
				}
				i ++;
			}
		}
		
		value = null;
		asFactoredString = null;
	}
	
	public FactoredInteger(PrimeFactor ... factors){
		primeFactors = new LinkedList<PrimeFactor>();
		for(int i = 0; i < factors.length; i ++) {
			primeFactors.add(factors[i]);
		}
		
		value = null;
		asFactoredString = null;
	}
	
	//Multiply a factored integer by an int
	public FactoredInteger multiply(int n) {
		FactoredInteger fi = new FactoredInteger(n, 1);
		return multiply(fi);
	}
	
	/**
	 * Multiply two factored integers together while preserving the list of prime factors.
	 * This essentially just means making a list of PrimeFactors. All the prime factors of
	 * both factored integers are in the new list and the ones they have in common have their
	 * exponents added together. For example, if we're adding (2^2 * 3^1) to (3^1 * 7^1) the
	 * result will be (2^2 * 3^2 * 7^1)
	 * @param fi the FactoredInteger we're adding this one to
	 * @return the sum of the two FactoredIntegers
	 */
	public FactoredInteger multiply(FactoredInteger fi) {
		LinkedList<PrimeFactor> productFactors = new LinkedList<PrimeFactor>();
		LinkedList<PrimeFactor> fiFactors = fi.getFactors();
		
		Iterator<PrimeFactor> itA = primeFactors.iterator();
		Iterator<PrimeFactor> itB = fiFactors.iterator();
		PrimeFactor currentA = itA.next();
		PrimeFactor currentB = itB.next();
		
		//Flags to see if we're at the end of the list of prime factors
		boolean endA = false;
		boolean endB = false;
		
		/*
		 * We need to iterate through the prime factors of both factored integers and
		 * add them to a new list.
		 * 
		 */
		
		while(!(endB && endA)) {//Keep going until we've reached the end of both lists
			//If we haven't reached the end of list A and either the current factor at A is less than the one at B
			//OR we've reached the end of B, we need that means that we need to add the current factor in A
			//to the list
			if(!endA && (currentA.getBase() < currentB.getBase() || endB)) {
				int newBase = currentA.getBase();
				int newExponent = currentA.getExponent();
				productFactors.add(new PrimeFactor(newBase, newExponent));
				if(itA.hasNext()) {
					currentA = itA.next();
				} else endA = true;
			} else if(!endB && (currentB.getBase() < currentA.getBase() || endA)) {//Same thing as for A
				int newBase = currentB.getBase();
				int newExponent = currentB.getExponent();
				productFactors.add(new PrimeFactor(newBase, newExponent));
				if(itB.hasNext()) {
					currentB = itB.next();
				} else endB = true;
			} else if(currentA.getBase() == currentB.getBase()) {
				//If the bases of A and B are equal, we need to add the exponents together
				int newBase = currentA.getBase();
				int newExponent = currentA.getExponent() + currentB.getExponent();
				productFactors.add(new PrimeFactor(newBase, newExponent));
				
				//Make sure we don't go past the end of the list
				if(itA.hasNext()) {
					currentA = itA.next();
				} else endA = true;
				
				if(itB.hasNext()) {
					currentB = itB.next();
				} else endB = true;
			}
		}
		
		return new FactoredInteger(productFactors);
	}
	
	/**
	 * Returns the result of dividing this integer by another integer fi.
	 * Assumes that all factors of i exist in this FactoredInteger. This is used
	 * by the reduce in FactoredFraction since an integer cannot be divided
	 * directly by a FactoredInteger. Works pretty much the same way as the multiply method
	 * @param fi: The one we're dividing by
	 * @return A factored integer which is the result of dividing this FactoredInteger by fi
	 */
	public FactoredInteger integerDivide(FactoredInteger fi) {
		LinkedList<PrimeFactor> productFactors = new LinkedList<PrimeFactor>();
		LinkedList<PrimeFactor> fiFactors = fi.getFactors();
		
		Iterator<PrimeFactor> itA = primeFactors.iterator();
		Iterator<PrimeFactor> itB = fiFactors.iterator();
		PrimeFactor currentA = itA.next();
		PrimeFactor currentB = itB.next();
		
		boolean endA = false;
		boolean endB = false;
		
		//Keep going as long as one list isn't finished
		while(!(endB && endA)) {
			if(!endA && (currentA.getBase() < currentB.getBase() || endB)) {
				int newBase = currentA.getBase();
				int newExponent = currentA.getExponent();
				productFactors.add(new PrimeFactor(newBase, newExponent));
				if(itA.hasNext()) {
					currentA = itA.next();
				} else endA = true;
			} else if(!endB && (currentB.getBase() < currentA.getBase() || endA)) {
				if(itB.hasNext()) {
					currentB = itB.next();
				} else endB = true;
			} else if(currentA.getBase() == currentB.getBase()) {
				int newBase = currentA.getBase();
				int newExponent = currentA.getExponent() - currentB.getExponent();
				if(newExponent > 0) {
					productFactors.add(new PrimeFactor(newBase, newExponent));
				}
				
				if(itA.hasNext()) {
					currentA = itA.next();
				} else endA = true;
				
				if(itB.hasNext()) {
					currentB = itB.next();
				} else endB = true;
			}
		}
		
		if(productFactors.size() == 0) {
			productFactors.add(new PrimeFactor(1, 1));
		}
		
		return new FactoredInteger(productFactors);
	}
	
	public LinkedList<PrimeFactor> getFactors() {
		return primeFactors;
	}
	
	/**
	 * Return the integer value of this FactoredInteger
	 * @return The value of this FactoredInteger of as a BigInteger
	 */
	public BigInteger getValue() {
		if(value == null) {
			Iterator<PrimeFactor> it = primeFactors.iterator();
			value = new BigInteger("0");
			
			long sum = 0;
			while(it.hasNext()) {
				PrimeFactor current = it.next();
				long currentVal = (long)Math.pow(current.getBase(), current.getExponent());
				
				if (Long.MAX_VALUE / currentVal >= sum) {
					value = pseudoMultiply(value, new BigInteger("" + sum));
					sum = 0;
				}
				sum = pseudoMultiply(sum, currentVal);
			}
			value = pseudoMultiply(value, new BigInteger("" + sum));
		}
		
		return value;
	}
	
	/**
	 * Return the integer value of this FactoredInteger
	 * @return The value of this FactoredInteger of as a long
	 */
	public long longValue() {
		Iterator<PrimeFactor> it = primeFactors.iterator();
		long value = 0;
		while(it.hasNext()) {
			if(value == 0) {
				value += it.next().getValue();
			}
			else {
				value *= it.next().getValue();
			}
		}
		return value;
	}
	
	public long pseudoMultiply(long a, long b) {
		if(a == 0) {
			return a + b;
		} else return a - b;
	}
	
	public BigInteger pseudoMultiply(BigInteger a, BigInteger b) {
		if(a.toString() == "0") {
			return a.add(b);
		} else {
			return a.multiply(b);
		}
	}
	
	/**
	 * Find out if n is a factor in this FactoredInteger
	 * @param n the integer being checked
	 * @return True if n is hte base of one of these factors and false otherwise
	 */
	public boolean hasFactor(int n) {
		Iterator<PrimeFactor> it = primeFactors.iterator();
		PrimeFactor current;
		while(it.hasNext()) {
			current = it.next();
			if(current.getBase() == n) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Find out if this FactoredInteger has a common factor with f
	 * @param f the FactoredInteger we're checking against
	 * @return true if f has a common factor with this and false otherwise
	 */
	public boolean hasCommonFactor(FactoredInteger f) {
		Iterator<PrimeFactor> itA = primeFactors.iterator();
		PrimeFactor currentA = itA.next();
		Iterator<PrimeFactor> itB = f.getFactors().iterator();
		PrimeFactor currentB = itB.next();
		
		while(true) {
			
			if(currentA.getBase() == currentB.getBase()) {
				return true;
			} else if(currentA.getBase() < currentB.getBase()) {
				if(itA.hasNext()) {
					currentA = itA.next();
				} else break;
			} else {
				if(itB.hasNext()) {
					currentB = itB.next();
				} else break;
			}
		}
		
		return false;
	}
	
	/**
	 * Square the current integer by multiplying the exponents of all its
	 * prime factors by 2
	 * @return The square of this FactoredInteger
	 */
	public FactoredInteger square() {
		Iterator<PrimeFactor> it = primeFactors.iterator();
		PrimeFactor current;
		LinkedList<PrimeFactor> ret = new LinkedList<PrimeFactor>();
		while(it.hasNext()) {
			current = it.next();
			int currentBase = current.getBase();
			int newExp = current.getExponent()*2;
			PrimeFactor newf = new PrimeFactor(currentBase, newExp);
			ret.add(newf);
		}
		return new FactoredInteger(ret);
	}
	public String toString() {

		if(asFactoredString == null) {
			asFactoredString = "";
			Iterator<PrimeFactor> it = primeFactors.iterator();
			PrimeFactor current;
			while(it.hasNext()) {
				current = it.next();
				asFactoredString += current.getBase() + "^" + current.getExponent();
				if(it.hasNext()) {
					asFactoredString = asFactoredString + " * ";
				}
			}
			asFactoredString = asFactoredString + "(" + getValue() + ")";
		}
		
		return asFactoredString;
	}
}
