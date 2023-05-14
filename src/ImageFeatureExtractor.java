import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFeatureExtractor {

    public static double[] extractFeatures(BufferedImage image, double threshold) {
        int targetWidth = 30;
        int targetHeight = 30;

        BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();

        int regionWidth = width / 3;
        int regionHeight = height / 3;

        double[] features = new double[9];

        for (int regionY = 0; regionY < 3; regionY++) {
            for (int regionX = 0; regionX < 3; regionX++) {
                int paintedPixels = 0;
                int totalPixels = 0;
                for (int y = regionY * regionHeight; y < (regionY + 1) * regionHeight; y++) {
                    for (int x = regionX * regionWidth; x < (regionX + 1) * regionWidth; x++) {
                        Color color = new Color(resizedImage.getRGB(x, y));
                        int grayscale = (int) (0.2989 * color.getRed() + 0.5870 * color.getGreen() + 0.1140 * color.getBlue());
                        if (grayscale / 255.0 < threshold) {
                            paintedPixels++;
                        }
                        totalPixels++;
                    }
                }
                features[regionY * 3 + regionX] = (double) paintedPixels / totalPixels;
            }
        }
        return features;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage croppedImage = cropWhiteSpace(originalImage);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, croppedImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(croppedImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }

    public static BufferedImage cropWhiteSpace(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int left = 0, top = 0, right = width - 1, bottom = height - 1;
        int whiteRGB = Color.WHITE.getRGB();

        while (left < width) {
            boolean foundNonWhite = false;
            for (int y = 0; y < height; y++) {
                if (originalImage.getRGB(left, y) != whiteRGB) {
                    foundNonWhite = true;
                    break;
                }
            }
            if (foundNonWhite) {
                break;
            }
            left++;
        }

        while (top < height) {
            boolean foundNonWhite = false;
            for (int x = 0; x < width; x++) {
                if (originalImage.getRGB(x, top) != whiteRGB) {
                    foundNonWhite = true;
                    break;
                }
            }
            if (foundNonWhite) {
                break;
            }
            top++;
        }

        while (right >= 0) {
            boolean foundNonWhite = false;
            for (int y = 0; y < height; y++) {
                if (originalImage.getRGB(right, y) != whiteRGB) {
                    foundNonWhite = true;
                    break;
                }
            }
            if (foundNonWhite) {
                break;
            }
            right--;
        }

        while (bottom >= 0) {
            boolean foundNonWhite = false;
            for (int x = 0; x < width; x++) {
                if (originalImage.getRGB(x, bottom) != whiteRGB) {
                    foundNonWhite = true;
                    break;
                }
            }
            if (foundNonWhite) {
                break;
            }
            bottom--;
        }

        int croppedWidth = right - left + 1;
        int croppedHeight = bottom - top + 1;
        return originalImage.getSubimage(left, top, croppedWidth, croppedHeight);
    }

    public static void saveImageToFile(BufferedImage image, String filePath, String format) throws IOException {
        File outputFile = new File(filePath);
        ImageIO.write(image, format, outputFile);
    }
}
