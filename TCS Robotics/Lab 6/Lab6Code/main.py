import cv2 as cv
import numpy as np

callBackImg = None
cX = 0
cY = 0


def dark_blue(orig):
    (height, width, channels) = orig.shape
    ret = np.zeros((height, width, 1))
    for i in range(height):
        for j in range(width):
            pixel = orig[i][j]
            if int(pixel[2]) == 63 and int(pixel[1]) == 72 and int(pixel[0]) == 204:
                ret[i][j] = 255
            else:
                ret[i][j] = 0
    return ret


def centroid(pic):
    (height, width, selected) = pic.shape
    num_pixels = 0
    avg_x = 0
    avg_y = 0
    for i in range(height):
        for j in range(width):
            pixel = pic[i][j]
            if pixel == 255:
                num_pixels += 1
                avg_x += j
                avg_y += i
    avg_x = int(avg_x / num_pixels)
    avg_y = int(avg_y / num_pixels)
    return avg_x, avg_y


def task1():
    global callBackImg
    orig = cv.imread(cv.samples.findFile("testImage1.png"))
    # callBackImg = dark_blue(orig)
    callBackImg = cv.inRange(orig, (204, 72, 63), (204, 72, 63))
    callBackImg = callBackImg.reshape(callBackImg.shape[0], callBackImg.shape[1], 1)
    (cx, cy) = centroid(callBackImg)
    copy = callBackImg.copy()
    cv.circle(copy, (cx, cy), 1, 0, 5)
    cv.imshow("Task 1", copy)

    while True:
        k = cv.waitKey(1)
        if k == ord('q'):
            break


def dilation(pic):
    copy = pic.copy()
    (height, width, selected) = copy.shape
    for i in range(height):
        for j in range(width):
            if copy[i][j] == 255:
                continue
            for y in range(-1, 2):
                for x in range(-1, 2):
                    if i+y < 0 or j+x < 0 or i+y >= height or j+x >= width:
                        continue
                    elif pic[i+y][j+x] == 255:
                        copy[i][j] = 255
                        continue
    return copy


def erosion(pic):
    copy = pic.copy()
    (height, width, selected) = copy.shape
    for i in range(height):
        for j in range(width):
            if pic[i][j] == 0:
                continue
            for y in range(-1, 2):
                for x in range(-1, 2):
                    if i + y < 0 or j + x < 0 or i + y >= height or j + x >= width:
                        continue
                    elif pic[i + y][j + x] == 0:
                        copy[i][j] = 0
                        continue
    return copy


def task2():
    global callBackImg
    # callBackImg = dilation(callBackImg)
    # callBackImg = erosion(callBackImg)

    kernel = np.ones((3, 3), np.uint8)
    callBackImg = cv.dilate(callBackImg, kernel, iterations=1).reshape(callBackImg.shape[0], callBackImg.shape[1], 1)
    callBackImg = cv.erode(callBackImg, kernel, iterations=1).reshape(callBackImg.shape[0], callBackImg.shape[1], 1)

    (cx, cy) = centroid(callBackImg)
    copy = callBackImg.copy()
    cv.circle(copy, (cx, cy), 1, 0, 5)
    cv.imshow("Task 2", copy)

    while True:
        k = cv.waitKey(1)
        if k == ord('q'):
            break


def convolution(pic, kernel):
    copy = pic.copy()
    (height, width, selected) = copy.shape
    for i in range(1, height-1):
        for j in range(1, width-1):
            total = 0
            for k in range(-1, 2):
                for f in range(-1, 2):
                    total += (kernel[k+1][f+1] * pic[i+k][j+f])
            copy[i][j] = total
    return copy


def laplacian(pic):
    kernel = np.array(([0, -1, 0],
                       [-1, 4, -1],
                       [0, -1, 0]), np.uint8)
    pic = convolution(pic, kernel)
    return pic


def sobel(pic, direction):
    kernel = []
    if direction == 0:  # Horizontal edges
        kernel = np.array(([1, 2, 1],
                           [0, 0, 0],
                           [-1, -2, -1]), np.uint8)
    elif direction == 1:  # Vertical edges
        kernel = np.array(([1, 0, -1],
                           [2, 0, -2],
                           [1, 0, -1]), np.uint8)
    pic = convolution(pic, kernel)
    return pic


def task3():
    global callBackImg
    # callBackImg = laplacian(callBackImg)
    # callBackImg = sobel(callBackImg, 0)
    # callBackImg = sobel(callBackImg, 1)

    callBackImg = cv.Laplacian(callBackImg, cv.CV_64F).reshape(callBackImg.shape[0], callBackImg.shape[1], 1)
    # callBackImg = cv.Sobel(callBackImg, cv.CV_64F, 0, 1, ksize=3).reshape(callBackImg.shape[0], callBackImg.shape[1], 1)
    # callBackImg = cv.Sobel(callBackImg, cv.CV_64F, 1, 0, ksize=3).reshape(callBackImg.shape[0], callBackImg.shape[1], 1)

    (cx, cy) = centroid(callBackImg)
    copy = callBackImg.copy()
    cv.circle(copy, (cx, cy), 1, 255, 5)
    cv.imshow("Task 3", copy)

    while True:
        k = cv.waitKey(1)
        if k == ord('q'):
            break


def task4():
    global callBackImg
    cap = cv.VideoCapture(0)
    if not cap.isOpened():
        print("Cannot open camera")
        exit()
    while True:
        # Capture frame-by-frame
        ret, callBackImg = cap.read()
        # if frame is read correctly ret is True
        if not ret:
            print("Can't receive frame (stream end?). Exiting ...")
            break

        copy = callBackImg.copy()
        copy = cv.inRange(copy, (0, 0, 120), (60, 60, 255))
        kernel = np.ones((3, 3), np.uint8)
        copy = cv.dilate(copy, kernel, iterations=1).reshape(copy.shape[0], copy.shape[1], 1)
        copy = cv.erode(copy, kernel, iterations=1).reshape(copy.shape[0], copy.shape[1], 1)
        (cx, cy) = centroid(copy)
        cv.circle(copy, (cx, cy), 1, 0, 5)

        cv.imshow("Task 4 Original", callBackImg)
        cv.imshow("Task 4 Blob", copy)

        k = cv.waitKey(1)
        if k == ord('q'):
            break

    # When everything done, release the capture
    cap.release()
    cv.destroyAllWindows()


def task5():
    global callBackImg
    cap = cv.VideoCapture(0)
    if not cap.isOpened():
        print("Cannot open camera")
        exit()
    while True:
        # Capture frame-by-frame
        ret, callBackImg = cap.read()
        # if frame is read correctly ret is True
        if not ret:
            print("Can't receive frame (stream end?). Exiting ...")
            break

        copy = callBackImg.copy()
        copy = cv.inRange(copy, (0, 0, 120), (60, 60, 255)).reshape(copy.shape[0], copy.shape[1], 1)
        copy = 255 - copy
        (numLabels, labels, stats, centroids) = cv.connectedComponentsWithStats(copy, 4, cv.CV_32S)

        (cx, cy) = centroids[0]
        if not np.isnan(cx) and not np.isnan(cy):
            cv.circle(copy, (int(cx), int(cy)), 1, 255, 5)

        cv.imshow("Task 5 Original", callBackImg)
        cv.imshow("Task 5 Blob", copy)

        k = cv.waitKey(1)
        if k == ord('q'):
            break

    # When everything done, release the capture
    cap.release()
    cv.destroyAllWindows()


if __name__ == '__main__':
    # task1()
    # task2()
    # task3()
    # task4()
    task5()
