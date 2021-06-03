import numpy as np
import torch

# TODO: Please be sure to read the comments in the main lab and think about your design before
# you begin to implement this part of the lab.

# Layers in this file are arranged in roughly the order they
# would appear in a network.


class Layer:
    def __init__(self, out_size):
        """
        :param out_size: The size/shape the layer's output must be
        """
        self.output = torch.zeros(out_size)


class Input(Layer):
    def __init__(self, out_size):
        """
        :param out_size: The size/shape the layer's output must be
        """
        Layer.__init__(self, out_size)

    def set(self,output):
        """
        Set the output of this input layer.
        :param output: The output to set, as a numpy array. Raise an error if this output's size
                       would change.
        """
        if (isinstance(output, np.ndarray)):
            output = torch.from_numpy(output)
        assert(self.output.shape == output.shape)
        self.output = output

    def randomize(self):
        """
        Set the output of this input layer to random values sampled from the standard normal
        distribution (numpy has a nice method to do this). Ensure that the output does not
        change size.
        """
        self.output = np.random.standard_normal(self.output.shape)

    def forward(self):
        """
        Do nothing, as this is only an input layer.
        """
        pass


class Linear(Layer):
    def __init__(self, W, b, x):
        """
        :param W: The weights Input layer
        :param b: The biases Input layer
        :param x: Reference to the previous Layer
        """
        assert(W.output.ndim == 2)
        assert(b.output.ndim == 1)
        assert(x.output.ndim == 1)
        assert(W.output.shape[1] == x.output.shape[0])
        assert(W.output.shape[0] == b.output.shape[0])
        Layer.__init__(self, b.output.shape[0])
        self.W = W
        self.b = b
        self.x = x

    def forward(self):
        """
        Set this layer's output based on the outputs of the layers that feed into it.
        """
        self.output = torch.matmul(self.W.output, self.x.output) + self.b.output


class ReLU(Layer):
    def __init__(self, x):
        """
        :param x: Reference to the previous Layer
        """
        assert(x.output.ndim == 1)
        Layer.__init__(self, x.output.shape)
        self.x = x

    def forward(self):
        """
        Set this layer's output based on the outputs of the layers that feed into it.
        """
        self.output = torch.clamp(self.x.output, min=0)


class L2Loss(Layer):
    """
    This is a good loss function for regression problems.

    It implements the squared L2 norm of the inputs.
    """
    def __init__(self, y_pred, y_real=0):
        """
        :param y_pred: Reference to the previous layer
        :param y_real: A numpy array of the expected values for each of the original network inputs.
            This is optional so that this layer may also be used for matrix regularization
        """
        if y_real == 0: # If layer is being used for regularization
            assert(y_pred.output.ndim > 0)
        else:           # If layer is being used for loss
            assert(y_pred.output.ndim == 1)
            assert(y_real.output.ndim == 1)
            assert(y_pred.output.shape[0] == y_real.output.shape[0])
        Layer.__init__(self, 1)
        self.y_pred = y_pred
        self.y_real = y_real

    def forward(self):
        """
        Set this layer's output based on the outputs of the layers that feed into it.
        """
        if self.y_real == 0:    # If layer is being used for regularization
            self.output = 0.5 * torch.sum(torch.pow((self.y_pred.output),2))
        else:                   # If layer is being used for loss
            self.output = 0.5 * torch.sum(torch.pow((self.y_real.output-self.y_pred.output),2))


class Sum(Layer):
    def __init__(self, s1, s2):
        """
        :param s1: Reference to one of the 2 previous layers
        :param s2: Reference to the other of the 2 previous layers
        """
        assert(s1.output.shape == s2.output.shape)
        Layer.__init__(self, s1.output.shape[0])
        self.s1 = s1
        self.s2 = s2

    def forward(self):
        """
        Set this layer's output based on the outputs of the layers that feed into it.
        """
        self.output = self.s1.output + self.s2.output


