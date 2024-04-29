// Driver program for computer vs human game
// Requires CompetitiveXOPuzzle.java in same directory as this file
import java.util.Scanner;

public class CompetitiveXOPuzzleDriver {

	public static void main(String[] args) {
		
		// Get board size from user
		System.out.print("Board size?: ");
		Scanner keyboard = new Scanner(System.in);
		int size = keyboard.nextInt();
		
		// Play game
		CompetitiveXOPuzzle game = new CompetitiveXOPuzzle(size);
		game.play();
	}

}
