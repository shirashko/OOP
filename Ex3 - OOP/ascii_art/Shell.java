package ascii_art;

import ascii_art.exceptions.*;

/**
 * The main driver class for the ASCII art generation application.
 * It handles user input to perform various operations such as adjusting image resolution,
 * changing the character set, and generating ASCII art from images.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class Shell {
    /**
     * The error message to be printed when a command is generally incorrect.
     */
    private static final String INITIALIZATION_ERROR_MESSAGE_PREFIX = "Failed to initialize the ASCII art " +
            "program due to an error: ";
    private static final String PROMPT = ">>> ";
    private final AsciiArtOutputManager outputManager;
    private final AsciiCharacterSetManager asciiCharacterSetManager;
    private final AsciiArtImageManager imageManager;
    private final AsciiArtAlgorithm asciiArtAlgorithm;

    /**
     * Initializes the Shell with default settings.
     * Loads the initial image and sets up the required managers.
     *
     * @throws ImageLoadingException If an error occurs during image loading.
     */
    public Shell() throws ImageLoadingException {
        outputManager = new AsciiArtOutputManager();
        asciiCharacterSetManager = new AsciiCharacterSetManager();
        imageManager = new AsciiArtImageManager();
        // Initialize the algorithm with the default parameters
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(asciiCharacterSetManager.getSubImgCharMatcher(),
                                                       imageManager);
    }


    /**
     * The main method of the program. Initializes the Shell and starts the main loop.
     * @param args The command line arguments. Not used in this program.
     */
    public static void main(String[] args) {
        try {
            Shell shell = new Shell();
            shell.run();
        } catch (ImageLoadingException e) {
            // Print an informative error message about the problem and exit the program with a non-zero
            // status code to indicate abnormal termination.
            System.err.println(INITIALIZATION_ERROR_MESSAGE_PREFIX + e.getMessage());
            System.exit(1);
        }
    }


    /**
     * Executes the main loop of the program, reading commands from the user and executing them.
     * Continues until the user enters the "exit" command.
     */
    public void run() {
        // Main loop, exit only when the user enters the "exit" command
        while (true) {
            try {
                String[] commandWithOptionalArgs = getUserCommandWithOptionalParams();
                Command command = Command.fromString(commandWithOptionalArgs[0]);
                switch (command) {
                    case EXIT:
                        return; // Exit the program
                    case CHARS:
                        handleCharsCommand();
                        break;
                    case ADD:
                        handleAddCommand(commandWithOptionalArgs);
                        break;
                    case REMOVE:
                        handleRemoveCommand(commandWithOptionalArgs);
                        break;
                    case RES:
                        handleResCommand(commandWithOptionalArgs);
                        break;
                    case IMAGE:
                        handleImageCommand(commandWithOptionalArgs);
                        break;
                    case OUTPUT:
                        handleOutputCommand(commandWithOptionalArgs);
                        break;
                    case ASCIIART:
                        handleAsciiArtCommand();
                        break;
                }
            } catch (ShellException se) { // In case of any exception, print the error message and continue
                printError(se.getMessage());
            }
        }
    }

    /*
     * Handles the 'image' command to set the current image to a new image loaded from the specified path.
     * Assumes the image path is supplied as the second word in the command.
     */
    private void handleImageCommand(String[] commandArray) throws InvalidCommandException,
            ImageLoadingException {
        imageManager.setImage(commandArray);
    }

    /*
     * Handles the 'res' command to adjust the resolution of the image.
     * Assumes the resolution change is supplied as the second word in the command.
     */
    private void handleResCommand(String[] commandArray) throws ResolutionOutOfBoundsException,
            InvalidCommandException {
        imageManager.adjustResolution(commandArray);
    }

    /*
     * Handles the 'remove' command to remove characters from the ASCII art character set.
     */
    private void handleRemoveCommand(String[] commandArray) throws InvalidCommandException {
        asciiCharacterSetManager.removeCharsByCommand(commandArray);
    }

    /*
     * Handles the 'add' command to add characters to the ASCII art character set.
     */
    private void handleAddCommand(String[] commandArray) throws InvalidCommandException {
        asciiCharacterSetManager.addCharsByCommand(commandArray);
    }

    // Utility methods

    /*
     * Prints a specified error message to the console.
     *
     * @param message The error message to be printed.
     */
    private void printError(String message) {
        System.out.println(message);
    }

    /*
     * Reads a command from the user and validates it.
     *
     * @return An array containing the command and its optional parameters.
     * @throws CommandFormatException If the command is not recognized or the format is incorrect.
     */
    private String[] getUserCommandWithOptionalParams() throws InvalidCommandException {
        System.out.print(PROMPT);
        String userInput = KeyboardInput.readLine();

        // return the command and its optional parameters
        return userInput.split(" ");
    }


    // Command handling methods

    /*
     * Handles the 'asciiart' command to generate and output the ASCII art.
     * For this command to be executed, the character set must not be empty.
     * @throws EmptyCharacterSetException If the character set is empty.
     */
    private void handleAsciiArtCommand() throws EmptyCharacterSetException {
        asciiCharacterSetManager.validateCharacterSet(); // Ensure the character set is not empty before
                                                         // generating ASCII art
        char[][] asciiArt = asciiArtAlgorithm.run();
        outputManager.outputAsciiArt(asciiArt);
    }

    /*
     * Handles the 'chars' command to print the current ASCII art character set in the console in
     * sorted order by ascii value.
     */
    private void handleCharsCommand() {
        asciiCharacterSetManager.printChars();
    }

    /*
     * Handles the 'output' command to change the output format of the ASCII art.
     *
     * @param commandArray An array containing the command and its parameters.
     */
    private void handleOutputCommand(String[] commandArray) throws InvalidCommandException {
        outputManager.setOutputMethod(commandArray);
    }
}
