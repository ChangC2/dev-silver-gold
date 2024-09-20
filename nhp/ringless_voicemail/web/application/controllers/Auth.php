<?php

defined('BASEPATH') or exit('No direct script access allowed');

class Auth extends MY_Controller
{

	public function __construct()
	{
		parent::__construct();

		$this->load->model('model_accounts');
		$this->load->model('model_signs');
	}


	public function index()
	{
		$this->load->view('login');
	}

	/* 
		Check if the login form is submitted, and validates the user credential
		If not submitted it redirects to the login page
	*/
	public function login()
	{
		$this->load->view('login');
	}

	public function admin()
	{
		$this->load->view('admin_login');
	}


	public function checkUser()
	{
		$email_exists = $this->model_accounts->check_email($this->input->post('email'));
		if ($email_exists == TRUE) {
			$login = $this->model_accounts->login($this->input->post('email'), $this->input->post('password'));
			if ($login) {
				$token = $this->encrypt($login['id']);
				$data = array(
					"permission" => $login['permission'],
					"token" => urlencode($token),
					"status" => true
				);
				echo json_encode($data);
			} else {
				$data = array(
					"status" => false
				);
				echo json_encode($data);
			}
		} else {
			$data = array(
				"status" => false
			);
			echo json_encode($data);
		}
	}

	public function terms()
	{
		$this->load->view('terms');
	}

	/*
		clears the session and redirects to login page
	*/
	public function logout()
	{
		$this->session->sess_destroy();
		redirect(base_url('auth/login'), 'refresh');
	}

	public function admin_logout()
	{
		$this->session->sess_destroy();
		redirect(base_url('auth/admin'), 'refresh');
	}

	public function signature()
	{
		$token = $this->input->post('token');
		$user_id = $this->decrypt($token);
		$user_info = $this->model_accounts->get_user_info(null, $user_id);
		$user_name = $user_info['accountname'];
		$sign = $this->input->post('data');

		$data = array(
			'sign' => $sign,
			'created_at' => date('Y-m-d H:i:s'),
			'user_id' => $user_id,
			'user_name' => $user_name,
		);

		$result = $this->model_signs->save($data);

		if ($result == false) {
			$res = array(
				"status" => false,
			);
		} else {
			$res = array(
				"status" => true,
			);
		}
		echo json_encode($res);
	}
}
