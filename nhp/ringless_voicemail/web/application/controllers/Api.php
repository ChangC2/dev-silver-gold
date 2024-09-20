<?php


class Api extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->load->model('model_recordings');
		$this->load->model('model_accounts');
		$this->load->model('model_contacts');
		$this->load->model('model_log');
		$this->load->model('model_signs');
		$this->load->model('model_common');
		$this->load->model('model_orders');
		$this->load->model('model_ips');
	}

	public function index()
	{
	}

	function login()
	{
		$request_id = $this->input->post('user_id');
		$request_id = $this->decryptString($request_id);
		$name = $this->input->post('user_name');
		$name = strtok($name, " ");
		$email = $this->input->post('user_email');
		$phone = $this->input->post('user_phone');
		$credits = $this->input->post('user_credits');
		$farm_name = $this->input->post('farm_name');
		$contactsUrl = $this->fileUpload();

		if (!$contactsUrl) {
			$res = array(
				"status" => false,
				"message" => "No Contacts",
			);
			echo json_encode($res);
		} else {
			$csvData = file_get_contents($contactsUrl);
			$rows = explode("\n", $csvData);
			$contacts = array();
			foreach ($rows as $row) {
				$newRow = str_replace("\r", '', $row);
				$s = explode(':', $newRow);
				if (count($s) > 1) {
					$contactInfo = array('name' => $s[0], 'phone' => str_replace(array('-', ' ', '(', ')'), "", $s[1]));
					$contacts[] = $contactInfo;
				}
			}

			$user_id = "";
			$user_info = $this->model_accounts->get_user_info(null, null, $request_id);
			if ($user_info) {
				$user_id = $user_info['id'];
				$data = array(
					"accountname" => $name,
					"email" => $email,
					"mobile_number" => str_replace(array('-', ' ', '(', ')'), "", $phone),
					"credits" => $credits,
					"farm_name" => $farm_name,
					"password" => '$2y$10$HvcbiRovmJhjHE4UWdaR8uP1Q5D3Yvg3Q4ibzOzYkzH6j7w3HRPVu',
					"request_id" => $request_id
				);
				$this->model_accounts->update($data, $user_id);
			} else {
				$data = array(
					"accountname" => $name,
					"email" => $email,
					"mobile_number" => $phone,
					"request_id" => $request_id,
					"password" => '$2y$10$HvcbiRovmJhjHE4UWdaR8uP1Q5D3Yvg3Q4ibzOzYkzH6j7w3HRPVu',
					"status" => 1,
					"credits" => $credits,
					"ip_address" => $this->input->ip_address(),
					"farm_name" => $farm_name
				);
				$user_id = $this->model_accounts->create($data, 3);
			}

			$this->model_contacts->delete(null, $user_id);
			if ($contacts != null && count($contacts) > 0) {
				foreach ($contacts as $contact) {
					$data = array(
						'name' => $contact["name"],
						'phone' => $contact["phone"],
						'created_at' => date('Y-m-d H:i:s'),
						'user' => $user_id,
					);
					$this->model_contacts->create($data);
				}
			}

			$token = $this->encrypt($user_id);
			$res = array(
				"status" => true,
				"token" => $token,
			);
			echo json_encode($res);
		}
	}

	function getRecordings()
	{
		$token = $this->input->post('token');
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			if ($user_info) {
				$data["records"] = $this->model_recordings->get(null, $user_id);
				$data["contacts"] = $this->model_contacts->get(null, $user_id, 10, 0);
				$data["contact_count"] = count($this->model_contacts->get(null, $user_id));
				$data["farm_name"] = $user_info["farm_name"];
				$data["credits"] = $user_info["credits"];
				$data["forwarding_number"] = $user_info["mobile_number"];
				$res = array(
					"status" => true,
					"data" => $data,
				);
			} else {
				$res = array(
					"status" => false,
					"message" => "Invalid Token",
				);
			}
		} else {
			$res = array(
				"status" => false,
				"message" => "Invalid Token",
			);
		}
		echo json_encode($res);
	}

	function addRecording()
	{
		$token = $this->input->post('token');
		if (!isset($token)){
			$token = $this->input->post('user_id');
		}
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			if ($user_info) {
				$user_name = $user_info['accountname'];
				$audioUrl = $this->fileUpload("audios");
				//echo base_url("/") . $audioUrl . "\n";
				if ($audioUrl) {
					$data = array(
						'name' => $this->input->post('name'),
						'url' => base_url("/") . $audioUrl,
						'created_at' => date('Y-m-d H:i:s'),
						'user_id' => $user_id,
						'user_name' => $user_name,
					);
					$result = $this->model_recordings->create($data);
					if ($result == false) {
						$res = array(
							"status" => false,
							"message" => "Error occurred!"
						);
					} else {
						$data["id"] = $result;
						$res = array(
							"status" => true,
							"record" => $data,
						);
					}
				} else {
					$res = array(
						"status" => false,
						"message" => "Error occurred!"
					);
				}
			} else {
				$res = array(
					"status" => false,
					"message" => "Invalid Token",
				);
			}
		} else {
			$res = array(
				"status" => false,
				"message" => "Invalid Token",
			);
		}
		echo json_encode($res);
	}

	function updateRecording()
	{
		$id = $_POST['id'];
		$data = array(
			'name' => $this->input->post('name'),
		);
		$audioUrl = $this->fileUpload("audios");
		if ($audioUrl) {
			$data["url"] = base_url("/") . $audioUrl;
		}
		$update = $this->model_recordings->update($id, $data);
		if ($update == true) {
			$res = array(
				"status" => true,
				"record" => $this->model_recordings->get($id)
			);
		} else {
			$res = array(
				"status" => false,
				"message" => "Error occurred!"
			);
		}
		echo json_encode($res);
	}

	function deleteRecording()
	{
		$id = $this->input->post("id");
		$delete = $this->model_recordings->delete($id);
		if ($delete == true) {
			$res = array(
				"status" =>  true
			);
		} else {
			$res = array(
				"status" => false,
				"message" => "Error occurred!"
			);
		}
		echo json_encode($res);
	}

	function getOrders()
	{
		$token = $this->input->post('token');
		if ($token != null) {
			$user_id = $this->decrypt($token);

			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			if ($user_info) {
				$orders = $this->model_orders->get(null, $user_id);
				$combinedOrders = array();
				foreach ($orders as $order) {
					$row = $order;
					$row["success"] = $this->model_log->get_success($order["id"]);
					$row["fail"] = $this->model_log->get_fail($order["id"]);
					$row["queue"] = $row["record_count"] - $row["success"] - $row["fail"];
					$combinedOrders[] = $row;
				}
				$res = array(
					"status" => true,
					"orders" => $combinedOrders,
				);
			} else {
				$res = array(
					"status" => false,
					"message" => "Invalid Token",
				);
			}
		} else {
			$res = array(
				"status" => false,
				"message" => "Invalid Token",
			);
		}
		echo json_encode($res);
	}

	function getMailLogs()
	{
		$order_id = $_POST['order_id'];
		$order = $this->model_orders->get($order_id);
		$logs = $this->model_log->get_for_order($order_id, 10, 0);
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

	function checkExistInOrder()
	{
		$token = $this->input->post('token');
		$user_id = $this->decrypt($token);
		$audioUrl = $this->input->post('audioUrl');
		if ($this->model_orders->existedInOrder($user_id, $audioUrl)) {
			$res = array(
				"status" => true,
			);
		} else {
			$res = array(
				"status" => false,
			);
		}
		echo json_encode($res);
	}

	function saveMailLog()
	{
		$token = $this->input->post('token');
		$record_name = $this->input->post('record_name');
		$farm_name = $this->input->post('farm_name');
		$audioUrl = $this->input->post('audioUrl');
		$forwarding_number = $this->input->post('forwarding_number');

		$user_id = $this->decrypt($token);
		$user_info = $this->model_accounts->get_user_info(null, $user_id);
		$user_name = $user_info['accountname'];
		$email = $user_info['email'];
		$contacts = $this->model_contacts->get(null, $user_id);
		$credits = $user_info['credits'];

		if (count($contacts) * 0.1 > (float)$credits) {
			$res = array(
				"status" => false,
				"message" => "Not Enough Credits to run this process. Go back to Custom Data and click on Credits to refill. Email Helpdesk@newhomepage.com if you have any questions"
			);
		} else {
			$six_digit_random_number = random_int(100000, 999999);
			$data = array(
				'created_at' => date('Y-m-d H:i:s'),
				'user_id' => $user_id,
				'user_name' => $user_name,
				'user_email' => $email,
				'record_name' => $record_name,
				'farm_name' => $farm_name,
				'forwarding_number' => $forwarding_number,
				'order_no' => $six_digit_random_number,
				'record_count' => count($contacts),
				'url' => $audioUrl,
			);

			$order_id = $this->model_orders->create($data);

			$ids = array();

			foreach ($contacts as $contact) {
				$log_data = array(
					'created_at' => date('Y-m-d H:i:s'),
					'user_id' => $user_id,
					'user_name' => $user_name,
					'url' => $audioUrl,
					'receiver' => $contact["phone"],
					'receiver_name' => $contact["name"],
					'status' => "Pending in Queue",
					'order_id' => $order_id,
				);
				$ids[] = $this->model_log->create($log_data);
			}
			$note = "Order ID:$six_digit_random_number  Ringless Voicemail:$farm_name  Records:" . strval(count($contacts));

			$this->updateCredits($user_id, -count($contacts) * 0.1, $farm_name, $note);

			$res = array(
				"status" => true,
			);
		}
		echo json_encode($res);
	}

	function decryptString($value){
		$xorConstant = 0x53;
		$data = base64_decode($value);

		$result = '';
		for ($i = 0; $i < strlen($data); $i++) {
			$result .= chr(ord($data[$i]) ^ $xorConstant);
		}

		// Assuming the result is in valid UTF-8, no conversion is necessary.
		$plainText = $result;
		return $plainText;
	}

	function testDecrypt()
	{
		$code = $_POST['code'];
		echo strtok($code, " "); // Test
		// echo $this->decryptString($code);
	}

	function testEncrypt()
	{
		$code = $_POST['code'];
		echo $this->encryptString($code);
	}
}
