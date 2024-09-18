<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

date_default_timezone_set('UTC');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
// DeLog($req);
$tableName = "time_savings_faxon";

$table = new TableMgr();
$result_theme = new Result_theme();

$sql = "SELECT * FROM `$tableName`";
$timeSavings = $table->ExecuteQuery_Simple($sql);
$res = array();
foreach ($timeSavings as $timeSaving) {
	$r = $timeSaving;
	$date = date("Y-m-d", strtotime($timeSaving['Date']));
	$time = $r['Time'];
	$tool = $r['Tool'];
	$section = $r['Section'];
	$channel = $r['Channel'];
	$learnedTime = $r['Learned_Monitor_Time'];
	$elapsedTime = $r['Elapsed_Monitor_Time'];
	$timeSavings = $r['Time_SavingsSec'];
	$timeSavingsPercentage = $r['Time_Savings'];
	$created_at = date("Y-m-d H:i:s", strtotime('-3 hours', strtotime($timeSaving['Date'] . $timeSaving['Time'])));
	$query = "INSERT INTO `time_savings` (`customer_id`, `machine_id`, `created_at`, `date`, `time`, `progNum`, `tool`,`section`, `channel`, `learnedTime`, `elapsedTime`, `timeSavings`, `timeSavingsPercentage`)";
	$query .= 					" VALUES ('faxon','Doosan HBM','$created_at','$date','$time','4132','$tool', $section,$channel,$learnedTime,$elapsedTime,'$timeSavings','$timeSavingsPercentage')";
	$table->ExecuteQuery_Simple_NoResult($query);
}
echo $result_theme->MakeResult(true, $res);
