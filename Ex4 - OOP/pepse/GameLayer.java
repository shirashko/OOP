package pepse;

import danogl.collisions.Layer;

/**
 * An enumeration to define and manage the layer indices for rendering game objects within the PEPSE
 * game environment. The use of distinct layers ensures that game elements are rendered in a specific
 * order, which is crucial for rendering visuals correctly and for managing interactions such as
 * collisions, visibility, and layer-specific behaviors.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
enum GameLayer {

    /**
     * The sky layer. this layer is farthest back and should be rendered first, , providing a backdrop for
     * all other game elements.
     */
    SKY(Layer.BACKGROUND),
    /**
     * The sun layer. This layer is rendered behind the ground and trees, providing a visual representation
     * of the sun. The sun itself does not interact with the avatar.
     */
    SUN(Layer.BACKGROUND),


    /**
     * The sun halo layer. This layer is rendered in the sun layer, providing a glow effect for the sun.
     */
    SUN_HALO(SUN.getLayerID()),

    /**
     * The ground layer where the avatar and trees stand. This layer is rendered above the sky and sun
     * layers, but below the trees and leaves.
     */
    GROUND(Layer.STATIC_OBJECTS),

    /**
     * The trees layer. This layer is rendered above the ground layer, providing a visual representation
     * of trees that the avatar can collide with.
     * This layer shouldn't be used for other objects.
     */
    TREES_TRUNK(Layer.STATIC_OBJECTS - 2),

    /**
     * The leaves layer. This layer is rendered above the ground and trees, providing a visual representation
     * of leaves of trees, which the avatar can pass through.
     * This layer shouldn't be used for other objects.
     */
    LEAVES(Layer.STATIC_OBJECTS - 1),

    /**
     * The avatar layer. This layer is rendered above the ground for visual
     * effect.
     */
    AVATAR(Layer.DEFAULT),

    /**
     * The night overlay layer. This layer is rendered above all other layers, providing a visual overlay
     * for the night effect. The night overlay visually covers everything but does not affect interaction.
     */
    NIGHT(Layer.FOREGROUND),

    /**
     * The energy display layer. This layer is rendered above all other layers, providing a visual
     * representation of the avatar's energy level. The energy display visually covers everything but does
     * not affect interaction.
     */
    ENERGY_DISPLAY(Layer.UI),
    /**
     * The fruits layer. This layer is rendered above all other layers, providing a visual representation
     * of the fruits that the avatar can collect.
     */
    FRUITS(Layer.STATIC_OBJECTS + 3);

    // Holds the layer id associated with each enum constant.
    private final int id;

    /*
     * Constructs a GameLayer enum constant with the specified layer index.
     *
     * @param index The integer value representing the layer index for rendering.
     */
    GameLayer(int index) {
        this.id = index;
    }

    /**
     * Retrieves the layer index associated with the enum constant.
     *
     * @return An integer representing the layer id for rendering.
     */
    int getLayerID() {
        return id;
    }
}