<?php

class Accounts extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Accounts';

		$this->load->model('model_accounts');
		$this->load->model('model_groups');
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

		$accounts_data = $this->model_accounts->getUsers();
		$result = array();
		foreach ($accounts_data as $row) {
			$v = $row;
			$group = $this->model_accounts->getAccountGroup($v['id']);
			$v["group_name"] = $group['group_name'];
			$v["group_id"] = $group['id'];
			$result[] = $v;
		}
		$this->data['accounts_data'] = $result;
		$this->render_template('accounts/index', $this->data);
	}

	public function create()
	{
		$this->form_validation->set_rules('groups', 'Group', 'required');
		$this->form_validation->set_rules('accountname', 'AccountName', 'trim|required|min_length[3]|max_length[12]|is_unique[accounts.accountname]');
		$this->form_validation->set_rules('email', 'Email', 'trim|required|is_unique[accounts.email]');
		$this->form_validation->set_rules('password', 'Password', 'trim|required|min_length[6]');
		$this->form_validation->set_rules('status', 'Status', 'required');
		$this->form_validation->set_rules('cpassword', 'Confirm password', 'trim|required|matches[password]');

		$token = $this->input->get('token');
		$token = urlencode($token);
		if ($this->form_validation->run() == TRUE) {
			$token = $this->input->post('token');
			$token = urlencode($token);
			// true case
			$password = $this->password_hash($this->input->post('password'));

			$data = array(
				'accountname' => $this->input->post('accountname'),
				'mobile_number' => $this->input->post('mobile_number'),
				'status' => $this->input->post('status'),
				'password' => $password,
				'credits' => $this->input->post('credits'),
				'email' => $this->input->post('email')
			);

			$create = $this->model_accounts->create($data, $this->input->post('groups'));
			if ($create == false) {
				$this->session->set_flashdata('errors', 'Error occurred!!');
				redirect('accounts/create?token=' . $token, 'refresh');
			} else {
				$this->session->set_flashdata('success', 'Successfully created');
				redirect('accounts?token=' . $token, 'refresh');
			}
		} else {
			// false case
			if ($_SERVER['REQUEST_METHOD'] == 'POST') {
				$this->session->set_flashdata('errors', 'Error occurred!!');
			}
			$group_data = $this->model_groups->getGroupData();
			$this->data['group_data'] = $group_data;
			$this->data['permission'] =	$user_info['permission'];
			$this->data['accountname'] =	$user_info['accountname'];
			$this->data['token'] =	$token;
			$this->render_template('accounts/create', $this->data);
		}
	}

	public function password_hash($pass = '')
	{
		if ($pass) {
			$password = password_hash($pass, PASSWORD_DEFAULT);
			return $password;
		}
	}

	public function edit()
	{
		$id = $this->input->get('id');
		$token = $this->input->get('token');
		if ($id) {
			$this->form_validation->set_rules('groups', 'Group', 'required');
			$this->form_validation->set_rules('accountname', 'Account Name', 'trim|required');
			$this->form_validation->set_rules('email', 'Email', 'trim|required');
			$this->form_validation->set_rules('mobile_number', 'mobile_number', 'trim|required');
			$this->form_validation->set_rules('status', 'Status', 'required');

			if ($this->form_validation->run() == TRUE) {
				$token = $this->input->post('token');
				$token = urlencode($token);
				if (empty($this->input->post('password')) && empty($this->input->post('cpassword'))) {
					$data = array(
						'accountname' => $this->input->post('accountname'),
						'mobile_number' => $this->input->post('mobile_number'),
						'status' => $this->input->post('status'),
						'email' => $this->input->post('email'),
						'test_count' => $this->input->post('test_count'),
						'credits' => $this->input->post('credits')
					);

					$update = $this->model_accounts->update($data, $id, $this->input->post('groups'));
					if ($update == true) {
						$this->session->set_flashdata('success', 'Successfully created');
						redirect('accounts?token=' . $token, 'refresh');
					} else {
						$this->session->set_flashdata('errors', 'Error occurred!!');
						redirect("accounts/edit?token=$token&id=$id", 'refresh');
					}
				} else {
					$this->form_validation->set_rules('password', 'Password', 'trim|required|min_length[6]');
					$this->form_validation->set_rules('cpassword', 'Confirm password', 'trim|required|matches[password]');
					$password = $this->password_hash($this->input->post('password'));
					$data = array(
						'accountname' => $this->input->post('accountname'),
						'password' => $password,
						'mobile_number' => $this->input->post('mobile_number'),
						'status' => $this->input->post('status'),
						'email' => $this->input->post('email'),
						'test_count' => $this->input->post('test_count'),
						'credits' => $this->input->post('credits')
					);

					$update = $this->model_accounts->update($data, $id, $this->input->post('groups'));
					if ($update == true) {
						$this->session->set_flashdata('success', 'Successfully updated');
						redirect('accounts?token=' . $token, 'refresh');
					} else {
						$this->session->set_flashdata('errors', 'Error occurred!!');
						redirect("accounts/edit?token=$token&id=$id", 'refresh');
					}
				}
			} else {
				$account_data = $this->model_accounts->getAccountData($id);
				$groups = $this->model_accounts->getAccountGroup($id);

				$this->data['account_data'] = $account_data;
				$this->data['account_group'] = $groups;

				$group_data = $this->model_groups->getGroupData();
				$this->data['group_data'] = $group_data;
				$this->data['permission'] =	$user_info['permission'];
				$this->data['accountname'] =	$user_info['accountname'];
				$this->data['token'] =	$token;
				$this->render_template('accounts/edit', $this->data);
			}
		}
	}

	public function delete()
	{
		$id = $_POST['id'];

		$delete = $this->model_accounts->delete($id);

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


	public function profile()
	{
		$account_id = $this->session->accountdata('id');

		$account_data = $this->model_accounts->getAccountData($account_id);
		$this->data['account_data'] = $account_data;

		$account_group = $this->model_accounts->getAccountGroup($account_id);
		$this->data['account_group'] = $account_group;

		$this->render_template('accounts/profile', $this->data);
	}


	public function setting()
	{
		$id = $this->session->accountdata('id');

		if ($id) {
			$this->form_validation->set_rules('accountname', 'Accountname', 'trim|required|min_length[5]|max_length[12]');
			$this->form_validation->set_rules('email', 'Email', 'trim|required');
			$this->form_validation->set_rules('fname', 'First name', 'trim|required');


			if ($this->form_validation->run() == TRUE) {
				// true case
				if (empty($this->input->post('password')) && empty($this->input->post('cpassword'))) {
					$data = array(
						'accountname' => $this->input->post('accountname'),
						'email' => $this->input->post('email'),
						'firstname' => $this->input->post('fname'),
						'lastname' => $this->input->post('lname'),
						'phone' => $this->input->post('phone'),
						'gender' => $this->input->post('gender'),
					);

					$update = $this->model_accounts->update($data, $id, $this->input->post('groups'));
					if ($update == true) {
						$this->session->set_flashdata('success', 'Successfully updated');
						redirect('accounts/setting/', 'refresh');
					} else {
						$this->session->set_flashdata('errors', 'Error occurred!!');
						redirect('accounts/setting/', 'refresh');
					}
				} else {
					//$this->form_validation->set_rules('password', 'Password', 'trim|required|min_length[8]');
					//$this->form_validation->set_rules('cpassword', 'Confirm password', 'trim|required|matches[password]');

					if ($this->form_validation->run() == TRUE) {

						$password = $this->password_hash($this->input->post('password'));

						$data = array(
							'accountname' => $this->input->post('accountname'),
							'password' => $password,
							'email' => $this->input->post('email'),
							'firstname' => $this->input->post('fname'),
							'lastname' => $this->input->post('lname'),
							'phone' => $this->input->post('phone'),
							'gender' => $this->input->post('gender'),
						);

						$update = $this->model_accounts->update($data, $id, $this->input->post('groups'));
						if ($update == true) {
							$this->session->set_flashdata('success', 'Successfully updated');
							redirect('accounts/setting/', 'refresh');
						} else {
							$this->session->set_flashdata('errors', 'Error occurred!!');
							redirect('accounts/setting/', 'refresh');
						}
					} else {
						// false case
						$account_data = $this->model_accounts->getAccountData($id);
						$groups = $this->model_accounts->getAccountGroup($id);

						$this->data['account_data'] = $account_data;
						$this->data['account_group'] = $groups;

						$group_data = $this->model_groups->getGroupData();
						$this->data['group_data'] = $group_data;

						$this->render_template('accounts/setting', $this->data);
					}
				}
			} else {
				// false case
				$account_data = $this->model_accounts->getAccountData($id);
				$groups = $this->model_accounts->getAccountGroup($id);

				$this->data['account_data'] = $account_data;
				$this->data['account_group'] = $groups;

				$group_data = $this->model_groups->getGroupData();
				$this->data['group_data'] = $group_data;

				$this->render_template('accounts/setting', $this->data);
			}
		}
	}
}
