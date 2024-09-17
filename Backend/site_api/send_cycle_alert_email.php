<?php
include_once("./common/headers.php");
include_once("./common/Result.php");
include_once("./common/mailSender.php");

$result_theme = new Result_theme();
$mailSender = new mailSender();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$msg = isset($params['msg']) != '' ? $params['msg'] : 'no data';

date_default_timezone_set('UTC');



contents = String . format("%s' cycle ended at %s.", machineName, cycleStopTime);

$mailSender->SendEmail("cam@slytrackr.com", "Cycle Stop Time", $msg);
echo $result_theme->MakeResult(true, $msg);

