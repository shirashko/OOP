import java.util.Locale;

/**
 * A factory class for creating instances of different types of players in the Tic-Tac-Toe game.
 * This class provides a method to create a player object based on a specified type.
 * Supported player types include "human", "clever", "whatever", and "genius".
 *
 * @author Shir Rashkovits
 * @see Player
 * @see HumanPlayer
 * @see CleverPlayer
 * @see WhateverPlayer
 * @see GeniusPlayer
 */
public class PlayerFactory {

    /**
     * Constructs a PlayerFactory instance.
     */
    public PlayerFactory() {
        // no initialization is required
    }

    /**
     * Creates and returns a Player object based on the specified player type.
     * The method supports various player types, each represented by a different class.
     *
     * @param type The type of player to create. needs to be lowercase, and This should be one of the following strings:
     *             "human", "clever", "whatever", "genius". If an unrecognized type is provided,
     *             the method returns null.
     * @return A Player object of the specified type, or null if the type is not recognized.
     */
    public Player buildPlayer(String type) {
        Player player;
        switch (type) {
            case "human":
                player = new HumanPlayer();
                break;
            case "clever":
                player = new CleverPlayer();
                break;
            case "whatever":
                player = new WhateverPlayer();
                break;
            case "genius":
                player = new GeniusPlayer();
                break;
            default:
                player = null;
        }
        return player;
    }
}
