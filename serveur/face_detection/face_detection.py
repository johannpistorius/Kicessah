# -*- coding: utf-8 -*-
"""
Created on Mon Nov 25 08:27:00 2019

@author: pistorij
"""

import numpy as np
import cv2

def face_roi(im):
    """
    Function that detects faces from an image
    
    Parameters
    ----------
    im : str
        image name
        
    Returns
    -------
    faces
        list of rectangles
    """
    img = cv2.imread('../'+im+'.jpg')
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)
    return faces