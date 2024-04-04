package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import bricker.main.LifeManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.util.Objects;

/**
 * Represents a falling heart object in the game, which can increment the player's life count
 * when collided with the main paddle. This class extends GameObject, allowing it to participate
 * in the game's physics and rendering systems. The heart moves downwards at a constant speed and
 * is removed from the game either upon collision with the main paddle or when it falls off the screen.
 */
public class FallingLife extends GameObject {

    // Constant speed at which the heart falls down the screen.
    private static final int FALLING_LIFE_SPEED = 100;
    private final LifeManager lifeCounterManager; // Manages the player's life count.
    private final BrickerGameManager brickerGameManager; // Game manager for adding or removing game objects.
    private final String mainPaddleTag; // Tag identifier for the main paddle to detect collisions.

    // Layer ID for adding the heart to the game, ensuring proper rendering and collision.
    private final int layerId;
    /**
     * Constructs a new FallingHeart instance with specified properties and adds it to the game.
     *
     * @param topLeftCorner        Position of the heart in window coordinates, where (0,0) is the top-left.
     * @param dimensions           Dimensions of the heart in window coordinates (width and height).
     * @param heartImageRenderable The renderable image for the heart, providing its visual appearance.
     * @param lifeCounterManager   The manager responsible for tracking and updating the player's lives.
     * @param brickerGameManager   The game manager for handling game objects.
     * @param mainPaddleTag        The tag used to identify the main paddle for collision purposes.
     * @param layerId              The layer ID on which the heart should be added, affecting rendering and
     *                             collisions.
     */
    public FallingLife(Vector2 topLeftCorner, Vector2 dimensions, ImageRenderable heartImageRenderable,
                       LifeManager lifeCounterManager, BrickerGameManager brickerGameManager,
                       String mainPaddleTag, int layerId) {
        super(topLeftCorner, dimensions, heartImageRenderable);
        this.lifeCounterManager = lifeCounterManager;
        this.brickerGameManager = brickerGameManager;
        this.mainPaddleTag = mainPaddleTag;
        this.layerId = layerId;
        this.setVelocity(Vector2.of(0, FALLING_LIFE_SPEED)); // Set the heart to fall downwards.
        brickerGameManager.addGameObject(this, layerId); // Add the heart to the game.
    }

    /**
     * Determines whether this heart should collide with another game object.
     * In this case, the heart should only collide with the main paddle.
     *
     * @param other The other GameObject involved in the potential collision.
     * @return true if the other object is the main paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Objects.equals(other.getTag(), mainPaddleTag);
    }

    /**
     * Handles the event when the heart collides with another object, specifically the main paddle.
     * Increments the player's life count and removes the heart from the game.
     *
     * @param other     The GameObject with which this heart has collided.
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // If the heart collides with the main paddle, increment the life count.
        lifeCounterManager.incrementLife();
        brickerGameManager.removeGameObject(this, layerId); // Remove the heart from the game.
    }

    /**
     * Updates the heart's state each frame. Specifically, it checks if the heart has fallen
     * off the bottom of the screen and removes it from the game if so.
     *
     * @param deltaTime The time in seconds since the last frame update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Check if the heart has fallen off the screen and remove it if necessary.
        if (this.getTopLeftCorner().y() > brickerGameManager.getGameWindowDimensions().y()) {
            brickerGameManager.removeGameObject(this, layerId);
        }
    }
}
