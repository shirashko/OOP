package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a numeric display for the player's lives in the game.
 * This custom GameObject updates its text and color dynamically to reflect
 * the current number of lives remaining, providing visual feedback to the player.
 * The text color changes according to the number of lives: green for safe, yellow for warning,
 * and red for danger, indicating low lives remaining.
 */
public class NumericLifeCounter extends GameObject {
    // Color indicating a safe number of lives remaining.
    private static final Color SAFE_COLOR = Color.GREEN;

    // Color indicating a moderate, cautionary number of lives remaining.
    private static final Color WARNING_COLOR = Color.YELLOW;

    // Color indicating a low number of lives remaining, high danger.
    private static final Color DANGER_COLOR = Color.RED;

    // Threshold for changing the text color to the warning color.
    private static final int WARNING_LIVES_THRESHOLD = 2;
    // Threshold for changing the text color to the danger color.
    private static final int DANGER_LIVES_THRESHOLD = 1;
    // Initialize with a value that guarantees update in the first frame.
    private static final int INIT_PREV_LIFE_COUNT = -1;

    private final TextRenderable textRenderable; // The renderable component for displaying text.
    private final Counter livesCount; // Counter object tracking the number of lives.
    private int previousLivesCount; // Used to detect changes in the number of lives.

    /**
     * Constructs a new NumericLifeCounter instance.
     *
     * @param topLeftCorner The position on the screen where the life counter text will be displayed.
     * @param dimensions The size of the text display area.
     * @param renderable The text renderable used for displaying the number of lives.
     * @param livesCount A counter object that tracks the current number of lives.
     */
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable renderable,
                              Counter livesCount) {
        super(topLeftCorner, dimensions, renderable);
        this.textRenderable = renderable;
        this.livesCount = livesCount;
        this.previousLivesCount = INIT_PREV_LIFE_COUNT;
    }

    /**
     * Updates the display of the life counter each frame. If the number of lives has changed,
     * it updates the text to reflect the current number of lives and changes the text color
     * based on the remaining lives: green for safe, yellow for warning, and red for danger.
     *
     * @param deltaTime The time elapsed since the last frame update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int numberOfLives = livesCount.value();

        // Update the text and color only if the number of lives has changed.
        if (numberOfLives != previousLivesCount) {
            previousLivesCount = numberOfLives;
            textRenderable.setString(Integer.toString(numberOfLives));

            // Set the text color based on the current number of lives.
            if (numberOfLives <= DANGER_LIVES_THRESHOLD) {
                textRenderable.setColor(DANGER_COLOR);
            } else if (numberOfLives == WARNING_LIVES_THRESHOLD) {
                textRenderable.setColor(WARNING_COLOR);
            } else {
                textRenderable.setColor(SAFE_COLOR);
            }
        }
    }
}
