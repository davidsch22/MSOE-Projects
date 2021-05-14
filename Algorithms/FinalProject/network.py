import torch

class Network:
    def __init__(self):
        """
        Initializes a layers attribute
        """
        self.layers = []

    def add(self, layer):
        """
        Adds a new layer to the network.

        Sublayers can *only* be added after their inputs have been added.
        (In other words, the DAG of the graph must be flattened and added in order from input to output)
        :param layer: The sublayer to be added
        """
        self.layers.append(layer)

    def set_input(self,input):
        """
        :param input: The numpy array that represents the signal input (e.g., the image to be classified)
        """
        assert(len(self.layers) > 0)
        self.layers[0].set(input)

    def set_output(self,output):
        """
        :param output: SubLayer that produces the useful output (e.g., clasification decisions) as its output.
        """
        # This becomes messier when your output is the variable o from the middle of the Softmax
        # layer -- I used try/catch on accessing the layer.classifications variable.
        # when trying to access read the output layer's variable -- and that ended up being in a
        # different method than this one.
        assert(len(self.layers) > 0)
        self.layers[len(self.layers) - 1] = output

    def forward(self):
        """
        Compute the output of the network in the forward direction.

        :return: A numpy array with useful output (e.g., the softmax decisions)
        """
        for layer in self.layers:
            layer.forward()
        return self.layers[len(self.layers) - 1].output
    
    def backward(self):
        """
        Performs backpropagation through every layer of the network and adjusts
        the gradients of every layer's inputs.
        """
        self.layers[-1].accumulate_grad(torch.tensor(1))
        for i in range(len(self.layers)-1, -1, -1):
            self.layers[i].backward()
        
    def step(self, step_size):
        """
        Adjusts the weights of every layer after backpropagating through the entire network
        """
        for i in range(len(self.layers)):
            self.layers[i].step(step_size)
    
    def clear_grad(self):
        for i in range(len(self.layers)):
            self.layers[i].clear_grad()
