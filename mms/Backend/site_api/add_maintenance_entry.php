<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$task_name = isset($params['task_name']) != '' ? $params['task_name'] : '';
$task_category = isset($params['task_category']) != '' ? $params['task_category'] : '';
$picture = isset($params['picture']) != '' ? $params['picture'] : '';
$task_instruction = isset($params['task_instruction']) != '' ? $params['task_instruction'] : '';
$frequency = isset($params['frequency']) != '' ? $params['frequency'] : 0;
$interlock = isset($params['interlock']) != '' ? $params['interlock'] : 0;
$is_finished = 0;
$created_at = date('Y-m-d H:i:s');

$files = isset($params['urls']) != '' ? $params['urls'] : '';

if ($customer_id != '') {
    $tableName = $customer_id . "_maintenance";
    $query = "INSERT INTO `$tableName` (`machine_id`,`task_name`,`task_category`,`files`,`picture`,`task_instruction`,`frequency`,`interlock`,`is_finished`, `created_at`)";
    $query .= " VALUES ('$machine_id','$task_name','$task_category','$files','$picture','$task_instruction',$frequency,$interlock,$is_finished, '$created_at')";

    // $query = "INSERT INTO `$tableName` (`customer`, `description`, `jobID`, `partNumber`, `programNumber`, `qtyCompleted`, `qtyRequired`, `targetCycleTime`)";
    // $query .= " VALUES ('$customer','$description','$jobID','$partNumber','$programNumber',$qtyCompleted,$qtyRequired,'$targetCycleTime')";
    $table->ExecuteQuery_Simple_NoResult($query);

    $data = $table->ReadTable($tableName, "", " ORDER BY `Id` DESC LIMIT 1;");
    $data['sql'] = $query;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");
}
