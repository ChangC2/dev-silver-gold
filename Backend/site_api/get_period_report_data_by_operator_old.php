<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$machine = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
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
        $tableName = "$customer_id" . "_status";
        $sql = "SELECT machine_id FROM $tableName ";
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
            $tableName1 = "$customer_id" . "_hst";

            $tableName2 = "$customer_id" . "_status";

            $where = "$tableName1.machine_id IN ('" . implode("','", array_map('strVal', $machine_id)) . "') AND str_to_date($tableName1.date,'%m/%d/%Y')>=str_to_date('$startDate','%m/%d/%Y') AND str_to_date($tableName1.date,'%m/%d/%Y')<=str_to_date('$endDate','%m/%d/%Y')";
            $sql = "SELECT * FROM `$tableName1`  WHERE $where ORDER BY machine_id ASC, str_to_date(date,'%m/%d/%Y') DESC";
            $OEE = $table->ExecuteQuery_Simple($sql);

            $newOEE = array();
            foreach ($OEE as $o) {
                $m_id = $o['machine_id'];
                $tableName = "$customer_id" . "_ganttdata";
                $sTime = strtotime($o['date']) - $timezone;
                $eTime = $sTime + 86400;
                $where = "`end` <= $eTime AND `start`>=$sTime AND `machine_id` = '$m_id' AND `job_id` <> ''";
                $sql = "SELECT DISTINCT job_id FROM `$tableName` WHERE $where";
                $gantts = $table->ExecuteQuery_Simple($sql);
                //$jobId = "";
                $jobIds = "";
                $ops = "";

                $newO = $o;

                if (count($gantts) > 0) {
                    $jobId = $gantts[0]['job_id'];
                    $tableName = "$customer_id" . "_jobdata";
                    $where = "`jobID` = '$jobId' AND `aux1data` <> '' AND `aux2data` <> '' AND `aux3data` <> '' AND `aux1data` <> 'null' AND `aux2data` <> 'null' AND `aux3data` <> 'null'";
                    $sql = "SELECT * FROM `$tableName` WHERE $where LIMIT 1";

                    $jobdatas = $table->ExecuteQuery_Simple($sql);
                    $sq_inches = 0;
                    if (count($jobdatas) > 0) {
                        $sq_inches = (($jobdatas[0]['aux1data'] + $jobdatas[0]['aux1data']) * $jobdatas[0]['aux1data'] * ($o['goodParts'] + $o['badParts']));
                    }
                    $newO['sq_inches'] = $sq_inches;

                    if (count($job_id) == 0) { // if there is no job ID's selected by the user in the report table, this will enter all of the jobs
                        foreach ($gantts as $gantt) {

                            if ($jobIds == '')
                                $jobIds .= $gantt['job_id'];
                            else
                                $jobIds .= ", ".$gantt['job_id'];
                        }
                    } else { // if there are job ID's selected by the user, this will only return the matching tables with those jobs
                        foreach ($gantts as $gantt) {
                            for ($p = 0; $p < count($job_id); $p++) {
                                if (strpos($gantt['job_id'],$job_id[$p] )!== false) {
                                    if ($jobIds == '') {
                                        $jobIds .= $job_id[$p];//$gantt['job_id'];
                                    } else {

                                        $jobIds .= ", ".$job_id[$p];//.$gantt['job_id']; // Adds a space and comma if there is more than 2 operators
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    }


                    $tableName = "$customer_id" . "_ganttdata"; ////// add operator variable here `Operator` LIKE '%$operator%'  <> '' LIKE '%$operator[0]%'
                    $where = "`end` <= $eTime AND `start`>=$sTime AND `machine_id` = '$m_id' AND `Operator` <> '' ";
                    $sql = "SELECT DISTINCT Operator FROM `$tableName` WHERE $where";


                    $gantts = $table->ExecuteQuery_Simple($sql);

                    // Checks each operator that the user selected against the existing data and shows only that information
                    

                        if(count($operator) == 0){ //count($operator) == 0
                            foreach ($gantts as $gantt) {
                                if ($ops == ''){
                                    $ops .= $gantt['Operator'];
                                }
                                else{
                                    $ops .= ", " . $gantt['Operator'];
                                }
                            }
                        }

                        else{
                            foreach ($gantts as $gantt) {
                                for ($p = 0; $p < count($operator); $p++) {
                                    if (strpos($gantt['Operator'], $operator[$p])!== false) {
                                        if ($ops == '') {
                                            $ops .= $operator[$p];
                                        } else {
                                            $ops .= ", " . $operator[$p]; // Adds a space and comma if there is more than 2 operators
                                        }
                                    } 
                                        continue;
                                    }
                                }
                        }
                    
                        
            
                } else {
                    $newO['sq_inches'] = '';
                }

                $newO['orders'] = $jobIds;
                $newO['Operator'] = $ops;
                $newOEE[] = $newO;
            }

            $res['oee'] = $newOEE;

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
        }
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
