package ascii_art.exceptions;

/**
 * Base class for exceptions related to ASCII art generation by the shell, capturing common issues
 * encountered during the execution of the ASCII art application. Being a checked exception,
 * it enforces explicit handling, thereby encouraging robust error management practices.
 * This class is intended to be extended by more specific exceptions to capture different types of
 * errors that can occur during ASCII art generation.
 * <p>
 * This class enables to handle shell exceptions together, while allowing specific exceptions to be
 * caught separately.
 *
 */
abstract public class ShellException extends Exception {
    /**
     * Constructs a ShellException with a detailed error message.
     *
     * @param message Detailed message about the error.
     */
    public ShellException(String message) {
        super(message);
    }
}
