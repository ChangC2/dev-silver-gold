<?php

include_once("./common/upload_resized_image.php");

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');

$myResult = array();
$myResult['status'] = false;

if (isset($_FILES['file'])) {

	$file_name = $_FILES['file']['name'];
	$file_size = $_FILES['file']['size'];
	$file_tmp = $_FILES['file']['tmp_name'];
	$file_type = $_FILES['file']['type'];

	$myResult['file_tmp'] = $file_tmp;

	$time = date("YmdHsi");

	try {
		$fName = $time . "_" . $file_name;
		$url = "images/photo/" . $fName;

		upload_resized_image($file_tmp, $url);

		//move_uploaded_file($file_tmp, $url);

		$actual_link = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
		$actual_link = str_replace(basename(__FILE__, '.php') . ".php", "", $actual_link);
		$compressedUrl = $actual_link . "images/photo/" . $fName;

		$myResult['url'] = $compressedUrl;
		$myResult['actual_link'] = $actual_link;
		$myResult['status'] = true;
		$myResult['message'] = "Upload success";
	} catch (Exception $err) {
		$myResult['message'] = "$err";
	}
} else {
	$myResult['status'] = false;
	$myResult['message'] = "Not found picture.";
}

echo json_encode($myResult);
