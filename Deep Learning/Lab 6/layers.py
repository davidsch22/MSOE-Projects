import numpy as np
import torch

# Layers in this file are arranged in roughly the order they
# would appear in a network.

class Layer:
    def __init__(self, out_size):
        """
        :param out_size: The size/shape the layer's output must be
        """
        self.output = torch.zeros(out_size)
        self.grad = torch.zeros(out_size)
    
    def accumulate_grad(self, grad):
        """
        Adds the gradient it receives to this layer's self.grad

        :param grad: The gradient to add
        """
        assert(self.grad.shape == grad.shape)
        self.grad = self.grad + grad

    def clear_grad(self):
        """
        Resets the gradients all to zero
        """
        self.grad = torch.zeros(self.output.shape)
    
    def backward(self):
        """
        Performs back-propagation through this single layer, then accumulates
        the input gradients into the gradients of the previous layers.
        """
        pass

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights
        of the current layer based on step_size and the current gradients

        :param step_size: Percentage of the gradient that should be applied to the weights (Learning rate)
        """
        pass


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

    def backward(self):
        """
        Do nothing, as this is only an input layer.
        """
        pass

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights
        of the current layer based on step_size and the current gradients

        :param step_size: Percentage of the gradient that should be applied to the weights (Learning rate)
        """
        diff = -step_size * self.grad
        self.output += diff
        self.clear_grad()


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

    def backward(self):
        """
        Performs back-propagation through this single layer, then accumulates
        the input gradients into the gradients of the previous layers.
        """
        dW = torch.matmul(self.grad.reshape(self.grad.shape[0], 1), self.x.output.reshape(1, self.x.output.shape[0]))
        db = self.grad
        dx = torch.matmul(self.W.output.t(), self.grad)
        self.W.accumulate_grad(dW)
        self.b.accumulate_grad(db)
        self.x.accumulate_grad(dx)

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights
        of the current layer based on step_size and the current gradients

        :param step_size: Percentage of the gradient that should be applied to the weights (Learning rate)
        """
        self.W.step(step_size)
        self.b.step(step_size)
        self.x.clear_grad()
        self.clear_grad()


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
    
    def backward(self):
        """
        Performs back-propagation through this single layer, then accumulates
        the input gradients into the gradients of the previous layers.
        """
        dx = self.grad * (self.x.output > 0).float()
        self.x.accumulate_grad(dx)

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights
        of the current layer based on step_size and the current gradients

        :param step_size: Percentage of the gradient that should be applied to the weights (Learning rate)
        """
        self.clear_grad()


class SoftmaxLoss(Layer):
    """
    Puts the input through the softmax function, followed by the cross-entropy
    loss function based on a given array of expected values
    """
    def __init__(self, x, y_true):
        """
        :param x: Reference to the previous layer
        :param y_true: A numpy array of the expected values for each of the original network inputs
        """
        assert(x.output.ndim == 1)
        assert(y_true.output.ndim == 1)
        assert(x.output.shape[0] == y_true.output.shape[0])
        Layer.__init__(self, 1)
        self.x = x
        self.y_pred = 0
        self.y_true = y_true
    
    def softmax(self, X):
        """
        Compute the softmax for more values than the obvious implementation

        :param X: Values to put through the softmax
        """
        exp = torch.exp(X - torch.max(X))
        return exp / torch.sum(exp)

    def forward(self):
        """
        Set this layer's output based on the outputs of the layers that feed into it.
        """
        self.y_pred = self.softmax(self.x.output) # Softmax
        self.output = -torch.sum(self.y_true.output * torch.log(self.y_pred)) # Cross-entropy loss

    def backward(self):
        """
        Performs back-propagation through this single layer, then accumulates
        the input gradients into the gradients of the previous layers.
        """
        assert(isinstance(self.y_pred, torch.Tensor))
        dx = self.grad * (self.y_pred - self.y_true.output)
        self.x.accumulate_grad(dx)

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights
        of the current layer based on step_size and the current gradients

        :param step_size: Percentage of the gradient that should be applied to the weights (Learning rate)
        """
        self.clear_grad()


class L2Norm(Layer):
    """
    This is a good loss function for regression problems.

    It implements the squared L2 norm of the inputs.
    """
    def __init__(self, W):
        """
        :param W: Reference to the weights Input layer
        """
        assert(W.output.ndim > 0)
        Layer.__init__(self, 1)
        self.W = W

    def forward(self):
        """
        Set this layer's output based on the outputs of the layers that feed into it.
        """
        self.output = 0.5 * torch.sum(torch.pow((self.W.output),2))
    
    def backward(self):
        """
        Performs back-propagation through this single layer, then accumulates
        the input gradients into the gradients of the previous layers.
        """
        dW = self.grad * self.W.output
        self.W.accumulate_grad(dW)

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights
        of the current layer based on step_size and the current gradients

        Given that the network was constructed in the correct order, the weights' Linear
        layer should have already called its step method, so calling it here would be redundant

        :param step_size: Percentage of the gradient that should be applied to the weights (Learning rate)
        """
        self.clear_grad()


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

    def backward(self):
        """
        Performs back-propagation through the single layer and then accumulates the input
        gradients into the gradients of the previous layers.
        """
        ds1 = self.grad
        ds2 = self.grad
        self.s1.accumulate_grad(ds1)
        self.s2.accumulate_grad(ds2)

    def step(self, step_size):
        """
        Performs a single step of stochastic gradient descent, updating the weights of the
        current layer based on the step_size parameter and the current gradients.

        :param step_size: The step size the SGD will use
        """
        self.clear_grad()
