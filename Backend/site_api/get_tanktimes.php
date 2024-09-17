<?php

use LDAP\Result;

include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$tableName = "tanktimes";
$customerId = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();


if ($customerId == '') {
	echo $result_theme->MakeResult(false, array(), "no data");
} else {
	$days = 7;
	if (isset($params['days']) && $params['days'] != '') {
		$days = $params['days'];
	}
	try {
		$sql = "SELECT * FROM `tanktimes` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$tank_times = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = null;
				$newR["part_id"] = $r["part_id"];
				$newR["machine_id"] = $r["machine_id"];
				$newR["operator"] = $r["operator"];
				$newR["finish_time"] = $r["date"] . " " . $r["time"];
				$newR["v1"] = date("i:s", $r["ttime1"] / 1000) . "\n" . $r["ttemp1"];
				$newR["v2"] = date("i:s", $r["ttime2"] / 1000) . "\n" . $r["ttemp2"];
				$newR["v3"] = date("i:s", $r["ttime3"] / 1000) . "\n" . $r["ttemp3"];
				$newR["v4"] = date("i:s", $r["ttime4"] / 1000) . "\n" . $r["ttemp4"];
				$newR["v5"] = date("i:s", $r["ttime5"] / 1000) . "\n" . $r["ttemp5"];
				$newR["v6"] = date("i:s", $r["ttime6"] / 1000) . "\n" . $r["ttemp6"];
				$newR["v7"] = date("i:s", $r["ttime7"] / 1000) . "\n" . $r["ttemp7"];
				$newR["v8"] = date("i:s", $r["ttime8"] / 1000) . "\n" . $r["ttemp8"];
				$tank_times[] = $newR;
			}
		}
		$result["tank_times"] = $tank_times;
		echo $result_theme->MakeResult(true, $tank_times);
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, array(), json_encode($err));
	}
}
