<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$operator = isset($params['operator']) != '' ? $params['operator'] : '';
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;
$timezone = intval($timezone) * 3600;
$job_id = isset($params['job_id']) != '' ? $params['job_id'] : '';
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';
$startTime = strtotime($startDate) - $timezone;
$endTime = strtotime($endDate) - $timezone + 86400;

$table = new TableMgr();
$result_theme = new Result_theme();


$res = array();
$machine_id = array();
if ($operator == '' && $job_id != '') { // Check if no operator selected
    echo $result_theme->MakeResult(false, "", "no data");
}

//// Check if more than one operator
else {
    try {
        //for ($j = 0; $j < count($operator); $j++) {
        $tableName = "$customer_id" . "_shifts";

        $where = "operator IN ('" . implode("','", array_map('strVal', $operator)) . "') " .
            "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')>=str_to_date('$startDate','%m/%d/%Y') " .
            "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')<=str_to_date('$endDate','%m/%d/%Y')";

        $sql = "SELECT DISTINCT(machine) FROM $tableName ";
        $machineList = $table->ExecuteQuery_Simple($sql);
        for ($i = 0; $i < count($machineList); $i++) {
            $machine_id[] = $machineList[$i][0];
        }

        if (count($machine_id) == 0) {
            echo $result_theme->MakeResult(false, "", "No Machine");
        } else {
            $tableName = "tbl_app_setting";
            $where = "(`factory_id`='$customer_id' AND `machine_id` IN ('" . implode("','", array_map('strVal', $machine_id)) . "'))";
            $appSettings = $table->ReadTable($tableName, $where, "");
            $res['appSettings'] = $appSettings;

            // read hst data
            $tableName = "$customer_id" . "_shifts";

            $where = "machine IN ('" . implode("','", array_map('strVal', $machine_id)) . "') " .
                // "AND operator IN ('" . implode("','", array_map('strVal', $operator)) . "') " .
                "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')>=str_to_date('$startDate','%m/%d/%Y') " .
                "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')<=str_to_date('$endDate','%m/%d/%Y')";

            $sql =  "SELECT id, machine as machine_id, substring(date, 1, 10) as date" .
                ", SUM(oee) as oee, SUM(availability) as availability" .
                ", SUM(quality) as quality, SUM(performance) as performance" .
                ", SUM(goodParts) as goodParts, SUM(badParts) as badParts" .
                ", SUM(inCycle) as inCycle, SUM(uncat) as uncat" .
                ", SUM(offline) as offline, SUM(r1t) as r1t" .
                ", SUM(r2t) as r2t, SUM(r3t) as r3t" .
                ", SUM(r4t) as r4t, SUM(r5t) as r5t" .
                ", SUM(r6t) as r6t, SUM(r7t) as r7t" .
                ", SUM(r8t) as r8t" .
                ", SUM(aux1data) as aux1data" .
                ", SUM(aux2data) as aux2data" .
                ", SUM(aux3data) as aux3data" .
                ", SUM(rework) as rework" .
                ", SUM(setup) as setup " .
                "FROM `$tableName` " .
                "WHERE $where " .
                "GROUP BY substring(date, 1, 10), `machine` " .
                "ORDER BY `machine` ASC, str_to_date(date,'%Y-%m-%d') DESC";

            $OEE = $table->ExecuteQuery_Simple($sql);
            $res['sql'] = $sql;
            $newOEE = array();
            foreach ($OEE as $o) {
                $newO = $o;
                $m_id = $o['machine_id'];
                $sql = "SELECT GROUP_CONCAT(orders.jobID SEPARATOR ',') as jobIds " .
                    "FROM (SELECT DISTINCT(jobID) FROM $tableName " .
                    "WHERE machine='$m_id' " .
                    "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')>=str_to_date('$startDate','%m/%d/%Y') " .
                    "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')<=str_to_date('$endDate','%m/%d/%Y') " .
                    "AND `jobID` <> ''" .
                    "GROUP BY substring(date, 1, 10),`jobID` ORDER BY `machine` ASC, str_to_date(date,'%Y-%m-%d') DESC) orders";
                $jobIds = "";
                $jobIdsTable = $table->ExecuteQuery_Simple($sql);
                if (count($jobIdsTable) > 0) {
                    $jobIds = $jobIdsTable[0]['jobIds'];
                }
                if ($jobIds === null) {
                    $jobIds = "";
                }
                $newO['orders'] = $jobIds;

                $sql = "SELECT GROUP_CONCAT(ops_table.operator SEPARATOR ',') as ops " .
                    "FROM (SELECT DISTINCT(operator) FROM $tableName " .
                    "WHERE machine='$m_id' " .
                    "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')>=str_to_date('$startDate','%m/%d/%Y') " .
                    "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')<=str_to_date('$endDate','%m/%d/%Y') " .
                    "AND `operator` <> ''" .
                    "GROUP BY substring(date, 1, 10),`operator` ORDER BY `machine` ASC, str_to_date(date,'%Y-%m-%d') DESC) ops_table";
                $ops = "";
                $opsTable = $table->ExecuteQuery_Simple($sql);
                if (count($opsTable) > 0) {
                    $ops = $opsTable[0]['ops'];
                }
                if ($ops === null) {
                    $ops = "";
                }
                $newO['Operator'] = $ops;

                $newO['Utilization'] = "0";

                $sq_inches = 1;
                if ($o['aux1data'] <> 0) {
                    $sq_inches = $sq_inches * $o['aux1data'];
                }
                if ($o['aux2data'] <> 0) {
                    $sq_inches = $sq_inches * $o['aux2data'];
                }
                if ($o['aux3data'] <> 0) {
                    $sq_inches = $sq_inches * $o['aux3data'];
                }
                if ($o['goodParts'] <> 0) {
                    $sq_inches = $sq_inches * $o['goodParts'];
                }
                if ($o['aux1data'] == 0 && $o['aux2data'] == 0 && $o['aux3data'] == 0) {
                    $sq_inches = 0;
                }
                $newO['sq_inches'] = $sq_inches;

                $newOEE[] = $newO;
            }

            $res['oee'] = $newOEE;

            // $tableName = "$customer_id" . "_ganttdata";
            // $where = " `end` <= $endTime AND `start`>=$startTime AND `machine_id` IN ('" . implode("','", array_map('strVal', $machine_id)) . "')";
            // $sql = "SELECT `status`, `start`, `end`, `color` FROM `$tableName` WHERE $where";

            // $ganttList = $table->ExecuteQuery_Simple($sql);

            // // Filtering gantt
            // $statusList = array();
            // $colorList = array();
            // $timeList = array();
            // $timeCounter = 0;

            // for ($i = 0; $i < count($ganttList); $i++) {
            //     $status = $ganttList[$i][0];
            //     $from =  $ganttList[$i][1];
            //     $to =  $ganttList[$i][2];
            //     $color = $ganttList[$i][3];

            //     if ($from < $startTime) {
            //         $from = $startTime;
            //     }
            //     if ($to > $endTime) {
            //         $to = $endTime;
            //     }

            //     $timeCounter += intval($to) - intval($from);
            //     if (in_array($status, $statusList)) {
            //         $timeList[$status] += intval($to) - intval($from);
            //     } else {
            //         $timeList[$status] = intval($to) - intval($from);
            //         $colorList[$status] = $color;
            //         $statusList[] = $status;
            //     }
            // }

            // // Add Offline
            // $totalTime = $endTime - $startTime;
            // if ($timeCounter < $totalTime) {
            //     $offlineStatus = "Offline";
            //     $statusList[] = $offlineStatus;
            //     $colorList[$offlineStatus] = "#000000";
            //     $timeList[$offlineStatus] = $totalTime - $timeCounter;
            // }

            // $gantt['statusList'] = $statusList;
            // $gantt['colorList'] = $colorList;
            // $gantt['timeList'] = $timeList;
            // $res['gantt'] = $gantt;

            // $res['totalTime'] = $totalTime;
            // $res['calTime'] = $timeCounter;
            // $res['offline'] = $totalTime - $timeCounter;
            // $res['start'] = $startTime;
            // $res['end'] = $endTime;

            echo $result_theme->MakeResult(true, $res);
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
