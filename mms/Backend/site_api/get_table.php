<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$tableName = isset($params['table']) != '' ? $params['table'] : '';
$where = isset($params['where']) != '' ? $params['where'] : '';

try {
	$res = $table->ReadTable($tableName, $where, "");
	echo $result_theme->MakeResult(true, $res);
} catch (Exception $err) {
	echo $result_theme->MakeResult(false, "", json_encode($err));
}
