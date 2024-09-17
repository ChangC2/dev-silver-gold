<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$factory_id = isset($params['factory_id']) != '' ? $params['factory_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$tableName = "tbl_app_setting";

if ($factory_id == '' || $machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no param");
} else {

    $where = "(`factory_id`='$factory_id' AND `machine_id`='$machine_id')";

    try {
        $res = $table->ReadTable($tableName, $where, "");
        if (count($res) == 0) {
            echo $result_theme->MakeResult(false, "", "no data");
        } else {
            echo $result_theme->MakeResult(true, $res[0]);
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}