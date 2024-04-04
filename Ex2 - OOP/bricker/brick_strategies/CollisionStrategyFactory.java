package bricker.brick_strategies;

import bricker.gameobjects.*;
import bricker.main.*;
import danogl.util.Counter;

import java.util.Random;

/**
 * Factory class responsible for creating and configuring various collision strategies
 * for bricks in the game. This class enables dynamic behavior customization for bricks
 * upon collision by applying different effects such as adding extra balls, vanishing paddles,
 * changing camera focus, or gaining extra lives.
 * It also supports creating multiple special behaviors for a single brick.
 */
public class CollisionStrategyFactory {
    private static final int NUMBER_OF_SPECIAL_STRATEGIES = 5;
    private static final int TOTAL_SINGLE_SPECIAL_BEHAVIOR_STRATEGIES = 4;
    private static final int STRATEGY_CHANCE_BOUND = 10;
    private static final int MIN_NUMBER_OF_STRATEGIES_FOR_DOUBLE_BEHAVIOR = 2;

    private final BrickerGameManager brickerGameManager;
    private final BallManager ballManager;
    private final PaddleManager paddleManager;
    private final Counter bricksCounter;
    private final LifeManager lifeCounterManager;
    private final int cameraResetterLayerID;
    private final static Random random = new Random();

    /*
     * Enum representing different types of strategies that can be applied to bricks upon collision.
     */
    enum StrategyType {
        ADD_PUCKS,
        VANISH_PADDLE,
        CAMERA_FOCUS,
        GAIN_EXTRA_LIFE,
        DOUBLE_BEHAVIOR,
        BASIC; // for a default strategy as part of the random selection

        // Helper method to get a strategy type by index
        public static StrategyType byIndex(int index) {
            StrategyType[] values = StrategyType.values();
            if (index < 0 || index >= values.length) {
                return BASIC;
            }
            return values[index];
        }
    }


    /**
     * Constructs a new instance of CollisionStrategyFactory.
     *
     * @param brickerGameManager    The main game manager to interact with various game components.
     * @param lifeCounterManager    Manager for the player's life count in the game.
     * @param cameraResetterLayerID Layer ID for the camera resetter objects.
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager, BallManager ballManager,
                                    PaddleManager paddleManager, Counter bricksCounter,
                                    LifeManager lifeCounterManager, int cameraResetterLayerID) {
        this.ballManager = ballManager;
        this.paddleManager = paddleManager;
        this.bricksCounter = bricksCounter;
        this.brickerGameManager = brickerGameManager;
        this.lifeCounterManager = lifeCounterManager;
        this.cameraResetterLayerID = cameraResetterLayerID;
    }


    /**
     * Creates a random collision strategy from a predefined set of strategies.
     * Strategies include adding extra balls, vanishing paddle, gaining extra lives,
     * changing camera zoom and focus to follow the main ball of the game, as well as a basic collision
     * response.
     *
     * @return A CollisionStrategy object representing the chosen strategy.
     */
    public CollisionStrategy getRandomCollisionStrategy() {
        // Adjust the bound for random.nextInt to the number of strategies
        int randomValue = random.nextInt(STRATEGY_CHANCE_BOUND);
        StrategyType selectedType = StrategyType.byIndex(randomValue);
        BasicCollisionStrategy basicCollisionStrategy = createBasicStrategy();
        switch (selectedType) {
            case ADD_PUCKS:
                return createAddPucksStrategy(basicCollisionStrategy);
            case VANISH_PADDLE:
                return createAddVanishPaddleStrategy(basicCollisionStrategy);
            case CAMERA_FOCUS:
                return createCameraStrategy(basicCollisionStrategy);
            case GAIN_EXTRA_LIFE:
                return createGainExtraLifeStrategy(basicCollisionStrategy);
            case DOUBLE_BEHAVIOR:
                return createDoubleBehaviorStrategy(basicCollisionStrategy);
            default:
                return basicCollisionStrategy;
        }
    }


