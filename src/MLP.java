import java.awt.image.BufferedImage;
import java.util.List;

public class MLP {
    private final Layer[] layers;
    private final int numLabels;

    public MLP(int[] layerSizes, int numLabels) {
        this.numLabels = numLabels;
        layers = new Layer[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i], layerSizes[i + 1]);
            layers[i].initializeWeights();
        }
    }

    public int predictLabel(double[] inputs) {
        double[] currentInputs = inputs;
        for (Layer layer : layers) {
            layer.forward(currentInputs);
            currentInputs = layer.getOutputs();
        }

        int predictedLabel = 0;
        double maxProbability = currentInputs[0];
        for (int i = 1; i < numLabels; i++) {
            if (currentInputs[i] > maxProbability) {
                maxProbability = currentInputs[i];
                predictedLabel = i;
            }
        }
        return predictedLabel;
    }

    public void train(List<BufferedImage> trainImages, List<Integer> trainLabels,
                      List<BufferedImage> testImages, List<Integer> testLabels,
                      int maxEpochs, int printInterval, double learningRate) {

        for (int epoch = 0; epoch < maxEpochs; epoch++) {
            for (int i = 0; i < trainImages.size(); i++) {
                BufferedImage image = trainImages.get(i);
                double[] inputs = ImageFeatureExtractor.extractFeatures(image);
                int label = trainLabels.get(i);

                double[] targetOutputs = new double[numLabels];
                targetOutputs[label] = 1;

                for (int layerIdx = layers.length - 1; layerIdx >= 0; layerIdx--) {
                    Layer layer = layers[layerIdx];
                    if (layerIdx == layers.length - 1) {
                        layer.calculateOutputLayerDeltas(targetOutputs);
                    } else {
                        Layer nextLayer = layers[layerIdx + 1];
                        layer.calculateHiddenLayerDeltas(nextLayer);
                    }
                }

                double[] prevLayerOutputs = inputs;
                for (Layer layer : layers) {
                    layer.updateWeights(prevLayerOutputs, learningRate);
                    prevLayerOutputs = layer.getOutputs();
                }
            }

            if (epoch % printInterval == 0) {
                int correctPredictions = 0;
                for (int i = 0; i < testImages.size(); i++) {
                    BufferedImage image = testImages.get(i);
                    double[] inputs = ImageFeatureExtractor.extractFeatures(image);
                    int trueLabel = testLabels.get(i);
                    int predictedLabel = predictLabel(inputs);

                    if (predictedLabel == trueLabel) {
                        correctPredictions++;
                    }
                }
                double accuracy = (double) correctPredictions / testImages.size();
                System.out.println("Epoch: " + epoch + ", Accuracy: " + String.format("%.2f", accuracy * 100) + "%");
            }
        }
    }
}

