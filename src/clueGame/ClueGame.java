/*
 * Authors: Nicholas Wenzel and Brennen Guthals
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.ControlGUI;
import gui.PlayerGUI;

public class ClueGame extends JFrame{
	private Board board;
	
	private ControlGUI control;
	
	private PlayerGUI playerGUI;
	
	private DetectiveNotes detNotes = new DetectiveNotes();
		
	public ClueGame() {
		setup();
		
		
	}
	
	/*
	 * Sets up the board and calls the gui function
	 */
	private void setup() {
		board = Board.getInstance();
		board.setConfigFiles("ClueMap.csv", "ClueMapLegend.txt");		
		//board.initialize();
		
		GUI();
		
	}
	/*
	 * sets close operation, size and calls the boards draw function 
	 */
	private void GUI() {
		setTitle("Clue Game!");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(this.board, BorderLayout.CENTER);
		
		setSize(820, 880);
		
	 
		
		playerGUI = new PlayerGUI(this.board);
		
		add(playerGUI, BorderLayout.EAST);
		
		
		control = new ControlGUI();
		
		add(control, BorderLayout.SOUTH);
		
		makeMenu();
		
		
		//makeSplash();
		
		
		
	}
	
	/*
	 * Function for making the splash screen, lets the user know which character they are and what color your piece 
	 * is, in addition to telling them how to move the turn fowrward. 
	 */
	private static void makeSplash() {
		JOptionPane.showMessageDialog(null, "You are " + Board.getInstance().playerNames.get(0) + " and your color is " + Board.getInstance().playerColors.get(0) + " , click a target square to play.", "Welcome to Clue!", JOptionPane.INFORMATION_MESSAGE);

	}
	
	/*
	 * Makes the menu bar unpopulated
	 */
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuBar.add(createFileMenu());
		
	}
	/*
	 * Adds file option to menu
	 */
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(makeDectNotes());
		menu.add(makeFileExit());
		return menu;
	}
	/*
	 * Adds detective notes to menu
	 */
	private JMenuItem makeDectNotes() {
		JMenuItem dect = new JMenuItem("Show Detective Notes");

		dect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClueGame.this.detNotes.setVisible(true);
			}
		});
		return dect;
	}
	/*
	 * Adds exit to file menu
	 */

	private JMenuItem makeFileExit() {
		JMenuItem exit = new JMenuItem("Exit");

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return exit;
	}

	public static void main(String[] args) {
		ClueGame clueGame = new ClueGame();
		clueGame.setVisible(true);
		clueGame.makeSplash();
	}
}
