package ascii_art.exceptions;

import ascii_art.exceptions.ShellException;

/**
 * Thrown when an attempt to adjust an image's resolution is outside the allowed boundaries.
 * Extending {@link ShellException} makes this a checked exception, requiring explicit handling.
 * This ensures operations like setting the image resolution respect defined constraints,
 * such as minimum and maximum values.
 * The acceptable ranges from min value = max(1,ungWidth/imgHeight) up to the max value = image's
 * width (and height and width are assumed to be power of 2).
 */
public class ResolutionOutOfBoundsException extends ShellException {
    /**
     * Constructs a new ResolutionOutOfBoundsException with a specific message detailing
     * the violation of resolution boundaries.
     *
     * @param message Detailed message about the boundary violation, including attempted
     *                resolution and allowed range.
     */
    public ResolutionOutOfBoundsException(String message) {
        super(message);
    }
}
