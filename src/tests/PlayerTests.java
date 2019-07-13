/*
 * Brennan Guthals & Nicolas Wenzel
 * 3/21/2019
 * CSCI306 ClueGame
 */

package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.PlayerType;

public class PlayerTests {
	private static Board board;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueMap.csv", "ClueMapLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
		//Load player file
	}


	/*
	 * Test to see if player is loaded correctly
	 * In this test, see if Human player is loaded correctly
	 */
	
	
	@Test
	public void testPlayer1() {
		Player player = board.players.get(0);
		assertEquals("Mal Reynolds",player.getPlayerName());
		assertEquals(Color.red,player.getColor());
		assertEquals(3,player.getColumn());
		assertEquals(0,player.getRow());
		assertEquals(player.type,PlayerType.HUMAN);
	}

	/*
	 * Test to see if player is loaded correctly
	 * In this test, see if Computer player is loaded correctly
	 */
	
	
	@Test
	public void testPlayer3() {
		Player player = board.players.get(2);
		assertEquals("Garrus Vakarian",player.getPlayerName());
		assertEquals(Color.blue,player.getColor());
		assertEquals(21,player.getColumn());
		assertEquals(5,player.getRow());
		assertEquals(player.type,PlayerType.COMPUTER);
	}

	//Test to see if player is loaded correctly
	//In this test, see if Computer player is loaded correctly
	
	
	@Test
	
	
	public void testPlayer6() {
		Player player = board.players.get(5);
		assertEquals("John Galt",player.getPlayerName());
		assertEquals(Color.black,player.getColor());
		assertEquals(13,player.getColumn());
		assertEquals(20,player.getRow());
		assertEquals(player.type,PlayerType.COMPUTER);
	}

	/*
	 * Tests to see if deck was created properly, checks to see if deck size is correct, and that correct amount of weapons, people, and rooms are in deck.
	 */
	
	
	@Test
	public void testCreateCards() {
		assertEquals(board.deck.size(), 23);
		int countWeapon = 0;
		int countPerson = 0;
		int countRoom = 0;
		
		for(Card i: board.deck) {
			if(i.type == CardType.WEAPON) {
				countWeapon++;
			}
			else if(i.type == CardType.PERSON) {
				countPerson++;
			}
			else if(i.type == CardType.ROOM) {
				countRoom++;
			}
		}
		assertEquals(countWeapon, 6);
		assertEquals(countRoom, 11);
		assertEquals(countPerson, 6);
		
		boolean person = false;
		boolean wep = false;
		boolean place = false; 
		
		for(Card card: board.deck) {
			if(card.getCardName().contains("Garrus Vakarian")) {
				person = true; 
			}
			if (card.getCardName().contains("Revolver")) {
				wep = true; 
			}
			
			if (card.getCardName().contains("Ballroom")) {
				place = true; 
			}
		}
		
		
		assertTrue(person); 
		assertTrue(wep);
		assertTrue(place); 
	}

	/*
	 * Tests to see if each player got the right amount of card, and that after cards were dealt, the deck size is 0
	 */
	


	

}