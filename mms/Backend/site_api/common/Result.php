<?php

class Result_theme
{
    function MakeResult($status, $data = "", $message = "")
    {
        $res = array();
        $res['status'] = $status;
        $res['data'] = $data;
        $res['message'] = $message;

        return json_encode($res);
    }
}
