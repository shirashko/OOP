package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Counter;

/**
 * A basic implementation of the CollisionStrategy interface.
 * This strategy handles the collision of a game object by removing it from the game.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private final BrickerGameManager brickerGameManager;
    private final Counter bricksCounter;
    private final int bricklayerId;

    /**
     * Constructs a new BasicCollisionStrategy instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     *                               This is used to remove game objects from the game upon collision.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager, Counter bricksCounter,
                                  int brickLayerId) {
        this.brickerGameManager = brickerGameManager;
        this.bricksCounter = bricksCounter;
        this.bricklayerId = brickLayerId;
    }


    /**
     * This method is called when a collision is detected.
     * It removes the colliding object (thisObj) from the game.
     *
     * @param thisObj The game object associated with this collision strategy that is involved
     *                in the collision.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (brickerGameManager.removeGameObject(thisObj, bricklayerId))
        {
            bricksCounter.decrement();
        }
    }
}
