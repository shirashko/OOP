package image;

import java.awt.*;

/**
 * This class is responsible for processing images, such as padding and dividing into sub-images.
 * @author Shir Rashkovits and Yoav Dolev
 */
public class ImageProcessor {
    private static final int MAX_INTENSITY = 255;
    private static final double RED_WEIGHT_TO_GREYSCALE = 0.2126;
    private static final double GREEN_WEIGHT_TO_GREYSCALE = 0.7152;
    private static final double BLUE_WEIGHT_TO_GREYSCALE = 0.0722;

    /**
     * Pads the image with white pixels to make its dimensions the closest power of two.
     * If the width and height of the image are already powers of two, no padding is applied.
     * Assumes the width and height of the image are even, padding symmetrically in both
     * sides to achieve the closest power of two dimensions.
     */
    public static Image padImage(Image image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // Calculate the new dimensions that are the closest power of two
        int newWidth = getNextPowerOfTwo(imageWidth);
        int newHeight = getNextPowerOfTwo(imageHeight);

        // Calculate the padding required on each side
        int paddingX = (newWidth - imageWidth) / 2;
        int paddingY = (newHeight - imageHeight) / 2;

        // Create a new pixel array with the new dimensions
        Color[][] newPixelArray = new Color[newHeight][newWidth];

        // Copy the original pixel array into the new pixel array with padding
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                if (isPadPixel(row, paddingY, col, paddingX, imageWidth, imageHeight)) {
                    newPixelArray[row][col] = Color.WHITE; // Padding with white pixels
                } else {
                    newPixelArray[row][col] = image.getPixel(row - paddingY, col - paddingX);
                }
            }
        }

        return new Image(newPixelArray, newWidth, newHeight);
    }

    /**
     * Calculates the average brightness of the image, normalized to the range [0, 1].
     * @param image the image to calculate the average brightness of
     * @return the average brightness of the image, normalized to the range [0, 1]
     */
    public static double calculateImageNormalizedGrayscaleAverage(Image image) {
        double sum = 0;
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                sum += getGreyscaleIntensity(image.getPixel(row, col));
            }
        }
        double averageBrightness = sum / (image.getHeight() * image.getWidth());
        return averageBrightness / MAX_INTENSITY;
    }


    /**
     * Divides the original image into square sub-images based on the specified resolution. The resolution
     * parameter determines how many square sub-images are created per row in the resulting division. For
     * instance, a resolution of 128 means the image will be divided into 128 square sub-images along each
     * row, resulting in each line of the ASCII art representation containing 128 characters. This method
     * facilitates the conversion of image pixels into ASCII characters by segmenting the image into
     * manageable, square sections that can each be represented by a single character based on average
     * brightness or other criteria.
     *
     * @param image The source image to be divided into sub-images. This image is assumed to be with
     *              dimensions that are powers of two.
     * @param resolution The number of sub-images to be created along one row, defining the size of each
     *                   sub-image.
     * @return A 2D array of Image objects, each representing a square sub-image of the original image.
     */
    public static Image[][] divideIntoSubImages(Image image, int resolution) {
        int subImageSize = image.getWidth() / resolution;
        int numRows = image.getHeight() / subImageSize;
        Image[][] subImages = new Image[numRows][resolution];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < resolution; col++) {
                Color[][] subImagePixels = new Color[subImageSize][subImageSize];
                for (int subRow = 0; subRow < subImageSize; subRow++) {
                    for (int subCol = 0; subCol < subImageSize; subCol++) {
                        int originalRow = row * subImageSize + subRow;
                        int originalCol = col * subImageSize + subCol;
                        subImagePixels[subRow][subCol] = image.getPixel(originalRow, originalCol);
                    }
                }
                subImages[row][col] = new Image(subImagePixels, subImageSize, subImageSize);
            }
        }

        return subImages;
    }

    /*
     * Determines whether a specific pixel location should be considered padding.
     * Padding pixels are added to adjust the image dimensions to powers of two.
     *
     * @param row The row index of the pixel.
     * @param paddingY The padding applied vertically.
     * @param col The column index of the pixel.
     * @param paddingX The padding applied horizontally.
     * @param width The original width of the image before padding.
     * @param height The original height of the image before padding.
     * @return true if the pixel is part of the padding; false otherwise.
     */
    private static boolean isPadPixel(int row, int paddingY, int col, int paddingX, int width, int height) {
        return row < paddingY || row >= paddingY + height || col < paddingX || col >= paddingX + width;
    }


    /*
     * Checks if a given integer is a power of two.
     *
     * @param n The number to check.
     * @return true if n is a power of two; false otherwise.
     */
    private static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0;
    }

    /*
     * Finds the next power of two greater than or equal to a given number.
     * If the number is already a power of two, it is returned unchanged.
     *
     * @param n The number for which to find the closest power of two.
     * @return The next power of two greater than or equal to n.
     */
    private static int getNextPowerOfTwo(int n) {
        if (isPowerOfTwo(n)) {
            return n;
        }
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }

    /*
     * Calculates the greyscale intensity of a color using weighted averages.
     * This method is used for converting colored pixels to grayscale by considering
     * the luminance of each color component.
     *
     * @param color The color to convert to grayscale intensity.
     * @return The grayscale intensity of the color.
     */
    private static double getGreyscaleIntensity(Color color) {
        return (color.getRed() * RED_WEIGHT_TO_GREYSCALE + color.getGreen() * GREEN_WEIGHT_TO_GREYSCALE +
                color.getBlue() * BLUE_WEIGHT_TO_GREYSCALE);
    }

}