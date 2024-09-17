<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
if ($customer_id == '') {
    echo $result_theme->MakeResult(false, "");
    die;
}
$tableName = $customer_id . "_machine_group";
$data = $table->ReadTable($tableName);

echo $result_theme->MakeResult(true, $data, $tableName);
