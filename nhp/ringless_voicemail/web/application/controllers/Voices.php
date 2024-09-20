<?php

class Voices extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Voices';

		$this->load->model('model_voices');
		$this->load->model('model_accounts');
	}

	public function index()
	{
		$token = $this->input->get('token');
		$selected_status = $this->input->get('selected_status');
		$selected_user = $this->input->get('selected_user');
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$voices = $this->model_voices->get(null, $user_info["permission"] != 0 ? $user_id : $selected_user, $selected_status);
			$users = $this->model_accounts->getUsers();
			if ($user_info) {
				$this->data['permission'] =	$user_info['permission'];
				$this->data['accountname'] = $user_info['accountname'];
				$this->data['token'] =	$token;
				$this->data['voices_data'] = $voices;
				$this->data['users'] = $users;
				$this->data['selected_user'] = $selected_user;
				$this->data['selected_status'] = $selected_status;
				$this->data['create'] = 'application/views/voices/create.php';
				$this->data['play'] = 'application/views/voices/play.php';
				$this->data['edit'] = 'application/views/voices/edit.php';
				$this->data['js'] = 'application/views/voices/script.php';

				$this->render_template('voices/index', $this->data);
			} else {
				redirect('auth/login', 'refresh');
			}
		} else {
			redirect('auth/login', 'refresh');
		}
	}
	

	function add()
	{
		$token = $this->input->post('token');
		$user_id = $this->decrypt($token);
		$user_info = $this->model_accounts->get_user_info(null, $user_id);
		$user_name = $user_info['accountname'];
		$permission = $user_info["permission"];
		$voiceName = $this->input->post('name');
		$audioUrl = $this->input->post('url');
		$data = array(
			'name' => $voiceName,
			'url' => $audioUrl,
			'voice_id' => $this->input->post('voice_id'),
			'eden_id' => "en-US-EricNeural",
			'created_at' => $this->input->post('created_at'),
			'user_id' => $user_id,
			'permission' => $permission,
			'user_name' => $user_name,
		);

		$insertId = $this->model_voices->create($data);
		if ($insertId == false) {
			$res = array(
				"status" => false,
				"msg" => "Error occurred!"
			);
		} else {
			$res = array(
				"status" => true,
				"id" => $insertId,
			);
			$this->send_smtp(null, "Farming - New User Cloned Voice", $audioUrl);
		}
		echo json_encode($res);
	}

	public function verify()
	{
		$encyptedId = $this->input->get('id');
		$id = $this->decryptString($encyptedId);
		$voice = $this->model_voices->get($id);
		if (!isset($voice)){
			echo "Invalid Voice";
			die;
		}
		$user_info = $this->model_accounts->get_user_info(null, $voice["user_id"]);
		
		$adminName = $this->input->get('admin_name');
		$voiceId = $this->input->get('voice_id');

		if ($voiceId != ""  && $voice['active'] == 0 && $adminName != null) {
			$data = array(
				'admin_name' => $adminName,
				'voice_id' => $voiceId,
				"active" => 1,
			);
			$this->model_voices->update($id, $data);

			$voice = $this->model_voices->get($id);
			$user_info = $this->model_accounts->get_user_info(null, $voice["user_id"]);
			$voiceName = $voice['name'];
			$mailBody = file_get_contents('assets/html/voice_mail.html');

			$mailBody = str_replace("user_name", $user_info["accountname"], $mailBody);
			$mailBody = str_replace("voice_name", $voiceName, $mailBody);
			$mailBody = str_replace("user_id", $user_info["request_id"], $mailBody);
			$mailBody = str_replace("user_email", $user_info["email"], $mailBody);
			$mailBody = str_replace("user_phone", $user_info["mobile_number"], $mailBody);
			$this->send_smtp($user_info["email"], "Farming - Your Cloned Voice", $mailBody);
		}

		$this->data['voice'] = $voice;
		$this->data['encypted_id'] = $encyptedId;
		$this->data['user_info'] = $user_info;
		$this->load->view('templates/header', $this->data);
		$this->load->view('voices/verify', $this->data);
	}

	public function update()
	{
		$id = $_POST['id'];
		$voiceName = $this->input->post('name');
		$voiceId = $this->input->post('voice_id');
		$data = array(
			'name' => $voiceName,
			'voice_id' => $voiceId,
		);

		$update = $this->model_voices->update($id, $data);

		if ($update == true) {
			$res = array(
				"status" => true
			);
			if ($voiceId != ""){
				$voice = $this->model_voices->get($id);
				$user_id = $voice["user_id"];
				$user_info = $this->model_accounts->get_user_info(null, $user_id);
				$audioUrl = $voice['url'];

				$mailBody = file_get_contents('assets/html/voice_mail_verify.html');
				$mailBody = str_replace("user_name", $user_info["accountname"], $mailBody);
				$mailBody = str_replace("voice_name", $voiceName, $mailBody);
				$mailBody = str_replace("verify_url", base_url() . "voices/verify?id=" . $this->encryptString($id), $mailBody);
				$mailBody = str_replace("voice_url", $audioUrl, $mailBody);
				$mailBody = str_replace("user_id", $user_info["request_id"], $mailBody);
				$mailBody = str_replace("user_email", $user_info["email"], $mailBody);
				$mailBody = str_replace("user_phone", $user_info["mobile_number"], $mailBody);
				$this->send_smtp(null, "Farming - New User Cloned Voice", $mailBody);
			}
		} else {
			$res = array(
				"status" => false,
				"msg" => "Error occurred!"
			);
		}
		echo json_encode($res);
	}

	public function delete()
	{
		$id = $_POST['id'];
		$delete = $this->model_voices->delete($id);
		if ($delete == true) {
			$res = array(
				"status" => true
			);
		} else {
			$res = array(
				"status" => false,
				"msg" => "Error occurred!"
			);
		}
		echo json_encode($res);
	}
}
