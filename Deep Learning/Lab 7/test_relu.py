from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestReLU(TestCase):
    def setUp(self):
        self.x = layers.Input(4)
        self.x.set(torch.tensor([-3,0,2,7],dtype=torch.float64))
        self.relu = layers.ReLU(self.x)

    def test_forward(self):
        self.relu.forward()
        np.testing.assert_allclose(self.relu.output.detach().numpy(), np.array([0,0,2,7]))
    
    def test_backward(self):
        self.relu.accumulate_grad(torch.tensor([0.1,0.1,0.1,0.1],dtype=torch.float64))
        self.relu.backward()
        np.testing.assert_allclose(self.x.grad.detach().numpy(), np.array([0,0,0.1,0.1]))


if __name__ == '__main__':
    unittest.main()
