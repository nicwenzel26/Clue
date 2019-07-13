/*
 * Authors: Nicholas Wenzel and Brennen Guthals
 */

package clueGame;

import java.awt.Color;

import gui.ControlGUI;
import gui.GuessGUI;



public class HumanPlayer extends Player {
	
	public Solution suggestion;
	public GuessGUI guessGUI;
	
	public HumanPlayer() {
		super();
	}
	
	/*
	 * Constructor for the human player taking in a name, color and starting location
	 */
	public HumanPlayer(String name, int row, int col, Color color) {
		super(name,row,col,color);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see clueGame.Player#move(clueGame.Board)
	 * 
	 * Function that starts the move process for the human, player
	 * sets the finished status to flase and highlights the possibel targets
	 */
	@Override
	public void move(Board board) {
		finished = false;
		
		board.highlightSquare(true);
		
	}
	
	/*
	 * Returns if the player has finished the turn or not. 
	 */
	public boolean getFinStatus() {
		return finished;
	}
	
	/*
	 * (non-Javadoc)
	 * @see clueGame.Player#finishTurn(clueGame.BoardCell)
	 * 
	 * Function that sets the new player location to the clicked on cell and sets the finsihed
	 * status to true so the next player button functions. 
	 */
	
	@Override
	public void finishTurn(BoardCell clicked) {
		setRow(clicked.row);
		setColumn(clicked.column);
		

		finished = true;
	}
}

