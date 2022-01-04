#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from pybricks.ev3devices import (Motor, TouchSensor, ColorSensor, InfraredSensor, UltrasonicSensor, GyroSensor)
from pybricks.parameters import Port, Stop, Direction, Button, Color
from pybricks.tools import wait, StopWatch, DataLog
from pybricks.robotics import DriveBase
from pybricks.media.ev3dev import SoundFile, ImageFile
from pybricks.messaging import BluetoothMailboxServer, TextMailbox

from _thread import allocate_lock
from time import sleep

# Create your objects here.
ev3 = EV3Brick()

left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

color = ColorSensor(Port.S4)

# Write your program here.
ev3.speaker.beep()

# Declare constants here.
WHEEL = 17.6
TRACK = 11.7
SPEED = 102

# Note the timeout parameter doesn't seem to work
# This wait checks for a message and then just returns
# Compared to wait() which will sit there and wait
# for a message
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

def setupConnection():
    server = BluetoothMailboxServer()
    mbox = TextMailbox('greeting', server)

    # The server must be started before the client!
    print('waiting for connection...')
    server.wait_for_connection()
    print('connected!')
    return mbox

def playSound(note):
    if note == "q":
        return
    elif note == "a":
        ev3.speaker.beep(880,100)
    elif note == "c":
        ev3.speaker.beep(523,100)
    elif note == "g":
        ev3.speaker.beep(783,100)

def server():
    mbox = setupConnection()
    lastColor = ""
    
    while 1:
        # wait_with_timeout(mbox,mbox.name) # Non-blocking
        mbox.wait() # Blocking
        
        msg = mbox.read()
        if msg != None:
            print("Message received is: "+msg)
            
            # Assumes there are only two tokens
            cmd,arg = mbox.read().split(":")
            if cmd == "play":
                playSound(arg)
            elif cmd == "forward":
                moveForward(arg)
            elif cmd == "turn":
                turn90(arg)
            elif cmd == "wasd":
                wasd(arg)
            elif cmd == "sensor":
                nextColor = color.color()
                while nextColor == lastColor:
                    nextColor = color.color()   
                lastColor = nextColor
                mbox.send(color.color())
            elif cmd == "motors":
                left_speed, right_speed = arg.split(",")
                motors(int(left_speed), int(right_speed))
                mbox.send("Motor speeds set")
            else:
                print("Received invalid cmd "+cmd)
            
            # Clears msg and the mailbox
            msg = None
            mbox._connection._mailboxes={}


def moveForward(dist):
    """
    Moves the robot forward a specified distance for task 1
    """
    dist = float(dist)
    run_time = dist / (WHEEL/360) / SPEED

    left_motor.run(SPEED)
    right_motor.run(SPEED)
    sleep(run_time)
    left_motor.run(0)
    right_motor.run(0)

def turn90(dir):
    """
    Turns the robot 90 degrees in a specified direction for task 1
    """
    if dir == "left":
        left_motor.run(-37.584)
        right_motor.run(37.584)
        sleep(5)
        left_motor.run(0)
        right_motor.run(0)
    elif dir == "right":
        left_motor.run(37.584)
        right_motor.run(-37.584)
        sleep(5)
        left_motor.run(0)
        right_motor.run(0)

def wasd(button):
    """
    Moves the robot based on the most recent WASD button pressed for task 4
    """
    if button == "w":
        left_motor.run(SPEED)
        right_motor.run(SPEED)
    elif button == "a":
        left_motor.run(-SPEED)
        right_motor.run(SPEED)
    elif button == "s":
        left_motor.run(-SPEED)
        right_motor.run(-SPEED)
    elif button == "d":
        left_motor.run(SPEED)
        right_motor.run(-SPEED)
    elif button == "space":
        left_motor.run(0)
        right_motor.run(0)

def motors(left_speed, right_speed):
    """
    Sets the motors to given speeds for task 5
    """
    left_motor.run(left_speed)
    right_motor.run(right_speed)


def main():
    server()

if __name__ == '__main__':
    main()
