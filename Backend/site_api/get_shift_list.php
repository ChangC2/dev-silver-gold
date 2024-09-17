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
$machine = isset($params['machine']) != '' ? $params['machine'] : '';
$operator = isset($params['operator']) != '' ? $params['operator'] : '';
$jobID = isset($params['jobID']) != '' ? $params['jobID'] : '';
$days = isset($params['days']) != '' ? $params['days'] : 30;
$timezone = isset($params['timezone']) != '' ? $params['timezone'] : 0;

$tableName = $customerId . "_shifts";

$table = new TableMgr();
$result_theme = new Result_theme();

if ($customerId == '') {
	echo $result_theme->MakeResult(false, array(), "no data");
} else {
	$days = 7;
	if (isset($params['days']) && $params['days'] != '') {
		$days = $params['days'] - 1;
	}

	$where = "substring(TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)), 1, 10) <= substring(date, 1, 10)";
	$where .= " And `machine`='$machine'";
	if ($operator !== '') {
		$where .= " And `operator`='$operator'";
	}
	if ($jobID !== '') {
		$where .= " And `jobID`='$jobID'";
	}

	$sql = "SELECT SUM(goodParts) as goodParts, SUM(badParts) as badParts, SUM(inCycle) as inCycle, SUM(oee) as oee, SUM(availability) as availability, SUM(quality) as quality, SUM(performance) as performance, substring(date, 1, 10) as date FROM $tableName WHERE $where GROUP BY substring(date, 1, 10) ORDER BY date DESC";

	$columns = ["goodParts", "badParts", "inCycle", "oee", "availability", "quality", "performance", "date"];
	try {
		$res = $table->ExecuteQuery_With_Column($columns, $sql, $timezone);
		if (count($res) > 0) {
			$result["shifts"] = $res;
			echo $result_theme->MakeResult(true, $result);
		} else {
			echo $result_theme->MakeResult(false, "no data");
		}
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, array(), json_encode($err));
	}
}
