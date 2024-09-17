<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "labor_setting";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$user_id = isset($params['user_id']) != '' ? $params['user_id'] : '';
$time_format = isset($params['time_format']) != '' ? $params['time_format'] : '';
$is_started_auto = isset($params['is_started_auto']) != '' ? $params['is_started_auto'] : '';
$start_time = isset($params['start_time']) != '' ? $params['start_time'] : '';

if ($user_id != '') {
    $res = $table->ReadTable($tableName, "`user_id`='$user_id'", "");
    if (count($res) > 0){
        $query = "UPDATE `$tableName` SET `time_format` = '$time_format', `is_started_auto` = '$is_started_auto', `start_time` = '$start_time' WHERE `user_id` = $user_id";
        $table->ExecuteQuery_Simple_NoResult($query);
    }else{
        $query = "INSERT INTO `$tableName` (`user_id`, `time_format`, `is_started_auto`, `start_time`)";
        $query .= " VALUES ('$user_id','$time_format','$is_started_auto','$start_time')";
        $table->ExecuteQuery_Simple_NoResult($query);
    }

    $labor_setting = $table->ReadTable("labor_setting", "`user_id`='" . $user_id . "'", "");
    if (count($labor_setting) > 0) {
        $res["time_format"] = $labor_setting[0]["time_format"];
        $res["is_started_auto"] = $labor_setting[0]["is_started_auto"];
        $res["start_time"] = $labor_setting[0]["start_time"];
    } else {
        $res["time_format"] = "";
        $res["is_started_auto"] = "";
        $res["start_time"] = "0";
    }
    echo $result_theme->MakeResult(true, $res, "Success");
} else {
    echo $result_theme->MakeResult(false, "");  
}
