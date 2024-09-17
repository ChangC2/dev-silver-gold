<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;
$timezone = intval($timezone) * 3600;
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';
$startTime = strtotime($startDate) - $timezone;
$endTime = strtotime($endDate) - $timezone + 86400;

$table = new TableMgr();
$result_theme = new Result_theme();


$res = array();

if ($machine_id == '' || $machine_id == '' || $machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        $tableName = "tbl_app_setting";
        $where = "(`factory_id`='$customer_id' AND `machine_id` IN ('" . implode("','", array_map('strVal', $machine_id)) . "'))";
        $appSettings = $table->ReadTable($tableName, $where, "");
        $res['appSettings'] = $appSettings;

        // read hst data
        $tableName = "$customer_id" . "_shifts";

        $where = "machine IN ('" . implode("','", array_map('strVal', $machine_id)) . "') " .
            "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')>=str_to_date('$startDate','%m/%d/%Y') " .
            "AND str_to_date(substring(date, 1, 10),'%Y-%m-%d')<=str_to_date('$endDate','%m/%d/%Y')";

        $sql =  "SELECT id, machine as machine_id, operator as Operator, jobID as Orders, substring(date, 1, 10) as date" .
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
            ", SUM(goodParts * aux3data * (aux1data + aux2data)) as sq_inches " .
            ", SUM((stopTime - startTime - inCycle)) as downtimes " .
            "FROM `$tableName` " .
            "WHERE $where " .
            "GROUP BY substring(date, 1, 10), machine, operator, jobID " .
            "ORDER BY str_to_date(date,'%Y-%m-%d') ASC,`machine` ASC,  `operator` ASC ,  `jobID` ASC";

        $OEE = $table->ExecuteQuery_Simple($sql);
        $res['oee'] = $OEE;


        $sql =  "SELECT id, machine as machine_id, operator as Operator, substring(date, 1, 10) as date" .
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
            ", SUM(goodParts * aux3data * (aux1data + aux2data)) as sq_inches " .
            ", SUM((stopTime - startTime - inCycle)) as downtimes " .
            "FROM `$tableName` " .
            "WHERE $where " .
            "GROUP BY substring(date, 1, 10), machine, operator " .
            "ORDER BY str_to_date(date,'%Y-%m-%d') ASC,`machine` ASC,  `operator` ASC ,  `jobID` ASC";

        $OEE_O = $table->ExecuteQuery_Simple($sql);
        $res['oee_o'] = $OEE_O;

        $tableName = "$customer_id" . "_ganttdata";
        $where = " `end` <= $endTime AND `start`>=$startTime AND `machine_id` IN ('" . implode("','", array_map('strVal', $machine_id)) . "')";
        $sql = "SELECT `status`, `start`, `end`, `color` FROM `$tableName` WHERE $where";

        $ganttList = $table->ExecuteQuery_Simple($sql);

        // Filtering gantt
        $statusList = array();
        $colorList = array();
        $timeList = array();
        $timeCounter = 0;

        for ($i = 0; $i < count($ganttList); $i++) {
            $status = $ganttList[$i][0];
            $from =  $ganttList[$i][1];
            $to =  $ganttList[$i][2];
            $color = $ganttList[$i][3];

            if ($from < $startTime) {
                $from = $startTime;
            }
            if ($to > $endTime) {
                $to = $endTime;
            }

            $timeCounter += intval($to) - intval($from);
            if (in_array($status, $statusList)) {
                $timeList[$status] += intval($to) - intval($from);
            } else {
                $timeList[$status] = intval($to) - intval($from);
                $colorList[$status] = $color;
                $statusList[] = $status;
            }
        }
        // Add Offline
        $totalTime = $endTime - $startTime;
        if ($timeCounter < $totalTime) {
            $offlineStatus = "Offline";
            $statusList[] = $offlineStatus;
            $colorList[$offlineStatus] = "#000000";
            $timeList[$offlineStatus] = $totalTime - $timeCounter;
        }

        $gantt['statusList'] = $statusList;
        $gantt['colorList'] = $colorList;
        $gantt['timeList'] = $timeList;
        $res['gantt'] = $gantt;

        $res['totalTime'] = $totalTime;
        $res['calTime'] = $timeCounter;
        $res['offline'] = $totalTime - $timeCounter;
        $res['start'] = $startTime;
        $res['end'] = $endTime;


        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
