<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "labor_tracking";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$user_id = isset($params['user_id']) != '' ? $params['user_id'] : '';
$description = isset($params['description']) != '' ? $params['description'] : '';
$jobID = isset($params['jobID']) != '' ? $params['jobID'] : '';
$category = isset($params['category']) != '' ? $params['category'] : "";
$date = isset($params['date']) != '' ? $params['date'] : "0000-00-00";
$start_time = isset($params['start_time']) != '' ? $params['start_time'] : "00:00:00";
$end_time = isset($params['end_time']) != '' ? $params['end_time'] : "00:00:00";

if ($user_id != '') {
    $query = "INSERT INTO `$tableName` (`user_id`, `description`, `jobID`, `category`, `date`, `start_time`, `end_time`)";
    $query .= " VALUES ('$user_id','$description','$jobID','$category','$date','$start_time','$end_time')";
    $table->ExecuteQuery_Simple_NoResult($query);
    echo $result_theme->MakeResult(true, "", "Success");
} else {
    echo $result_theme->MakeResult(false, "");  
}
