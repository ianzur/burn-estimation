# color quantization 

import argparse
from pathlib import Path

import numpy as np
import cv2 as cv


def main(args):
    img = cv.imread(str(args.image))
    img = cv.resize(img, (640, 480))
    img = cv.cvtColor(img, cv.COLOR_BGR2Lab)

      
    
    Z = img.reshape((-1,3))
    # convert to np.float32
    Z = np.float32(Z)
    # define criteria, number of clusters(K) and apply kmeans()
    criteria = (cv.TERM_CRITERIA_EPS + cv.TERM_CRITERIA_MAX_ITER, 10, 1.0)
    K = 8
    ret,label,center=cv.kmeans(Z,K,None,criteria,10,cv.KMEANS_RANDOM_CENTERS)
    # Now convert back into uint8, and make original image
    center = np.uint8(center)
    res = center[label.flatten()]
    res2 = res.reshape((img.shape))
    cv.imshow('res2',cv.cvtColor(res2, cv.COLOR_Lab2RGB))
    cv.waitKey(0)
    cv.destroyAllWindows()

if __name__ == "__main__":
    
    parser = argparse.ArgumentParser(description='Code for Thresholding Operations using inRange tutorial.')
    parser.add_argument('--image', help="path to image file", type=Path)
    args = parser.parse_args()
    main(args)