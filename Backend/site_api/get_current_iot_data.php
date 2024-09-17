<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$device_id = isset($params['device_id']) != '' ? $params['device_id'] : '';

$res = array();
$sql = "SELECT * FROM `tbl_app_setting` WHERE `device_id`='$device_id'";
$appSettings = $table->ExecuteQuery_Simple($sql);
if (count($appSettings) > 0) {
    $customer_id = $appSettings[0]["factory_id"];
    $tableName = $customer_id . '_iotdata';
    $sql = "SELECT * FROM $tableName where `sensor_id`='$device_id' ORDER BY created_at DESC LIMIT 1 ";
    $iotDatas = $table->ExecuteQuery_Simple($sql);
    if (count($iotDatas) > 0) {
        $input_signals = $iotDatas[0];
        $current_time = time() * 1000;
        if ($current_time / 1000 - (int)$input_signals["created_at"] > 60 * 5) {
            $input_signals["value1"] = 0;
            $input_signals["value2"] = 0;
            $input_signals["value3"] = 0;
            $input_signals["value4"] = 0;
        }
        $res["input_values"] = $input_signals;
        echo $result_theme->MakeResult(true, $res);
    } else {
        echo $result_theme->MakeResult(false, "", "");
    }
} else {
    echo $result_theme->MakeResult(false, "", "");
}
