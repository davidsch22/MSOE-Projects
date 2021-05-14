import torch
import numpy as np
import matplotlib.pyplot as plt
import torchvision
import warnings
import os.path

import network
import layers

# For simple regression problem
TRAINING_POINTS = 1000

# For fashion-MNIST and similar problems
DATA_ROOT = '/data/cs3450/data/'
FASHION_MNIST_TRAINING = '/data/cs3450/data/fashion_mnist_flattened_training.npz'
FASHION_MNIST_TESTING = '/data/cs3450/data/fashion_mnist_flattened_testing.npz'
CIFAR10_TRAINING = '/data/cs3450/data/cifar10_flattened_training.npz'
CIFAR10_TESTING = '/data/cs3450/data/cifar10_flattened_testing.npz'
CIFAR100_TRAINING = '/data/cs3450/data/cifar100_flattened_training.npz'
CIFAR100_TESTING = '/data/cs3450/data/cifar100_flattened_testing.npz'


def create_linear_training_data():
    """
    This method simply rotates points in a 2D space.
    Be sure to use L2 regression in the place of the final softmax layer before testing on this
    data!
    :return: (x,y) the dataset. x is a numpy array where columns are training samples and
             y is a numpy array where columns are expected values for the training sample.
    """
    x = torch.randn((2, TRAINING_POINTS))
    x1 = x[0:1, :].clone()
    x2 = x[1:2, :]
    y = torch.cat((-x2, x1), axis=0)
    return x, y


def create_folded_training_data():
    """
    This method introduces a single non-linear fold into the sort of data created by create_linear_training_data. Be sure to REMOVE the final softmax layer before testing on this data!
    Be sure to use L2 regression in the place of the final softmax layer before testing on this
    data!
    :return: (x,y) the dataset. x is a numpy array where columns are training samples and
             y is a numpy array where columns are expected values for the training sample.
    """
    x = torch.randn((2, TRAINING_POINTS))
    x1 = x[0:1, :].clone()
    x2 = x[1:2, :]
    x2 *= 2 * ((x2 > 0).float() - 0.5)
    y = torch.cat((-x2, x1), axis=0)
    return x, y


def create_square():
    """
    This is the square example that we looked at in class.
    insideness is true if the points are inside the square.
    :return: (points, insideness) the dataset. points is a 2xN array of points and insideness is true if the point is inside the square.
    """
    win_x = [2,2,3,3]
    win_y = [1,2,2,1]
    win = torch.tensor([win_x,win_y],dtype=torch.float32)
    win_rot = torch.cat((win[:,1:],win[:,0:1]),axis=1)
    t = win_rot - win # edges tangent along side of poly
    rotation = torch.tensor([[0, 1],[-1,0]],dtype=torch.float32)
    normal = rotation @ t # normal vectors to each side of poly
        # torch.matmul(rotation,t) # Same thing

    points = torch.rand((2,2000),dtype = torch.float32)
    points = 4*points

    vectors = points[:,np.newaxis,:] - win[:,:,np.newaxis] # reshape to fill origin
    insideness = (normal[:,:,np.newaxis] * vectors).sum(axis=0)
    insideness = insideness.T
    insideness = insideness > 0
    insideness = insideness.all(axis=1)
    return points, insideness


def load_dataset_flattened(train=True,dataset='Fashion-MNIST',download=False):
    """
    :param train: True for training, False for testing
    :param dataset: 'Fashion-MNIST', 'CIFAR-10', or 'CIFAR-100'
    :param download: True to download. Keep to false afterwords to avoid unneeded downloads.
    :return: (x,y) the dataset. x is a numpy array where columns are training samples and
             y is a numpy array where columns are one-hot labels for the training sample.
    """
    if dataset == 'Fashion-MNIST':
        if train:
            path = FASHION_MNIST_TRAINING
        else:
            path = FASHION_MNIST_TESTING
        num_labels = 10
    elif dataset == 'CIFAR-10':
        if train:
            path = CIFAR10_TRAINING
        else:
            path = CIFAR10_TESTING
        num_labels = 10
    elif dataset == 'CIFAR-100':
        if train:
            path = CIFAR100_TRAINING
        else:
            path = CIFAR100_TESTING
        num_labels = 100
    else:
        raise ValueError('Unknown dataset: '+str(dataset))

    if os.path.isfile(path):
        print('Loading cached flattened data for',dataset,'training' if train else 'testing')
        data = np.load(path)
        x = torch.tensor(data['x'],dtype=torch.float32)
        y = torch.tensor(data['y'],dtype=torch.float32)
        pass
    else:
        class ToTorch(object):
            """Like ToTensor, only to a numpy array"""

            def __call__(self, pic):
                return torchvision.transforms.functional.to_tensor(pic)

        if dataset == 'Fashion-MNIST':
            data = torchvision.datasets.FashionMNIST(
                root=DATA_ROOT, train=train, transform=ToTorch(), download=download)
        elif dataset == 'CIFAR-10':
            data = torchvision.datasets.CIFAR10(
                root=DATA_ROOT, train=train, transform=ToTorch(), download=download)
        elif dataset == 'CIFAR-100':
            data = torchvision.datasets.CIFAR100(
                root=DATA_ROOT, train=train, transform=ToTorch(), download=download)
        else:
            raise ValueError('This code should be unreachable because of a previous check.')
        x = torch.zeros((len(data[0][0].flatten()), len(data)),dtype=torch.float32)
        for index, image in enumerate(data):
            x[:, index] = data[index][0].flatten()
        labels = torch.tensor([sample[1] for sample in data])
        y = torch.zeros((num_labels, len(labels)), dtype=torch.float32)
        y[labels, torch.arange(len(labels))] = 1
        np.savez(path, x=x.detach().numpy(), y=y.detach().numpy())
    return x, y


