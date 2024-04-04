package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class represents a graphical display element within the game world,
 * specifically designed to visually represent the player's current energy level as a percentage.
 * The display updates in real-time to reflect changes in the player's energy,
 * providing immediate feedback on energy consumption and regeneration.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class AvatarEnergyDisplay extends GameObject {
    // Constants
    private static final Color DISPLAY_COLOR = Color.BLACK; // The color used for the energy text display.
    private static final Vector2 DISPLAY_DIMENSIONS = Vector2.of(30, 30);
    private static final Vector2 DISPLAY_TOP_LEFT_CORNER = Vector2.of(10,10);
    private static final String MESSAGE_DISPLAY_FORMAT = "%.0f%%";

    // Fields
    private final TextRenderable textRenderable; // Renderable component rendering the text on screen.

    /**
     * Constructs a new EnergyDisplay instance.
     * The EnergyDisplay is a GameObject that displays the current energy level of the player as a
     * percentage.
     * It is designed to be updated dynamically to reflect changes in the player's energy level.
     * The display is positioned in the top-left corner of the game window and has a fixed size.
     */
    public AvatarEnergyDisplay() {
        // Call to the parent GameObject constructor.
        super(DISPLAY_TOP_LEFT_CORNER, DISPLAY_DIMENSIONS, null);

        // Create a new TextRenderable object to display the energy text, and set it as the renderable
        // component of this GameObject.
        this.textRenderable = new TextRenderable("");
        renderer().setRenderable(textRenderable);
        this.textRenderable.setColor(DISPLAY_COLOR); // Set the color of the text to the predefined
        // ENERGY_COLOR.
    }

    /**
     * Updates the text of the energy display to reflect the current energy level of the player.
     * This method is designed to be called externally, typically within the game's update loop,
     * to dynamically adjust the displayed text based on the player's current energy.
     *
     * @param currentEnergy The current energy level of the player, expected to be in the range of 0 to 100.
     *                      The value represents a percentage of the total energy capacity.
     */
    public void updateEnergyDisplay(float currentEnergy) {
        // Update the text of the TextRenderable object to display the current energy percentage.
        // The String.format method is used to convert the float value into a formatted string without
        // decimal places.
        textRenderable.setString(String.format(MESSAGE_DISPLAY_FORMAT, currentEnergy));
    }
}