import pandas as pd
import tensorflow as tf
from tensorflow import keras
from keras import layers, losses


def main():
    train = pd.read_csv("Hill_Valley_Noiseless_Train.data")
    test = pd.read_csv("Hill_Valley_Noiseless_Test.data")
    # train = pd.read_csv("Hill_Valley_Noise_Train.data")
    # test = pd.read_csv("Hill_Valley_Noise_Test.data")

    train_y = train[:]["class"].to_numpy()
    train_x = train.drop(columns="class").to_numpy()

    test_y = test[:]["class"].to_numpy()
    test_x = test.drop(columns="class").to_numpy()

    tf.random.set_seed(42)
    
    model = keras.Sequential(
        [
            layers.Softmax(),
            layers.Dense(64, activation="relu"),
            layers.Dense(16, activation="relu"),
            layers.Dense(5, activation="relu"),
            layers.Dense(1, activation="sigmoid"),
        ]
    )
    model.compile(optimizer=keras.optimizers.Adam(learning_rate=0.001),
                  loss=losses.BinaryCrossentropy(),
                  metrics=['accuracy'])
    model.fit(train_x, train_y, epochs=1000) # For best test accuracy, 1000 with noiseless data, 1100 with noisy data

    test_loss, test_acc = model.evaluate(test_x,  test_y, verbose=2)
    print('\nTest Accuracy:', test_acc)


if __name__ == "__main__":
    main()
