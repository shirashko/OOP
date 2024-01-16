import java.util.Random;

/**
 * Represents a type of player in the Tic-Tac-Toe game that makes random moves.
 * The WhateverPlayer implements the Player interface and selects a random, unoccupied cell on the board
 * to place its mark each turn.
 *
 * @author Shir Rashkovits
 * @see Player
 * @see Board
 * @see Mark
 */
public class WhateverPlayer implements Player {
    // A single Random instance for all instances of WhateverPlayer
    private static final Random random = new Random();

    /**
     * Executes a turn for the WhateverPlayer.
     * During its turn, the player randomly selects an unoccupied cell on the board and places its mark there.
     * The player will continue to randomly select cells until it finds an unoccupied cell.
     * If the board is full, the player will keep selecting cells and the function will never return, so it is important
     * to check that the board is not full before calling this function.
     * TODO: make sure it is ok to state that. if not, understand the desired behavior and fix implementation and documentation
     *
     * @param board The game board on which the player will play their turn.
     * @param mark  The mark (X or O) that the player is playing.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        boolean placed = false;

        while (!placed) {
            // Randomly guess a position within the bounds of the board
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            // Check if the guessed position is unoccupied and place the mark if it is
            if (board.getMark(row, col) == Mark.BLANK) {
                placed = board.putMark(mark, row, col);
            }
        }
    }
}
