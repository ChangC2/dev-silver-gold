<?php

class Scripts extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Scripts';

		$this->load->model('model_scripts');
		$this->load->model('model_accounts');
	}

	public function index()
	{
		$token = $this->input->get('token');
		$selected_user = $this->input->get('selected_user');
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$scripts = $this->model_scripts->get(null, $user_info["permission"] != 0 ? $user_id : $selected_user, null);
			$users = $this->model_accounts->getUsers();
			if ($user_info) {
				$this->data['permission'] =	$user_info['permission'];
				$this->data['accountname'] =	$user_info['accountname'];
				$this->data['token'] =	$token;
				$this->data['scripts_data'] = $scripts;
				$this->data['users'] = $users;
				$this->data['selected_user'] = $selected_user;
				$this->data['create'] = 'application/views/scripts/create.php';
				$this->data['contacts_modal'] = 'application/views/scripts/contacts.php';
				$this->data['edit'] = 'application/views/scripts/edit.php';
				$this->data['js'] = 'application/views/scripts/script.php';

				$this->render_template('scripts/index', $this->data);
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
			'salutation' => $this->input->post('salutation'),
			'content' => $this->input->post('content'),
			'created_at' => $this->input->post('created_at'),
			'user_id' => $user_id,
			'user_name' => $user_name,
		);

		$result = $this->model_scripts->create($data);
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
			'salutation' => $this->input->post('salutation'),
			'content' => $this->input->post('content'),
		);

		$update = $this->model_scripts->update($id, $data);

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
		$delete = $this->model_scripts->delete($id);
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
