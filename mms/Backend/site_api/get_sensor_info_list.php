<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$tableMgr = new TableMgr();
$result_theme = new Result_theme();

// Get requested params
$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$idList = isset($params['idList']) != '' ? $params['idList'] : [];

$tableName = "SensorData";
try {
    if (count($idList) > 0) {
        $data = [];
        for ($i = 0; $i < count($idList); $i++) {
            $res = $tableMgr->ReadTable($tableName, "`sensor_id`='$idList[$i]'", " ORDER BY `reading_time` DESC LIMIT 1");
            if (count($res) > 0) {
                $data[$idList[$i]] = $res[0];
            }else{
                $data[$idList[$i]] = false;
            }
        }

        echo $result_theme->MakeResult(true, $data);
    } else {
        echo $result_theme->MakeResult(false);
    }
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
