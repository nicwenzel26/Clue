package tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {
	private static Board board;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueMap.csv", "ClueMapLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	@Before
	public void setUp() {
		board.resetVariables();
	}

	@Test
	public void testWalkway() {
		Set<BoardCell> adj = new HashSet<BoardCell>(); 

		//Testing walkway with only walkway cells around it
		//Should contain (11, 5) (11, 3) (12, 4) (10, 4)
		adj = board.getAdjList(11, 4);
		//Should have a size of 4
		assertEquals(4, adj.size());
		assertTrue(adj.contains(board.getCellAt(11, 5)));
		assertTrue(adj.contains(board.getCellAt(11, 3)));
		assertTrue(adj.contains(board.getCellAt(12, 4)));
		assertTrue(adj.contains(board.getCellAt(10, 4)));



		//Testing left had side of walkway should have 2 adjs being (10, 1) and (11, 0)
		adj = board.getAdjList(10, 0);
		assertTrue(adj.contains(board.getCellAt(10, 1)));
		assertTrue(adj.contains(board.getCellAt(11, 0)));

		assertEquals(2, adj.size());


		//Testing the right hand side of the walkway, should have 3 adjs being (6,21) (7,20) (8,21)
		adj = board.getAdjList(7, 21);
		assertTrue(adj.contains(board.getCellAt(6, 21)));
		assertTrue(adj.contains(board.getCellAt(7, 20)));
		assertTrue(adj.contains(board.getCellAt(8, 21)));
		assertEquals(3, adj.size());


		//Testing walkway that is adjacent to a room but not a door
		//Should have 3 adj being (9, 4) (7,4) (8,5)
		adj = board.getAdjList(8,4);
		assertTrue(adj.contains(board.getCellAt(9, 4)));
		assertTrue(adj.contains(board.getCellAt(7, 4)));
		assertTrue(adj.contains(board.getCellAt(8, 5)));
		assertEquals(3, adj.size());

		//Another walkway adj to a room but not a door 
		//Should have 2 adj being (0,3) and (1,4)
		adj = board.getAdjList(0, 4);
		assertTrue(adj.contains(board.getCellAt(0, 3)));
		assertTrue(adj.contains(board.getCellAt(1, 4)));
		assertEquals(2, adj.size());


	}


	@Test
	public void testInRoom() {
		Set<BoardCell> adj = new HashSet<BoardCell>();

		//Testing in top of room, should have no adj
		adj = board.getAdjList(0, 1);
		assertEquals(0, adj.size());

		//Testing in middle of the room, should have no adj
		adj = board.getAdjList(12, 19);
		assertEquals(0, adj.size());


		//Testing at right hand side of room, should have no adj
		adj = board.getAdjList(18, 21);
		assertEquals(0, adj.size());


	}

	@Test
	public void testAdjDoors() {
		//Test beside door to the left, should have adj 3, 
		//Should include (15,4) and (16,3) and (14,3)
		Set<BoardCell> adj = board.getAdjList(15, 3);
		assertTrue(adj.contains(board.getCellAt(16, 3)));
		assertTrue(adj.contains(board.getCellAt(14, 3)));
		assertTrue(adj.contains(board.getCellAt(15, 4)));
		assertEquals(3,adj.size());

		//Test beside door to the right, and up
		//Should contain (17, 12) (16, 13) (18, 13)
		adj = board.getAdjList(17, 13);
		assertTrue(adj.contains(board.getCellAt(17, 12)));
		assertTrue(adj.contains(board.getCellAt(16, 13)));
		assertTrue(adj.contains(board.getCellAt(18, 13)));
		assertEquals(3, adj.size());


		//Test beside down door
		//Should contain (5,14) (5, 12), (4,13) (6, 13)

		adj = board.getAdjList(5, 13);
		assertTrue(adj.contains(board.getCellAt(5, 14)));
		assertTrue(adj.contains(board.getCellAt(5, 12)));
		assertTrue(adj.contains(board.getCellAt(4, 13)));
		assertTrue(adj.contains(board.getCellAt(6, 13)));
		assertEquals(4, adj.size());


	}

	@Test
	public void testingDoorAdj() {
		//Testing up door
		//Should contain 1 (11,0)
		Set<BoardCell> adj = board.getAdjList(12, 0);
		assertTrue(adj.contains(board.getCellAt(11, 0)));
		assertEquals(1, adj.size());

		//Testing right door
		//Should contain 1, (17,13)
		adj = board.getAdjList(17, 12);
		assertTrue(adj.contains(board.getCellAt(17, 13)));
		assertEquals(1, adj.size());

		//Testing down door
		//Should contain (16,17)
		adj = board.getAdjList(15, 17);
		assertTrue(adj.contains(board.getCellAt(16, 17)));
		assertEquals(1, adj.size());

		//Testing left door 
		//Should contain (0,15)
		adj = board.getAdjList(0, 16);
		assertTrue(adj.contains(board.getCellAt(0, 15)));
		assertEquals(1, adj.size()); 

	}


	//Testing one step targets
	@Test
	public void testOneStep() {
		board.calcTargets(20, 3, 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(20, 2)));
		assertTrue(targets.contains(board.getCellAt(19, 3)));


		board.calcTargets(16, 17, 1);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 18)));
		assertTrue(targets.contains(board.getCellAt(16, 16)));
		assertTrue(targets.contains(board.getCellAt(15, 17)));

	}

	@Test
	public void testTwoSteps() {
		board.calcTargets(20, 3, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 3)));
		assertTrue(targets.contains(board.getCellAt(19, 2)));


		board.calcTargets(16, 17, 2);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 19)));
		assertTrue(targets.contains(board.getCellAt(16, 15)));
		assertTrue(targets.contains(board.getCellAt(15, 16)));
		assertTrue(targets.contains(board.getCellAt(15, 17)));
	}

	@Test
	public void testThreeSteps() {
		board.calcTargets(20, 3, 3);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(19, 3)));
		assertTrue(targets.contains(board.getCellAt(18, 2)));
		assertTrue(targets.contains(board.getCellAt(17, 3)));
		assertTrue(targets.contains(board.getCellAt(20, 2)));


		board.calcTargets(16, 17, 3);
		targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 20)));
		assertTrue(targets.contains(board.getCellAt(16, 14)));
		assertTrue(targets.contains(board.getCellAt(15, 15)));
		assertTrue(targets.contains(board.getCellAt(14, 16)));
		assertTrue(targets.contains(board.getCellAt(15, 17)));
	}


	@Test
	public void testFourSteps() {
		board.calcTargets(20, 3, 4);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(19,2)));
		assertTrue(targets.contains(board.getCellAt(16,3)));
		assertTrue(targets.contains(board.getCellAt(18,3)));
		assertTrue(targets.contains(board.getCellAt(17,2)));

		board.calcTargets(16, 17, 4);
		targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 21)));
		assertTrue(targets.contains(board.getCellAt(16, 13)));
		assertTrue(targets.contains(board.getCellAt(17, 14)));
		assertTrue(targets.contains(board.getCellAt(15, 14)));
		assertTrue(targets.contains(board.getCellAt(15, 16)));
		assertTrue(targets.contains(board.getCellAt(15, 17)));
		assertTrue(targets.contains(board.getCellAt(13, 16)));
		assertTrue(targets.contains(board.getCellAt(16, 15)));
	}


	@Test
	public void testFiveSteps() {
		board.calcTargets(20, 3, 5);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 2)));
		assertTrue(targets.contains(board.getCellAt(17, 3)));
		assertTrue(targets.contains(board.getCellAt(19, 3)));
		assertTrue(targets.contains(board.getCellAt(15, 3)));
		assertTrue(targets.contains(board.getCellAt(20, 2)));
		board.calcTargets(16, 17, 5);
		targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCellAt(14,16)));
		assertTrue(targets.contains(board.getCellAt(15,15)));
		assertTrue(targets.contains(board.getCellAt(15,17)));
		assertTrue(targets.contains(board.getCellAt(16,14)));
		assertTrue(targets.contains(board.getCellAt(15,13)));
		assertTrue(targets.contains(board.getCellAt(17,14)));
		assertTrue(targets.contains(board.getCellAt(17,13)));


	}


	@Test
	public void testSixSteps() {
		board.calcTargets(20, 3, 6);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 3)));
		assertTrue(targets.contains(board.getCellAt(15, 4)));
		assertTrue(targets.contains(board.getCellAt(16, 3)));
		assertTrue(targets.contains(board.getCellAt(17, 2)));
		assertTrue(targets.contains(board.getCellAt(19, 2)));
		assertTrue(targets.contains(board.getCellAt(18, 3)));

		board.calcTargets(16, 17, 6);
		targets = board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCellAt(15, 14)));
		assertTrue(targets.contains(board.getCellAt(16, 13)));
		assertTrue(targets.contains(board.getCellAt(15, 12)));
		assertTrue(targets.contains(board.getCellAt(16, 15)));
		assertTrue(targets.contains(board.getCellAt(15, 17)));
		assertTrue(targets.contains(board.getCellAt(15, 16)));
		assertTrue(targets.contains(board.getCellAt(13, 16)));
		assertTrue(targets.contains(board.getCellAt(18, 13)));
		assertTrue(targets.contains(board.getCellAt(11, 16)));
		assertTrue(targets.contains(board.getCellAt(17, 14)));
		assertTrue(targets.contains(board.getCellAt(17,12)));
	}

	@Test 
	public void testRoomExit() {
		board.calcTargets(12, 0, 1);
		Set<BoardCell> targets = board.getTargets();

		assertTrue(targets.contains(board.getCellAt(11, 0)));
		assertEquals(1, targets.size());


		board.calcTargets(0, 16, 2); 
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(1, 15)));
		assertEquals(1,targets.size());
	}

}
