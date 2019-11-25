# Kicessah
Une application de traitement de l'information sur support type tablette

## Clone

```bash
git clone https://github.com/johannpistorius/Kicessah
```

## Computer Vision

**Description** : Traitement de l'image avec détection et reconnaissance automatique de visage  
**Langage de programmation** : Python  
**Bibliothèque** : Opencv  
[Référence](https://opencv-python-tutroals.readthedocs.io/en/latest/py_tutorials/py_objdetect/py_face_detection/py_face_detection.html#face-detection)  
 

## Android

TODO

## Network

l'adresse du serveur est 192.168.118.106

Pour traiter une image depuis la tablette , il faut envoyer une requete http post, avec un image dans un champs nommé 'fichier' à l'adresse 192.168.118.106/API.php
Pour tester avec un image importé, il faut aller sur l'adresse 192.168.118.106/Form.php

L'API enregistre l'image recu sous le nom 'picture_to_process.jpg' dans la racine, puis execute le programme nommé "test.py" qui se trouve aussi dans la racine (possibilité de changé l'arborcence en modifiant une variable du fichier API.php

Pour que le revoit soit correct, il faut que dans le programme python, il n'y ai qu'un print qui écrira le nom de la personne reconnue.