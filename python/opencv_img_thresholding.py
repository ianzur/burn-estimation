# original example from: https://docs.opencv.org/3.4/da/d97/tutorial_threshold_inRange.html
# 
# Added:
#   - more color spaces (specify via command line arg)
# 
# python3 opencv_img_thresholding.py --image "/home/ian/Documents/UNT/wearables/burn recognition/segmentation.jpg" --color_space lab
# 
# modify by: ianzur
# 

import argparse
from pathlib import Path

import cv2 as cv

window_capture_name = 'input'
window_detection_name = 'mask'

low_a_name = ""
low_b_name = ""
low_c_name = ""
high_a_name = ""
high_b_name = ""
high_c_name = ""

def a_low_trackbar(val):
    global low_a
    global high_a
    low_a = val
    low_a = min(high_a-1, low_a)
    cv.setTrackbarPos(low_a_name, window_detection_name, low_a)

def a_high_trackbar(val):
    global low_a
    global high_a
    high_a = val
    high_a = max(high_a, low_a+1)
    cv.setTrackbarPos(high_a_name, window_detection_name, high_a)

def b_low_trackbar(val):
    global low_b
    global high_b
    low_b = val
    low_b = min(high_b-1, low_b)
    cv.setTrackbarPos(low_b_name, window_detection_name, low_b)
    
def b_high_trackbar(val):
    global low_b
    global high_b
    high_b = val
    high_b = max(high_b, low_b+1)
    cv.setTrackbarPos(high_b_name, window_detection_name, high_b)
    
def c_low_trackbar(val):
    global low_c
    global high_c
    low_c = val
    low_c = min(high_c-1, low_c)
    cv.setTrackbarPos(low_c_name, window_detection_name, low_c)
    
def c_high_trackbar(val):
    global low_c
    global high_c
    high_c = val
    high_c = max(high_c, low_c+1)
    cv.setTrackbarPos(high_c_name, window_detection_name, high_c)



def color_space_limits(space: str):
    global low_a
    global low_b
    global low_c
    global high_a
    global high_b
    global high_c
    
    global low_a_name
    global low_b_name
    global low_c_name
    global high_a_name
    global high_b_name
    global high_c_name
    
    max_value = 255

    low_a = 0
    low_b = 0
    low_c = 0
    high_a = max_value
    high_b = max_value
    high_c = max_value


    space = space.lower()
    
    if space == "hsv":
        channels = ["H","S","V"]
        high_a = 360//2
    elif space == "rgb":
        channels = ["B", "G", "R"]
    elif space == "lab":
        channels = ["L", "a", "b"]
    elif space == "luv":
        channels = ["L", "a", "b"]
    elif space == "ycrcb":
        channels = ["Y", "Cr", "Cb"]
    elif space == "xyz":
        channels = ["x", "y", "z"]
    elif space == "yuv":
        channels = ["Y", "U", "V"]

    low_a_name, low_b_name, low_c_name = [(" ").join(["Low", chn]) for chn in channels]
    high_a_name, high_b_name, high_c_name = [(" ").join(["High", chn]) for chn in channels]
        

def main(args):

    img = cv.imread(str(args.image))
    
    color_space = args.color_space.lower()
    
    if color_space == "hsv":
        img = cv.cvtColor(img, cv.COLOR_BGR2HSV)
    elif color_space == "rgb":
        pass
    elif color_space == "lab":
        img = cv.cvtColor(img, cv.COLOR_BGR2LAB)
    elif color_space == "luv":
        img = cv.cvtColor(img, cv.COLOR_BGR2LUV)
    elif color_space == "ycrcb":
        img = cv.cvtColor(img, cv.COLOR_BGR2YCrCb)
    elif color_space == "xyz":
        img = cv.cvtColor(img, cv.COLOR_BGR2XYZ)
    elif color_space == "yuv":
        img = cv.cvtColor(img, cv.COLOR_BGR2YUV)
        
        
    color_space_limits(color_space)
    
    # create view windows    
    cv.namedWindow(window_capture_name)
    cv.namedWindow(window_detection_name)
    
    # controlling track bars
    cv.createTrackbar(low_a_name, window_detection_name , low_a, high_a, a_low_trackbar)
    cv.createTrackbar(high_a_name, window_detection_name , high_a, high_a, a_high_trackbar)
    cv.createTrackbar(low_b_name, window_detection_name , low_b, high_b, b_low_trackbar)
    cv.createTrackbar(high_b_name, window_detection_name , high_b, high_b, b_high_trackbar)
    cv.createTrackbar(low_c_name, window_detection_name , low_c, high_c, c_low_trackbar)
    cv.createTrackbar(high_c_name, window_detection_name , high_c, high_c, c_high_trackbar)

    while True:
        
        frame = img
        
        frame = cv.resize(frame, (640, 480))
                   
        frame_threshold = cv.inRange(frame, (low_a, low_b, low_c), (high_a, high_b, high_c))
        
        cv.imshow(window_capture_name, frame)
        cv.imshow(window_detection_name, frame_threshold)
        
        key = cv.waitKey(300)
        if key == ord('q') or key == 27:
            break
    
    


if __name__ == "__main__":
    
    parser = argparse.ArgumentParser(description='Code for Thresholding Operations using inRange tutorial.')
    parser.add_argument('--image', help="path to image file", type=Path)
    parser.add_argument('--color_space', help="color_space", default='HSV', type=str)
    args = parser.parse_args()

    main(args)
    




