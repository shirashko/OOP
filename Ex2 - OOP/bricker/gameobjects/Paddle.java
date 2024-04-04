package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a paddle in the game. This class extends GameObject and is responsible
 * for handling the paddle's movement based on user input.
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300; // The speed at which the paddle moves
    private final UserInputListener inputListener;   // Listener for user input
    private final Vector2 windowDimensions; // The dimensions of the window in which the paddle is rendered

    /**
     * Constructor for creating a new Paddle object.
     *
     * @param topLeftCorner The top left corner of the object's bounding box.
     * @param dimensions The dimensions of the object's bounding box.
     * @param renderable The object's renderable, used for rendering the paddle on the screen.
     * @param inputListener A UserInputListener for handling keyboard input.
     * @param windowDimensions The dimensions of the window in which the paddle is rendered.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, String paddleTag) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.setTag(paddleTag);
    }

    /**
     * Updates the state of the paddle. This method is called once per frame.
     * It checks for user input and moves the paddle accordingly. It also prevents the paddle from moving
     * outside the window. In such a case, the paddle will not move.
     *
     * @param deltaTime The time elapsed since the last update call, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        Vector2 moveDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && getTopLeftCorner().x() > 0){
            moveDir = moveDir.add(Vector2.LEFT);
        }
        Vector2 topRightCorner = getTopLeftCorner().add(new Vector2(getDimensions().x(), 0));
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && topRightCorner.x() < windowDimensions.x()) {
            moveDir = moveDir.add(Vector2.RIGHT);
        }

        setVelocity(moveDir.mult(MOVEMENT_SPEED));
    }

}
