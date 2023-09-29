import java.util.Scanner;

/**
 * The Player class represents a player in a Tic-Tac-Toe game.
 */
public class Player {

    Scanner in; // Scanner object for input from the user.

    /**
     * Constructs a new Player instance and initializes the Scanner for input.
     */
    public Player(){
        in = new Scanner(System.in);
    }

    /**
     * Allows the player to make a move on the game board.
     *
     * @param mark  The Mark (X or O) that the player is placing on the board.
     * @param board The game board on which the move is made.
     */
    public void playTurn(Mark mark, Board board){
        System.out.println("Please enter the cell (row,column) where you want to place " + mark + ". Use the format: 02 for " +
                "row 0 and column 2");
        int rowColumn = in.nextInt(); // The function assumes user input is an integer.

        // Calculate the row and column indices based on user input.
        // Subtract one because board indices range from zero to board.SIZE - 1.
        int rowToPlay = (rowColumn / 10) - 1;
        int colToPlay = (rowColumn % 10) - 1;

        while (!board.putMark(mark, rowToPlay, colToPlay)){ // Continue until a valid coordinate is provided.
            System.out.println("This (row, col) cell is out of bounds or already occupied. Please enter again.");
            rowColumn = in.nextInt();
            rowToPlay = (rowColumn / 10) - 1;
            colToPlay = (rowColumn % 10) - 1;
        }
    }
}
