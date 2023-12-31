/**
 * Enum to represent different marks which can appear in a board cell.
 */
enum Mark { BLANK, X, O }

/**
 * Enum to represent the different statuses the board can be in.
 */
enum GameStatus { DRAW, X_WIN, O_WIN, IN_PROGRESS }

/**
 * Represents the game board for a Tic-Tac-Toe-like game.
 */
public class Board {
    public static final int SIZE = 3; // The board size (length and width)
    public static final int WIN_STREAK = 3; // The length of a winning sequence. Always smaller than SIZE
    private Mark[][] board;
    private GameStatus gameStatus;
    private int numberOfPlayedCells; // to keep track if a tie has been reached (when the board is full)
    private static final         // Define directions for checking
    int[][] directions = {
            {0, 1},   // Right
            {0, -1},  // Left
            {1, 0},   // Down
            {-1, 0},  // Up
            {1, 1},   // Down-Right
            {-1, -1}, // Up-Left
            {1, -1},  // Down-Left
            {-1, 1}   // Up-Right
    };
    private static final int ROW_IDX = 0;
    private static final int COL_IDX = 1;


    /**
     * Initializes the game board with all cells marked as BLANK.
     */
    public Board() {
        // When the game starts, all the cells are filled with a BLANK mark
        board = new Mark[SIZE][SIZE];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = Mark.BLANK;
            }
        }

        gameStatus = GameStatus.IN_PROGRESS;
    }

    /**
     * Places the specified mark in the given cell if it's a valid move.
     *
     * @param mark The mark to place (X or O).
     * @param row  The row of the cell.
     * @param col  The column of the cell.
     * @return true if the move is valid and the mark is placed, false otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (isValidBoardCell(row, col) && isEmptyCell(row, col)) {
            board[row][col] = mark;
            numberOfPlayedCells++;
            updateGameStatus(row, col);
            return true;
        }
        return false;
    }

    /**
     * Gets the current game status (IN_PROGRESS, DRAW, X_WIN, or O_WIN).
     *
     * @return The current game status.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Gets the mark in the specified cell.
     *
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @return The mark in the cell (X, O, or BLANK).
     */
    public Mark getMark(int row, int col) {
        if (isValidBoardCell(row, col)) {
            return board[row][col];
        }
        return Mark.BLANK;
    }

    private int countMarkInDirection(int row, int col, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while(isValidBoardCell(row, col) && board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    private void updateGameStatus(int row, int col) {
        Mark markToUpdate = board[row][col];

        // Check each direction
        for (int[] dir : directions) {
            int countForward = countMarkInDirection(row, col, dir[ROW_IDX], dir[COL_IDX], markToUpdate);
            int countBackward = countMarkInDirection(row, col, -dir[ROW_IDX], -dir[COL_IDX  ], markToUpdate);

            if (thereIsWinningStreak(markToUpdate, countForward, countBackward)) {
                return;
            }
        }

        // Check the case where all board cells were played without any winning streak
        if (numberOfPlayedCells == SIZE * SIZE) {
            gameStatus = GameStatus.DRAW;
        }
    }

    private boolean thereIsWinningStreak(Mark mark, int countLeft, int countRight) {
        if (countLeft + countRight - 1 >= WIN_STREAK){ //todo: 1 count as magic number?
            if (mark == Mark.X){
                gameStatus = GameStatus.X_WIN;
            }
            else{
                gameStatus = GameStatus.O_WIN;
            }
            return true;
        }
        return false;
    }

    private boolean isValidBoardCell(int row, int col) {
        return 0 <= row && 0 <= col && row < SIZE && col < SIZE;
    }

    // assumes validity of the cell
    private boolean isEmptyCell(int row, int col){
        return board[row][col] == Mark.BLANK;
    }
}
