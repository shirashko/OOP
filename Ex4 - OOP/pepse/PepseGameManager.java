package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.Avatar;
import pepse.world.AvatarEnergyDisplay;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Fruit;
import pepse.world.trees.Tree;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;


/**
 * PepseGameManager (PEPSE: Precise Environmental Procedural Simulator Extraordinaire) is the central
 * controller for managing the initialization, rendering, and updating
 * of game objects within the PEPSE game environment. This class is responsible for setting up the game
 * world, including terrain, avatar, celestial objects (sun and its halo), night overlay, and an energy
 * display. Each game object is placed on its appropriate layer to ensure correct rendering order and
 * interaction dynamics between objects.
 * <p>
 * Responsibilities include:
 * - Initializing the game world and its components.
 * - Managing the game loop and invoking updates on game objects.
 * - Organizing game objects into layers for rendering and interaction purposes.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public class PepseGameManager extends GameManager {
    /**
     * The ratio of the ground's height to the window's height. This ratio is used to determine the ground's
     * height relative to the window size, while the rest of the window is covered by the sky.
     */
    public static final float SKY_TO_WINDOW_HEIGHT_RATIO = 2/3f;

    private static final Random random = new Random();

    // Seed for procedural generation elements, like terrain.
    private static final int SEED = random.nextInt(500);
    private static final int DAY_CYCLE_LENGTH = 30; // Duration of a complete day-night cycle in seconds.
    private static final int START_OF_THE_WINDOW_X = 0;
    private static final int START_OF_PLANTING = START_OF_THE_WINDOW_X + Block.SIZE;
    private static final int AVATAR_STARTING_POSITION_X = START_OF_THE_WINDOW_X;
    private Vector2 windowDimensions;
    private Terrain terrain;
    private Avatar avatar;


    /**
     * Entry point for the game application.
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game by setting up the game window, loading resources, and creating initial game
     * objects. This method is called once at the start of the game.
     *
     * @param imageReader Provides access to image resources.
     * @param soundReader Provides access to sound resources.
     * @param inputListener Listens for user input events.
     * @param windowController Manages the game window and view.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();

        initSky(); // Set up the sky background.
        this.terrain = initTerrain(); // Generate the terrain.
        initDayNight(); // Set up the day-night cycle.
        this.avatar = initAvatar(imageReader, inputListener); // Instantiate the player's avatar.
        initFlora(); // Generate flora elements.
//        if (avatarRelocationNedded) {
//            avatar.setAvatarPosition(Vector2.of(AVATAR_STARTING_POSITION_X,
//                    terrain.groundHeightAt(AVATAR_STARTING_POSITION_X) + firstTreeHeight));
//        }
        manageLayersCollisions(); // Set up layer interactions.
    }

    /*
     * Initializes the flora in the game world, including trees, leaves, and fruits.
     */
    private void initFlora() {
        Flora flora = new Flora(terrain::groundHeightAt);
        List<Tree> trees = flora.createInRange(START_OF_PLANTING , (int) Math.floor(windowDimensions.x()));

        // Iterate through trees to add components to the game and set behaviors.
        trees.forEach(tree -> {
            // Add tree components to the game.
            addTreeComponentsToGame(tree);

            // Set onAvatarJump callbacks for tree components.
            setTreeComponentsJumpObservers(tree);

            // Set collision behaviors for fruits.
            setFruitsCollisionBehavior(tree);
        });
    }

    /*
     * Adds tree components to the game world, including leaves, trunk, and fruits.
     */
    private void addTreeComponentsToGame(Tree tree) {
        tree.getLeaves().forEach(leaf -> gameObjects().addGameObject(leaf, GameLayer.LEAVES.getLayerID()));
        gameObjects().addGameObject(tree.getTrunk(), GameLayer.TREES_TRUNK.getLayerID());
        tree.getFruits().forEach(fruit -> gameObjects().addGameObject(fruit, GameLayer.FRUITS.getLayerID()));
    }

    /*
     * Sets the jump observers for tree components, including leaves, trunk, and fruits.
     */
    private void setTreeComponentsJumpObservers(Tree tree) {
        tree.getLeaves().forEach(leaf -> avatar.addJumpObserver(leaf::rotate));
        tree.getFruits().forEach(fruit -> avatar.addJumpObserver(fruit::changeColor));
        avatar.addJumpObserver(tree.getTrunk()::changeColor);
    }

    /*
     * Sets the collision behaviors for fruits.
     */
    private void setFruitsCollisionBehavior(Tree tree) {
        tree.getFruits().forEach(fruit -> fruit.setOnCollisionEnter(getOnCollisionStrategy(fruit)));
    }


    /*
     * Returns a BiConsumer that defines the action to be taken when the avatar collides with a fruit.
     */
    private BiConsumer<GameObject, Collision> getOnCollisionStrategy(Fruit fruit) {
        // Define the combined action for fruit collision
        return (other, collision) -> {
            if (other.getTag().equals(GameTag.AVATAR.getTag())) {
                avatar.addEnergy(Fruit.ENERGY_GAIN_PER_FRUIT);
                gameObjects().removeGameObject(fruit, GameLayer.FRUITS.getLayerID());
                // Make it visible again
                new ScheduledTask(avatar, DAY_CYCLE_LENGTH, false,
                                  () -> gameObjects().addGameObject(fruit, GameLayer.FRUITS.getLayerID()));
                // Increase avatar's energy
            }
        };
    }

    /*
     * Manages the interactions between different layers in the game world.
     */
    private void manageLayersCollisions() {
        setLayersIfShouldCollide(GameLayer.AVATAR, GameLayer.FRUITS, true);
        setLayersIfShouldCollide(GameLayer.AVATAR, GameLayer.TREES_TRUNK, true);
        setLayersIfShouldCollide(GameLayer.LEAVES, GameLayer.LEAVES, false);
        setLayersIfShouldCollide(GameLayer.AVATAR, GameLayer.LEAVES, false);
    }

    /*
     * Sets whether two layers should collide or not.
     */
    private void setLayersIfShouldCollide(GameLayer avatar, GameLayer treesTrunk, boolean shouldCollide) {
        this.gameObjects().layers().shouldLayersCollide(avatar.getLayerID(),
                treesTrunk.getLayerID(), shouldCollide);
    }

    /*
     * Creates and configures the player's avatar along with its energy display UI component.
     * The avatar is positioned and its energy display is initialized and added to the UI layer.
     *
     * @param imageReader To load avatar image assets.
     * @param inputListener To listen for player inputs.
     */
    private Avatar initAvatar(ImageReader imageReader, UserInputListener inputListener) {
        AvatarEnergyDisplay energyDisplay = initAvatarEnergyDisplay();
        Vector2 avatarInitPos = Vector2.of(AVATAR_STARTING_POSITION_X,
                                       terrain.groundHeightAt(AVATAR_STARTING_POSITION_X));
        Avatar avatar = new Avatar(avatarInitPos, inputListener, imageReader);
        avatar.addEnergyObserver(energyDisplay::updateEnergyDisplay);
        gameObjects().addGameObject(avatar, GameLayer.AVATAR.getLayerID());
        return avatar;
    }

    /*
     * Creates and initializes the energy display UI component for the player's avatar.
     */
    private AvatarEnergyDisplay initAvatarEnergyDisplay() {
        AvatarEnergyDisplay energyDisplay = new AvatarEnergyDisplay();
        gameObjects().addGameObject(energyDisplay, GameLayer.ENERGY_DISPLAY.getLayerID());
        return energyDisplay;
    }

    /*
     * Creates the day-night cycle by adding the sun, its halo, and the night overlay to the game world.
     */
    private void initDayNight() {
        initNightEffect(); // Create the night overlay effect.
        initSunWithHalo(); // Place the sun and its halo in the sky.
    }

    /*
     * Creates the sun and its halo, placing them in the sky layer.
     */
    private void initSunWithHalo() {
        GameObject sun = Sun.create(windowDimensions, DAY_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, GameLayer.SUN.getLayerID());

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, GameLayer.SUN_HALO.getLayerID());
    }


    /*
     * Creates the night overlay effect, placing it in the night layer.
     */
    private void initNightEffect() {
        GameObject night = Night.create(windowDimensions, DAY_CYCLE_LENGTH);
        gameObjects().addGameObject(night, GameLayer.NIGHT.getLayerID());
    }

    /*
     * Creates the game's terrain and adds it to the ground layer.
     */
    private Terrain initTerrain() {
        Terrain terrain = new Terrain(windowDimensions, SEED);
        List<Block> terrainBlocks = terrain.createInRange(START_OF_THE_WINDOW_X,
                                                          (int) Math.ceil(windowDimensions.x()));
        // Add ground blocks to the game.
        for (GameObject block : terrainBlocks) {
            gameObjects().addGameObject(block, GameLayer.GROUND.getLayerID());
        }
        return terrain;
    }

    /*
     * Creates the sky background and places it in the sky layer.
     */
    private void initSky() {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, GameLayer.SKY.getLayerID());
    }
}
