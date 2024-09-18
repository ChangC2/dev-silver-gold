<?php

use LDAP\Result;

include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customerId = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$progNum = isset($params['progNum']) != '' ? $params['progNum'] : '';
$alarmType = isset($params['alarmType']) != '' ? $params['alarmType'] : '';
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';

$tableName = "alarms";

$table = new TableMgr();
$result_theme = new Result_theme();

if ($customerId == '') {
	echo $result_theme->MakeResult(false, array(), "no data");
} else {
	$where = "`customer_id` = '$customerId'";
	
	if ($machine_id !== ''){
		$where .= " And `machine_id`='$machine_id'";
	}
	if ($progNum !== '') {
		$where .= " And `progNum`='$progNum'";
	}
	if ($alarmType !== '') {
		$where .= " And `alarmType`='$alarmType'";
	}

	if ($startDate !== '') {
		$where .= " And `date` >= '$startDate'";
	}

	if ($endDate !== '') {
		$where .= " And `date` <= '$endDate'";
	}

	$sql = "SELECT machine_id,date,time,progNum,tool,section,channel,elapsedTime,alarmType  FROM $tableName WHERE $where  ORDER BY date DESC";

	$columns = ["machine_id", "date", "time", "progNum", "tool", "section", "channel","elapsedTime", "alarmType"];
	try {
		$res = $table->ExecuteQuery_With_Column($columns, $sql);
		if (count($res) > 0) {
			$result["alarms"] = $res;
			$result["where"] = $where;
			echo $result_theme->MakeResult(true, $result);
		} else {
			$result["sql"] = $sql;
			echo $result_theme->MakeResult(false, $result, "no data");
		}
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, array(), json_encode($err));
	}
}


