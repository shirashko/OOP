package bricker.brick_strategies;

import bricker.main.LifeManager;
import danogl.GameObject;

/**
 * Decorator for collision strategy that adds extra life to the game when the object is destroyed
 */
public class GainExtraLifeStrategyDecorator extends CollisionStrategyDecorator {
    private final LifeManager lifeCounterManager;
    private final String mainPaddleTag;

    /**
     * Constructor for the decorator that spawns extra life possibility to the game when
     * collision happens in shapeof falling life image from the destroyed object (that other
     * game object collided with), that can be caught by the paddle.
     * @param basicCollisionStrategy basic collision strategy
     * @param lifeCounterManager life counter manager
     * @param mainPaddleTag main paddle tag
     */
    public GainExtraLifeStrategyDecorator(CollisionStrategy basicCollisionStrategy,
                                             LifeManager lifeCounterManager,
                                             String mainPaddleTag) {
        super(basicCollisionStrategy);
        this.lifeCounterManager = lifeCounterManager;
        this.mainPaddleTag = mainPaddleTag;
    }

    /**
     * Method that is called when the collision happens. It calls the basic collision strategy and then spawns
     * @param thisObj The first game object involved in the collision.
     * @param otherObj The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        lifeCounterManager.createFallingHeart(thisObj.getCenter(), mainPaddleTag);
    }
}
