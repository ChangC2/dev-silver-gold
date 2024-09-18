<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "labor_categories";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$user_id = isset($params['user_id']) != '' ? $params['user_id'] : '';
$name = isset($params['name']) != '' ? $params['name'] : '';
$order = isset($params['order']) != '' ? $params['order'] : 1;

if ($user_id != '' && $name != '') {
    $query = "INSERT INTO `$tableName` (`user_id`, `name`, `order`)";
    $query .= " VALUES ('$user_id','$name','$order')";
    $table->ExecuteQuery_Simple_NoResult($query);

    $where = "`user_id` = '" . $user_id . "'";
    $labor_categories = $table->ReadTable($tableName, $where, " ORDER BY `order` , id");
    $data['labor_categories'] = $labor_categories;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");  
}
