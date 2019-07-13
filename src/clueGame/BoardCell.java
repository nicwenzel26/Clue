/*
 * Brennan Guthals & Nicholas Wenzel
 * 3/6/2019
 * ClueGame
 */
package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class BoardCell {
	public int row;

	public int column;
	
	public String type;
	
	private int x;
	
	private int y;
	
	public static int BOARD_CELL_SIZE = 30;
	
	public static int DOOR_SIZE = 5;
	
	protected boolean highlighted;

	public BoardCell(int row, int column, String room) {
		super();
		this.row = row;
		this.column = column;
		this.type = room;
		
		this.y = row * 30;
		this.x = column * 30;
	}
	
	public BoardCell() {
		
	}
	
	/*
	 * Function to determine if a specific cell is a door.
	 * Returns true if the length is 2 and the second character is not 'N'
	 * Else it returns false
	 */
	public boolean isDoorway() {
		if(type.length() == 2 && type.charAt(1) != 'N') {
			return true;
		}
		else {
			return false; 
		}
	}
	
	public boolean isWalkway() {
		String x = this.getInitial();
		if(getInitial().equals("W")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * Function for returning the direction of the door. 
	 * If the cell is a door, then the direction is determined by the second letter.
	 * Else NONE is returned.
	 */
	public DoorDirection getDoorDirection() {
		if(isDoorway()) {
			if(type.charAt(1) == 'R') {
				return DoorDirection.RIGHT;
				
			}
			else if(type.charAt(1) == 'L') {
				return DoorDirection.LEFT;
			}
			else if(type.charAt(1) == 'D') {
				return DoorDirection.DOWN;
			}
			else if(type.charAt(1) == 'U') {
				return DoorDirection.UP;
			}
			else {
				return DoorDirection.NONE;
			}
		}

		return DoorDirection.NONE;
	}

	public String getInitial() {
		return type.substring(0,1); 
	}
	
	/*
	 * Draws the boardcells one by one, coloring yellow if walkway and grey if not 
	 */
	public void draw(Graphics2D g) {
		
		if(this.highlighted) {
			g.setColor(Color.CYAN);
			g.fillRect(this.x, this.y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(this.x, this.y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
		}
		
		else if(isWalkway()) {
			g.setColor(Color.YELLOW);
			g.fillRect(this.x, this.y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
			g.setColor(Color.BLACK);
			g.drawRect(this.x, this.y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
		}


		else {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(this.x, this.y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);

		}
		/*
		 * If its a door, add a smaller rectangle orientaed apropriatly 
		 */
		if(isDoorway()) {
			g.setColor(Color.BLUE);
			if(getDoorDirection() == DoorDirection.RIGHT) {
				g.fillRect(this.x + 25, this.y, DOOR_SIZE, BOARD_CELL_SIZE);
			}
			if(getDoorDirection() == DoorDirection.LEFT) {
				g.fillRect(this.x, this.y, DOOR_SIZE, BOARD_CELL_SIZE);
			}
			if(getDoorDirection() == DoorDirection.UP) {
				g.fillRect(this.x, this.y, BOARD_CELL_SIZE, DOOR_SIZE);
			}
			if(getDoorDirection() == DoorDirection.DOWN) {
				g.fillRect(this.x, this.y + 25, BOARD_CELL_SIZE, DOOR_SIZE);
			}
		}
		
		/*
		 * Adds Labled to map
		 */
		Font f = new Font("TimesRoman", Font.BOLD, 13);
		
		g.setFont(f);
		
		g.setColor(Color.BLACK);
		
		drawRoomName("Cloak Room", 0, 60, g);
		drawRoomName("Drawing Room", 200, 80, g);
		drawRoomName("Kitchen", 380, 80, g);
		drawRoomName("Ballroom", 550, 80, g);
		drawRoomName("Gallery", 20, 240, g);
		drawRoomName("Wine Cellar", 220, 240, g);
		drawRoomName("Parking Garage", 550, 380, g);
		drawRoomName("Archery Range", 0, 420, g);
		drawRoomName("Stables", 200, 500, g);
		drawRoomName("Billiard Room", 300, 500, g);
		drawRoomName("Swimming Pool", 500, 550, g);

	}
	
	public void drawRoomName(String name, int x, int y, Graphics2D g) {
		g.drawString(name, x, y);
	}
	
	/*
	 * Function for setting the highlight status of the cell
	 */
	public void setPossibleTarget(boolean highlight) {
		this.highlighted = highlight;
	}
	
	




}