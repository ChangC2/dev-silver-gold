<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customerId = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

if ($customerId == '') {
	echo $result_theme->MakeResult(false, "", "no data");
} else {
	try {
		$result = array();
		$sql = "SELECT part_id FROM `tanktimes` where `customer_id`='$customerId' AND part_id <> '' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result['Phosphate Line 1'] = $res[0]["part_id"];  
		}

		$sql = "SELECT part_id FROM `cleaning_station` where `customer_id`='$customerId' AND part_id != '' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Cleaning Station"] = $res[0]["part_id"]; 
		}

		$sql = "SELECT part_id FROM `paint_station` where `customer_id`='$customerId' AND part_id != '' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Paint Booth"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `blast_station` where `customer_id`='$customerId' AND part_id != '' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Blast Booth"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `assembly_station1` where `customer_id`='$customerId' AND part_id != '' AND machine_id = 'Assembly Blu/137' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Blu/137"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `assembly_station1` where `customer_id`='$customerId' AND part_id != '' AND machine_id = 'Assembly Blu/137-2' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Blu/137-2"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `blu136_assembly` where `customer_id`='$customerId' AND part_id != ''  AND machine_id = 'Assembly Blu/136' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Blu/136"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `blu136_assembly` where `customer_id`='$customerId' AND part_id != ''  AND machine_id = 'Assembly Blu/136-2' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Blu/136-2"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `blu136_assembly` where `customer_id`='$customerId' AND part_id != ''  AND machine_id = 'Assembly Blu/136-3' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Blu/136-3"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `blu136_assembly` where `customer_id`='$customerId' AND part_id != ''  AND machine_id = 'Assembly Blu/136-4' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Blu/136-4"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `assembly_station3` where `customer_id`='$customerId' AND part_id != '' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Assembly Sub"] = $res[0]["part_id"];
		}

		$sql = "SELECT part_id FROM `quality_station` where `customer_id`='$customerId' AND part_id != '' ORDER By timestamp desc ";
		$res = $table->ExecuteQuery_Simple($sql);
		if (count($res) > 0) {
			$result["Quality Control"] = $res[0]["part_id"];
		}

		echo $result_theme->MakeResult(true, $result);
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, "", json_encode($err));
	}
}
