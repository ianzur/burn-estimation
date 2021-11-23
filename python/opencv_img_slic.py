# test SLIC algorithms
# ianzur

import argparse
from pathlib import Path

import cv2 as cv

region_size = 10
ruler = 10.0
algo = 0

window_input_name = "input"
window_detection_name = "result"

region_tb_name = "region"
ruler_tb_name = "ruler"
algo_tb_name = "algo: [0=SLIC, 1=SLICO, 2=MSLIC}"

algos = [cv.ximgproc.SLIC, cv.ximgproc.SLICO, cv.ximgproc.MSLIC]


def region_trackbar(val):
    global region_size
    region_size = int(val)
    cv.setTrackbarPos(region_tb_name, window_detection_name, region_size)  

def ruler_trackbar(val):
    global ruler
    ruler = val
    cv.setTrackbarPos(ruler_tb_name, window_detection_name, ruler)
    
def algo_trackbar(val):
    global algo
    algo = int(val)
    cv.setTrackbarPos(algo_tb_name, window_detection_name, algo)

l_down = False
super_pixel_labels = None
res = None

def onClick(event, x, y, flags, param):
    global l_down, super_pixel_labels, res
    
    if event ==cv.EVENT_LBUTTONDOWN:
        l_down = True
        thisSuperPixel = super_pixel_labels[x][y]
        print(thisSuperPixel)
    elif event == cv.EVENT_MOUSEMOVE:
        if l_down == True:
            thisSuperPixel = super_pixel_labels[x][y]
            print(thisSuperPixel)
    if event ==cv.EVENT_LBUTTONUP:
        l_down = False
    
def main():
    global super_pixel_labels
    global res

      
    img = cv.imread(str(args.image))
    
    # gaussian blur with a small 3 x 3 kernel and additional conversion into CieLAB color space.
    img = cv.GaussianBlur(img, (3,3), 0)
    img = cv.cvtColor(img, cv.COLOR_BGR2Lab)
    
    # create view windows    
    cv.namedWindow(window_input_name)
    cv.namedWindow(window_detection_name)
    
    cv.setMouseCallback(window_detection_name, onClick)

    
    frame = cv.resize(img, (640, 480))

    super_pixels = cv.ximgproc.createSuperpixelSLIC(frame, algos[2], region_size, ruler)
    super_pixels.iterate(100)

    # the label (which superpixel do each pixel belong to)
    # shape == 
    super_pixel_labels = super_pixels.getLabels()
            
    frame = cv.cvtColor(frame, cv.COLOR_Lab2RGB)
    
    mask = super_pixels.getLabelContourMask()
    
    points = []
    
    while True:
                          
        res = cv.bitwise_and(frame,frame,mask=~mask)
        
        
        cv.imshow(window_input_name, frame )
        cv.imshow(window_detection_name, res)
        
        key = cv.waitKey(30)
        if key == ord('q') or key == 27:
            break

if __name__ == "__main__":
    
    parser = argparse.ArgumentParser(description='Code for Thresholding Operations using inRange tutorial.')
    parser.add_argument('--image', help="path to image file", type=Path)
    args = parser.parse_args()
    main()