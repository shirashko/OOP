package ascii_art;

import image_char_matching.SubImgCharMatcher;

/**
 * This class is responsible for running the algorithm that creates the ASCII art.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class AsciiArtAlgorithm {
    private final SubImgCharMatcher subImageMatcher; // Used for matching sub-images brightness to
    // characters.
    private final AsciiArtImageManager imageManager; // Used for getting the sub images brightness

    /**
     * Constructs an AsciiArtAlgorithm with a specific {@link SubImgCharMatcher} and
     * {@link AsciiArtImageManager}.
     *
     * @param subImageMatcher The SubImgCharMatcher to use for matching sub-images to characters.
     * @param imageManager The ImageManager to use for getting the image and resolution.
     */
    public AsciiArtAlgorithm(SubImgCharMatcher subImageMatcher, AsciiArtImageManager imageManager) {
        this.subImageMatcher = subImageMatcher;
        this.imageManager = imageManager;
    }

    /**
     * Runs the algorithm that creates the ASCII art.
     * Each sub image in the image is replaced by the ascii character that best matches its brightness.
     * @return the 2D array of chars that represents the ASCII art image.
     */
    public char[][] run() {
        double[][] subImagesNormalizedBrightness = imageManager.getSubImagesNormalizedBrightness();
        int height = subImagesNormalizedBrightness.length;
        int width = subImagesNormalizedBrightness[0].length;

        char[][] asciiArt = new char[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                asciiArt[row][col] = subImageMatcher.getCharByImageBrightness(
                                                                    subImagesNormalizedBrightness[row][col]);
            }
        }
        return asciiArt;
    }
}