<?php

namespace App\Controllers\api;

use App\Controllers\BaseController;
use App\Models\PropertyModel;
use App\Models\UserModel;
use App\Models\CommentModel;
use App\Models\ViewHistoryModel;



class Property extends BaseController {

    private UserModel $userModel;
    private PropertyModel $propertyModel;
    private CommentModel $commentModel;
    private ViewHistoryModel $viewHistoryModel;

    public function __construct()
    {
        $this->userModel = new UserModel();
        $this->propertyModel = new PropertyModel();
        $this->commentModel = new CommentModel();
        $this->viewHistoryModel = new ViewHistoryModel();
    }


    public function add(){
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();
        
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $data = array(
            "name" => $params['name'],
            "description" => $params['description'],
            "address" => $params['address'],
            "commission_type" => $params['commission_type'],
            "commission_value" => $params['commission_value'],
            "image" => $params['image'],
            "user" => $_SESSION['user_id']
        );
        

        $id = $this->propertyModel->insert($data);
        $res = array(
            "res" => "success",
            "id" => $id
        );
        echo json_encode($res);
    }

    public function update(){
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);

        $id = $params['id'];
        $data = array(
            "name" => $params['name'],
            "description" => $params['description'],
            "address" => $params['address'],
            "commission_type" => $params['commission_type'],
            "commission_value" => $params['commission_value'],
            "image" => $params['image'],
        );

        if($this->propertyModel->update($id, $data)){
            $res = array(
                "res" => "success",
            );
        } else {
            $res = array(
                "res" => "fail",
            );
        }
        echo json_encode($res);
    }

    public function delete(){
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);

        $id = $params['id'];
        if($this->propertyModel->where(array("id" => $id))->delete()) {
            $res = array(
                "res" => "success"
            );
        } else {
            $res = array(
                "res" => "fail"
            );
        }
        echo json_encode($res);
    }

    public function get_qrcode()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $encrptId = $this->encryptString($params['id']);
        $res = array(
            "res" => "success",
            "url" => base_url('page/property?id=') . $encrptId,
            "qr_code" => (new \chillerlan\QRCode\QRCode)->render(base_url('page/property?id=') . $encrptId)
        );
        echo json_encode($res);
    }

    public function get_html()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $encrptId = $this->encryptString($params['id']);
        $property_link = base_url('page/property?id=') . $encrptId;
        $property = $this->propertyModel->find($params['id']);
        $property["created_at"] = date("F d, y", strtotime($property["created_at"]));
        $property["comments"] = $this->commentModel->where(array('target' => $property["id"], "type" => 0))->countAllResults();
        $property["views"] = $this->viewHistoryModel->where(array('property' => $property["id"]))->countAllResults();

        $html = file_get_contents('assets/html/property.html');
        $html = str_replace("property_name", $property["name"], $html);
        $html = str_replace("property_address", $property["address"], $html);
        $html = str_replace("property_image", $property["image"], $html);
        $html = str_replace("property_comments", $property["comments"], $html);
        $html = str_replace("property_views", $property["views"], $html);
        $html = str_replace("property_created_at", $property["created_at"], $html);
        $html = str_replace("property_description", $property['description'], $html);
        $html = str_replace("property_link", $property_link, $html);
        $res = array(
            "res" => "success",
            "html" => $html
        );
        echo json_encode($res);
    }


    public function upload()
    {
        $photo_name = '';
        $photo_path = 'images/property/';

        if (!file_exists($photo_path)) {
            mkdir($photo_path, 0777, true);
        }
        $name = "";
        $tmp_name = "";
        if (isset($_FILES["file"]["name"])) {
            $name =  $_FILES["file"]["name"];
            $tmp_name = $_FILES['file']['tmp_name'];
        } else {
            $data = array(
                "res" => "fail"
            );
            die(json_encode($data));
        }
        if (!empty($name)) {
            $photo_name = date("YmdHsi") . "_" . $name;
            if (move_uploaded_file($tmp_name, $photo_path . $photo_name)) {
                $data = array(
                    "res" => "success",
                    "name" => $photo_name
                );
                die(json_encode($data));
            }
        } else {
            $data = array(
                "res" => "fail"
            );
            die(json_encode($data));
        }
    }
}