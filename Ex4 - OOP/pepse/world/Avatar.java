package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import pepse.GameTag;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents the player's character in the game world.
 * The avatar has energy that regenerates over time when idle and decreases when moving or jumping.
 * The character can move left or right using keyboard arrows and jump using the spacebar.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Avatar extends GameObject {
    // Constants
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(50, 100);
    private static final int KEY_MOVE_LEFT = KeyEvent.VK_LEFT;
    private static final int KEY_MOVE_RIGHT = KeyEvent.VK_RIGHT;
    private static final int KEY_JUMP = KeyEvent.VK_SPACE;
    private static final float MOVEMENT_LEFT_THRESHOLD = 0;
    private static final int GRAVITY = 1000;  // Gravity acceleration.
    private static final float VELOCITY_X = 400; // Horizontal movement speed.
    private static final float JUMP_VELOCITY_Y = -650;  // Vertical velocity applied when jumping.
    private static final float ENERGY_REGEN_IDLE = 1; // Energy gained per update cycle when idle.
    private static final float ENERGY_LOSS_MOVE = 0.5f; // Energy lost per update cycle when moving.
    private static final float ENERGY_LOSS_JUMP = 10;  // Energy lost when initiating a jump.

    // Fields
    private final UserInputListener inputListener; // Listener for user inputs.
    private final AnimationManager animationManager; // Initializes and updates the avatar's animations.
    private final EnergyManager energyManager; // Manages the avatar's energy level and updates the listener.
    private final List<Runnable> jumpObservers = new ArrayList<>();
    private MovementOption currentMovement = MovementOption.IDLE;


    /**
     * Constructs a new Avatar instance.
     *
     * @param pos           The position on the screen where the avatar should be placed initially, with the
     *                      character standing in the bottom-right corner.
     * @param inputListener Listener to respond to user inputs.
     * @param imageReader   To read the image representing the avatar.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        // Calculate the position of the avatar's top-left corner based on the specified pos.
        super(Vector2.of(pos.x(), pos.y() - AVATAR_DIMENSIONS.y()), AVATAR_DIMENSIONS, null);
        this.inputListener = inputListener;

        // Set gravity acceleration here, assuming positive y is downward:
        transform().setAccelerationY(GRAVITY);

        // Prevent the avatar from intersecting other objects from any direction to handle collisions.
        physics().preventIntersectionsFromDirection(Vector2.ZERO);

        // Initialize the AnimationManager and set the initial state and animation.
        animationManager = new AnimationManager(this, imageReader);
        //animationManager.updateAnimation(MovementOption.IDLE, false);

        // Initialize the EnergyManager with listener and default energy set to the maximum level.
        this.energyManager = new EnergyManager();
        setTag(GameTag.AVATAR.getTag());
    }

    /**
     * Updates the avatar's state. Handles movement, jumping, and energy management.
     *
     * @param deltaTime Time since the last update cycle.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        processMovementAndEnergyConsumption();
    }

    /**
     * Registers an observer to receive notifications of energy level changes.
     * @param observer The observer to be notified of energy level changes. It is a Consumer of Float
     *                 that will be called with the new energy level as an argument.
     */
    public void addEnergyObserver(Consumer<Float> observer) {
        energyManager.addEnergyObserver(observer);
    }

    /**
     * Unregisters an observer from receiving notifications of energy level changes.
     * @param observer The observer to be removed.
     */
    public void removeEnergyObserver(Consumer<Float> observer) {
        energyManager.removeEnergyObserver(observer);
    }

    /**
     * Adjusts the avatar's energy level by a specified amount.
     * The energy level is constrained between 0 and MAX_ENERGY.
     *
     * @param amount The amount to adjust the energy by. Can be positive (energy gain)
     *               or negative (energy consumption).
     */
    public void addEnergy(float amount) {
        energyManager.regenerate(amount);
    }

    /**
     * Registers an observer to receive notifications of jump events.
     * @param observer The observer to be notified of jump events. It is a Runnable
     *                 whose run method will be called without arguments when a jump event occurs.
     */
    public void addJumpObserver(Runnable observer) {
        jumpObservers.add(observer);
        if (currentMovement == MovementOption.JUMP) { // If the avatar is currently jumping, notify the
            // observer immediately.
            observer.run();
        }
    }

    /**
     * Unregisters an observer from receiving notifications of jump events.
     * @param observer The observer to be removed.
     */
    public void removeJumpObserver(Runnable observer) {
        jumpObservers.remove(observer);
    }

    /*
     * Determines the avatar's movement based on user inputs and energy level.
     * Updates the energy level based on the avatar's actions.
     * If the avatar is idle, energy is regenerated.
     * If the avatar is moving, energy is deducted.
     * If the avatar is jumping, energy is deducted.
     */
    private void processMovementAndEnergyConsumption() {
        float xVel = 0;
        MovementOption nextMovement = MovementOption.IDLE;
        // Determine the avatar's current movement state. If already start jumping, keep jumping.
        this.currentMovement = getVelocity().y() == 0 ? MovementOption.IDLE : MovementOption.JUMP;

        if (horizontalMovementRequest() && energyManager.canConsume(ENERGY_LOSS_MOVE)) {
            nextMovement = MovementOption.MOVE_HORIZONTALLY;
            xVel = handleHorizontalMovement();
        }
        if (jumpMovementRequest() && isValidToInitiateNewJump() &&
                                                        energyManager.canConsume(ENERGY_LOSS_JUMP)) {
            handleJump();
            nextMovement = MovementOption.JUMP;
        }
        if (nextMovement == MovementOption.IDLE && getVelocity().y() == 0) {
            handleIdle();
        }

        transform().setVelocityX(xVel); // Apply horizontal velocity to the avatar based on the user's input.
        updateAnimation(xVel, nextMovement);
        currentMovement = nextMovement;
    }

    private void updateAnimation(float xVel, MovementOption nextMovement) {
        // Update animation based on movement. Flip the animation if moving left.
        boolean isMovingLeft = isVelocityIndicatingLeftMovement(xVel);
        animationManager.updateAnimation(nextMovement, isMovingLeft);
    }

    /*
     * Handles the avatar's idle state.
     */
    private void handleIdle() {
        energyManager.regenerate(ENERGY_REGEN_IDLE);
    }

    /*
     * Handles the avatar's horizontal movement.
     */
    private float handleHorizontalMovement() {
        float xVel = inputListener.isKeyPressed(KeyEvent.VK_LEFT) ? -VELOCITY_X : VELOCITY_X;
        energyManager.consume(ENERGY_LOSS_MOVE); // Use EnergyManager to consume energy.
        return xVel;
    }


    /*
     * Handles the avatar's jump action.
     */
    private void handleJump() {
        transform().setVelocityY(JUMP_VELOCITY_Y); // Apply jump velocity here
        energyManager.consume(ENERGY_LOSS_JUMP);
        notifyJumpObservers(); // Invoke jump callbacks
    }

    /*
     * Checks if the avatar is currently idle.
     */
    private boolean isValidToInitiateNewJump() {
        return currentMovement != MovementOption.JUMP;
    }

    /*
     * Checks if the user has requested a jump.
     */
    private boolean jumpMovementRequest() {
        return inputListener.isKeyPressed(KEY_JUMP);
    }

    /*
     * Checks if the user has requested horizontal movement.
     */
    private boolean horizontalMovementRequest() {
        return inputListener.isKeyPressed(KEY_MOVE_LEFT) || inputListener.isKeyPressed(KEY_MOVE_RIGHT);
    }

    /*
     * Notifies all registered observers about a jump event.
     * This method is called to inform observers when the avatar jumps.
     */
    private void notifyJumpObservers() {
        jumpObservers.forEach(Runnable::run);
    }

    /*
     * Checks if the velocity indicates leftward movement.
     */
    private boolean isVelocityIndicatingLeftMovement(float velocity) {
        return velocity < MOVEMENT_LEFT_THRESHOLD;
    }
}
