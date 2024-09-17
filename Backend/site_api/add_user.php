<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$barcode = isset($params['barcode']) != '' ? $params['barcode'] : '';
$username = isset($params['username']) != '' ? $params['username'] : '';
$password = isset($params['password']) != '' ? $params['password'] : '';
$password = md5($password);
$username_full = isset($params['username_full']) != '' ? $params['username_full'] : '';
$user_picture = isset($params['user_picture']) != '' ? $params['user_picture'] : '';
$security_level = isset($params['security_level']) != '' ? $params['security_level'] : '';
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

if ($username != '') {
    $tableName = $customer_id . "_user";
    $query = "INSERT INTO `$tableName` (`barcode`,`username`,`password`,`username_full`,`user_picture`,`security_level`, `customer_id`)";
    $query .= " VALUES ($barcode,'$username','$password','$username_full','$user_picture',$security_level, '$customer_id')";

    $table->ExecuteQuery_Simple_NoResult($query);
    $data = $table->ReadTable($tableName, "", " ORDER BY `Id` DESC LIMIT 1;");
    if (count($data)>0){
        echo $result_theme->MakeResult(true, $data[0]);
    }else{
        echo $result_theme->MakeResult(false, "");
    }
    
} else {
    echo $result_theme->MakeResult(false, "");
}
