package pepse.world;

/**
 * Enumerates the different movement options for the Avatar.
 * Used by AnimationManager to switch animations based on the Avatar's actions.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public enum MovementOption {
    /**
     * Avatar is standing still
     */
    IDLE,

    /**
     * Avatar is moving left or right
     */
    MOVE_HORIZONTALLY,

    /**
     * Avatar is jumping
     */
    JUMP;
}
