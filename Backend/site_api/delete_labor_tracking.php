<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "labor_tracking";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$id = isset($params['id']) != '' ? $params['id'] : '';

if ($id != '') {
    $query = "DELETE FROM $tableName WHERE `id` = '$id'";
    $table->ExecuteQuery_Simple_NoResult($query);
    echo $result_theme->MakeResult(true, "");
} else {
    echo $result_theme->MakeResult(false, "");
}
