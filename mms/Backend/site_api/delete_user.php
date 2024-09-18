<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true); // ID value
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$id = isset($params['id']) != '' ? $params['id'] : '';

if (true) {
    $tableName = $customer_id . "_user";
    $query = "DELETE FROM $tableName WHERE $tableName.`id` = $id";

    $table->ExecuteQuery_Simple_NoResult($query);

    $data['sql'] = $query;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");
}
