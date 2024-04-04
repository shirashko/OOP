package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.GameTag;

import java.awt.*;
import java.util.function.BiConsumer;

/**
 * Represents a fruit object within the game world that can be collected by the player's avatar.
 * Upon collection, the fruit disappears temporarily and grants energy to the avatar.
 * The fruit reappears after a predetermined delay, making it collectible again.
 * The fruit is able to change its color to an alternative color.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Fruit extends GameObject {
    // Constants
    private static final Color FRUIT_COLOR = Color.RED;
    private static final Color ALTERNATIVE_FRUIT_COLOR = Color.YELLOW;
    /**
     * The amount of energy granted by eating a fruit.
     */
    public static final float ENERGY_GAIN_PER_FRUIT = 10;

    // Fields
    // Possible callback to set for desired behavior when the fruit is eaten.behavior when the fruit is
    // eaten.
    private BiConsumer<GameObject, Collision> onFruitCollision = null;
    private Color color;

    /**
     * Constructs a new Fruit instance.
     *
     * @param topLeftCorner The position of the fruit in window coordinates (pixels).
     * @param dimensions    The dimensions of the fruit.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions) {
        super(topLeftCorner, dimensions, new OvalRenderable(FRUIT_COLOR));
        color = FRUIT_COLOR;
        setTag(GameTag.FRUIT.getTag());
    }

    /**
     * Sets the callback to be triggered when the fruit is eaten by the avatar.
     *
     * @param onFruitEaten The callback to be triggered when the fruit is eaten.
     */
    public void setOnCollisionEnter(BiConsumer<GameObject, Collision> onFruitEaten) {
        this.onFruitCollision = onFruitEaten;
    }

    /**
     * Handles collision events between the fruit and other game objects.
     * When the fruit collides with the avatar, it triggers the onFruitEaten callback,
     * grants energy to the avatar, and schedules the fruit to reappear after a delay.
     *
     * @param other The other GameObject involved in the collision.
     * @param collision Details about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (onFruitCollision != null) {
            onFruitCollision.accept(other, collision);
        }
    }

    /**
     * Changes the color of the fruit to an alternative color.
     */
    public void changeColor() {
        color = color == ALTERNATIVE_FRUIT_COLOR ? FRUIT_COLOR : ALTERNATIVE_FRUIT_COLOR;
        renderer().setRenderable(new OvalRenderable(color));
    }
}
