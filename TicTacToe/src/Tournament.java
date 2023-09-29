/**
 * The `Tournament` class organizes a series of Tic-Tac-Toe games (rounds) between specific players,
 * with a defined interface for rendering, while alternating player roles between games.
 * After each game, the updated result is displayed on the screen.
 * The updated result includes the number of wins for Player 1, the number of wins for Player 2,
 * and the number of games that ended in a draw.
 */
public class Tournament {
    // constants to represent arguments indices in main
    private static final int ARG_ROUNDS = 0;
    private static final int ARG_RENDERER = 1;
    private static final int ARG_PLAYER1 = 2;
    private static final int ARG_PLAYER2 = 3;

    // constants to represent the possible game outcome todo maybe transfer to private enum?
    private static final int PLAYER1_IDX = 0;
    private static final int PLAYER2_IDX = 1;
    private static final int DRAW_IDX = 2;
    private static final int NUMBER_OF_POSSIBLE_GAME_OUTCOMES = 3;
    private static final int NUMBER_OF_EXPECTED_ARGUMENTS = 4;

    private Player player1;
    private Player player2;
    Renderer renderer;
    private int rounds; // Number of rounds in the tournament


    /**
     * Constructs a `Tournament` instance with the specified parameters.
     *
     * @param rounds   The number of rounds in the tournament.
     * @param renderer The renderer for displaying game results.
     * @param player1  The first player participating in the tournament.
     * @param player2  The second player participating in the tournament.
     */
    public Tournament(int rounds, Renderer renderer, Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.renderer = renderer;
        this.rounds = rounds;
    }

    /**
     * Plays the tournament and returns the results.
     *
     * @return An array representing the results of the tournament:
     *         - Index 0: Number of wins for Player 1
     *         - Index 1: Number of wins for Player 2
     *         - Index 2: Number of games that ended in a draw
     */
    public int[] playTournament(){
        int[] winningStatus = new int[NUMBER_OF_POSSIBLE_GAME_OUTCOMES];
        Player[] players = {player1, player2};
        for (int i=0; i<rounds; i++){
            Game game = new Game(players[i%2], players[(i+1)%2], renderer); // each round the players switch marks
            GameStatus winner = game.run();
            updateHowGameEnded(winner, winningStatus, i); //todo: winning - need to print what I currently printing?
        }
        return winningStatus;
    }

    /*
     * Updates the tournament result based on the game's outcome. 
     * knows the turns logic 
     *
     * @param winner         The result of the game (DRAW, X_WIN, or O_WIN).
     * @param winningStatus  An array to update with the tournament results.
     * @param round          The current round number (minus 1).
     */
    private static void updateHowGameEnded(GameStatus winner, int[] winningStatus, int round) {
        switch (winner) {
            case DRAW:
                winningStatus[DRAW_IDX]++;
                break;
            case X_WIN:
                winningStatus[round % 2]++;
                break;
            case O_WIN:
                winningStatus[(round + 1) % 2]++;
                break;
        }
        System.out.printf("Updated winning status of game: Player 1 won %s times, player 2 won " +
                        "%s times, and %s games ended with a draw.\r", winningStatus[PLAYER1_IDX], winningStatus[PLAYER2_IDX],
                winningStatus[DRAW_IDX]);
    }

    /*
     * The main method that initializes players, renderer, and the game, and then runs the game.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {

        // creating a tournament according to the user request, the args array, checking validity of the
        // request in the process.
        if (args.length != NUMBER_OF_EXPECTED_ARGUMENTS) {
            System.out.println("Usage: java Tournament [number of rounds] [RenderType] [Player1Type] [Player2Type]");
            return;
        }

        PlayerFactory playerFactory = new PlayerFactory();
        Player player1 = playerFactory.buildPlayer(args[ARG_PLAYER1]);
        if (player1 == null)
        {
            System.out.println(args[ARG_PLAYER1] + " is not a valid player name.");
            return;
        }

        Player player2 = playerFactory.buildPlayer(args[ARG_PLAYER2]);
        if (player2 == null)
        {
            System.out.println(args[ARG_PLAYER2] + " is not a valid player name.");
            return;
        }

        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[ARG_RENDERER]);
        if (renderer == null)
        {
            System.out.println(args[ARG_RENDERER] + " is not a valid renderer name.");
            return;
        }

        int rounds = Integer.parseInt(args[ARG_ROUNDS]); // assumes the validity of the type of the input (int)
        if (isNotValidRoundsNumber(rounds)) return;

        Tournament t = new Tournament(rounds, renderer, player1, player2);
        int[] winningStatus = t.playTournament();
        System.out.printf("Results of the Tournament: Player 1 won %d times. Player 2 won %d " +
                        "times, and %d games ended with a draw.%n", winningStatus[PLAYER1_IDX],  winningStatus[PLAYER2_IDX],
                winningStatus[DRAW_IDX]);

    }

    private static boolean isNotValidRoundsNumber(int rounds) {
        if (rounds <= 0){ //todo: counts a magic number?
            System.out.println(rounds + " is not a valid number of tournament rounds. Please enter a positive number");
            return true;
        }
        return false;
    }

}
