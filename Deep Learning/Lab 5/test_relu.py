from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestReLU(TestCase):
    def setUp(self):
        self.a = layers.Input(4)
        self.a.set(torch.tensor([-3,0,2,7],dtype=torch.float64))
        self.relu = layers.ReLU(self.a)

    def test_forward(self):
        self.relu.forward()
        np.testing.assert_allclose(self.relu.output.detach().numpy(), np.array([0,0,2,7]))


if __name__ == '__main__':
    unittest.main()
