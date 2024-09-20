<?php

class Account extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		//$this->not_logged_in();

		$this->data['page_title'] = 'Account';

		$this->load->model('model_accounts');
		$this->load->model('model_groups');
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

			$account_data = $this->model_accounts->getAccountData($user_id);
			$groups = $this->model_accounts->getAccountGroup($user_id);

			$this->data['account_data'] = $account_data;
			$this->data['account_group'] = $groups;

			$group_data = $this->model_groups->getGroupData();
			$this->data['group_data'] = $group_data;
			$this->render_template('account/index', $this->data);
		} else {
			redirect('auth/login', 'refresh');
		}
	}

	public function update()
	{
		$token = $this->input->post('token');
		$user_id = $this->decrypt($token);
		$user_info = $this->model_accounts->get_user_info(null, $user_id);
		if ($user_info['permission'] == 0) {
			$this->saveEnv();
		}

		$phone_number = $this->input->post('mobile_number');
		$phone_number = str_replace(array("(", ")", "-", " "), "", $phone_number);

		$data = array(
			'email' => $this->input->post('email'),
			'accountname' => $this->input->post('accountname'),
			'mobile_number' => $phone_number,
			'credits' => $this->input->post('credits'),
			'caller_id' => $this->input->post('caller_id'),
			'farm_name' => $this->input->post('farm_name'),
		);
		if ($this->input->post('password') != null){
			$password = $this->password_hash($this->input->post('password'));
			$data["password"] = $password;
		}

		$update = $this->model_accounts->update($data, $user_id);

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

	public function password_hash($pass = '')
	{
		if ($pass) {
			$password = password_hash($pass, PASSWORD_DEFAULT);
			return $password;
		}
	}

	private function saveEnv()
	{
		$stripe_pk = $this->input->post('stripe_pk');
		$stripe_sk = $this->input->post('stripe_sk');
		$min_credits = $this->input->post('min_credits');
		$credit_cost = $this->input->post('credit_cost');
		$voicemail_credits = $this->input->post('voicemail_credits');

		$envFile = (ENVIRONMENT !== 'production') ? ".env.development" : ".env.production"; // Line 0 : offset of current caller_id
		$lines = file($envFile); //file in to an array
		for ($i = 0; $i < count($lines); $i++) {
			if (strpos($lines[$i], "STRIPE_PUBLISHABLE_KEY") !== false) {
				$lines[$i] = "STRIPE_PUBLISHABLE_KEY=$stripe_pk\n";
			}
			if (strpos($lines[$i], "STRIPE_SECRET_KEY") !== false) {
				$lines[$i] = "STRIPE_SECRET_KEY=$stripe_sk\n";
			}
			if (strpos($lines[$i], "CREDIT_COST") !== false) {
				$lines[$i] = "CREDIT_COST=$credit_cost\n";
			}
			if (strpos($lines[$i], "MIN_CREDITS_TO_PURCHASE") !== false) {
				$lines[$i] = "MIN_CREDITS_TO_PURCHASE=$min_credits\n";
			}
			if (strpos($lines[$i], "CREDITS_TO_SEND") !== false) {
				$lines[$i] = "CREDITS_TO_SEND=$voicemail_credits\n";
			}
		}
		file_put_contents($envFile, implode($lines));
	}
}
