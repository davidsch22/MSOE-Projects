from unittest import TestCase
import numpy as np
import unittest
import layers
import torch

class TestConvolution(TestCase):
    def setUp(self):
        self.x = layers.Input((2,5,4))
        self.x.set(torch.tensor([[[1, 2, 3, 4], [2, 3, 4, 5], [3, 4, 5, 6], [4, 5, 6, 7], [5, 6, 7, 8]],
                                 [[4, 3, 2, 1], [5, 4, 3, 2], [6, 5, 4, 3], [7, 6, 5, 4], [8, 7, 6, 5]]],dtype=torch.float32))
        self.filter = layers.Input((3,2,3,3))
        self.filter.set(torch.tensor([[[[-1, 0, 0],      [0, 0, 0],       [0, 0, 1]],
                                       [[0, 0, 1],       [0, 0, 0],       [-1, 0, 0]]],
                                      [[[1/9, 1/9, 1/9], [1/9, 1/9, 1/9], [1/9, 1/9, 1/9]],
                                       [[1/9, 1/9, 1/9], [1/9, 1/9, 1/9], [1/9, 1/9, 1/9]]],
                                      [[[0, 1, 0],       [0, 0, 0],       [0, -1, 0]],
                                       [[0, 0, 0],       [-1, 0, 1],      [0, 0, 0]]]], dtype=torch.float32))
        self.conv = layers.Convolution(self.x, self.filter)

    def test_forward(self):
        self.conv.forward()
        np.testing.assert_allclose(self.conv.output.detach().numpy(), np.array([[[0,0],
                                                                                 [0,0],
                                                                                 [0,0]],
                                                                                [[7,7],
                                                                                 [9,9],
                                                                                 [11,11]],
                                                                                [[-4,-4],
                                                                                 [-4,-4],
                                                                                 [-4,-4]]],dtype=np.float32), rtol=2e-7)
    
    def test_backward(self):
        self.conv.accumulate_grad(torch.tensor([[[0.1,0.1],
                                                 [0.1,0.1],
                                                 [0.1,0.1]],
                                                [[0.1,0.1],
                                                 [0.1,0.1],
                                                 [0.1,0.1]],
                                                [[0.1,0.1],
                                                 [0.1,0.1],
                                                 [0.1,0.1]]],dtype=torch.float32))
        self.conv.backward()
        np.testing.assert_allclose(self.x.grad.detach().numpy(), np.array([[[-0.0889,  0.0222,  0.1222,  0.0111],
                                                                            [-0.0778,  0.0444,  0.1444,  0.0222],
                                                                            [-0.0667, -0.0333,  0.1667,  0.1333],
                                                                            [ 0.0222, -0.0556,  0.0444,  0.1222],
                                                                            [ 0.0111, -0.0778,  0.0222,  0.1111]],
                                                                           [[ 0.0111,  0.0222,  0.1222,  0.1111],
                                                                            [-0.0778, -0.0556,  0.2444,  0.2222],
                                                                            [-0.1667, -0.1333,  0.2667,  0.2333],
                                                                            [-0.1778, -0.1556,  0.1444,  0.1222],
                                                                            [-0.0889, -0.0778,  0.0222,  0.0111]]]), rtol=2e-3)
        np.testing.assert_allclose(self.filter.grad.detach().numpy(), np.array([[[[1.5, 2.1, 2.7], [2.1, 2.7, 3.3], [2.7, 3.3, 3.9]],
                                                                                 [[2.7, 2.1, 1.5], [3.3, 2.7, 2.1], [3.9, 3.3, 2.7]]],
                                                                                [[[1.5, 2.1, 2.7], [2.1, 2.7, 3.3], [2.7, 3.3, 3.9]],
                                                                                 [[2.7, 2.1, 1.5], [3.3, 2.7, 2.1], [3.9, 3.3, 2.7]]],
                                                                                [[[1.5, 2.1, 2.7], [2.1, 2.7, 3.3], [2.7, 3.3, 3.9]],
                                                                                 [[2.7, 2.1, 1.5], [3.3, 2.7, 2.1], [3.9, 3.3, 2.7]]]]), rtol=1e-1)


if __name__ == '__main__':
    unittest.main()
