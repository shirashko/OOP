/**
 * A factory class responsible for creating Renderer instances based on the provided type.
 * Supports the creation of various renderers like ConsoleRenderer and VoidRenderer.
 *
 */
public class RendererFactory {

    /**
     * Default constructor for the RendererFactory class.
     */
    public RendererFactory() {
        // No additional initialization required.
    }

    /**
     * Creates and returns an instance of a Renderer based on the provided type.
     *
     * @param type The type of renderer to create. Supported types: "console", "none". needs to be lowercase.
     * @param size The size of the board for rendering, applicable only for some renderers like ConsoleRenderer.
     * @return Renderer instance corresponding to the given type or null if the type is not recognized.
     */
    public Renderer buildRenderer(String type, int size) {
        Renderer renderer;
        switch (type) {
            case "console":
                renderer = new ConsoleRenderer(size);
                break;
            case "none":
                renderer = new VoidRenderer();
                break;
            default:
                renderer = null;
        }
        return renderer;
    }
}
