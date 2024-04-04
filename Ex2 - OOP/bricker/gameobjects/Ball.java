package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the game, extending the GameObject class.
 * This class is responsible for handling the behavior of a ball,
 * including its response to collisions.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter;

    /**
     * @param topLeftCorner Position of the ball, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param renderable The renderable representing the ball. Can be null, in which case
     *              the GameObject will not be rendered.
     * @param collisionSound The sound to play when the ball collides with something.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
    }

    /**
     * Called when this ball start collides with another game object.
     * This method handles the collision by changing the ball's velocity
     * based on the collision normal and playing a sound effect.
     *
     * @param other The GameObject that this ball collided with.
     * @param collision The Collision object containing information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Whenever the ball collides with something, we flip its velocity and make a sound.
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionSound.play();

        collisionCounter++;
    }

    /**
     * Returns the number of collisions this ball has been involved in.
     * @return The number of collisions this ball has been involved in.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

}
