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
$need_downtimes = isset($params['need_downtimes']) != 0 ? $params['need_downtimes'] : 0;
date_default_timezone_set('UTC');
$timezone = $timezone * 3600;

$utilities = array();
$inCycles = array();
$dates = array();
$timestampes = array();
$downtimes = array();

if ($customer_id != '') {
    $start = strtotime($startDate) - $timezone;
    $end = strtotime($endDate) - $timezone;
    $tableName = $customer_id . "_ganttdata";
    $sql = "SELECT start,end FROM `$tableName` where `end` >= $start AND `start`<=$end and `machine_id` = '$machine_id' and status = 'In Cycle' order by start";
    $gantt = $table->ExecuteQuery_With_Column(array('start', 'end'), $sql);

    $sDate = new DateTime($startDate);
    $eDate = new DateTime($endDate);
    while ($sDate < $eDate) {
        $dates[] = $sDate->format('m/d');
        $timestampes[] = $sDate->getTimestamp() - $timezone;
        $inCycles[] = 0;
        $sDate->modify('+1 day');
    }

    for ($i = 0; $i < count($gantt); $i++) {
        for ($j = 0; $j < count($timestampes); $j++) {
            if ((int)$gantt[$i]["end"] > $timestampes[$j] && (int)$gantt[$i]["start"] < $timestampes[$j] + 24 * 3600) {
                $endValue = (int)$gantt[$i]["end"] > $timestampes[$j] + 24 * 3600 ? $timestampes[$j] + 24 * 3600 : (int)$gantt[$i]["end"];
                $startValue = (int)$gantt[$i]["start"] < $timestampes[$j] ? $timestampes[$j] : (int)$gantt[$i]["start"];
                $inCycles[$j] += $endValue - $startValue;
            }
        }
    }
    $current_time = time() * 1000;
    for ($i = 0; $i < count($dates); $i++) {
        $current_passed_time = $current_time - $timestampes[$i] * 1000  > 24 * 3600 * 1000 ? 24 * 3600 * 1000 : $current_time - $start * 1000;
        // $utilities[] = array("day" => $dates[$i], "utilization" => number_format((float)$inCycles[$i] * 100 * 1000 / $current_passed_time, 2, '.', ''));
        $utilities[] = array("day" => $dates[$i], "utilization" => number_format((float)$inCycles[$i] / 3600, 2, '.', ''));
    }

    if ($need_downtimes) {
        $aWeekAgo = $end - 24 * 7 * 3600;
        $sql = "SELECT start,end,status,color FROM `$tableName` where `end` >= $aWeekAgo AND `start`<=$end and `machine_id` = '$machine_id' order by start";
        $gantt = $table->ExecuteQuery_With_Column(array('start', 'end', 'status', 'color'), $sql);
        if (count($gantt) > 0) {
            $gantt[0]["start"] = (int)$gantt[0]["start"] < $aWeekAgo ? $aWeekAgo : (int)$gantt[0]["start"];
            $gantt[count($gantt) - 1]["end"] = (int)$gantt[count($gantt) - 1]["end"] > $end ? $end : (int)$gantt[count($gantt) - 1]["end"];

            for ($i = 0; $i < count($gantt); $i++) {
                if (count($downtimes) == 0 && $gantt[$i]["status"] != "Offline") {
                    $downtimes[] = array("status" => $gantt[$i]["status"], "color" => $gantt[$i]["color"], "duration" => (int)$gantt[$i]["end"] - (int)$gantt[$i]["start"]);
                }
                $isIn = false;
                for ($j = 0; $j < count($downtimes); $j++) {
                    if ($downtimes[$j]["status"] == $gantt["$i"]["status"]) {
                        $isIn = true;
                        $downtimes[$j]["duration"] += (int)$gantt["$i"]["end"] - (int)$gantt["$i"]["start"];
                        break;
                    }
                }
                if (!$isIn && $gantt[$i]["status"] != "Offline") {
                    $downtimes[] = array("status" => $gantt[$i]["status"], "color" => $gantt[$i]["color"], "duration" => (int)$gantt[$i]["end"] - (int)$gantt[$i]["start"]);
                }
            }
        }
        $totalDowntimes = 0;
        for ($j = 0; $j < count($downtimes); $j++) {
            $downtimes[$j]["duration"] = $downtimes[$j]["duration"] / 3600;
            $totalDowntimes += $downtimes[$j]["duration"];
        }
        $downtimes[] = array("status" => "Offline", "color" => "#000000", "duration" => 24 * 7 - $totalDowntimes);
    }
    $output_data = array(
        "downtimes" => $downtimes,
        "utilities" => $utilities,
    );
    echo $result_theme->MakeResult(true, $output_data);
} else {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
