import java.util.ArrayList;

/**
 * The purpose of this class is to reduce the amount of work it takes to iterate through a group of fractions.
 * With a 'base' fraction being the inverse square of an integer, most fractions instantiated by the main class
 * will be sums of those base fractions. One of the requirements defined by the problem is that each base fraction must
 * only be used once. Since we're looking at sums of fractions, it's important to check whether two fractions are sums
 * with one of these base fractions in common. One way to do this is to check before adding two fractions together
 * to see if they have share a base fraction.
 * 
 * Although checking can be done in constant time with the user of a bit vector, it would be quicker to break up the
 * into groups to reduce the number of times this needs to be done. One way to do this is to look at the smallest base
 * fraction in each sum and arrange fractions according to this. Since all fractions in a particular group share at least
 * one fraction (the smallest one), we know we'll never need to add fractions in this group together.
 * 
 * Unfortunately you can't have an ArrayList of an ArrayList because java doesn't allow for generic objects of generic
 * objects, so this class is necessary.
 * @author Drew
 *
 */
public class FractionGroup {
	private ArrayList<FactoredFraction> fractions;
	
	public FractionGroup() {
		fractions = new ArrayList<FactoredFraction>();
	}
	
	public void add(FactoredFraction f) {
		fractions.add(f);
	}
	
	public FactoredFraction get(int i) {
		return fractions.get(i);
	}
	
	public int size() {
		return fractions.size();
	}
	
	public String toString(int max) {
		String toString = "";
		for(int i = 0; i < fractions.size(); i ++) {
			toString += "[" + fractions.get(i).inSum(max) + "]";
			
			if(i < fractions.size() - 1) {
				toString += ", ";
			}
		}
		return toString;
	}
	
	public int lowestFractionDenominator(int max) {
		return fractions.get(0).getLowestFractionDenominator(max);
	}
	
	public boolean isEmpty() {
		return fractions.isEmpty();
	}
}
