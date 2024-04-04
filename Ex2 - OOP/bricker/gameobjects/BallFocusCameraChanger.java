package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

/**
 * This class is responsible for resetting the camera's focus on the ball after a certain number of hits.
 * It tracks the number of collisions the ball has with other objects and resets the camera focus once
 * the specified number of hits has been reached. The camera zoom factor is also adjustable, allowing for
 * a closer view of the ball when it is being focused.
 */
public class BallFocusCameraChanger extends GameObject {
    // Number of hits required to reset the camera's focus on the ball.
    private static final int NUMBER_OF_HITS_TO_RESET_FOCUS = 4;

    // Zoom factor for the camera when focused on the ball.
    private static final float BALL_FOCUS_ZOOM_FACTOR = 1.2f;

    // Reference to the ball object to track its hits.
    private final Ball ball;

    // Reference to the game manager to control the camera settings.
    private final BrickerGameManager brickerGameManager;

    // The layer ID used when removing this object from the game.
    private final int layerId;

    // Counter for the initial number of hits of the ball at the time of this object's creation.
    private final int initialHitWhenCreated;

    /**
     * Constructs a new BallHitCameraResetter instance.
     *
     * @param ballToFollowHits The ball object whose hits are to be tracked.
     * @param brickerGameManager The game manager to control the camera settings.
     * @param layerId The layer ID for removing this object from the game.
     */
    public BallFocusCameraChanger(Ball ballToFollowHits, BrickerGameManager brickerGameManager, int layerId) {
        super(Vector2.ZERO, Vector2.ZERO, null); // Initializes with no position, size, or renderable.
        this.ball = ballToFollowHits;
        this.brickerGameManager = brickerGameManager;
        this.layerId = layerId;
        this.initialHitWhenCreated = ball.getCollisionCounter(); // Store initial number of hits.

        // Set the camera to focus on the ball with specified zoom factor.
        brickerGameManager.setCamera(
                new Camera(
                        ball, // Object to follow.
                        Vector2.ZERO, // Follow the center of the object.
                        // Zoom in on the ball.
                        brickerGameManager.getGameWindowDimensions().mult(BALL_FOCUS_ZOOM_FACTOR),
                        // Use game window dimensions for camera size.
                        brickerGameManager.getGameWindowDimensions()
                )
        );
    }

    /**
     * Updates the state of the camera resetter each frame.
     * Checks if the ball has reached the specified number of hits to reset the camera focus.
     *
     * @param deltaTime The time elapsed since the last update call.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Check if the number of hits has reached the threshold to reset the camera focus.
        if (ball.getCollisionCounter() >= NUMBER_OF_HITS_TO_RESET_FOCUS + initialHitWhenCreated) {
            brickerGameManager.setCamera(null); // Reset camera focus to default.
            brickerGameManager.removeGameObject(this, layerId); // Remove this object from the game.
        }
    }
}
