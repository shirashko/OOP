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

        Player[] players = {playerX, playerO};
        Mark[] marks = {Mark.X, Mark.O};
        int counter = 0;
        int idx;

        while (board.getGameStatus() == GameStatus.IN_PROGRESS) {
            renderer.renderBoard(board);
            idx = counter % 2;
            players[idx].playTurn(marks[idx], board);
            counter++;
        }

        renderer.renderBoard(board);
        return board.getGameStatus();
    }

    /**
     * The main method that initializes players, renderer, and the game, and then runs the game.
     *
     * @param args The command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        Player playerX = new Player();
        Player playerO = new Player();
        Renderer renderer = new Renderer();

        Game game = new Game(playerX, playerO, renderer);
        GameStatus winner = game.run();

        if (winner == GameStatus.DRAW){
            System.out.println("The game ended with a tie");
        }
        else if (winner == GameStatus.X_WIN) {
            System.out.println("The winner is X");
        }
        else{
            System.out.println("The winner is O");
        }
    }
}
