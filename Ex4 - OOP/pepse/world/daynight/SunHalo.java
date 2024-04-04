package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.GameTag;

import java.awt.*;

/**
 * This class is responsible for creating a halo effect around the sun.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class SunHalo {

    // Constants for halo sizing and coloring.
    private static final float HALO_SIZE_FACTOR = 1.5f; // Factor to scale the halo size relative to the sun.
    private static final int HALO_ALPHA_CHANNEL = 20; // Alpha value for halo transparency.
    private static final float HALO_CENTER_OFFSET_FACTOR = 0.5f; // Factor to center the halo on the sun.

    private static final Color HALO_COLOR = new Color(255, 255, 0, HALO_ALPHA_CHANNEL);


    /**
     * Creates and returns a GameObject representing the sun's halo.
     *
     * @param sun The sun GameObject around which the halo will be created.
     * @return A new GameObject representing the sun's halo.
     */
    public static GameObject create(GameObject sun) {
        OvalRenderable haloRenderable = new OvalRenderable(HALO_COLOR);
        Vector2 haloSize = sun.getDimensions().mult(HALO_SIZE_FACTOR);

        Vector2 sunCenter = sun.getCenter();
        // Adjust the halo position to be centered on the sun.
        Vector2 haloPosition = sunCenter.subtract(haloSize.mult(HALO_CENTER_OFFSET_FACTOR));

        GameObject sunHalo = new GameObject(haloPosition, haloSize, haloRenderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(GameTag.SUN_HALO.getTag());

        // Add a component to ensure the halo follows the sun's position.
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;
    }
}
