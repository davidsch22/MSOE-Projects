#!/usr/bin/env pybricks-micropython
from pybricks.hubs import EV3Brick
from pybricks.ev3devices import Motor
from pybricks.parameters import Port
import time

# Initialize the EV3 Brick
ev3 = EV3Brick()

# Initialize motors at ports B and C
left_motor = Motor(Port.B)
right_motor = Motor(Port.C)

# Run the motors for a set time, then stop
left_motor.run(37.584)
right_motor.run(-37.584)

time.sleep(5)

left_motor.run(0)
right_motor.run(0)
