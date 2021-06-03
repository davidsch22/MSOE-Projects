from unittest import TestCase
import numpy as np
import unittest
import network
import layers
import torch

class TestNetwork(TestCase):
    def setUp(self):
        self.network = network.Network()

        self.x = layers.Input(2)
        self.x.set(torch.tensor([6,3],dtype=torch.float64))

        self.W = layers.Input((3, 2))
        self.W.set(torch.tensor([[0.3,0.5], [0.2,0.7], [0.1,0.4]],dtype=torch.float64))
        self.b = layers.Input(3)
        self.b.set(torch.tensor([0.1,0.2,0.3],dtype=torch.float64))
        self.M = layers.Input((2, 3))
        self.M.set(torch.tensor([[0.3,0.5,0.1], [0.2,0.7,0.8]],dtype=torch.float64))
        self.c = layers.Input(2)
        self.c.set(torch.tensor([0.3,0.5],dtype=torch.float64))
        self.y_actual = layers.Input(2)
        self.y_actual.set(torch.tensor([0,1],dtype=torch.float64))
        
        self.u = layers.Linear(self.W, self.b, self.x)
        self.h = layers.ReLU(self.u)
        self.v = layers.Linear(self.M, self.c, self.h)
        self.L = layers.SoftmaxLoss(self.v, self.y_actual)

        self.s1 = layers.L2Norm(self.W)
        self.s2 = layers.L2Norm(self.M)
        self.s = layers.Sum(self.s1, self.s2)

        self.J = layers.Sum(self.L, self.s)

        self.network.add(self.u)
        self.network.add(self.h)
        self.network.add(self.v)
        self.network.add(self.L)
        self.network.add(self.s1)
        self.network.add(self.s2)
        self.network.add(self.s)
        self.network.add(self.J)

    def test_forward(self):
        Jout = self.network.forward()
        np.testing.assert_allclose(Jout.detach().numpy(), np.array([1.403]), atol=0.0005)

    def test_backward(self):
        Jout = self.network.forward() # 1.403
        Jout = Jout.reshape(1)
        self.J.accumulate_grad(Jout)
        self.network.backward()
        np.testing.assert_allclose(self.s.grad.detach().numpy(), np.array([1.403]), atol=0.0005)
        np.testing.assert_allclose(self.s1.grad.detach().numpy(), np.array([1.403]), atol=0.0005)
        np.testing.assert_allclose(self.s2.grad.detach().numpy(), np.array([1.403]), atol=0.0005)
        np.testing.assert_allclose(self.L.grad.detach().numpy(), np.array([1.403]), atol=0.0005)
        np.testing.assert_allclose(self.v.grad.detach().numpy(), np.array([0.163,-0.163]), atol=0.0005)
        np.testing.assert_allclose(self.M.grad.detach().numpy(), np.array([[0.975,1.272,0.482], [-0.273,0.412,0.781]]), atol=0.0005)
        np.testing.assert_allclose(self.c.grad.detach().numpy(), np.array([0.163,-0.163]), atol=0.0005)
        np.testing.assert_allclose(self.h.grad.detach().numpy(), np.array([0.016,-0.033,-0.114]), atol=0.0005)
        np.testing.assert_allclose(self.u.grad.detach().numpy(), np.array([0.016,-0.033,-0.114]), atol=0.0005)
        np.testing.assert_allclose(self.W.grad.detach().numpy(), np.array([[0.519,0.751], [0.085,0.885], [-0.544,0.219]]), atol=0.0005)
        np.testing.assert_allclose(self.b.grad.detach().numpy(), np.array([0.016,-0.033,-0.114]), atol=0.0005)


if __name__ == '__main__':
    unittest.main()
