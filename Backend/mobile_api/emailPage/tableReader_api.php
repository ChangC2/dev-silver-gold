<?php

class tableReader
{
    function CallAPI($method, $url, $data = false)
    {
        $curl = curl_init();

        switch ($method){
           case "POST":
              curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
              curl_setopt($curl, CURLOPT_POST, 1);
              if ($data)
                curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
              break;
           case "PUT":
              curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "PUT");
              if ($data)
                curl_setopt($curl, CURLOPT_POSTFIELDS, $data);			 					
              break;
           default:
              if ($data)
                 $url = sprintf("%s?%s", $url, http_build_query($data));
        }
        
        // OPTIONS:
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_HTTPHEADER, array(
           'Content-Type: application/json',
           'Content-Length: '.strlen($data)
        ));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        //curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
     
        // EXECUTE:
        $result = curl_exec($curl);
        if(!$result){die("Connection Failure");}
        curl_close($curl);
        return $result;
    }

    public function readTable($table_name, $whereString)
    {
        
        $data = array();
        $data['table_name'] = $table_name;
        $data['whereString'] = $whereString;
        $resData = $this->CallAPI('POST', 'https://slymms.com/emailPage/tableReader.php', json_encode($data));
        //$resData = $this->CallAPI('POST', 'http://192.168.1.58/mms_email/tableReader_backend.php', json_encode($data));
        return json_decode($resData);
    }
}

/*
$reader = new tableReader();
$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$table_name = isset($params['table_name']) != '' ? $params['table_name'] : '';
$whereString = isset($params['whereString']) != '' ? $params['whereString'] : '';

echo json_encode($reader->readTable($table_name, $whereString));
*/


// $test = new tableReader();
// echo json_encode($test->readTable('tbl_logins',  ''));
