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

//            for (int label : trainLabels) {
//                System.out.println(label);
//            }

            List<BufferedImage> testImages = DataLoader.loadImages(testFolderPath, labels);

//            for (int i = 0; i < testImages.size(); i++) {
//                String outputPath = "Data/Pictures/" + i + ".png";
//                BufferedImage resizedTestImage = ImageFeatureExtractor.resizeImage(testImages.get(i), 28, 28);
//                ImageFeatureExtractor.saveImageToFile(resizedTestImage, outputPath, "png");
//            }

            List<Integer> testLabels = DataLoader.loadLabels(testFolderPath, labels);

            int numOfFeatures = ImageFeatureExtractor.extractFeatures(trainImages.get(0),0.5).length;

            //==========test==========
//            BufferedImage testImage = testImages.get(0);
//            int targetWidth = 28;
//            int targetHeight = 28;
//
//            long startTime = System.nanoTime();
//
//            BufferedImage resizedTestImage = ImageFeatureExtractor.resizeImage(testImage, targetWidth, targetHeight);
//
//            long endTime = System.nanoTime();
//            long duration = endTime - startTime;
//
//            System.out.println("Czas wykonania operacji przeskalowania: " + (duration / 1_000_000) + " ms");

            //==========test==========

            double learningRate = 0.1;
            int maxEpochs = 100;
            int printInterval = 1;

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
