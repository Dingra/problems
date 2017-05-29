/*
 * The problem asks for the savings you would achieve when changing the example given into what's called a minimum spanning tree.
 * 
 * This problem has been solved already so it's just a matter of implementing a known solution. The one I chose was the reverse-delete method.
 * 
 * The reverse delete method works by sorting all of the edges in the tree in descending order by weight. Each edge is "removed" from the graph and the
 * graph is checked for connectedness. If the graph is not connected, the edge is added back in. If it is, it stays removed and the weight of that edge is
 * added to the total savings
 */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class SpanningTree {
	
	private ArrayList<Edge> edgelist;//a list of all edges in a spanning tree
	private int[][] matrix;//A matrix representation of a spanning tree
	private int numberOfVertices;
	
	/*
	 * A small graph for which the max savings are known is used for testing to make sure that the
	 * algorithm was implemented properly
	 */
	public SpanningTree(boolean test) {
		edgelist = new ArrayList<Edge>();
		
		//Add all of the edges as their own entities
		edgelist.add(new Edge(0, 1, 16));
		edgelist.add(new Edge(0, 2, 12));
		edgelist.add(new Edge(0, 3, 21));
		edgelist.add(new Edge(1, 3, 17));
		edgelist.add(new Edge(1, 4, 20));
		edgelist.add(new Edge(2, 3, 28));
		edgelist.add(new Edge(2, 5, 31));
		edgelist.add(new Edge(3, 4, 18));
		edgelist.add(new Edge(3, 5, 19));
		edgelist.add(new Edge(3, 6, 23));
		edgelist.add(new Edge(4, 6, 11));
		edgelist.add(new Edge(5, 6, 27));
		
		numberOfVertices = 7;
		matrix = new int[numberOfVertices][numberOfVertices];
		
		for(int i = 0; i < numberOfVertices; i ++) {
			for(int j = 0; j < numberOfVertices; j ++) {
				matrix[i][j] = 0;//Weight of 0 means there's no edge
			}
		}
		
		//Create the matrix to check for connectedness
		matrix[0][1] = 16;
		matrix[0][2] = 12;
		matrix[0][3] = 21;
		matrix[1][3] = 17;
		matrix[1][4] = 20;
		matrix[2][3] = 28;
		matrix[2][5] = 31;
		matrix[3][4] = 18;
		matrix[3][5] = 19;
		matrix[3][6] = 23;
		matrix[4][6] = 11;
		matrix[5][6] = 27;
		
		matrix[1][0] = 16;
		matrix[2][0] = 12;
		matrix[3][0] = 21;
		matrix[3][1] = 17;
		matrix[4][1] = 20;
		matrix[3][2] = 28;
		matrix[5][2] = 31;
		matrix[4][3] = 18;
		matrix[5][3] = 19;
		matrix[6][3] = 23;
		matrix[6][4] = 11;
		matrix[6][5] = 27;
	}
	
	public SpanningTree(String f, int num) {
		edgelist = new ArrayList<Edge>();
		
		//I suppose I should find the number of vertices by scanning the file once and counting the number of lines...
		//but whatever. This is less work and I know for a fact that I'm not going to need to scan a file that's bigger than
		//what I have so I'm just going to hard code the number in the main class.
		numberOfVertices = num;
		
		matrix = new int[numberOfVertices][numberOfVertices];
		
		String filename = f;
		
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			String currentLine;
			
			try {
				int count = 0;
				while ((currentLine = br.readLine()) != null) {
					
					String[] lineArr = currentLine.split(",");
					
					for(int i = 0; i < numberOfVertices; i ++) {
						if(!lineArr[i].equals("-")) {
							int currentWeight = Integer.parseInt(lineArr[i]);
							
							//each edge must only be in the list once or it'll be counted twice
							if(matrix[i][count]!= 0) {
								edgelist.add(new Edge(count, i, currentWeight));//add to the edge list
							}
							
							matrix[count][i] = currentWeight;
						}
					}
					
					count ++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Find the sum of the edges of the minimum spanning tree and return it
	 * 
	 * The method used is the reverse-delete method which sorts the edges by weight in descending order and tests
	 * to see if removing each edge would cause the tree to become unconnected. If it does not, the edge is removed
	 * from the actual tree
	 */
	public int solveMinSpanningTree() {
		Collections.sort(edgelist);
		
		int savings = 0;
		
		//Iterate through all of the edges, starting with the one with the greatest weight
		for(int i = 0; i < edgelist.size(); i ++) {
			Edge currentEdge = edgelist.get(i);
			
			int v1 = currentEdge.getVertex(1);
			int v2 = currentEdge.getVertex(2);
			
			int[][] matrixClone = new int[numberOfVertices][numberOfVertices];
			
			//Create a copy of the current matrix. The edge can be removed from this one to test for connectedness
			for(int j = 0; j < numberOfVertices; j ++) {
				for(int k = 0; k < numberOfVertices; k ++) {
					matrixClone[j][k] = matrix[j][k];
				}
			}
				
			//Remove the current edge
			matrixClone[v1][v2] = 0;
			matrixClone[v2][v1] = 0;
			
			//If the graph is still connected when the current edge is removed, remove it from the orignal matrix
			if(isConnected(matrixClone)) {
					
				savings += currentEdge.getWeight();//Add the weight to the total savings
				
				//Remove the edge from the matrix and the edgelist
				matrix[v1][v2] = 0;
				matrix[v2][v1] = 0;
			}
		}
		return savings;
	}
	
	/**
	 * Determine whether the graph currentMatrix is connected, ie, it does not have any isolated nodes of pockets of nodes
	 * @return true if it is connected, false otherwise
	 */
	public boolean isConnected(int[][] currentMatrix) {
		//Keeps track of the vertices that have been found. Once the tree has been traversed, if all nodes
		//are found by DFS, the graph is connected. Otherwise, it is not
		int[] foundVertices = new int[numberOfVertices];
		
		//Initially set all vertices to be "not found"
		for(int i = 1; i < numberOfVertices; i ++) {
			foundVertices[i] = 0;
		}
		
		depthFirstSearch(currentMatrix, 0, foundVertices);
		
		//If any vertex is not found by DFS then it can't be part of the tree, thus the graph is not connected
		for(int i = 0; i < numberOfVertices; i ++) {
			if(foundVertices[i] == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	//Implementation of the Depth First Search algorithm
	public int[] depthFirstSearch(int[][] currentMatrix, int currentVertex, int[] foundVertices) {
		
		foundVertices[currentVertex] = 1;
		
		for(int i = 0; i < numberOfVertices; i ++) {
			if(currentMatrix[currentVertex][i] > 0) {//If the current vertex is connected to vertex i
				currentMatrix[currentVertex][i] = 0;//remove the edge by setting its weight to zero
				currentMatrix[i][currentVertex] = 0;//Have to do it for both occurrances of the edge in the matrix
				if(foundVertices[i] == 0) {//make sure that we are not re-checking a node that's already been checked
					foundVertices =  depthFirstSearch(currentMatrix, i, foundVertices);//Continue searching
				}
			}
		}
		
		return foundVertices;
	}
	
}
