package ascii_art;

import ascii_art.exceptions.InvalidCommandException;

/**
 * Enumerates the commands available in the ASCII art application.
 * Each command is associated with a specific string representation that users can input.
 * @author Shir Rashkovits and Yoav Dolev
 */
enum Command {
    /**
     * The command to exit the application.
     */
    EXIT("exit"),
    /**
     * The command to change the set of characters used for ASCII art generation.
     */
    CHARS("chars"),
    /**
     * The command to change the resolution of the image.
     */
    ADD("add"),
    /**
     * The command to change the resolution of the image.
     */
    REMOVE("remove"),
    /**
     * The command to change the resolution of the image.
     */
    RES("res"),
    /**
     * The command to change the output method for the ASCII art.
     */
    IMAGE("image"),
    /**
     * The command to change the output method for the ASCII art.
     */
    OUTPUT("output"),
    /**
     * The command to change the output method for the ASCII art.
     */
    ASCIIART("asciiArt");

    private static final String GENERAL_INCORRECT_COMMAND_MESSAGE = "Did not execute due to incorrect " +
            "command.";
    /**
     * The minimum length of a command string with arguments.
     */
    static final int COMMAND_WITH_ARGS_MIN_LENGTH = 2;
    /**
     * The index of the image path argument in the command array.
     */
    static final int IMAGE_PATH_ARG_IDX = 1;
    /**
     * The index of the resolution change argument in the command array.
     */
    static final int RES_CHANGE_ARG_IDX = 1;
    /**
     * The index of the output type argument in the command array.
     */
    static final int OUTPUT_TYPE_ARG_IDX = 1;


    private final String commandString;

    /**
     * Constructs a Command enum instance with the specified string representation.
     *
     * @param commandString The string representation of the command.
     */
    Command(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Gets the string representation of this command.
     *
     * @return The string representation of the command.
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Converts a string input into a corresponding Command enum instance.
     * This method is case-sensitive.
     *
     * @param text The string representation of a command to be converted.
     * @return The corresponding Command enum instance, or null if the input does not match any command.
     */
    public static Command fromString(String text) throws InvalidCommandException {
        for (Command command : Command.values()) {
            if (command.getCommandString().equals(text)) {
                return command;
            }
        }
        throw new InvalidCommandException(GENERAL_INCORRECT_COMMAND_MESSAGE);
    }
}
