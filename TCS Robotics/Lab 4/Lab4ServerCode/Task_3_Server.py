#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from pybricks.ev3devices import (Motor, TouchSensor, ColorSensor,
                                 InfraredSensor, UltrasonicSensor, GyroSensor)
from pybricks.parameters import Port, Stop, Direction, Button, Color
from pybricks.tools import wait, StopWatch, DataLog
from pybricks.robotics import DriveBase
from pybricks.media.ev3dev import SoundFile, ImageFile
from pybricks.messaging import BluetoothMailboxServer, TextMailbox

from _thread import allocate_lock

# Create your objects here.
ev3 = EV3Brick()
motor_one = Motor(Port.A)
motor_two = Motor(Port.D)
touch = TouchSensor(Port.S1)
sonic = UltrasonicSensor(Port.S2)
gyro = GyroSensor(Port.S3)
rgb = ColorSensor(Port.S4)

# Write your program here.
ev3.speaker.beep()

# SERVER SIDE

#Note the timeout parameter doesn't seem to work
#This wait checks for a message and then just returns
#Compared to wait() which will sit there and wait
#for a message
def wait_with_timeout(mbox,name):
        """Waits until ``mbox`` receives a value."""
        lock = allocate_lock()
        lock.acquire()
        with mbox._connection._lock:
            mbox._connection._updates[name] = lock
        try:
            #timeout parameter doesn't work as
            #expected so I'm just setting
            #the wait flag to be zero
            return lock.acquire(0,1)
        finally:
            with mbox._connection._lock:
                del mbox._connection._updates[name]


def ping():
    server = BluetoothMailboxServer()
    mbox = TextMailbox('greeting', server)

    # The server must be started before the client!
    print('waiting for connection...')
    server.wait_for_connection()
    print('connected!')

    # In this program, the server waits for the client to send the first message
    # and then sends a reply.
    mbox.wait()
    print(mbox.read())
    mbox.send('hello to you!')


def setupConnection():
    server = BluetoothMailboxServer()
    mbox = TextMailbox('greeting', server)

    # The server must be started before the client!
    print('waiting for connection...')
    server.wait_for_connection()
    print('connected!')
    return mbox


def receiveMsg():
    mbox = setupConnection()
    while 1:
        mbox = setupConnection()
        mbox.wait()
        msg = mbox.read()
        print("Message received is: "+msg)
        if msg == "q":
            break    


def playNotes():
    mbox = setupConnection()

    while 1:
        wait_with_timeout(mbox,mbox.name)
        msg = mbox.read()
        if msg != None:
            print("Message received is: "+msg)
            if msg == "q":
                break
            elif msg == "a":
                ev3.speaker.beep(880,100)
            elif msg == "c":
                ev3.speaker.beep(523,100)
            elif msg == "g":
                ev3.speaker.beep(783,100)
            mbox._connection._mailboxes={}
            msg = None


def server():
    mbox = setupConnection()
    pressed = ""
    while 1:
        wait_with_timeout(mbox,mbox.name) #non-blocking
        #wait() #blocking
        msg = mbox.read()
        #if pressed != []:
        #    print("pressed  is "+str(pressed))
        if msg != None:
            print("Message received is: "+msg)
            #assumes there are only two tokens
            cmd,arg = mbox.read().split(":")
            if cmd == "play":
                #print("arg is "+arg)
                if arg == "q":
                    break
                elif arg == "a":
                    ev3.speaker.beep(880,100)
                elif arg == "c":
                    ev3.speaker.beep(523,100)
                elif arg == "g":
                    ev3.speaker.beep(783,100)
            elif cmd == "buttons":
                #print("pressed is "+str(pressed))
                mbox.send(pressed)
            else:
                print("Received invalid cmd "+cmd)
            #clears msg and the mailbox
            msg = None
            mbox._connection._mailboxes={}
        
        buttons = ev3.buttons.pressed()
        #gets the buttons pressed if any
        if buttons != []:
            pressed = "" #clears pressed
            for i in range(len(buttons)):
                pressed+=str(buttons[i])
                if i!=len(buttons)-1:
                    pressed+=":"
                    

def printButtons():
    pressed = ""
    while True:
        buttons = ev3.buttons.pressed()
        print(buttons)
        #gets the buttons pressed if any
        if buttons != []:
            pressed = "" #clears pressed
            for i in range(len(buttons)):
                pressed+=str(buttons[i])
                if i!=len(buttons)-1:
                    pressed+=":"
        print("Pressed is "+pressed)


def sendSensorData():
    mbox = setupConnection()
    while 1:

        # Phase 1
        mbox.wait()
        cmd,arg = mbox.read().split(":")
        if cmd == "sensors":
            port = arg.upper()
            print('Sensor data requested for Port '+ port +'.')

            # Phase 2
            if port == 'S1':
                sensor = 'Touch Sensor'
            elif port == 'S2':
                sensor = 'Sonic Sensor'
            elif port == 'S3':
                sensor = 'Gyro Sensor'
            elif port == 'S4':
                sensor = 'Color Sensor'
            else:
                sensor = 'Invalid Port Number'

            # Phase 3
            if sensor == 'Touch Sensor':
                data = str(touch.pressed())
            elif sensor == 'Sonic Sensor':
                data = str(sonic.distance()) + ' mm' 
            elif sensor == 'Gyro Sensor':
                data = str(gyro.speed()) + ' deg/s'
            elif sensor == 'Color Sensor':
                data = str(rgb.color())
            else:
                data = 'No Data Available'    

            mbox.send(sensor + ':\t' + data)               


def sendDataStream():
    mbox = setupConnection()
    while 1:
        print("Received continuous sensor data stream request.")

        while True:
            data_stream = ("Touch Sensor: "+str(touch.pressed())+"\t\tSonic Sensor: "+str(sonic.distance())+
                        " mm"+"\t\tGyro Sensor: "+str(gyro.speed())+" deg/s"+"\t\tColor Sensor: "+str(rgb.color()))

            mbox.send(data_stream)


def main():
    #sendDataStream()
    sendSensorData()
    #receiveMsg()
    #playNotes()
    #server()
    #printButtons()


if __name__ == '__main__':
    main()
