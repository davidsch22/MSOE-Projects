#
# test_and_or.py: creates various tic-tac-toe configurations 
#   for testing purposes
#
# Author: Derek Riley, 2020
#

from nn import NeuralNetwork
import numpy as np


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
        for ln in fl:
            cols = ln.strip().split(",")
            board = [0 if s == "o" else 1 for s in cols[:-1]]
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


def test_nn_1():
    x, y = load_tictactoe_csv("tic-tac-toe.csv")
    nn = NeuralNetwork(x, y, 4, .1)
    nn.train(1000)
    # print(nn.loss())
    assert nn.loss() < .06


def test_nn_2():
    x, y = load_tictactoe_csv("tic-tac-toe.csv")
    nn = NeuralNetwork(x, y, 10, .1)
    nn.train(10000)
    # print(nn.loss())
    assert nn.loss() < .0025


def test_nn_3():
    x, y = load_tictactoe_csv("tic-tac-toeFull.csv")
    nn = NeuralNetwork(x, y, 10, .04)
    nn.train(10000)
    #    print("3 " + str(nn.loss()))
    assert nn.loss() < .1


def test_nn_4():
    x, y = load_tictactoe_csv("tic-tac-toeFull.csv")
    nn = NeuralNetwork(x, y, 10, .03)
    nn.train(100000)
    #    print("4 " + str(nn.loss()))
    assert nn.loss() < .001


def test_nn_5():
    x, y = load_tictactoe_csv("tic-tac-toeFull.csv")
    nn = NeuralNetwork(x, y, 20, .01)
    nn.train(100000)
    #    print(nn.loss())
    assert nn.loss() < .01


def test_prediction():
    x, y = load_tictactoe_csv("tic-tac-toe.csv")
    x_full, y_full = load_tictactoe_csv("tic-tac-toeFull.csv")
    nn = NeuralNetwork(x, y, 10, .1)
    nn.train(10000)
    out = nn.inference(x_full[:32])  # Can only test 32 at a time because it was trained with 32
    for result in out:
        if result[0] > 0.9:
            print("X won")
        else:
            print("X did not win")


def test_prediction_full():
    x, y = load_tictactoe_csv("tic-tac-toeFull.csv")
    nn = NeuralNetwork(x, y, 10, .1)
    nn.train(10000)
    out = nn.inference(x)
    for result in out:
        if result[0] > 0.9:
            print("X won")
        else:
            print("X did not win")


def run_all() -> None:
    """Runs all test cases"""
    test_or_nn_1()
    test_or_nn_2()
    test_or_nn_3()
    test_and_nn_1()
    test_and_nn_2()
    test_and_nn_3()
    test_nn_1()
    test_nn_2()
    test_nn_3()
    test_nn_4()
    test_nn_5()
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
        test_nn_1()
    elif n == 8:
        test_nn_2()
    elif n == 9:
        test_nn_3()
    elif n == 10:
        test_nn_4()
    elif n == 11:
        test_nn_5()
    elif n == 12:
        test_prediction()
    elif n == 13:
        test_prediction_full()
    else:
        print("Error: unrecognized test number " + str(n))
    print("Test passes")
    return 0


if __name__ == "__main__":
    exit(main())
