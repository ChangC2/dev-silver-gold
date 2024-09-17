<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$tableName = isset($params['table']) != '' ? $params['table'] : '';
$startDate = isset($params['startDate']) != '' ? $params['startDate'] : '';
$endDate = isset($params['endDate']) != '' ? $params['endDate'] : '';

$table = new TableMgr();
$result_theme = new Result_theme();


if($tableName == '' || $startDate == '' || $endDate==''){
	echo $result_theme->MakeResult(false, "", "no data"); 
}else{
		$startDate = date_create_from_format('m/d/Y', $startDate);		
		$start = $startDate->getTimestamp();
		$endDate = date_create_from_format('m/d/Y', $endDate);
		$end = $endDate->getTimestamp();

		$where = "(`start`>=$start AND `end` <= $end)";
		

		$sql = "SELECT machine_id, status, start, end, color FROM `$tableName` WHERE $where";
				
		try {
			$res = $table->ExecuteQuery_Simple($sql);
			echo $result_theme->MakeResult(true, $res);
		} catch (Exception $err) {
			echo $result_theme->MakeResult(false, "", json_encode($err));
		}

}

