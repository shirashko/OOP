package ascii_art;

import ascii_art.exceptions.ImageLoadingException;
import ascii_art.exceptions.InvalidCommandException;
import ascii_art.exceptions.ResolutionOutOfBoundsException;
import image.Image;
import image.ImageProcessor;

import java.io.IOException;

/**
 * Manages the resolution and processing of an image for ASCII art conversion.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class AsciiArtImageManager {
    // Default values
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final String RESOLUTION_DOUBLE_COMMAND = "up";
    private static final String RESOLUTION_HALVE_COMMAND = "down";

    // Error messages
    private static final String IMAGE_UPLOAD_ERROR = "Did not execute due to problem with image file.";
    private static final String OUT_OF_RESOLUTION_BOUNDARIES = "Did not change resolution" +
                                                                   "due to exceeding boundaries.";
    private static final String RESOLUTION_FORMAT_ERROR = "Did not change resolution due to incorrect " +
                                                                                                "format.";
    private static final String IMAGE_FORMAT_ERROR = "Did not upload image due to incorrect format.";
    private static final int DEFAULT_RESOLUTION = 128;

    // Messages for user feedback
    private static final String RESOLUTION_MESSAGE = "Resolution set to %d.";


    // Fields for image management
    private Image image; // holds the padded image
    private int resolution; // Current resolution, assumed to be a power of 2
    private double[][] subImagesNormalizedBrightnesses;
    private boolean updateNeeded; // Flag to track if recalculation of brightnesses is needed


    /**
     * Constructs an ImageManager with a default image and resolution.
     *
     * @throws ImageLoadingException If an error occurs during image loading.
     */
    public AsciiArtImageManager() throws ImageLoadingException {
        setImage(DEFAULT_IMAGE_PATH);
        this.resolution = DEFAULT_RESOLUTION;
        this.updateNeeded = true;
    }

    /**
     * Adjusts the resolution of the image. The resolution can be doubled ("up") or halved ("down").
     *
     * @param commandArray The command string split into an array of words, which should include the
     *                     resolution change argument.
     * @throws ResolutionOutOfBoundsException If the requested adjustment would exceed resolution boundaries.
     * @throws InvalidCommandException If the resolution change argument is not provided or is not
     * recognized.
     */
    public void adjustResolution(String[] commandArray) throws ResolutionOutOfBoundsException,
            InvalidCommandException {
        if (commandArray.length < Command.COMMAND_WITH_ARGS_MIN_LENGTH) {
            throw new InvalidCommandException(RESOLUTION_FORMAT_ERROR);
        }
        String resChange = commandArray[Command.RES_CHANGE_ARG_IDX];
        switch (resChange) {
            case RESOLUTION_DOUBLE_COMMAND:
                if (resolution == image.getWidth()) {
                    throw new ResolutionOutOfBoundsException(OUT_OF_RESOLUTION_BOUNDARIES);
                }
                resolution *= 2;
                break;
            case RESOLUTION_HALVE_COMMAND:
                if (resolution == 1) {
                    throw new ResolutionOutOfBoundsException(OUT_OF_RESOLUTION_BOUNDARIES);
                }
                resolution = Math.max(resolution / 2, 1);
                break;
            default:
                throw new InvalidCommandException(RESOLUTION_FORMAT_ERROR);
        }

        updateNeeded = true;
        informSuccessfulResolutionChange();
    }

    private void informSuccessfulResolutionChange() {
        System.out.printf(RESOLUTION_MESSAGE, resolution);
        System.out.println();
    }

    /**
     * Sets the current image to a new image loaded from the specified path.
     * @param commandArray The command string split into an array of words, which should include the image
     *                     path argument.
     * @throws ImageLoadingException If an error occurs during image loading.
     * @throws InvalidCommandException If the image path argument is not provided.
     */
    public void setImage(String[] commandArray) throws InvalidCommandException, ImageLoadingException {
        if (commandArray.length < Command.COMMAND_WITH_ARGS_MIN_LENGTH) {
            throw new InvalidCommandException(IMAGE_FORMAT_ERROR);
        }

        String imagePath = commandArray[Command.IMAGE_PATH_ARG_IDX];
        setImage(imagePath);
    }


    /**
     * Calculates or retrieves normalized brightness values for sub-images of the current image, based on
     * resolution. Sub-images are created by dividing the current image according to the resolution. Each
     * sub-image's average
     * grayscale brightness is normalized (0 to 1). Brightness is recalculated only if the image or
     * resolution has changed since the last call, enhancing efficiency. This supports ASCII art generation
     * by mapping image brightness to characters.
     *
     * @return A 2D array of doubles with normalized brightness values for each sub-image, where 0 represents
     *         black and 1 represents white. The array dimensions correspond to the sub-image positions
     *         (rows and columns) in the original image.
     */
    public double[][] getSubImagesNormalizedBrightness() {
        if (!updateNeeded) {
            return subImagesNormalizedBrightnesses;
        }

        Image[][] subImages = ImageProcessor.divideIntoSubImages(image, resolution);
        double[][] brightness = new double[subImages.length][subImages[0].length];

        for (int row = 0; row < subImages.length; row++) {
            for (int col = 0; col < subImages[0].length; col++) {
                brightness[row][col] = ImageProcessor.calculateImageNormalizedGrayscaleAverage(
                        subImages[row][col]);
            }
        }

        this.subImagesNormalizedBrightnesses = brightness;
        this.updateNeeded = false;

        return brightness;
    }

    private void setImage(String imagePath) throws ImageLoadingException {
        try {
            image = new Image(imagePath);
        } catch (IOException e) {
            throw new ImageLoadingException(IMAGE_UPLOAD_ERROR);
        }

        image = ImageProcessor.padImage(image);
        updateNeeded = true;
    }

}
