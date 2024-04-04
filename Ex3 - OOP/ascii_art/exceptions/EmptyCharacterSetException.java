package ascii_art.exceptions;

/**
 * Exception thrown when an attempt is made to generate ASCII art with an empty character set.
 * Extending {@link ShellException} makes this a checked exception, requiring explicit handling.
 * This ensures operations like generating ASCII art respect the requirement of a non-empty character set.
 * The character set is expected to be populated with characters before generating ASCII art.
 */
public class EmptyCharacterSetException extends ShellException {
    /**
     * Constructs an EmptyCharacterSetException with a detailed error message.
     * @param message Detailed message about the error.
     */
    public EmptyCharacterSetException(String message) {
        super(message);
    }
}
