import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String trainFolderPath = "Data/Train";
        String testFolderPath = "Data/Test";
        String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z"};

        try {
            List<BufferedImage> trainImages = DataLoader.loadImages(trainFolderPath, labels);
            List<Integer> trainLabels = DataLoader.loadLabels(trainFolderPath, labels);
            List<BufferedImage> testImages = DataLoader.loadImages(testFolderPath, labels);
            List<Integer> testLabels = DataLoader.loadLabels(testFolderPath, labels);

            int numOfFeatures = ImageFeatureExtractor.extractFeatures(trainImages.get(0)).length;

            double learningRate = 0.1;
            int maxEpochs = 1000;
            int printInterval = 10;

            int[] layerSizes = {numOfFeatures, 100, 50, labels.length};
            MLP mlp = new MLP(layerSizes, labels.length);
            mlp.train(trainImages, trainLabels, testImages, testLabels, maxEpochs, printInterval, learningRate);

            SwingUtilities.invokeLater(() -> new DrawingGUI(mlp, labels));

        } catch (IOException e) {
            System.err.println("Error while loading images and labels.");
            e.printStackTrace();
        }
    }
}
