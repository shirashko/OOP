package ascii_art;

import ascii_art.exceptions.InvalidCommandException;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the output of ASCII art to different destinations (e.g., console, HTML file).
 * This class allows switching between output methods dynamically at runtime.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class AsciiArtOutputManager {
    private static final String FONT_TYPE = "Courier New";
    private static final String OUTPUT_FILE_NAME = "out.html";
    private static final String CONSOLE_OUTPUT_OPTION = "console";
    private static final String DEFAULT_OUTPUT_OPTION = CONSOLE_OUTPUT_OPTION;
    private static final String HTML_OUTPUT_OPTION = "html";
    private static final String OUTPUT_FORMAT_ERROR = "Did not change output method due to incorrect " +
                                                                                                "format.";

    private final Map<String, AsciiOutput> outputs = new HashMap<>(); // Available output methods
    private AsciiOutput currentOutput; // The currently selected output method

    /**
     * Constructs an AsciiArtOutputManager and initializes available output methods.
     */
    public AsciiArtOutputManager() {
        // Initialize available output methods
        outputs.put(CONSOLE_OUTPUT_OPTION, new ConsoleAsciiOutput());
        outputs.put(HTML_OUTPUT_OPTION, new HtmlAsciiOutput(OUTPUT_FILE_NAME, FONT_TYPE));

        // Set default output method
        this.currentOutput = outputs.get(DEFAULT_OUTPUT_OPTION);
    }

    /**
     * Changes the current output method based on the user's choice.
     *
     * @param commandArray The command string split into an array of words, which should include the output
     *                     type argument.
     * @throws InvalidCommandException If the output type argument is not provided or is not recognized.
     */
    public void setOutputMethod(String[] commandArray) throws InvalidCommandException {
        if (commandArray.length < Command.COMMAND_WITH_ARGS_MIN_LENGTH) {
            throw new InvalidCommandException(OUTPUT_FORMAT_ERROR);
        }
        AsciiOutput selectedOutput = outputs.get(commandArray[Command.OUTPUT_TYPE_ARG_IDX]);
        if (selectedOutput != null) {
            this.currentOutput = selectedOutput;
        } else {
            throw new InvalidCommandException(OUTPUT_FORMAT_ERROR);
        }
    }

    /**
     * Outputs the given ASCII art using the current output method.
     *
     * @param asciiArt The 2D array of characters representing the ASCII art to be output.
     */
    public void outputAsciiArt(char[][] asciiArt) {
        currentOutput.out(asciiArt);
    }
}
