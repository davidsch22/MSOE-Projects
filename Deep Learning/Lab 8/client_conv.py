import torch
import numpy as np
import matplotlib.pyplot as plt
import torchvision
import warnings
import os.path

import network
import layers

EPOCHS = 50

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
             y is a numpy array where columns are one-hot labels for the training sample.
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
             y is a numpy array where columns are one-hot labels for the training sample.
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
            """Like ToTensor. Perhaps no differences."""

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


def load_dataset_images(train=True,dataset='Fashion-MNIST',download=False):
    """
    :param train: True for training, False for testing
    :param dataset: 'Fashion-MNIST' or 'CIFAR-10'
    :param download: True to download. Keep to false afterwords to avoid unneeded downloads.
    :return: (x,y) the dataset. x is a numpy array where columns are training samples and
             y is a numpy array where columns are one-hot labels for the training sample.
    """
    if dataset == 'Fashion-MNIST':
        num_labels = 10
    elif dataset == 'CIFAR-10':
        num_labels = 10
    elif dataset == 'CIFAR-100':
        num_labels = 100
    else:
        raise ValueError('Unknown dataset: '+str(dataset))

    class ToTorch(object):
        """Like ToTensor. Perhaps no differences."""

        def __call__(self, pic):
            return torchvision.transforms.functional.to_tensor(pic)

    print('Loading',dataset,'for','training' if train else 'testing')
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
    x = torch.zeros((len(data),)+data[0][0].shape)
    for index, image in enumerate(data):
        x[index,:,:,:] = data[index][0]
    labels = np.array([sample[1] for sample in data])
    y = torch.zeros((num_labels, labels.size), dtype=torch.float32)
    y[labels, torch.arange(len(labels))] = 1
    return x, y


if __name__ == '__main__':
    torch.set_default_dtype(torch.float32)
    if torch.cuda.is_available():
        torch.cuda.set_device(0)
        torch.set_default_tensor_type(torch.cuda.FloatTensor)
        print("Running on the GPU")
    else:
        print("Running on the CPU")

    # Select your datasource.
    # dataset = 'Fashion-MNIST'
    dataset = 'CIFAR-10'
    # dataset = 'CIFAR-100'

    #x_train, y_train = create_linear_training_data()
    #x_train, y_train = create_folded_training_data()
    #points_train, insideness_train = create_square()
    #x_train, y_train = load_dataset_flattened(train=True, dataset=dataset, download=False)
    x_train, y_train = load_dataset_images(train=True, dataset=dataset, download=False)

    CHANNELS = x_train.shape[1]
    SAMPLES = x_train.shape[0]
    OUTPUTS = y_train.shape[0]

    EPOCHS = 15
    CHANNELS_2 = 8
    HIDDEN = 128
    LEARN_RATE = 0.001

    # Build your network.
    # Conv -> Relu -> Conv -> Relu -> Flatten -> Linear -> Relu -> SoftmaxLoss
    network = network.Network()

    x = layers.Input(x_train.shape[1:])
    f1 = layers.Input((CHANNELS_2,CHANNELS,3,3)) # Filter for 1st Conv
    f1.set(torch.randn(f1.output.shape) / 10.0)
    f2 = layers.Input((1,CHANNELS_2,3,3)) # Filter for 2nd Conv
    f2.set(torch.randn(f2.output.shape) / 10.0)
    b = layers.Input(HIDDEN) # Biases of first Linear
    b.set(torch.randn(b.output.shape) / 10.0)
    c = layers.Input(OUTPUTS) # Biases of second Linear
    c.set(torch.randn(c.output.shape) / 10.0)
    y_actual = layers.Input(OUTPUTS)

    u = layers.Convolution(x, f1)
    f = layers.ReLU(u)
    v = layers.Convolution(f, f2)
    g = layers.ReLU(v)
    w = layers.Flatten(g)
    W = layers.Input((HIDDEN, w.output.shape[0])) # Weights of first Linear
    W.set(torch.randn(W.output.shape) / 10.0)
    M = layers.Input((OUTPUTS, HIDDEN)) # Weights of second Linear
    M.set(torch.randn(M.output.shape) / 10.0)
    h = layers.Linear(W, b, w)
    o = layers.ReLU(h)
    k = layers.Linear(M, c, o)
    r = layers.ReLU(k)
    L = layers.SoftmaxLoss(r, y_actual)

    REG_CONST = 1/SAMPLES
    s1 = layers.L2Norm(W, REG_CONST)
    s2 = layers.L2Norm(M, REG_CONST)
    s = layers.Sum(s1, s2)
    J = layers.Sum(L, s)

    network.add(u)
    network.add(f)
    network.add(v)
    network.add(g)
    network.add(w)
    network.add(h)
    network.add(o)
    network.add(k)
    network.add(r)
    network.add(L)
    network.add(s1)
    network.add(s2)
    network.add(s)
    network.add(J)

    # Train your network.
    for i in range(EPOCHS):
        true = 0
        for j in range(SAMPLES):
            x_sample = x_train[j,:,:,:]
            y_sample = y_train[:,j].reshape(OUTPUTS)
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
            network.step(LEARN_RATE)
        
        print("Epoch", i+1, "- Accuracy:", true/SAMPLES, "Loss:", loss.item())

    # Test the accuracy of your network
    # x_test, y_test = load_dataset_flattened(train=False, dataset=dataset, download=False)
    x_test, y_test = load_dataset_images(train=False, dataset=dataset, download=False)
    true = 0
    SAMPLES = x_test.shape[0]
    
    for i in range(SAMPLES):
        x_sample = x_test[i,:,:,:]
        y_sample = y_test[:,i].reshape(OUTPUTS)
        x.set(x_sample)
        y_actual.set(y_sample)

        loss = network.forward()
        y_pred = L.y_pred
        pred_class = torch.argmax(y_pred)
        true_class = torch.argmax(y_sample)
        if pred_class == true_class:
            true += 1
    
    print("Testing - Accuracy:", true/SAMPLES, "Final Loss:", loss.item())
