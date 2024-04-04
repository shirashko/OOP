package bricker.main;

import bricker.brick_strategies.CollisionStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Manages the core game logic and setup for a brick-breaker game. This includes initializing game elements
 * such as the ball, paddle, bricks, borders, and background. It also handles game lifecycle events like
 * initialization, updates, and rendering, as well as managing the game state (e.g., checking win/loss
 * conditions).
 */
public class BrickerGameManager extends GameManager {

    // Game configuration
    private final static int NUMBER_OF_EXPECTED_ARGUMENTS = 2;
    private final static int DEFAULT_NUMBER_OF_BRICKS_PER_ROW = 8;
    private final static int DEFAULT_NUMBER_OF_BRICKS_ROWS = 7;

    // Window Game configuration
    private static final int GAME_WINDOW_WIDTH = 700;
    private static final int GAME_WINDOW_HEIGHT = 500;
    private final static Vector2 GAME_WINDOW_DIMENSIONS = Vector2.of(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
    private final static String GAME_TITLE = "Bricker Game";


    // Game status configuration
    private final static String LOOSING_PROMPT = "You lose! Play again?";
    private final static String WINNING_PROMPT = "You win! Play again?";
    private final static int WINNING_KEY = KeyEvent.VK_W;
    private static final int BRICKS_LEFT_TO_WIN_GAME = 0;


    // Game-specific properties
    private UserInputListener inputListener;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private final int numberOfBricksInRow;
    private final int numberOfBricksRows;
    private LifeManager lifeCountManager;
    private BricksManager bricksManager;
    private BallManager ballManager;
    private PaddleManager paddleManager;

    /**
     * The main method to run the game.
     * @param args The command line arguments for the number of bricks per row and the number of rows.
     *             If no arguments are provided, or if the number of provided arguments is not 2,
     *             the game will run with the default configuration.
     *             Assuming the first argument is the number of bricks per row and the second argument
     *             is the number of rows, and that both arguments are positive integers.
     */
    public static void main(String[] args) {
        // Create the game manager with the given or default configuration
        BrickerGameManager brickerGameManager;
        if (args.length == NUMBER_OF_EXPECTED_ARGUMENTS) {
            int numberOfBricksPerRow = Integer.parseInt(args[0]);
            int numberOfRows = Integer.parseInt(args[1]);
            brickerGameManager = new BrickerGameManager(GAME_TITLE, GAME_WINDOW_DIMENSIONS,
                    numberOfBricksPerRow, numberOfRows);
        } else {
            brickerGameManager = new BrickerGameManager(GAME_TITLE, GAME_WINDOW_DIMENSIONS);
        }

        // Start the game
        brickerGameManager.run();
    }

    /**
     * Creates a new window with the specified title and of the specified dimensions for a bricker game.
     * Uses a default number of bricks in a row and a default number of bricks rows.
     *
     * @param windowTitle can be null to indicate the usage of the default window title
     * @param windowDimensions dimensions in pixels. can be null to indicate a
     *                         full-screen window whose size in pixels is the main screen's resolution
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        this(windowTitle, windowDimensions, DEFAULT_NUMBER_OF_BRICKS_PER_ROW, DEFAULT_NUMBER_OF_BRICKS_ROWS);
    }

    /**
     * Creates a new window with the specified title and of the specified dimensions for a bricker game.
     *
     * @param windowTitle can be null to indicate the usage of the default window title
     * @param windowDimensions dimensions in pixels. can be null to indicate a
     *                         full-screen window whose size in pixels is the main screen's resolution
     * @param numberOfBricksInRow The number of bricks in a row. It assumed to be positive.
     * @param numberOfBricksRows The number of bricks rows. It assumed to be positive.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numberOfBricksInRow,
                              int numberOfBricksRows) {
        super(windowTitle, windowDimensions);
        this.numberOfBricksInRow = numberOfBricksInRow;
        this.numberOfBricksRows = numberOfBricksRows;
    }

    /**
     * The method will be called once when the game starts to run, and again after every invocation
     * Initializes the game by creating all necessary game objects and setting up the game environment.
     * This method is called once when the game starts.
     *
     * @param imageReader Tool to read images from disk.
     * @param soundReader Tool to read sounds from disk.
     * @param inputListener Tool to listen for user input.
     * @param windowController Tool to control the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // Save given resources in order to control the Bricker game
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.imageReader = imageReader;
        this.soundReader = soundReader;

        // Create object that will manage the life count aspects of the game
        this.lifeCountManager = initializeLifeManager();

        // Create game objects
        initializeBackground();
        initializeBorders(); // The game includes borders on all sides of the screen besides bottom one to
                             // prevent the ball from falling off the screen

        this.ballManager = initializeBallManager();
        ballManager.initializeMainBall();

        this.paddleManager = initializePaddleManager();
        paddleManager.initializeMainPaddle();

        this.bricksManager = initializeBricksManager();
    }

    /**
     * Returns the dimensions of the game window.
     * @return The dimensions of the game window.
     */
    public Vector2 getGameWindowDimensions() {
        return windowController.getWindowDimensions();
    }

    /**
     * Checks if the given game object is inside the window bounds.
     * @param gameObject The game object to check is inside the window bounds.
     * @return True if the game object is inside the window bounds, false otherwise.
     */
    public boolean isInsideWindowBounds(GameObject gameObject) {
        return !isObjectBelowWindow(gameObject);
    }

    /**
     * Removes the given game object from the game.
     * @param gameObject The game object to remove from the game.
     * @param layerId The layer ID of the game object to remove.
     * @return True if the game object was removed, false otherwise.
     */
    public boolean removeGameObject(GameObject gameObject, int layerId) {
        return gameObjects().removeGameObject(gameObject, layerId);
    }

    /**
     * Adds the given game object to the game.
     * @param gameObject The game object to add to the game.
     * @param layerId The layer ID of the game object to add.
     */
    public void addGameObject(GameObject gameObject, int layerId) {
        gameObjects().addGameObject(gameObject, layerId);
    }

    /**
     * Called once per frame. Any logic is put here. Rendering, on the other hand,
     * should only be done within 'render'.
     * This method activate the updates and handle collisions of the game objects.
     * Also, it handles game status, suggest the user to play again, etc.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isWinningSituation()) {
            handleRestartOrCloseOption(WINNING_PROMPT);
        }

        if (isObjectBelowWindow(ballManager.getMainBall())) {
            // The ball fell below the window, so the user lost a life
            lifeCountManager.decrementLife();
            ballManager.resetMainBall();

            if (isLoosingSituation()) {
                handleRestartOrCloseOption(LOOSING_PROMPT);
            }
        }
    }

    private LifeManager initializeLifeManager() {
        return new LifeManager(this, imageReader);
    }

    /*
     * Configures the background to align with camera movements, ensuring visual consistency.
     * This method makes the background move in sync with the camera, enhancing immersion by adjusting visuals
     * as the camera follows the game's main focus (for example, the ball).
     *
     * @param background The game's background object to be configured.
     */
    private void configureBackgroundForCamera(GameObject background) {
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    private PaddleManager initializePaddleManager() {
        return new PaddleManager(imageReader, inputListener, this);
    }

    /*
     * Creates the background of the game.
     *
     * @param imageReader The image reader to read the background's image.
     * @param windowDimensions The window's dimensions.
     */
    private void initializeBackground() {
        ImageRenderable backgroundImage =
                            imageReader.readImage(Constants.ResourcePath.BACKGROUND_IMAGE.getPath(), true);
        GameObject background = new GameObject(Vector2.ZERO, getGameWindowDimensions(), backgroundImage);
        addGameObject(background, Constants.GameLayer.BACKGROUND.getId());
        configureBackgroundForCamera(background);
    }


    /*
     * Creates the walls of the game.
     *
     * @param windowDimensions The window's dimensions.
     */
    private void initializeBorders() {
        Vector2 windowDimensions = getGameWindowDimensions();

        // Left wall
        createBorder(Vector2.ZERO, Vector2.of(Constants.BORDER_THICKNESS, windowDimensions.y()));

        // Right wall
        createBorder(Vector2.of(windowDimensions.x() - Constants.BORDER_THICKNESS,  0),
                Vector2.of(Constants.BORDER_THICKNESS, windowDimensions.y()));

        // Top wall
        createBorder(Vector2.ZERO, Vector2.of(windowDimensions.x(), Constants.BORDER_THICKNESS));
    }

    /*
     * Creates a border in the game window.
     *
     * @param position The position of the wall.
     * @param dimensions The dimensions of the wall.
     */
    private void createBorder(Vector2 position, Vector2 dimensions) {
        GameObject border = new GameObject(position, dimensions, null);
        border.setTag(Constants.GameObjectsTags.BORDER.getValue());
        addGameObject(border, Constants.GameLayer.BORDER.getId());
    }


    /*
     * Initializes the bricks of the game.
     */
    private BricksManager initializeBricksManager() {
        Counter bricksCounter = new Counter(); // Counter to track the number of bricks in the game
        CollisionStrategyFactory collisionStrategyFactory =
                initializeCollisionStrategyFactory(bricksCounter);

        bricksManager = new BricksManager(this, imageReader, collisionStrategyFactory,
                numberOfBricksInRow, numberOfBricksRows, bricksCounter);

        bricksManager.initializeBricks();

        return bricksManager;
    }


    /*
     * Initializes the collision strategy factory for the game.
     */
    private CollisionStrategyFactory initializeCollisionStrategyFactory(Counter bricksCounter) {
        return new CollisionStrategyFactory(
                this,
                ballManager,
                paddleManager,
                bricksCounter,
                lifeCountManager,
                Constants.GameLayer.FOCUS_CAMERA_CHANGER.getId());

    }

    /*
     * Initializes the ball manager for the game.
     */
    private BallManager initializeBallManager() {
        return new BallManager(imageReader, soundReader, this);
    }


    /*
     * Checks if the given game object is below the window.
     */
    private boolean isObjectBelowWindow(GameObject gameObject) {
        return gameObject.getCenter().y() > getGameWindowDimensions().y();
    }

    /*
    * Handles the user's choice to restart the game or close the window.
    */
    private void handleRestartOrCloseOption(String prompt) {
        if (windowController.openYesNoDialog(prompt)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /*
    * Checks if the game is in a winning situation.
    */
    private boolean isWinningSituation() {
        return noMoreBricksToBreak() || IsWinningKeyPressed();
    }

    /*
    * Checks if the user broke all the bricks in the game.
    */
    private boolean noMoreBricksToBreak() {
        return bricksManager.getBricksCounter().value() == BRICKS_LEFT_TO_WIN_GAME;
    }

    /*
     * Checks if the user pressed the winning key.
     */
    private boolean IsWinningKeyPressed() {
        return inputListener.isKeyPressed(WINNING_KEY);
    }

    private boolean isLoosingSituation() {
        return lifeCountManager.getLifeCount() == LifeManager.LOOSING_NUMBER_OF_LIVES;
    }
}
