<?php

class oneUser
{
	function readData($prefix)
	{
		include_once("tableReader.php");
		$reader = new tableReader();

		include_once("oneMachine.php");

		$machines = $reader->readTable($prefix . "_status", "");

		$info = array();
		$info['status'] = array();
		$info['hst'] = array();
		$info['gantt'] = array();
		$info['hstList'] = array();
		$info['info'] = $reader->readTable($prefix . "_info", "")[0];

		// Get current time
		date_default_timezone_set('UTC');
		$date = new DateTime();
		$date = $date->modify($info['info']['timezone'] . ' hour');
		$date = $date->modify('-1 day');
		$info['mytime'] = $date->format('m-d-Y');

		$date = $date->format('m/d/Y');

		//$date = "05/23/2023";

		for ($i = 0; $i < count($machines); $i++) {
			$machine = new oneMachine();
			$res = $machine->readData($prefix, $machines[$i]['machine_id'], $date, $info['info']['timezone']);
			$info['status'][] = $res['status'];
			$info['hst'][] = $res['hst'];
			$info['gantt'][] = $res['gantt'];
			$info['hstList'][] = $res['hstList'];
		}
		return $info;
	}
}
