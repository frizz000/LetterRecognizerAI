import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFeatureExtractor {

    public static double[] extractFeatures(BufferedImage image) {
        int targetWidth = 100;
        int targetHeight = 100;

        BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();
        double[] features = new double[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(resizedImage.getRGB(x, y));
                int grayscale = (int) (0.2989 * color.getRed() + 0.5870 * color.getGreen() + 0.1140 * color.getBlue());
                features[y * width + x] = grayscale / 255.0;
            }
        }
        return features;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
}
