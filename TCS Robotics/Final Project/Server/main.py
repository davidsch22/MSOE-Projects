#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from pybricks.ev3devices import Motor
from pybricks.parameters import Port
from pybricks.messaging import BluetoothMailboxServer, TextMailbox
from _thread import allocate_lock
from time import sleep

# Create your objects here.
ev3 = EV3Brick()
left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

# Write your program here.
ev3.speaker.beep()


# Note the timeout parameter doesn't seem to work
# This wait checks for a message and then just returns
# Compared to wait() which will sit there and wait
# for a message
def wait_with_timeout(mbox, name):
    """Waits until ``mbox`` receives a value."""
    lock = allocate_lock()
    lock.acquire()
    with mbox._connection._lock:
        mbox._connection._updates[name] = lock
    try:
        return lock.acquire(0, 1)
    finally:
        with mbox._connection._lock:
            del mbox._connection._updates[name]


# Sets up a connection between the server and the client.
def setupConnection():
    server = BluetoothMailboxServer()
    mbox = TextMailbox('greeting', server)
    print('waiting for connection...')
    server.wait_for_connection()
    print('connected!')
    return mbox


# Sets up a connection to the client and waits for a message. Once
# a message is received (total fingers for each hand), it will be split
# into two values, multiplied, and then set as the robot's motor speed.
def server():
    mbox = setupConnection()

    while 1:
        mbox.wait()
        msg = mbox.read()

        if msg != None:
            print("Number of Fingers: " + msg)
            left, right = mbox.read().split(",")
            left_speed = int(left) * 150
            right_speed = int(right) * 150
            set_motor_speed(left_speed, right_speed)

        else:
            print("Received invalid command: " + cmd)

        # Clears msg and the mailbox
        msg = None
        mbox._connection._mailboxes = {}


# Method used to set the speed of each motor.
def set_motor_speed(left_speed, right_speed):
    """
    Sets the motors to given speeds.
    """
    left_motor.run(left_speed)
    right_motor.run(right_speed)


if __name__ == '__main__':
    server()
