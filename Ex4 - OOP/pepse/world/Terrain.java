package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameTag;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates and manages terrain blocks within the game window. Terrain blocks are GameObjects that
 * represent the ground. The Terrain class also provides methods to determine the height of the ground
 * at any given x-coordinate, enabling interaction with other objects in the game world.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Terrain {
    // The base color of the ground blocks. The blocks will have varying shades of this color.
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    // Controls "frequency" of the terrain undulations. A larger number results in more frequent ups and
    // downs (more 'hilly' terrain), and a smaller number results in smoother terrain.
    private static final int NOISE_SCALING_FACTOR = 7;

    // The number of blocks to stack under the top ground layer. Controls how many layers of blocks are
    // created beneath the ground height.
    private static final int TERRAIN_DEPTH = 20;

    // ground height at x=0
    private final float groundHeightAtX0;

    // Generates Perlin noise for terrain undulations.
    private final NoiseGenerator noiseGenerator;


    /**
     * Initializes a new Terrain instance.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             A seed for the noise generator to ensure reproducible terrain shapes.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        // Assume the ground covers the lower third of the screen.
        this.groundHeightAtX0 = windowDimensions.y() * PepseGameManager.SKY_TO_WINDOW_HEIGHT_RATIO;
        //Use the seed to initialize a random number generator here.
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Computes the ground height at a specific x-coordinate by adding noise to the base height.
     *
     * @param x The x-coordinate at which to compute the ground height.
     * @return The computed ground height at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * NOISE_SCALING_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates a vertical column of terrain blocks for each x-coordinate within the specified range.
     * The blocks are generated based on Perlin noise-generated heights, and the columns extend downwards
     * to a specified terrain depth. This method ensures that the terrain is continuous and covers
     * the entire area between the minimum and maximum x-coordinates provided.
     *
     * @param minX The minimum x-coordinate (inclusive) where blocks will start being created.
     *             The starting x-coordinate is adjusted to ensure it is a multiple of Block.SIZE
     *             for grid alignment.
     * @param maxX The maximum x-coordinate (inclusive) where blocks will stop being created.
     * @return A list of Block objects representing the terrain within the specified range.
     *         Each x-coordinate in the range will have a column of blocks starting from the ground height
     *         determined by groundHeightAt and extending downwards to the terrain depth.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();

        // Ensure minX is a multiple of Block.SIZE
        minX = (minX / Block.SIZE) * Block.SIZE;

        // Iterate over each x-coordinate in the specified range
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            createBlocksColumn(x, blocks);
        }

        return blocks;
    }

    /*
     * Creates a vertical column of terrain blocks at a specific x-coordinate, extending downwards to the
     * terrain depth. The blocks are generated based on Perlin noise-generated heights, and the columns
     * ensure that the terrain is continuous and covers the entire area between the minimum and maximum
     * x-coordinates provided.
     * @param x The x-coordinate at which to create the column of blocks.
     * @return A list of Block objects representing the terrain within the specified range.
     */
    private void createBlocksColumn(int x, List<Block> blocks) {
        // Determine the starting height of the terrain at this x-coordinate
        float groundHeight = groundHeightAt(x);
        int startY = (int) Math.floor(groundHeight / Block.SIZE) * Block.SIZE;

        // Create a column of blocks at this x-coordinate, extending downwards to the terrain depth
        for (int y = 0; y < TERRAIN_DEPTH; y++) {
            // Calculate the position for this block in the column
            Vector2 position = new Vector2(x, startY + y * Block.SIZE);
            // Create the block with a color that may vary for a natural look
            Color curBlockColor = ColorSupplier.approximateColor(BASE_GROUND_COLOR);
            Block block = new Block(position, new RectangleRenderable(curBlockColor));
            block.setTag(GameTag.GROUND.getTag()); // Tag the block as part of the ground
            blocks.add(block);
        }
    }

}
