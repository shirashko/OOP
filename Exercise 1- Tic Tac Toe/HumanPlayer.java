/**
 * Represents a human player in a Tic-Tac-Toe game. This class handles the interaction between the human player
 * and the game board, allowing the player to input their moves.
 *
 * @author Shir Rashkovits
 */
public class HumanPlayer implements Player {

    private static final int DECIMAL_BASE = 10;

    /**
     * Constructs a HumanPlayer instance.
     */
    public HumanPlayer() {
    }

    /**
     * Executes the player's turn by reading input from the human player and placing a mark on the board.
     * The input is expected to be a two-digit integer where the first digit is the row and the second digit is
     * the column.
     * Continues to request input until a valid move is made.
     * If invalid input is provided, the player will get an indicative message and will continue to request input
     * until a valid move is made.
     * Valid coordinates: [0, 𝐵𝑜𝑎𝑟𝑑. 𝑆𝑖𝑧𝑒 − 1]
     *
     * @param board The game board on which the player will play their turn.
     * @param mark  The mark (X or O) that the player is playing.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        // init turn
        askUserForCellToMark(mark);
        boolean isValidTurn = false;

        // get input from user until it's valid
        while (!isValidTurn) {
            // get the desired cell from the player
            int rowCol = KeyboardInput.readInt();
            int row = rowCol / DECIMAL_BASE;
            int col = rowCol % DECIMAL_BASE;

            // check the validity of the cell and report the problem if needed
            if (board.getMark(row, col) != Mark.BLANK) {
                reportAlreadyTakenCell();
                continue; // TODO: I don't like it... to check this
            }

            // TODO: check this is the logic I need to follow - no check boundaries in this class, only rely on Board class checks
            isValidTurn = board.putMark(mark, row, col); // TODO: make sure in which case it's suppose to return false
            if (!isValidTurn) {
                reportOutOfBoardBoundariesCell();
            }
        }
    }

    /*
     * Ask the user to enter the coordinates of their next move on the board.
     *
     * @param mark The player's mark (X or O) for whom the input is being requested.
     */
    private void askUserForCellToMark(Mark mark) {
        System.out.println(Constants.playerRequestInputString(mark.toString()));
    }

    /*
     * Reports to the player that the chosen cell is already occupied.
     */
    private static void reportAlreadyTakenCell() {
        System.out.println(Constants.OCCUPIED_COORDINATE);
    }

    /*
     * Reports to the player that the chosen cell is out of the board's boundaries.
     */
    private static void reportOutOfBoardBoundariesCell() {
        System.out.println(Constants.INVALID_COORDINATE);
    }
}