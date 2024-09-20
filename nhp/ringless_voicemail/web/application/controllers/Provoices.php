<?php

class Provoices extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Voices';

		$this->load->model('model_voices');
		$this->load->model('model_accounts');
		$this->load->model('model_common');
	}

	public function index()
	{
		$token = $this->input->get('token');
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$voices = $this->model_voices->get_pro_voices($user_info["permission"] == 0 ? true : false);
			$users = $this->model_accounts->getUsers();
			if ($user_info) {
				$this->data['permission'] =	$user_info['permission'];
				$this->data['accountname'] = $user_info['accountname'];
				$this->data['token'] =	$token;
				$this->data['voices_data'] = $voices;
				$this->data['users'] = $users;
				$this->data['create'] = 'application/views/provoices/create.php';
				$this->data['play'] = 'application/views/provoices/play.php';
				$this->data['edit'] = 'application/views/provoices/edit.php';
				$this->data['js'] = 'application/views/provoices/script.php';

				$this->render_template('provoices/index', $this->data);
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
			'eden_id' => $this->input->post('eden_id'),
			'created_at' => $this->input->post('created_at'),
			'user_id' => $user_id,
			'permission' => $permission,
			'user_name' => $user_name,
			'active' => 1
		);

		$result = $this->model_voices->create($data);
		if ($result == false) {
			$res = array(
				"status" => false,
				"msg" => "Error occurred!"
			);
		} else {
			$res = array(
				"status" => true,
				"id" => $result,
			);
		}
		echo json_encode($res);
	}

	public function update()
	{
		$id = $_POST['id'];
		$voiceName = $this->input->post('name');
		$voiceId = $this->input->post('voice_id');
		$edenId = $this->input->post('eden_id');
		$data = array(
			'name' => $voiceName,
			'voice_id' => $voiceId,
			'eden_id' => $edenId,
		);

		$update = $this->model_voices->update($id, $data);

		if ($update == true) {
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
