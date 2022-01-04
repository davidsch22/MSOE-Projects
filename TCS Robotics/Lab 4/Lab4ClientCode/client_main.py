#!/usr/bin/env python3
from pybricks.messaging import BluetoothMailboxClient, TextMailbox
from stateMachine import StateMachine
import keyboard

# Client code to be run on a pc

SERVER = '24:71:89:4a:86:c4'
mboxClient = BluetoothMailboxClient()
mbox = TextMailbox('greeting', mboxClient)


def is_number(n):
    try:
        float(n)
    except ValueError:
        return False
    return True


def print_values_color():
    """
    This function is used to print out test values from the color sensor
    """
    mbox.send("sensor:dummy")
    mbox.wait()
    color = mbox.read()
    print("Color: " + color)


def task_five_turn_left():
    """
    This function is used as the turn left action for the FSM
    """
    mbox.send("motors:0,100")
    mbox.wait()


def task_five_turn_right():
    """
    This function is used as the turn right action for the FSM
    """
    mbox.send("motors:100,0")
    mbox.wait()


def task_five_on_tape_condition():
    """
    This function is used to check the on tape condition for the FSM
    """
    mbox.send("sensor:dummy")
    mbox.wait()
    color = mbox.read()
    return color == "Color.WHITE"


def task_five_off_tape_condition():
    """
    This function is used to check the off tape condition for the FSM
    """
    mbox.send("sensor:dummy")
    mbox.wait()
    color = mbox.read()
    return color != "Color.WHITE"


def task_five():
    """
    This is the main execution loop for the task five state machine
    """
    state_machine = StateMachine()

    state_machine.add_state("Turn_Left", task_five_turn_left, default=True) \
        .add_state("Turn_Right", task_five_turn_right) \
        .add_transition("Turn_Left", "Turn_Right", task_five_on_tape_condition) \
        .add_transition("Turn_Right", "Turn_Left", task_five_off_tape_condition)

    while True:
        state_machine.run()


def client():
    print('establishing connection...')
    mboxClient.connect(SERVER)
    print('connected!')
    while 1:
        cmd = input("Enter a cmd (play or buttons): ")
        if cmd == "play":
            freq = input("Enter a frequency")
            msg = cmd+":"+freq
            print("Sending message "+msg)
            mbox.send(msg)
        elif cmd == "buttons":
            msg = cmd+":dummy"
            print("Sending message "+msg)
            mbox.send(msg)
            mbox.wait()  # wait for a response
            b = mbox.read()
            print("lasted button pressed was "+str(b))
        elif cmd == "forward":
            dist = input("Enter a distance (in cm)")
            while not is_number(dist):
                dist = input("Not a valid distance. Try again")
            msg = cmd+":"+dist
            print("Sending message "+msg)
            mbox.send(msg)
        elif cmd == "turn":
            direction = input("Enter a direction (left/right)")
            while direction != "left" and direction != "right":
                direction = input("Not a valid direction. Try again")
            msg = cmd+":"+direction
            print("Sending message "+msg)
            mbox.send(msg)
        elif cmd == "wasd":
            print("Press one of the WASD keys to move the robot. Press space when you are finished to stop it.")
            stop = False
            last_msg = ""
            while not stop:
                if keyboard.is_pressed("w"):
                    msg = cmd + ":w"
                elif keyboard.is_pressed("a"):
                    msg = cmd + ":a"
                elif keyboard.is_pressed("s"):
                    msg = cmd + ":s"
                elif keyboard.is_pressed("d"):
                    msg = cmd + ":d"
                elif keyboard.is_pressed("space"):
                    msg = cmd + ":space"
                    stop = True
                else:
                    msg = last_msg

                if msg != last_msg:
                    mbox.send(msg)
                    last_msg = msg
        elif cmd == "line":
            task_five()
        elif cmd == "color":
            print_values_color()
        else:
            print("Unrecognized cmd: "+cmd)


def main():
    client()


if __name__ == '__main__':
    main()
