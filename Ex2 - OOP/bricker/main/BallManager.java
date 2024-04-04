package bricker.main;

import bricker.gameobjects.Ball;
import bricker.gameobjects.PuckBall;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Manages ball objects in the game, including the initialization and behavior of the main ball
 * and additional puck balls. This class handles creating these balls with specific images, sounds,
 * and initial positions, as well as setting their velocities.
 */
public class BallManager {
    // Ratio to determine puck ball size relative to main ball.
    private static final float PUCKBALL_TO_MAIN_BALL_RADIUS_RATIO = 0.75f;
    private final static int MAIN_BALL_RADIUS = 15;
    private final static int BALLS_SPEED = 150;
    private static final Random random = new Random();


    // Random generator for setting initial ball directions.
    private final Vector2 centerOfGameWindow;
    private final ImageRenderable ballImage; // Image for the main ball.
    private final ImageRenderable puckBallImage; // Image for puck balls.
    private final Sound collisionSound; // Sound to play on ball collisions.
    private final BrickerGameManager brickerGameManager; // Game manager to interact with game objects.
    private Ball mainBall; // Reference to the main ball.

    /**
     * Constructs a new BallManager with necessary configurations and resources.
     *
     * @param imageReader        Image reader for loading ball images.
     * @param soundReader        Sound reader for loading collision sounds.
     * @param brickerGameManager Game manager for interacting with game objects.
     */
    public BallManager(ImageReader imageReader, SoundReader soundReader,
                       BrickerGameManager brickerGameManager) {
        this.ballImage = imageReader.readImage(Constants.ResourcePath.BALL_IMAGE.getPath(), true);
        this.puckBallImage = imageReader.readImage(Constants.ResourcePath.PUCK_BALL_IMAGE.getPath(), true);
        this.collisionSound = soundReader.readSound(Constants.ResourcePath.BALL_HIT_SOUND.getPath());
        this.brickerGameManager = brickerGameManager;
        this.centerOfGameWindow = brickerGameManager.getGameWindowDimensions().
                                                                mult(Constants.CENTER_MULTIPLIER);
    }

    /**
     * Initializes and adds the main ball to the game.
     * The main ball is positioned at the center of the game window and given a random initial velocity.
     */
    public void initializeMainBall() {
        Vector2 ballDimensions = Vector2.of(2 * MAIN_BALL_RADIUS, 2 * MAIN_BALL_RADIUS);
        Vector2 ballInitialPosition = centerOfGameWindow.subtract(Vector2.of(MAIN_BALL_RADIUS,
                                                                             MAIN_BALL_RADIUS));
        this.mainBall = new Ball(ballInitialPosition, ballDimensions, ballImage, collisionSound);
        mainBall.setTag(Constants.GameObjectsTags.MAIN_BALL.getValue());
        randomizeBallDirection(mainBall);
        brickerGameManager.addGameObject(mainBall, Constants.GameLayer.BALLS.getId());
    }

    /**
     * Creates a puck ball with dimensions based on the PUCKBALL_TO_MAIN_BALL_RADIUS_RATIO, placing
     * it at the game window's center and setting its velocity to a random direction, but only
     * towards the upper part of the game window.
     *
     * @return The initialized puck ball.
     */
    public PuckBall initializePuckBall() {
        float puckballDiameter = 2 * MAIN_BALL_RADIUS * PUCKBALL_TO_MAIN_BALL_RADIUS_RATIO;
        Vector2 puckBallDimensions = Vector2.of(puckballDiameter, puckballDiameter);
        PuckBall puckBall = new PuckBall(brickerGameManager, Vector2.ZERO, puckBallDimensions,
                puckBallImage, collisionSound, Constants.GameLayer.BALLS.getId());
        randomizeInTheUpperPartOfTheUnitCircle(puckBall);
        return puckBall;
    }

    /**
     * Retrieves the main ball object.
     *
     * @return The main ball.
     */
    public Ball getMainBall() {
        return mainBall;
    }

    /**
     * Retrieves the layer ID for balls in the game.
     *
     * @return The layer ID for balls.
     */
    public int getBallLayerId() {
        return Constants.GameLayer.BALLS.getId();
    }

    /**
     * Retrieves the tag for the main ball.
     *
     * @return The tag of the main ball.
     */
    public String getMainBallTag() {
        return Constants.GameObjectsTags.MAIN_BALL.getValue();
    }

    /**
     * Resets the main ball by setting its position to the center of the game window
     * and randomizing its velocity to a new direction.
     */
    void resetMainBall() {
        // Set the ball to the center of the window
        mainBall.setCenter(centerOfGameWindow);

        // Set the ball's velocity to a random direction
        randomizeBallDirection(mainBall);
    }

    /*
     * Sets the velocity of a ball to a random direction at the predefined speed.
     *
     * @param ball The ball whose velocity is to be randomized.
     */
    private void randomizeBallDirection(GameObject ball) {
        int ballVelX = random.nextBoolean() ? BALLS_SPEED : -BALLS_SPEED;
        int ballVelY = random.nextBoolean() ? BALLS_SPEED : -BALLS_SPEED;
        ball.setVelocity(Vector2.of(ballVelX, ballVelY));
    }

    /*
     * Sets the velocity of a ball towards the upper part of the unit circle, simulating
     * a random upwards direction.
     *
     * @param ball The ball whose velocity is to be randomized towards the upper part.
     */
    private void randomizeInTheUpperPartOfTheUnitCircle(GameObject ball) {
        double angle = random.nextDouble() * Math.PI; // Angle in radians, ensuring upward direction
        float velocityX = (float)Math.cos(angle) * BALLS_SPEED;
        float velocityY = -(float)Math.sin(angle) * BALLS_SPEED; // Negative to ensure upward motion
        ball.setVelocity(Vector2.of(velocityX, velocityY));
    }
}
