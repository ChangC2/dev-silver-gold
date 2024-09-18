<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$tableMgr = new TableMgr();
$result_theme = new Result_theme();
$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// Get requested params

try {
    $tableName = "arduino_firmware";
    $firmwares = $tableMgr->ReadTable($tableName, "", "");
    if (count($firmwares) > 0){
        echo $result_theme->MakeResult(true, $firmwares[0]);
    }else{
        echo $result_theme->MakeResult(false, "");
    }
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "");
}
