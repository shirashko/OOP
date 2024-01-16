/**
 * Represents the game board for a Tic-Tac-Toe game.
 * This class manages the state and size of the Tic-Tac-Toe board, and allows for marks (X, O, BLANK)
 * to be placed on the board.
 *
 * @author Shir Rashkovits
 * @see Mark
 */
public class Board {
    private static final int DEFAULT_BOARD_SIZE = 4;
    private int size; // The size of the board (number of rows and so as columns (square board)
    private Mark[][] board;

    /**
     * Constructs a default Tic-Tac-Toe board with a predefined size.
     */
    public Board() {
        size = DEFAULT_BOARD_SIZE;
        initializeBoardWithBlankMarks();
    }

    /**
     * Constructs a Tic-Tac-Toe board with the specified size.
     *
     * @param size The size of the board (number of rows and columns).
     */
    public Board(int size) {
        this.size = size;
        initializeBoardWithBlankMarks();
    }

    /**
     * Retrieves the size of the board (board is squared NXN, return N)
     *
     * @return The size of the board.
     */
    public int getSize() {
        return size;
    }

    /**
     * Attempts to place a mark on the board at the specified row and column.
     * Fails if the cell is already occupied or the coordinates are out of the board bounds.
     *
     * @param mark The mark (X, O, or BLANK) to place on the board.
     * @param row  The row to place the mark in.
     * @param col  The column to place the mark in.
     * @return true if the mark was successfully placed, false otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) { // TODO: check in each case need to return false
        // Check if the row and col are valid (no out of bounds)
        if (AreCoordinatesOutOfBoardBounds(row, col)) {
            return false;
        }
        // Check if the requested cell is already occupied
        if (board[row][col] != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        return true;
    }

    /**
     * Retrieves the mark at the specified row and column.
     *
     * @param row The row to retrieve the mark from.
     * @param col The column to retrieve the mark from.
     * @return The mark at the specified location, or Mark.BLANK if got illegal coordinates (out of board bounds).
     */
    public Mark getMark(int row, int col) {
        // Check if the row and col are valid
        if (AreCoordinatesOutOfBoardBounds(row, col)) {
            return Mark.BLANK;
        }
        return board[row][col];
    }

    // row and col coordinates needs to be in the range of [0, size-1]
    private boolean AreCoordinatesOutOfBoardBounds(int row, int col) {
        return (row < 0 || row >= size || col < 0 || col >= size);
    }

    /*
    * Initializes the board according to the board size, and fills it with blank marks (Mark.BLANK).
    */
    private void initializeBoardWithBlankMarks() {
        board = new Mark[this.size][this.size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = Mark.BLANK;
            }
        }
    }
}
