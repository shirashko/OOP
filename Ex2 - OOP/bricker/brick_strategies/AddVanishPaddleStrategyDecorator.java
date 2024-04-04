package bricker.brick_strategies;

import bricker.gameobjects.VanishPaddle;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Counter;


/**
 * A collision strategy decorator that adds a functionality to add a vanishing paddle
 * into the game upon collision. This strategy checks if there's no other active vanish paddle,
 * and if there's non it adds a special paddle that disappears after being hit 4 times.
 */

public class AddVanishPaddleStrategyDecorator extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;
    private final VanishPaddle vanishPaddle;
    private final Counter extraPaddlesCounter;
    private final int vanishPaddleLayerId;

    /**
     * Constructs an AddVanishPaddleStrategyDecorator with necessary game elements and counters.
     *
     * @param brickerGameManager The game manager to interact with the game's object management.
     * @param vanishPaddle The vanish paddle object to be added if there's no active vanish paddle.
     * @param collisionStrategy The original collision strategy to be decorated with vanish paddle
     *                          functionality.
     * @param extraPaddlesCounter A counter to track the availability of extra paddles in the game.
     * @param vanishPaddleLayerId The layer ID for adding the vanish paddle to the game, ensuring
     *                            it appears correctly.
     */

    public AddVanishPaddleStrategyDecorator(BrickerGameManager brickerGameManager, VanishPaddle vanishPaddle,
                                            CollisionStrategy collisionStrategy, Counter extraPaddlesCounter,
                                            int vanishPaddleLayerId) {
        super(collisionStrategy);
        this.brickerGameManager = brickerGameManager;
        this.vanishPaddle = vanishPaddle;
        this.extraPaddlesCounter = extraPaddlesCounter;
        this.vanishPaddleLayerId = vanishPaddleLayerId;
    }

    /**
     * Overrides the onCollision method to include the logic for potentially adding a
     * vanish paddle to the game. This method is called when a collision occurs, and
     * it first calls the original collision strategy's onCollision
     * method. If there's no active vanish paddle, a vanish paddle is added to the game.
     *
     * @param thisObj The object this strategy is attached to, typically a brick object.
     * @param otherObj The object that has collided with thisObj.
     */

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if (extraPaddlesCounter.value() == 0) {
            brickerGameManager.addGameObject(vanishPaddle, vanishPaddleLayerId);
            extraPaddlesCounter.increment();
        }
    }
}
