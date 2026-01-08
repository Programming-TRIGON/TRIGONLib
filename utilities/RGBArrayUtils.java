package frc.trigon.lib.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A class that contains utilities for arrays of rgb values.
 */
public class RGBArrayUtils {
    /**
     * Converts a png image into a resized array of rgb values.
     *
     * @param filePath     the path to the image
     * @param targetWidth  the target resized width of the array
     * @param targetHeight the target resized height of the array
     * @return the resized array
     * @throws IOException if the image cannot be found
     */
    public static int[][][] convertPngToRgbArray(String filePath, int targetWidth, int targetHeight) throws IOException {
        return convertPackedRgbToSeparateRgbArray(convertPngToPackedRgbArray(filePath, targetWidth, targetHeight));
    }

    public static int[][] convertPngToPackedRgbArray(String filePath, int targetWidth, int targetHeight) throws IOException {
        BufferedImage rawImage = ImageIO.read(new File(filePath));
        if (rawImage == null)
            return null;

        BufferedImage processedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = processedImage.createGraphics();
        g2d.drawImage(rawImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        int[][] rgbArray = new int[targetHeight][targetWidth];
        for (int y = 0; y < targetHeight; y++)
            for (int x = 0; x < targetWidth; x++)
                rgbArray[y][x] = processedImage.getRGB(x, y);
        return rgbArray;
    }

    /**
     * Converts a 2D array of packed ARGB integer values (0xAARRGGBB) into a 3D array
     * where each pixel's RGB components (0-255) are separated.
     * The alpha component is discarded in this conversion.
     *
     * @param packedRgbArray The 2D integer array containing packed ARGB values.
     * @return A 3D integer array where `result[y][x][0]` is Red, `result[y][x][1]` is Green,
     * and `result[y][x][2]` is Blue
     */
    public static int[][][] convertPackedRgbToSeparateRgbArray(int[][] packedRgbArray) {
        int[][][] separateRgbArray = new int[packedRgbArray.length][packedRgbArray[0].length][3];

        for (int y = 0; y < separateRgbArray.length; y++) {
            for (int x = 0; x < separateRgbArray[0].length; x++) {
                separateRgbArray[y][x] = unpackPixel(packedRgbArray[y][x]);
            }
        }
        return separateRgbArray;
    }

    private static int[] unpackPixel(int packedPixel) {
        return new int[]{
                (packedPixel >> 16) & 0xFF,
                (packedPixel >> 8) & 0xFF,
                packedPixel & 0xFF
        };
    }
}
