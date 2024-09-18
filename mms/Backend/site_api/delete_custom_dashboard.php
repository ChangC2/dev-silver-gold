<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "custom_dashboards";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$id = isset($params['id']) != '' ? $params['id'] : '';

if ($id != '') {
    $query = "DELETE FROM $tableName WHERE `id` = '$id'";
    $table->ExecuteQuery_Simple_NoResult($query);

    $sql = "SELECT DISTINCT customer_id FROM $tableName";
    $customerIds = $table->ExecuteQuery_With_Column(["customer_id"], $sql);
    $res = array();
    foreach ($customerIds as $cID) {
        $where = "`customer_id` = '" . $cID["customer_id"] . "'";
        $custom_dahsboards = $table->ReadTable($tableName, $where, " ORDER BY `order` , id");
        $res[$cID["customer_id"]] = $custom_dahsboards;
    }
    $data['custom_dashboards'] = $res;
    echo $result_theme->MakeResult(true, $data);
} else {
    echo $result_theme->MakeResult(false, "");
}
