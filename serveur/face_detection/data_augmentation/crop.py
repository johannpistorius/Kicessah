# -*- coding: utf-8 -*-
"""
Created on Thu Dec 12 14:34:20 2019

@author: pistorij
"""

import numpy as np
import cv2
import glob
import os
import os.path

def face_roi(img):
    """
    Function that detects faces from an image
    
    Parameters
    ----------
    img : Image
        picture to process
        
    Returns
    -------
    faces
        list of rectangles
    """
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)
    return faces

name = ["Johann","SebastienZ","Thibaud","Celia","Clement","Gennaro","Claire","Florian","Hugo","Gonzalo","Pablo","SebastienC"]
for nom in name:
    for filename in glob.glob('Images\\'+nom+'\\*.JPG'):
        
        
        img = cv2.imread(filename)
        #face detection
        
        print('filename = ' + filename)
        #give parent directory of the image
        path_filename = os.path.dirname(filename)
        print('path_filename = ' + path_filename)
        #give name of the current image
        nom_image = os.path.basename(filename)
        print('nom_image = ' + nom_image)
        #give complete path of the image
        path_to_save = os.getcwd() + "/" + path_filename
        print('path_to_save = ' + path_to_save)
        
        
        face_location = face_roi(img)
        
        try:
            cv2.imwrite(os.path.join(path_to_save ,'crop' + os.path.basename(filename)),img[face_location[0][1]:face_location[0][1]+face_location[0][3],face_location[0][0]:face_location[0][0]+face_location[0][2],:])
        except:
            continue
        