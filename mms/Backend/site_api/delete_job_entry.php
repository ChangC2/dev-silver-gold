<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$entry_id = isset($params['entry_id']) != '' ? $params['entry_id'] : '';

if ($customer_id != '') {
    $tableName = $customer_id . "_jobdata";
    $query = "DELETE FROM $tableName WHERE $tableName.Id = $entry_id";

    $table->ExecuteQuery_Simple_NoResult($query);

    $data = $table->ReadTable($tableName, "", " ORDER BY `Id` DESC LIMIT 1;");
    $data['sql'] = $query;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");
}
