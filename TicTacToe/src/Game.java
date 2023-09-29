/**
 * The Game class represents a game of Tic-Tac-Toe and manages the game's logic.
 * The game is being played by two players, each one in its own turn. The game end when by the board logic
 * it arrived to a certain stage. The game progress is being displayed to the user by a renderer object.
 */
public class Game {
    Renderer renderer; // Used for rendering the game board.
    Player playerX;    // Represents Player X.
    Player playerO;    // Represents Player O.

    /**
     * Constructs a new Game instance with the given players and renderer.
     *
     * @param playerX   The Player object representing Player X.
     * @param playerO   The Player object representing Player O.
     * @param renderer  The Renderer used to display the game board.
     */
    public Game(Player playerX, Player playerO, Renderer renderer){
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * Runs the game until it's completed and returns the final game status.
     *
     * @return The GameStatus indicating the outcome of the game (e.g., X_WIN, O_WIN, DRAW).
     */
    public GameStatus run() {
        Board board = new Board();
        renderer.renderBoard(board);

        // define lists of players and marks to enable easy way to manipulate the game flow
        Player[] players = {playerX, playerO};
        Mark[] marks = {Mark.X, Mark.O};
        int counter = 0;
        int idx;

        while (board.getGameStatus() == GameStatus.IN_PROGRESS) {
            renderer.renderBoard(board);
            idx = counter % 2; // todo: counts as magic number?
            players[idx].playTurn(marks[idx], board);
            counter++;
        }

        renderer.renderBoard(board);
        return board.getGameStatus();
    }
}
