package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameTag;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

/**
 * Represents a leaf of a tree.
 * The leaf will sway back and forth in a random angle, and will be created with a random initial delay.
 * This leaf can be rotated by 90 degrees.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Leaf extends GameObject {
    private static final Color BASE_COLOR = new Color(50, 200, 30);
    private static final float MAX_SWAY_START_DELAY_SECS = 3f; // Maximum initial delay in seconds for the
    // start
    private static final float SWAY_ANGLE_RANGE_DEGREES = 20f;
    private static final int LEAF_CYCLE_LENGTH = 1;
    private static final float WIDTH_SHRINK_RATIO = 0.8f;
    private static final int VERTICAL_ROTATION_ANGLE_DEGREES = 90;
    private static final Random random = new Random();

    /**
     * Construct a new Leaf instance.
     *
     * @param topLeftCorner Position of the leaf, in window coordinates (pixels).
     * @param dimensions    Width and height in window coordinates.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions) {
        super(topLeftCorner, dimensions,
              new RectangleRenderable(ColorSupplier.approximateColor(BASE_COLOR)));
        createWindEffect();
        this.setTag(GameTag.LEAF.getTag());
    }

    /*
     * Creates the wind effect for the leaf.
     */
    private void createWindEffect() {
        float waitTime = getWaitTime();
        initScheduledTask(waitTime, this::makeLeafMoveByTheWind);
        initScheduledTask(waitTime, this::makeLeafChangeWidthByTheWind);
    }

    /*
     * Makes the leaf change its width by the wind.
     */
    private void makeLeafChangeWidthByTheWind() {
        Vector2 startDimensions = Vector2.of(getDimensions().x(), getDimensions().y());
        Vector2 endDimensions = Vector2.of(startDimensions.x() * WIDTH_SHRINK_RATIO, startDimensions.y());
        new Transition<>(
                this,
                this::setDimensions,
                startDimensions,
                endDimensions,
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                LEAF_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /*
     * Initializes a scheduled task to perform the given action after the given wait time.
     */
    private void initScheduledTask(float waitTime, Runnable action) {
        new ScheduledTask(this, waitTime, false, action);
    }

    /*
     * Makes the leaf move in the wind by swaying back and forth in a random angle.
     */
    private void makeLeafMoveByTheWind() {
        float startAngle = renderer().getRenderableAngle();
        new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                startAngle,
                startAngle + SWAY_ANGLE_RANGE_DEGREES,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                LEAF_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /*
     * @return A random initial delay for the leaf to start swaying.
     */
    private static float getWaitTime() {
        return random.nextFloat() * MAX_SWAY_START_DELAY_SECS;
    }

    /**
     * Rotates the leaf by 90 degrees.
     */
    public void rotate() {
        new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                this.renderer().getRenderableAngle(),
                this.renderer().getRenderableAngle() + VERTICAL_ROTATION_ANGLE_DEGREES,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                LEAF_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_ONCE,
                null
        );
    }

}
