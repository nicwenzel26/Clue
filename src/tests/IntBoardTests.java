package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import experiment.BoardCell;
import experiment.IntBoard;

public class IntBoardTests {
	/*
	 * Testing board space 0,0, it should include a space to the right, (0,1)
	 * and a space below (1, 0) and should have a size of 2.
	 */
	@Test
	public void testAdj0() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(0,0);

		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}

	/*
	 * Testing bottom right corner, cell (3, 3). It should have a cell above at (2,3)
	 * and a cell to the left at (3,2) and should have a length of 2
	 */

	@Test
	public void testAdj33() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(3,3);

		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertEquals(2, testList.size());
	}

	/*
	 * Testing point on the right edge of the board (1,3), it should contain a space 
	 * above at (0,3), a space down at (2, 3) and a space to the left (1, 2) and its length 
	 * should be three. 
	 */

	@Test
	public void testAdj13() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(1,3);

		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(3, testList.size());
	}

	/*
	 * Testing a point on the left edge of the board, cell (2,0), this should containt 
	 * a cell above at (1,0), a cell below at (3,0) and a cell to the right at (2,1)
	 * and should have a length of three. 
	 */

	@Test
	public void testAdj20() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(2,0);

		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(3, 0)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(3, testList.size());
	}

	/*
	 * Testing cell (1,1), this should have a cell above at (0,1), a cell below at (2,1)
	 * a cell to the right at (1,2) and a cell to the left at (1,0) and should have 
	 * a size of 4
	 */

	@Test
	public void testAdj11() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(1,1);

		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertEquals(4, testList.size());
	}
	/*
	 * Testing the point (2,2), it should have a point above at (1,2) at point below
	 * at (3,2) a point to the right at (2,3) and a point to the left (2,1) and should
	 * have a length of 4. 
	 */
	@Test
	public void testAdj22() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(2,2);

		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(4, testList.size());
	}
	/*
	 * Testing a starting location of (0,0) and a dice roll of 2
	 * Target list should include the spaces of (0,2), (1,1), and (2,0)
	 */
	@Test
	public void testTargets0_2() {

		IntBoard board = new IntBoard(); 

		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));

	}
	/*
	 * Testing a starting location of (0,0) and a roll of 1 shouod be the same 
	 * as the adj list. 
	 */
	@Test
	public void testTargets0_1() {

		IntBoard board = new IntBoard(); 

		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	/*
	 * Testing a starting location of (0,0) and a roll of 3
	 */
	@Test
	public void testTargets0_3() {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}

	/*
	 * Testing the starting point of (2,2) with a roll of a 4
	 * Should have 7 target locations. 
	 */

	@Test
	public void testTargets22_4() {

		IntBoard board = new IntBoard(); 

		BoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 4);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
	}
	/*
	 * Testing a start location of (2,2) with a roll of 3
	 */
	@Test
	public void testTargets22_5() {

		IntBoard board = new IntBoard(); 

		BoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	
	/*
	 * Testing starting position (2,2) and a roll of 6
	 * Should have 7 possible targets. 
	 */
	@Test
	public void testTargets22_6() {

		IntBoard board = new IntBoard(); 

		BoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 6);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
	}



}
