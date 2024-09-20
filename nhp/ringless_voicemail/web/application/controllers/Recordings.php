<?php

class Recordings extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Recordings';

		$this->load->model('model_recordings');
		$this->load->model('model_accounts');
	}

	public function index()
	{
		$token = $this->input->get('token');
		$selected_user = $this->input->get('selected_user');
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$recordings = $this->model_recordings->get(null, $user_info["permission"] != 0 ? $user_id : $selected_user, null);
			$users = $this->model_accounts->getUsers();
			if ($user_info) {
				$this->data['permission'] =	$user_info['permission'];
				$this->data['accountname'] =	$user_info['accountname'];
				$this->data['token'] =	$token;
				$this->data['recordings_data'] = $recordings;
				$this->data['users'] = $users;
				$this->data['selected_user'] = $selected_user;
				$this->data['create'] = 'application/views/recordings/create.php';
				$this->data['edit'] = 'application/views/recordings/edit.php';
				$this->data['play'] = 'application/views/recordings/play.php';
				$this->data['js'] = 'application/views/recordings/script.php';
				$this->render_template('recordings/index', $this->data);
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

		$data = array(
			'name' => $this->input->post('name'),
			'url' => $this->input->post('url'),
			'created_at' => $this->input->post('created_at'),
			'user_id' => $user_id,
			'user_name' => $user_name,
		);

		$result = $this->model_recordings->create($data);
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

		$data = array(
			'name' => $this->input->post('name'),
			'url' => $this->input->post('url'),
		);

		$update = $this->model_recordings->update($id, $data);

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
		$delete = $this->model_recordings->delete($id);
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
