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
	if (isset($params['days']) && $params['days'] != ''){
		$days = $params['days'];
	}
	try {
		$sql = "SELECT * FROM `tanktimes` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$tank_times = array();
		if (count($res) > 0) {
			foreach ($res as $r){
				$newR = null;
				$newR["part_id"] = $r["part_id"];
				$newR["machine_id"] = $r["machine_id"];
				$newR["operator"] = $r["operator"];
				$newR["rm_lot"] = $r["rm_lot"];
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

		$sql = "SELECT * FROM `cleaning_station` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$cleaning_station = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$cleaning_station[] = $newR;
			}
		}
		$result["cleaning_station"] = $cleaning_station;

		$sql = "SELECT * FROM `paint_station` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$paint_station = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$paint_station[] = $newR;
			}
		}
		$result["paint_station"] = $paint_station;

		$sql = "SELECT * FROM `blast_station` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$blast_station = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$blast_station[] = $newR;
			}
		}
		$result["blast_station"] = $blast_station;


		$sql = "SELECT * FROM `assembly_station1` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$assembly_station1 = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$assembly_station1[] = $newR;
			}
		}
		$result["assembly_station1"] = $assembly_station1;

		$sql = "SELECT * FROM `assembly_station3` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$assembly_station3 = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$assembly_station3[] = $newR;
			}
		}
		$result["assembly_station3"] = $assembly_station3;

		$sql = "SELECT * FROM `blu136_assembly` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$blu136_assembly = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$blu136_assembly[] = $newR;
			}
		}
		$result["blu136_assembly"] = $blu136_assembly;

		$sql = "SELECT * FROM `quality_station` where TIMESTAMP(DATE_SUB(NOW(), INTERVAL $days day)) <= created_at And `customer_id`='$customerId' ORDER By timestamp desc";
		$res = $table->ExecuteQuery_Simple($sql);
		$quality_station = array();
		if (count($res) > 0) {
			foreach ($res as $r) {
				$newR = $r;
				$newR["time"] = $r["date"] . " " . $r["time"];
				$quality_station[] = $newR;
			}
		}
		$result["quality_station"] = $quality_station;

		
		echo $result_theme->MakeResult(true, $result);
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, array(), json_encode($err));
	}
}


