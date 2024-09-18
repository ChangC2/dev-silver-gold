<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$res = array();
if ($customer_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        $tableName = $customer_id . "_jobdata";
        $sql = "SELECT DISTINCT jobID FROM $tableName";
        $jobList = $table->ExecuteQuery_Simple($sql);
        $jobs = array();
        for ($i = 0; $i < count($jobList); $i++) {
            $one_record['jobID'] =  $jobList[$i][0];
            $res[] = $one_record;
        }
        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
