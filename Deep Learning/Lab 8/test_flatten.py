from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestFlatten(TestCase):
    def setUp(self):
        self.x = layers.Input((2,2,2))
        self.x.set(torch.tensor([[[1, 2],[3, 4]],[[5, 6],[7, 8]]],dtype=torch.float64))
        self.flatten = layers.Flatten(self.x)

    def test_forward(self):
        self.flatten.forward()
        np.testing.assert_allclose(self.flatten.output.detach().numpy(), np.array([1,2,3,4,5,6,7,8]))
    
    def test_backward(self):
        self.flatten.accumulate_grad(torch.tensor([0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1],dtype=torch.float64))
        self.flatten.backward()
        np.testing.assert_allclose(self.x.grad.detach().numpy(), np.array([[[0.1, 0.1],[0.1, 0.1]],[[0.1, 0.1],[0.1, 0.1]]]))


if __name__ == '__main__':
    unittest.main()
