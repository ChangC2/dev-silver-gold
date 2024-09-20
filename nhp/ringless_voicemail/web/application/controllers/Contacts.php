<?php

class Contacts extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		// $this->not_logged_in();

		$this->data['page_title'] = 'Contacts';

		$this->load->model('model_contacts');
		$this->load->model('model_log');
		$this->load->model('model_accounts');
	}

	public function index()
	{
		$user_id = "";
		if ($this->input->get('token') != null) {
			$token = $this->input->get('token');
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$this->data['permission'] =	$user_info['permission'];
			$this->data['accountname'] =	$user_info['accountname'];
			$this->data['token'] =	$token;
			$this->data['user_id'] = $user_id;
			if (!$user_info) {
				redirect('auth/login', 'refresh');
			}
		} else {
			redirect('auth/login', 'refresh');
		}

		$contacts = $this->model_contacts->get(null, $user_id);
		$this->data['contacts_data'] = $contacts;
		$this->data['create'] = 'application/views/contacts/create.php';
		$this->data['edit'] = 'application/views/contacts/edit.php';
		$this->data['js'] = 'application/views/contacts/script.php';
		$this->render_template('contacts/index', $this->data);
	}


	function add()
	{
		$data = array(
			'name' => $this->input->post('name'),
			'phone' => $this->input->post('phone'),
			'created_at' => $this->input->post('created_at'),
			'user' => $this->input->post('user_id')
		);
		$result = $this->model_contacts->create($data);
		if ($result == false) {
			$res = array(
				"status" => false,
				"msg" => "Error occurred!"
			);
		} else {
			$res = array(
				"status" => true,
				"id" => $result
			);
		}
		echo json_encode($res);
	}

	public function delete()
	{
		$id = $_POST['id'];
		$delete = $this->model_contacts->delete($id);
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

	public function update()
	{
		$id = $_POST['id'];

		$data = array(
			'name' => $this->input->post('name'),
			'phone' => $this->input->post('phone'),
		);

		$update = $this->model_contacts->update($id, $data);
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
}
