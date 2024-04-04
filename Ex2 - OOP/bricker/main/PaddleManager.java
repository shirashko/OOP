package bricker.main;

import bricker.gameobjects.Paddle;
import bricker.gameobjects.VanishPaddle;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Manages the creation and initialization of paddle objects in the game.
 * This includes the main paddle that the player controls and any special paddles
 * such as the VanishPaddle that has unique behaviors. The manager is also responsible
 * for positioning these paddles within the game window.
 */
public class PaddleManager {

    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final Vector2 PADDLE_DIMENSIONS = Vector2.of(PADDLE_WIDTH, PADDLE_HEIGHT);

    // Vertical offset from the bottom of the window for paddles
    private static final int DISTANCE_OF_PADDLE_FROM_BOTTOM = 13;

    // Combined dimensions for paddles
    private final Vector2 windowDimensions; // Dimensions of the game window, used for paddle positioning
    private final ImageRenderable paddleImage; // Visual representation of paddles
    private final UserInputListener inputListener; // Listener for user inputs, used for paddle movement

    // Game manager for adding/removing objects from the game
    private final BrickerGameManager brickerGameManager;

    // Tracks the number of special paddles in play
    private static final Counter extraPaddleCounter = new Counter();

    /**
     * Constructs a PaddleManager with the necessary components for paddle management.
     *
     * @param imageReader       Image reader for loading paddle images.
     * @param inputListener     Listener for capturing user input.
     * @param brickerGameManager Game manager for adding or removing game objects.
     */
    public PaddleManager(ImageReader imageReader, UserInputListener inputListener,
                         BrickerGameManager brickerGameManager) {
        this.windowDimensions =brickerGameManager.getGameWindowDimensions();
        this.paddleImage = imageReader.readImage(Constants.ResourcePath.PADDLE_IMAGE.getPath(), true);
        this.inputListener = inputListener;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Initializes and adds the main paddle to the game.
     * The main paddle is positioned at the bottom center of the game window.
     */
    public void initializeMainPaddle() {
        // Calculate horizontal center
        float paddleX = (windowDimensions.x() * Constants.CENTER_MULTIPLIER) -
                (PADDLE_WIDTH * Constants.CENTER_MULTIPLIER);
        float paddleY = windowDimensions.y() - DISTANCE_OF_PADDLE_FROM_BOTTOM - PADDLE_HEIGHT;
        Vector2 paddleTopLeftCorner = Vector2.of(paddleX, paddleY);

        // Create and add the main paddle to the game
        Paddle mainPaddle = new Paddle(paddleTopLeftCorner, PADDLE_DIMENSIONS, paddleImage,
                                       inputListener, windowDimensions,
                                       Constants.GameObjectsTags.MAIN_PADDLE.getValue());

        brickerGameManager.addGameObject(mainPaddle, Constants.GameLayer.PADDLES.getId());
    }

    /**
     * Creates and returns a VanishPaddle, which disappears after a set number of collisions.
     * It is positioned in the center of the game window.
     *
     * @return A VanishPaddle object.
     */
    public VanishPaddle initializeVanishPaddle() {
        Vector2 windowCenter = windowDimensions.mult(Constants.CENTER_MULTIPLIER);
        // Half dimensions for center alignment
        Vector2 halfPaddleSize = PADDLE_DIMENSIONS.mult(Constants.CENTER_MULTIPLIER);
        // Top-left corner based on center alignment
        Vector2 vanishPaddleTopLeftCorner = windowCenter.subtract(halfPaddleSize);

        return new VanishPaddle(vanishPaddleTopLeftCorner,PADDLE_DIMENSIONS,
                paddleImage, inputListener, windowDimensions, Constants.GameLayer.PADDLES.getId(),
                brickerGameManager, Constants.GameObjectsTags.BORDER.getValue(), extraPaddleCounter);
    }

    /**
     * Accesses the counter tracking the number of extra (special) paddles currently in the game.
     *
     * @return The Counter object for extra paddles.
     */
    public Counter getExtraPaddlesCounter() {
        return extraPaddleCounter;
    }

    /**
     * Returns the tag used for identifying the main paddle.
     *
     * @return The tag string for the main paddle.
     */
    public String getMainPaddleTag() {
        return Constants.GameObjectsTags.MAIN_PADDLE.getValue();
    }

    /**
     * Returns the layer ID for vanish paddle.
     * @return The layer ID for vanish paddle.
     */
    public int getVanishPaddleLayerId() {
        return Constants.GameLayer.PADDLES.getId();
    }
}