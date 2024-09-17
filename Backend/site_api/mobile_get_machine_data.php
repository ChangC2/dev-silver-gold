<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';

if ($customer_id != '' && $startDate != '' && $endDate != '') {
    try {
        // Read Status Table
        $tableName = $customer_id . "_status";
        $where = "";
        if ($machine_id != '') {
            $where .= "`machine_id` = '$machine_id' ";
        }
        $machines = $table->ReadTable($tableName, $where);

        $Date = $startDate;
        date_default_timezone_set('UTC');
        $timezone = $timezone * 3600;

        $start = strtotime($startDate) - $timezone;
        $end = strtotime($endDate) - $timezone;
        $current_time = time() * 1000 - $timezone;
        $current_passed_time = $current_time - $start * 1000 > 24 * 3600 * 1000 ? 24 * 3600 * 1000 : $current_time - $start * 1000;

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

            $incycle = 0;
            for ($j = 0; $j < count($machineGantt); $j++) {
                if ($machineGantt[$j]["status"] == "In Cycle") {
                    $incycle += (int)$machineGantt[$j]["end"] - (int)$machineGantt[$j]["start"];
                }
            }
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
            $machines[$i]["utilization"] = $incycle * 100 * 1000 / $current_passed_time;
            if ($machine_id != '') {
                $machines[$i]["gantt"] = $machineGantt;

                $month_arr = array();
                for ($ii = 30 - 1; $ii > 0; $ii--) {
                    $date = strtotime("-" . $ii . " day", strtotime($startDate) - $timezone);
                    array_push($month_arr, date('m/d/Y', $date));
                }
                array_push($month_arr, $startDate);
                $machines[$i]["month_arr"] = $month_arr;

                $firstDate = $month_arr[0];
                $lastDate = $month_arr[sizeof($month_arr) - 1];

                $start = strtotime($firstDate) - $timezone;
                $end = strtotime($lastDate) - $timezone;
                $current_time = time() * 1000 - $timezone;

                $tableName = $customer_id . "_ganttdata";
                $ganttSql = "SELECT DATE_FORMAT(FROM_UNIXTIME(start - $timezone), '%m/%d/%Y') as day, SUM(end-start) as incycle FROM `$tableName` where `end` >= $start AND `start`<=$end and `machine_id` = '$machine_id' and status = 'In Cycle' group by day order by start";
                $gantt = $table->ExecuteQuery_Simple($ganttSql);

                $tmp_result = array();
                for ($ii = 0; $ii < sizeof($gantt); $ii++) {
                    $dayStart = strtotime($gantt[$ii]['day']) - $timezone;
                    $day_current_passed_time = $current_time - $dayStart * 1000 > 24 * 3600 * 1000 ? 24 * 3600 * 1000 : $current_time - $dayStart * 1000;

                    $incycle = $gantt[$ii]['incycle'] * 100 * 1000 / $day_current_passed_time;
                    $tmp_result[] = array("day" => $gantt[$ii]['day'], "utilization" => $incycle);
                }
                $utilizations = array();
                for ($ii = 0; $ii < sizeof($month_arr); $ii++) {
                    $utilization = 0;
                    $cur_date = $month_arr[$ii];
                    for ($j = 0; $j < sizeof($tmp_result); $j++) {
                        if ($tmp_result[$j]['day'] == $cur_date) {
                            $utilization = $tmp_result[$j]['utilization'];
                            break;
                        }
                    }
                    $utilizations[] = array("day" => $cur_date, "utilization" => $utilization);
                }
                $machines[$i]["utilizations"] = $utilizations;
                
            }
        }

        /*
        $shiftInfo = "24 Hours";
        $shift_start = $start * 1000;
        $shift_end = $end * 1000;

        $tableName = "$customer_id" . "_info";
        $sql = "SELECT * FROM `$tableName`";
        $infos = $table->ExecuteQuery_Simple($sql);

        $current_time = time() * 1000 - $timezone;
        $current_passed_time = $current_time - $start * 1000;

        // Get Shift Info
        if (count($infos) > 0) {
            $s1 = $infos[0]["shift1_start"];
            $e1 = $infos[0]["shift1_end"];

            if ($s1 != $e1 && $s1 != "" && $e1 != "") {
                $s1Start = strtotime($startDate . " " . $s1) * 1000 - $timezone * 1000;
                $s1End = strtotime($startDate . " " . $e1) * 1000 - $timezone * 1000;

                if ($current_time > $s1Start && $current_time < $s1End) {
                    $shift_start = $s1Start;
                    $shift_end = $s1End;
                    $shiftInfo = "Shift1 ($s1~$e1)";
                } else if ($current_time > $s1Start && $current_time < (strtotime($startDate . " " . $e1)  + 24 * 3600) * 1000 - $timezone * 1000) {
                    $shift_start = $s1Start;
                    $shift_end = (strtotime($startDate . " " . $e1)  + 24 * 3600) * 1000 - $timezone * 1000;
                    $shiftInfo = "Shift1 ($s1~$e1)";
                } else if ($current_time > (strtotime($startDate . " " . $s1)  - 24 * 3600) * 1000 - $timezone * 1000 && $current_time < $s1End) {
                    $shift_start = (strtotime($startDate . " " . $s1)  - 24 * 3600) * 1000 - $timezone * 1000;
                    $shift_end = $s1End;
                    $shiftInfo = "Shift1 ($s1~$e1)";
                }
            }

            $s2 = $infos[0]["shift2_start"];
            $e2 = $infos[0]["shift2_end"];

            if ($s2 != $e2 && $s2 != "" && $e2 != "") {
                $s2Start = strtotime($startDate . " " . $s2) * 1000 - $timezone * 1000;
                $s2End = strtotime($startDate . " " . $e2) * 1000 - $timezone * 1000;

                if ($current_time > $s2Start && $current_time < $s2End) {
                    $shift_start = $s2Start;
                    $shift_end = $s2End;
                    $shiftInfo = "Shift2 ($s2~$e2)";
                } else if ($current_time > $s2Start && $current_time < (strtotime($startDate . " " . $e2)  + 24 * 3600) * 1000 - $timezone * 1000) {
                    $shift_start = $s2Start;
                    $shift_end = (strtotime($startDate . " " . $e2)  + 24 * 3600) * 1000 - $timezone * 1000;
                    $shiftInfo = "Shift2 ($s2~$e2)";
                } else if ($current_time > (strtotime($startDate . " " . $s2)  - 24 * 3600) * 1000 - $timezone * 1000 && $current_time < $s2End) {
                    $shift_start = (strtotime($startDate . " " . $s2)  - 24 * 3600) * 1000 - $timezone * 1000;
                    $shift_end = $s2End;
                    $shiftInfo = "Shift2 ($s2~$e2)";
                }
            }

            $s3 = $infos[0]["shift3_start"];
            $e3 = $infos[0]["shift3_end"];
            if ($s3 != $e3 && $s3 != "" && $e3 != "") {
                $s3Start = strtotime($startDate . " " . $s3) * 1000 - $timezone * 1000;
                $s3End = strtotime($startDate . " " . $e3) * 1000 - $timezone * 1000;

                if ($current_time > $s3Start && $current_time < $s3End) {
                    $shift_start = $s3Start;
                    $shift_end = $s3End;
                    $shiftInfo = "Shift3 ($s3~$e3)";
                } else if ($current_time > $s3Start && $current_time < (strtotime($startDate . " " . $e3)  + 24 * 3600) * 1000 - $timezone * 1000) {
                    $shift_start = $s3Start;
                    $shift_end = (strtotime($startDate . " " . $e3)  + 24 * 3600) * 1000 - $timezone * 1000;
                    $shiftInfo = "Shift3 ($s3~$e3)";
                } else if ($current_time > (strtotime($startDate . " " . $s3)  - 24 * 3600) * 1000 - $timezone * 1000 && $current_time < $s3End) {
                    $shift_start = (strtotime($startDate . " " . $s3)  - 24 * 3600) * 1000 - $timezone * 1000;
                    $shift_end = $s3End;
                    $shiftInfo = "Shift3 ($s3~$e3)";
                }
            }
        }

        for ($i = 0; $i < count($hst); $i++) {
            $hst[$i]["shift_info"] = $shiftInfo;
            // Read Gantt table
            $tableName = "$customer_id" . "_ganttdata";
            $where = "`end` >= $start AND `start`<=$end  AND `status` = 'In Cycle'";
            $where .= " AND " . "`machine_id` = '" . $hst[$i]["machine_id"] . "' ";

            $gantt24 = $table->ReadTable($tableName, $where, " ORDER BY start");
            for ($ii = 0; $ii < count($gantt24); $ii++) {
                if ($gantt24[$ii]['start'] < $start) {
                    $gantt24[$ii]['start'] = $start;
                }
                if ($gantt24[$ii]['end'] > $end) {
                    $gantt24[$ii]['end'] = $end;
                }
            }

            $inCyle24 = 0;
            for ($ii = 0; $ii < count($gantt24); $ii++) {
                $inCyle24 += $gantt24[$ii]['end'] - $gantt24[$ii]['start'];
            }

            $condStart = $shift_start / 1000;
            $condEnd = $shift_end / 1000;
            $where = "`machine_id` = '" . $hst[$i]["machine_id"] . "' ";
            $where .= " AND `status` = 'In Cycle' ";
            $where .= " AND `end` > $condStart ";
            $where .= " AND `start` < $condEnd ";
            $sql =  "SELECT start, end, interface " .
                "FROM `$tableName` " .
                "WHERE $where ";
            $ganttList = $table->ExecuteQuery_Simple($sql);
           
            $interface = "";
            if (count($ganttList) > 0) {
                for ($ii = 0; $ii < count($ganttList); $ii++) {
                    if ($ganttList[$ii]['start'] < $condStart) {
                        $ganttList[$ii]['start'] = $condStart;
                    }
                    if ($ganttList[$ii]['end'] > $condEnd) {
                        $ganttList[$ii]['end'] = $condEnd;
                    }
                }
                $interface = $ganttList[0]["interface"];
            }
            $hst[$i]["interface"] = $interface;

            $timeDiff = $shift_end - $shift_start;
            if ($current_time < $shift_end && $current_time > $shift_start) {
                $timeDiff = $current_time - $shift_start;
            }

            if (strpos($interface, "ESP") !== false) {
                if (count($ganttList) > 0) {
                    $inCyle = 0;
                    for ($ii = 0; $ii < count($ganttList); $ii++) {
                        $inCyle += $ganttList[$ii]['end'] - $ganttList[$ii]['start'];
                    }
                    $hst[$i]["availability"] = $inCyle * 100 * 1000 / $timeDiff;
                    $hst[$i]["Utilization"] = $inCyle24 * 100 * 1000 / $current_passed_time;
                    $hst[$i]["goodParts"] = 0;
                    $hst[$i]["badParts"] = 0;
                    $hst[$i]["oee"] = $hst[$i]["availability"];
                    $hst[$i]["performance"] = 0;
                    $hst[$i]["quality"] = 0;
                }
            } else {
                // Read HST table

                $tableName = "$customer_id" . "_shifts";

                $where = "`machine` = '" . $hst[$i]["machine_id"] . "' ";
                $where .= " AND `startTime` >= '$shift_start' ";
                $where .= " AND `stopTime` <= '$shift_end' ";

                $sql =  "SELECT SUM(inCycle) as inCycle, SUM(goodParts) as goodParts, SUM(badParts) as badParts " .
                    "FROM `$tableName` " .
                    "WHERE $where ";
                $hstList = $table->ExecuteQuery_Simple($sql);
                if (count($hstList) > 0) {
                    $inCyle = $hstList[0]["inCycle"] !== null ? $hstList[0]["inCycle"] : 0;
                    $goodParts = $hstList[0]["goodParts"] !== null ? $hstList[0]["goodParts"] : 0;
                    $badParts = $hstList[0]["badParts"] !== null ? $hstList[0]["badParts"] : 0;

                    $hst[$i]["Utilization"] = $inCyle24 * 100 * 1000 / $current_passed_time;
                    $hst[$i]["goodParts"] = $goodParts;
                    $hst[$i]["badParts"] = $badParts;
                    $hst[$i]["availability"] = $inCyle * 100 / $timeDiff;
                    $hst[$i]["quality"] = $goodParts + $badParts > 0 ?  $goodParts * 100 / ($goodParts + $badParts) : 0;
                    $hst[$i]["oee"] = $hst[$i]["availability"] * $hst[$i]["quality"] / 100;
                }

                $sql =  "SELECT * FROM `$tableName` WHERE $where ";
                $allHstList = $table->ExecuteQuery_Simple($sql);
                $performance = 0;
                if (count($allHstList) > 0) {
                    foreach ($allHstList as $oneHst) {
                        $inCyle = $oneHst["inCycle"] !== null ? $oneHst["inCycle"] : 0;
                        $goodParts = $oneHst["goodParts"] !== null ? $oneHst["goodParts"] : 0;
                        $badParts = $oneHst["badParts"] !== null ? $oneHst["badParts"] : 0;
                        $targetCycleTime = $oneHst["targetCycleTime"] !== null ? $oneHst["targetCycleTime"] : 0;

                        $weigth = ($oneHst["stopTime"] - $oneHst["startTime"]) / ($shift_end - $shift_start);
                        if ($inCyle > 0) {
                            $performance = $performance + $weigth * ($targetCycleTime * ($goodParts + $badParts)) / $inCyle;
                        }
                    }
                }
                $hst[$i]["performance"] = $performance * 100;
            }
        }
        
*/
        $res = array();
        $res['machines'] = $machines;
        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
} else {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
