/*
 * Brennan Guthals & Nicholas Wenzel
 * 3/6/2019
 * ClueGame
 */
package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class FileInitTests {
	//Set selected variables to expected values
	public static final int LEGEND_SIZE = 13;
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 21;
	public static final int NUM_DOORS = 14;
	private static Board board;

	@BeforeClass
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueMap.csv", "ClueMapLegend.txt");		
		board.initialize();
	}

	//Iterate through the whole map and see if BoardCell is a doorway, and count number of doorways
	//After the for loop, see if number of doors counted is the number expected
	@Test
	public void testNumberOfDoorways() {
		int doors = 0;

		for(int i = 0; i < board.getNumRows(); i++) {
			for(int j = 0; j < board.getNumColumns(); j++) {
				BoardCell holder = board.getCellAt(i, j); 
				if(holder.isDoorway() == true) {
					doors++;
				}
			}
		}
		assertEquals(doors,NUM_DOORS);
	}

	//Test each room and see if getInitial returns the expected initial
	@Test
	public void testRoomInitials() {

		assertEquals("C", board.getCellAt(0, 0).getInitial());
		assertEquals("A", board.getCellAt(12, 0).getInitial());
		assertEquals("D", board.getCellAt(0, 5).getInitial());
		assertEquals("S", board.getCellAt(20, 4).getInitial());
		assertEquals("R", board.getCellAt(20,10).getInitial());
		assertEquals("K", board.getCellAt(0, 12).getInitial());
		assertEquals("B", board.getCellAt(0, 16).getInitial());
		assertEquals("P", board.getCellAt(9,17).getInitial());
		assertEquals("L", board.getCellAt(18,14).getInitial());
		assertEquals("X", board.getCellAt(10,13).getInitial());
	}

	//Test to see if getNumRows and getNumColumns returns the expected numbers
	//The subract 1 is due to the numbered edges that we were instructed to place on the the csv
	@Test
	public void testRowsColumns() {
		
		assertEquals(NUM_ROWS, board.getNumRows() - 1);
		assertEquals(NUM_COLUMNS, board.getNumColumns() - 1);
	}

	//Test that getDoorDirection is returning expected values by picking a door of each enum type: UP, DOWN, RIGHT, LEFT, NONE
	@Test
	public void testDoorDirections() {
		BoardCell room = board.getCellAt(4, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(4, 5);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(9,21);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		room = board.getCellAt(17,12);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(14, 14);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(0, 3);
		assertFalse(cell.isDoorway());	
	}

	@Test
	public void testLegend() throws FileNotFoundException{
		FileReader reader = new FileReader("ClueMapLegend.txt");
		Scanner in = new Scanner(reader);
		//Set numLines equal to one since we grab first line before loop
		int numLines = 1;
		String firstLine = "";
		String lastLine = "";
		firstLine = in.nextLine();
		while(in.hasNextLine()) {
			numLines++;
			lastLine = in.nextLine();
		}
		//Check to see that legend size is as expected, and check to see if first and last line has been correctly read.
		assertEquals(numLines, LEGEND_SIZE);
		assertEquals("C, Cloak Room, Card", firstLine);
		assertEquals("X, Closet, Other", lastLine);
	}

}

