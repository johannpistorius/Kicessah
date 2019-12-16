<?php
ini_set("display_errors",1);
$nomFichier="picture_to_process.jpg";
$PathDuProgPython = "test.py";

if (isset ($_POST['text'])){
	echo $_POST['text'];
	$content = file_get_contents("log.txt");
	$date = new DateTime();
	$date = $date->format("d/m/y h:i:s");
	file_put_contents("log.txt",$content."\n".$date." : ".$_POST['text']);
	die();
}
/**

if(isset($_FILES['fichier'])){
	$file = $_FILES['fichier']['tmp_name'];
	if(!move_uploaded_file($file,$nomFichier)){
		echo "\nerreur d'upload";
	}
	else{

		$output = shell_exec("python ".$PathDuProgPython);
		echo $output;
	}
}
else{
	echo "\n fichier non défini";
	die();
}
**/
?>
