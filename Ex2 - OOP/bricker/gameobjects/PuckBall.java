package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a special type of Ball, known as a PuckBall, in the game. PuckBalls have the
 * same basic behaviors as a regular Ball but with additional logic, potentially including
 * unique interactions within the game world. For example, a PuckBall have a different
 * collision behavior and a limited lifespan.
 */
public class PuckBall extends Ball {
    private final BrickerGameManager brickerGameManager;
    private final int layerId;

    /**
     * Constructs a new PuckBall object with specified properties.
     *
     * @param brickerGameManager The game manager responsible for managing game state and interactions.
     * @param topLeftCorner The top-left corner of the ball's initial position.
     * @param dimensions The dimensions (width and height) of the ball.
     * @param renderable The visual representation of the ball.
     * @param collisionSound The sound to play when the ball collides with another object.
     */
    public PuckBall(BrickerGameManager brickerGameManager, Vector2 topLeftCorner, Vector2 dimensions,
                    Renderable renderable, Sound collisionSound, int layerId) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.brickerGameManager = brickerGameManager;
        this.layerId = layerId;
    }

    /**
     * Update method called once per frame, used to implement game-specific behavior.
     * This method checks if the PuckBall is within the bounds of the game window and
     * removes it from the game if it is not.
     *
     * @param deltaTime The time elapsed since the last frame update.
     */
    @Override
    public void update(float deltaTime) {
        if (brickerGameManager.isInsideWindowBounds(this)) {
            super.update(deltaTime);
        } else {
            brickerGameManager.removeGameObject(this, layerId);
        }
    }
}

