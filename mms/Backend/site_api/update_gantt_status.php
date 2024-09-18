<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$tableName = $params['customer_id']."_ganttdata";
$machine_id = $params['machine_id'];
$start = $params['start'];
$end = $params['end'];
$status = $params['status'];
$color = $params['color'];
$query = "UPDATE `$tableName` SET `color`='$color', `status`='$status' WHERE `machine_id`='$machine_id' AND `start`>='$start' AND `end`<='$end'";

try {
    $res = $table->ExecuteQuery_Simple_NoResult($query);
    echo $result_theme->MakeResult(true);
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
