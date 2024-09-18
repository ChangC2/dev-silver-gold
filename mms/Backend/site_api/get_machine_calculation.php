<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$factory_id = isset($params['factory_id']) != '' ? $params['factory_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$date = isset($params['date']) != '' ? $params['date'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$tableName = "tbl_app_setting";

if ($factory_id == '' || $machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no param");
} else {
    $where = "(`factory_id`='$factory_id' AND `machine_id`='$machine_id')";
    try {
        $res = $table->ReadTable($tableName, $where, "");
        if (count($res) == 0) {
            echo $result_theme->MakeResult(false, "", "no data");
        } else {
            $settings = $res[0];
            $where = "`id`='" . $settings['calc_chart_formula'] . "'";
            $res1 = $table->ReadTable("tbl_app_setting_calc", $where, "");
            if (count($res1) == 0) {
                echo $result_theme->MakeResult(false, "", "no data");
            } else {
                $formula = $res1[0];
                //$formula["formula"] = "*";
                $sql = "SELECT " . $formula["formula"] . " FROM " . $factory_id . "_shifts" . " WHERE `machine` = '" . $machine_id . "' AND  substring(date, 1, 10) = '" . $date . "'";

                if ($settings["calc_chart_option"] == "1") {
                    $sql1 = "SELECT operator FROM " . $factory_id . "_shifts" . " WHERE `machine` = '" . $machine_id . "' ORDER BY id DESC";
                    $res3 = $table->ExecuteQuery_Simple($sql1);
                    $operator = "";
                    if (count($res3) > 0) {
                        $operator = $res3[0]["operator"];
                    }
                    $sql = $sql . " AND " . "`operator` = '" . $operator . "'";
                }

                if ($settings["calc_chart_option"] == "2") {
                    $sql1 = "SELECT jobID FROM " . $factory_id . "_shifts" . " WHERE `machine` = '" . $machine_id . "' ORDER BY id DESC";
                    $res3 = $table->ExecuteQuery_Simple($sql1);
                    $jobId = "";
                    if (count($res3) > 0) {
                        $jobId = $res3[0]["jobID"];
                    }
                    $sql = $sql . " AND " . "`jobID` = '" . $jobId . "'";
                }

                $res2 = $table->ExecuteQuery_Simple($sql);
                if (count($res2) == 0) {
                    echo $result_theme->MakeResult(false, "", "no data");
                } else {
                    $calc = $res2[0];
                    $key = array_keys($calc)[0];

                    $resFinal = array();
                    $resFinal['status'] = true;
                    $resFinal['value'] = $calc[$key] == null ? "0" : $calc[$key];
                    $resFinal['sql'] = $sql;
                    echo json_encode($resFinal);
                }
            }
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
