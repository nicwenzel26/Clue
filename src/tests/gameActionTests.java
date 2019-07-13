/*
 * Brennan Guthals & Nicholas Wenzel
 * 3/6/2019
 * ClueGame
 */
package tests;

import static org.junit.Assert.*;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import org.junit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;


public class gameActionTests {

	public static Board board;

	//Room cards
	public static Card cloakRoomCard = new Card("Cloak Room", CardType.ROOM);
	public static Card archeryRoomCard = new Card("Archery Room", CardType.ROOM);
	public static Card kitchenCard = new Card("Kitchen", CardType.ROOM);
	public static Card stablesCard = new Card("Stables", CardType.ROOM);
	public static Card ballroomCard = new Card("Ballroom", CardType.ROOM);
	//People cards
	public static Card amosCard = new Card("Amos Lee", CardType.PERSON);
	public static Card garrusCard = new Card("Garrus Vakarian", CardType.PERSON);
	public static Card jimCard = new Card("Jim Holden", CardType.PERSON);
	public static Card taliCard = new Card("Tali Zorah", CardType.PERSON);
	public static Card malCard = new Card("Mal Reynolds", CardType.PERSON);
	public static Card johnCard = new Card("John Galt", CardType.PERSON);
	//Weapon cards
	public static Card revolverCard = new Card("Revolver", CardType.WEAPON);
	public static Card wrenchCard = new Card("Wrench", CardType.WEAPON);
	public static Card ropeCard = new Card("Rope", CardType.WEAPON);
	public static Card knifeCard = new Card("Knife", CardType.WEAPON);
	public static Card candleCard = new Card("Candle Stick", CardType.WEAPON);
	public static Card leadCard = new Card("Lead Pipe", CardType.WEAPON);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		board = Board.getInstance();
		board.setConfigFiles("ClueMap.csv", "ClueMapLegend.txt");		
		board.initialize();


	}

	/*
	 * Test to see how computer player will pick a target when no doors are present 
	 */
	@Test
	public void testTargetRandom() {
		ComputerPlayer player = new ComputerPlayer();
		//Test location with no rooms
		board.calcTargets(20,12, 1);
		Set<BoardCell> calcTargets = board.getTargets();
		//Three locations that should be picked
		//Row 19 Col 12
		boolean location1 = false;
		//Row 20 Col 13
		boolean location2 = false;

		//Test a lot of times
		for(int i = 0; i < 100; i++) {
			//Computer player picks a target
			BoardCell selectedTarget = player.pickLocation(board.getTargets());
			if(selectedTarget.equals(board.getCellAt(19,12))) {
				location1 = true;
			}
			else if(selectedTarget.equals(board.getCellAt(20, 13))) {
				location2 = true;
			}
			else {
				fail("Target not valid");
			}
		}
		assertTrue(location1);
		assertTrue(location2);
	}

	/*
	 * Test to see how computer player will pick a target door is present
	 */
	@Test
	public void testTargetRoom() {
		ComputerPlayer player = new ComputerPlayer();
		//Test location next to room
		board.calcTargets(1, 15, 2);
		Set<BoardCell> calcTargets = board.getTargets();
		//Desired location that doesn't change
		BoardCell desiredLocation = board.getCellAt(0, 16);
		for(int i = 0; i < 100; i++) {
			//Computer player selects target each loop
			BoardCell selectedTarget = player.pickLocation(board.getTargets());
			assertEquals(selectedTarget,desiredLocation);
			//Reset last visited
			player.setLastVisited(' ');
		}
	}

	/*
	 * Test to see how computer player will pick a target door is present but is also last visited
	 */
	@Test
	public void testTargetRoomLastVisited() {
		ComputerPlayer player = new ComputerPlayer();
		//Test location that has access to a room
		board.calcTargets(16, 18, 2);
		//Locations that can be picked
		//Row 16 Col 20
		boolean location1 = false;
		//Row 15 Col 17
		boolean location2 = false;
		//Row 16 Col 16
		boolean location3 = false;

		for(int i = 0; i < 100; i++) {
			//Computer player selects target each loop
			BoardCell selectedTarget = player.pickLocation(board.getTargets());

			if(selectedTarget.equals(board.getCellAt(16, 20))) {
				location1 = true;
			}
			else if(selectedTarget.equals(board.getCellAt(15, 17))) {
				location2 = true;
			}
			else if(selectedTarget.equals(board.getCellAt(16, 16))) {
				location3 = true;
			}
			else {
				fail("Target not valid");
			}
		}
		assertTrue(location1);
		assertTrue(location2);
		assertTrue(location3);
	}

	/*
	 * Test to see if computer player can make an accusation correctly
	 */
	@Test
	public void testMakeAccustation() {
		//Set sample solution
		Solution solution = new Solution("Garrus Vakarian", "Gallery", "Wrench");
		board.setAnswer(solution);
		//Tests correct accusation
		assertTrue(board.checkAccusation(solution));
		//Tests false accusation with wrong person
		assertFalse(board.checkAccusation(new Solution("Jim Holden","Gallery","Wrench")));
		//Tests false accusation with wrong room
		assertFalse(board.checkAccusation(new Solution("Garrus Vakarian","Stables","Wrench")));
		//Tests false accusation with wrong weapon
		assertFalse(board.checkAccusation(new Solution("Garrus Vakarian","Gallery","Revolver")));
	}

	/*
	 * Test to see how computer player will create a suggestion and if in the expected manner
	 */
	@Test
	public void testCreateSuggestion() {
		ComputerPlayer player = new ComputerPlayer("Jim Holden", 0, 0, Color.black);

		//Add all person cards to seen except the anticipated suggested
		player.updateSeenCards(garrusCard);
		player.updateSeenCards(jimCard);
		player.updateSeenCards(amosCard);
		player.updateSeenCards(malCard);
		player.updateSeenCards(johnCard);

		//Add all weapon cards to seen except the anticipated suggested
		player.updateSeenCards(wrenchCard);
		player.updateSeenCards(ropeCard);
		player.updateSeenCards(knifeCard);
		player.updateSeenCards(leadCard);
		player.updateSeenCards(candleCard);

		//Suggestion created in archery room
		player.createSuggestion("Archery Room");

		//Test to see if one player that's not seen is selected
		assertEquals("Tali Zorah", player.getSuggestedPerson());
		//Room matched current location
		assertEquals("Archery Room", player.getSuggestedRoom());
		//Test to see if one weapon that's not seen is selected
		assertEquals("Revolver", player.getSuggestedWeapon());

	}

	/*
	 * Test to see how computer player will create a suggestion when it has multiple unseen cards and if in the expected manner
	 */
	@Test
	public void testCreateSuggestionMultiple() {
		ComputerPlayer player = new ComputerPlayer("Jim Holden", 0, 0, Color.black);


		//Add all person cards to seen except 2 anticipated suggestions
		player.updateSeenCards(garrusCard);
		player.updateSeenCards(jimCard);
		player.updateSeenCards(malCard);
		player.updateSeenCards(johnCard);

		//Add all weapon cards to seen except 2 anticipated suggestions
		player.updateSeenCards(wrenchCard);
		player.updateSeenCards(ropeCard);
		player.updateSeenCards(leadCard);
		player.updateSeenCards(candleCard);

		boolean guessAmos = false;
		boolean guessTali = false;
		boolean guessKnife = false;
		boolean guessRevolver = false;

		//Run a lot of times to allow for random picks
		for(int i = 0; i < 100; i++) {
			player.createSuggestion("Ballroom");
			if(player.getSuggestedPerson().contentEquals("Amos Lee")) {
				guessAmos = true;
			}
			else if(player.getSuggestedPerson().contentEquals("Tali Zorah")) {
				guessTali = true;
			}
			if(player.getSuggestedWeapon().equals("Knife")) {
				guessKnife = true;
			}
			else if(player.getSuggestedWeapon().equals("Revolver")) {
				guessRevolver = true;
			}
			//Make sure that guessed room is current room
			assertEquals("Ballroom",player.getSuggestedRoom());
		}

		//Make sure that each was picked at least once
		assertTrue(guessAmos);
		assertTrue(guessTali);
		assertTrue(guessKnife);
		assertTrue(guessRevolver);

	}

	/*
	 * Test to see how computer player will disprove with one card match
	 */
	@Test
	public void testDisproveSuggestionOneMatch() {
		ComputerPlayer player = new ComputerPlayer("Amos Lee", 1, 1, Color.red);

		Solution suggestion = new Solution("Jim Holden", "Ballroom", "Wrench");

		//Test no match
		assertEquals(null, player.disproveSuggestion(suggestion));

		//Add cards to hand
		player.hand.add(revolverCard);
		player.hand.add(amosCard);
		player.hand.add(kitchenCard);
		player.hand.add(knifeCard);
		player.hand.add(archeryRoomCard);
		player.hand.add(jimCard);

		//Test one match
		assertEquals(jimCard,player.disproveSuggestion(suggestion));
	}

	/*
	 * Test to see how computer player will disprove with multiple card match
	 */
	@Test
	public void testDisproveSuggestionsMultMatch() {
		ComputerPlayer player = new ComputerPlayer("Amos Lee", 1, 1, Color.red);

		Solution suggestion = new Solution("Jim Holden", "Ballroom", "Wrench");

		//Add cards to hand
		player.hand.add(revolverCard);
		player.hand.add(amosCard);
		player.hand.add(kitchenCard);
		player.hand.add(knifeCard);
		player.hand.add(wrenchCard);
		player.hand.add(ballroomCard);
		player.hand.add(jimCard);

		//Cards that should be picked to disprove
		boolean returnJim = false;
		boolean returnBallroom = false;
		boolean returnWrench = false;

		//Run a lot of times to allow for each to be picked
		for(int i = 0; i < 100; i++) {
			Card returnFromDisprove = player.disproveSuggestion(suggestion);

			if(returnFromDisprove.getCardName().equals("Jim Holden")) {
				returnJim = true;
			}
			else if(returnFromDisprove.getCardName().equals("Ballroom")) {
				returnBallroom = true;
			}
			else if(returnFromDisprove.getCardName().equals("Wrench")) {
				returnWrench = true;
			}
		}

		assertTrue(returnJim);
		assertTrue(returnBallroom);
		assertTrue(returnWrench);
	}

	/*
	 * Test to make sure suggestions are handled correctly by the game board
	 */
	@Test
	public void testHandleSuggestion() {
		ArrayList<Player> players = new ArrayList();

		HumanPlayer humanPlayer = new HumanPlayer("John Galt", 0, 0, Color.blue);
		ComputerPlayer amos = new ComputerPlayer("Amos Lee", 1, 1, Color.black);
		ComputerPlayer garrus = new ComputerPlayer("Garrus Vakarian", 2, 2, Color.red);

		players.add(humanPlayer);
		players.add(amos);
		players.add(garrus);

		humanPlayer.addCard(wrenchCard);
		humanPlayer.addCard(archeryRoomCard);
		humanPlayer.addCard(amosCard);
		humanPlayer.addCard(candleCard);

		amos.addCard(cloakRoomCard);
		amos.addCard(jimCard);
		amos.addCard(leadCard);
		amos.addCard(stablesCard);

		garrus.addCard(revolverCard);
		garrus.addCard(kitchenCard);
		garrus.addCard(malCard);
		garrus.addCard(ropeCard);

		board.updatePlayers(players);

		Solution suggestion = new Solution("Tali Zorah", "Ballroom","Knife");
		//Return null if nobody can disprove
		assertNull(board.handleSuggestion(suggestion,garrus));
		suggestion.weapon = "Rope";
		//Return null if only accusing player can disprove
		assertNull(board.handleSuggestion(suggestion, garrus));
		suggestion.weapon = "Wrench";
		//Return null if human player can disprove but is also the accuser
		assertNull(board.handleSuggestion(suggestion, humanPlayer));
		//Suggestion only human can disprove
		assertEquals(wrenchCard,board.handleSuggestion(suggestion, amos));

		//Both amos and garrus have rope card
		amos.addCard(ropeCard);
		//Suggestion now includes rope
		suggestion.weapon = "Rope";

		//Ensure correct player disproves
		assertEquals(ropeCard,board.handleSuggestion(suggestion, humanPlayer));
		assertEquals("Amos Lee", board.getPlayerDisprove());

	}





















}
