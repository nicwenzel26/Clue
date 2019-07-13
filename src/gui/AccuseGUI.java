package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import clueGame.Board;
import clueGame.Solution;

public class AccuseGUI extends JDialog{

	public Board board;
	
	public JComboBox<String> people;
	
	public JComboBox<String> rooms;
	
	public JComboBox<String> weapons;
	
	public Solution accusation;
	
	public JButton submit;
	
	public JButton cancel;
	
	public boolean go = false; 
	
	public AccuseGUI() {
		setTitle("Make an Accusation");
		
		setSize(400,200);
		
		setLayout(new GridLayout(4,2));
		//Setting modal to be true so the program has to pause before it continues 
		setModal(true);
		
		board = Board.getInstance();
		
		JLabel roomLabel = new JLabel("Room:");
		
		add(roomLabel);
		
		rooms = new JComboBox();
		
		
		for(String room: board.roomNames) {
			rooms.addItem(room);
		}
		
		add(rooms);
		
		
		people = new JComboBox();
		
		for(String person : board.playerNames) {
			people.addItem(person);
		}
		
		JLabel personLabel = new JLabel("Person: ");
		
		add(personLabel);
		add(people);
		
		weapons = new JComboBox();
		
		for(String weap : board.weapNames) {
			weapons.addItem(weap);
		}
		
		JLabel weapLabel = new JLabel("Weapons: ");
		
		add(weapLabel);
		add(weapons);
		
		
		accusation = new Solution();
		
		makeButtons();
		
		
	}

	private void makeButtons() {
		
		ButtonListener listener = new ButtonListener();
		
		//Adding two buttons submit and cancel
		
		submit = new JButton("Submit");
		
		cancel = new JButton("Cancel");
		
		//Adding action listener to both the submit and cancel buttons
		submit.addActionListener(listener);
		cancel.addActionListener(listener);
		
		//Addd submit and cancel buttons to the dialog
		add(submit);
		add(cancel);
		
	}
	
	/*
	 * Button listener class that handels when the cancel and submit buttons are pressed 
	 */
	private class ButtonListener implements ActionListener {
		private ButtonListener() {
			
		}
		
		public void actionPerformed(ActionEvent event) {
			
			//If the source of the button press is the submit button, assign the solution and close the window
			if(event.getSource() == AccuseGUI.this.submit) {
				AccuseGUI.this.accusation.person = AccuseGUI.this.people.getSelectedItem().toString();
				AccuseGUI.this.accusation.room = AccuseGUI.this.rooms.getSelectedItem().toString();
				AccuseGUI.this.accusation.weapon = AccuseGUI.this.weapons.getSelectedItem().toString();
				
				go = true;
				
				AccuseGUI.this.setVisible(false);
			}
			//If they press cancel close the window 
			else if(event.getSource() == AccuseGUI.this.cancel) {
				AccuseGUI.this.setVisible(false);
			}
		}
	}
}
