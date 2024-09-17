<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

// Get requested params
$tableName = 'SensorData';
$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$from_date = isset($params['from_date']) != '' ? $params['from_date'] : ''; // will be read as utc timestamp #ex: 1335939007
$to_date = isset($params['to_date']) != '' ? $params['to_date'] : ''; // will be read as utc timestamp #ex: 1335939007

$sql = "SELECT  sensor_id, reading_time, value1, value2 FROM SensorData WHERE `reading_time`>='$from_date' AND `reading_time` <= '$to_date' GROUP BY sensor_id, LEFT(`reading_time`, 13) ORDER BY reading_time";

try {
    $sensors = $table->readDataByQuery($sql);
    $result = [];
    if (count($sensors) > 0){
        $created = "";
        $oldCreated = "";
        $partId = "";
        foreach ($sensors  as $sensor){
            $oneItem = $sensor;
            $created = substr($sensor[1], 0, 13);
            if ($oldCreated == $created) {
                $oneItem[] = $partId;
            }else{
                $sql = "SELECT part_id FROM tanktimes WHERE LEFT(created_at, 13) = '$created'";
                $partIds = $table->readDataByQuery($sql);
                //$oneItem[] = $sql;
                if (count($partIds) > 0) {
                    $partId = $partIds[0][0];
                }
                $oneItem[] = $partId;
                $oldCreated = $created;
            }
            $result[] = $oneItem;
        }
    }
    echo $result_theme->MakeResult(true, $result);
} catch (Exception $err) {
    echo $result_theme->MakeResult(false, "", json_encode($err));
}
