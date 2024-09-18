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
    $sql = "SELECT * FROM `hennig_data` where `sensor_id`='$sensor_id' AND `reading_time`>='$from_date' AND `reading_time`<='$to_date' GROUP BY LEFT(`reading_time`, 13)";
    $tmpSensorValues = $table->ExecuteQuery_Simple($sql);

    $sql = "SELECT * FROM tbl_hennigs where `sensor_id`='$sensor_id'";
    $sensors = $table->ExecuteQuery_Simple($sql);
    $oneSensor = array();
    if ($sensors && count($sensors) > 0) {
        $oneSensor = $sensors[0];
    }

    $sensorValues = array();
    foreach ($tmpSensorValues as $row) {
        $oneRecord = array();
        $oneRecord["Sensor"] = $oneSensor["sensor_name"];
        $oneRecord["Sensor ID"] = $oneSensor["sensor_id"];
        $oneRecord[$oneSensor["for_value1"]] = $row["value1"];
        $oneRecord[$oneSensor["for_value2"]] = $row["value2"];
        if ($oneSensor["for_value3"] != ""){
            $oneRecord[$oneSensor["for_value3"]] = $row["value3"];
        }
        if ($oneSensor["for_value4"] != "") {
            $oneRecord[$oneSensor["for_value4"]] = $row["value4"];
        }
        if ($oneSensor["for_value5"] != "") {
            $oneRecord[$oneSensor["for_value5"]] = $row["value5"];
        }
        if ($oneSensor["for_value6"] != "") {
            $oneRecord[$oneSensor["for_value6"]] = $row["value6"];
        }
        $oneRecord["Reading Time"] = $row["reading_time"];
        $sensorValues[] = $oneRecord;
    }
    $res["sensor_values"] = $sensorValues;
    echo $result_theme->MakeResult(true, $res);
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
