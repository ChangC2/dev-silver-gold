<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$query = isset($params['query']) != '' ? $params['query'] : '';


$result_theme = new Result_theme();

if ($query != '') {
	$table = new TableMgr();
	$res = $table->ExecuteQuery_Simple_NoResult($query);
	echo $result_theme->MakeResult(true, $query);
} else {
	echo $result_theme->MakeResult(false, "");
}
