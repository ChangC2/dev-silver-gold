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
$for_value1 = isset($params['for_value1']) != '' ? $params['for_value1'] : 'Value1';
$for_value1 = filter($for_value1);
$for_value2 = isset($params['for_value2']) != '' ? $params['for_value2'] : 'Value2';
$for_value2 = filter($for_value2);
$for_value3 = isset($params['for_value3']) != '' ? $params['for_value3'] : 'Value3';
$for_value3 = filter($for_value3);
$for_value4 = isset($params['for_value4']) != '' ? $params['for_value4'] : 'Value4';
$for_value4 = filter($for_value4);
$for_value5 = isset($params['for_value5']) != '' ? $params['for_value5'] : 'Value5';
$for_value5 = filter($for_value5);
$for_value6 = isset($params['for_value6']) != '' ? $params['for_value6'] : 'Value6';
$for_value6 = filter($for_value6);
$image = isset($params['image']) != '' ? $params['image'] : '';
$image = filter($image);
$type = isset($params['type']) != '' ? $params['type'] : 0;
$unit = isset($params['unit']) != '' ? $params['unit'] : "";
$min_limit = isset($params['min_limit']) != '' ? $params['min_limit'] : 0;
$max_limit = isset($params['max_limit']) != '' ? $params['max_limit'] : 0;
$alert_emails = isset($params['alert_emails']) != '' ? $params['alert_emails'] : "";
$modbus_ip = isset($params['modbus_ip']) != '' ? $params['modbus_ip'] : "";
$modbus_hreg = isset($params['modbus_hreg']) != '' ? $params['modbus_hreg'] : "";

$tableName = "tbl_hennigs";

if ($sensor_id != '' && $sensor_name != '') {
    $sql = "INSERT INTO `$tableName` (`sensor_id`, `customer_id`, `sensor_name`, `for_value1`, `for_value2`, `for_value3`, `for_value4`, `for_value5`, `for_value6`, `image`, `type`, `unit`, `min_limit`, `max_limit`, `alert_emails`, `modbus_ip`, `modbus_hreg`) VALUES (";
    $sql .= "'$sensor_id', '$customer_id', '$sensor_name', '$for_value1', '$for_value2', '$for_value3', '$for_value4', '$for_value5', '$for_value6', '$image', $type, '$unit', '$min_limit', '$max_limit', '$alert_emails', '$modbus_ip', '$modbus_hreg')";
    try {
        $res = $table->ExecuteQuery_Simple_NoResult($sql);
        if ($res == true) {
            echo $result_theme->MakeResult(true, $sql);
        } else {
            echo $result_theme->MakeResult(false, "", "Duplicated Sensor id.");
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", "Something went wrong while adding new sensor.");
    }
} else {
    echo $result_theme->MakeResult(false, "", "There are some missing parameters.");
}
