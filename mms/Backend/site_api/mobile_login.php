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
	$loginResponse = $table->ReadTable($tableName, "`username`='$username' AND `password`='$password'", "");
	if (count($loginResponse) == 0) {
		if ($customer_id != '') {
			$tableName1 = $params['customer_id'] . "_user";
			$loginResponse = $table->ReadTable($tableName1, "`username`='$username' AND `password`='$password'", "");
		} 
	}

	if (count($loginResponse) == 0) {
		echo $result_theme->MakeResult(false, "", "Authentication failed.");
	} else {
		$res["user"] = $loginResponse[0];
		$tableName = $customer_id . "_info";
		$factoryInfo = $table->ReadTable($tableName);
		if (isset($factoryInfo) && count($factoryInfo) > 0) {
			$res["factory"] = $factoryInfo[0];
			$tableName = $customer_id . "_machine_group";
			$res["factory"]['factory_id'] = $customer_id;
			$res["factory"]['groupInfo'] = $table->ReadTable($tableName);
			echo $result_theme->MakeResult(true, $res, "Login Success");
		}else{
			echo $result_theme->MakeResult(false, "", "Authentication failed.");
		}
	}
} catch (Exception $err) {
	echo $result_theme->MakeResult(false, "", json_encode($err));
}
