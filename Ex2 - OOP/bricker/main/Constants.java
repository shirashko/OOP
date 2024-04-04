package bricker.main;

import danogl.collisions.Layer;

/**
 * Contains global constants used across the Bricker game, aimed at centralizing configuration for ease of
 * access and ensuring consistency in game mechanics and visuals. This class organizes constants into enums
 * for resource paths, object tags, and rendering layers, promoting a type-safe and organized approach to
 * managing game constants. This structure enhances the code's readability and maintainability, making it
 * straightforward to update and extend the game's core settings. Constants defined here are primarily used
 * by game managers, which in turn, provide these constants to the game objects that require them.
 */
class Constants {

    /**
     * A multiplier used for calculating the center position of objects within the game window.
     * This aids in positioning objects in the game window.
     */
    static final float CENTER_MULTIPLIER = 0.5f;

    /**
     * Defines the thickness of the game's border walls. This value is beneficial for rendering the game's
     * objects within the game window, managing collision detection with the game's boundaries.
     */
    static final int BORDER_THICKNESS = 5;

    /**
     * Enumerates the file paths to various game resources, such as images and sounds.
     * This organization method centralizes the management of resource paths, facilitating
     * easier updates and maintenance.
     */
    enum ResourcePath {

        /** File path for the brick image used in the game. */
        BRICK_IMAGE("assets/brick.png"),

        /** File path for the sound played when the ball hits an object. */
        BALL_HIT_SOUND("assets/blop_cut_silenced.wav"),

        /** File path for the main paddle image used in the game. */
        PADDLE_IMAGE("assets/paddle.png"),

        /** File path for the main ball image used in the game. */
        BALL_IMAGE("assets/ball.png"),

        /** File path for the puck ball image used in the game. */
        PUCK_BALL_IMAGE("assets/mockBall.png"),

        /** File path for the background image used in the game. */
        BACKGROUND_IMAGE("assets/DARK_BG2_small.jpeg"),

        /** File path for the heart image used to represent player lives icons. */
        LIFE_IMAGE("assets/heart.png");

        private final String path;

        /**
         * Constructs a new ResourcePath with the specified file path.
         *
         * @param path The file path as a string.
         */
        ResourcePath(String path) {
            this.path = path;
        }

        /**
         * Retrieves the file path associated with this resource.
         *
         * @return The file path as a string.
         */
        String getPath() {
            return path;
        }
    }

    /**
     * Enumerates the tags used to identify and categorize game objects. Tags facilitate effective
     * collision management and gameplay logic, providing a type-safe way of distinguishing between
     * different object types.
     */
    enum GameObjectsTags {

        /** Tag for the game's border walls. */
        BORDER("border"),

        /** Tag for the main ball object. Essential for collision logic and game mechanics. */
        MAIN_BALL("mainBall"),

        /** Tag for the main paddle controlled by the player. */
        MAIN_PADDLE("mainPaddle");

        private final String value;

        /**
         * Constructs a new GameObjectsTags with the specified tag value.
         * @param value The tag value as a string.
         */

        GameObjectsTags(String value) {
            this.value = value;
        }

        /**
         * Retrieves the tag value used for identifying the object.
         *
         * @return The tag as a string.
         */
        String getValue() {
            return value;
        }
    }

    /**
     * Enumerates the layers within the game to manage the rendering order and collision detection layers of
     * game objects. Using layers ensures that objects are rendered and interact in a controlled manner,
     * enhancing the game's visual and interactive structure.
     */
    enum GameLayer {
        /** Layer for the bricks in the game. */
        BRICKS(Layer.STATIC_OBJECTS),

        /** Layer for the main paddle and any extra paddles in the game. */
        PADDLES(Layer.DEFAULT),

        /** Layer for the main ball and any other balls (pucks) in the game. */
        BALLS(Layer.DEFAULT),

        /** Layer for the camera resetter */
        FOCUS_CAMERA_CHANGER(Layer.BACKGROUND),

        /** Layer for the game's background. */
        BACKGROUND(Layer.BACKGROUND),

        /** Layer for the game's border walls. */
        BORDER(Layer.DEFAULT),

        /** Layer for the falling hearts representing player lives. */
        FALLING_LIFE(Layer.DEFAULT),

        /** Layer for the player's life presentation (graphic & numeric). */
        LIFE_PRESENTATION(Layer.UI);

        private final int id;

        /**
         * Constructs a new GameLayer with the specified integer ID.
         * @param id The integer ID of the layer.
         */
        GameLayer(int id) {
            this.id = id;
        }

        /**
         * Retrieves the integer ID of the layer, which is used for specifying the rendering and collision
         * detection order of game objects.
         *
         * @return The layer ID as an integer.
         */
        int getId() {
            return id;
        }
    }

    /*
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {
    }
}