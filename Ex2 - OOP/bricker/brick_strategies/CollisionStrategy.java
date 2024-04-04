package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Defines a strategy for handling collisions between game objects in the game.
 * Implementations of this interface can define various behaviors that should occur
 * when a collision takes place.
 */
public interface CollisionStrategy {

    /**
     * Method to be called when a collision occurs between two game objects.
     * Implementations should define the specific behavior of what happens during
     * the collision, such as changing object properties or triggering game events.
     *
     * @param thisObj The first game object involved in the collision.
     * @param otherObj The second game object involved in the collision.
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
