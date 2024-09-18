<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// $customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

$tableName = "visser_user";
$users = $table->ReadTable($tableName, "", "");
foreach ($users as $user) {
    $id = $user["id"];
    $password = md5($user["password"]);
    $query = "UPDATE `$tableName` SET `password` = '$password' WHERE id='$id'";
    $table->ExecuteQuery_Simple_NoResult($query);
}
$users = $table->ReadTable($tableName, "", "");
echo $result_theme->MakeResult(true, $users);
