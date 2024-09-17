<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

// Get requested params
$tableName = 'SensorData';
$sensor_id = isset($params['sensor_id']) != '' ? $params['sensor_id'] : '';
$read_date = isset($params['read_date']) != '' ? $params['read_date'] : ''; // will be read as utc timestamp #ex: 1335939007
$from_date = isset($params['from_date']) != '' ? $params['from_date'] : ''; // will be read as utc timestamp #ex: 1335939007
$to_date = isset($params['to_date']) != '' ? $params['to_date'] : ''; // will be read as utc timestamp #ex: 1335939007

$beforeDate = date('Y-m-d', strtotime($read_date . ' - 1 days'));
$afterDate = date('Y-m-d', strtotime($read_date . ' + 1 days'));

if ($from_date == '' || $to_date == '') {
    $from_date = $beforeDate . " 00:00:00";
    $to_date = "$afterDate 23:59:59";
}

$where_txt = "`reading_time`>='$from_date' AND `reading_time`<='$to_date'";
if ($sensor_id != '') {
    $where_txt .= " AND `sensor_id`='$sensor_id'";
}
$where_txt .= " GROUP BY sensor_id, LEFT(`reading_time`, 13) ";
try {
    $res = $table->ReadTable($tableName, $where_txt, ' ');
    echo $result_theme->MakeResult(true, $res);
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
