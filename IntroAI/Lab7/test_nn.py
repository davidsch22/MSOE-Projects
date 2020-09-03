#
# test_and_or.py: creates various tic-tac-toe configurations 
#   for testing purposes
#
# Author: Derek Riley, 2020
#

from nn import NeuralNetwork, calc_loss
import numpy as np
import random


def create_or_nn_data():
    # input training data set for OR
    x = np.array([[0, 0],
                  [0, 1],
                  [1, 0],
                  [1, 1]])
    # expected outputs corresponding to given inputs
    y = np.array([[0],
                  [1],
                  [1],
                  [1]])
    return x, y


def create_and_nn_data():
    # input training data set for OR
    x = np.array([[0, 0],
                  [0, 1],
                  [1, 0],
                  [1, 1]])
    # expected outputs corresponding to given inputs
    y = np.array([[0],
                  [0],
                  [0],
                  [1]])
    return x, y


def load_tictactoe_csv(filepath):
    boards = []
    labels = []
    with open(filepath) as fl:
        lines = fl.readlines()
        random.shuffle(lines)
        for ln in lines:
            cols = ln.strip().split(",")
            board = []
            for s in cols[:-1]:
                if s == "o":
                    board.append(0)
                elif s == "b":
                    board.append(0.5)
                else:
                    board.append(1)
            label = 0 if cols[-1] == "Owin" else 1
            labels.append([label])
            boards.append(board)
    x = np.array(boards)
    y = np.array(labels)
    return x, y


def test_and_nn_1():
    x, y = create_or_nn_data()
    nn = NeuralNetwork(x, y, 4, 1)
    nn.train(150)
    # print(nn.loss())
    assert nn.loss() < .04


def test_and_nn_2():
    x, y = create_or_nn_data()
    nn = NeuralNetwork(x, y, 4, 2)
    nn.train(150)
    # print(nn.loss())
    assert nn.loss() < .01


def test_and_nn_3():
    x, y = create_or_nn_data()
    nn = NeuralNetwork(x, y, 3, 1)
    nn.train(1500)
    # print(nn.loss())
    assert nn.loss() < .002


def test_or_nn_1():
    x, y = create_and_nn_data()
    nn = NeuralNetwork(x, y, 4, 1)
    nn.train(150)
    # print(nn.loss())
    assert nn.loss() < .3


def test_or_nn_2():
    x, y = create_and_nn_data()
    nn = NeuralNetwork(x, y, 10, 1)
    nn.train(1000)
    # print(nn.loss())
    assert nn.loss() < .002


def test_or_nn_3():
    x, y = create_and_nn_data()
    nn = NeuralNetwork(x, y, 1, 2)
    nn.train(1500)
    # print(nn.loss())
    assert nn.loss() < .0009


def test_blanks_10nodes():
    x, y = load_tictactoe_csv("tic-tac-toeWBlanks.csv")
    train_num = int(len(x) * 0.9)
    train = x[:train_num]
    test = x[train_num:]
    train_y = y[:train_num]
    test_y = y[train_num:]
    nn = NeuralNetwork(train, train_y, 10, .01)
    nn.train(1000)
    test_out = nn.inference(test)
    loss = calc_loss(test_y, test_out)
    print("Test Loss: " + str(loss))
    for i in range(len(test_out)):
        if test_out[i] >= 0.8 and test_y[i] == 1:
            print("Correct")
        elif test_out[i] < 0.8 and test_y[i] == 0:
            print("Correct")
        else:
            print("Incorrect")


def test_blanks_20nodes():
    x, y = load_tictactoe_csv("tic-tac-toeWBlanks.csv")
    train_num = int(len(x) * 0.9)
    train = x[:train_num]
    test = x[train_num:]
    train_y = y[:train_num]
    test_y = y[train_num:]
    nn = NeuralNetwork(train, train_y, 20, .01)
    nn.train(1000)
    test_out = nn.inference(test)
    loss = calc_loss(test_y, test_out)
    print("Test Loss: " + str(loss))

    correct = 0
    for i in range(len(test_out)):
        if (test_out[i] >= 0.85 and test_y[i] == 1) or (test_out[i] < 0.85 and test_y[i] == 0):
            correct += 1
        else:
            print(test[i])

    print("Test Accuracy: " + str(correct / len(test_out)))


def run_all() -> None:
    """Runs all test cases"""
    test_or_nn_1()
    test_or_nn_2()
    test_or_nn_3()
    test_and_nn_1()
    test_and_nn_2()
    test_and_nn_3()
    print("All tests pass.")


def main() -> int:
    """Main test program which prompts user for tests to run and displays any
    result.
    """
    n = int(input("Enter test number (1-11; 0 = run all): "))
    if n == 0:
        run_all()
        return 0
    elif n == 1:
        test_or_nn_1()
    elif n == 2:
        test_or_nn_2()
    elif n == 3:
        test_or_nn_3()
    elif n == 4:
        test_and_nn_1()
    elif n == 5:
        test_and_nn_2()
    elif n == 6:
        test_and_nn_3()
    elif n == 7:
        test_blanks_10nodes()
    elif n == 8:
        test_blanks_20nodes()
    else:
        print("Error: unrecognized test number " + str(n))
    # print("Test passes")
    return 0


if __name__ == "__main__":
    exit(main())
