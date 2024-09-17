<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$id = isset($params['id']) != '' ? $params['id'] : '';      #only for update
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$name = isset($params['name']) != '' ? $params['name'] : '';
$machine_list = isset($params['machine_list']) != '' ? $params['machine_list'] : '';
$is_delete = isset($params['is_delete']) != '' ? $params['is_delete'] : false;


$tableName = $customer_id . "_machine_group";
if ($is_delete && $id != '') {
    $query = "DELETE FROM  `$tableName` WHERE `id`=$id;";
    $table->ExecuteQuery_Simple_NoResult($query);
    echo $result_theme->MakeResult(true);
} else {
    if ($customer_id == "" || $name == "" || $machine_list == "") {
        echo $result_theme->MakeResult(false, "");
        die;
    }

    $timestamp = time();
    $created_at = date("Y-m-d h:i:s", $timestamp);


    if ($id == '') {
        $query = "INSERT INTO `$tableName` (`name`, `machine_list`)";
        $query .= " VALUES ('$name', '$machine_list')";
        $table->ExecuteQuery_Simple_NoResult($query);
    } else {
        $query = "UPDATE `$tableName` SET `name`='$name', `machine_list`='$machine_list' WHERE `id`=$id;";
        $table->ExecuteQuery_Simple_NoResult($query);
    }

    $data = $table->ReadTable($tableName);
    echo $result_theme->MakeResult(true, $data);
}
