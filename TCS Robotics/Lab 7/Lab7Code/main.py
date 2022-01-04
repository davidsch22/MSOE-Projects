from typing import List, Tuple

import cv2 as cv
from math import pow, sqrt
from pqueue import PriorityQueue

callBackImg = None


def print_rgb(event, x, y, flags, params):
    (height, width, channels) = callBackImg.shape
    # EVENT_MOUSEMOVE
    if event == cv.EVENT_LBUTTONDOWN:
        if channels == 3:
            print(str(x)+" "+str(y)+" "+str(callBackImg[y][x][2])+" "+str(callBackImg[y][x][1])+" "+str(callBackImg[y][x][0]))
        else:
            print(str(x) + " " + str(y) + " " + str(callBackImg[y][x][0]))


def show_image(images):
    global callBackImg
    for i in range(len(images)):
        if i == 0:
            callBackImg = images[i]
        cv.imshow("Image "+str(i), images[i])
        cv.setMouseCallback("Image "+str(i), print_rgb)
    while True:
        k = cv.waitKey(0)
        if k == ord("q"):
            break
    cv.destroyAllWindows()


def get_neighbors(img, p):
    neighbors = []
    if p[0] - 1 >= 0:
        if not img[p[0] - 1][p[1]]:
            neighbors.append((p[0] - 1, p[1]))
    if p[0] + 1 < len(img):
        if not img[p[0] + 1][p[1]]:
            neighbors.append((p[0] + 1, p[1]))
    if p[1] - 1 >= 0:
        if not img[p[0]][p[1] - 1]:
            neighbors.append((p[0], p[1] - 1))
    if p[1] + 1 < len(img[0]):
        if not img[p[0]][p[1] + 1]:
            neighbors.append((p[0], p[1] + 1))
    return neighbors


def dist(p1, p2):
    return sqrt(pow(p2[1]-p1[1], 2)+pow(p2[0]-p1[0], 2))


# Assume start and goal are tuples of (y,x)
def astar(img, start, goal):
    f = PriorityQueue()
    f.put([start], 0)
    visited = []

    while not f.empty():
        test_path = f.get()
        nk = test_path[-1]
        if nk not in visited:
            visited.append(nk)

            if nk[0] == goal[0] and nk[1] == goal[1]:
                return test_path

            new_paths: List[Tuple[List[Tuple[int, int]], float]] = []
            for n in get_neighbors(img, nk):
                if n not in test_path:
                    p = (len(test_path)-1) + dist(nk, goal)  # c(ns->nk) + h(nk)
                    new_paths.append((test_path+[n], p))
            f.put_many(new_paths)
    return []


def find_blob(orig):
    (height, width, channels) = orig.shape
    lower_range = (254, 254, 254)
    upper_range = (256, 256, 256)
    mask = cv.inRange(orig, lower_range, upper_range)
    mask = mask.reshape(height, width, 1)
    return mask


def find_red_blob(orig):
    (height, width, channels) = orig.shape
    lower_range = (0, 0, 254)
    upper_range = (0, 0, 256)
    mask = cv.inRange(orig, lower_range, upper_range)
    mask = mask.reshape(height, width, 1)
    return mask


def find_start_stop(orig):
    (height, width, channels) = orig.shape
    start_lower_range = (195, 113, 67)
    start_upper_range = (197, 115, 69)
    start_mask = cv.inRange(orig, start_lower_range, start_upper_range)
    start_mask = start_mask.reshape(height, width, 1)
    start_mask = 255 - start_mask
    (numLabels, labels, stats, centroids) = cv.connectedComponentsWithStats(start_mask, 4, cv.CV_32S)
    start = (int(centroids[0][1]), int(centroids[0][0]))

    goal_lower_range = (70, 172, 111)
    goal_upper_range = (72, 174, 113)
    goal_mask = cv.inRange(orig, goal_lower_range, goal_upper_range)
    goal_mask = goal_mask.reshape(height, width, 1)
    goal_mask = 255 - goal_mask
    (numLabels, labels, stats, centroids) = cv.connectedComponentsWithStats(goal_mask, 4, cv.CV_32S)
    goal = (int(centroids[0][1]), int(centroids[0][0]))
    return [start, goal]


def draw_path(img, path):
    for p in path:
        cv.circle(img, (p[1], p[0]), 5, 125, -1)


def test_image1():
    orig = cv.imread(cv.samples.findFile("testImage1.png"))
    thres = find_blob(orig)
    # note for the start and goal,
    # the coordinates are backwards
    # y is [0] and x is [1]
    start = (27, 26)
    goal = (4, 3)
    path = astar(thres, start, goal)
    return [thres, path]


def test_image2():
    orig = cv.imread(cv.samples.findFile("testImage2.png"))
    thres = find_blob(orig)
    # note for the start and goal,
    # the coordinates are backwards
    # y is [0] and x is [1]
    start = (30, 25)
    goal = (2, 5)
    path = astar(thres, start, goal)
    return [thres, path]


# Example with a larger image
def test_image3():
    orig = cv.imread(cv.samples.findFile("testImage3.png"))
    (height, width, channels) = orig.shape

    # We have to resize here or else the astar will take forever
    scale = 10
    resize = cv.resize(orig, (int(width / scale), int(height / scale)))
    thres = find_blob(resize)
    # note for the start and goal,
    # the coordinates are backwards
    # y is [0] and x is [1]
    start = (30, 50)
    goal = (4, 10)

    path = astar(thres, start, goal)
    return [thres, path]


def task2():
    orig = cv.imread(cv.samples.findFile("task2.png"))
    (height, width, channels) = orig.shape

    # We have to resize here or else the astar will take forever
    scale = 10
    resize = cv.resize(orig, (int(width / scale), int(height / scale)))
    thres = find_red_blob(resize)
    # note for the start and goal,
    # the coordinates are backwards
    # y is [0] and x is [1]
    [start, goal] = find_start_stop(resize)

    path = astar(thres, start, goal)
    return [thres, path]


def task3():
    orig = cv.imread(cv.samples.findFile("task3.png"))
    (height, width, channels) = orig.shape

    # We have to resize here or else the astar will take forever
    scale = 10
    resize = cv.resize(orig, (int(width / scale), int(height / scale)))
    thres = find_blob(resize)
    # note for the start and goal,
    # the coordinates are backwards
    # y is [0] and x is [1]
    [start, goal] = find_start_stop(resize)

    path = astar(thres, start, goal)
    return [thres, path]


def main():
    # [img, path] = test_image1()
    # [img, path] = test_image2()
    # [img, path] = test_image3()
    # [img, path] = task2()
    [img, path] = task3()

    # We are assuming we are doing astar on smaller
    # images. We then have to resize them and the
    # paths back up
    (height, width, channels) = img.shape
    scale = 10
    resize = cv.resize(img, (width*scale, height*scale))
    (height, width) = resize.shape
    resize = resize.reshape(height, width, 1)
    rPath = []
    for p in path:
        rPath.append([p[0]*scale, p[1]*scale])

    print(rPath)

    draw_path(resize, rPath)
    show_image([resize])


if __name__ == '__main__':
    main()
