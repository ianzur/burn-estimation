# color quantization 

import argparse
from pathlib import Path

import numpy as np
import cv2 as cv

def doKmeans(img, K: int):
    
    Z = img.reshape((-1,3))
    # convert to np.float32
    Z = np.float32(Z)

    criteria = (cv.TERM_CRITERIA_EPS + cv.TERM_CRITERIA_MAX_ITER, 10, 1.0)
    ret,label,center=cv.kmeans(Z,K,None,criteria,10,cv.KMEANS_RANDOM_CENTERS)
    
    # Now convert back into uint8, and make original image
    center = np.uint8(center)
    res = center[label.flatten()]
    
    return res.reshape((img.shape))


def main(args):
    img = cv.imread(str(args.image))
    img = cv.resize(img, (640, 480))
    img = cv.cvtColor(img, cv.COLOR_BGR2Lab)
    
    img1 = doKmeans(img, 64)

    cv.imshow('step_1',cv.cvtColor(img1, cv.COLOR_Lab2RGB))
    
    img2 = doKmeans(img1, 16)
    
    cv.imshow('step_2',cv.cvtColor(img2, cv.COLOR_Lab2RGB))
        
    cv.waitKey(0)
    cv.destroyAllWindows()

if __name__ == "__main__":
    
    parser = argparse.ArgumentParser(description='Code for Thresholding Operations using inRange tutorial.')
    parser.add_argument('--image', help="path to image file", type=Path)
    args = parser.parse_args()
    main(args)