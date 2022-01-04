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

# Reset the motor encoders to 0
left_motor.reset_angle(0)
right_motor.reset_angle(0)

# Run the motors for a set time, then stop
# SPEED = 102
SPEED = 511

left_motor.run(SPEED)
right_motor.run(SPEED)

time.sleep(360/SPEED)

left_motor.run(0)
right_motor.run(0)

time.sleep(2)

print(left_motor.angle())
print(right_motor.angle())
