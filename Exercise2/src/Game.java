/**
 * Represents a game of Tic-Tac-Toe between two players.
 * This class manages the game board, tracks the players, determines the winning streak, and handles the game's progress.
 *
 * @author Shir Rashkovits
 * @see Board
 * @see Player
 * @see Renderer
 */
public class Game {
    private static final int DEFAULT_WIN_STREAK = 3;
    private Player playerX;
    private Player playerO;
    private int winsStreak;
    private Renderer renderer;
    private Board board;

    /**
     * Constructs a Tic-Tac-Toe game with default board (default size) and default win streak.
     *
     * @param playerX  The player who will play as 'X' (always start the game).
     * @param playerO  The player who will play as 'O'.
     * @param renderer The renderer to display the game board.
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        board = new Board();
        this.winsStreak = DEFAULT_WIN_STREAK;
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * Constructs a Tic-Tac-Toe game with the specified board size and win streak.
     * If the provided winStreak is invalid, for instance larger than the board size, then the win streak will be set to
     * the provided board size.
     * Constructor assumes size and winStreak are in positive integers in the range [2,9]. TODO: check if we can trust the board size or need to check for validity
     *
     * @param playerX   The player who will play as 'X'.
     * @param playerO   The player who will play as 'O'.
     * @param size      The size of the game board.
     * @param winStreak The number of consecutive marks needed to win (row/col/diagonal).
     * @param renderer  The renderer to display the game board.
     */
    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        board = new Board(size);
        this.winsStreak = winStreak;
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * Gets the number of consecutive marks needed to win the game.
     *
     * @return The win streak number.
     */
    public int getWinStreak() {
        return winsStreak;
    }

    /**
     * Gets the size of the game board (board is squared NXN, return N).
     *
     * @return The size of the board.
     */
    public int getBoardSize() {
        return board.getSize();
    }

    /**
     * Runs the game, handling the game's progression and interaction between players and the board.
     * This method handle turns, check for game-ending conditions, and render the board after each move.
     * Player with mark X starts first.
     *
     * @return The mark of the winning player, or null if the game ends in a draw.
     */
    public Mark run() {
        // Game always starts with player X
        Mark currentPlayerMark = Mark.X;
        Player currentPlayer = playerX;

        // The Game ends when one of the players wins, or when all cells are marked.
        // For that we need to keep track of the number of cells that have been marked on the board and compare it to
        // the number of cells in the board.
        int numberOfoccupiedCells = 0;
        int numberOfCellsInBoard = getBoardSize() * getBoardSize(); // board is squared

        while (true) {
            currentPlayer.playTurn(board, currentPlayerMark);
            numberOfoccupiedCells++;
            renderer.renderBoard(board);

            // Check for a win
            if (isWinner(currentPlayerMark)) {
                return currentPlayerMark;
            }

            // Check for a draw
            if (isBoardGameFull(numberOfoccupiedCells, numberOfCellsInBoard)) {
                return Mark.BLANK; // Indicate game finished with a draw
            }

            // Switch players
            if (currentPlayerMark == Mark.X) {
                currentPlayerMark = Mark.O;
                currentPlayer = playerO;
            } else {
                currentPlayerMark = Mark.X;
                currentPlayer = playerX;
            }
        }
    }

    /*
     * Checks if all cells on the board have been filled.
     * This method is used to determine if the game has ended in a draw.
     *
     * @param numberOccupiedCells The number of cells that have been marked on the board in the game.
     * @param numberOfCellsInBoard  The total number of cells in the game board.
     * @return true if all cells on the board are filled, false otherwise.
     */
    private static boolean isBoardGameFull(int numberOccupiedCells, int numberOfCellsInBoard) {
        return numberOccupiedCells == numberOfCellsInBoard;
    }

    // TODO: I think I have a problem with identifying a winning streak. To go over it


    /*
     * Determines if the specified player has won the game.
     * This method checks for a winning streak of the player's mark across the game board.
     * A player is considered to have won if their mark appears consecutively in the required number
     * of cells (defined by the win streak) in a row, column, or diagonal.
     *
     * @param mark The mark (X or O) of the player to check for a winning condition.
     * @return true if the player has won, false otherwise.
     */
    private boolean isWinner(Mark mark) {
        // Check all rows and columns
        for (int i = 0; i < board.getSize(); i++) {
            if (hasConsecutiveMarks(i, 0, 0, 1, mark)) return true; // Check each row
            if (hasConsecutiveMarks(0, i, 1, 0, mark)) return true; // Check each column
        }

        // Check diagonals
        for (int i = 0; i <= board.getSize() - winsStreak; i++) {
            for (int j = 0; j <= board.getSize() - winsStreak; j++) {
                if (hasConsecutiveMarks(i, j, 1, 1, mark)) return true; // Check diagonal down-right
                if (j >= winsStreak - 1 && hasConsecutiveMarks(i, j, 1, -1, mark)) return true; // Check diagonal down-left
            }
        }

        return false; // No winning streak found
    }


    /*
     * Checks for consecutive marks starting from a specific cell in a specified direction.
     * This method is a helper for the isWinner method to check for a winning streak in rows, columns, and diagonals.
     *
     * @param startRow  The starting row index for checking.
     * @param startCol  The starting column index for checking.
     * @param deltaRow  The row increment (1 for down, -1 for up, 0 for straight).
     * @param deltaCol  The column increment (1 for right, -1 for left, 0 for straight).
     * @param mark      The mark (X or O) to check for a winning streak.
     * @return true if a winning streak of the specified mark is found, false otherwise.
     */
    private boolean hasConsecutiveMarks(int startRow, int startCol, int deltaRow, int deltaCol, Mark mark) {
        int consecutiveCount = 0;

        for (int i = 0; i < winsStreak; i++) {
            int currentRow = startRow + i * deltaRow;
            int currentCol = startCol + i * deltaCol;

            if (board.getMark(currentRow, currentCol) == mark) {
                consecutiveCount++;
                if (consecutiveCount == winsStreak) {
                    return true; // Winning condition met
                }
            } else {
                break; // A non-matching mark breaks the streak
            }
        }

        return false; // No winning streak found in this direction
    }
}
