from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestSoftmaxLoss(TestCase):
    def setUp(self):
        self.x = layers.Input(2)
        self.x.set(torch.tensor([3.28,5.31],dtype=torch.float64))
        self.actual = layers.Input(2)
        self.actual.set(torch.tensor([0,1],dtype=torch.float64))
        self.smloss = layers.SoftmaxLoss(self.x, self.actual)

    def test_forward(self):
        self.smloss.forward()
        np.testing.assert_allclose(self.smloss.y_pred.detach().numpy(), np.array([0.116,0.884]), atol=0.0005)
        np.testing.assert_allclose(self.smloss.output.detach().numpy(), np.array([0.123]), atol=0.0005)

    def test_backward(self):
        self.smloss.forward() # Forward must run first to calculate the softmax used in the derivative
        self.smloss.accumulate_grad(torch.tensor([0.1],dtype=torch.float64))
        self.smloss.backward()
        np.testing.assert_allclose(self.x.grad.detach().numpy(), np.array([0.0116,-0.0116]), atol=0.0001)


if __name__ == '__main__':
    unittest.main()
