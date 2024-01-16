/**
 * An interface representing a renderer in a Tic-Tac-Toe game.
 * Implementations of this interface are responsible for rendering the game board.
 *
 */
public interface Renderer {

    /**
     * Renders the specified Tic-Tac-Toe game board.
     * Implementations of this method should provide a visual representation of the board,
     * which could be in a console, a graphical user interface, or any other format.
     *
     * @param board The game board to render.
     */
    void renderBoard(Board board);
}
