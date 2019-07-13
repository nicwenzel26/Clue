/*
 * Brennan Guthals & Nicolas Wenzel
 * 3/21/2019
 * CSCI306 ClueGame
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public abstract class Player {

	private String playerName;

	private int row;

	private int column; 

	private Color color; 

	public PlayerType type;

	public ArrayList<Card> hand = new ArrayList();

	public ArrayList<Card> seenCards = new ArrayList();
	
	public boolean flag = false;
	
	public boolean finished = false;

	public Player() {

	}

	public Player(String name, int row, int col, Color color) {
		this.playerName = name;
		this.row = row;
		this.column = col;
		this.color = color;
	}

	public Card disproveSuggestion(Solution suggestion) {
		int numMatching = 0;
		ArrayList<Card> matchingCards = new ArrayList();
		for(Card c : hand) {
			if(c.getCardName().equals(suggestion.person)) {
				matchingCards.add(c);
			}
			else if(c.getCardName().equals(suggestion.room)) {
				matchingCards.add(c);
			}
			else if(c.getCardName().equals(suggestion.weapon)) {
				matchingCards.add(c);
			}
		}
		if(matchingCards.size() == 1) {
			return matchingCards.get(0);
		}
		else if(matchingCards.size() > 1) {
			Random rand = new Random();
			int index = rand.nextInt(matchingCards.size());
			return matchingCards.get(index);
		}
		return null;
	}



	public void addCard(Card card) {
		hand.add(card);
	}

	public void updateSeenCards(Card card) {
		seenCards.add(card);
	}


	public String getPlayerName() {
		return playerName;
	}


	public int getRow() {
		return row;
	}


	public int getColumn() {
		return column;
	}


	public Color getColor() {
		return color;
	}
	public PlayerType getType() {
		return type;
	}

	public void setType(PlayerType type) {
		this.type = type;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	/*
	 * Draw function for player that draws it at set location and sets its color
	 * and draws an outline
	 */
	public void drawPlayer(Graphics2D g, Board board) {
		int size = BoardCell.BOARD_CELL_SIZE;
		
		int x = this.column * size;
		int y = this.row * size;
		
		g.setColor(this.color);
		g.fillOval(x, y, size, size);
		g.setColor(Color.BLACK);
		g.drawOval(x, y, size, size);
		
	}
	
	public abstract void move(Board board);

	public abstract void finishTurn(BoardCell cell);

}