    /*
     * Creates a random single collision strategy wrapped around a given collision strategy.
     * This allows for dynamic behavior customization by chaining different effects.
     *
     * @param collisionStrategy The base collision strategy to be wrapped.
     * @return A CollisionStrategy object representing the enhanced strategy.
     */
    private CollisionStrategy getRandomSingleSpecialStrategy(CollisionStrategy collisionStrategy) {
        int randomValue = random.nextInt(TOTAL_SINGLE_SPECIAL_BEHAVIOR_STRATEGIES);
        StrategyType selectedType = StrategyType.byIndex(randomValue);
        switch (selectedType) {
            case ADD_PUCKS:
                return createAddPucksStrategy(collisionStrategy);
            case VANISH_PADDLE:
                return createAddVanishPaddleStrategy(collisionStrategy);
            case CAMERA_FOCUS:
                return createCameraStrategy(collisionStrategy);
            case GAIN_EXTRA_LIFE:
                return createGainExtraLifeStrategy(collisionStrategy);
        }
        return null; // This should never happen, but it's here to satisfy the compiler.
    }

    /*
     * Enhances a given collision strategy by potentially adding up to two additional
     * random strategies, creating a composite strategy with double (or triple) behavior.
     * It randomly decides whether to add one or two extra strategies on top
     * of the provided strategy, thereby enriching the collision effects dynamically.
     */
    private CollisionStrategy createDoubleBehaviorStrategy(CollisionStrategy baseCollisionStrategy) {
        // Decide the number of additional strategies to add. Start with the base assumption of adding one
        // extra strategy.

        // Subtract 1 because we always start with the base strategy.
        int enhancements = MIN_NUMBER_OF_STRATEGIES_FOR_DOUBLE_BEHAVIOR;

        // Randomly decide if we should add one more strategy, making it a total of two enhancements.
        // The logic checks if the random draw is not for a DOUBLE_BEHAVIOR, and if so, it tries again.
        // If DOUBLE_BEHAVIOR is drawn the second time, it means adding another strategy.
        int strategyDrawAttempt = random.nextInt(NUMBER_OF_SPECIAL_STRATEGIES);
        if (strategyDrawAttempt != StrategyType.DOUBLE_BEHAVIOR.ordinal()) {
            strategyDrawAttempt = random.nextInt(NUMBER_OF_SPECIAL_STRATEGIES);
        }
        if (strategyDrawAttempt == StrategyType.DOUBLE_BEHAVIOR.ordinal()) {
            enhancements++; // Increase for a total possibility of adding two additional strategies.
        }

        CollisionStrategy currentStrategy = baseCollisionStrategy;
        for (int i = 0; i < enhancements; i++) {
            // Directly wrap the current strategy within a new randomly chosen strategy.
            currentStrategy = getRandomSingleSpecialStrategy(currentStrategy);
        }

        return currentStrategy; // Return the final wrapped strategy.
    }


    // Private methods for creating specific collision strategy instances are documented below.
    // Each method initializes a new strategy object with the required configuration and parameters.

    private CollisionStrategy createCameraStrategy(CollisionStrategy collisionStrategy) {
        return new CameraStrategyDecorator(collisionStrategy, brickerGameManager,
                ballManager.getMainBall(), cameraResetterLayerID, ballManager.getMainBallTag());
    }

    private CollisionStrategy createGainExtraLifeStrategy(CollisionStrategy collisionStrategy) {
        return new GainExtraLifeStrategyDecorator(collisionStrategy, lifeCounterManager,
                paddleManager.getMainPaddleTag());
    }

    private BasicCollisionStrategy createBasicStrategy() {
        return new BasicCollisionStrategy(brickerGameManager, bricksCounter,
                BricksManager.getBricksLayerId());
    }

    private AddVanishPaddleStrategyDecorator createAddVanishPaddleStrategy(
            CollisionStrategy collisionStrategy) {
        VanishPaddle vanishPaddle = paddleManager.initializeVanishPaddle();
        return new AddVanishPaddleStrategyDecorator(brickerGameManager, vanishPaddle, collisionStrategy,
                paddleManager.getExtraPaddlesCounter(), paddleManager.getVanishPaddleLayerId());
    }

    private AddBallsStrategyDecorator createAddPucksStrategy(CollisionStrategy collisionStrategy) {
        PuckBall puck1 = ballManager.initializePuckBall();
        PuckBall puck2 = ballManager.initializePuckBall();
        PuckBall[] pucks = new PuckBall[]{puck1, puck2};
        return new AddBallsStrategyDecorator(collisionStrategy, brickerGameManager,
                pucks, ballManager.getBallLayerId());
    }
}

