<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$tableName = $customer_id . "_status";

try {
	$res = $table->ReadTable($tableName, "", "");
	$machineList = [];
	foreach ($res as $machine) {
		$newMachine = $machine;
		$tableName = "tbl_app_setting";
		$where = "(`machine_id`='" . $machine["machine_id"] . "' AND `factory_id`='$customer_id')";
		$appSetting = $table->ReadTable($tableName, $where, "");
		if (count($appSetting) == 0) {
			$newMachine["device_id"] = "";
		} else {
			$newMachine["device_id"] = $appSetting[0]["device_id"];
		}
		$machineList[] = $newMachine;
	}
	echo $result_theme->MakeResult(true, $machineList);
} catch (Exception $err) {
	echo $result_theme->MakeResult(false, "", json_encode($err));
}
