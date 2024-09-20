<?php

class Orders extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Orders';

		$this->load->model('model_orders');
		$this->load->model('model_contacts');
		$this->load->model('model_log');
		$this->load->model('model_accounts');
	}

	public function index()
	{
		$this->data['page_title'] = 'Pending Orders';
		$user_id = "";
		if ($this->input->get('token') != null) {
			$token = $this->input->get('token');
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$this->data['permission'] =	$user_info['permission'];
			$this->data['accountname'] =	$user_info['accountname'];
			$this->data['token'] =	$token;
			if (!$user_info) {
				redirect('auth/login', 'refresh');
			}
		} else {
			redirect('auth/login', 'refresh');
		}

		$selected_user = $this->input->get('selected_user');
		$start_date = $this->input->get('start_date');
		$end_date = $this->input->get('end_date');
		$search_query = $this->input->get('search_query');

		$orders = $this->model_orders->get_pendings($user_info['permission'] == 0 ? $selected_user : $user_id, $start_date, $end_date, $search_query);
		$combinedOrders = array();
		foreach ($orders as $order) {
			$row = $order;
			$row["success"] = $this->model_log->get_success($order["id"]);
			$row["fail"] = $this->model_log->get_fail($order["id"]);
			$row["queue"] = $row["record_count"] - $row["success"] - $row["fail"];
			$combinedOrders[] = $row;
		}

		$users = $this->model_accounts->getUsers();
		$this->data['orders_data'] = $combinedOrders;
		$this->data['selected_user'] = $selected_user;
		$this->data['start_date'] = $start_date;
		$this->data['end_date'] = $end_date;
		$this->data['search_query'] = $search_query;
		$this->data['users'] = $users;
		$this->data['contacts_modal'] = 'application/views/orders/contacts.php';
		$this->data['play'] = 'application/views/orders/play.php';
		$this->data['js'] = 'application/views/orders/script.php';
		$this->render_template('orders/index', $this->data);
	}

	public function scheduled()
	{
		$this->data['page_title'] = 'Scheduled Orders';
		$user_id = "";
		if ($this->input->get('token') != null) {
			$token = $this->input->get('token');
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$this->data['permission'] =	$user_info['permission'];
			$this->data['accountname'] =	$user_info['accountname'];
			$this->data['token'] =	$token;
			if (!$user_info) {
				redirect('auth/login', 'refresh');
			}
		} else {
			redirect('auth/login', 'refresh');
		}

		$selected_user = $this->input->get('selected_user');
		$start_date = $this->input->get('start_date');
		$end_date = $this->input->get('end_date');
		$search_query = $this->input->get('search_query');

		$orders = $this->model_orders->get_scheduled($user_info['permission'] == 0 ? $selected_user : $user_id, $start_date, $end_date, $search_query);
		$combinedOrders = array();
		foreach ($orders as $order) {
			$row = $order;
			$row["success"] = $this->model_log->get_success($order["id"]);
			$row["fail"] = $this->model_log->get_fail($order["id"]);
			$row["queue"] = $row["record_count"] - $row["success"] - $row["fail"];
			$combinedOrders[] = $row;
		}
		$users = $this->model_accounts->getUsers();
		$this->data['orders_data'] = $combinedOrders;
		$this->data['selected_user'] = $selected_user;
		$this->data['start_date'] = $start_date;
		$this->data['end_date'] = $end_date;
		$this->data['search_query'] = $search_query;
		$this->data['users'] = $users;
		$this->data['contacts_modal'] = 'application/views/orders/contacts.php';
		$this->data['play'] = 'application/views/orders/play.php';
		$this->data['js'] = 'application/views/orders/script.php';
		$this->render_template('orders/index', $this->data);
	}

	public function delivered()
	{
		$this->data['page_title'] = 'Delivered Orders';
		$user_id = "";
		if ($this->input->get('token') != null) {
			$token = $this->input->get('token');
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$this->data['permission'] =	$user_info['permission'];
			$this->data['accountname'] =	$user_info['accountname'];
			$this->data['token'] =	$token;
			if (!$user_info) {
				redirect('auth/login', 'refresh');
			}
		} else {
			redirect('auth/login', 'refresh');
		}

		$selected_user = $this->input->get('selected_user');
		$start_date = $this->input->get('start_date');
		$end_date = $this->input->get('end_date');
		$search_query = $this->input->get('search_query');

		$orders = $this->model_orders->get_delivered($user_info['permission'] == 0 ? $selected_user : $user_id, $start_date, $end_date, $search_query);
		$combinedOrders = array();
		foreach ($orders as $order) {
			$row = $order;
			$row["success"] = $this->model_log->get_success($order["id"]);
			$row["fail"] = $this->model_log->get_fail($order["id"]);
			$row["queue"] = $row["record_count"] - $row["success"] - $row["fail"];
			$combinedOrders[] = $row;
		}
		$users = $this->model_accounts->getUsers();
		$this->data['orders_data'] = $combinedOrders;
		$this->data['selected_user'] = $selected_user;
		$this->data['start_date'] = $start_date;
		$this->data['end_date'] = $end_date;
		$this->data['search_query'] = $search_query;
		$this->data['users'] = $users;
		$this->data['contacts_modal'] = 'application/views/orders/contacts.php';
		$this->data['play'] = 'application/views/orders/play.php';
		$this->data['js'] = 'application/views/orders/script.php';
		$this->render_template('orders/index', $this->data);
	}

	public function viewVoicemail()
	{
		$id = $_POST['id'];
		$order = $this->model_orders->get($id);
		$logs = $this->model_log->get_for_order($id, 10, 0);
		$combinedLogs = array();
		foreach ($logs as $log) {
			$row = $log;
			$row["farm_name"] = $order["farm_name"];
			$row["forwarding_number"] = $order["forwarding_number"];
			$combinedLogs[] = $row;
		}
		$res = array(
			"status" => true,
			"mail_logs" => $combinedLogs,
		);
		echo json_encode($res);
	}

	public function delete()
	{
		$id = $_POST['id'];
		$order = $this->model_orders->get($id);
		$note = "Order ID:" . $order["order_no"] . "  Ringless Voicemail:" . $order["farm_name"] . "  Undelivered Credit Recordings:" . $order["record_count"];
		$this->updateCredits($order["user_id"], $order["record_count"] * 0.1, $note);
		$delete = $this->model_orders->delete($id);
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

	public function resend_failed()
	{
		$id = $_POST['id'];
		$order_data = array(
			'created_at' => date('Y-m-d H:i:s'),
			'status' => 0,
			'note' => "",
		);
		$this->model_orders->update($id, $order_data);
		$this->model_log->resend_failed($id);

		$fails = $this->model_log->get_fail($id);
		$order = $this->model_orders->get($id);
		$note = "Order ID:" . $order["order_no"] . "  Ringless Voicemail:" . $order["farm_name"] . "  Resent Credit Recordings:" . $fails;
		$this->updateCredits($order["user_id"], -$fails * 0.1, $note);
		$res = array(
			"status" => true,
		);
		echo json_encode($res);
	}

	public function resend_all()
	{
		$id = $_POST['id'];
		$order = $this->model_orders->get($id);
		$six_digit_random_number = random_int(100000, 999999);
		$data = array(
			'created_at' => date('Y-m-d H:i:s'),
			'user_id' => $order["user_id"],
			'user_name' => $order["user_name"],
			'user_email' => $order["user_email"],
			'record_name' => $order["record_name"],
			'farm_name' => $order["farm_name"],
			'forwarding_number' => $order["forwarding_number"],
			'order_no' => $six_digit_random_number,
			'record_count' => $order["record_count"],
			'url' => $order["url"],
			'type' => $order["type"],
			'content' => $order["content"],
			'voice_id' => $order["voice_id"],
			'send_time' => "",
		);
		$order_id = $this->model_orders->create($data);

		$logs = $this->model_log->get_for_order($id);
		foreach ($logs as $log) {
			$log_data = array(
				'created_at' => date('Y-m-d H:i:s'),
				'user_id' => $log["user_id"],
				'user_name' => $log["user_name"],
				'record_name' => $log["record_name"],
				'url' => $log["url"],
				'receiver' => $log["receiver"],
				'receiver_name' => $log["receiver_name"],
				'status' => "Pending in Queue",
				'order_id' => $order_id,
				'type' => $log["type"],
				'content' => $log["content"],
				'voice_id' => $log["voice_id"],
			);
			$this->model_log->create($log_data);
		}
		$note = "Order ID :" .  $order['order_no'] . "_Resent" . "\nTotal Recordings : " . $order["record_count"];
		$this->updateCredits($order["user_id"], -$order["record_count"] * 0.1, $note);
		$res = array(
			"status" => true,
		);
		echo json_encode($res);
	}
}
