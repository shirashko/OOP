package bricker.main;

import bricker.gameobjects.NumericLifeCounter;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.FallingLife;

/**
 * Manages and updates the visual representation of the player's lives within the game,
 * encompassing both graphical (as icons) and numeric displays. This class handles the
 * initialization, incrementation, decremental, and reset of life counters based on
 * gameplay events.
 */
public class LifeManager {

   /** Representing the number of lives indicating the player has lost the game. */
    public static final int LOOSING_NUMBER_OF_LIVES = 0;

    // Lives quantities
    private static final int INIT_NUMBER_OF_LIVES_PER_GAME = 3;
    private static final int MAX_NUMBER_OF_LIVES_PER_GAME = 4;

    // GraphicLifeCounter
    private static final int HEART_IMAGE_WIDTH = 20;
    private static final int HEART_IMAGE_HEIGHT = 20;
    private static final Vector2 HEART_IMAGE_DIMENSIONS = Vector2.of(HEART_IMAGE_WIDTH, HEART_IMAGE_HEIGHT);
    private final ImageRenderable lifeImage;
    private static final int DISTANCE_FROM_BOTTOM_FOR_GRAPHIC_LIVES = 10;

    // NumericLifeCounter
    private static final int DISTANCE_FROM_LEFT_FOR_LIVES_DISPLAY = 30;
    private static final float DISTANCE_FROM_BOTTOM_FOR_NUMERIC_LIVES = 13;
    private final static int FONT_SIZE_FOR_LIVES_DISPLAY = 20;
    private static final int NUMERIC_LIVES_X_OFFSET_FROM_HEARTS = -20;

    // Properties for managing the game state.
    private NumericLifeCounter numericLifeCounter;
    private final GraphicLifeManager graphicLifeCounter;
    private final BrickerGameManager brickerGameManager;
    private final Counter lifeCounter;


    /**
     * Manages the player's lives in a game, providing both graphical (icon-based) and numeric displays.
     * This class handles the initialization, incrementation, decremental, and reset of life counters
     * reflecting the player's current lives within the game context. It utilizes a {@link Counter} to track
     * the number of lives and updates both display forms accordingly.
     *
     * @param brickerGameManager The game manager instance to interact with the game window and lifecycle.
     */
    public LifeManager(BrickerGameManager brickerGameManager, ImageReader imageReader) {
        this.lifeImage = imageReader.readImage(Constants.ResourcePath.LIFE_IMAGE.getPath(), true);
        this.brickerGameManager = brickerGameManager;

        this.lifeCounter = new Counter(INIT_NUMBER_OF_LIVES_PER_GAME);
        this.numericLifeCounter = createNumericLifeCounter(brickerGameManager);
        this.graphicLifeCounter = createGraphicLifeCounter(brickerGameManager, lifeImage);
    }


    /**
     * Decrements the player's life count by one and updates the graphical and numeric displays accordingly.
     */
    public void decrementLife() {
        lifeCounter.decrement();
        graphicLifeCounter.decrementLife();
    }

    /**
     * Increments the player's life count by one and updates the graphical and numeric displays accordingly.
     */
    public void incrementLife() {
        if (lifeCounter.value() < MAX_NUMBER_OF_LIVES_PER_GAME) {
            lifeCounter.increment();
            graphicLifeCounter.incrementLife();
        }
    }


    /**
     * Returns the current number of lives the player has.
     * @return The current number of lives the player has.
     */
    public int getLifeCount() {
        return lifeCounter.value();
    }

    /**
     * Creates a new instance of the FallingHeart class to manage a falling heart object.
     * The falling heart is a game object that represents an extra life for the player.
     * When collected, it will increment the player's life count if the latter is not maxed.
     *
     * @param centerPosition   The position where the falling heart will be created.
     * @param tagToCollideWith The tag of the object the falling heart will collide with.
     *                         If the falling heart collides with an object with this tag,
     *                         it will trigger a collection event.
     */
    public void createFallingHeart(Vector2 centerPosition, String tagToCollideWith) {
        new FallingLife(centerPosition, HEART_IMAGE_DIMENSIONS, lifeImage, this,
                brickerGameManager, tagToCollideWith, Constants.GameLayer.FALLING_LIFE.getId());
    }

    /*
     * Creates a new instance of the GraphicLifeCounter class to manage the graphical representation
     * of the player's lives.
     * @param brickerGameManager The game manager instance to interact with the game window and lifecycle.
     * @param lifeImage The image used to represent a life in the graphical life counter.
     * @return A new instance of the GraphicLifeCounter class.
     */
    private GraphicLifeManager createGraphicLifeCounter(BrickerGameManager brickerGameManager,
                                                        ImageRenderable lifeImage) {
        return new GraphicLifeManager(
                Vector2.of(DISTANCE_FROM_LEFT_FOR_LIVES_DISPLAY,
                        DISTANCE_FROM_BOTTOM_FOR_GRAPHIC_LIVES),
                lifeImage,
                HEART_IMAGE_DIMENSIONS,
                INIT_NUMBER_OF_LIVES_PER_GAME,
                brickerGameManager,
                MAX_NUMBER_OF_LIVES_PER_GAME);
    }

    /*
     * Creates a new instance of the NumericLifeCounter class to manage the numeric representation
     * @param brickerGameManager The game manager instance to interact with the game window and lifecycle.
     * @return A new instance of the NumericLifeCounter class.
     */
    private NumericLifeCounter createNumericLifeCounter(BrickerGameManager brickerGameManager) {
        // Calculate the Y position for the numeric counter to align with the hearts
        float numericCounterYPosition = brickerGameManager.getGameWindowDimensions().y() -
                HEART_IMAGE_DIMENSIONS.y() - DISTANCE_FROM_BOTTOM_FOR_NUMERIC_LIVES;

        float numericCounterXPosition = DISTANCE_FROM_LEFT_FOR_LIVES_DISPLAY +
                NUMERIC_LIVES_X_OFFSET_FROM_HEARTS;

        Vector2 topLeftCorner = Vector2.of(numericCounterXPosition, numericCounterYPosition);
        Vector2 dimensions = Vector2.of(FONT_SIZE_FOR_LIVES_DISPLAY, FONT_SIZE_FOR_LIVES_DISPLAY);
        TextRenderable numericTextRenderable = new TextRenderable("");

        numericLifeCounter = new NumericLifeCounter(topLeftCorner, dimensions, numericTextRenderable,
                this.lifeCounter);

        brickerGameManager.addGameObject(numericLifeCounter, Constants.GameLayer.LIFE_PRESENTATION.getId());

        // Making sure the numeric life counter static when the camera moves
        numericLifeCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        return numericLifeCounter;
    }
}
