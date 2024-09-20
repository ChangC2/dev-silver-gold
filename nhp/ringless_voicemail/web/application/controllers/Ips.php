<?php

class Ips extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		//$this->not_logged_in();

		$this->data['page_title'] = 'Ips';

		$this->load->model('model_ips');
		$this->load->model('model_recordings');
		$this->load->model('model_contacts');
		$this->load->model('model_accounts');
	}

	public function index()
	{
		if ($this->input->get('token') != null) {
			$token = $this->input->get('token');
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$this->data['permission'] =	$user_info['permission'];
			$this->data['accountname'] =	$user_info['accountname'];
			$this->data['token'] =	$token;
		} else {
			redirect('auth/login', 'refresh');
		}

		$ips = $this->model_ips->get(null);

		$this->data['ips_data'] = $ips;
		$this->data['create'] = 'application/views/ips/create.php';
		$this->data['edit'] = 'application/views/ips/edit.php';
		$this->data['js'] = 'application/views/ips/script.php';
		$this->render_template('ips/index', $this->data);
	}

	function add()
	{
		$data = array(
			'ip_address' => $this->input->post('ip_address'),
		);
		$result = $this->model_ips->create($data);
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
		$delete = $this->model_ips->delete($id);
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
			'ip_address' => $this->input->post('ip_address'),
		);

		$update = $this->model_ips->update($id, $data);
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
