<?php

namespace App\Controllers\api;
use App\Controllers\BaseController;
use App\Models\CommentModel;
use App\Models\PropertyModel;
use App\Models\UserModel;

class Comment extends BaseController {

    private PropertyModel $propertyModel;
    private CommentModel $commentModel;
    private UserModel $userModel;

    public function __construct()
    {
        $this->propertyModel = new PropertyModel();
        $this->commentModel = new CommentModel();
        $this->userModel = new UserModel();
    }

    public function get()
    {
        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $conditions = [
            'target' => $params["id"],
            'type' => $params["type"]
        ];
        $comments = $this->commentModel->where($conditions)->orderBy('id', 'desc')->findAll();
        $res = array(
            "res" => "success",
            "comments" => $comments
        );
        echo json_encode($res);
    }

    public function get_count()
    {
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $idArray  = $this->propertyModel->select('id')->where(array('user' => $_SESSION["user_id"]))->findAll();
        $propertyIds = array_column($idArray, 'id');
        if (count($propertyIds) > 0){
            $conditions = [
                'type' => 0
            ];
            $this->commentModel->where($conditions);
            $this->commentModel->whereIn('target', $propertyIds);
            $count = $this->commentModel->countAllResults();
            $res = array(
                "res" => "success",
                "count" => $count
            );
        }else{
            $res = array(
                "res" => "success",
                "count" => 0
            );
        }
        echo json_encode($res);
    }

    public function add(){
        ini_set('session.gc_maxlifetime', 3600 * 24 * 365);
        session_set_cookie_params(3600 * 24 * 365);
        session_start();

        $request_json = file_get_contents('php://input');
        $params = json_decode($request_json, true);
        $user = $this->userModel->find($_SESSION['user_id']);
        date_default_timezone_set('UTC');
        $data = array(
            "content" => $params['content'],
            "target" => $params['id'],
            "type" => $params['type'],
            "user" => $user["id"],
            "user_name" => $user["name"],
            "created_at" => date('Y-m-d H:i:s')
        );
        $id = $this->commentModel->insert($data);
        $data["id"] = $id;

        if ($params['type'] == 0){
            $property = $this->propertyModel->find($params['id']);
            $owner = $this->userModel->find($property["user"]);
            if (count($user) > 0) {
                $mailBody = file_get_contents('assets/html/index.html');
                $mailBody = str_replace("property_name", $property["name"], $mailBody);
                $mailBody = str_replace("buyer_name", $user["first_name"] . " " . $user["last_name"], $mailBody);
                $mailBody = str_replace("date_of_submission", date('Y-m-d'), $mailBody);
                $mailBody = str_replace("time_of_submission", date('H:i:s'), $mailBody);
                $mailBody = str_replace("comment_content", $params['content'], $mailBody);
                $encrptId = $this->encryptString($id);
                $reply_link = base_url('page/comments?id=') . $encrptId;
                $mailBody = str_replace("reply_link", $reply_link, $mailBody);
                $this->send_smtp(trim($owner['email']), "New Comment", $mailBody);
            }
        }

        $res = array(
            "res" => "success",
            "comment" => $data
        );
        echo json_encode($res);
    }
}