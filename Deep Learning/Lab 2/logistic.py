from torch.utils import data
import torchvision
from torchvision import transforms
import torch

def load_data_fashion_mnist(batch_size, path='data', download=False, resize=None):  #@save
    """
    Modified from the d2l library.
    
    Download the Fashion-MNIST dataset and then load it into memory.
    
    This explicitly sets the number of workers to zero to avoid issues with
    Python multiprocessing on Windows. A larger number of workers can likely
    be used on Rosie.
    :param batch_size: number of training samples in a batch (e.g., 10)
    :param path: Data will be stored and read from here. (Default 'data')
    :param download: If can't find data, should we download it instead?. (Default False)
    :param resize: Default None means don't resize the images.
    :return: (train_loader, test_loader) Torch loaders for these two datasets.
    """
    trans = [transforms.ToTensor()]
    if resize:
        trans.insert(0, transforms.Resize(resize))
    trans = transforms.Compose(trans)
    mnist_train = torchvision.datasets.FashionMNIST(
        root=path, train=True, transform=trans, download=download)
    mnist_test = torchvision.datasets.FashionMNIST(
        root=path, train=False, transform=trans, download=download)
    return (data.DataLoader(mnist_train, batch_size, shuffle=True, num_workers=0),
        data.DataLoader(mnist_test, batch_size, shuffle=False, num_workers=0))

def net(X):
    return softmax(torch.matmul(X.reshape((-1, W.shape[0])), W) + b)

def cross_entropy(y_hat, y):
    return -torch.log(y_hat[range(len(y_hat)), y])

def accuracy(y_hat, y):  #@save
    """Compute the number of correct predictions."""
    if len(y_hat.shape) > 1 and y_hat.shape[1] > 1:
        y_hat = y_hat.argmax(axis=1)
    cmp = y_hat.type(y.dtype) == y
    return float(cmp.type(y.dtype).sum())

def evaluate_accuracy(net, data_iter):  #@save
    """Compute the accuracy for a model on a dataset."""
    if isinstance(net, torch.nn.Module):
        net.eval()  # Set the model to evaluation mode
    metric = Accumulator(2)  # No. of correct predictions, no. of predictions
    for X, y in data_iter:
        metric.add(accuracy(net(X), y), y.numel())
    return metric[0] / metric[1]

class Accumulator:  #@save
    """For accumulating sums over `n` variables."""
    def __init__(self, n):
        self.data = [0.0] * n

    def add(self, *args):
        self.data = [a + float(b) for a, b in zip(self.data, args)]

    def reset(self):
        self.data = [0.0] * len(self.data)

    def __getitem__(self, idx):
        return self.data[idx]
    
def train_epoch_ch3(net, train_iter, loss, updater):  #@save
    """The training loop defined in Chapter 3."""
    # Set the model to training mode
    if isinstance(net, torch.nn.Module):
        net.train()
    # Sum of training loss, sum of training accuracy, no. of examples
    metric = Accumulator(3)
    for X, y in train_iter:
        # Compute gradients and update parameters
        y_hat = net(X)
        l = loss(y_hat, y)
        if isinstance(updater, torch.optim.Optimizer):
            # Using PyTorch in-built optimizer & loss criterion
            updater.zero_grad()
            l.backward()
            updater.step()
            metric.add(float(l) * len(y), accuracy(y_hat, y), y.numel())
        else:
            # Using custom built optimizer & loss criterion
            l.sum().backward()
            updater(X.shape[0])
            metric.add(float(l.sum()), accuracy(y_hat, y), y.numel())
    # Return training loss and training accuracy
    return metric[0] / metric[2], metric[1] / metric[2]

def train_ch3(net, train_iter, test_iter, loss, num_epochs, updater):  #@save
    """
    Modified from the d2l library.
    Train a model (defined in Chapter 3).
    """
    results = []
    for epoch in range(num_epochs):
        train_metrics = train_epoch_ch3(net, train_iter, loss, updater)
        test_acc = evaluate_accuracy(net, test_iter)
        print('Epoch',epoch,'metrics:',train_metrics,'acc:',test_acc)
        results.append(train_metrics + (test_acc,))
    train_loss, train_acc = train_metrics
    assert train_loss < 0.5, train_loss
    assert train_acc <= 1 and train_acc > 0.7, train_acc
    assert test_acc <= 1 and test_acc > 0.7, test_acc
    return results

def sgd(params, lr, batch_size):  #@save
    """Minibatch stochastic gradient descent."""
    with torch.no_grad():
        for param in params:
            param -= lr * param.grad / batch_size
            param.grad.zero_()

# MNIST Training Error:
    # (0.5872562004725138, 0.79945) acc: 0.7784
    # (0.48309755427042644, 0.8338666666666666) acc: 0.8161
    # (0.4632148605108261, 0.8411666666666666) acc: 0.8332
    # (0.45386243765354156, 0.8438833333333333) acc: 0.8312
    # (0.4454366871356964, 0.8472833333333334) acc: 0.8386
    # (0.43983785299460093, 0.849) acc: 0.8254
    # (0.43559805737336477, 0.8498833333333333) acc: 0.8323
    # (0.43154215000867846, 0.8505833333333334) acc: 0.8236
    # (0.4266754540801048, 0.8534833333333334) acc: 0.841
    # (0.4251458285252253, 0.8533666666666667) acc: 0.8419
# After messing with the hyperparameters for a while, I was never able to end with a loss any less than 0.41, but the accuracy is still over 80%.

"""
Breaking and Fixing Softmax
-------------------------------------------------------
An example of an input that breaks softmax is:
    [12345, 67890, 99999999]
The softmax method below correctly computes the softmax for this input.
"""
def softmax(X):
    """
    This function serves as a sort of adapter to switch between
    softmax implementations more easily
    """
    return robust_softmax(X)

def simple_softmax(X):
    """
    Compute the softmax the straightforward way.
    """
    X_exp = torch.exp(X)
    partition = X_exp.sum(1, keepdim=True)
    return X_exp / partition  # The broadcasting mechanism is applied here

def robust_softmax(X):
    """
    Compute the softmax for more values than the obvious implementation.
    """
    z = X - torch.max(X)
    X_exp = torch.exp(z)
    partition = X_exp.sum(1, keepdim=True)
    return X_exp / partition

if __name__ == '__main__':
    batch_size = 32
    train_iter, test_iter = load_data_fashion_mnist(batch_size, download=True)

    num_inputs = 784
    num_outputs = 10

    W = torch.normal(0, 0.01, size=(num_inputs, num_outputs), requires_grad=True)
    b = torch.zeros(num_outputs, requires_grad=True)

    X = torch.tensor([[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]])

    lr = 0.1
    def updater(batch_size):
        return sgd([W, b], lr, batch_size)

    num_epochs = 10
    train_ch3(net, train_iter, test_iter, cross_entropy, num_epochs, updater)