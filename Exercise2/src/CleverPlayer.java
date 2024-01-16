/**
 * Represents a clever player in a Tic-Tac-Toe game.
 * This player strategy is to scan the board row by row,from top to bottom and left to right,
 * and marks the first empty cell it finds with its mark (X or O).
 * This is quite a clever strategy, since if the other player don't have a strategy to block
 * in the direction of a row, the clever player will keep marking cells in a row until it
 * wins (winning streak is smaller than board size).
 * // TODO: check if I'm suppose to expose the strategy in the documentation, or if I should say it counts that win streak is smaller than board size
 *
 * @author Shir Rashkovits
 */
public class CleverPlayer implements Player {

    /**
     * Plays a turn for the CleverPlayer.
     * Scans the game board sequentially starting from the top left corner and moving row by row.
     * The player marks the first empty cell it finds with its symbol.
     * If the board is full, the method will not place a mark and will return.
     *
     * @param board The game board on which the player will play their turn.
     * @param mark  The symbol (X or O) that represents the player on the board.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                // Check if the current cell is empty
                if (board.getMark(row, col) == Mark.BLANK) {
                    // Place the player's mark in the first empty cell found and exit the method
                    board.putMark(mark, row, col);
                    return;
                }
            }
        }
    }
}

