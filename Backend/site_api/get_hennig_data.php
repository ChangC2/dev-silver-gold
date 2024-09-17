<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// Get requested params
$tableName = 'hennig_data';
$sensor_id = isset($params['sensor_id']) != '' ? $params['sensor_id'] : '';
$from_date = isset($params['from_date']) != '' ? $params['from_date'] : ''; // will be read as utc timestamp #ex: 1335939007
$to_date = isset($params['to_date']) != '' ? $params['to_date'] : ''; // will be read as utc timestamp #ex: 1335939007

try {
    $res = array();
    if ($sensor_id == "5002") {
        $sql = "SELECT actual_pressure as value1, set_pressure as value2, hz as value3, run_time as value4, alarm as value5, low_tank_alarm as value6, '5002' as sensor_id, 'VariFlow - 1' as sensor, created_at as reading_time FROM `vari_flow` where `created_at`>='$from_date' AND `created_at`<='$to_date' GROUP BY LEFT(`created_at`, 13)";
        $sensorValues = $table->ExecuteQuery_Simple($sql);
        $res["sensor_values"] = $sensorValues;
        
        $sql = "SELECT actual_pressure as value1, set_pressure as value2, hz as value3, run_time as value4, '5002' as sensor_id, 'VariFlow - 1' as sensor, created_at as reading_time FROM `vari_flow` ORDER BY created_at DESC LIMIT 1 ";
        $currentValue = $table->ExecuteQuery_Simple($sql);
        if (count($currentValue) > 0) {
            $res["current_value"] = $currentValue[0];
        }
    } else {
        $sql = "SELECT * FROM `hennig_data` where `sensor_id`='$sensor_id' AND `reading_time`>='$from_date' AND `reading_time`<='$to_date' GROUP BY LEFT(`reading_time`, 13)";
        $sensorValues = $table->ExecuteQuery_Simple($sql);
        $res["sensor_values"] = $sensorValues;

        $sql = "SELECT * FROM `hennig_data` where `sensor_id`='$sensor_id' ORDER BY reading_time DESC LIMIT 1 ";
        $currentValue = $table->ExecuteQuery_Simple($sql);
        if (count($currentValue) > 0) {
            $res["current_value"] = $currentValue[0];
        }
    }
    echo $result_theme->MakeResult(true, $res);
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
