<?php
class oneMachine
{
    function readData($prefix, $machine_id, $date, $timezone)
    {
        include_once("tableReader.php");
        $reader = new tableReader();

        $status = $reader->readTable($prefix . "_status", "where `machine_id`='" . $machine_id . "'");
        $hst = $reader->readTable($prefix . "_hst", "where `machine_id`='" . $machine_id . "' and `date`='" . $date . "'");
        $timezone = $timezone * -1;

        date_default_timezone_set('UTC');
        $from = new DateTime($date);


        $from = $from->modify($timezone . ' hour');

        $from = $from->getTimestamp();

        $to = new DateTime($date);
        $to = $to->modify('+1 day');
        $to = $to->modify($timezone . ' hour');
        $to = $to->getTimestamp();

        $dateForHst = new DateTime($date);
        $dateForHst = $dateForHst->modify('-15 day');
        $dateForHst = $dateForHst->format('m/d/Y');
        $hstList = $reader->readTable($prefix . "_hst", "where `machine_id`='" . $machine_id . "' and `date`>='" . $dateForHst . "' and `date` <='" . $date . "' order by `date`");


        $gantt = $reader->readTable($prefix . "_ganttdata", "where `machine_id`='" . $machine_id . "' and `end`>" . $from . " and `start`<" . $to);
        for($i = 0; $i < count($gantt); $i++){
            if($gantt[$i]['start'] < $from){
                $gantt[$i]['start'] = $from;
            }
            if($gantt[$i]['end'] > $to){
                $gantt[$i]['end'] = $to;
            }
        }
        $info = array();
        $info['status'] = $status;
        $info['hst'] = $hst;
        $info['hstList'] = $hstList;
        $info['gantt'] = $gantt;
        return $info;
    }
};
