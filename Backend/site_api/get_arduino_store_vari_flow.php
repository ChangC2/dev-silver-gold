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

$system_status = isset($_GET['system_status']) != '' ? $_GET['system_status'] : "";
$set_pressure = isset($_GET['set_pressure']) != '' ? $_GET['set_pressure'] : "";
$actual_pressure = isset($_GET['actual_pressure']) != '' ? $_GET['actual_pressure'] : "";
$hz = isset($_GET['hz']) != '' ? $_GET['hz'] : "";
$run_time = isset($_GET['run_time']) != '' ? $_GET['run_time'] : "";
$alarm = isset($_GET['alarm']) != '' ? $_GET['alarm'] : "";
$lowTankAlarm = isset($_GET['lowTankAlarm']) != '' ? $_GET['lowTankAlarm'] : "";
date_default_timezone_set('America/Los_Angeles');

$tableName = "vari_flow";

date_default_timezone_set('America/Los_Angeles');
$created_at = date('Y-m-d H:i') . ":00";
$sql = "SELECT * FROM $tableName where ORDER BY created_at DESC limit 1";
$lastData = $table->ExecuteQuery_Simple($sql);
if ($lastData && count($lastData) > 0 && $lastData[0]["created_at"] < $created_at) {
    $sql = "INSERT INTO `$tableName` (`system_status`, `set_pressure`, `actual_pressure`, `hz`, `run_time`, `alarm`, `low_tank_alarm`, `time_stamp`, `created_at`) VALUES (";
    $sql .= "'$system_status', '$set_pressure', '$actual_pressure', '$hz', '$run_time', '$alarm', '$lowTankAlarm', '$time_stamp', '$created_at')";
    $res = $table->ExecuteQuery_Simple_NoResult($sql);
}
echo $result_theme->MakeResult($res, $sql);