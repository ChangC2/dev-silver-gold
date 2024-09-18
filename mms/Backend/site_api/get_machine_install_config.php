<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';

if ($customer_id != '') {
    try {
        $tableName = $customer_id . "_status";
        $where = "`machine_id` = '$machine_id'";
        $machine_details = $table->ReadTable($tableName, $where, "");

        // Read Status Table
        $tableName = $customer_id."_machine_image";
        $where = "`machine_id` = '$machine_id'";
        $images = $table->ReadTable($tableName, $where);

        $res = array();
        $res['machine_details'] = $machine_details[0];
        $res['images'] = $images;
        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
} else {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
