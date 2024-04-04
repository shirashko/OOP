package bricker.main;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

/**
 * Represents the player's lives in a graphical form as a series of heart images on the screen.
 * The number of displayed hearts corresponds to the current value of a Counter object representing the
 * player's lives.
 */
public class GraphicLifeManager {
    // Constants defining layout parameters for the heart images
    private static final int HEART_IMAGES_SPACING = 5; // The spacing between consecutive heart images.

    // Properties for managing the game state and rendering
    private final BrickerGameManager brickerGameManager;
    private int curNumberOfLives;
    private final GameObject[] liveIcons; // Array to store heart GameObjects for display.

    /**
     * Constructs a new GraphicLifeCounter instance to display player's lives.
     *
     * @param lifeTopLeftCorner       The top-left corner position of this counter in window coordinates.
     * @param lifeImage           The image renderable used for each heart representing a life.
     * @param initNumberOfLives   The initial number of lives to display.
     * @param brickerGameManager  The game manager responsible for managing game objects.
     * @param maxPossibleLives    The maximum number of lives that can be displayed.
     */
    public GraphicLifeManager(Vector2 lifeTopLeftCorner, ImageRenderable lifeImage,
                              Vector2 heartImageDimensions, int initNumberOfLives,
                              BrickerGameManager brickerGameManager, int maxPossibleLives) {
        this.curNumberOfLives = initNumberOfLives;
        this.liveIcons = new GameObject[maxPossibleLives];
        this.brickerGameManager = brickerGameManager;

        Vector2 windowDimensions = brickerGameManager.getGameWindowDimensions();
        // Calculate the Y position for hearts to be at the bottom, considering the bottom margin
        float heartsYPosition = windowDimensions.y() - heartImageDimensions.y() - (int) lifeTopLeftCorner.y();

        // Initialize heart GameObjects and add them to the GameObjectCollection, positioned at the bottom
        // left.
        for (int i = 0; i < maxPossibleLives; i++) {
            Vector2 heartPosition = new Vector2(lifeTopLeftCorner.x() + i *
                                        (heartImageDimensions.x() + HEART_IMAGES_SPACING), heartsYPosition);
            liveIcons[i] = new GameObject(heartPosition, heartImageDimensions, lifeImage);
            if (i < initNumberOfLives) {
                brickerGameManager.addGameObject(liveIcons[i], Constants.GameLayer.LIFE_PRESENTATION.getId());

                // Making the live icons static when the game camera moves
                liveIcons[i].setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            }
        }
    }

    /**
     * Increments the number of lives displayed by adding a heart to the screen.
     */
    public void incrementLife() {
        brickerGameManager.addGameObject(liveIcons[curNumberOfLives++],
                Constants.GameLayer.LIFE_PRESENTATION.getId());
    }

    /**
     * Decrements the number of lives displayed by removing a heart from the screen.
     */
    public void decrementLife() {
        brickerGameManager.removeGameObject(liveIcons[--curNumberOfLives],
                Constants.GameLayer.LIFE_PRESENTATION.getId());
    }
}
