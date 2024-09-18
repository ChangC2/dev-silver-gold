<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$detail = isset($params['detail']) != '' ? $params['detail'] : '';

$factory_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();

$tableName = "tbl_app_setting";

$res = "";

if ($factory_id == '' || $machine_id == '') {
    echo $result_theme->MakeResult(false, "", "no param");
} else {
    try {
        $keyList  = array_keys($detail);

        $where = "`machine_id`='" . $machine_id . "' AND `factory_id`='$factory_id'";
        $appSetting = $table->ReadTable($tableName, $where, "");
        if (count($appSetting) == 0) {
            $addQuery = "INSERT INTO `$tableName` (";
            for ($i = 0; $i < count($keyList); $i++) {
                $addQuery .= "`$keyList[$i]`,";
            }
            $addQuery = rtrim($addQuery, ",");
            $addQuery .= ") VALUES (";
            for ($i = 0; $i < count($keyList); $i++) {
                $addQuery .= "'" . $detail[$keyList[$i]] . "',";
            }
            $addQuery = rtrim($addQuery, ",");
            $addQuery .= ")";
            $table->ExecuteQuery_Simple_NoResult($addQuery);
        } else {
            $updateQuery = "UPDATE $tableName SET ";
            for ($i = 0; $i < count($keyList); $i++) {
                $updateQuery .= "`$keyList[$i]` = " . "'" . $detail[$keyList[$i]] . "',";
            }
            $updateQuery = rtrim($updateQuery, ",");
            $updateQuery .= " WHERE " . $where;
            $table->ExecuteQuery_Simple_NoResult($updateQuery);
        }

        $cslock_cycle = $detail['cslock_cycle'] == null ? 0 : $detail['cslock_cycle'];
        $cslock_reverse = $detail['cslock_reverse'] == null ? 0 : $detail['cslock_reverse'];
        $sql = "SELECT * FROM `buffer_ganttdata` where `customer_id`='$factory_id' AND machine_id == '$machine_id'";
        $buffer_ganttdata = $table->ExecuteQuery_Simple($sql);
        if (count($buffer_ganttdata) > 0) {
            $tableName = "buffer_ganttdata";
            $query = "UPDATE $tableName SET `cslock_cycle` = '$cslock_cycle', `cslock_reverse` = '$cslock_reverse' where `customer_id`='$factory_id' AND machine_id == '$machine_id'";
            $table->ExecuteQuery_Simple_NoResult($query);
        }
        
        echo $result_theme->MakeResult(true);
    } catch (Exception $err) {
        echo $result_theme->MakeResult(false, "", json_encode($err));
    }
}
