/**
 * Represents a genius player in a Tic-Tac-Toe game.
 * This player employs a strategy to play its turn by scanning the board column by column,
 * starting from the second column, and choosing the first empty cell to place its mark.
 * This is a genius strategy, since if the other player don't have a strategy to block in the direction of a column,
 * it increases its chances to win. Also, the fact is scans the board from the second column and not the first,
 * increases its chances to win since it is blocking in 3 directions (row, column, and diagonal).
 *
 * @author Shir Rashkovits
 */
public class GeniusPlayer implements Player {

    /**
     * Plays a turn for the GeniusPlayer.
     * Scans the game board column by column, starting from the second column,
     * and marks the first empty cell it finds with its symbol.
     * If no empty cell is found in columns 2 through the last, it checks the first column as a fallback.
     * If the board is full, the method will not place a mark and will return.
     *
     * @param board The game board on which the player will play their turn.
     * @param mark  The symbol (X or O) that represents the player on the board.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        // Start from the second column and iterate through all columns
        for (int col = 1; col < board.getSize(); col++) {
            // Iterate through each row in the current column
            for (int row = 0; row < board.getSize(); row++) {
                // Check if the current cell is empty
                if (board.getMark(row, col) == Mark.BLANK) {
                    // Place the player's mark in the first empty cell found and exit the method
                    board.putMark(mark, row, col);
                    return;
                }
            }
        }

        // If no empty cell is found in columns 2 to the last, check the first column as a fallback
        for (int row = 0; row < board.getSize(); row++) {
            if (board.getMark(row, 0) == Mark.BLANK) {
                board.putMark(mark, row, 0);
                return;
            }
        }
    }
}