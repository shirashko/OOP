package pepse;

/**
 * Enum to define tags for different game objects within PEPSE.
 * This approach provides a centralized and type-safe way to manage object tags.
 *
 * @author Shir Rashkovits and Yoav Dolev
 */
public enum GameTag {
    /**
     * Tag for the sky.
     */
    SKY("sky"),

    /**
     * Tag for the ground.
     */
    GROUND("ground"),

    /**
     * Tag for the avatar.
     */
    AVATAR("Avatar"),

    /**
     * Tag for the sun.
     */
    NIGHT("night"),

    /**
     * Tag for the sun.
     */
    SUN("sun"),

    /**
     * Tag for the sun halo.
     */
    SUN_HALO("sunHalo"),

    /**
     * Tag for the trees.
     */
    FRUIT("fruit"),

    /**
     * Tag for the tree leaves.
     */
    LEAF("leaf");

    private final String tag;

    GameTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the string representation of the tag.
     *
     * @return The string tag associated with the enum constant.
     */
    public String getTag() {
        return tag;
    }
}