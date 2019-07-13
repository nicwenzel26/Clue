/*
 * Authors Nicholas Wenzel and Brennen Guthals
 */

package clueGame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotes extends JDialog {
	private Board board;
	/*
	 * Contstructor that sets up board, and sets size and layout
	 */
	public DetectiveNotes() {
		board = Board.getInstance();
		board.setConfigFiles("ClueMap.csv", "ClueMapLegend.txt");		
		board.initialize();
		
		makeCheckBoxes();
		
		setSize(600, 500);
		
		setLayout(new GridLayout(3,2));
		
	}
	/*
	 * Makes either a checkout panel or drop down panel and adds it to the dialog
	 */
	private void makeCheckBoxes() {
		add(makeBoxes("People", board.playerNames));
		add(makeComb("Person Guess", board.playerNames));
		add(makeBoxes("Rooms", board.roomNames));
		add(makeComb("Room Guess", board.roomNames));
		add(makeBoxes("Weapons", board.weapNames));
		add(makeComb("Weapons Guess", board.weapNames));
		
		
	}
	/*
	 * Makes drop down with a string and a arraylist of strings 
	 * sets preffered size and adds border. 
	 */
	private JPanel makeComb(String label, ArrayList<String> objects) {
		JPanel panel = new JPanel();
		
		panel.setPreferredSize(new Dimension(100, 50));
		
		panel.setLayout(new GridLayout(0,2));
		
		panel.setBorder(new TitledBorder(new EtchedBorder(), label));
		
		JComboBox<String> comb = new JComboBox();
		comb.addItem("I Don't Know");
		
		for(String object: objects) {
			comb.addItem(object);
		}
		
		panel.add(comb);
		
		return panel;
	}
	/*
	 * Makes a panel of checkboxes with a string name and arraylist of string as argumanets 
	 * sets prefered size and layout as well as adds border
	 */
	private JPanel makeBoxes(String label, ArrayList<String> objects) {
		JPanel panel = new JPanel();
		
		panel.setPreferredSize(new Dimension(150, 50));
		
		panel.setLayout(new GridLayout(0,2));
		
		panel.setBorder(new TitledBorder(new EtchedBorder(), label));
		
		for(String object: objects ) {
			JCheckBox box = new JCheckBox(object);
			
			panel.add(box);
		}
		
		return panel;
	}
}
