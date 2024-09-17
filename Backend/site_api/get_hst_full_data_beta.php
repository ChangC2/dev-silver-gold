<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$res = array();
if ($machine_id == '' || $machine_id == '' || $machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        // read hst data
        $tableName = "$customer_id" . "_hst";
        $where = "`machine_id`='$machine_id' AND CONCAT(RIGHT(date,4),'/',LEFT(date,5))>='$startDate' AND CONCAT(RIGHT(date,4),'/',LEFT(date,5)) <= '$endDate'";
        $hstData = $table->ReadTable($tableName, $where, "");

        echo $result_theme->MakeResult(true, $hstData);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
