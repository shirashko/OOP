package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a brick in the game. This class extends GameObject and is responsible
 * for handling the behavior of a brick upon collision.
 * The behavior upon collision is defined by a provided CollisionStrategy.
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructor for creating a new Brick object.
     *
     * @param topLeftCorner The top left corner of the object's bounding box.
     * @param dimensions The dimensions of the object's bounding box.
     * @param renderable The object's renderable, used for rendering the brick on the screen.
     * @param collisionStrategy The strategy to be used when this brick is involved in a collision.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Called when this brick collides with another game object.
     * This method delegates the collision handling to the collisionStrategy.
     *
     * @param other The GameObject that this brick collided with.
     * @param collision The Collision object containing information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}
