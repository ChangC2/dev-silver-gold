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

$tableName = "tbl_sensors";

if ($sensor_id != '' && $customer_id != '') {


    try {
        $sql = "DELETE FROM `tbl_sensors` WHERE `sensor_id`='$sensor_id' AND `customer_id`='$customer_id'; ";
        $res = $table->ExecuteQuery_Simple_NoResult($sql);
        $sql = "DELETE FROM `SensorData` WHERE `sensor_id`='$sensor_id';";
        $res = $table->ExecuteQuery_Simple_NoResult($sql);
        
        if ($res == true) {
            echo $result_theme->MakeResult(true, $res);
        } else {
            echo $result_theme->MakeResult(false, "", "Fail to delete.");
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", "Fail to delete.");
    }
} else {
    echo $result_theme->MakeResult(false, "", "Fail to delete.");
}
