package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicButtonListener;

import clueGame.Board;
import clueGame.Solution;


public class GuessGUI extends JDialog {
	
	private Board board;
	
	public JButton submit;
	public JButton cancel;
	
	public Solution guess;
	
	public JComboBox<String> people;
	public JComboBox<String> weapons;
	
	public String roomName;
	
	public boolean go = false;
	
	
	
	public GuessGUI(String currentRoom) {
		setTitle("Make a Guess");
		
		setSize(400, 200);
		
		setLayout(new GridLayout(4, 2));
		
		setModal(true);
		
		board = Board.getInstance();
		
		roomName = currentRoom;
		
		//Label for the human players current room
		JLabel roomLabel = new JLabel("Room: ");
		
		add(roomLabel);
		
		//Text feild for showing the current room that will be used for the guess 
		JTextField roomField = new JTextField(currentRoom);

		//Making it so the user can not eddit this text field 
		roomField.setEditable(false);


		add(roomField); 

		people = new JComboBox();
		weapons = new JComboBox();


		//Iterate through names to be added to the drop down menue
		for(String person : board.playerNames) {
			people.addItem(person);
		}

		JLabel personLabel = new JLabel("Person:");

		//Added the people drop down menu
		add(personLabel);
		add(people);


		for(String weap : board.weapNames) {
			weapons.addItem(weap);
		}

		JLabel weapLabel = new JLabel("Weapon: ");

		//Adding the weapon drop down menu
		add(weapLabel);
		add(weapons);

		guess = new Solution();
		
		
		makeButtons();
		
		
		
	}
	
	
	private void makeButtons() {
		
		ButtonListener listener = new ButtonListener();
		
		submit = new JButton("Submit");
		
		cancel = new JButton("Cancel");
		
		//Adding a button listener to bothe the submit and cancel buttons
		submit.addActionListener(listener);
		cancel.addActionListener(listener);
		
		//Adding submit and canceld top the dialog box 
		add(submit);
		add(cancel);
		
		
		

	 
	    
	    		
	
	}
	
	private class ButtonListener implements ActionListener {
		private ButtonListener() {
			
		}
		
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == GuessGUI.this.submit) {
				GuessGUI.this.guess.person = GuessGUI.this.people.getSelectedItem().toString();
				GuessGUI.this.guess.weapon = GuessGUI.this.weapons.getSelectedItem().toString();
				GuessGUI.this.guess.room = GuessGUI.this.roomName;
				
				go = true; 
				
				GuessGUI.this.setVisible(false);
			}
			
			else if(event.getSource() == GuessGUI.this.cancel) {
				GuessGUI.this.setVisible(false);
			}
			
		}
	}

}
