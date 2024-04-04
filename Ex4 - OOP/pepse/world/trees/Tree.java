package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a tree object in the game, including its trunk and foliage. The class is responsible for
 * initializing the tree by creating and positioning both its trunk and leaves based on the given
 * bottom left corner of the tree.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Tree {
    private static final int LEAF_SIZE = Block.SIZE; // Assuming square leaves for consistency.
    private static final Vector2 LEAF_DIMENSIONS = Vector2.of(LEAF_SIZE, LEAF_SIZE);
    private static final float LEAF_DENSITY_PROBABILITY = 7; // Probability for leaf density.
    private static final int TREE_TOP_SIZE = 5; // Defining the square tree top size.
    private static final int FOLIAGE_AREA_SIZE = Block.SIZE * 5; // The total area size of the foliage.

    // Vertical adjustment for a small overlap with the trunk.
    private static final int FOLIAGE_VERTICAL_ADJUSTMENT = 40;
    private static final int PROBABILITY_RANGE_TOP = 10;
    private static final int FRUIT_DENSITY_PROBABILITY = 1;
    private static final float FRUIT_SIZE = LEAF_SIZE * 0.75f;
    private static final Vector2 FRUIT_DIMENSIONS = Vector2.of(FRUIT_SIZE, FRUIT_SIZE);

    private final Trunk trunk; // The trunk object of the tree.
    private final List<Leaf> leaves; // The list of leaf objects representing the tree's foliage.
    private final List<Fruit> fruits; // The list of fruit objects representing the tree's fruits.
    private static final Random random = new Random();

    /**
     * Constructs a Tree object at a specified location.
     *
     * @param treeBottomLeftCorner The bottom left corner of the tree's base position, used as a reference
     *                             to position the trunk and foliage.
     * @param trunkWidth           The width of the tree's trunk (uniform for all trees).
     */
    public Tree(Vector2 treeBottomLeftCorner, int trunkWidth) {
        this.trunk = new Trunk(treeBottomLeftCorner, trunkWidth); // Creates the trunk of the tree.

        Vector2 foliageStartCorner = getFoliageStartCorner(treeBottomLeftCorner, trunkWidth);

        // Initializes the foliage and fruits based on the calculated start corner.
        this.leaves = createFoliage(foliageStartCorner);
        this.fruits = createFruits(foliageStartCorner);
    }

    /*
     * Calculates the start corner of the foliage based on the tree's bottom left corner and trunk width.
     */
    private Vector2 getFoliageStartCorner(Vector2 treeBottomLeftCorner, int trunkWidth) {
        // Calculate the horizontal center of the trunk to align the foliage.
        float trunkCenterX = treeBottomLeftCorner.x() + trunkWidth / 2f;
        // Determine the foliage start position to center it above the trunk.
        float foliageCenterXOffset = FOLIAGE_AREA_SIZE / 2f;
        float foliageStartX = trunkCenterX - foliageCenterXOffset;

        // Calculate the vertical start position of the foliage, ensuring a small overlap with the trunk for
        // visual continuity.
        float trunkTopY = treeBottomLeftCorner.y() - trunk.getDimensions().y();
        float foliageVerticalAdjustmentOffset = FOLIAGE_AREA_SIZE - FOLIAGE_VERTICAL_ADJUSTMENT;
        float foliageStartY = trunkTopY - foliageVerticalAdjustmentOffset;

        return Vector2.of(foliageStartX, foliageStartY);
    }

    /*
     * Creates the foliage of the tree.
     */
    private List<Fruit> createFruits(Vector2 topLeftCorner) {
        List<Fruit> fruits = new ArrayList<>();
        for (int x = 0; x < TREE_TOP_SIZE; x++) {
            for (int y = 0; y < TREE_TOP_SIZE; y++) {
                if (random.nextInt(PROBABILITY_RANGE_TOP) < FRUIT_DENSITY_PROBABILITY) {
                    Vector2 leafPosition = new Vector2(topLeftCorner.x() + x * LEAF_SIZE,
                            topLeftCorner.y() + y * LEAF_SIZE);
                    Fruit leaf = new Fruit(leafPosition, FRUIT_DIMENSIONS);
                    fruits.add(leaf);
                }
            }
        }
        return fruits;
    }

    /*
     * Creates the foliage of the tree.
     */
    private static List<Leaf> createFoliage(Vector2 topLeftCorner) {
        List<Leaf> leaves = new ArrayList<>();
        for (int x = 0; x < TREE_TOP_SIZE; x++) {
            for (int y = 0; y < TREE_TOP_SIZE; y++) {
                if (random.nextInt(PROBABILITY_RANGE_TOP) < LEAF_DENSITY_PROBABILITY) {
                    Vector2 leafPosition = new Vector2(topLeftCorner.x() + x * LEAF_SIZE,
                            topLeftCorner.y() + y * LEAF_SIZE);
                    Leaf leaf = new Leaf(leafPosition, LEAF_DIMENSIONS);
                    leaves.add(leaf);
                }
            }
        }
        return leaves;
    }


    /**
     * Gets the trunk GameObject of the tree.
     *
     * @return The trunk GameObject.
     */
    public Trunk getTrunk() {
        return trunk;
    }

    /**
     * Gets the list of leaves (foliage) GameObjects of the tree.
     *
     * @return The list of leaves GameObjects.
     */
    public List<Leaf> getLeaves() {
        return leaves;
    }

    /**
     * Gets the list of fruit GameObjects of the tree.
     * @return The list of fruit GameObjects.
     */
    public List<Fruit> getFruits() {
        return fruits;
    }
}
