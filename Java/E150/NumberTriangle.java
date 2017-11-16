/**
 * The objective here is to take a bunch of pre-set integers placed in a trianglular formation and find the minimum sub
 * triangle for the whole thing. 
 * 
 * My solution works by looking at all negative numbers individually. For each one, it expands the triangle to the right
 * by adding all adjacent numbers in the right diagonal, checking the sum then continuing to expand until it's out of
 * diagonals to add.
 * 
 * Next it takes the expanded triangle and expands it down in a similar way. To avoid redundancy, if the left most point of the
 * next triangle is negative, the algorithm will stop and go to the next triangle.
 * 
 * Since each expansion increases the number of nods that need to be added to the sum, this can be quite time consuming as each
 * node in each line has to be added to the sum and a line can have up to 1000 numbers in it. To combat this, each node
 * stores its own value as well as the sum of all nodes leading up to it from the left and upper left. This means
 * that adding a line to the sum is done in O(1) time.
 * 
 * I should also mention that this was not the original solution I submitted to Project Euler. In my original code,
 * I actually didn't consider that I could simply sum up all nodes before a particular node and instead had pre defined values
 * which determined the number of nodes that would be summed up, and each node would have several of these breakpoints. This made
 * my solution far more complex and it actually took longer than brute force solutions, even though there were far fewer additions being
 * done. This is likely due to the increase in overhead. It wasn't until after I submitted my solution that I realized there was a much easier
 * way.
 */

import java.util.ArrayList;
public class NumberTriangle {
	private ArrayList<NumberNode> negatives;
	
	NumberNode[] numberTriangle;
	private int numberOfRows;
	
	public NumberTriangle() {
		numberTriangle = new NumberNode[500500];
		negatives = new ArrayList<NumberNode>();
		
		long t = 0;
		int x = 0;
		int y = 0;
		
		//Use the algorithm provided to create the triangle
		for(int k = 1; k <= 500500; k ++) {
			t = (long) ((615949*t + 797807) % Math.pow(2, 20));
			long sk = (long) (t - Math.pow(2, 19));
			createNumberNode(sk, x, y);
			
			//Need to keep track of x and y because we're using a single array
			x ++;
			if(x > y) {
				x = 0;
				y ++;
			}
		}
		
		System.out.println(solve());
	}
	
	public long solve() {
		long min = 0;
		
		//Look at all the negative numbers in the triangle
		for(int i = 0; i < negatives.size(); i ++) {
			long sum = 0;
			
			NumberNode current = negatives.get(i);
			//x and y coordinates of the top, left and right nodes
			int aX = current.getX();
			int bX = current.getX();
			int cX = current.getX();
			int aY = current.getY();
			int bY = current.getY();
			int cY = current.getY();
			
			//In case the smallest sub triangle is a single node
			long currentValue = current.getValue();
			if(currentValue < min) {
				min = current.getValue();
			}
			
			sum += currentValue;
			
			//Keep expanding the current triangle to the right by adding the diagonal until a wall is hit
			boolean breakCond = false;
			while(!breakCond) {
				if(cX >= cY) {
					breakCond = true;
				} else {
					sum += expandRight(aX, aY, cX, cY);
					
					if(sum < min) {
						min = sum;
					}
					
					//The coordinates for the top and rightmost nodes in the next triangle
					aX ++;
					aY --;
					cX ++;
					
					get(aX, aY);
				}
			}
			
			//With the triangle expanded as far right as possible, now expand it down until we hit the bottom or a negative number
			breakCond = false;
			while(!breakCond) {
				if(bY >= numberOfRows - 1) {
					break;
				}
				NumberNode nextB = get(bX, bY + 1);
				if(nextB.getValue() < 0) {
					//If the left most value in a triangle is negative, the one we're about
					//to check will actually be checked later when we examine that node, so don't bother here
					breakCond = true;
				} else {
					sum += expandDown(bX, bY, cX, cY);
					
					if(sum < min) {
						min = sum;
					}
					
					//The coordinates for the leftmost and rightmost nodes in the next triangle
					bY ++;
					cX ++;
					cY ++;
					
					get(aX, aY);
				}
			}
		}
		
		return min;
	}
	
	//For a triangle with left point bx, by and right point cx, cy, find the 
	//sum of the bottom line
	private long expandDown(int bX, int bY, int cX, int cY) {
		int newBX = bX;
		int newCX = cX + 1;
		int newCY = cY + 1;
		
		//The number of nodes between the bottom left and bottom right
		int numNodes = newCX - newBX + 1;
		
		long sum = sumLine(newCX, newCY, numNodes, false);
		return  sum;
	}
	
	//For a triangle with top aX, aY and right point cx, cY, find the sum of the
	//rightmost diagonal
	private long expandRight(int aX, int aY, int cX, int cY) {
		int newAY = aY - 1;
		int newCX = cX + 1;
		int newCY = cY;
		
		//The number of nodes between the top and bottom right
		int numNodes = newCY - newAY + 1;

		long sum = sumLine(newCX, newCY, numNodes, true);
		
		return sum;
	}
	
	//Get the sum of a number of nodes equal to numNodes up to and including
	//the node at x, y. This can either be a horizontal line (upLeft = false) or
	//a diagonal line (upLeft = true);
	private long sumLine(int x, int y, int numNodes, boolean upLeft) {
		NumberNode current = get(x, y);
		long sum;
		
		int shift = x - numNodes;

		if(upLeft) {
			sum = current.getSumUpLeft();
			
			//If the number of nodes that need to be summed up is greater than the number of nodes
			//in the row up to this point
			if(shift > 0) {
				//We need to remove the sum of a the first few nodes
				NumberNode removedNode = get(shift, y - numNodes);
				sum -= removedNode.getSumUpLeft();
			}
		} else {
			sum = current.getSumLeft();
			
			if(shift > 0) {
				NumberNode removedNode = get(shift, y);
				sum -= removedNode.getSumLeft();
			}
		}
		
		return sum;
	}
	
	//Creates a node with value val at coordinates x and y, also computes the sum up to that point
	private void createNumberNode(long val, int x, int y) {
		NumberNode newNode = new NumberNode(x, y, val);
		set(newNode, x, y);//place the new node in the number triangle
		if(val < 0) {
			negatives.add(newNode);
		}
		
		//If this is the first node in its row, its sum is just itself
		if(x == 0) {
			newNode.setSumLeft(val);
			newNode.setSumUpLeft(val);
		} else {
			
			//Get the sum of the previous node and add it to the new one
			NumberNode leftNode = get(x - 1, y);
			NumberNode upLeftNode = get(x - 1, y - 1);
			
			long leftSum = leftNode.getSumLeft();
			long upLeftSum = upLeftNode.getSumUpLeft();
			
			newNode.setSumLeft(leftSum + val);
			newNode.setSumUpLeft(upLeftSum + val);
		}
	}
	
	//Place a node in the number triangle. Since we're using a 1D array, we have to
	//use this method to find its spot
	private void set(NumberNode n, int x, int y) {
		if(y == 0) {
			numberTriangle[0] = n;
		} else {
			numberTriangle[(y*(y+1) / 2) + x] = n;
		}
	}
	
	public NumberNode get(int x, int y) {
		if(x > y) {
			return null;
		}
		
		if(y == 0) {
			return numberTriangle[0];
		}
		
		return numberTriangle[(y*(y+1) / 2) + x];
	}
}
