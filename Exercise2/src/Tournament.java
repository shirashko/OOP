/**
 * Represents a Tic-Tac-Toe tournament between two players.
 * This class manages the entire tournament process, including initializing players,
 * rendering the board, and conducting multiple rounds of Tic-Tac-Toe.
 *
 * @author Shir Rashkovits
 * @see Player
 * @see Renderer
 */
public class Tournament {
    private static final int NUMBER_OF_POSSIBLE_GAME_OUTCOMES = 3;
    private static final int DRAW_IDX = 2;
    int rounds;
    Renderer renderer;
    Player player1;
    Player player2;

    /**
     * The main method to execute the Tic-Tac-Toe tournament.
     *
     * @param args Program arguments as follows (assumes exactly 6 arguments):
     *             <ol>
     *               <li>number of tournament rounds (assumes whole positive number) </li>
     *               <li>n, size of board. (assumes 2<=n<=9)</li>
     *               <li>k, win streak, number of consecutive marks in row/col/diagonal to win. (assumes 2<=k<=9)</li>
     *               <li> render the board (none/console)</li>
     *               <li>first player type: human/whatever/clever/genius</li>
     *               <li>second player type: human/whatever/clever/genius</li>
     *             </ol>
     */
    public static void main(String[] args) {
        // reading the arguments from the command line and setting up the necessary data for the tournament
        int rounds = Integer.parseInt(args[0]);
        int boardSize = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);

        // Shouldn't be sensitive to case
        String rendererType = args[3].toLowerCase();
        String player1Type = args[4].toLowerCase();
        String player2Type = args[5].toLowerCase();

        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(rendererType, boardSize);
        if (renderer == null) { // Check for invalid renderer type
            System.out.println(Constants.UNKNOWN_RENDERER_NAME);
            return;
        }

        PlayerFactory playerFactory = new PlayerFactory();
        Player player1 = playerFactory.buildPlayer(player1Type);
        Player player2 = playerFactory.buildPlayer(player2Type);

        if (player1 == null || player2 == null) { // Check for invalid player type
            System.out.println(Constants.UNKNOWN_PLAYER_NAME);
            return;
        }

        // Initialize and play the tournament.
        Tournament tournament = new Tournament(rounds, renderer, player1, player2);
        tournament.playTournament(boardSize, winStreak, args[4], args[5]);
    }

    /**
     * Constructor for the Tournament class.
     * Initializes the tournament with the given parameters.
     *
     * @param rounds    The number of rounds to be played in the tournament.
     * @param renderer  The renderer used to display the game board.
     * @param player1   The first player in the tournament.
     * @param player2   The second player in the tournament.
     */
    public Tournament(int rounds, Renderer renderer, Player player1, Player player2) {
       this.rounds = rounds;
       this.renderer = renderer;
       this.player1 = player1;
       this.player2 = player2;
    }

    /**
     * Conducts the Tic-Tac-Toe tournament with the specified configurations.
     *
     * @param size          The size of the Tic-Tac-Toe board.
     * @param winStreak     The number of consecutive marks required in row/col/diagonal to win.
     * @param playerName1   The name/type of the first player.
     * @param playerName2   The name/type of the second player.
     */
    public void playTournament(int size, int winStreak, String playerName1, String playerName2) {
        Game game;
        Player[] players = {player1, player2};
        int[] winningStatus = new int[NUMBER_OF_POSSIBLE_GAME_OUTCOMES]; // 0 - player1 wins, 1 - player2 wins, 2 - ties
        // At each game the players switch marks. At the first game, player1 is X and player2 is O.
        // For implementing this logic, in even rounds the first player is X, in odd rounds the first player is O
        for (int round = 0; round < rounds; round++) {

            // initialize a new game with the appropriate players for each mark
            game = new Game(players[round%2], players[(round+1)%2],size, winStreak, renderer);

            // running a game and getting the mark of the winner
            Mark winner = game.run();

            updateHowGameEnded(winner, winningStatus, round);

            displayTournamentProgress(winningStatus[0], winningStatus[1], winningStatus[2], playerName1, playerName2);
        }
    }

    // The players mark depend on the round number. In even rounds the first player is X,
    // in odd rounds the first player is O
    private void updateHowGameEnded(Mark winner, int[] winningStatus, int round) {
        if (winner == Mark.X) {
            winningStatus[round%2]++;
        } else if (winner == Mark.O) {
            winningStatus[(round+1)%2]++;
        } else {
            winningStatus[DRAW_IDX]++;
        }
    }

    // TODO: check if it counts as string literals - if I should put it in a formatted constant
    private void displayTournamentProgress(int numberOfPlayer1Winning, int numberOfPlayer2Winning, int ties, String playerName1, String playerName2) {
        System.out.println("######### Results #########\n" +
                "Player 1, " + playerName1 + " won: " + numberOfPlayer1Winning + " rounds\n" +
                "Player 2, " + playerName2 + " won: " + numberOfPlayer2Winning + " rounds\n" +
                "Ties: " + ties);
    }

}