<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();
$tableName = "custom_widget";

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$dashboard_id = isset($params['dashboard_id']) != '' ? $params['dashboard_id'] : '';

if ($customer_id != '' && $dashboard_id != '') {
    $where = "`customer_id` = '$customer_id' AND `dashboard_id` = '$dashboard_id'";
    $custom_widgets = $table->ReadTable($tableName, $where, " ORDER BY id ASC");
    
    if (count($custom_widgets) > 0) {
        $results = [];
        foreach ($custom_widgets as $cw){
            $newCW = $cw;
            $where = "`machine_id` = '" . $cw["machine"]. "'";
            $machineInfo = $table->ReadTable($customer_id. "_status", $where, " ORDER BY id ASC");
            if (count($machineInfo) > 0){
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
