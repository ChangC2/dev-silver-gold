<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$tableName = "time_savings";
$customerId = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

if ($customerId == '') {
	echo $result_theme->MakeResult(false, "", "no data");
} else {
	$sql = "SELECT machine_id FROM `$tableName` where `customer_id`='$customerId' GROUP By `machine_id` ORDER By created_at asc ";
	try {
		$res = $table->ExecuteQuery_Simple($sql);
		$result = array();
		$result[] = getMachineInfo($customerId, ""); // For All
		if (count($res) > 0) {
			foreach ($res as $r) {
				$result[] =	getMachineInfo($customerId, $r['machine_id']);
			}
		}
		echo $result_theme->MakeResult(true, $result);
	} catch (Exception $err) {
		echo $result_theme->MakeResult(false, "", json_encode($err));
	}
}


function getMachineInfo($customerId, $machineId)
{
	$tableName = "time_savings";
	$table = new TableMgr();
	$newM["machine_id"] = "All";
	$where = "`customer_id`='$customerId'";
	if ($machineId !== "") {
		$newM["machine_id"] = $machineId;
		$where = $where . " AND `machine_id`='" . $machineId . "'";
	}
	$sql = "SELECT progNum FROM `$tableName` where $where GROUP By `progNum` ORDER By created_at asc ";
	$proNums = $table->ExecuteQuery_Simple($sql);
	$pNums = array();
	$pNums[] = getProgInfo($customerId, $machineId, ""); //For All 
	foreach ($proNums as $p) {
		$pNums[] = getProgInfo($customerId, $machineId, $p['progNum']);
	}
	$newM["progNum"] = $pNums;
	return $newM;
}

function getProgInfo($customerId, $machineId, $proNum)
{
	$tableName = "time_savings";
	$table = new TableMgr();
	$newP["progNum"] = "All";
	if ($proNum !== "") {
		$newP["progNum"] = $proNum;
	}

	$where = "`customer_id`='$customerId'";
	if ($machineId !== "") {
		$where = $where . " AND `machine_id`='" . $machineId . "'";
	}
	if ($proNum !== "") {
		$where = $where . " AND `progNum`='" . $proNum . "'";
	}

	$sql = "SELECT timeSavings, learnedTime , elapsedTime, concat(date, ' ', time) as date FROM `$tableName` where $where AND `created_at` >= NOW() - INTERVAL 90 DAY  ORDER By created_at asc ";
	$time_savings = $table->ExecuteQuery_Simple($sql);
	$newP["time_savings"] = $time_savings;

	$sql = "SELECT sum(timeSavings) as timeSavings FROM `$tableName` WHERE $where AND `created_at` >= NOW() - INTERVAL 30 DAY";
	$timeSavings30 = $table->ExecuteQuery_Simple($sql);
	if (count($timeSavings30) > 0) {
		$val = $timeSavings30[0]["timeSavings"];
		if ($val == null) {
			$val = 0;
		}
		$newP["timeSavings30"] = (int)($val / 60);
	}

	$sql = "SELECT sum(timeSavings) as timeSavings FROM `$tableName` WHERE $where AND `created_at` >= NOW() - INTERVAL 7 DAY";
	$timeSavings7 = $table->ExecuteQuery_Simple($sql);
	if (count($timeSavings7) > 0) {
		$val = $timeSavings7[0]["timeSavings"];
		if ($val == null) {
			$val = 0;
		}
		$newP["timeSavings7"] = (int)($val / 60);
	}

	$sql = "SELECT sum(timeSavings) as timeSavings FROM `$tableName` WHERE $where";
	$timeSavingsAll = $table->ExecuteQuery_Simple($sql);
	if (count($timeSavingsAll) > 0) {
		$val = $timeSavingsAll[0]["timeSavings"];
		if ($val == null) {
			$val = 0;
		}
		$newP["timeSavingsAll"] = (int)($val / 60);
	}
	return $newP;
}
