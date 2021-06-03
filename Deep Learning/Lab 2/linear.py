import random
import torch
from matplotlib import pyplot as plt

def synthetic_data(w, b, num_examples):  #@save
    """Generate y = Xw + b + noise."""
    X = torch.normal(0, 1, (num_examples, len(w)))
    y = torch.matmul(X, w) + b
    y += torch.normal(0, 0.01, y.shape)
    return X, y

def data_iter(batch_size, features, labels):
    num_examples = len(features)
    indices = list(range(num_examples))
    # The examples are read at random, in no particular order
    random.shuffle(indices)
    for i in range(0, num_examples, batch_size):
        batch_indices = torch.tensor(indices[i:min(i + batch_size, num_examples)])
        yield features[batch_indices], labels[batch_indices]

def linreg(X, w, b):  #@save
    """The linear regression model."""
    return torch.matmul(X, w) + b

def squared_loss(y_hat, y):  #@save
    """Squared loss."""
    return (y_hat - y)**2 / 2

def sgd(params, lr, batch_size):  #@save
    """Minibatch stochastic gradient descent."""
    with torch.no_grad():
        for param in params:
            param -= lr * param.grad / batch_size
            param.grad.zero_()

if __name__ == '__main__':
    true_w = torch.tensor([[0.3, 0.4], [-0.4, 0.3]])
    true_b = torch.tensor([5, 10])
    features, labels = synthetic_data(true_w, true_b, 1000)

    print('features:', features[0], '\nlabel:', labels[0])

    # plt.scatter(features[:, 1].numpy(), labels.numpy(), 1)
    # plt.savefig('tmp.png') # When on VS Code, you need to save the figures to files to see them.  You can open the files in VS Code or through the Rosie Virtual Desktop OOD App.

    batch_size = 10
    lr = 0.03
    num_epochs = 3
    net = linreg
    loss = squared_loss

    for X, y in data_iter(batch_size, features, labels):
        print(X, '\n', y)
        break

    w = torch.normal(0, 0.01, size=(2, 2), requires_grad=True)
    b = torch.zeros(2, requires_grad=True)

    for epoch in range(num_epochs):
        for X, y in data_iter(batch_size, features, labels):
            l = loss(net(X, w, b), y)  # Minibatch loss in `X` and `y`
            # Compute gradient on `l` with respect to [`w`, `b`]
            l.sum().backward()
            sgd([w, b], lr, batch_size)  # Update parameters using their gradient
        with torch.no_grad():
            train_l = loss(net(features, w, b), labels)
            print(f'epoch {epoch + 1}, loss {float(train_l.mean()):f}')

    print(f'error in estimating w: {true_w - w.reshape(true_w.shape)}')
    print(f'error in estimating b: {true_b - b}')

"""
Part 1a true_b Errors:
    error in estimating w: tensor([[5.9605e-05, -1.4302e-04], [-3.5107e-05, 3.7462e-04]], grad_fn=<SubBackward0>)
    error in estimating b: tensor([0.0005, -0.0011], grad_fn=<SubBackward0>)

Part 1b true_b Errors:
    error in estimating w: tensor([[4.6906e-04, 2.8363e-04], [2.0027e-04, 9.5695e-05]], grad_fn=<SubBackward0>)
    error in estimating b: tensor([0.0010, 0.0014], grad_fn=<SubBackward0>)
"""