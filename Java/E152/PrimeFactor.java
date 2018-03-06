public class PrimeFactor {
		
	private int base, exponent;
	public PrimeFactor(int base, int exponent) {
		 this.base = base;
		 this.exponent = exponent;
	}
	public int getExponent() {
		return exponent;
	}
	public int getBase() {
		return base;
	}
	public int getValue() {
		return (int)Math.pow(base, exponent);
	}
	
}