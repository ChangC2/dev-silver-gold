<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$tableMgr = new TableMgr();
$result_theme = new Result_theme();
$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
// Get requested params
try {
    $tableName = "hennig_iot_1";
    $where = "`customer_id`='$customer_id' AND `machine_id`='$machine_id'";
    $data = $tableMgr->ReadTable($tableName, $where, "");
    echo $result_theme->MakeResult(true, $data);
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "");
}
