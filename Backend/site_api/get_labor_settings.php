<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "user_login_barcode";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$user_id = isset($params['user_id']) != '' ? $params['user_id'] : '';

if ($user_id != '') {
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
    echo $result_theme->MakeResult(true, $res, "Login Success");
} else {
    echo $result_theme->MakeResult(false, "No Data");
}
