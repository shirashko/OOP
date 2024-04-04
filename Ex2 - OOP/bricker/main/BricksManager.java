package bricker.main;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Manages the creation, layout, and tracking of bricks in the game.
 * It is responsible for initializing bricks at the start of the game,
 * maintaining a count of the bricks, and dynamically adjusting the layout
 * based on the game window dimensions and specified configurations.
 */
public class BricksManager {
    private final static int HEIGHT_DISTANCE_OF_BRICKS_FROM_EACH_OTHER = 2;
    private final static int WIDTH_DISTANCE_OF_BRICKS_FROM_EACH_OTHER = 1;
    private final static int BRICK_HEIGHT = 15;
    private final Counter bricksCounter;
    private final BrickerGameManager brickerGameManager;
    private final CollisionStrategyFactory collisionStrategyFactory;
    private final ImageRenderable brickImage;
    private final int numberOfBricksInRow;
    private final int numberOfBricksRows;

    /**
     * Constructs a new BricksManager instance.
     * @param brickerGameManager The game manager responsible for managing game objects.
     * @param imageReader The image reader to load the brick image.
     * @param collisionStrategyFactory The factory to create collision strategies for the bricks.
     * @param numberOfBricksInRow The number of bricks in each row.
     * @param numberOfBricksRows The number of rows of bricks.
     * @param bricksCounter The counter to track the number of bricks in the game.
     */
    public BricksManager(BrickerGameManager brickerGameManager, ImageReader imageReader,
                         CollisionStrategyFactory collisionStrategyFactory,
                         int numberOfBricksInRow, int numberOfBricksRows, Counter bricksCounter) {
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategyFactory = collisionStrategyFactory;
        this.brickImage = imageReader.readImage(Constants.ResourcePath.BRICK_IMAGE.getPath(), false);
        this.numberOfBricksInRow = numberOfBricksInRow;
        this.numberOfBricksRows = numberOfBricksRows;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Returns the layer ID for the bricks.
     * @return The layer ID for the bricks.
     */
    public static int getBricksLayerId() {
        return Constants.GameLayer.BRICKS.getId();
    }

    /**
     * Initializes the bricks layout by creating rows of bricks according
     * to the specified configurations. It calculates the brick width and
     * starting position to ensure a consistent layout across the game window.
     */
    public void initializeBricks() {
        float brickWidth = calculateBrickWidth();
        float startX = calculateStartXPosition(brickWidth);
        for (int row = 0; row < numberOfBricksRows; row++) {
            createRowOfBricks(row, startX, brickWidth, brickImage, collisionStrategyFactory);
        }
    }

    /**
     * Returns the current count of bricks.
     *
     * @return The current brick count.
     */
    public Counter getBricksCounter() {
        return bricksCounter;
    }

    /*
     * Calculates the width of each brick based on the game window dimensions
     * and the configured number of bricks per row and spaces between them.
     *
     * @return The calculated brick width.
     */
    private float calculateBrickWidth() {
        Vector2 windowDimensions = brickerGameManager.getGameWindowDimensions();
        float totalAvailableWidth = windowDimensions.x() - 2 * Constants.BORDER_THICKNESS;
        return (totalAvailableWidth - (numberOfBricksInRow - 1) *
                WIDTH_DISTANCE_OF_BRICKS_FROM_EACH_OTHER) / numberOfBricksInRow;
    }

    /*
     * Calculates the starting X position for the bricks to ensure they are centered
     * in the game window.
     *
     * @param brickWidth The width of a single brick.
     * @return The calculated starting X position.
     */
    private float calculateStartXPosition(float brickWidth) {
        float totalRowWidth = brickWidth * numberOfBricksInRow + (numberOfBricksInRow - 1) *
                WIDTH_DISTANCE_OF_BRICKS_FROM_EACH_OTHER;
        return (brickerGameManager.getGameWindowDimensions().x() - totalRowWidth) / 2;
    }

    /*
     * Creates a row of bricks at a specified vertical position.
     * Each brick is instantiated with a collision strategy and added to the game.
     *
     * @param row The row number (vertical position) to create the bricks at.
     * @param startX The starting X position for the first brick in the row.
     * @param brickWidth The width of each brick.
     * @param brickImage The image to be used for rendering the bricks.
     * @param collisionStrategyFactory The factory to create collision strategies for the bricks.
     */
    private void createRowOfBricks(int row, float startX, float brickWidth, ImageRenderable brickImage,
                                   CollisionStrategyFactory collisionStrategyFactory) {
        for (int col = 0; col < numberOfBricksInRow; col++) {
            Vector2 brickPosition = Vector2.of(startX + col *
                            (brickWidth + WIDTH_DISTANCE_OF_BRICKS_FROM_EACH_OTHER),
                    BRICK_HEIGHT * row + row * HEIGHT_DISTANCE_OF_BRICKS_FROM_EACH_OTHER +
                            Constants.BORDER_THICKNESS);
            CollisionStrategy collisionStrategy = collisionStrategyFactory.getRandomCollisionStrategy();

            GameObject brick = new Brick(brickPosition, Vector2.of(brickWidth, BRICK_HEIGHT),
                    brickImage, collisionStrategy);
            brickerGameManager.addGameObject(brick, Constants.GameLayer.BRICKS.getId());
            bricksCounter.increment();
        }
    }
}
