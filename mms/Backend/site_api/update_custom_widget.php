<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "custom_widget";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$id = isset($params['id']) != '' ? $params['id'] : '';
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$dashboard_id = isset($params['dashboard_id']) != '' ? $params['dashboard_id'] : '';
$widget_type = isset($params['widget_type']) != '' ? $params['widget_type'] : '0';
$widget_size = isset($params['widget_size']) != '' ? $params['widget_size'] : '0';
$data_points = isset($params['data_points']) != '' ? $params['data_points'] : '';
$machine = isset($params['machine']) != '' ? $params['machine'] : '';
$operator = isset($params['operator']) != '' ? $params['operator'] : '';
$jobID = isset($params['jobID']) != '' ? $params['jobID'] : '';
$days = isset($params['days']) != '' ? $params['days'] : '30';

if ($customer_id != '' && $machine != '' && $data_points != '') {
    $query = "UPDATE `$tableName` SET `customer_id` = '$customer_id', 
                                        `dashboard_id` = '$dashboard_id',  
                                        `widget_type` = '$widget_type', 
                                        `widget_size` = '$widget_size', 
                                        `data_points` = '$data_points', 
                                        `machine` = '$machine',
                                        `operator` = '$operator',
                                        `jobID` = '$jobID',
                                        `days` = '$days' WHERE `id` = $id";

    $table->ExecuteQuery_Simple_NoResult($query);

    $where = "`customer_id` = '$customer_id' AND `dashboard_id` = '$dashboard_id'";
    $custom_widgets = $table->ReadTable($tableName, $where, " ORDER BY id ASC");
    
    if (count($custom_widgets) > 0) {
        $results = [];
        foreach ($custom_widgets as $cw) {
            $newCW = $cw;
            $where = "`machine_id` = '" . $cw["machine"] . "'";
            $machineInfo = $table->ReadTable($customer_id . "_status", $where, " ORDER BY id ASC");
            if (count($machineInfo) > 0) {
                $newCW["machineImage"] = $machineInfo[0]["machine_picture_url"];
            }
            $results[] = $newCW;
        }
        $data['custom_widgets'] = $results;
        echo $result_theme->MakeResult(true, $data);
    } else {
        echo $result_theme->MakeResult(false, "");
    }
} else {
    echo $result_theme->MakeResult(false, "");
}
