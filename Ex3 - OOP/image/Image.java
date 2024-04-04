package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image {

    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructs an Image by loading it from a specified file.
     * The image is read into a 2D array of {@link Color} objects representing each pixel.
     *
     * @param filename the path to the image file to be loaded.
     * @throws IOException if an error occurs during image reading.
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * Constructs an Image from a given 2D array of {@link Color} objects.
     * This allows for creating an image directly from pixel data.
     *
     * @param pixelArray a 2D array of {@link Color} objects representing the image's pixels.
     * @param width the width of the image in pixels.
     * @param height the height of the image in pixels.
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the width of the image in pixels.
     *
     * @return the width of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the image in pixels.
     *
     * @return the height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the {@link Color} of a specific pixel in the image.
     *
     * @param x the x-coordinate of the pixel.
     * @param y the y-coordinate of the pixel.
     * @return the {@link Color} of the specified pixel.
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Saves the image to a file in JPEG format. The image is written based on the
     * internal representation of pixel data.
     *
     * @param fileName the name of the file to save the image to, without the extension.
     *                 The ".jpeg" extension is automatically appended.
     * @throws RuntimeException if an error occurs during file writing.
     */
    public void saveImage(String fileName) {
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
