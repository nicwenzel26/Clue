/*
 * Initializing board that contains a grid of board cells, 
 * sets of visited and target lists, and methods for calculating the targets. 
 */

package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.sun.javafx.collections.MappingChange.Map;

public class IntBoard {
	private HashMap<BoardCell, Set<BoardCell>> adjMtx;

	private Set<BoardCell> visited;

	private Set<BoardCell> targets;

	private BoardCell[][] grid;
	
	/*
	 * Constructor for IntBoard, initializes adjMtx, visited, targets, grid
	 * and calls calc adjecencies. 
	 */
	public IntBoard() {

		adjMtx = new HashMap<BoardCell, Set<BoardCell>>();

		visited = new HashSet<BoardCell>(); 

		targets = new HashSet<BoardCell>();

		grid = new BoardCell[4][4];
		
		for(int row = 0; row < 4; row++) {
			for(int col = 0; col < 4; col++) {
				grid[row][col] = new BoardCell(row, col);
			}
		}

		calcAdjacencies();


	} 
	/*
	 * Function for calculating the adjecencies of the given cells in the grid of cells
	 */

	public void calcAdjacencies() {
		for(int row = 0; row < 4; row++) {
			for(int col = 0; col < 4; col++) {
				determineAdj(row,col);
			}
		}
	
	}
	/*
	 * Calculating the adjeceneis fro each cell given a row and col
	 * 
	 */
	public void determineAdj(int row, int col) {
	    Set<BoardCell> adj = new HashSet();
	    if (row - 1 >= 0) {
	      adj.add(grid[(row - 1)][col]);
	    }
	    if (row + 1 < 4) {
	      adj.add(grid[(row + 1)][col]);
	    }
	    if (col - 1 >= 0) {
	      adj.add(grid[row][(col - 1)]);
	    }
	    if (col + 1 < 4) {
	      adj.add(grid[row][(col + 1)]);
	    }
	    adjMtx.put(grid[row][col], adj);
	}
	
	
	/*
	 * Function for setting the adjList given a specific cell 
	 */
	public Set<BoardCell> getAdjList(BoardCell cell) {
		Set<BoardCell> adjList	= adjMtx.get(cell);

		return adjList;


	}
	
	/*
	 * Function that takes in a start cell and the dice roll and populates the list 
	 * of potential targets. 
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		int numSteps = pathLength; 
		Set<BoardCell> adjList = getAdjList(startCell);
		
		visited.add(startCell); 
		
		for( BoardCell adj : adjList) {
			if(visited.contains(adj)) {
				continue; 
			}
			
			else {
				visited.add(adj);
				if(numSteps == 1) {
					targets.add(adj);
				}
				
				else {
					calcTargets(adj,numSteps - 1);
				}
			}
			
			visited.remove(adj); 
	}

	}
	
	
	public Set<BoardCell> getTargets() {
		return(targets);
		
	}
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
}

