# -*- coding: utf-8 -*-
"""
Created on Fri Dec 27 14:09:26 2019

@author: zecch
"""

import cv2
import glob
import os
import os.path




name =["SebastienC"]
for nom in name:
    for filename in glob.glob('Images\\'+nom+'\\*.JPG'):
        
        img = cv2.imread(filename)
        
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
#
        blur = cv2.blur(img,(10,10))
# !!!!!!!!!! USE ctrl+w TO CLOSE WINDOW, if not, the program freeze
#        cv2.imshow('blur',blur)
#        cv2.waitKey()
#        cv2.destroyAllWindows()
        cv2.imwrite(os.path.join(path_to_save ,'blur' + os.path.basename(filename)),blur)