package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.BallFocusCameraChanger;
import bricker.main.BrickerGameManager;
import danogl.GameObject;


/**
 * A decorator for collision strategies that adds camera focusing functionality
 * on the ball upon collision. This strategy ensures that when a collision occurs
 * with the main ball, which focuses the camera on the ball and changes the zoom.
 */

public class CameraStrategyDecorator extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;
    private final Ball ball;
    private final int cameraResetterLayerId;
    private final String mainBallTag;

    /**
     * Constructs a CameraStrategyDecorator with a given collision strategy, game manager, and ball.
     *
     * @param collisionStrategy The original collision strategy to be decorated with camera functionality.
     * @param gameManager The game manager, providing access to game-wide operations like adding objects.
     * @param ball The ball object to focus the camera on upon collision.
     * @param cameraResetterLayerId The layer ID to add the camera resetter object. This parameter might
     *                              seem unusual, but it's used to manage the game object layering
     *                              system for adding the camera resetter.
     * @param mainBallTag The tag used to identify the main ball object, used to check collision targets.
     */

    public CameraStrategyDecorator(CollisionStrategy collisionStrategy, BrickerGameManager gameManager,
                                   Ball ball,int cameraResetterLayerId, String mainBallTag) {
        super(collisionStrategy);
        this.brickerGameManager = gameManager;
        this.ball = ball;
        this.cameraResetterLayerId = cameraResetterLayerId;
        this.mainBallTag = mainBallTag;
    }

    /**
     * Invokes the decorated collision strategy's behavior and additionally checks if the collision
     * involves the main ball. If so, and if the camera strategy is not already active, it activates
     * the camera focusing mechanism by adding a BallHitCameraResetter object to the game.
     *
     * @param thisObj The game object this strategy is attached to.
     * @param otherObj The game object that has collided with thisObj.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if (collideWithBall(otherObj) && CameraStrategyIsNotOn()) {
            brickerGameManager.addGameObject(
                    new BallFocusCameraChanger(ball, brickerGameManager, cameraResetterLayerId),
                    cameraResetterLayerId);
        }
    }

    private boolean CameraStrategyIsNotOn() {
        return brickerGameManager.camera() == null;
    }

    private boolean collideWithBall(GameObject otherObj) {
        return otherObj.getTag().equals(mainBallTag);
    }
}
