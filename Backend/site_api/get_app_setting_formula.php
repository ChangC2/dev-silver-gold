<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");


$table = new TableMgr();
$result_theme = new Result_theme();

$tableName = "tbl_app_setting_calc";

try {
    $res = $table->ReadTable($tableName, "", "");
    if (count($res) == 0) {
        echo $result_theme->MakeResult(false, "", "no data");
    } else {
        echo $result_theme->MakeResult(true, $res);
    }
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
