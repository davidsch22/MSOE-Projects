from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestL2Norm(TestCase):
    def setUp(self):
        self.W = layers.Input(3)
        self.W.set(torch.tensor([8,5,10],dtype=torch.float64))
        self.l2norm = layers.L2Norm(self.W, 0) # 0 had to be added for new constant (backpropagation test will fail)

    def test_forward(self):
        self.l2norm.forward()
        np.testing.assert_allclose(self.l2norm.output.detach().numpy(), np.array([94.5]))
    
    def test_backward(self):
        self.l2norm.accumulate_grad(torch.tensor([0.1],dtype=torch.float64))
        self.l2norm.backward()
        np.testing.assert_allclose(self.W.grad.detach().numpy(), np.array([0.8,0.5,1]))


if __name__ == '__main__':
    unittest.main()
