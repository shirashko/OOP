package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.GameTag;
import pepse.PepseGameManager;

import java.awt.*;

/**
 * This class is responsible for creating and managing the sun's movement in the game world,
 * simulating the day and night cycle by moving the sun in a circular path across the screen.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Sun {
    private static final float SUN_DIAMETER = 100; // Diameter of the sun in pixels.
    private static final Vector2 SUN_DIMENSIONS = Vector2.of(SUN_DIAMETER, SUN_DIAMETER);
    private static final float MORNING_ANGLE = 0f;
    private static final float NIGHT_ANGLE = 360f;
    private static final Color SUN_COLOR = Color.YELLOW;

    /**
     * Creates and returns a GameObject that represents the sun.
     * The sun is animated to move in a circular path, representing the day and night cycle.
     *
     * @param windowDimensions The dimensions of the game window, used to determine the sun's path.
     * @param cycleLength      The duration of one complete day-night cycle, in seconds.
     * @return A GameObject that visually represents the sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        OvalRenderable sunRenderable = new OvalRenderable(SUN_COLOR);
        Vector2 sunInitTopLeftCorner = getSunInitTopLeftCorner(windowDimensions);
        GameObject sun = new GameObject(sunInitTopLeftCorner, SUN_DIMENSIONS, sunRenderable);

        sun.setTag(GameTag.SUN.getTag()); // Assign a unique tag for identification.
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // Fixed position relative to the camera.
        createVisualSunCycle(windowDimensions, cycleLength, sun); // Initialize the sun's movement animation.
        return sun;
    }


    /*
     * Calculates the initial position of the sun, so it will be in the middle of the sky, to start the
     * day-night cycle.
     */
    private static Vector2 getSunInitTopLeftCorner(Vector2 windowDimensions) {
        float sunRadius = SUN_DIAMETER / 2;
        float xDimension = windowDimensions.x()/2 - sunRadius;
        float yDimension = windowDimensions.y() * PepseGameManager.SKY_TO_WINDOW_HEIGHT_RATIO / 2 -
                                                                                                sunRadius;
        return Vector2.of(xDimension, yDimension);
    }

    /*
     * Initializes and starts the animation transition for the sun's movement across the sky.
     * The sun follows a circular path centered on the game window to simulate the day-night cycle.
     *
     * @param windowDimensions The dimensions of the game window, used to calculate the sun's path.
     * @param cycleLength      The duration of the day-night cycle, in seconds.
     * @param sun              The GameObject representing the sun, to be animated.
     */
    private static void createVisualSunCycle(Vector2 windowDimensions, float cycleLength, GameObject sun) {
        float cycleCenterX = windowDimensions.x() / 2;
        float cycleCenterY = windowDimensions.y() * PepseGameManager.SKY_TO_WINDOW_HEIGHT_RATIO;
        Vector2 cycleCenter = Vector2.of(cycleCenterX, cycleCenterY);
        Vector2 initialSunCenter = sun.getCenter();

        new Transition<>(
                sun,
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter).
                                                                    rotated(angle).add(cycleCenter)),
                MORNING_ANGLE, // Start angle.
                NIGHT_ANGLE, // End angle for a full rotation.
                Transition.LINEAR_INTERPOLATOR_FLOAT, // Interpolation type for smooth transition.
                cycleLength, // Duration for the complete cycle.
                Transition.TransitionType.TRANSITION_LOOP, // Looping transition type for continuous cycle.
                null // No action on reaching the final value.
        );
    }
}