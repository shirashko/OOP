/**
 * A renderer implementation for the Tic-Tac-Toe game that performs no actual rendering.
 * This class can be used in contexts where a visual representation of the game board is not required,
 * such as automated testing or when running the game in a non-interactive mode.
 *
 */
public class VoidRenderer implements Renderer {

    /**
     * Creates an instance of VoidRenderer.
     */
    public VoidRenderer() {
        // no initialization is required
    }

    /**
     * Implements the renderBoard method from the Renderer interface, but does not perform any rendering.
     * This method is intentionally left empty to fulfill the contract of the Renderer interface without any side effects.
     *
     * @param board The game board that would normally be rendered. In this implementation, it is not used.
     */
    @Override
    public void renderBoard(Board board) {
        // No rendering action is performed.
    }
}
