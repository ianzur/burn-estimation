# current mask fails to segment only the burn

import cv2
import numpy as np
import PIL
from PIL import Image
import matplotlib.pyplot as plt

from sklearn.cluster import KMeans


#read in picture
path = '/home/ian/Documents/UNT/wearables/burn recognition/segmentation.jpg'
img = np.asarray(Image.open(path))
img = img[:,:,:3]
flat_img = img.reshape((-1, 3))

# 1st Stage K Means with 64 colors
clt = KMeans(64)
clt.fit(flat_img)
cluster_centers = clt.cluster_centers_
cluster_labels = clt.labels_

k64 = cluster_centers[cluster_labels]
k64_norm = (cluster_centers[cluster_labels].reshape(img.shape))/255

# Plot image with 64 colors
plt.figure("64")
plt.axis("off")
plt.title("K = 64, Step = 1")
plt.imshow(k64_norm)
plt.show()

# 2nd Stage K Means with 16 colors
clt = KMeans(16)
clt.fit(k64)
cluster_centers = clt.cluster_centers_
cluster_labels = clt.labels_

k16 = cluster_centers[cluster_labels]
k16_norm = (cluster_centers[cluster_labels].reshape(img.shape))/255


# Plot image with 16 colors
plt.figure("16")
plt.axis("off")
plt.title("K = 16, Step = 2")
plt.imshow(k16_norm)
plt.show()

mask_1 = cluster_centers[6,:]
k16_r = np.copy(k16)
k16_r = np.where((mask_1 == k16), 0, k16_r)

k16_r = (k16_r.reshape(img.shape))/255
# Plot masked image
plt.figure("mask")
plt.axis("off")
plt.title("mask of n=16 step =2 image")
plt.imshow(k16_r)
plt.show()
