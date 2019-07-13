/*
 * Brennan Guthals & Nicholas Wenzel
 * 3/6/2019
 * ClueGame
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.AccuseGUI;
import gui.ControlGUI;
import gui.GuessGUI;

import java.util.*;



public class Board extends JPanel implements MouseListener{
	private static final int MAX_BOARD_SIZE = 100; 
	private int numRows;
	private int numColumns; 

	public BoardCell[][] board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE]; 

	public HashMap<Character, String> legend = new HashMap<Character, String>();

	public HashMap<BoardCell, Set<BoardCell>> adjMatrix;

	public Set<BoardCell> targets; 

	public Set<BoardCell> visited;

	public ArrayList<Card> deck = new ArrayList<Card>();

	public ArrayList<Player> players = new ArrayList<Player>();
	
	public ArrayList<String> playerColors = new ArrayList<String>();
	
	public ArrayList<String> playerNames = new ArrayList<String>();
	
	public ArrayList<String> roomNames = new ArrayList<String>();
	
	public ArrayList<String> weapNames = new ArrayList<String>();

	public String boardConfigFile;

	public String roomConfigFile;
	
	public Player currentPlayer;
	
	public int currentPlayerNumber;
	
	public int firstRoll;
	

	private Solution answer;
	//Variable used for testing
	public String playerDisprove;

	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		answer = new Solution();
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	public void resetVariables() {
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
	}
	public void initialize() {
		try {
			
			//Loading the rooms weaons and people from config files
			loadRoomConfig();

			loadBoardConfig();

			loadPeopleConfig("Player1.txt");

			loadPeopleConfig("Player2.txt"); 

			loadPeopleConfig("Player3.txt");

			loadPeopleConfig("Player4.txt");

			loadPeopleConfig("Player5.txt");

			loadPeopleConfig("Player6.txt");

			loadWeaponConfig();



		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (BadConfigFormatException e) {
			e.getMessage();		}
		
		//Calculate the cell adjacencies
		calcAdjacencies(); 
		
		//Deal the cards as evenly as possible between each of the 6 players
		dealCards();
		
		//Add a mouse listener object to the board, calls mouse specific functions
		addMouseListener(this);
		
		//Set the current player number to 0 and the current player to the human
		currentPlayerNumber = 0;
		
		currentPlayer = players.get(currentPlayerNumber);
		
		
		
		Random rand =  new Random();
		
		//Roll random d6 for the first round since next player can not be called
		firstRoll = rand.nextInt(6) + 1;
		
		//Calculate the possible targets for the human players first turn, subsequent calls will be handled by nextPlayer
		calcTargets(currentPlayer.getRow(),currentPlayer.getColumn(), firstRoll);
		
		//Highlight possible choices for human player
		highlightSquare(true);
		
		
		
	}

	/*
	 * Function for returning the legend of the board given the roomConfigFile(Legend)
	 * File reader reads in the file stopping at commas and new lines, and puts the 
	 * Character and String from the legend file into the legend map. 
	 */

	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException {

		FileReader reader = new FileReader(roomConfigFile);
		Scanner in = new Scanner(reader); 
		in.useDelimiter(",|\\n");
		while(in.hasNextLine()) {
			String character = in.next();
			String roomName = in.next();
			
			if(!roomName.contains("Closet") && !roomName.contains("Walk")) {
				roomNames.add(roomName);
			}
			
			String card = in.next();
			card = card.substring(1); 

			if((!card.contains("Card") && (!card.contains("Other")))) {
				throw new BadConfigFormatException("Not a valid type: " + card);
			}

			roomName = roomName.substring(1); 

			if(card.contains("Card")) {
				Card newCard = new Card(roomName, CardType.ROOM);
				deck.add(newCard);
			}
			char c = character.charAt(0); 

			legend.put(c, roomName);
		}

	}

	/*
	 * Function for loading in the Board Configuration File 
	 * Reads in the file and splits it into an array at commas
	 * Sets the number of cols based on size of row.
	 * Iterates through until there is no next line in the file 
	 * Makes a board cell of the current col and row and puts it into the board
	 * Updates the number of rows in the board
	 */

	public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(boardConfigFile);
		Scanner in = new Scanner(reader); 

		int rows = 0;

		while(in.hasNextLine()) {
			String line = in.nextLine();
			String[] row = line.split(",");
			if(rows == 0) {
				numColumns = row.length; 
			}
			else if(numColumns != row.length) {
				throw new BadConfigFormatException("Rows do not have the same number of columns.") ;
			}

			for(int i = 0; i < numColumns; i++) {
				String cha = row[i];
				Character c = cha.charAt(0);

				String room = legend.get(c);
	

				if(room == null) {
					throw new BadConfigFormatException("Room type not defined " + cha);
				}

				board[rows][i] = new BoardCell(rows, i, cha); 
			}

			rows = rows + 1; 

		}

		numRows = rows; 

	}

	/*
	 * Functions for loading the files for the people in the game 
	 */
	public void loadPeopleConfig(String player) throws FileNotFoundException {
		FileReader reader = new FileReader(player);
		Scanner in = new Scanner(reader);

		String PlayerName;

		PlayerName = in.nextLine();
		
		playerNames.add(PlayerName); 

		String startingLoc = in.nextLine(); 

		String[] line = startingLoc.split(" ");



		String playerColor = in.nextLine();

		playerColors.add(playerColor);
		
		Color play_Color = convertColor(playerColor);

		String pType = in.nextLine();

		if(pType.contains("Human")) {
			HumanPlayer human = new HumanPlayer();

			human.setPlayerName(PlayerName);
			human.setColor(play_Color);
			human.setRow(Integer.parseInt(line[0]));
			human.setColumn(Integer.parseInt(line[1]));
			human.setType(PlayerType.HUMAN);

			players.add(human);
		}

		else {
			ComputerPlayer comp = new ComputerPlayer();
			comp.setPlayerName(PlayerName);
			comp.setColor(play_Color);
			comp.setRow(Integer.parseInt(line[0]));
			comp.setColumn(Integer.parseInt(line[1]));
			comp.setType(PlayerType.COMPUTER);

			players.add(comp); 

		}

		Card card = new Card(PlayerName, CardType.PERSON);

		deck.add(card);


	}


	/*
	 * Functions for loading the weapons config files 
	 */
	public void loadWeaponConfig() throws FileNotFoundException {
		FileReader reader = new FileReader("Weapons.txt"); 
		Scanner in = new Scanner(reader); 

		while(in.hasNextLine()) {
			String weapon = in.nextLine();
			
			weapNames.add(weapon);

			Card card = new Card(weapon, CardType.WEAPON);

			deck.add(card); 
		}
	}

	/*
	 * Function for taking in two file names and assinging them to the board Configuration file and the
	 * room configuration file. 
	 */
	public void setConfigFiles(String board, String room) {
		boardConfigFile = board;

		roomConfigFile = room;

	}

	/*
	 * Returns the legend
	 */
	public Map<Character, String> getLegend() {
		return legend;

	}


	/*
	 * Returns number of rows in board
	 */
	public int getNumRows() {
		return numRows;
	}

	/*
	 * Returns number of cols in board
	 */
	public int getNumColumns() {
		return numColumns;
	}
	/*
	 * Returns the cell given a row and col 
	 */
	public BoardCell getCellAt(int row, int col) {
		return board[row][col]; 
	}

	public void calcTargets(int i, int j, int numSteps) {
		BoardCell startCell = getCellAt(i,j);
		targets = new HashSet();
		visited = new HashSet();
		visited.add(startCell);

		findTargets(i, j, numSteps);
	}

	/*
	 * Function for finding the targets given the row column and the number of steps 
	 */

	public void findTargets(int i, int j, int numSteps) {
		BoardCell startCell = getCellAt(i,j);		
		Set<BoardCell> list = (Set)adjMatrix.get(startCell);
		for (BoardCell adjCell : list)
		{
			if (!visited.contains(adjCell))
			{
				visited.add(adjCell);

				if (adjCell.isDoorway()) {
					targets.add(adjCell);
				}
				else if (numSteps == 1) {
					targets.add(adjCell);
				}
				else {
					findTargets(adjCell.row,adjCell.column, numSteps - 1);

				}
				visited.remove(adjCell);
			}
		}
	}

	/*
	 * Returns the targets set of Board Cells
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}


	/*
	 *Calculates the adjacencies of the given cell 
	 */
	public void calcAdjacencies() {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numColumns;  col++) {
				addAdj(row,col);
			}
		}
	}

	/*
	 * Adds the adjacencies into the adj matrix 
	 */

	public void addAdj(int row, int col) {
		Set<BoardCell> adj = new HashSet();
		BoardCell cell = getCellAt(row,col);

		if(cell.isWalkway()) {
			//Check if door is facing the right direction
			caseWalkway(row-1,col,adj,DoorDirection.DOWN);
			caseWalkway(row+1,col,adj,DoorDirection.UP);
			caseWalkway(row,col-1,adj,DoorDirection.RIGHT);
			caseWalkway(row,col+1,adj,DoorDirection.LEFT);
		}
		else if(cell.isDoorway()) {
			DoorDirection direction = cell.getDoorDirection();
			caseDoor(row,col,adj,direction);
		}

		adjMatrix.put(cell, adj);
	}
	/*
	 * Function for walkways 
	 */

	public void caseWalkway(int row, int col, Set<BoardCell> nextTo, DoorDirection direction) {
		if ((row < 0) || (col < 0) || (row >= numRows) || (col >= numColumns)) {
			return;
		}
		BoardCell cell = board[row][col];

		if (cell.isWalkway()) {
			nextTo.add(cell);
		}
		else if (cell.isDoorway())
		{
			DoorDirection dir = cell.getDoorDirection();
			if (dir == direction) {
				nextTo.add(cell);
			}
		}
	}

	/*
	 * Case for doorways
	 */

	public void caseDoor(int row, int col, Set<BoardCell> nextTo, DoorDirection direction) {
		if ((direction == DoorDirection.DOWN) && (row + 1 < numRows) && (board[(row + 1)][col].isWalkway())) {
			nextTo.add(board[(row + 1)][col]);
		}
		else if ((direction == DoorDirection.UP) && (row - 1 >= 0) && (board[(row - 1)][col].isWalkway())) {
			nextTo.add(board[(row - 1)][col]);
		}
		else if ((direction == DoorDirection.LEFT) && (col - 1 >= 0) && (board[row][(col - 1)].isWalkway())) {
			nextTo.add(board[row][(col - 1)]);
		}
		else if ((direction == DoorDirection.RIGHT) && (col + 1 < numColumns) && (board[row][(col + 1)].isWalkway())) {
			nextTo.add(board[row][(col + 1)]);
		}
	}

	/*
	 * Returns the adjacenies list 
	 */
	public Set<BoardCell> getAdjList(int i, int j) {
		Set<BoardCell> list = new HashSet();
		BoardCell cell = getCellAt(i,j);
		list = adjMatrix.get(cell);
		return list;
	}

	//Function to allow for testing
	public void setAnswer(Solution solution) {
		answer.person = solution.person;
		answer.room = solution.room;
		answer.weapon = solution.weapon;
	}

	public void selectAnswer() {

	}
	
	/*
	 * Function for handleing the suggestions made by players. It takes in a sugesstion and accusing player
	 * Determines if the card is in the hand of the other players and lets the players know by the Contol GUI
	 * if the suggestion can be disproven.
	 */
	public Card handleSuggestion(Solution suggestion, Player accusingPlayer) {
		int numAsked = 0;

		int currentPlayer = players.indexOf(accusingPlayer);
		
		/*
		 * Go through the players hands and see if the suggestion can be disproven
		 */
		while(numAsked < players.size()) {
			currentPlayer = (currentPlayer + 1) % players.size();
			Player player = (Player)players.get(currentPlayer);

			if(player != accusingPlayer) {
				Card card = new Card();
				card = player.disproveSuggestion(suggestion);

				if(card != null) {
					for(Player p: players) {
						p.updateSeenCards(card);
					}
					playerDisprove = player.getPlayerName();
					
					//If someone can disprove push the information that was disproven to the control GUI
					ControlGUI.guessResField.setText(card.getCardName());
					
					return card;
				}
			}
			
			numAsked++;
		}
		
		//If nobody can disprove the sugesttion let the players know by pushing the infromation to the control GUI
		ControlGUI.guessResField.setText("Nobody can disprove");
		
		return null;
	}

	public void updatePlayers(ArrayList<Player> players) {
		this.players = players;
	}
	/*
	 * Funstion for testing if the accusation made by human or computer is correct
	 */
	public boolean checkAccusation(Solution accusation) {
		
		int numRight = 0;
		if(accusation.person.contains(answer.person)) {
			numRight++;
		}
		if(accusation.room.contains(answer.room)) {
			numRight++;
		}
		if(accusation.weapon.contains(answer.weapon)) {
			numRight++;
		}
		if(numRight == 3) {
			return true;
		}

		return false; 
	}
	/*
	 * Function for dealing the cards to each player
	 * Random selectiion of cards and goes to each player sequentially. 
	 */

	public void dealCards() {
		ArrayList<Card> vist = new ArrayList<Card>();
		int playerCount = 0;
		int randPerson = new Random().nextInt(playerNames.size());
		answer.person = playerNames.get(randPerson);
		String personName = playerNames.get(randPerson);
		
		int randWeapon = new Random().nextInt(weapNames.size());
		answer.weapon = weapNames.get(randPerson);
		String weaponName = weapNames.get(randPerson);
		
		int randRoom = new Random().nextInt(roomNames.size());
		answer.room = roomNames.get(randPerson);
		String roomName = roomNames.get(randPerson);
		
		while(vist.size() != 20) {
			int num = new Random().nextInt(deck.size());
			Card card = deck.get(num);

			if (vist.contains(card)) {
				continue;
			}
			else if(card.getCardName() == personName || card.getCardName() == weaponName || card.getCardName() == roomName) {
				continue;
			}
			else {

				Player player = players.get(playerCount);

				player.hand.add(card);

				vist.add(card);

				players.set(playerCount, player);

				playerCount++;

				if(playerCount == 6) {
					playerCount = 0;
				}

			}
		}




	}


	/*
	 * Function for converting a string to a color
	 */
	public Color convertColor(String strColor) {
		Color color;

		try {
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color)field.get(null);

		} catch (Exception e) {
			color = null;
		}
		return color;
	}

	public String getPlayerDisprove() {
		return playerDisprove;
	}
	
	/*
	 * Paint function that calls draw on the boardcells and the players
	 */
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; 
		
		drawBoardCells(g2); 
		drawPlayers(g2);
	}
	/*
	 * For each item in board call the draw function of board cell
	 */
	private void drawBoardCells(Graphics2D g) {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				board[i][j].draw(g);
			}
		}
		
	}
	/*
	 * For each player, call the draw function of player
	 */
	public void drawPlayers(Graphics2D g) {
		for(Player p: this.players) {
			p.drawPlayer(g, this); 
		}
		
	}
	
	/*
	 * Function that tells the board to set a highlight condition to a cell if it is in the target list 
	 * and is passed in a true value
	 */
	public void highlightSquare(boolean highlight) {
		if(this.targets != null) {
			for(BoardCell cell : this.targets) {
				cell.setPossibleTarget(highlight);
			}
		}
	}
	/*
	 * Function for handeling when the next player button is pushed. Updates the current player and changes the displayed
	 * name
	 */
	public void nextPlayer() {
		highlightSquare(false);
		
		//If the current players finished status is false have a pop up that prompts the user to finihs their turn
		if(!currentPlayer.finished) {
			JOptionPane.showMessageDialog(null, "You must finish your turn");
			
			return;

		}
		
		//Reseting the Control GUI feilds so it only shows the relvent information from the current turn rather than
		//Having the information stick arount on subsequent turns 
		ControlGUI.guessField.setText("");
		ControlGUI.guessResField.setText("");
		
		
		currentPlayerNumber = (currentPlayerNumber + 1);
		
		//If the current player number value is greater than 5 reset to zero
		if(currentPlayerNumber == 6) {
			currentPlayerNumber = 0;
		}
		
		
		currentPlayer = players.get(currentPlayerNumber);
		
		
		//Setting the control GUI text field with the current player
		ControlGUI.turnField.setText(currentPlayer.getPlayerName());
		
		Random rand = new Random();
		
		//Rolling a dice bewtween 1 and 6
		int roll = rand.nextInt(6) + 1;
		
		//Updating control GUI Roll field with the random roll
		ControlGUI.rollValField.setText(Integer.toString(roll));
		
		//Calculating the possible targets for the current player
		calcTargets(currentPlayer.getRow(), currentPlayer.getColumn(), roll);
		
		//Move the current player different between computer and human
		currentPlayer.move(this);
		
		//Repaint the board with the new player locations and highlighted squares if the currentPlayer is human
		repaint();
		
	
	}
	
	/*
	 *Function for handeling the accusations of the human players 
	 */
	public void accuse() {
		
		boolean result = false;
		
		//If the current player is not human then the player can not click the make accusation buttons
		if(currentPlayer.type != PlayerType.HUMAN) {
			JOptionPane.showMessageDialog(null, "You can not accuse when it is not your turn");
			return;
		}
		
		AccuseGUI accuseGUI = new AccuseGUI();
		
		accuseGUI.setVisible(true);
		
		//If the marker has been set then test the accusation
		if(accuseGUI.go) {
			Solution acusation = accuseGUI.accusation;
			
			//Test the accusation
			result = checkAccusation(acusation);
			
			//If the player is correct show message and close the window
			if(result) {
				JOptionPane.showMessageDialog(null, "WOO! You win!");
				
				System.exit(0);
			}
			
			//If they are wrong show the message 
			else {
				JOptionPane.showMessageDialog(null, "Sorry, not correct.");
			}
		}
		
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 * A mouse event function determines if the clicked position by the player is valid. 
	 * 
	 */
	public void mouseClicked(MouseEvent event) {
		
		
		//Getting the clicked on board cell
		BoardCell clicked = getClicked(event.getX(), event.getY());
		
		//If the getClicked function returns null then the user clicked on a space the is not valid
		if (clicked == null) {
			
			//If the user selected a non valid target square have a pop up that prompts for reselection
			JOptionPane.showMessageDialog(null, "That is not a valid target");
		}
		
		//If the user selected a valid cell then call the current playters finishe turn function and unhighlight squares
		else {
			currentPlayer.finishTurn(clicked);
			
			highlightSquare(false);
			
			//Repaint the board with the unhighlighted squares
			repaint();
			
			if(clicked.isDoorway()) {
				String initial = clicked.getInitial();
				
				String currentRoom = null;
				
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
				//When in the room add a guess gui
				GuessGUI guessGUI = new GuessGUI(currentRoom);
				
				
				guessGUI.setVisible(true);
				
				
				//If the marker is set then set the solution and handle the suggestion to see if another player can disprove
				if(guessGUI.go) {
					ControlGUI.guessField.setText(guessGUI.guess.person + ", " + guessGUI.guess.room + ", " + guessGUI.guess.weapon);
					handleSuggestion(guessGUI.guess, this.currentPlayer);
				}
				
			}
		}
	}
	
	/*
	 * Function fo calculating the board cell the user clicked, first get the x and y pixel values that
	 * the user clicked and divide them by the board cell size to determine what cell the user clicked 
	 * 
	 */
	private BoardCell getClicked(int clickedX, int clickedY) {
		int row = clickedY / BoardCell.BOARD_CELL_SIZE;
		int col = clickedX / BoardCell.BOARD_CELL_SIZE;
		
		BoardCell clicked = board[row][col];
		
		//If the clicked cell is in the targets list return that cell
		if(targets.contains(clicked)) {
			return clicked;
		}
		
		//Else return null
		else {
			return null;
		}
	}
	
	/*
	 *Functions below track when the mouse is clicked and then authomatically calls the mouse listener function
	 *
	 *
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	
}

