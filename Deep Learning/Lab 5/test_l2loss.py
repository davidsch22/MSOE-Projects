from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestL2Loss(TestCase):
    def setUp(self):
        self.actual = layers.Input(3)
        self.actual.set(torch.tensor([8,5,10],dtype=torch.float64))
        self.pred = layers.Input(3)
        self.pred.set(torch.tensor([3,5,1],dtype=torch.float64))
        self.l2loss_loss = layers.L2Loss(self.pred, self.actual)
        self.a = layers.Input((2,3))
        self.a.set(torch.tensor([[8,5,10], [3,5,1]],dtype=torch.float64))
        self.l2loss_reg = layers.L2Loss(self.a)

    def test_forward(self):
        self.l2loss_loss.forward()
        np.testing.assert_allclose(self.l2loss_loss.output.detach().numpy(), np.array([53]))
        self.l2loss_reg.forward()
        np.testing.assert_allclose(self.l2loss_reg.output.detach().numpy(), np.array([112]))


if __name__ == '__main__':
    unittest.main()
