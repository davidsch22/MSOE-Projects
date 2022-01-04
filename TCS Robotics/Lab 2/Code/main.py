#!/usr/bin/env pybricks-micropython

from pybricks.hubs import EV3Brick
from pybricks.ev3devices import Motor
from pybricks.parameters import Port
from math import degrees, radians, atan2, sin, cos, acos
import time

ev3 = EV3Brick()

shoulder = Motor(Port.A)
elbow = Motor(Port.B)
pen = Motor(Port.C)

SPEED = 50
A1 = 13.5
A2 = 10.5


def pen_down():
    pen.run(70)
    time.sleep(2)


def pen_up():
    pen.run_time(-60, 1000)


def task1(q1, q2):
    # shoulder.reset_angle(90)
    # elbow.reset_angle(0)

    shoulder.run_target(SPEED, q1)
    elbow.run_target(SPEED, q2)

    # print(shoulder.angle())
    # print(elbow.angle())


def task2(x, y):
    pq2 = degrees(acos(((x**2)+(y**2)-(A1**2)-(A2**2))/(2*A1*A2)))
    pq1 = degrees(atan2(y,x)-atan2((A2*sin(radians(pq2))),(A1+(A2*cos(radians(pq2))))))

    nq2 = -pq2
    nq1 = degrees(atan2(y,x)-atan2((A2*sin(radians(nq2))),(A1+(A2*cos(radians(nq2))))))
    
    # print('q1: ', pq1)
    # print('Positive q2: ', pq2)
    # print()
    # print('q1: ', nq1)
    # print('Negative q2: ', nq2)

    task1(pq1, pq2)


def task3(shape):
    # Comment out angle reset in task1() if running this method
    shoulder.reset_angle(90)
    elbow.reset_angle(0)

    if shape == "triangle":
        task2(-5, 20)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()

        task2(-7.5, 15)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()

        task2(-2.5, 15)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()
    elif shape == "square":
        task2(-2.5, 7.5)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()

        task2(-2.5, 12.5)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()

        task2(-7.5, 12.5)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()

        task2(-7.5, 7.5)
        pen_down()
        elbow.run_time(20, 500)
        pen_up()


def passed(x, y, x1, y1, x2, y2):
    if x2-x == 0 and y2-y == 0:
        return True
    elif x2-x == 0:
        ydir = (y2-y1)/abs(y2-y1)
        ydirStep = (y2-y)/abs(y2-y)
        if ydirStep == ydir:
            return False
        else:
            return True
    elif y2-y == 0:
        xdir = (x2-x1)/abs(x2-x1)
        xdirStep = (x2-x)/abs(x2-x)
        if xdirStep == xdir:
            return False
        else:
            return True
    else:
        xdir = (x2-x1)/abs(x2-x1)
        xdirStep = (x2-x)/abs(x2-x)
        ydir = (y2-y1)/abs(y2-y1)
        ydirStep = (y2-y)/abs(y2-y)
        if xdirStep != xdir and ydirStep != ydir:
            return True
        else:
            return False


def task4(x1, y1, x2, y2):
    # Comment out angle reset in task1() if running this method
    # shoulder.reset_angle(90)
    # elbow.reset_angle(0)

    x = x1
    y = y1

    task2(x, y)
    pen_down()

    xstep = (x2-x1) / 10
    ystep = (y2-y1) / 10
    x += xstep
    y += ystep

    while not passed(x, y, x1, y1, x2, y2):
        task2(x, y)
        x += xstep
        y += ystep

    task2(x, y)
    pen_up()


def task5(shape):
    # Comment out angle reset in task4() if running this method
    shoulder.reset_angle(90)
    elbow.reset_angle(0)

    if shape == "triangle":
        x1 = -7.5
        y1 = 20
        x2 = -15
        y2 = 10
        x3 = 0
        y3 = 10
        
        task4(x1, y1, x2, y2)
        task4(x2, y2, x3, y3)
        task4(x3, y3, x1, y1)

    if shape == "square":
        x1 = 0
        y1 = 15
        x2 = 0
        y2 = 7.5
        x3 = -15
        y3 = 7.5
        x4 = -15
        y4 = 15
        
        task4(x1, y1, x2, y2)
        task4(x2, y2, x3, y3)
        task4(x3, y3, x4, y4)
        task4(x4, y4, x1, y1)


# task1(135, -20)
# task1(100, 90)
# task2(-13.98, 19.06)
# task2(-12.68, 11.47)
# task3("triangle")
# task3("square")
# task4(-7.5, 20, -7.5, 2.5)
# task4(-17.5, 12.5, 0, 12.5)
# task4(0, 17.5, -17.5, 0)
# task5("triangle")
task5("square")
