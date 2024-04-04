package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;

/**
 * Manages the animations for a GameObject based on its movement state.
 * It handles the loading of animation frames and updates the GameObject's renderable
 * to reflect the current state of the avatar, such as idle, moving, or jumping.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
class AnimationManager {
    // Constants
    private static final String PNG_SUFFIX = ".png"; // Suffix for image files
    private static final String IDLE_ANIMATION_PREFIX = "assets/idle_"; // Prefix for idle animation frames
    private static final String JUMP_ANIMATION_PREFIX = "assets/jump_"; // Prefix for jump animation
    // frames
    private static final String RUN_ANIMATION_PREFIX = "assets/run_"; // Prefix for run animation frames
    private static final int IDLE_FRAME_COUNT = 4; // Number of frames in idle animation
    private static final int JUMP_FRAME_COUNT = 4; // Number of frames in jump animation
    private static final int RUN_FRAME_COUNT = 6; // Number of frames in run animation
    private static final double ANIMATION_FRAME_DURATION = 0.1; // Duration for each frame

    // Fields
    private AnimationRenderable idleAnimation; // Animation for idle state
    private AnimationRenderable jumpAnimation; // Animation for jumping state
    private AnimationRenderable runAnimation; // Animation for running state
    private final GameObject gameObject; // The GameObject whose animations are being managed
    private final ImageReader imageReader; // Used to read images for the animations

    /**
     * Constructs an AnimationManager for the given GameObject.
     *
     * @param gameObject The GameObject whose animations are to be managed.
     * @param imageReader The ImageReader used to load the animation frames.
     */
    public AnimationManager(GameObject gameObject, ImageReader imageReader) {
        this.gameObject = gameObject;
        this.imageReader = imageReader;
        loadAnimations(); // Loads all animations when instantiated
    }

    /**
     * Loads all animation frames and initializes the AnimationRenderable objects
     * for different movement states.
     */
    private void loadAnimations() {
        idleAnimation = loadAnimation(IDLE_ANIMATION_PREFIX, IDLE_FRAME_COUNT);
        jumpAnimation = loadAnimation(JUMP_ANIMATION_PREFIX, JUMP_FRAME_COUNT);
        runAnimation = loadAnimation(RUN_ANIMATION_PREFIX, RUN_FRAME_COUNT);
        gameObject.renderer().setRenderable(idleAnimation); // Set to idle animation initially
    }

    /**
     * Loads the animation frames for a given state and returns an AnimationRenderable.
     *
     * @param basePath The base path for the frame images.
     * @param frameCount The number of frames in the animation.
     * @return An AnimationRenderable containing all the frames for the animation.
     */
    private AnimationRenderable loadAnimation(String basePath, int frameCount) {
        Renderable[] frames = new Renderable[frameCount];
        for (int frameNumber = 0; frameNumber < frameCount; frameNumber++) {
            String imagePath = getImagePath(basePath, frameNumber);
            frames[frameNumber] = imageReader.readImage(imagePath, true);
        }
        return new AnimationRenderable(frames, ANIMATION_FRAME_DURATION);
    }

    /**
     * Updates the GameObject's renderable to the appropriate animation based on the current movement state.
     *
     * @param movement The current movement state of the GameObject.
     * @param isFlipped Whether the animation should be flipped horizontally (e.g., for leftward movement).
     */
    public void updateAnimation(MovementOption movement, boolean isFlipped) {
        switch (movement) {
            case IDLE:
                gameObject.renderer().setRenderable(idleAnimation);
                break;
            case MOVE_HORIZONTALLY:
                gameObject.renderer().setRenderable(runAnimation);
                gameObject.renderer().setIsFlippedHorizontally(isFlipped);
                break;
            case JUMP:
                gameObject.renderer().setRenderable(jumpAnimation);
                break;
        }
    }

    /*
     * Returns the path for a specific frame of an animation.
     */
    private static String getImagePath(String basePath, int frameNumber) {
        return basePath + frameNumber + PNG_SUFFIX;
    }
}
