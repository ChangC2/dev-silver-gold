<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$customer = isset($params['customer']) != '' ? $params['customer'] : '';
$description = isset($params['description']) != '' ? $params['description'] : '';
$dueDate = isset($params['dueDate']) != '' ? $params['dueDate'] : '';
$jobID = isset($params['jobID']) != '' ? $params['jobID'] : '';
$orderDate = isset($params['orderDate']) != '' ? $params['orderDate'] : '';
$partNumber = isset($params['partNumber']) != '' ? $params['partNumber'] : '';
$programNumber = isset($params['programNumber']) != '' ? $params['programNumber'] : '';
$qtyCompleted = isset($params['qtyCompleted']) != '' ? $params['qtyCompleted'] : 0;
$qtyRequired = isset($params['qtyRequired']) != '' ? $params['qtyRequired'] : 0;
$targetCycleTime = isset($params['targetCycleTime']) != '' ? $params['targetCycleTime'] : 0;
$partsPerCycle = isset($params['partsPerCycle']) != '' ? $params['partsPerCycle'] : 0;

$aux1data = isset($params['aux1data']) != '' ? $params['aux1data'] : '';
$aux2data = isset($params['aux2data']) != '' ? $params['aux2data'] : '';
$aux3data = isset($params['aux3data']) != '' ? $params['aux3data'] : '';

$files = isset($params['urls']) != '' ? $params['urls'] : '';

if ($customer_id != '') {
    $tableName = $customer_id . "_jobdata";
    $query = "INSERT INTO `$tableName` (`customer`, `description`, `dueDate`, `jobID`, `orderDate`, `partNumber`, `programNumber`,`partsPerCycle`, `qtyCompleted`, `qtyRequired`, `targetCycleTime`, `files`, `aux1data`, `aux2data`, `aux3data`)";
    $query .= " VALUES ('$customer','$description','$dueDate','$jobID','$orderDate','$partNumber','$programNumber', $partsPerCycle,$qtyCompleted,$qtyRequired,$targetCycleTime,'$files','$aux1data','$aux2data','$aux3data')";

    $table->ExecuteQuery_Simple_NoResult($query);

    $data = $table->ReadTable($tableName, "", " ORDER BY `Id` DESC LIMIT 1;");
    $data['sql'] = $query;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");
}
