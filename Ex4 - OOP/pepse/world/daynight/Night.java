package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameTag;

import java.awt.*;

/**
 * This class is responsible for creating and managing the night effect in the game world.
 * It simulates the night by overlaying a semi-transparent black rectangle over the game window,
 * with its opacity changing to simulate the transition from day to night and vice versa.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Night {

    /* The maximum opacity level for the night overlay during the peak of the night. Represents a
    semi-transparent state to allow for visibility of objects beneath the overlay.
     */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates and returns a GameObject representing the night overlay.
     * The overlay's opacity will transition from fully transparent to semi-transparent
     * to simulate the night effect in a day-night cycle.
     *
     * @param windowDimensions The dimensions of the game window. The night overlay will span
     *                         the entire window.
     * @param cycleLength      The duration of one complete day-night cycle, in seconds.
     *                         This determines how quickly the transition between day and night occurs.
     * @return                 A GameObject representing the night overlay, with transitions set
     *                         to simulate day-night cycles.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Create a renderable component for the night overlay, using a black color to simulate darkness.
        RectangleRenderable nightRenderable = new RectangleRenderable(Color.BLACK);

        // Instantiate the night overlay GameObject with full window coverage.
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRenderable);

        // Ensure the overlay follows the camera to cover the entire game window at all times.
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Tag the GameObject for identification.
        night.setTag(GameTag.NIGHT.getTag());

        // Create and start a transition for the night overlay's opaqueness. The transition
        // oscillates between fully transparent (0) and semi-transparent (MIDNIGHT_OPACITY),
        // with a cubic interpolation for a smooth effect. The transition repeats back and forth
        // to simulate the continuous cycle of day and night.
        new Transition<>(night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return night;
    }
}
