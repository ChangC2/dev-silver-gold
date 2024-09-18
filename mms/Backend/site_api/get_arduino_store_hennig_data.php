<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

function filter($str)
{
    $resString = str_replace('"', '', $str);
    $resString = str_replace("'", '', $resString);
    return $resString;
}

$table = new TableMgr();
$result_theme = new Result_theme();
// Get requested params
$sensor_id = isset($_GET['sensor_id']) != '' ? $_GET['sensor_id'] : '';
$value1 = isset($_GET['data_1']) != '' ? $_GET['data_1'] : '0.0';
$value2 = isset($_GET['data_2']) != '' ? $_GET['data_2'] : '0.0';
$value3 = isset($_GET['data_3']) != '' ? $_GET['data_3'] : '0.0';
$value4 = isset($_GET['data_4']) != '' ? $_GET['data_4'] : '0.0';
$value5 = isset($_GET['data_5']) != '' ? $_GET['data_5'] : '0.0';
$value6 = isset($_GET['data_6']) != '' ? $_GET['data_6'] : '0.0';
$value7 = isset($_GET['data_7']) != '' ? $_GET['data_7'] : '0.0';
$value8 = isset($_GET['data_8']) != '' ? $_GET['data_8'] : '0.0';
$created_at = isset($_GET['created_at']) != '' ? $_GET['created_at'] : "";

$res = false;
if ($sensor_id != '') {
    $sql = "SELECT * FROM tbl_hennigs where `sensor_id`='$sensor_id'";
    $sensors = $table->ExecuteQuery_Simple($sql);
    $sensor_name = "";
    if ($sensors && count($sensors) > 0) {
        $sensor_name = $sensors[0]["sensor_name"];
    }
    $tableName = "hennig_data";
    date_default_timezone_set('America/Los_Angeles');
    $reading_time = date('Y-m-d H:i') . ":00";
    $sql = "SELECT * FROM $tableName where `sensor_id`='$sensor_id' ORDER BY reading_time DESC limit 1";
    $lastData = $table->ExecuteQuery_Simple($sql);
    if ($lastData && count($lastData) > 0 && $lastData[0]["reading_time"] < $reading_time) {
        $sql = "INSERT INTO `$tableName` (`sensor_id`, `sensor`, `value1`, `value2`, `value3`, `value4`, `value5`, `value6`, `value7`, `value8`, `reading_time`, `created_at`) VALUES (";
        $sql .= "'$sensor_id', '$sensor_name', '$value1', '$value2', '$value3', '$value4', '$value5', '$value6', '$value7', '$value8', '$reading_time', '$created_at')";
        $res = $table->ExecuteQuery_Simple_NoResult($sql);
    }
}
echo $result_theme->MakeResult($res, $sql);
