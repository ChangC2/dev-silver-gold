<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';


$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "alarms";

$res = array();
if ($customer_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        
        $sql = "SELECT DISTINCT machine_id FROM $tableName WHERE `customer_id` = '$customer_id'";
        $machineList = $table->ExecuteQuery_With_Column(["machine_id"], $sql);

        $sql = "SELECT DISTINCT progNum FROM $tableName WHERE `customer_id` = '$customer_id'";
        $progNumList = $table->ExecuteQuery_With_Column(["progNum"], $sql);

        $sql = "SELECT DISTINCT alarmType FROM $tableName WHERE `customer_id` = '$customer_id' AND `alarmType` != ''";
        $alarmTypeList = $table->ExecuteQuery_With_Column(["alarmType"], $sql);

        $res["machineList"] = $machineList;
        $res["progNumList"] = $progNumList;
        $res["alarmTypeList"] = $alarmTypeList;

        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
