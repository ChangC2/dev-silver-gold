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
$machineId = isset($params['machine_id']) != '' ? $params['machine_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();


if ($customerId == '' || $machineId == '') {
	echo $result_theme->MakeResult(false, array(), "no data");
} else {
	$days = 7;
	if (isset($params['days']) && $params['days'] != ''){
		$days = $params['days'];
	}
	try {
		$sql = "SELECT * FROM `stage` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' And `machine_id`='$machineId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$stages = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["datetime"] = substr($r["datetime"],0, 10)."\n". substr($r["datetime"], 11, 5);
				$newR["p_datetime"] = $r["p_date"] . "\n" . $r["p_time"];
				$stages[] = $newR;
			}
		}
		echo $result_theme->MakeResult(true, $stages);
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, array(), json_encode($err));
	}
}


