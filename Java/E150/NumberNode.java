/**
 * 
 * @author Owner
 * Contains the value of a node plus the sum of all nodes leading up to it from each direciton
 *
 */
public class NumberNode {
	
		private int x, y;
		long value;
		private long sumLeft, sumUpLeft;
		
		public NumberNode(int x, int y, long val) {
			this.x = x;
			this.y = y;
			value = val;
		}

		public long getValue() {
			return value;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public long getSumUpLeft() {
			return sumUpLeft;
		}

		public void setSumUpLeft(long sumUpLeft) {
			this.sumUpLeft = sumUpLeft;
		}

		public long getSumLeft() {
			return sumLeft;
		}

		public void setSumLeft(long sumLeft) {
			this.sumLeft = sumLeft;
		}
}
	