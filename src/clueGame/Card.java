/*
 * Brennan Guthals & Nicolas Wenzel
 * 3/21/2019
 * CSCI306 ClueGame
 */

package clueGame;

public class Card {
	private String cardName;

	public CardType type;
	public boolean equals() {
		return false; 
	}

	public Card(String name, CardType type){
		this.cardName = name;
		this.type = type;
	}
	
	public Card() {
		
	}

	public String getCardName() {
		return cardName;
	}
	
	public CardType getCardType() {
		return type;
	}
	
 

}
