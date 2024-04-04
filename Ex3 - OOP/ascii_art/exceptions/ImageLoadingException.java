package ascii_art.exceptions;

import ascii_art.exceptions.ShellException;

/**
 * Thrown to indicate an error occurred while loading an image.
 * This exception provides more specific context about image-related errors,
 * helping to distinguish them from other I/O errors that might need to be handled differently.
 * Extending {@link ShellException} makes this a checked exception, requiring explicit handling.
 */
public class ImageLoadingException extends ShellException {
    /**
     * Constructs an ImageLoadingException with a detailed error message.
     *
     * @param message Detailed message about the image loading error.
     */
    public ImageLoadingException(String message) {
        super(message);
    }
}