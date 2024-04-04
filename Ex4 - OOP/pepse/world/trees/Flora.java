package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Represents the collective flora within the game world, specifically focusing on tree generation.
 * This class is responsible for generating trees across a specified horizontal range in the game world.
 * Trees are generated based on a given probability and positioned according to the terrain's height,
 * which is determined by a callback function.
 */
public class Flora {
    // Constants

    // Probability of planting a tree in each column.
    private static final float PLANTING_PROBABILITY = 0.1f;
    // Callback to determine the ground height at a given x-coordinate.
    private final Function<Float, Float> groundHeightAtX;

    // The width of the tree trunks, standardized to match a block's size.
    private static final int TREE_TRUNK_WIDTH = Block.SIZE;

    // Fields
    private static final Random random = new Random();

    /**
     * Constructs a Flora instance with a specific mechanism to determine ground height.
     *
     * @param groundHeightAtX A callback function that accepts an x-coordinate (as a Float) and returns
     *                        the corresponding ground height (as a Float) at that coordinate. This allows
     *                        the flora to adapt to varying terrain heights within the game world.
     */
    public Flora(Function<Float, Float> groundHeightAtX) {
        this.groundHeightAtX = groundHeightAtX;
    }

    /**
     * Generates and positions trees within a specified horizontal range in the game world.
     * The actual positions and number of trees depend on a defined planting probability.
     *
     * @param minX The minimum x-coordinate (inclusive) marking the start of the range where trees can be
     *             generated.
     * @param maxX The maximum x-coordinate (inclusive) marking the end of the range where trees can be
     *             generated.
     * @return A list containing all the Tree objects created within the specified range.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        List<Tree> trees = new ArrayList<>();
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            if (random.nextFloat() < PLANTING_PROBABILITY) {
                float y = groundHeightAtX.apply((float) x); // Determine ground height at x-coordinate.
                Tree tree = new Tree(Vector2.of(x, y), TREE_TRUNK_WIDTH); // Create a new tree.
                trees.add(tree); // Add the newly created tree to the list.
            }
        }
        return trees;
    }
}
