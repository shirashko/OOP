public class RendererFactory {
    Renderer buildRenderer(String rendererArg) {
        switch (rendererArg){
            case "console":
                return new ConsoleRenderer();
            case "void":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
