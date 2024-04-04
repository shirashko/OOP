package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameTag;

import java.awt.*;

/**
 * Represents the sky in the game world. It is responsible for creating a sky-colored background
 * that spans the entire window and moves with the camera to maintain a consistent backdrop
 * across the game world.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Creates and returns a GameObject representing the sky.
     * This sky object will span the entire window and will be rendered with a solid color
     * representing the daytime sky. The created sky is set to move with the camera, which
     * ensures that it is always visible in the background, no matter where the camera moves.
     * It is also tagged for easy identification.
     *
     * @param windowDimensions A Vector2 object representing the width and height of the game window.
     * @return A GameObject representing the sky, with camera coordinate space and a 'sky' tag.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));

        // The sky's coordinate space is set to camera coordinates to ensure it moves with the camera,
        // giving the impression that the sky is a distant, stationary background layer.
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // The sky is tagged with "sky" to allow for easy identification and potential manipulation
        // or referencing in other parts of the game code.
        sky.setTag(GameTag.SKY.getTag());

        return sky;
    }
}
