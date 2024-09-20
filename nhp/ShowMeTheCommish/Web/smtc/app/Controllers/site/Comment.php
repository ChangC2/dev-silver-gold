<?php

namespace App\Controllers\site;

use App\Controllers\BaseController;
use App\Models\CommentModel;
use App\Models\UserModel;
use App\Models\PropertyModel;

class Comment extends BaseController
{

    private UserModel $userModel;
    private PropertyModel $propertyModel;
    private CommentModel $commentModel;

    public function __construct()
    {
        $this->userModel = new UserModel();
        $this->propertyModel = new PropertyModel();
        $this->commentModel = new CommentModel();
    }

    public function index()
    {
    }

    public function comments(): string
    {
        if (!$this->check_session()) {
            return $this->go_login();
        }
        $comment_id = isset($_GET['id']) ? $this->decryptString($_GET['id']) : "";

        $property = $_GET['property'] ?? "";

        $idArray  = $this-> propertyModel->select('id')->where(array('user' => $_SESSION["user_id"]))->findAll();
        $propertyIds = isset($_GET['property']) ? array($_GET['property']) : array_column($idArray, 'id');
        if (count($propertyIds) > 0) {
            $conditions = [
                'type' => 0
            ];
            $this->commentModel->where($conditions);
            $this->commentModel->whereIn('target', $propertyIds);
            $comments = $this->commentModel->findAll();

            for ($i = 0; $i < count($comments); $i++) {
                $comments[$i]["created_at"] = date("F d, y h:i", strtotime($comments[$i]["created_at"]));
                $property = $this->propertyModel->where(array("id" => $comments[$i]["target"]))->first();
                $encrptId = $this->encryptString($property['id']);
                $name = $property["name"];
                $url = base_url('page/property?id=') . $encrptId;
                $propertyHtml = "<a href='$url' target='_blank'>$name</a>";
                $comments[$i]["property"] = $propertyHtml;
            }
        } else {
            $comments = array();
        }
        $data = array(
            'comments' => $comments,
            'comment_id' => $comment_id,
            'menu' => 'topnav-comment',
            'modal' => 'pages/comments/modal.php',
            'js' => 'pages/comments/script.php',
        );
        return view("template/header")
            . view("pages/comments/index", $data) . view("template/footer");
    }
}
