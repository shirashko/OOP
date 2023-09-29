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
    public static final int SIZE = 5; // The board size
    public static final int WIN_STREAK = 3; // The length of a winning sequence. Always smaller than SIZE

    private int LEFT = -1;
    private int RIGHT = 1;
    private int UP = 1;
    private int DOWN = -1;
    private int STAY_IN_PLACE = 0;

    private Mark[][] board;
    private GameStatus gameStatus;
    private int numberOfPlayedCells; // to keep track if a tie has been reached (when the board is full)

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
        while(row < SIZE && row >= 0 && col < SIZE && col >= 0 && board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    private void updateGameStatus(int row, int col){
        // Check if last change in the board brought to a horizontal, diagonal or vertical WIN_STREAK
        // sequence of Mark.X or Mark.O mark and update the game status accordingly

        Mark lastMark = board[row][col];

        // Horizontally : left and right directions
        int countLeft = countMarkInDirection(row, col, STAY_IN_PLACE, LEFT, lastMark);
        int countRight = countMarkInDirection(row, col, STAY_IN_PLACE, RIGHT, lastMark);
        if (thereIsWinningStreak(lastMark, countLeft, countRight)){
            return;
        }

        // Vertically - up and down
        int countDown = countMarkInDirection(row, col, DOWN, STAY_IN_PLACE, lastMark);
        int countUp = countMarkInDirection(row, col, UP, STAY_IN_PLACE, lastMark);
        if (thereIsWinningStreak(lastMark, countDown, countUp)){
            return;
        }

        // Diagonally
        int countUpRight = countMarkInDirection(row, col, UP, RIGHT, lastMark);
        int countDownLeft = countMarkInDirection(row, col, DOWN, LEFT, lastMark);
        if (thereIsWinningStreak(lastMark, countUpRight, countDownLeft)){
            return;
        }

        int countUpLeft = countMarkInDirection(row, col, UP, LEFT, lastMark);
        int countDownRight = countMarkInDirection(row, col, DOWN, RIGHT, lastMark);
        if (thereIsWinningStreak(lastMark, countUpLeft, countDownRight)){
            return;
        }

        // Check the case where all board cells were played without any winning streak
        if (numberOfPlayedCells == SIZE*SIZE){
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
        return 0 <= row && 0 <= col && row < board.length && col < board[row].length;
    }


    // assumes validity of the cell
    private boolean isEmptyCell(int row, int col){
        return board[row][col] == Mark.BLANK;
    }
}
