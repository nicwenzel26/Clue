/*
 * Nicholas Wenzel and Brennen Guthals 
 */

package gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;

public class PlayerGUI extends JPanel {
	
	private ArrayList<Card> cards;
	
	
	private Board board;
	
	/*
	 * Function for generating the side panel that shows the human players cards
	 */
	public PlayerGUI(Board boardFromClueGame) {
		
		board = boardFromClueGame;
		
		//Setting layout to grid of 6 by 1
		setLayout(new GridLayout(6,1));
		
		//Makes titled eteched border with the title "My Cards"
		setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		
		cards = board.players.get(0).hand;
		
		//Adds people cards panel
		makePanels("People", CardType.PERSON);
		
		//Adds weapon cards panel
		makePanels("Weapons", CardType.WEAPON);
		
		//Adds room cards panel
		makePanels("Rooms", CardType.ROOM);
		
		
		
	}
	
	/*
	 * Function to make each of the panels for each card type, takes in a string that will be the title 
	 * of each panel and the type of card 
	 */
	private void makePanels(String label, CardType type) {
		
		JPanel panel = new JPanel();
		
		//Sets layout of panel to grid layout of adjustable rows and one col
		panel.setLayout(new GridLayout(0, 1));
		
		//Make etched titled border with title equal to the passed in string
		panel.setBorder(new TitledBorder(new EtchedBorder(), label));
		
		
		/*
		 * For each card in the human players deck see if it is of the type we are looking at right now,
		 * if it is make a new text feild with that cards name and add it to the panel
		 */
		for (Card card : cards) {
			if (card.getCardType() == type) {
				JTextField field = new JTextField(card.getCardName());
				
				//Making it so that the user can not edit this text box
				field.setEditable(false);
				
				//adds the text feild to the panel
				panel.add(field);
				
			}
		}
		//Adds the current panel to the overall panel of the PLayerGUI
		add(panel);
		
	}
}
	

