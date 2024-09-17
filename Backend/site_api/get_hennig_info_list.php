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

$tableName = "hennig_data";
try {
    if (count($idList) > 0) {
        $data = [];
        for ($i = 0; $i < count($idList); $i++) {
            if ($idList[$i] == "5002") {
                $sql = "SELECT system_status as system_status, actual_pressure as value1, set_pressure as value2, hz as value3, run_time as value4, alarm as value5, low_tank_alarm as value6, '5002' as sensor_id, 'VariFlow - 1' as sensor, created_at as reading_time FROM `vari_flow` ORDER BY `created_at` DESC LIMIT 1";
                $res = $tableMgr->ExecuteQuery_Simple($sql);
            } else {
                $res = $tableMgr->ReadTable($tableName, "`sensor_id`='$idList[$i]'", " ORDER BY `reading_time` DESC LIMIT 1");
            }
            if (count($res) > 0) {
                $data[$idList[$i]] = $res[0];
            } else {
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
