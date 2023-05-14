public class Layer {
    private final Neuron[] neurons;
    private final double[] outputs;
    private final double[] deltas;

    public Layer(int numInputs, int numNeurons) {
        this.neurons = new Neuron[numNeurons];
        this.outputs = new double[numNeurons];
        this.deltas = new double[numNeurons];
        for (int i = 0; i < numNeurons; i++) {
            neurons[i] = new Neuron(numInputs);
        }
    }

    //obliczanie wyjść warstwy
    public void forward(double[] inputs) {
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].activate(inputs);
        }
    }

    //blad sieci neuronowej
    public void calculateOutputLayerDeltas(double[] targetOutputs) {
        for (int i = 0; i < neurons.length; i++) {
            double output = outputs[i];
            double target = targetOutputs[i];
            deltas[i] = (target - output) * output * (1 - output);
        }
    }

    public void initializeWeights() {
        for (Neuron neuron : neurons) {
            neuron.initializeWeights(neurons.length);
        }
    }

    public void calculateHiddenLayerDeltas(Layer nextLayer) {
        for (int i = 0; i < neurons.length; i++) {
            double output = outputs[i];
            double sum = 0;
            for (int j = 0; j < nextLayer.neurons.length; j++) {
                Neuron nextLayerNeuron = nextLayer.neurons[j];
                sum += nextLayerNeuron.getWeight(i) * nextLayer.deltas[j];
            }
            deltas[i] = sum * output * (1 - output);
        }
    }

    public void updateWeights(double[] prevLayerOutputs, double learningRate) {
        for (int i = 0; i < neurons.length; i++) {
            Neuron neuron = neurons[i];
            neuron.updateWeights(prevLayerOutputs, deltas[i], learningRate);
        }
    }

    public double[] getOutputs() {
        return outputs;
    }
}
