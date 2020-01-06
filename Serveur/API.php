<?php
ini_set("display_errors",1);
$pathDeLImage="picture_to_process.jpg";
$pathDuProgPython = "test.py";

/** Debug thibaud
if (isset ($_POST['text'])){
	echo $_POST['text'];
	$content = file_get_contents("log.txt");
	$date = new DateTime();
	$date = $date->format("d/m/y h:i:s");
	file_put_contents("log.txt",$content."\n".$date." : ".$_POST['text']);
}
**/

if(isset($_FILES['fichier'])){ //On execute le programme qui si il y a un fichier dans la variable 'fichier' de la requete post
	$file = $_FILES['fichier']['tmp_name'];
	if(!move_uploaded_file($file,$nomFichier)){ //on la copie dans le serveur au path indiqué
		echo "\nerreur d'upload";
	}
	else{
		//si l'opération est un succès, on execute le programme python
		$output = shell_exec("python ".$pathDuProgPython);
		//puis on affiche la réponse
		echo $output;
	}
}
else{
	echo "\n fichier non défini";
	die();
}
?>
