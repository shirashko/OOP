package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents an immovable rectangular block in the game, with a fixed size of 30x30 pixels.
 * This block is designed to remain static and unaffected by collisions with other objects,
 * maintaining its position and velocity. Collisions will result in no intersection if the
 * other object is also configured to prevent intersections. This behavior is contingent on
 * the objects being set to collide, typically by being in the same layer.
 * When the collision is avoided, the block will not be affected by the other object's velocity, only the
 * other object will be affected.
 * This class extends GameObject for inherited classes will be able to override onCollisionEnter
 * and by that to change the behavior of the block to a special behavior when it is collided.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Block extends GameObject {
    /**
     * The fixed size of all instances of Block in the game, in pixels.
     */
    public static final int SIZE = 30;

    /**
     * Constructs a Block instance at a given position with a specified appearance.
     *
     * @param topLeftCorner The top-left corner where the block will be positioned in the game window.
     * @param renderable    The visual representation of the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        setupPhysics();
    }

    /*
     * Configures the physics properties of the block to be immovable and to prevent intersections
     * from any direction, effectively making the block static during collisions.
     */
    private void setupPhysics() {
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
