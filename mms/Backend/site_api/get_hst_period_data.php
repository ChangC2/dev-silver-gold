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
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$res = array();
if ($machine_id == '' || $machine_id == '' || $machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        // read hst data
        $tableName = "$customer_id" . "_hst";
        $where = "`machine_id`='$machine_id' AND `date`>='$startDate' AND `date` <= '$endDate'";
        $sql = "SELECT `date`, `oee`, `availability`, `quality`, `performance` FROM `$tableName` WHERE $where";
        $OEE = $table->ReadTable($tableName, $where, "");
        $res['oee'] = $OEE;
        $res['oee_sql'] = $sql;

        // read gantt data
        $timezone = intval($timezone) * 3600;

        $startTime = strtotime($startDate) - $timezone;
        $endTime = strtotime($endDate) - $timezone;
        $endTime += 86400;
        // $startDate = date_create_from_format('m/d/Y', $startDate);
        // $startTime = $startDate->getTimestamp() - $timezone;
        // $endDate = date_create_from_format('m/d/Y', $endDate);
        // $endDate->modify('+1 day');
        // $endTime = $endDate->getTimestamp() - $timezone;

        $tableName = "$customer_id" . "_ganttdata";
        // `end` >= $start AND `start`<=$end 
        $where = " `end` >= $startTime AND `start`<=$endTime AND `machine_id`='$machine_id'";
        $sql = "SELECT `status`, `start`, `end`, `color` FROM `$tableName` WHERE $where";
        $res['gantt_sql'] = $sql;
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

        if ($timeCounter > $totalTime) {
            echo $result_theme->MakeResult(false, "");    
        } else {
            echo $result_theme->MakeResult(true, $res);
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
