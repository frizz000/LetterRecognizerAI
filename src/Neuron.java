import java.util.Random;

public class Neuron {
    private final double[] weights;
    private double bias;
    private final Random random;

    public Neuron(int numInputs) {
        this.weights = new double[numInputs];
        this.random = new Random();
        initializeWeights(numInputs);
    }

    public void initializeWeights(int numInputs) {
        double variance = 2.0 / numInputs;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextGaussian() * Math.sqrt(variance);
        }
        bias = random.nextGaussian() * Math.sqrt(variance);
    }

    public double activate(double[] inputs) {
        double sum = bias;
        for (int i = 0; i < inputs.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sigmoid(sum);
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public void updateWeights(double[] inputs, double delta, double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * delta * inputs[i];
        }
        bias += learningRate * delta;
    }

    public double getWeight(int index) {
        return weights[index];
    }
}

