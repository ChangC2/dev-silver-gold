<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_string = isset($params['customer_string']) != '' ? $params['customer_string'] : '';

if ($customer_string == '') {
    echo $result_theme->MakeResult(false, "", "no param");
    die;
}

$customerList = explode(",", $customer_string);
$resList = [];
foreach ($customerList as $customer_id) {
    $customer_id = trim($customer_id);
    $tableName = $customer_id . "_info";
    $customerInfo = $table->ReadTable($tableName);
    if (isset($customerInfo) && count($customerInfo)>0){
        $res[$customer_id]  = $table->ReadTable($tableName)[0];
        $tableName = $customer_id . "_machine_group";
        $res[$customer_id]['groupInfo'] = $table->ReadTable($tableName);
        $res[$customer_id]['groupTable'] = $tableName;
    }
}
echo $result_theme->MakeResult(true, $res);
