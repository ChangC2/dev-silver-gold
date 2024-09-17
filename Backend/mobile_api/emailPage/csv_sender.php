<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');

require('fpdf.php');
include_once('mailSender.php');

function baseUrl()
{
    return str_replace("csv_sender.php", "", sprintf(
        "%s://%s%s",
        isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] != 'off' ? 'https' : 'http',
        $_SERVER['SERVER_NAME'],
        $_SERVER['REQUEST_URI']
    ));
}



$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$csvData = isset($params['csvData']) != '' ? $params['csvData'] : '';
$emailList = isset($params['emailList']) != '' ? $params['emailList'] : '';

$res = [];
// create jpeg from imageData
if ($csvData != '') {
    $date = new DateTime();
    $uniqName = "report_" . $date->getTimestamp();
    $csvFile = "csv_files/" . $uniqName . ".csv";
    $fp = fopen($csvFile, 'w');
    foreach ($csvData as $fields) {
        fputcsv($fp, $fields);
    }
    fclose($fp);
    // Send email
    $mailer = new mailSender();
    for ($k = 0; $k < count($emailList); $k++) {
        $mailer->SendEmail("Manual Report.", $csvFile, $emailList[$k], "Manual Report");
    }
    $res['emailList'] = $emailList;
    $res['result'] = true;
    $res['url'] = $csvFile;
} else {
    // echo false;
    $res['result'] = false;
}

echo json_encode($res);
