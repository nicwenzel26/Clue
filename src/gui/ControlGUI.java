/*Authors Nicholas Wenzel and Brennen Guthals
 * 
 */

package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Board;


public class ControlGUI extends JPanel {
	
	private JButton nextPlayerButton;
	private JButton accuseButton;
	
	public static JTextField turnField;
	public static JTextField rollValField;
	public static JTextField guessField;
	public static JTextField guessResField;
	
	public ControlGUI() {
		setLayout(new GridLayout(2,0)); 
		
		
		
		add(makeButtons());
		
		add(makeInfoBoxes());
		
		
	}
	
	/*
	 * Functions for making the buttons on the control panel. It also add the label and text feild that goes to the 
	 * right of the buttons
	 */
	private JPanel makeButtons() {
		JPanel panel = new JPanel();
		ButtonListener listener = new ButtonListener();
		//Setting the panels layout to have 1 row and a adjustable number of cols
		panel.setLayout(new GridLayout(1,0));
		
		panel.add(whoseTurn()); 
		
		//Creating button for the next player
		nextPlayerButton = new JButton("Next Player");
		
		nextPlayerButton.addActionListener(listener);
		
		panel.add(nextPlayerButton);
		
		//Creating a button to make an accusation
		accuseButton = new JButton("Make Accusation");
		
		accuseButton.addActionListener(listener);
		
		panel.add(accuseButton); 
		
		
		return panel;
	}
	
	/*
	 * Function for making the whose turn area of the control gui. Creates new panel with lable and returns
	 * that panel to be added to the main panel. 
	 */
	private JPanel whoseTurn() {
		JPanel panel = new JPanel();
		
		//Label for the current turn box
		JLabel whoLabel = new JLabel("Whose Turn?");
		
		panel.add(whoLabel); 
		
		//Text field set to a length of 20
		turnField = new JTextField(20);
		
		turnField.setText(Board.getInstance().currentPlayer.getPlayerName());
		
		//Making the field uneditable by the player
		turnField.setEditable(false);
		
		panel.add(turnField);
		
		return(panel); 
		
	}
	
	private JPanel makeInfoBoxes() {
		JPanel panel = new JPanel();
		
		//Adding the roll/die box
		panel.add(rollInfo());
		
		//Adding the information about the current guess of the current player
		panel.add(guessInfo());
		
		//Adding the information about the result of the current guess from the current player
		panel.add(guessResInfo());
		
		return panel;
	}
	
	/*
	 * Function for make the box that contains the dice roll information for the current player
	 */
	private JPanel rollInfo() {
		
		JPanel panel = new JPanel();
		
		//Setting the panels layout to be a grid with one row and 2 cols 
		panel.setLayout(new GridLayout(1, 2));
		
		//Label for the current dice roll
		JLabel rollLabel = new JLabel("Roll");
		
		panel.add(rollLabel);
		
		//Text field to show the current roll
		rollValField = new JTextField(5);
		
		//Making it so the player can not edit the text box
		rollValField.setEditable(false);
		
		rollValField.setText(Integer.toString(Board.getInstance().firstRoll));
		
		panel.add(rollValField);
		
		//Setting this panel to hava a border, etched, and the title of "Die"
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		
		return panel;
		
		
	}
	
	/*
	 * Function for adding the panel with the infromation about the current guess of the current player. 
	 * Adds label and text box for showing the current guess 
	 */
	private JPanel guessInfo() {
		JPanel panel = new JPanel();
		
		//Setting the layout for the current panel to be a grid with 2 rows and an adjustable number of cols 
		panel.setLayout(new GridLayout(2,0));
		
		//Label for the current guess text field
		JLabel guessLabel = new JLabel("Guess");
		
		panel.add(guessLabel);
		
		//Text feild for showing the current guess of the current player
		guessField = new JTextField(25);
		
		//Making it so the user can not edit the text box
		guessField.setEditable(false);
		
		panel.add(guessField);
		
		//Making a border that is etched with the title "Guess"
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		
		return panel;
		
		
	}
	
	/*
	 * Function for adding the panel about the guess result from the current player, had a label and a text field. 
	 */
	private JPanel guessResInfo() {
		JPanel panel = new JPanel();
		
		//Setting the layout for the panel as grid layour with 1 row and 2 cols
		panel.setLayout(new GridLayout(1,2));
		
		//Label for the response based on the current guess 
		JLabel guessResLabel = new JLabel("Response");
		
		panel.add(guessResLabel);
		
		//Text field for the response to the guess
		guessResField = new JTextField(15); 
		
		//Making the text feild uneditable 
		guessResField.setEditable(false);
		
		panel.add(guessResField);
		
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result:"));
		
		return panel;

	}
	
	
	public static void controlNextPlayer() {
		Board.getInstance().nextPlayer();
	}
	
	public static void accusePlayer() {
		Board.getInstance().accuse();
	}
	
	
	private class ButtonListener implements ActionListener {
		private ButtonListener() {
			
		}
		
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == ControlGUI.this.nextPlayerButton) {
				controlNextPlayer();
			}
			
			else if(event.getSource() == ControlGUI.this.accuseButton) {
				accusePlayer();
			}
			
		}
	}
}


