package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Objects;

/**
 * Represents a specialized paddle that vanishes after being hit a certain number of times.
 * This class extends Paddle, adding behavior to track hits and remove itself from the game after
 * reaching a threshold.
 */
public class VanishPaddle extends Paddle {
    // Number of hits after which the paddle should vanish.
    private static final int NUMBER_OF_HITS_TO_VANISH = 4;

    // Reference to the game manager to allow removal of the paddle.
    private final BrickerGameManager brickerGameManager;

    // Tag used to identify border objects, to ignore collisions with borders.
    private final String borderTag;
    private int paddleHitCounter; // Counter for the number of times the paddle has been hit.
    private final int layerId; // The layer ID of the paddle, used for removal.
    private final Counter extraPaddlesCounter; // Tracks the number of extra paddles currently in the game.

    /**
     * Constructs a new VanishPaddle object.
     *
     * @param topLeftCorner The top-left corner of the paddle.
     * @param dimensions The size of the paddle.
     * @param paddleImage The visual representation of the paddle.
     * @param inputListener Listener for user input.
     * @param windowDimensions The dimensions of the game window.
     * @param layerId The layer ID where the paddle is added, for managing its removal.
     * @param brickerGameManager The game manager responsible for managing game objects.
     * @param borderTag The tag used to identify borders, to prevent vanishing on border collision.
     * @param extraPaddlesCounter A counter for tracking the number of extra paddles.
     */
    public VanishPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable paddleImage,
                        UserInputListener inputListener, Vector2 windowDimensions, int layerId,
                        BrickerGameManager brickerGameManager, String borderTag,
                        Counter extraPaddlesCounter) {
        super(topLeftCorner, dimensions, paddleImage, inputListener, windowDimensions, ""); // No need
        // for a special tag when the paddle is not the main paddle, and the falling life object should not
        // collide with it. In future versions, the tag could be used to identify the paddle for other
        // purposes.
        this.borderTag = borderTag;
        this.brickerGameManager = brickerGameManager;
        this.layerId = layerId;
        this.extraPaddlesCounter = extraPaddlesCounter;
        this.paddleHitCounter = 0;
    }

    /**
     * Called when this paddle collides with another game object.
     * This method handles the collision by incrementing the paddleHitCounter and removing the paddle
     * from the game if the counter reaches a predetermined number of hits.
     * Only one vanish paddle can be active at a time. If the paddle is removed, the extraPaddlesCounter is
     * decremented.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!collideWithBorder(other)) {
            paddleHitCounter++;

            if (paddleHitCounter == NUMBER_OF_HITS_TO_VANISH) {
                brickerGameManager.removeGameObject(this, layerId);
                extraPaddlesCounter.decrement();
                paddleHitCounter = 0;
            }
        }
    }

    /*
    * Checks if the paddle collided with the border.
    */
    private boolean collideWithBorder(GameObject other) {
        return Objects.equals(other.getTag(), borderTag);
    }
}