def init_adagrad_states(parameters):
    list_parameters = []
    for param in parameters:
        list_parameters.append(torch.zeros(param.output.shape))
    return list_parameters


def adagrad(params, states, lr):
    eps = 1e-6
    for p, s in zip(params, states):
        s[:] += torch.square(p.grad)
        p.output -= lr * p.grad / torch.sqrt(s + eps)
        p.clear_grad()


if __name__ == '__main__':
    torch.set_default_dtype(torch.float32)
    if torch.cuda.is_available():
        torch.cuda.set_device(0)
        torch.set_default_tensor_type(torch.cuda.FloatTensor)
        print("Running on the GPU")
    else:
        print("Running on the CPU")
    
    # Select your datasource.
    dataset = 'Fashion-MNIST'
    # dataset = 'CIFAR-10'
    # dataset = 'CIFAR-100'

    #x_train, y_train = create_linear_training_data()
    #x_train, y_train = create_folded_training_data()
    #points_train, insideness_train = create_square()
    x_train, y_train = load_dataset_flattened(train=True, dataset=dataset, download=True)

    inputs = x_train.shape[0]
    samples = x_train.shape[1]
    outputs = y_train.shape[0]

    HIDDEN = 100
    EPOCHS = 20
    LEARN_RATE = 0.01

    # Build your network.
    network = network.Network()

    x = layers.Input(inputs)
    W = layers.Input((HIDDEN, inputs))
    W.set(torch.randn((HIDDEN, inputs)) / 10.0)
    b = layers.Input(HIDDEN)
    b.set(torch.randn(HIDDEN) / 10.0)
    M = layers.Input((outputs, HIDDEN))
    M.set(torch.randn((outputs, HIDDEN)) / 10.0)
    c = layers.Input(outputs)
    c.set(torch.randn(outputs) / 10.0)
    y_actual = layers.Input(outputs)

    u = layers.Linear(W, b, x)
    h = layers.ReLU(u)
    v = layers.Linear(M, c, h)
    L = layers.SoftmaxLoss(v, y_actual)

    reg_const = 1/samples
    s1 = layers.L2Norm(W, reg_const)
    s2 = layers.L2Norm(M, reg_const)
    s = layers.Sum(s1, s2)

    J = layers.Sum(L, s)

    network.add(u)
    network.add(h)
    network.add(v)
    network.add(L)
    network.add(s1)
    network.add(s2)
    network.add(s)
    network.add(J)

    params = [W,b,c,M]
    derivative_list = init_adagrad_states(params)

    # Train your network.
    for i in range(EPOCHS):
        true = 0
        for j in range(samples):
            x_sample = x_train[:,j].reshape(inputs)
            y_sample = y_train[:,j].reshape(outputs)
            x.set(x_sample)
            y_actual.set(y_sample)

            # Get loss from input
            loss = network.forward()
            y_pred = L.y_pred
            pred_class = torch.argmax(y_pred)
            true_class = torch.argmax(y_sample)
            if pred_class == true_class:
                true += 1

            # Backpropagate to adjust gradients
            network.backward()

            # Use gradients to adjust weights
            adagrad(params, derivative_list, LEARN_RATE)
            network.clear_grad()
            # network.step(LEARN_RATE)
        
        print("Epoch", i+1, "- Accuracy:", true/samples, "Loss:", loss.item())
    
    # Test the accuracy of your network
    x_test, y_test = load_dataset_flattened(train=False, dataset=dataset, download=True)
    true = 0
    samples = x_test.shape[1]
    
    for i in range(samples):
        x_sample = x_test[:,i].reshape(inputs)
        y_sample = y_test[:,i].reshape(outputs)
        x.set(x_sample)
        y_actual.set(y_sample)

        loss = network.forward()
        y_pred = L.y_pred
        pred_class = torch.argmax(y_pred)
        true_class = torch.argmax(y_sample)
        if pred_class == true_class:
            true += 1
    
    print("Testing - Accuracy:", true/samples, "Final Loss:", loss.item())
