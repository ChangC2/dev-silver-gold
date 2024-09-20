<?php

class Sendmail extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();

		$this->data['page_title'] = 'Voice Recordings';

		$this->load->model('model_recordings');
		$this->load->model('model_voices');
		$this->load->model('model_scripts');
		$this->load->model('model_accounts');
		$this->load->model('model_contacts');
		$this->load->model('model_log');
		$this->load->model('model_sms_log');
		$this->load->model('model_signs');
		$this->load->model('model_common');
		$this->load->model('model_orders');
		$this->load->model('model_ips');
	}

	public function index()
	{
		$token = $this->input->get('token');
		$credits = 0;
		if ($token != null) {
			$user_id = $this->decrypt($token);
			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			if ($user_info) {
				$phone = $user_info['mobile_number'];
				$credits = $user_info['credits'];
				$farm_name = $user_info['farm_name'];
				$sign = $this->model_signs->get($user_id);
				$signed = 0;
				if ($sign != null) {
					$signed = 1;
				}

				$this->data['permission'] =	$user_info['permission'];
				$this->data['accountname'] =	$user_info['accountname'];
				$this->data['token'] =	$token;

				$recordings = $this->model_recordings->get(null, $user_id);
				$pro_voices = $this->model_voices->get_pro_voices();
				$user_voices = $this->model_voices->get_available_voices($user_id);
				$scripts = $this->model_scripts->get(null, $user_id);

				$this->data['recordings'] = $recordings;
				$this->data['pro_voices'] = $pro_voices;
				$this->data['user_voices'] = $user_voices;
				$this->data['scripts'] = $scripts;
				$this->data['contacts'] = $this->model_contacts->get(null, $user_id, 10, 0);
				$this->data["contact_count"] = count($this->model_contacts->get(null, $user_id));
				$this->data['forwarding_number'] = $phone;
				$this->data['farm_name'] = $farm_name;
				$this->data['credits'] = $credits;
				$this->data['signed'] = $signed;

				$this->data['sign_header'] = 'application/views/terms/header.php';
				$this->data['sign_part1'] = 'application/views/terms/part1.php';
				$this->data['sign_part2'] = 'application/views/terms/part2.php';
				$this->data['sign_part3'] = 'application/views/terms/part3.php';
				$this->data['sign'] = 'application/views/terms/sign.php';
				$this->data['sign_script'] = 'application/views/terms/script.php';

				$this->data['submit'] = 'application/views/sendmail/submit.php';
				$this->data['contacts_modal'] = 'application/views/sendmail/contacts.php';
				$this->data['js'] = 'application/views/sendmail/script.php';

				$this->render_template('sendmail/index', $this->data);
			} else {
				//$this->sendTokenMail();
				redirect('auth/login', 'refresh');
			}
		} else {
			//$this->sendTokenMail();
			redirect('auth/login', 'refresh');
		}
	}


	function sendTokenMail()
	{
		$user_info = $this->model_accounts->get_ip_user($this->input->ip_address());
		if ($user_info) {
			$name = $user_info['accountname'];
			$email = "Zho@newhomepage.com"; //$user_info['email'];
			$token = $this->encrypt($user_info["id"]);

			$mailBody = file_get_contents('assets/html/token.html');
			$mailBody = str_replace("user_name", $name, $mailBody);
			$mailBody = str_replace("user_token", $token, $mailBody);
			$mailBody = str_replace("user_ip", $this->input->ip_address(), $mailBody);
			$this->send_smtp(trim($email), "Ringless Voicemail", $mailBody);
		}
	}

	function checkExistInOrder()
	{
		$token = $this->input->post('token');
		$user_id = $this->decrypt($token);
		if ($this->input->post('type') == 1) {
			echo false;
		} else {
			$audioUrl = $this->input->post('audioUrl');
			if ($this->model_orders->existedInOrder($user_id, $audioUrl)) {
				echo true;
			} else {
				echo false;
			}
		}
	}

	function save_mail_log()
	{
		$token = $this->input->post('token');
		$record_name = $this->input->post('record_name');
		$farm_name = $this->input->post('farm_name');
		$audioUrl = $this->input->post('audioUrl');
		$forwarding_number = $this->input->post('forwarding_number');
		$type = $this->input->post('type');
		$content = $this->input->post('content');
		$voice_id = $this->input->post('voice_id');
		$send_time = $this->input->post('send_time');

		$user_id = $this->decrypt($token);
		$user_info = $this->model_accounts->get_user_info(null, $user_id);
		$user_name = $user_info['accountname'];
		$email = $user_info['email'];
		$contacts = $this->model_contacts->get(null, $user_id);
		$credits = $user_info['credits'];

		if (count($contacts) * 0.1 > (float)$credits) {
			$res = array(
				"status" => false,
				"message" => "Not Enough Credits to run this process.\nGo back to Custom Data and click on Credits to refill. \nEmail Helpdesk@newhomepage.com if you have any questions"
			);
		} else {
			$break_up = $this->input->post('break_up');
			$broadcastsNumber = 1;
			$betweenDays = 1;
			if ($break_up == true && count($contacts) > 1) {
				$broadcastsNumber = $this->input->post('broadcasts_number');
				$betweenDays = $this->input->post('between_days');
			}
			$six_digit_random_number = random_int(100000, 999999);
			for ($i = 0; $i < $broadcastsNumber; $i++) {
				$recordCount = intval((count($contacts) - 1) / $broadcastsNumber) + 1;
				$newSendTime = $send_time;
				if ($i > 0 && $send_time != "") {
					$sendTime = new DateTime($send_time);
					$daysToAdd = $betweenDays * $i;
					$sendTime->modify("+$daysToAdd days");
					$newSendTime = $sendTime->format('Y-m-d H:i:s');
				}

				$finalCount = $recordCount;
				if ($i == $broadcastsNumber - 1) {
					$finalCount = count($contacts) - ($broadcastsNumber - 1) * $recordCount;
				}
				$data = array(
					'created_at' => date('Y-m-d H:i:s'),
					'user_id' => $user_id,
					'user_name' => $user_name,
					'user_email' => $email,
					'record_name' => $record_name,
					'farm_name' => $farm_name,
					'forwarding_number' => $forwarding_number,
					'order_no' => $broadcastsNumber > 1 ? $six_digit_random_number . " (Scheduled-" . ($i + 1) . ")" : $six_digit_random_number,
					'record_count' => $finalCount,
					'url' => $audioUrl,
					'type' => $type,
					'content' => $content,
					'voice_id' => $voice_id,
					'send_time' => $newSendTime,
				);
				$order_id = $this->model_orders->create($data);
				for ($j = 0; $j < $recordCount; $j++) {
					if ($i * $recordCount + $j == count($contacts)) break;
					$contact = $contacts[$i * $recordCount + $j];
					$newContent = str_replace("First Name", $contact["name"], $content);
					$log_data = array(
						'created_at' => date('Y-m-d H:i:s'),
						'user_id' => $user_id,
						'user_name' => $user_name,
						'record_name' => $record_name,
						'url' => $type == 0 ? $audioUrl : "",
						'receiver' => $contact["phone"],
						'receiver_name' => $contact["name"],
						'status' => "Pending in Queue",
						'order_id' => $order_id,
						'type' => $type,
						'content' => $newContent,
						'voice_id' => $voice_id,
					);
					$this->model_log->create($log_data);
				}

				$note = "Order ID : $six_digit_random_number\nTotal Recordings : " . strval($finalCount);
				$this->updateCredits($user_id, -$finalCount * getenv('CREDITS_TO_SEND'), $note);
			}

			$res = array(
				"status" => true,
			);
		}
		echo json_encode($res);
	}

	function save_test_mail_log()
	{
		$token = $this->input->post('token');
		$record_name = $this->input->post('record_name');
		$farm_name = $this->input->post('farm_name');
		$audioUrl = $this->input->post('audioUrl');
		$forwarding_number = $this->input->post('forwarding_number');
		$type = $this->input->post('type');
		$content = $this->input->post('content');
		$voice_id = $this->input->post('voice_id');

		$user_id = $this->decrypt($token);
		$user_info = $this->model_accounts->get_user_info(null, $user_id);
		$user_name = $user_info['accountname'];
		$email = $user_info['email'];
		$credits = $user_info['credits'];

		if (0.1 > (float)$credits) {
			$res = array(
				"status" => false,
				"message" => "Not Enough Credits to run this process.\nGo back to Custom Data and click on Credits to refill. \nEmail Helpdesk@newhomepage.com if you have any questions"
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
				'record_count' => 1,
				'url' => $audioUrl,
				'type' => $type,
				'content' => $content,
				'voice_id' => $voice_id,
			);

			$order_id = $this->model_orders->create($data);
			$newContent = str_replace("First Name", strtok($user_name, " "), $content);
			$log_data = array(
				'created_at' => date('Y-m-d H:i:s'),
				'user_id' => $user_id,
				'user_name' => $user_name,
				'record_name' => $record_name,
				'url' => $audioUrl,
				'receiver' => $forwarding_number,
				'receiver_name' => $user_name,
				'status' => "Pending in Queue",
				'order_id' => $order_id,
				'type' => $type,
				'content' => $newContent,
				'voice_id' => $voice_id,
			);
			$this->model_log->create($log_data);
			$note = "Order ID:$six_digit_random_number  Ringless Voicemail:$farm_name  Recordings:1";
			$this->updateCredits($user_id, -0.1, $note);
			$res = array(
				"status" => true,
			);
		}
		echo json_encode($res);
	}

	function send_mail()
	{
		$pendingOrders = $this->model_orders->get_pendings();
		if (count($pendingOrders) > 0) {
			foreach ($pendingOrders as $order) {
				$user_info = $this->model_accounts->get_user_info(null, $order["user_id"]);
				$caller_id = $user_info["caller_id"] == "" ? $this->getCallerId() : $user_info["caller_id"];
				$maillogs = $this->model_log->get_for_order($order["id"]);
				if ($maillogs != null && count($maillogs) > 0) {
					$routeId = $this->curlCommioCreateRoute($order["forwarding_number"]);
					if ($routeId != null && $this->curlCommioAssignRoute($caller_id, $routeId)) {
						$counter = 0;
						foreach ($maillogs as $maillog) {
							if ($maillog["status"] == "Pending in Queue") {
								if (!preg_match('/^[0-9]{10}+$/', $maillog["receiver"])){
									break;
								}
								if ($counter == 100) {
									$caller_id = $user_info["caller_id"] == "" ? $this->getCallerId() : $user_info["caller_id"];
									$this->curlCommioAssignRoute($caller_id, $routeId);
									$counter = 0;
								}
								$phone = "+1" . $maillog["receiver"];
								
								if ($maillog["type"] == 0) {
									$this->curlCowBoy($phone, $maillog["url"], $caller_id, $maillog["id"]);
								} else {
									$this->curlCowBoyAI($phone, $maillog["voice_id"], $maillog["content"], $caller_id, $maillog["id"]);
								}
								$counter = $counter + 1;
								$log_data = array(
									'status' => "Processing"
								);
								$this->model_log->update($maillog["id"], $log_data);
								$cowboy_log_data = array(
									'log_id' => $maillog["id"],
									'content' => "Contact: $phone Caller ID: $caller_id"
								);
								$this->model_common->insert_record("cowboy_log", $cowboy_log_data);
							}
						}
						$order_data = array(
							'created_at' => date('Y-m-d H:i:s'),
							'status' => 1,
						);
						$this->model_orders->update($order["id"], $order_data);
					}
				}
			}
		}

		$scheduledOrders = $this->model_orders->get_scheduled();
		if (count($scheduledOrders) > 0) {
			foreach ($scheduledOrders as $order) {
				if ($order["send_time"] < date('Y-m-d H:i:s')) {
					$user_info = $this->model_accounts->get_user_info(null, $order["user_id"]);
					$caller_id = $user_info["caller_id"] == "" ? $this->getCallerId() : $user_info["caller_id"];
					$maillogs = $this->model_log->get_for_order($order["id"]);
					if ($maillogs != null && count($maillogs) > 0) {
						$routeId = $this->curlCommioCreateRoute($order["forwarding_number"]);
						if ($routeId != null && $this->curlCommioAssignRoute($caller_id, $routeId)) {
							$counter = 0;
							foreach ($maillogs as $maillog) {
								if ($maillog["status"] == "Pending in Queue") {
									if ($counter == 100) {
										$caller_id = $user_info["caller_id"] == "" ? $this->getCallerId() : $user_info["caller_id"];
										$this->curlCommioAssignRoute($caller_id, $routeId);
										$counter = 0;
									}
									$phone = "+1" . $maillog["receiver"];
									if ($maillog["type"] == 0) {
										$this->curlCowBoy($phone, $maillog["url"], $caller_id, $maillog["id"]);
									} else {
										$this->curlCowBoyAI($phone, $maillog["voice_id"], $maillog["content"], $caller_id, $maillog["id"]);
									}

									$counter = $counter + 1;

									$log_data = array(
										'status' => "Processing"
									);
									$this->model_log->update($maillog["id"], $log_data);
									$cowboy_log_data = array(
										'log_id' => $maillog["id"],
										'content' => "Contact: $phone Caller ID: $caller_id"
									);
									$this->model_common->insert_record("cowboy_log", $cowboy_log_data);
								}
							}
							$order_data = array(
								'created_at' => date('Y-m-d H:i:s'),
								'send_time' => "",
								'status' => 1,
							);
							$this->model_orders->update($order["id"], $order_data);
						}
					}
				}
			}
		}

		$res = array(
			"status" => true,
			"pending_orders" => $pendingOrders,
			"scheduled_orders" => $scheduledOrders,
		);
		echo json_encode($res);
	}

	public function getCallerId()
	{
		$callerIdsFile = "assets/caller_ids.ini"; // Line 0 : offset of current caller_id
		$lines = file($callerIdsFile); //file in to an array
		$lineNo = (int)$lines[0];
		$caller_id = $lines[$lineNo];
		$caller_id = str_replace(array("\r", "\n"), "", $caller_id);
		$lineNo = $lineNo + 1;
		if ($lineNo > count($lines) - 1) {
			$lineNo = 0;
		}
		$lines[0] = $lineNo . "\n";
		file_put_contents($callerIdsFile, implode($lines));
		return $caller_id;
	}

	public function curlCommioCreateRoute($phone)
	{

		$apiUrl = "https://api.thinq.com/account/22568/staticinrouteprofile";
		$ch = curl_init();
		// Will return the response, if false it print the response
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_URL, $apiUrl);
		$headers  = [
			'Content-Type: application/json',
			'Authorization: Basic ' . base64_encode("xavier:85ceb4838729d0ced67669aeb8d5bbcf5202face")
		];
		$name = date("YmdHsi") . "_" . $phone;
		$data = [
			'profile' => [
				'name' => $name,
				'inboundRoutes' => [
					[
						'routeType' => 'PSTN',
						'routePort' => '',
						'routeToAddress' => "1" . $phone
					]
				]
			]
		];
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		$json = curl_exec($ch);
		curl_close($ch);
		try {
			$response = json_decode($json);
			return $response->profile->id;
		}
		//catch exception
		catch (Exception $e) {
			return null;
		}
	}

	public function curlCommioAssignRoute($phone, $routeId)
	{
		$apiUrl = "https://api.thinq.com/account/22568/origination/did/routing";
		$curl = curl_init();
		// Will return the response, if false it print the response

		$data = [
			"routing" => [
				[
					"did" => $phone,
					"route_id" => $routeId
				]
			]
		];

		// Convert the data array to JSON
		$jsonData = json_encode($data);

		curl_setopt_array($curl, [
			CURLOPT_URL => $apiUrl,
			CURLOPT_RETURNTRANSFER => true,
			CURLOPT_CUSTOMREQUEST => 'PUT',
			CURLOPT_POSTFIELDS => $jsonData,
			CURLOPT_HTTPHEADER => [
				'Content-Type: application/json',
				'Authorization: Basic ' . base64_encode("xavier:85ceb4838729d0ced67669aeb8d5bbcf5202face"),
				'Content-Length: ' . strlen($jsonData)
			]
		]);

		$json = curl_exec($curl);
		// Check for any errors
		if (curl_errno($curl)) {
			return false;
		} else {
			try {
				$response = json_decode($json);
				return $response != null && $response->routing != null ? true : false;
			} catch (Exception $e) {
				return false;
			}
		}
		curl_close($curl);
	}

	public function curlCommioSendSMSMessages($from, $to, $message)
	{
		$apiUrl = "https://api.thinq.com/account/22568/product/origination/sms/send";
		$curl = curl_init();
		// Will return the response, if false it print the response

		$data = [
			"from_did" => $from,
			"to_did" => $to,
			"message" => $message
		];

		// Convert the data array to JSON
		$jsonData = json_encode($data);

		curl_setopt_array($curl, [
			CURLOPT_URL => $apiUrl,
			CURLOPT_RETURNTRANSFER => true,
			CURLOPT_CUSTOMREQUEST => 'POST',
			CURLOPT_POSTFIELDS => $jsonData,
			CURLOPT_HTTPHEADER => [
				'Content-Type: application/json',
				'Authorization: Basic ' . base64_encode("xavier:85ceb4838729d0ced67669aeb8d5bbcf5202face"),
				'Content-Length: ' . strlen($jsonData)
			]
		]);

		$json = curl_exec($curl);
		return $json;
		curl_close($curl);
	}

	public function curlCowBoy($phone, $url, $caller_id, $foreign_id)
	{

		$apiUrl = "https://api.dropcowboy.com/v1/rvm";
		$ch = curl_init();
		// Will return the response, if false it print the response
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_URL, $apiUrl);
		$headers  = [
			'Content-Type: application/json'
		];
		$postData = [
			'team_id' => '1eb4e83c-f052-47ea-8189-4673e29a89a9',
			'secret' => '48ae2d31-7c87-461c-a54c-5b64a4738dcf',
			'audio_url' => $url,
			'audio_type' => 'wav',
			'phone_number' => $phone,
			'caller_id' => "+1" . $caller_id,
			"foreign_id" => $foreign_id,
			"callback_url" => base_url() . "sendmail/cowboy_callback",
			"byoc" => "{}",
		];
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($postData));
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_exec($ch);
		curl_close($ch);
	}

	public function curlCowBoyAI($phone, $voice_id, $content, $caller_id, $foreign_id)
	{
		$apiUrl = "https://api.dropcowboy.com/v1/rvm";
		$ch = curl_init();
		// Will return the response, if false it print the response
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_URL, $apiUrl);
		$headers  = [
			'Content-Type: application/json'
		];
		$postData = [
			'team_id' => '1eb4e83c-f052-47ea-8189-4673e29a89a9',
			'secret' => '48ae2d31-7c87-461c-a54c-5b64a4738dcf',
			'voice_id' => $voice_id,
			'tts_body' => $content,
			'phone_number' => $phone,
			'caller_id' => "+1" . $caller_id,
			"foreign_id" => $foreign_id,
			"callback_url" => base_url() . "sendmail/cowboy_callback",
			"byoc" => "{}",
		];
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($postData));
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_exec($ch);
		curl_close($ch);
		// file_put_contents("log.txt", print_r($postData, 1) . PHP_EOL, FILE_APPEND);
	}

	public function cowboy_callback()
	{
		$json = file_get_contents('php://input');

		$response = json_decode($json);
		$id = $response->foreign_id;
		$log_data = array(
			'created_at' => date('Y-m-d H:i:s'),
			'status' => $response->status,
			'reason' => $response->reason,
		);
		$this->model_log->update($id, $log_data);
		$mailLog = $this->model_log->get($id);
		if ($mailLog && $mailLog["order_id"]) {
			$order = $this->model_orders->get($mailLog["order_id"]);
			if ($order && $order["note"] == "") {
				$deliveredCount = $this->model_log->get_delivered_count($order["id"]);
				$successCount = $this->model_log->get_success($order["id"]);
				if ($order["record_count"] == $deliveredCount) {
					$note = "Order ID:" . $order["order_no"] . "  Ringless Voicemail:" . $order["farm_name"] . "  Undelivered Credit Recordings:" . strval($deliveredCount - $successCount);
					$order_data = array(
						'note' => $note
					);
					$this->model_orders->update($order["id"], $order_data);

					$mailBody = file_get_contents('assets/html/index.html');
					$mailBody = str_replace("order_id", $order["order_no"], $mailBody);
					$mailBody = str_replace("account_email", $order["user_email"], $mailBody);
					$mailBody = str_replace("forwarding", $order["forwarding_number"], $mailBody);
					$mailBody = str_replace("link_to_message", $order["url"], $mailBody);
					$mailBody = str_replace("farm_name", $order["farm_name"], $mailBody);
					$mailBody = str_replace("number_of_phones", $order["record_count"], $mailBody);
					$mailBody = str_replace("date_of_submission", date('Y-m-d'), $mailBody);
					$mailBody = str_replace("time_of_submission", date('H:i:s'), $mailBody);
					$mailBody = str_replace("number_of_mails", $successCount, $mailBody);
					$mailBody = str_replace("credits_used", $successCount / 10, $mailBody);
					$subject = "Farming-" . $order["farm_name"] . " Ringless Voicemail";
					$this->send_smtp(trim($order["user_email"]), $subject, $mailBody);
					if ($order["record_name"] != "Test Message" || $mailLog["receiver_name"] != "Test User") {
						$this->updateCredits($order["user_id"], ($order["record_count"] - $successCount) * 0.1, $note);
					}
				}
			}
		}
	}

	function login()
	{
		$userIp = $this->input->ip_address();
		$ipList = $this->model_ips->get();
		$allowed = false;
		foreach ($ipList as $ip) {
			if ($ip["ip_address"] == $userIp) {
				$allowed = true;
				break;
			}
		}

		if (!$allowed) {
			$res = array(
				"status" => false,
				"message" => "This Server is not allowed by us",
			);

			$mailBody = "Server Ip : $userIp";
			$this->send_smtp(null, "Not allowed Server", $mailBody);
			echo json_encode($res);
			return;
		}

		if (!empty($this->input->post('user_email'))) {
			$request_id = $this->input->post('user_id');
			$name = $this->input->post('user_name');
			$name = strtok($name, " ");
			$email = $this->input->post('user_email');
			$phone = $this->input->post('user_phone');
			$credits = $this->input->post('user_credits');
			$farm_name = $this->input->post('farm_name');

			file_put_contents("log.txt", print_r("User ID: $request_id  Name : $name  Email : $email  Phone Number : $phone   Farm Name : $farm_name", 1) . PHP_EOL, FILE_APPEND);

			$contactsUrl = "https://videodrop.leadmarketer.com/nhpdropfiles/" . $this->input->post('contacts');
			$csvData = file_get_contents($contactsUrl);
			$rows = explode("\n", $csvData);
			$contacts = array();
			foreach ($rows as $row) {
				$newRow = str_replace("\r", '', $row);
				$s = explode(':', $newRow);
				if (count($s) > 1) {
					$contactInfo = array('name' => $s[0], 'phone' => str_replace(array('Dnc', 'DNC', 'dnc', '-', ' ', '(', ')'), "", $s[1]));
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
					"ip_address" => $this->input->ip_address(),
					"request_id" => $request_id
				);
				$this->model_accounts->update($data, $user_id);
			} else {
				$data = array(
					"accountname" => $name,
					"email" => $email,
					"mobile_number" => str_replace(array('-', ' ', '(', ')'), "", $phone),
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
				"status" => "success",
				"token" => urlencode($token),
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Missing Email Address",
			);
			echo json_encode($res);
		}
	}

	public function getIpAddress()
	{
		echo $this->input->ip_address();
	}

	function sendTestMail()
	{
		$user_id = "";
		if (!empty($this->input->post('user_email'))) {
			$request_id = $this->input->post('user_id');
			$name = $this->input->post('user_name');
			$name = strtok($name, " ");
			$email = $this->input->post('user_email');
			$user_phone = str_replace(array('-', ' ', '(', ')'), "", $this->input->post('user_phone'));
			$farm_name = $this->input->post('farm_name');
			$contact = str_replace(array('-', ' ', '(', ')'), "", $this->input->post('contact'));
			$audioUrl = $this->input->post('audio_url');

			$user_info = $this->model_accounts->get_user_info(null, null, $request_id);
			if ($user_info) {
				$user_id = $user_info['id'];
				$data = array(
					"accountname" => $name,
					"email" => $email,
					"mobile_number" => $user_phone,
					"farm_name" => $farm_name,
					"ip_address" => $this->input->ip_address(),
					"request_id" => $request_id
				);
				$this->model_accounts->update($data, $user_id);
			} else {
				$data = array(
					"accountname" => $name,
					"email" => $email,
					"mobile_number" => $user_phone,
					"request_id" => $request_id,
					"password" => '$2y$10$HvcbiRovmJhjHE4UWdaR8uP1Q5D3Yvg3Q4ibzOzYkzH6j7w3HRPVu',
					"status" => 1,
					"ip_address" => $this->input->ip_address(),
					"farm_name" => $farm_name,
					"test_count" => 5,
				);
				$user_id = $this->model_accounts->create($data, 3);
			}

			if ($user_info["test_count"] > 0) {

				$six_digit_random_number = random_int(100000, 999999);
				$data = array(
					'created_at' => date('Y-m-d H:i:s'),
					'user_id' => $user_id,
					'user_name' => $name,
					'user_email' => $email,
					'record_name' => "Test Message",
					'farm_name' => $farm_name,
					'forwarding_number' => $user_phone,
					'order_no' => $six_digit_random_number,
					'record_count' => 1,
					'status' => 1,
					'url' => $audioUrl,
				);

				$order_id = $this->model_orders->create($data);

				$log_data = array(
					'created_at' => date('Y-m-d H:i:s'),
					'user_id' => $user_id,
					'user_name' => $name,
					'url' => $audioUrl,
					'receiver' => $contact,
					'receiver_name' => "Test User",
					'status' => "Pending in Queue",
					'order_id' => $order_id,
				);
				$mailLogId = $this->model_log->create($log_data);

				$routeId = $this->curlCommioCreateRoute($user_phone);
				$caller_id = $this->getCallerId();
				if ($routeId != null && $this->curlCommioAssignRoute($caller_id, $routeId)) {
					$phone = "+1" . $contact;
					$this->curlCowBoy($phone, $audioUrl, $caller_id, $mailLogId);
				}
				$user_id = $user_info['id'];
				$data = array(
					"test_count" => $user_info["test_count"] - 1
				);
				$this->model_accounts->update($data, $user_id);
				$mailBody = "User Id : $request_id, User name : $name, User email : $email, Forwarding # : $user_phone, Caller id : $caller_id, Customer : $contact";
				$this->send_smtp(trim("xavier@newhomepage.com"), "Ringless Voicemail Test", $mailBody);

				$res = array(
					"status" => "success",
					"message" => "You have sent the test Ringless voicemail.",
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "We only allow for 5 tests from an account. If you need to get a demo of this product please reach out to our support at helpdesk@newhomepage.com",
				);
				echo json_encode($res);
			}
		} else {
			$res = array(
				"status" => false,
				"message" => "Missing Email Address",
			);
			echo json_encode($res);
		}
	}

	function sendSMSMessage()
	{
		$userIp = $this->input->ip_address();
		$ipList = $this->model_ips->get();
		$allowed = false;
		foreach ($ipList as $ip) {
			if ($ip["ip_address"] == $userIp) {
				$allowed = true;
				break;
			}
		}

		if (!$allowed) {
			$res = array(
				"status" => false,
				"message" => "This Server is not allowed by us",
				"ip" => $userIp,
			);

			$mailBody = "Server Ip : $userIp";
			// $this->send_smtp(null, "Not allowed Server", $mailBody);
			echo json_encode($res);
			return;
		}

		$request_id = $this->input->post('user_id');
		$number = $this->input->post('number');
		$message = $this->input->post('message');
		$user_info = $this->model_accounts->get_user_info(null, null, $request_id);
		if ($user_info) {
			$user_id = $user_info['id'];
			$routeId = $this->curlCommioCreateRoute($user_info["mobile_number"]);
			$caller_id = "5623302289"; //$this->getCallerId();
			$this->curlCommioAssignRoute($caller_id, $routeId);
			$response = $this->curlCommioSendSMSMessages($caller_id, $number, $message);
			try {
				$json = json_decode($response, true);
				if (isset($json["guid"])) {
					$log_data = array(
						'created_at' => date('Y-m-d H:i:s'),
						'user_id' => $user_id,
						'user_name' => $user_info["accountname"],
						'user_phone' => $user_info["mobile_number"],
						'user_email' => $user_info["email"],
						'receiver_phone' => $number,
						'content' => $message,
					);
					$this->model_sms_log->create($log_data);
					$res = array(
						"status" => true,
						"data" => $json
					);
					echo json_encode($res);
				} else {
					$res = array(
						"status" => false,
						"data" => $json,
					);
					echo json_encode($res);
				};
			} catch (Exception $e) {
				$res = array(
					"status" => true,
					"data" => "Fail to send message",
				);
				echo json_encode($res);
			}
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail to send message",
			);
			echo json_encode($res);
		}
	}
}
