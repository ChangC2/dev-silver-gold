<?php

class Maillog extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->not_logged_in();

		$this->data['page_title'] = 'Mail Log';

		$this->load->model('model_log');
		$this->load->model('model_contacts');
	}

	public function index()
	{
		$user_id = $this->session->userdata('id');
		$maillog = $this->session->userdata('permission') > 0 ?  $this->model_log->get(null, $user_id) : $this->model_log->get();
		$this->data['maillog_data'] = $maillog;
		$this->data['play'] = 'application/views/maillog/play.php';
		$this->data['mail'] = 'application/views/maillog/mail.php';
		$this->data['js'] = 'application/views/maillog/script.php';
		$this->data['contacts'] = $this->model_contacts->get();
		$this->render_template('maillog/index', $this->data);
	}

	public function delete()
	{
		$id = $_POST['id'];
		$delete = $this->model_log->delete($id);
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
