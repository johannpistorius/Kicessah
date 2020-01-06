# -*- coding: utf-8 -*-
"""
Created on Mon Nov 25 08:24:41 2019

@author: pistorij
"""
import cv2
import face_detection as fd
import face_recognition as fr

img = cv2.imread('../picture_to_process.jpg')

#face detection
face_location = fd.face_roi(img)

#face recognition
name = fr.face_conv(img,face_location)

print(name)