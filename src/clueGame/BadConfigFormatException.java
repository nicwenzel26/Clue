/*
 * Brennan Guthals & Nicholas Wenzel
 * 3/6/2019
 * ClueGame
 */
package clueGame;

public class BadConfigFormatException extends Exception {

	public BadConfigFormatException() {
		super("Bad configuration of format.");
		
	}

	public BadConfigFormatException(String error) {
		super(error);
		
		
	}



}
