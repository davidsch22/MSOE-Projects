from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestLinear(TestCase):
    def setUp(self):
        self.W = layers.Input((3, 2))
        self.W.set(torch.tensor([[3,5], [2,7], [1,4]],dtype=torch.float64))
        self.b = layers.Input(3)
        self.b.set(torch.tensor([1,2,3],dtype=torch.float64))
        self.x = layers.Input(2)
        self.x.set(torch.tensor([6,3],dtype=torch.float64))
        self.linear = layers.Linear(self.W, self.b, self.x)

    def test_forward(self):
        self.linear.forward()
        np.testing.assert_allclose(self.linear.output.detach().numpy(), np.array([34,35,21]))


if __name__ == '__main__':
    unittest.main()
