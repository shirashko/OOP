/**
 * An interface representing a player in a Tic-Tac-Toe game.
 * Implementations of this interface are responsible for handling a player's turn in the game.
 * This includes deciding where to place a mark on the game board.
 *
 */
public interface Player {

    /**
     * Executes a turn for the player.
     * Implementations of this method should determine the location on the board where to place the player's mark
     * and perform the action of marking the board.
     *
     * @param board The game board on which the player will play their turn.
     * @param mark The mark (X or O) that the player is playing.
     */
    void playTurn(Board board, Mark mark);
}
