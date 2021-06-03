from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestSum(TestCase):
    def setUp(self):
        self.a = layers.Input(2)
        self.a.set(torch.tensor([3,5],dtype=torch.float64))
        self.b = layers.Input(2)
        self.b.set(torch.tensor([1,2],dtype=torch.float64))
        self.sum = layers.Sum(self.a, self.b)

    def test_forward(self):
        self.sum.forward()
        np.testing.assert_allclose(self.sum.output.detach().numpy(), np.array([4,7]))
    
    def test_backward(self):
        self.sum.accumulate_grad(torch.tensor([0.1,0.1],dtype=torch.float64))
        self.sum.backward()
        np.testing.assert_allclose(self.a.grad.detach().numpy(), np.array([0.1,0.1]))
        np.testing.assert_allclose(self.b.grad.detach().numpy(), np.array([0.1,0.1]))


if __name__ == '__main__':
    unittest.main()
