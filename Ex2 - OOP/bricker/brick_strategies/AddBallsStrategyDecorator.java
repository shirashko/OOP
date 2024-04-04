package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.PuckBall;
import bricker.main.BrickerGameManager;
import danogl.GameObject;


/**
 * This class is a decorator for CollisionStrategy that adds functionality to add additional balls
 * into the game upon a collision event. It is part of the strategy design pattern implementation,
 * allowing dynamic change of behavior during runtime.
 */

public class AddBallsStrategyDecorator extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;
    private final PuckBall[] pucks;
    private final int puckLayerId;

    /**
     * Constructs an AddBallsStrategyDecorator object.
     *
     * @param collisionStrategy The collision strategy to be decorated.
     * @param gameManager The game manager responsible for managing game objects.
     * @param pucks An array of puck-balls to be added to the game upon collision.
     * @param layerId The layer ID where the balls will be added.
     */
    public AddBallsStrategyDecorator(CollisionStrategy collisionStrategy, BrickerGameManager gameManager,
                                     PuckBall[] pucks, int layerId) {
        super(collisionStrategy);
        this.brickerGameManager = gameManager;
        this.pucks = pucks;
        this.puckLayerId = layerId;
    }

    /**
     * Called when a collision occurs between two game objects. This method enhances the base
     * collision behavior by adding two balls to the game at the collision location.
     *
     * @param thisObj The game object initiating the collision.
     * @param otherObj The game object with which the initiating object has collided.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        for(Ball puck: pucks) {
            puck.setCenter(thisObj.getCenter());
            brickerGameManager.addGameObject(puck, puckLayerId);
        }
    }
}
