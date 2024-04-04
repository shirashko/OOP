package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Decorator for collision strategies
 * This class acts as the base decorator, holding a reference to a {@code CollisionStrategy}
 * object which it decorates.
 */
public abstract class CollisionStrategyDecorator implements CollisionStrategy {
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a new StrategyCollisionDecorator instance.
     * @param collisionStrategy The collision strategy to decorate.
     */
    public CollisionStrategyDecorator(CollisionStrategy collisionStrategy) {
        this.collisionStrategy = collisionStrategy;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        collisionStrategy.onCollision(thisObj, otherObj);
    }
}
