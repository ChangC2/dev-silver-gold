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
		$url = "pictures/" . $fName;

		upload_resized_image($file_tmp, $url);

		$compressedUrl = "pictures/" . $fName;

		$myResult['url'] = $compressedUrl;
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