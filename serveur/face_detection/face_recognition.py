# -*- coding: utf-8 -*-
"""
Created on Mon Nov 25 08:27:44 2019

@author: pistorij
"""

import numpy as np
import cv2

def face_conv(img,face_location):
    """
    Function that recognizes faces from database
    
    Parameters
    ----------
    img : Image
        picture to process
    face_location : str
        location of face on image
        
    Returns
    -------
    name
        name of detected person
    """
    for(x,y,w,h) in face_location:
        #TODO    
    return name