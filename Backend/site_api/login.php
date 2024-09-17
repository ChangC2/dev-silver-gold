<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

// DeLog($req);
$tableName = "user_login_barcode";

$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$username = isset($params['username']) ? $params['username'] : '';
$password = isset($params['password']) ? $params['password'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

try {
	$password = md5($password);
	$res = $table->ReadTable($tableName, "`username`='$username' AND `password`='$password'", "");
	if (count($res) == 0) {
		if ($customer_id != '') {
			$tableName1 = $params['customer_id'] . "_user";
			$res = $table->ReadTable($tableName1, "`username`='$username' AND `password`='$password'", "");
		} 
	}

	if (count($res) == 0) {
		echo $result_theme->MakeResult(false, $res, "Authentication failed.");
	} else {
		$labor_setting = $table->ReadTable("labor_setting", "`user_id`='" . $res[0]['id'] . "'", "");
		if (count($labor_setting) > 0) {
			$res[0]["time_format"] = $labor_setting[0]["time_format"];
			$res[0]["is_started_auto"] = $labor_setting[0]["is_started_auto"];
			$res[0]["start_time"] = $labor_setting[0]["start_time"];
		} else {
			$res[0]["time_format"] = "";
			$res[0]["is_started_auto"] = "";
			$res[0]["start_time"] = "0";
		}
		if ($customer_id != '') {
			$customer_id = trim($customer_id);
			$tableName = $customer_id . "_info";
			$customerInfo = $table->ReadTable($tableName);
			if (isset($customerInfo) && count($customerInfo) > 0) {
				$res[$customer_id]  = $table->ReadTable($tableName)[0];
				$tableName = $customer_id . "_machine_group";
				$res[$customer_id]['groupInfo'] = $table->ReadTable($tableName);
				$res[$customer_id]['groupTable'] = $tableName;
			}
		}
		echo $result_theme->MakeResult(true, $res, "Login Success");
	}
} catch (Exception $err) {
	echo $result_theme->MakeResult(false, "", json_encode($err));
}
