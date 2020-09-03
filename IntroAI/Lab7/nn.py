#
# nn.py: Basic Neural Network implementation stub.  
# You will fill out the stubs below using numpy as much as possible.  
# This class serves as a base for you to build on for the labs.  
#
# Author: Derek Riley, 2020
# Inspired by https://towardsdatascience.com/how-to-build-your-own-neural-network-from-scratch-in-python-68998a08e4f6
#


import numpy as np


def sigmoid(x: np.ndarray) -> np.ndarray:
    """This is the sigmoid activation function."""
    return 1 / (1 + np.exp(-(12 * x - 6)))


def sigmoid_derivative(x: np.ndarray) -> np.ndarray:
    """This is the derivative of the sigmoid function."""
    return 12 * sigmoid(x) * (1 - sigmoid(x))


def calc_loss(expected_out, actual_out) -> float:
    """ Calculate the MSE error for the given output data."""
    _sum = np.sum(np.square(expected_out - actual_out))
    return _sum / len(actual_out)


class NeuralNetwork:
    """Represents a basic fully connected single-layer neural network.  

    Attributes:
        _train (2D numpy array): input features used for training,
            one row for each sample, and one column for each feature
        _val (2D numpy array): input features used for validation,
            one row for each sample, and one column for each feature
        _weights1 (numpy array): connection weights between the input
            and hidden layer
        _weights2 (numpy array): connection weights between the hidden
            layer and output neuron
        _train_y (numpy array): expected outputs of the network used for training,
            one row for each sample, and one column for each output variable
        _val_y (numpy array): expected outputs of the network used for validation,
            one row for each sample, and one column for each output variable
        _output (numpy array): stores the current output of the network
            after a feedforward pass
        _learning_rate (float): scales the derivative influence in backprop
    """

    def __init__(self, x: np.ndarray, y: np.ndarray, num_hidden_neurons=4, lr=1):
        """Setup a Neural Network with a single hidden layer.  This method
        requires two vectors of x and y values as the input and output data.
        """
        # 89% of the given data is for training and the rest for validation, making
        # 80% of all of the data for training and 10% for validation,
        # after 10% was taken beforehand in test_nn.py for testing afterwards
        train_num = int(len(x) * 0.89)
        self._train = x[:train_num]
        self._val = x[train_num:]

        self._weights1 = np.random.rand(self._train.shape[1],
                                        num_hidden_neurons)
        self._weights2 = np.random.rand(num_hidden_neurons, 1)

        self._biases1 = np.random.rand(self._train.shape[0],
                                       num_hidden_neurons)
        self._biases2 = np.random.rand(self._train.shape[0], 1)

        self._layer1 = np.zeros((self._train.shape[0], num_hidden_neurons))

        self._train_y = y[:train_num]
        self._val_y = y[train_num:]

        self._output = np.zeros(self._train_y.shape)
        self._learning_rate = lr

    def inference(self, inputs) -> np.ndarray:
        """
        Use the network to make predictions for a given vector of inputs.
        This is the math to support a feedforward pass.  
        """
        dot_1 = np.dot(inputs, self._weights1)
        self._layer1 = sigmoid(dot_1 + self._biases1[:len(inputs)])

        dot_2 = np.dot(self._layer1, self._weights2)
        return sigmoid(dot_2 + self._biases2[:len(inputs)])

    def feedforward(self):
        """
        This is used in the training process to calculate and save the
        outputs for backpropogation calculations.  
        """
        self._output = self.inference(self._train)

    def backprop(self):
        """
        Update model weights based on the error between the most recent 
        predictions (feedforward) and the training values.  
        """
        # Application of the chain rule to find derivatives of the loss function
        # This block of code was inspired by the code shown here:
        # https://towardsdatascience.com/how-to-build-your-own-neural-network-from-scratch-in-python-68998a08e4f6
        deriv_loss = 2 * (self._train_y - self._output)  # Upstream gradient to the output
        gradient = deriv_loss * sigmoid_derivative(self._output)  # Local gradient to the output

        d_weights2 = np.dot(self._layer1.T, gradient)
        d_biases2 = gradient

        gradient = np.dot(gradient, self._weights2.T) * sigmoid_derivative(self._layer1)
        d_weights1 = np.dot(self._train.T, gradient)
        d_biases1 = gradient

        # Update the weights and biases with the derivatives of the loss function
        self._weights1 += self._learning_rate * d_weights1
        self._weights2 += self._learning_rate * d_weights2
        self._biases1 += self._learning_rate * d_biases1
        self._biases2 += self._learning_rate * d_biases2

    def train(self, epochs=100):
        """This method trains the network for the given number of epochs.
        It doesn't return anything, instead it just updates the state of
        the network variables.
        """
        for i in range(epochs):
            self.feedforward()
            self.backprop()
            val_out = self.inference(self._val)

            train_loss = calc_loss(self._train_y, self._output)
            val_loss = calc_loss(self._val_y, val_out)
            print("Epoch: " + str(i) + " | Train Loss: " + str(train_loss) + " | Val Loss: " + str(val_loss))

    def loss(self) -> float:
        """ Calculate the MSE error for the set of training data."""
        _sum = np.sum(np.square(self._train_y - self._output))
        return _sum / len(self._output)
