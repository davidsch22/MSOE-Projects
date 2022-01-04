#!/usr/bin/env python3
from pybricks.messaging import BluetoothMailboxClient, TextMailbox

# client code to be run on a pc

SERVER = '24:71:89:4f:02:f7'


def ping():
    client = BluetoothMailboxClient()
    mbox = TextMailbox('greeting', client)

    print('establishing connection...')
    client.connect(SERVER)
    print('connected!')

    # In this program, the client sends the first message and then waits for the
    # server to reply.
    mbox.send('hello!')
    mbox.wait()
    print(mbox.read())


def sendMsg():
    client = BluetoothMailboxClient()
    mbox = TextMailbox('greeting', client)

    print('establishing connection...')
    client.connect(SERVER)
    print('connected!')
    while 1:
        cmd = input("Enter a letter: ")
        print("Sent command was: " + cmd)
        mbox.send(cmd)


def client():
    client = BluetoothMailboxClient()
    mbox = TextMailbox('greeting', client)

    print('establishing connection...')
    client.connect(SERVER)
    print('connected!')
    while 1:
        cmd = input("Enter a cmd (play or buttons): ")
        if cmd == "play":
            freq = input("Enter a frequency")
            msg = cmd + ":" + freq
            print("Sending message " + msg)
            mbox.send(cmd + ":" + freq)
        elif cmd == "buttons":
            msg = cmd + ":" + "dummy"
            print("Sending message " + msg)
            mbox.send(msg)
            mbox.wait()  # wait for a response
            b = mbox.read()
            print("lasted button pressed was " + str(b))
        else:
            print("Unrecognized cmd: " + cmd)


def requestSensorData():
    client = BluetoothMailboxClient()
    mbox = TextMailbox('greeting', client)
    print('establishing connection...')
    client.connect(SERVER)
    print('connected!')
    while 1:
        cmd = input("Enter the port number (S1-S4): ")
        print("Sensor for port number " + cmd + " has been requested.")
        mbox.send("sensors:" + cmd)
        mbox.wait()
        b = mbox.read()
        print(str(b))


def requestDataStream():
    client = BluetoothMailboxClient()
    mbox = TextMailbox('greeting', client)
    print('establishing connection...')
    client.connect(SERVER)
    print('connected!')
    while 1:
        print("Requested Data Stream.")
        while True:
            mbox.wait()
            b = mbox.read()
            print(str(b))


def main():
    #requestDataStream()
    requestSensorData()
    #ping()
    #sendMsg()
    #client()


if __name__ == '__main__':
    main()
