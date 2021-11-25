import cv2
import numpy as np
import PIL
from PIL import Image
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans

#read in picture
path = '/home/ian/Downloads/burned_baby.png'
img = np.asarray(Image.open(path))
img = img[:,:,:3]
flat_img = img.reshape((-1, 3))

# K Means with 16 colors
clt = KMeans(16)
clt.fit(flat_img)
cluster_centers = clt.cluster_centers_
cluster_labels = clt.labels_

#mapping cluster center values to labels
k16 = cluster_centers[cluster_labels]
k16_norm = (cluster_centers[cluster_labels].reshape(img.shape))/255


# Plot image with 16 colors
# plt.ion()
plt.figure("K Means with 16")
plt.axis("off")
plt.title("K Means recreated image with K = 16")
plt.imshow(k16_norm)
# plt.ioff()
plt.show(block=False)

#Mask creation
mask_Set = []
mask_img =[]

for i in range(len(cluster_centers)):
    mask_Set.append(np.where(cluster_labels == i, 1,0).reshape((img.shape[0],img.shape[1])))
    mask = np.moveaxis([mask_Set[-1],mask_Set[-1],mask_Set[-1]],0,-1)
    mask_img.append(mask * img)

# #Plot individual masks
# plt.ion()
ro = 4
co = 4
fig, axs = plt.subplots(nrows=ro, ncols=co, figsize=(12, 10),
                        subplot_kw={'xticks': [], 'yticks': []})
for i in range((len(mask_img))):
    ax = axs[i//co, i%ro]
    ax.set_axis_off()
    ax.imshow(mask_img[i])
    ax.set_title('Mask ' + str(i))
plt.tight_layout(pad=0.4, w_pad=0.5, h_pad=1.0)
plt.show(block=False)

#user to define content of masked image
mask_sbo = np.zeros((3,img.shape[0],img.shape[1],img.shape[2]))
for i in range(len(cluster_centers)):
    sbo = int(input("Is mask "+str(i) +" 0-skin, 1-burn, 2-other"))
    mask_sbo[sbo] +=mask_img[i]

#plotting of skin, burn and other combined mask
names=['skin', 'burn','other']
for i in range(len(mask_sbo)):
    # plt.ion()
    plt.figure("mask" + names[i])
    plt.axis("off")
    plt.title("Mask " + names[i])
    plt.imshow(np.uint8(mask_sbo[i]))
    # plt.ioff()
    plt.show()

sbo_sum = []

#calculating % burn based on combined mask
for m in mask_sbo:
    sbo_sum.append(np.sum(np.nonzero(m))/3)
per_burn = sbo_sum[1]/(sbo_sum[0]+sbo_sum[1])*100
print('Percent Burn: '+str(per_burn))