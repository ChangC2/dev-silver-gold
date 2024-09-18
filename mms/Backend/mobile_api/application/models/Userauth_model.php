<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Userauth_model extends CI_Model{
    public function __construct(){
        
    }        

    const app_id = "nrbgoloxzamv";
    const app_key = "4adee18e-3f01-4ad0-a408-06c958375c38";
    const init_url = "https://api.cloudboost.io/data/nrbgoloxzamv/";
    const init_url_file = "https://api.cloudboost.io/file/nrbgoloxzamv/_File/find";
    public function curl($url, $param)
    {
        $ch=curl_init();
        curl_setopt($ch, CURLOPT_URL, $url); 
        //curl_setopt($ch, CURLOPT_POSTFIELDS, '{"key":"4adee18e-3f01-4ad0-a408-06c958375c38","sort":[],"select":[],"query":{"$includeList":[],"$include":[]},"skip":0}');//$data_string);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $param);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);       
        curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
        curl_setopt($ch, CURLOPT_HTTPHEADER,array('Content-Type:application/json','Accept:application/json'));
        
        $result = curl_exec($ch);

        // if (strpos($url, '_File') !== false) {
            
        //     print_r($result. "   url:=".$url."   param:=".$param);
        // }


        if(curl_errno($ch))
             echo 'Curl error: '.curl_error($ch);
        
        curl_close($ch);
        return json_decode($result);
    }

    public function login($data){
        //https://api.cloudboost.io
        
        $url = self::init_url.'logins/find';
        $param = array();
        $query = array();
        $query['user_name'] = $data['username'];
        $query['password'] = $data['password'];

        $skip = 0;
        $param['key'] = self::app_key;
        $param['query'] = $query;
        $data_string = json_encode($param);

        $result = $this->curl($url, $data_string);
        //$result['info'] = $this->getInfo($resul['login']->customer_id);
        return $result;
    }

    public function getInfo($prefix)
    {
        $url = self::init_url.$prefix.'_info/find';
        $param['key'] = self::app_key;
        $data_string = json_encode($param);

        $result = $this->curl($url, $data_string);
        return $result;
    }
    public function getFileData($url){
        return file_get_contents($url);
    }

    public function getMachineDetail($prefix, $machine_id){
        $url = self::init_url.$prefix.'_status/find';
        $param['key'] = self::app_key;
        $param['query'] = array();
        $param['query']['_id'] = $machine_id;

        $data_string = json_encode($param);

        $result = $this->curl($url, $data_string);
        return $result;
    }

    public function getStatus($prefix)
    {
        // Read Status
        $url = self::init_url.$prefix.'_status/find';
        $param['key'] = self::app_key;

        $data_string = json_encode($param);

        $result = $this->curl($url, $data_string);

        // Read File
        $url = self::init_url.'_File/find';

        $param['key'] = self::app_key;
        $param['limit'] = 1;
        $param['skip'] = 0;
        $param['query'] = array();

        for($i = 0; $i < count($result); $i++){
            $gantt_data = isset($result[$i]->gantt_data)?$result[$i]->gantt_data:"";
            if($gantt_data != ""){
                
                // Get File URL
                $_id = $gantt_data->_id;
                $param['query']['_id'] = $_id;
                $data_string = json_encode($param);
                $files = $this->curl($url, $data_string);
                
                // Get File Content
                if(count($files) > 0){
                    $file = $files[0];
                    $file_url = isset($file->url)?$file->url:"";
                    // print_r($file_url);
                    if($file_url != ""){
                        $fileContent = self::getFileData($file_url);
                        // print_r($fileContent);
                        $result[$i]->gantt_data = $fileContent;
                    }
                }
            }else{
                
        
            }
        }

        return $result;
    }
    public function getFile($prefix){
        $url = self::init_url_file;
        $param['key'] = self::app_key;
        $param['limit'] = 1;
        $param['skip'] = 0;
        $param['sdk'] = "java";
        $param['query'] = array();
        $param['_type'] = "file";

        $status = self::getStatus($prefix);
        $result = array();
        for($i = 0; $i < count($status); $i++){
            $gantt_data = isset($status[$i]->gantt_data)?$status[$i]->gantt_data:"";
            if($gantt_data != ""){
                $_id = $gantt_data->_id;
                $param['query']['_id'] = $_id;
                $data_string = json_encode($param);
                $result[] = $this->curl($url, $data_string);
            }else{
                $resul[] = "";
            }
        }
        print_r($result);
        return $result;
    }
}