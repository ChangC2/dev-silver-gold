<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$res = array();
if ($customer_id == '') {
    echo $result_theme->MakeResult(false, "", "no data");
} else {
    try {
        $tableName = $customer_id . "_user";
        if ($customer_id == "ics" || $customer_id == "action" || $customer_id == "royb1" || $customer_id == "visser") {
            $sql = "SELECT DISTINCT username_full, user_picture FROM $tableName WHERE `customer_id` LIKE '%$customer_id%'";
        } else {
            $sql = "SELECT DISTINCT username_full, user_picture FROM $tableName WHERE `customer_id` = '$customer_id'";
        }
        $operatorList = $table->ExecuteQuery_Simple($sql);
        $operators = array();
        for ($i = 0; $i < count($operatorList); $i++) {
            $one_record['name'] =  $operatorList[$i][0];
            $one_record['image'] =  $operatorList[$i][1];
            $res[] = $one_record;
        }
        echo $result_theme->MakeResult(true, $res);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
