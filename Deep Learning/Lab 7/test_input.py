from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestInput(TestCase):
    def setUp(self):
        self.a = layers.Input(2)
        self.a.set(torch.tensor([3,5],dtype=torch.float64))

    def test_forward(self):
        self.a.forward()
        np.testing.assert_allclose(self.a.output.detach().numpy(), np.array([3,5]))


if __name__ == '__main__':
    unittest.main()
