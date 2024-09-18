<?php

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');

$filename  = $_POST['filename'];
// Get requested params
$filename = $filename != null ? $filename : 'mms';

$myResult = array();
$myResult['status'] = false;
$urls = "";
if (isset($_FILES['file'])) {
	$file_name = $_FILES['file']['name'];
	$file_size = $_FILES['file']['size'];
	$file_tmp = $_FILES['file']['tmp_name'];
	$file_type = $_FILES['file']['type'];

	try {
		$fName = "$filename.bin";
		$url = "ota/firmwares/" . $fName;
		move_uploaded_file($file_tmp, $url);

		$myResult['status'] = true;
		$myResult['message'] = "Upload success";
	} catch (Exception $err) {
		$myResult['status'] = false;
		$myResult['message'] = "Upload Failed";
	}


	$myResult['status'] = true;
	$myResult['filename'] = $filename;
	$myResult['message'] = "Upload Success";
} else {
	$myResult['status'] = false;
	$myResult['message'] = "Upload Failed";
}

echo json_encode($myResult);
