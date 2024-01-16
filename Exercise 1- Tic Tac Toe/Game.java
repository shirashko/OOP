/**
 * Represents a game of Tic-Tac-Toe between two players.
 * This class manages the game board, tracks the players, and handles the game's progress.
 * A winning condition is defined by a consecutive streak of marks (X or O) in a row, column, or diagonal.
 * The game ends when one of the players wins, or when all cells of the board are filled.
 *
 */
public class Game {
    private static final int DEFAULT_WIN_STREAK = 3;
    private Player playerX;
    private Player playerO;
    private int winStreak;
    private Renderer renderer;
    private Board board;

    /**
     * Constructs a Tic-Tac-Toe game with a default board size and default win streak.
     *
     * @param playerX  The player who will play as 'X' (always start the game).
     * @param playerO  The player who will play as 'O'.
     * @param renderer The renderer to display the game board.
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        board = new Board();
        this.winStreak = DEFAULT_WIN_STREAK;
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * Constructs a Tic-Tac-Toe game with a squared board of the specified size and with the specified win streak.
     * If the provided winStreak is invalid, meaning it is larger than the board size, then the win streak will be
     * set to the provided board size.
     * Constructor assumes size and winStreak are positive integers.
     *
     * @param playerX   The player who will play as 'X'.
     * @param playerO   The player who will play as 'O'.
     * @param size      The size of the game board.
     * @param winStreak The number of consecutive marks needed to win (row/col/diagonal).
     * @param renderer  The renderer to display the game board.
     */
    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        if (winStreak > size) { // Check for invalid win streak (larger than board size)
            winStreak = size;
        }
        board = new Board(size);
        this.winStreak = winStreak;
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
        return winStreak;
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

        // Track the count of occupied cells in order to determine a draw.
        int numberOfOccupiedCells = 0;
        int numberOfCellsInBoard = getBoardSize() * getBoardSize(); // squared board

        while (true) {
            currentPlayer.playTurn(board, currentPlayerMark);
            numberOfOccupiedCells++;
            renderer.renderBoard(board);

            // Check for a win
            if (isWinner(currentPlayerMark)) {
                return currentPlayerMark;
            }

            // Check for a draw
            if (isGameBoardFull(numberOfOccupiedCells, numberOfCellsInBoard)) {
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
    private static boolean isGameBoardFull(int numberOccupiedCells, int numberOfCellsInBoard) {
        return numberOccupiedCells == numberOfCellsInBoard;
    }

    /*
     * Determines if the specified player has won the game.
     * A player wins if they have a consecutive streak of their mark (either X or O)
     * in any row, column, or diagonal on the board.
     *
     * @param mark The mark of the player to check for a winning condition.
     * @return true if the specified player has won, false otherwise.
     */
    private boolean isWinner(Mark mark) {
        // Check rows and columns
        for (int i = 0; i < getBoardSize(); i++) {
            if (checkRow(i, mark) || checkColumn(i, mark)) {
                return true;
            }
        }

        // Check diagonals
        return checkDiagonals(mark);
    }

    /*
     * Checks if there's a winning streak of the specified mark in the given row.
     * A winning streak is defined as having the specified number of consecutive marks.
     *
     * @param row  The row index to check for a winning streak.
     * @param mark The mark (X or O) to look for in the row.
     * @return true if a winning streak is found in the row, false otherwise.
     */
    private boolean checkRow(int row, Mark mark) {
        int count = 0;
        for (int col = 0; col < getBoardSize(); col++) {
            if (board.getMark(row, col) == mark) {
                count++;
                if (count == winStreak) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }

    /*
     * Checks if there's a winning streak of the specified mark in the given column.
     * A winning streak is defined as having the specified number of consecutive marks.
     *
     * @param col  The column index to check for a winning streak.
     * @param mark The mark (X or O) to look for in the column.
     * @return true if a winning streak is found in the column, false otherwise.
     */
    private boolean checkColumn(int col, Mark mark) {
        int count = 0;
        for (int row = 0; row < getBoardSize(); row++) {
            if (board.getMark(row, col) == mark) {
                count++;
                if (count == winStreak) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }


    /*
     * Checks all possible diagonals on the board for a winning streak of the specified mark.
     * A winning streak is defined as having the specified number of consecutive marks.
     The method considers all diagonals where a win streak can fit, both from top-left to bottom-right
     and from top-right to bottom-left.
     @param mark The mark (X or O) to look for in the diagonals.
     @return true if a winning streak is found in any diagonal, false otherwise.
     */
    private boolean checkDiagonals(Mark mark) {
        int size = getBoardSize();
        // Check all possible diagonals that can fit the win streak
        for (int i = 0; i <= size - winStreak; i++) {
            for (int j = 0; j <= size - winStreak; j++) {
                if (checkDiagonalFromTopLeft(i, j, mark) || checkDiagonalFromTopRight(i, j, mark)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Checks a diagonal running from top-left to bottom-right for a winning streak of the specified mark.
     * A winning streak is defined as having the specified number of consecutive marks.
     *
     * @param row  The starting row index of the diagonal.
     * @param col  The starting column index of the diagonal.
     * @param mark The mark (X or O) to look for in the diagonal.
     * @return true if a winning streak is found in the diagonal, false otherwise.
     */
    private boolean checkDiagonalFromTopLeft(int row, int col, Mark mark) {
        int count = 0;
        for (int i = 0; i < winStreak; i++) {
            if (board.getMark(row + i, col + i) == mark) {
                count++;
                if (count == winStreak) return true;
            } else {
                break;
            }
        }
        return false;
    }

    /*
     * Checks a diagonal running from top-right to bottom-left for a winning streak of the specified mark.
     * A winning streak is defined as having the specified number of consecutive marks.
     *
     * @param row  The starting row index of the diagonal.
     * @param col  The starting column index of the diagonal.
     * @param mark The mark (X or O) to look for in the diagonal.
     * @return true if a winning streak is found in the diagonal, false otherwise.
     */
    private boolean checkDiagonalFromTopRight(int row, int col, Mark mark) {
        int count = 0;
        for (int i = 0; i < winStreak; i++) {
            if (board.getMark(row + i, col + winStreak - 1 - i) == mark) {
                count++;
                if (count == winStreak) return true;
            } else {
                break;
            }
        }
        return false;
    }
}


