<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$id = isset($params['id']) != '' ? $params['id'] : '';
$username = isset($params['username']) != '' ? $params['username'] : '';
$password = isset($params['password']) != '' ? $params['password'] : '';
$username_full = isset($params['username_full']) != '' ? $params['username_full'] : '';
$user_picture = isset($params['user_picture']) != '' ? $params['user_picture'] : '';
$security_level = isset($params['security_level']) != '' ? $params['security_level'] : 0;
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

if ($username != '') {
    $tableName = $customer_id . "_user";
    if ($password == ""){
        $query = "UPDATE $tableName SET `username` = '$username', `username_full` = '$username_full', `user_picture` = '$user_picture', `security_level` = $security_level WHERE $tableName.`id` = $id";
        $table->ExecuteQuery_Simple_NoResult($query);
    }else{
        $password = md5($password);
        $query = "UPDATE $tableName SET `username` = '$username', `password` = '$password', `username_full` = '$username_full', `user_picture` = '$user_picture', `security_level` = $security_level WHERE $tableName.`id` = $id";
        $table->ExecuteQuery_Simple_NoResult($query);
    }
    echo $result_theme->MakeResult(true, "");
} else {
    echo $result_theme->MakeResult(false, "");
}
