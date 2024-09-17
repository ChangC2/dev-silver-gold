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
$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// Get requested params
$sensor_id = isset($params['sensor_id']) != '' ? $params['sensor_id'] : '';
$sensor_id = filter($sensor_id);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$customer_id = filter($customer_id);
$sensor_name = isset($params['sensor_name']) != '' ? $params['sensor_name'] : '';
$sensor_name = filter($sensor_name);
$location = isset($params['location']) != '' ? $params['location'] : '';
$location = filter($location);
$for_value1 = isset($params['for_value1']) != '' ? $params['for_value1'] : '';
$for_value1 = filter($for_value1);
$for_value2 = isset($params['for_value2']) != '' ? $params['for_value2'] : '';
$for_value2 = filter($for_value2);
$for_value3 = isset($params['for_value3']) != '' ? $params['for_value3'] : '';
$for_value3 = filter($for_value3);
$image = isset($params['image']) != '' ? $params['image'] : '';
$image = filter($image);
$type = isset($params['type']) != '' ? $params['type'] : 0;
$unit = isset($params['unit']) != '' ? $params['unit'] : "";

$min_limit = isset($params['min_limit']) ? $params['min_limit'] : 0;
$max_limit = isset($params['max_limit']) ? $params['max_limit'] : 0;
$frequency = isset($params['frequency']) ? $params['frequency'] : 0;
$alert_emails = isset($params['alert_emails']) ? $params['alert_emails'] : "";


$tableName = "tbl_sensors";

if ($sensor_id != '' && $customer_id != '' && $sensor_name != '') {

    $sql = "UPDATE `$tableName` SET ";
    $sql .= "`sensor_name`='$sensor_name', ";
    $sql .= "`location`='$location', ";
    $sql .= "`for_value1`='$for_value1', ";
    $sql .= "`for_value2`='$for_value2', ";
    $sql .= "`for_value3`='$for_value3', ";
    $sql .= "`image`='$image', ";
    $sql .= "`type`='$type', ";
    $sql .= "`unit`='$unit', ";
    $sql .= "`min_limit`='$min_limit', ";
    $sql .= "`max_limit`='$max_limit', ";
    $sql .= "`frequency`='$frequency', ";
    $sql .= "`alert_emails`='$alert_emails' ";
    $sql .= " WHERE `customer_id`='$customer_id' AND `sensor_id`='$sensor_id'";
    try {
        $res = $table->ExecuteQuery_Simple_NoResult($sql);
        if ($res == true) {
            echo $result_theme->MakeResult(true, $res);
        } else {
            echo $result_theme->MakeResult(false, $sql, "Something went wrong with params");
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, $sql, "Something went wrong while adding new sensor.");
    }
} else {
    echo $result_theme->MakeResult(false, "", "There are some missing parameters.");
}
