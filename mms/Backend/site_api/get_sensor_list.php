<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$tableMgr = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

// Get requested params
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

try {
    if ($customer_id != '') {
        $tableName = "tbl_sensors";
        $sql = "`customer_id` = '$customer_id'";
        $list = $tableMgr->ReadTable($tableName, $sql);
        echo $result_theme->MakeResult(true, $list, "success");
    } else {
        echo $result_theme->MakeResult(false, "", "no customer info");
    }
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
