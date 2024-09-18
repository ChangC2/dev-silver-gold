<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$mms_version = isset($params['mms_version']) != '' ? $params['mms_version'] : '1.0';
$iot_version = isset($params['iot_version']) != '' ? $params['iot_version'] : '1.0';


$tableName = "arduino_firmware";
$query = "UPDATE $tableName SET `mms_version` = '$mms_version', `iot_version` = '$iot_version'";

$table->ExecuteQuery_Simple_NoResult($query);

echo $result_theme->MakeResult(true, $data);