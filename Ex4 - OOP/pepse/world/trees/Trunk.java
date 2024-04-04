package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

import java.util.Random;

/**
 * Represents the trunk part of a tree.
 * This trunk can change its color to different shades of brown.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Trunk extends GameObject {
    private static final Color BASE_TRUNK_COLOR = new Color(100, 50, 20); // Base color of the tree trunk.
    private static final int MIN_TRUNK_HEIGHT = 100; // Minimum trunk height
    private static final int MAX_TRUNK_HEIGHT = 200; // Maximum trunk height
    private static final Random random = new Random();


    /**
     * Constructs a new Trunk instance.
     *
     * @param bottomLeftCorner The position of the trunk's bottom-left corner in window coordinates (pixels).
     * @param trunkWidth       The width of the trunk in pixels.
     */
    public Trunk(Vector2 bottomLeftCorner, int trunkWidth) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        int trunkHeight = getRandomTrunkHeight();
        Vector2 topLeftCorner = Vector2.of(bottomLeftCorner.x(), bottomLeftCorner.y() - trunkHeight);
        setDimensions(Vector2.of(trunkWidth, trunkHeight));
        setTopLeftCorner(topLeftCorner);
        changeColor();

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Changes the color of the trunk to a random shade of brown.
     * The new color is applied to the trunk's renderable component.
     */
    public void changeColor() {
        Color curTrunkColor = ColorSupplier.approximateColor(BASE_TRUNK_COLOR);
        Renderable trunkRenderable = new RectangleRenderable(curTrunkColor);
        renderer().setRenderable(trunkRenderable);
    }

    /*
     * Generates a random height for the trunk within the specified range.
     *
     * @return The random height of the trunk.
     */
    private static int getRandomTrunkHeight() {
        int randomHeightComponent = random.nextInt(MAX_TRUNK_HEIGHT - MIN_TRUNK_HEIGHT + 1);
        return MIN_TRUNK_HEIGHT + randomHeightComponent;
    }
}

