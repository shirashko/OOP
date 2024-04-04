package ascii_art.exceptions;

import ascii_art.exceptions.ShellException;

/**
 * Thrown to indicate that a user's input command is either unrecognized or incorrectly formatted.
 * This checked exception promotes explicit handling of command validation errors,
 * ensuring that operations such as adjusting image resolution, specifying output type,
 * or manipulating the character set are executed with valid commands and parameters.
 * Extending {@link ShellException} makes this a checked exception, requiring explicit handling.
 */
public class InvalidCommandException extends ShellException {
    /**
     * Constructs an {@code InvalidCommandException} with a specific message detailing the nature
     * of the command error. The message elaborates on the cause of the exception, whether it be an
     * unrecognized command or a deviation from the expected command format.
     *
     * @param message The detail message about the command error. This message is intended to provide
     *                clear information to the user about what went wrong.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
