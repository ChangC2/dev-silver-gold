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
$opertor = isset($params['opertor']) != '' ? $params['opertor'] : '';
$jobID = isset($params['jobID']) != '' ? $params['jobID'] : '';
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$res = array();
if ($machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        // read hst data
        $tableName = "$customer_id" . "_shifts";
        $where = "`machine_id`='$machine_id' AND `date`>='$startDate' AND `date` <= '$endDate'";
        if ($operator != ""){
            $where = $where . "`operator`='$operator'";
        }
        if ($jobID != ""){
            $where = $where . "`jobID`= '$jobID'";
        }
        $hstData = $table->ReadTable($tableName, $where, "");
        echo $result_theme->MakeResult(true, $hstData);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}


// $sql = "SELECT LEFT(`date`, 10), SUM(goodParts), SUM(badParts) FROM visser_shifts WHERE machine = 'NHX6300 #1' GROUP BY LEFT(`date`, 10)";
// try {
//     $res = $table->readDataByQuery($sql);
//     echo $result_theme->MakeResult(true, $res);
// } catch (Exception $err) {
//     echo $result_theme->MakeResult(false, "", json_encode($err));
// }
