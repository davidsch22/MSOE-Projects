from unittest import TestCase
import numpy as np
import unittest
import network
import layers
import torch

class TestNetwork(TestCase):
    def setUp(self):
        self.network = network.Network()

        x = layers.Input(2)
        x.set(torch.tensor([6,3],dtype=torch.float64))

        W = layers.Input((3, 2))
        W.set(torch.tensor([[0.3,0.5], [0.2,0.7], [0.1,0.4]],dtype=torch.float64))
        b = layers.Input(3)
        b.set(torch.tensor([0.1,0.2,0.3],dtype=torch.float64))
        M = layers.Input((2, 3))
        M.set(torch.tensor([[0.3,0.5,0.1], [0.2,0.7,0.8]],dtype=torch.float64))
        c = layers.Input(2)
        c.set(torch.tensor([0.3,0.5],dtype=torch.float64))
        y_actual = layers.Input(2)
        y_actual.set(torch.tensor([3,5],dtype=torch.float64))
        
        u = layers.Linear(W, b, x)
        h = layers.ReLU(u)
        v = layers.Linear(M, c, h)
        L = layers.L2Loss(v, y_actual)

        s1 = layers.L2Loss(W)
        s2 = layers.L2Loss(M)
        s = layers.Sum(s1, s2)

        J = layers.Sum(L, s)

        self.network.add(u)
        self.network.add(h)
        self.network.add(v)
        self.network.add(L)
        self.network.add(s1)
        self.network.add(s2)
        self.network.add(s)
        self.network.add(J)

    def test_forward(self):
        np.testing.assert_equal(self.network.forward().detach().numpy(), np.array([1.36725]))


if __name__ == '__main__':
    unittest.main()
