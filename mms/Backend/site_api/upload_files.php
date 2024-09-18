<?php

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');

$myResult = array();
$myResult['status'] = false;
$urls = "";
if (isset($_FILES['files'])) {
	$upload_dir = 'files/';
    if(!empty(array_filter($_FILES['files']['name']))) {
        foreach ($_FILES['files']['tmp_name'] as $key => $value) {
            $file_tmpname = $_FILES['files']['tmp_name'][$key];
            $file_name = $_FILES['files']['name'][$key];
			$filepath = $upload_dir.time().'_'.$file_name;
			move_uploaded_file($file_tmpname, $filepath);
			$urls = $urls == "" ? time().'_'.$file_name : $urls.":".time().'_'.$file_name;
        }
	}
	$myResult['urls'] = $urls;
	$myResult['status'] = true;
	$myResult['files'] = $_FILES['files'];
	$myResult['message'] = "Upload success";
} else {
	$myResult['status'] = false;
	$myResult['files'] = $_FILES['files'];
	$myResult['message'] = "Not found picture.";
}

echo json_encode($myResult);
