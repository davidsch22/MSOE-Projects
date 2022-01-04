#!/usr/bin/env python3
import mediapipe as mp
import cv2
import numpy as np
from pybricks.messaging import BluetoothMailboxClient, TextMailbox

# SERVER = '24:71:89:4a:86:c4'  # DS
SERVER = '24:71:89:4f:02:f7'  # AR

mp_drawing = mp.solutions.drawing_utils
mp_hands = mp.solutions.hands


# Retrieves whether hand is left or right, confidence score, and the x
# and y coordinates of the wrist to know where to place the label.
def get_hand_info(index, hand, results):
    output = None
    for idx, classification in enumerate(results.multi_handedness):
        if classification.classification[0].index == index:
            # Process the detection results
            label = classification.classification[0].label
            score = classification.classification[0].score
            text = '{} {}'.format(label, round(score, 2))

            # Extract the x and y coordinates of the wrist
            coords = tuple(np.multiply(
                np.array((hand.landmark[mp_hands.HandLandmark.WRIST].x, hand.landmark[mp_hands.HandLandmark.WRIST].y)),
                [640, 480]).astype(int))

            output = text, coords
    return output


# Method to count the number of fingers on a hand. Uses the landmarks
# of the finger tips and compares its y-value to the y-value of a lower
# landmark to determine if the finger is open or closed.
def count_fingers(hand, totals):
    count = 0
    if len(hand.landmark) != 0:
        # if text[0] == "Right" and hand.landmark[4].x > hand.landmark[3].x:  # Right Thumb
        #    count = count + 1
        # elif text[0] == "Left" and hand.landmark[4].x < hand.landmark[3].x:  # Left Thumb
        #    count = count + 1
        if hand.landmark[8].y < hand.landmark[6].y:  # Index finger
            count = count + 1
        if hand.landmark[12].y < hand.landmark[10].y:  # Middle finger
            count = count + 1
        if hand.landmark[16].y < hand.landmark[14].y:  # Ring finger
            count = count + 1
        if hand.landmark[20].y < hand.landmark[18].y:  # Little finger
            count = count + 1
    totals.append(count)

    return totals


# Detects, displays confidence level, displays label, and retrieves the number
# of fingers for each hand. Then sends that data to the server.
def control(mbox):
    cap = cv2.VideoCapture(0)
    with mp_hands.Hands(min_detection_confidence=0.8, min_tracking_confidence=0.5) as hands:
        while cap.isOpened():
            ret, frame = cap.read()
            image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            # Flip on horizontal
            image = cv2.flip(image, 1)
            image.flags.writeable = False
            # Detections
            results = hands.process(image)
            # Set flag to true
            image.flags.writeable = True
            image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

            # Holds the total fingers for each hand
            totals = []
            # Displays the confidence level of the hand detection
            if results.multi_hand_landmarks:
                for num, hand in enumerate(results.multi_hand_landmarks):
                    mp_drawing.draw_landmarks(image, hand, mp_hands.HAND_CONNECTIONS,
                                              mp_drawing.DrawingSpec(color=(121, 22, 76), thickness=2, circle_radius=4),
                                              mp_drawing.DrawingSpec(color=(250, 44, 250), thickness=2,
                                                                     circle_radius=2))

                    # Displays the hand info, left or right hand, wrist coords, and then counts fingers
                    if get_hand_info(num, hand, results):
                        text, coord = get_hand_info(num, hand, results)
                        cv2.putText(image, text, coord, cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)
                    fingers = count_fingers(hand, totals)

                # Sends total fingers for both hands to server
                if len(fingers) == 2:
                    print(fingers)
                    left = str(fingers[0])
                    right = str(fingers[1])
                    msg = left + "," + right
                    mbox.send(msg)

            cv2.imshow('Hand Tracking', image)
            if cv2.waitKey(10) & 0xFF == ord('q'):
                break

    cap.release()
    cv2.destroyAllWindows()


# Creates a connection between the client and server, and
# then asks the user to enter a command.
def client():
    node = BluetoothMailboxClient()
    mbox = TextMailbox('greeting', node)
    print('establishing connection...')
    node.connect(SERVER)
    print('connected!')
    while 1:
        cmd = input("Enter a cmd (hands): ")
        if cmd == "hands":
            control(mbox)
        else:
            print("Unrecognized cmd: " + cmd)


if __name__ == '__main__':
    client()
