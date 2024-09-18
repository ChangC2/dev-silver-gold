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
$machine = isset($params['machine']) != '' ? $params['machine'] : '';
$taskName = isset($params['taskName']) != '' ? $params['taskName'] : '';
$taskCategory = isset($params['taskCategory']) != '' ? $params['taskCategory'] : '';
$taskPicture = isset($params['taskPicture']) != '' ? $params['taskPicture'] : '';
$taskInstruction = isset($params['taskInstruction']) != '' ? $params['taskInstruction'] : '';
$frequency = isset($params['frequency']) != '' ? $params['frequency'] : '';
$cycleStartInterlock = isset($params['cycleStartInterlock']) != '' ? $params['cycleStartInterlock'] : '';


$files = isset($params['files']) != '' ? $params['files'] : '';

if ($customer_id != '') {
    $tableName = $customer_id . "_maintenance";
    $query = "UPDATE `$tableName` SET `files` = '$files', `machine_id` = '$machine', `picture` = '$taskPicture', `task_name` = '$taskName', `task_category` = '$taskCategory', `task_instruction` = '$taskInstruction', `frequency` = $frequency, `interlock` = $cycleStartInterlock WHERE `$tableName`.`id` = $entry_id";
    

    
    $table->ExecuteQuery_Simple_NoResult($query);

    $data = $table->ReadTable($tableName, "", " ORDER BY `Id` DESC LIMIT 1;");
    $data['sql'] = $query;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");
}
