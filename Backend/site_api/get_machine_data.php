<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$shiftTimes = isset($params['shiftTime']) != '' ? explode('-', $params['shiftTime']) : array();

if ($customer_id != '' && $startDate != '' && $endDate != '') {
    try {
        // Read Status Table
        $tableName = $customer_id . "_status";
        $where = "";
        if ($machine_id != '') {
            $where .= "`machine_id` = '$machine_id' ";
        }
        $machines = $table->ReadTable($tableName, $where);

        date_default_timezone_set('UTC');
        $timezone = $timezone * 3600;

        $start = strtotime($startDate) - $timezone;
        $end = strtotime($endDate) - $timezone;
        $current_time = time() * 1000;
        $current_passed_time = $current_time - $start * 1000  > 24 * 3600 * 1000 ? 24 * 3600 * 1000 : $current_time - $start * 1000;

        $shiftInfo = "24 Hours";
        $shift_start = $start * 1000;
        $shift_end = $end * 1000;

        $tableName = "$customer_id" . "_info";
        $sql = "SELECT * FROM `$tableName`";
        $infos = $table->ExecuteQuery_Simple($sql);
        $shifts = array();
        if (count($infos) > 0) {
            $shifts[] = $infos[0]["shift1_start"] . "-" . $infos[0]["shift1_end"];
            $shifts[] = $infos[0]["shift2_start"] . "-" . $infos[0]["shift2_end"];
            $shifts[] = $infos[0]["shift3_start"] . "-" . $infos[0]["shift3_end"];
        }

        // Get Shift Info
        if (count($shiftTimes) < 2){
            if (count($infos) > 0) {
                $isSetShift = false;
                $s1 = $infos[0]["shift1_start"];
                $e1 = $infos[0]["shift1_end"];

                if (
                    $s1 != $e1 && $s1 != "" && $e1 != ""
                ) {
                    $s1Start = strtotime($startDate . " " . $s1) * 1000 - $timezone * 1000;
                    $s1End = strtotime($startDate . " " . $e1) * 1000 - $timezone * 1000;

                    if ($current_time > $s1Start && $current_time < $s1End) {
                        $shift_start = $s1Start;
                        $shift_end = $s1End;
                        $shiftInfo = "Shift1 ($s1~$e1)";
                        $isSetShift = true;
                    } else if ($s1Start > $s1End && $current_time > $s1Start && $current_time < (strtotime($startDate . " " . $e1)  + 24 * 3600) * 1000 - $timezone * 1000) {
                        $shift_start = $s1Start;
                        $shift_end = (strtotime($startDate . " " . $e1)  + 24 * 3600) * 1000 - $timezone * 1000;
                        $shiftInfo = "Shift1 ($s1~$e1)";
                        $isSetShift = true;
                    } else if ($s1Start > $s1End && $current_time > (strtotime($startDate . " " . $s1)  - 24 * 3600) * 1000 - $timezone * 1000 && $current_time < $s1End) {
                        $shift_start = (strtotime($startDate . " " . $s1)  - 24 * 3600) * 1000 - $timezone * 1000;
                        $shift_end = $s1End;
                        $shiftInfo = "Shift1 ($s1~$e1)";
                        $isSetShift = true;
                    }
                }

                $s2 = $infos[0]["shift2_start"];
                $e2 = $infos[0]["shift2_end"];

                if (
                    $s2 != $e2 && $s2 != "" && $e2 != "" && !$isSetShift
                ) {
                    $s2Start = strtotime($startDate . " " . $s2) * 1000 - $timezone * 1000;
                    $s2End = strtotime($startDate . " " . $e2) * 1000 - $timezone * 1000;

                    if ($current_time > $s2Start && $current_time < $s2End) {
                        $shift_start = $s2Start;
                        $shift_end = $s2End;
                        $shiftInfo = "Shift2 ($s2~$e2)";
                        $isSetShift = true;
                    } else if ($s2Start > $s2End && $current_time > $s2Start && $current_time < (strtotime($startDate . " " . $e2)  + 24 * 3600) * 1000 - $timezone * 1000) {
                        $shift_start = $s2Start;
                        $shift_end = (strtotime($startDate . " " . $e2)  + 24 * 3600) * 1000 - $timezone * 1000;
                        $shiftInfo = "Shift2 ($s2~$e2)";
                        $isSetShift = true;
                    } else if ($s2Start > $s2End && $current_time > (strtotime($startDate . " " . $s2)  - 24 * 3600) * 1000 - $timezone * 1000 && $current_time < $s2End) {
                        $shift_start = (strtotime($startDate . " " . $s2)  - 24 * 3600) * 1000 - $timezone * 1000;
                        $shift_end = $s2End;
                        $shiftInfo = "Shift2 ($s2~$e2)";
                        $isSetShift = true;
                    }
                }

                $s3 = $infos[0]["shift3_start"];
                $e3 = $infos[0]["shift3_end"];
                if (
                    $s3 != $e3 && $s3 != "" && $e3 != "" && !$isSetShift
                ) {
                    $s3Start = strtotime($startDate . " " . $s3) * 1000 - $timezone * 1000;
                    $s3End = strtotime($startDate . " " . $e3) * 1000 - $timezone * 1000;

                    if ($current_time > $s3Start && $current_time < $s3End) {
                        $shift_start = $s3Start;
                        $shift_end = $s3End;
                        $shiftInfo = "Shift3 ($s3~$e3)";
                    } else if ($s3Start > $s3End && $current_time > $s3Start && $current_time < (strtotime($startDate . " " . $e3)  + 24 * 3600) * 1000 - $timezone * 1000) {
                        $shift_start = $s3Start;
                        $shift_end = (strtotime($startDate . " " . $e3)  + 24 * 3600) * 1000 - $timezone * 1000;
                        $shiftInfo = "Shift3 ($s3~$e3)";
                    } else if ($s3Start > $s3End && $current_time > (strtotime($startDate . " " . $s3)  - 24 * 3600) * 1000 - $timezone * 1000 && $current_time < $s3End) {
                        $shift_start = (strtotime($startDate . " " . $s3)  - 24 * 3600) * 1000 - $timezone * 1000;
                        $shift_end = $s3End;
                        $shiftInfo = "Shift3 ($s3~$e3)";
                    }
                }
            }
        }else{
            $shift_start = strtotime($startDate . " " . $shiftTimes[0]) * 1000 - $timezone * 1000;
            $shift_end = strtotime($startDate . " " . $shiftTimes[1]) * 1000 - $timezone * 1000;
            $shiftInfo = $shiftTimes[0] . "~" . $shiftTimes[1];
        }

        $timeDiff = $shift_end - $shift_start;
        if ($current_time < $shift_end && $current_time > $shift_start
        ) {
            $timeDiff = $current_time - $shift_start;
        }

        // Read Gantt Table
        $tableName = $customer_id . "_ganttdata";
        $where = "`end` >= $start AND `start`<=$end ";
        $gantt = $table->ReadTable($tableName, $where, " ORDER BY start");
        for ($i = 0; $i < count($gantt); $i++) {
            if ($gantt[$i]['start'] < $start) {
                $gantt[$i]['start'] = $start;
            }
            if ($gantt[$i]['end'] > $end) {
                $gantt[$i]['end'] = $end;
            }
        }

        for ($i = 0; $i < count($machines); $i++) {
            $machines[$i]["shift_info"] = $shiftInfo;
            // Read Gantt Table
            $machineGantt = [];
            for ($j = 0; $j < count($gantt); $j++) {
                if ($gantt[$j]['machine_id'] == $machines[$i]["machine_id"]) {
                    $machineGantt[] = $gantt[$j];
                }
            }

            for ($j = 0; $j < count($machineGantt) - 1; $j++) {
                if ((int)$machineGantt[$j]["end"] > (int)$machineGantt[$j + 1]["start"]) {
                    $machineGantt[$j]["end"] = $machineGantt[$j + 1]["start"];
                }
            }

            for ($j = count($machineGantt) - 1; $j > 0; $j--) {
                if ($machineGantt[$j]["status"] == $machineGantt[$j - 1]["status"]) {
                    (int)$machineGantt[$j - 1]["end"] = (int)$machineGantt[$j]["end"];
                    unset($machineGantt[$j]);
                    $machineGantt = array_values($machineGantt);
                }
            }
            $machines[$i]["gantt"] = $machineGantt;
            
            $lastGantt = $machineGantt[count($machineGantt) - 1];
            if ($lastGantt["status"] == "Offline") {
                $machines[$i]["status"] = "Offline";
                $machines[$i]["color"] = "#000000";
                $machines[$i]["elapsed"] = $current_time / 1000 - (int)$lastGantt["start"];
            } else {
                if ($current_time / 1000 - (int)$lastGantt["end"] > 60 * 5) {
                    $machines[$i]["status"] = "Offline";
                    $machines[$i]["color"] = "#000000";
                    $machines[$i]["elapsed"] = $current_time / 1000 - (int)$lastGantt["end"];
                } else {
                    $machines[$i]["status"] = $lastGantt["status"];
                    $machines[$i]["color"] = $lastGantt["color"];
                    $machines[$i]["elapsed"] = (int)$lastGantt["end"] - (int)$lastGantt["start"];
                }
            }
            $inCyle = 0;
            for ($j = 0; $j < count($machineGantt); $j++) {
                if ($machineGantt[$j]["status"] == "In Cycle" && $machineGantt[$j]["end"] > $shift_start / 1000 && $machineGantt[$j]["start"] < $shift_end / 1000) {
                    $endValue = (int)$machineGantt[$j]["end"] > $shift_end / 1000 ? $shift_end / 1000 : (int)$machineGantt[$j]["end"];
                    $startValue = (int)$machineGantt[$j]["start"] < $shift_start / 1000 ? $shift_start / 1000 : (int)$machineGantt[$j]["start"];
                    $inCyle += $endValue - $startValue;
                }
            }
            $machines[$i]["utilization"] = $inCyle * 100 * 1000 / $current_passed_time;
            $machines[$i]["availability"] = $inCyle * 100 * 1000 / $timeDiff;

            $machines[$i]["interface"] = $lastGantt["interface"];
            $machines[$i]["Operator"] = $lastGantt["Operator"];
            $machines[$i]["operator_picture_url"] = $lastGantt["operator_picture_url"];
            $machines[$i]["job_id"] = $lastGantt["job_id"];
            if (strpos($interface, "ESP") !== false) {
                if (count($machineGantt) > 0) {
                    $machines[$i]["oee"] = $machines[$i]["availability"];
                    $machines[$i]["goodParts"] = 0;
                    $machines[$i]["badParts"] = 0;
                    $machines[$i]["performance"] = 0;
                    $machines[$i]["quality"] = 0;
                }
            } else {
                // Read Shift table
                $tableName = "$customer_id" . "_shifts";
                $performanceField = "SUM(((`stopTime` - `startTime`) / " . ($shift_end - $shift_start) . ") * `targetCycleTime` * (`goodParts` + `badParts`) / `inCycle`)";
                $where = "`machine` = '" . $machines[$i]["machine_id"] . "' ";
                $where .= " AND `startTime` >= '$shift_start' ";
                $where .= " AND `stopTime` <= '$shift_end' ";

                $sql =  "SELECT SUM(inCycle) as inCycle, SUM(goodParts) as goodParts, SUM(badParts) as badParts, $performanceField as performance " .
                    "FROM `$tableName` " .
                    "WHERE $where ";
                $machines[$i]["sql"] = $sql;
                $hstList = $table->ExecuteQuery_Simple($sql);
                if (count($hstList) > 0) {
                    $inCyle = $hstList[0]["inCycle"] !== null ? $hstList[0]["inCycle"] : 0;
                    $goodParts = $hstList[0]["goodParts"] !== null ? $hstList[0]["goodParts"] : 0;
                    $badParts = $hstList[0]["badParts"] !== null ? $hstList[0]["badParts"] : 0;
                    $performance = $hstList[0]["performance"] !== null ? $hstList[0]["performance"] : 0;

                    $machines[$i]["goodParts"] = $goodParts;
                    $machines[$i]["badParts"] = $badParts;
                    $machines[$i]["performance"] = $performance * 100;
                    $machines[$i]["quality"] = $goodParts + $badParts > 0 ?  $goodParts * 100 / ($goodParts + $badParts) : 0;
                    $machines[$i]["oee"] = $machines[$i]["availability"] * $machines[$i]["quality"] / 100;
                }
            }
        }

        $res = array();
        $res['machines'] = $machines;
        $res['shifts'] = $shifts;
        $res['shift_start'] = $shift_start;
        $res['shift_end'] = $shift_end;

        $res['current_time'] = $current_time;
        // $res['timeDiff'] = $timeDiff;
        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
} else {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
