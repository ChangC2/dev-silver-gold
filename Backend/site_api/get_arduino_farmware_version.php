<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$tableMgr = new TableMgr();
$result_theme = new Result_theme();

$customer_id  = $_GET['customer_id'];
$sensor_id  = $_GET['sensor_id'];
// Get requested params
$customer_id = $customer_id != null ? $customer_id : '';
$sensor_id = $sensor_id != null ? $sensor_id  : '';
$ret = array();
$ret["iot_version"] = "1.0";
$ret["mms_version"] = "1.0";
$ret["modbus_ip"] = "192.168.1.20";
$ret["hreg"] = "1,2,3,4";
$ret["timezone"] = "1.0";
try {
    if ($customer_id != '') {
        $tableName = "arduino_firmware";
        // $where = "`customer_id`='$customer_id' AND `sensor_id`='$sensor_id'";
        $firmwares = $tableMgr->ReadTable($tableName, "", "");
        if (count($firmwares) > 0) {
            $ret["iot_version"] = $firmwares[0]["iot_version"];
            $ret["mms_version"] = $firmwares[0]["mms_version"];
        }
        $sql = "SELECT * FROM tbl_hennigs where `sensor_id`='$sensor_id'";
        $sensors = $tableMgr->ExecuteQuery_Simple($sql);
        if (count($sensors) > 0) {
            $ret["modbus_ip"] = $sensors[0]["modbus_ip"];
            $ret["hreg"] = $sensors[0]["modbus_hreg"];
        }
        $tableName = $customer_id . "_info";
        $sql = "SELECT * FROM $tableName";
        $customerInfo = $tableMgr->ExecuteQuery_Simple($sql);
        if (count($customerInfo) > 0) {
            $ret["timezone"] = $customerInfo[0]["timezone"];
        }
    }
} catch (Exception $err) {
}
echo json_encode($ret);
