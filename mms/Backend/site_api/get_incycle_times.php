<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$customer_id = isset($params['customer_id']) != '' ? $params['customer_id'] : '';
$machine_ids = isset($params['machine_ids']) != '' ? $params['machine_ids'] : [];
$start_times = isset($params['start_times']) != '' ? $params['start_times'] : [];


if ($customer_id != '') {
    $tableName = $customer_id . "_ganttdata";
    $res = [];
    for ($i = 0; $i < count($machine_ids); $i++) {
        $machine_id = $machine_ids[$i];
        $start_time = $start_times[$i];

        $data = $table->ReadTable($tableName, "`machine_id` = '$machine_id' AND `start` >= $start_time AND `status`= 'In Cycle'", "");

        $incycle_time = 0;
        for ($j = 0; $j < count($data); $j++) {
            $incycle_time += $data[$j]['end'] - $data[$j]['start'];
        }
        $item['machine_id'] = $machine_id;
        $item['incycle_time'] = $incycle_time;
        $res[] = $item;
    }
    echo $result_theme->MakeResult(true, $res);
} else {
    echo $result_theme->MakeResult(false, "");
}
