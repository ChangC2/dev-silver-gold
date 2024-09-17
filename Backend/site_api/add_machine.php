<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$id = isset($params['id']) != '' ? $params['id'] : '';
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_picture_url = isset($params['machine_picture_url']) != '' ? $params['machine_picture_url'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$device_id = isset($params['device_id']) != '' ? $params['device_id'] : '';
$machine_type = isset($params['machine_type']) != '' ? $params['machine_type'] : 0; // 0: Android, 1: ESP, 2 RASP

date_default_timezone_set('UTC');
$timestamp = time();
$created_at = date("Y-m-d h:i:s", $timestamp);
if ($machine_id != '') {
    $tableName = $customer_id . "_status";
    $machineList  = $table->ReadTable($tableName);
    $order = count($machineList) + 1;
    $query = "INSERT INTO `$tableName` (`id`,`machine_id`, `machine_picture_url`, `created_at`, `time_stamp`,`time_stamp_ms`, `order`)";
    $query .= " VALUES ('$id','$machine_id', '$machine_picture_url', '$created_at','$timestamp','$timestamp','$order')";
    $table->ExecuteQuery_Simple_NoResult($query);

    if ($machine_type == 1){
        $status = "Offline";
        $color = "#000000";
        $start = time();
        $end = $start;
        $created_at = date("Y-m-d H:i:s");
        $interface = "ESP32(3.9.28)";

        $sql = "SELECT * FROM `buffer_ganttdata` where `customer_id`='$factory_id' AND machine_id == '$machine_id'";
        $buffer_ganttdata = $table->ExecuteQuery_Simple($sql);
        if (count($buffer_ganttdata) == 0) {
            $query = "INSERT INTO `buffer_ganttdata` (`customer_id`, `machine_id`, `status`, `color`, `start`, `end`, `time_stamp`, `created_at`, `interface`)";
            $query .= " VALUES ('$customer_id','$machine_id', '$status', '$color', '$start', '$end','$created_at','$created_at', 'ESP32(3.9.28)')";
            $table->ExecuteQuery_Simple_NoResult($query);
        }
    }

    echo $result_theme->MakeResult(true, $order);
} else {
    echo $result_theme->MakeResult(false, "");
}
