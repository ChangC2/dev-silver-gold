<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$userID  = isset($params['userID ']) != '' ? $params['userID '] : '';
$userName  = isset($params['userName ']) != '' ? $params['userName '] : '';
$accountID  = isset($params['accountID ']) != '' ? $params['accountID '] : '';
$machineName  = isset($params['machineName ']) != '' ? $params['machineName '] : '';


$result_theme = new Result_theme();

$query = "UPDATE `$table` SET `Operator`='$userName' WHERE `machineName` = '$machineName'";
if ($query != '') {
    $table = $accountID . "_status";
    $table = new TableMgr();
	$res = $table->ExecuteQuery_Simple_NoResult($query);
	echo $result_theme->MakeResult(true, $query);
} else {
	echo $result_theme->MakeResult(false, "");
}
