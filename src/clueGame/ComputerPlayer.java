/*
 * Authors Nicholas Wenzel and Brennan Guthals
 */

package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

import gui.ControlGUI;

public class ComputerPlayer extends Player {

	private char lastVisitedRoom;

	private Solution suggestion;
	
	private Solution accusation;

	private String currentRoom;
	
	public ComputerPlayer() {
		seenCards = new ArrayList();
		suggestion = new Solution();
	}
	/*
	 * Constructor for newComputer players with name, color and starting location
	 */
	public ComputerPlayer(String name, int row, int col, Color color) {
		super();
		this.setPlayerName(name);
		this.setRow(row);
		this.setColumn(col);
		this.setColor(color);
		seenCards = new ArrayList();
		suggestion = new Solution();
	}
	
	/*
	 * Function for picking the next location of the computer players
	 */
	public BoardCell pickLocation(Set<BoardCell> targets) {
		Random rand = new Random();
		
		BoardCell pick = new BoardCell();
		
		int counter = 0;
		
		int index = rand.nextInt(targets.size());
		for(BoardCell cell : targets) {
			//See if room, and if it was last visited, if not set as last visited and pick room
			if(cell.isDoorway() && (cell.getInitial().charAt(0) != lastVisitedRoom)) {
				lastVisitedRoom = cell.getInitial().charAt(0);
				return cell;
			}

			if(index == counter) {
				pick = cell;
			}
			counter++;

		}


		return pick; 
	}

	//If computer makes correct accusation, tells player that they won and ends game, if computer makes incorrect accusation, tells player computer made wrong guess
	public void makeAccusation(Solution accusation) {
		boolean didWin = Board.getInstance().checkAccusation(accusation);
		
		if(didWin == true) {
			JOptionPane.showMessageDialog(null, "The computer won with a correct guess of " + accusation.person + " " + accusation.room + " " + accusation.weapon);
			System.exit(0);
		}
		else if(didWin == false) {
			JOptionPane.showMessageDialog(null, "The computer made an incorrect accusation of " + accusation.person + " " + accusation.room + " " + accusation.weapon);
			
		}
	}
	/*
	 * Function that handles creating suggestions for computer players. Takes in a string as a room
	 * and makes a guess based on the seen cards in its hand and from previous suggestings 
	 */
	public void createSuggestion(String room) {
		
		//Makes the players current room the suggested room. 
		suggestion.room = room;

		ArrayList<Card> people= new ArrayList();

		ArrayList<Card> weapons = new ArrayList();

		for(Card c : Board.getInstance().deck) {
			boolean notSeen = false;
			int counter = 0;
			String deckCardName = c.getCardName();
			for(Card card : seenCards) {
				
				String seenCardName = card.getCardName();

				if(seenCardName.contains(deckCardName)) {
					continue;
				}
				
				if(c.type.equals(CardType.ROOM)) {
					continue;
				}
				counter++;
			}
			
			if(counter == seenCards.size()) {
				
				if(c.type.equals(CardType.PERSON)) {
					people.add(c);
				}
				
				else if(c.type.equals(CardType.WEAPON)) {
					weapons.add(c);
				}
			}
		}

		Random rand = new Random();
		
		//Choosing random person to suggest
		int random = rand.nextInt(people.size());
		suggestion.person = people.get(random).getCardName();
		
		//When a computer player makes a suggestion it moves the player it suggested to the room it is currently in 
		//Allows for computer players to go into more rooms than just the two they happen to be nearest at the start of the game. 
		for(Player p: Board.getInstance().players) {
			if(suggestion.person.contains(p.getPlayerName())) {
				p.setRow(this.getRow());
				p.setColumn(this.getColumn());
			}
		}
		
		//Choosing random weapon to suggest 
		random = rand.nextInt(weapons.size());
		suggestion.weapon = weapons.get(random).getCardName();

	}
	
	//Check to see if computer has seen any card in a suggestion
	public boolean haveCard(Solution suggestion) {
		String suggestionRoom = suggestion.room;
		String suggestionWeapon = suggestion.weapon;
		String suggestionPerson = suggestion.person;
		
		for(Card card : seenCards) {
			if(card.getCardName().equals(suggestionRoom)) {
				return true;
			}
		}
		
		for(Card card : seenCards) {
			if(card.getCardName().equals(suggestionPerson)) {
				return true;
			}
		}
		
		for(Card card : seenCards) {
			if(card.getCardName().equals(suggestionWeapon)) {
				return true;
			}
		}
		
		return false;
	}

	//Function to allow for proper testing
	public void setLastVisited(char c) {
		this.lastVisitedRoom = c;
	}

	public Solution getSuggestion() {

		return null;
	}

	public String getSuggestedRoom() {
		return suggestion.room;
	}

	public String getSuggestedWeapon() {
		return suggestion.weapon;
	}

	public String getSuggestedPerson() {
		return suggestion.person;
	}
	/*
	 * (non-Javadoc)
	 * @see clueGame.Player#move(clueGame.Board)
	 * 
	 * Function for moving the computer players. Uses the pickLocation function and the current targets list 
	 * and sets the new location to one of these possible targets. 
	 */
	@Override
	public void move(Board board) {
		//If nobody could disprove last suggestion, make accusation;
		if(flag == true) {
			makeAccusation(accusation);
		}
		
		//Default finished is true for computer players
		finished = true;
		
		Set<BoardCell> targets = Board.getInstance().targets;
		
		BoardCell newLoc = pickLocation(targets);
		
		//Setting new location
		setRow(newLoc.row);
		setColumn(newLoc.column);
		
		//Determining the room that was last visited
		lastVisitedRoom = newLoc.getInitial().charAt(0); 
		
		//If the current location is a room start the suggestion process
		if(newLoc.isDoorway()) {
			String initial = newLoc.getInitial();
			switch(initial) {
			case "C":
				currentRoom = "Cloak Room";
				break;
			case "G":
				currentRoom = "Gallery";
				break;
			case "A":
				currentRoom = "Archery Range";
				break;
			case "D":
				currentRoom = "Drawing Room";
				break;
			case "I":
				currentRoom = "Wine Cellar";
				break;
			case "S":
				currentRoom = "Stables";
				break;
			case "R":
				currentRoom = "Billiard Room";
				break;
			case "K":
				currentRoom = "Kitchen";
				break;
			case "B":
				currentRoom = "Ballroom";
				break;
			case "P":
				currentRoom = "Parking Garage";
				break;
			case "L":
				currentRoom = "Swimming Pool";
				break;
			}
			
			//Creating a suggestion once in the room and pushing the information to the control GUI
			createSuggestion(currentRoom);
			ControlGUI.guessField.setText(suggestion.person + " " + suggestion.room + " " + suggestion.weapon);
			
			
			//Set flag if nobody can disprove suggestion. Next turn, computer will make use that suggestion as an accusation
			if(Board.getInstance().handleSuggestion(suggestion,this) == null && haveCard(suggestion) == false) {
				accusation = new Solution();
				accusation = suggestion;
				flag = true;
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see clueGame.Player#finishTurn(clueGame.BoardCell)
	 * 
	 * In computer player this function does nothing becuase the movement is autonomouse
	 */
	@Override
	public void finishTurn(BoardCell cell) {
		
	}

}
