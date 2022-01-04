#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from pybricks.ev3devices import (Motor, TouchSensor, ColorSensor,
                                 InfraredSensor, UltrasonicSensor, GyroSensor)
from pybricks.parameters import Port, Stop, Direction, Button, Color
from pybricks.tools import wait, StopWatch, DataLog
from pybricks.robotics import DriveBase
from pybricks.media.ev3dev import SoundFile, ImageFile

import time

from stateMachine import StateTransition, StateMachine

# Task One
def print_values_ultrasonic(ultrasonic):
    """
    This function is used to print out test values from the ultrasonic sensor
    """
    while True:
        try:
            print("Distance: " + str(ultrasonic.distance(silent=False)))
        except OSError:
            print('OSError')


def task_one(left_motor, right_motor, ultrasonic):
    """
    This is the main execution loop for task one
    """
    hold_distance_mm = 125

    while True:
        offset_distance_mm = ultrasonic.distance(silent=False) - hold_distance_mm

        left_motor.run(offset_distance_mm)
        right_motor.run(offset_distance_mm)


# Task Two
def print_values_color(color):
    """
    This function is used to print out test values from the color sensor
    """
    while True:
        print("Color: " + str(color.color()))


def task_two_turn_left():
    """
    This function is used as the turn left action for the FSM
    """
    left_motor.run(0)
    right_motor.run(100)


def task_two_turn_right():
    """
    This function is used as the turn right action for the FSM
    """
    left_motor.run(100)
    right_motor.run(0)


def task_two_on_tape_condition():
    """
    This function is used to check the on tape condition for the FSM
    """
    return color.color() == Color.WHITE


def task_two_off_tape_condition():
    """
    This function is used to check the off tape condition for the FSM
    """
    return color.color() != Color.WHITE
    

def task_two(color):
    """
    This is the main execution loop for the task two state machine
    """
    state_machine = StateMachine()

    state_machine.add_state("Turn_Left", task_two_turn_left, default=True)\
                 .add_state("Turn_Right", task_two_turn_right)\
                 .add_transition("Turn_Left", "Turn_Right", task_two_on_tape_condition)\
                 .add_transition("Turn_Right", "Turn_Left", task_two_off_tape_condition)

    while True:
        state_machine.run()


# Task Three
def print_values_touch():
    """
    This function is used to print out the test values from the touch sensors
    """
    while True:
        print("Left Pressed: ", left_touch.pressed(), ", Right Pressed: ", right_touch.pressed())


def turn_90(dir):
    """
    This function makes the robot rotate 90 degrees left or right depending on the dir parameter
    """
    left_motor.reset_angle(0)
    right_motor.reset_angle(0)
    if dir == "left":
        left_motor.run_target(100, -188)
        right_motor.run_target(100, 188)
    if dir == "right":
        left_motor.run_target(100, 188)
        right_motor.run_target(100, -188)


def task_three_follow():
    """
    This function makes the robot move forward
    """
    left_motor.run(100)
    right_motor.run(100)


def task_three_turn():
    """
    This function makes the robot back away from the wall and turn parallel to it
    """
    left_motor.run(-100)
    right_motor.run(-100)
    time.sleep(3)
    turn_90("right")


def task_three_check():
    """
    This function makes the robot turn toward the wall and move to it
    """
    if left_motor.angle() > 200 and right_motor.angle() > 200:
        turn_90("left")
    left_motor.run(100)
    right_motor.run(100)


def task_three_check_condition():
    """
    This function checks if the robot has moved forward a certain distance
    """
    ROTS = 360 * 3
    return left_motor.angle() > ROTS and right_motor.angle() > ROTS


def task_three_wall_condition():
    """
    This function checks if either of the touch sensors are pressed
    """
    return left_touch.pressed() or right_touch.pressed()


def task_three_not_touch_condition():
    """
    This function checks if both of the touch sensors are not pressed
    """
    return not left_touch.pressed() and not right_touch.pressed()


def task_three():
    """
    This is the main execution loop for the task three state machine
    """
    state_machine = StateMachine()

    state_machine.add_state("Following_Wall", task_three_follow, default=True)\
                 .add_state("Turning_Corner", task_three_turn)\
                 .add_state("Checking_Wall", task_three_check)\
                 .add_state("Continuing", task_three_turn)\
                 .add_transition("Following_Wall", "Checking_Wall", task_three_check_condition)\
                 .add_transition("Following_Wall", "Turning Corner", task_three_wall_condition)\
                 .add_transition("Turning_Corner", "Following_Wall", task_three_not_touch_condition)\
                 .add_transition("Checking_Wall", "Continuing", task_three_wall_condition)\
                 .add_transition("Continuing", "Following_Wall", task_three_not_touch_condition)
    
    while True:
        state_machine.run()


# Define devices
ev3 = EV3Brick()

# ultrasonic = UltrasonicSensor(Port.S2)
# color = ColorSensor(Port.S3)
left_touch = TouchSensor(Port.S1)
right_touch = TouchSensor(Port.S4)

left_motor = Motor(Port.B)
right_motor = Motor(Port.C)


# Run the main program
while(True):
    # print_values_ultrasonic(ultrasonic)
    # task_one(left_motor, right_motor, ultrasonic)
    # print_values_color(color)
    # task_two(color)
    print_values_touch()
    # task_three()
