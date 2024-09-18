<?php
defined('BASEPATH') or exit('No direct script access allowed');
header('Content-Type: text/html; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');
class Api extends CI_Controller
{

	public function __construct()
	{
		parent::__construct();
		$this->load->model("admin_model");
	}

	public function getUserIP()
	{
		// Get real visitor IP behind CloudFlare network
		if (isset($_SERVER["HTTP_CF_CONNECTING_IP"])) {
			$_SERVER['REMOTE_ADDR'] = $_SERVER["HTTP_CF_CONNECTING_IP"];
			$_SERVER['HTTP_CLIENT_IP'] = $_SERVER["HTTP_CF_CONNECTING_IP"];
		}
		$client  = @$_SERVER['HTTP_CLIENT_IP'];
		$forward = @$_SERVER['HTTP_X_FORWARDED_FOR'];
		$remote  = $_SERVER['REMOTE_ADDR'];

		if (filter_var($client, FILTER_VALIDATE_IP)) {
			$ip = $client;
		} elseif (filter_var($forward, FILTER_VALIDATE_IP)) {
			$ip = $forward;
		} else {
			$ip = $remote;
		}
		return $ip;
	}

	public function signout()
	{
		$response['username'] = "";
		$this->session->unset_userdata('logged_in');
		redirect(base_url() . "login");
	}


	public function get_content()
	{
		if (!isset($this->session->userdata['logged_in'])) $this->signout();
		$param = array();

		$param['userdata'] = $this->session->userdata['logged_in'];
		$param['title'] = "Plant Dashboard";

		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->getStatus($customerID));
	}

	public function get_customer_detail()
	{
		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->getInfo($customerID));
	}

	public function setEmailSetting()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		$customerID = $this->session->userdata['logged_in']['customer_id'];
		$emailString = $req['emailString'];

		echo json_encode($this->admin_model->setEmailSetting($customerID, $emailString));
	}

	public function get_allHstData()
	{
		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->get_allHstData($customerID));
	}

	public function get_machine_detail()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		if (!isset($this->session->userdata['logged_in'])) $this->signout();
		$machine_id = $req['id'];

		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->getMachineDetail($customerID, $machine_id)[0]);
	}


	public function get_mechine_details()
	{
		$factory_id = $this->input->post('factory_id');
		$machine_id = $this->input->post('machine_id');
		$res = $this->admin_model->get_mechine_details($factory_id, $machine_id);
		echo json_encode($res);
	}

	public function upload_machine_image()
	{
		$factory_id = $this->input->post('factory_id');

		if ($_FILES['image']['name'] != '') {
			$data['image'] = $_FILES['image'];
		}
		$data['machine_id'] = $this->input->post('machine_id');
		$res = $this->admin_model->upload_mechine_image($factory_id, $data);

		echo json_encode($res);
	}

	public function edit_machine_image()
	{
		$id = $this->input->post('id');
		$factory_id = $this->input->post('factory_id');

		if ($_FILES['image']['name'] != '') {
			$data['image'] = $_FILES['image'];
		}
		$res = $this->admin_model->edit_machine_image($factory_id, $id, $data);
		echo json_encode($res);
	}

	public function delete_machine_image()
	{
		$id = $this->input->post('id');
		$factory_id = $this->input->post('factory_id');
		$res = $this->admin_model->delete_machine_image($factory_id, $id);
		echo json_encode($res);
	}

	public function set_machine_install_Config()
	{
		$factory_id = $this->input->post('factory_id');
		$machine_id = $this->input->post('machine_id');
		$data['machine_name'] = $this->input->post('machine_name');
		$data['serial_number'] = $this->input->post('serial_number');
		$data['cycle_signal'] = $this->input->post('cycle_signal');
		$data['cycle_interlock_interface'] = $this->input->post('cycle_interlock_interface');
		$data['cycle_interlock_on'] = $this->input->post('cycle_interlock_on');
		$data['cycle_interlock_open'] = $this->input->post('cycle_interlock_open');
		$res = $this->admin_model->set_machine_install_Config($factory_id, $machine_id, $data);
		echo json_encode($res);
	}


	public function get_machine_time()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		$machine_name = $req['machine_id'];
		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->get_machine_detail_data($customerID, $machine_name));
	}

	public function get_machine_detail_data_today()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		$machine_name = $req['machine_id'];
		$sel_date = isset($req['sel_date']) != '' ? $req['sel_date'] : 'now';
		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->get_machine_detail_data_today($customerID, $machine_name, $sel_date));
	}



	public function get_machine_detail_data()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		$machine_name = $req['machine_id'];
		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->get_machine_detail_data($customerID, $machine_name));
	}

	public function get_ganttData()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		$machine_name = $req['machine_id'];
		$sel_date = isset($req['sel_date']) != '' ? $req['sel_date'] : 'now';

		$customerID = $this->session->userdata['logged_in']['customer_id'];
		echo json_encode($this->admin_model->get_ganttData($customerID, $machine_name, $sel_date));
	}

	public function get_ganttData2()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);
		$prefix = $req['prefix'];
		$machine_name = $req['machine_id'];
		$sel_date = isset($req['sel_date']) != '' ? $req['sel_date'] : 'now';

		echo json_encode($this->admin_model->get_ganttData($prefix, $machine_name, $sel_date));
	}


	public function get_timezone()
	{
		$customerID = $this->session->userdata['logged_in']['customer_id'];
		$info = $this->admin_model->getInfo($customerID);
		echo json_encode($info);
	}

	public function get_timezone2()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);
		$prefix = $req['prefix'];

		$info = $this->admin_model->getInfo($prefix);
		echo json_encode($info);
	}

	public function loginWithUserId()
	{
		$customerId = $this->input->post('customerId');
		$userId = $this->input->post('userId');
		$deviceId = $this->input->post('deviceId');

		$res = $this->admin_model->loginWithUserId($userId, $deviceId, $customerId);

		echo json_encode($res);
	}

	public function loginWithCustomerId()
	{
		$customerId = $this->input->post('customerId');

		$res = $this->admin_model->loginWithCustomerId($customerId);

		echo json_encode($res);
	}

	public function checkValidStatus()
	{

		$userId = $this->input->post('userId');
		$deviceId = $this->input->post('deviceId');


		$res = $this->admin_model->checkValidStatus($userId, $deviceId);

		echo json_encode($res);
	}
	public function getGanttDataMobile()
	{
		$customerId = $this->input->post('customerId');
		$machineId = $this->input->post('machineId');
		$date = $this->input->post('date');

		$res = $this->admin_model->getGanttDataMobile($customerId, $machineId, $date);

		echo json_encode($res);
	}
	public function getJobData()
	{
		$customerId = $this->input->post('customerId');
		$jobId = $this->input->post('jobId');

		$res = $this->admin_model->getJobData($customerId, $jobId);

		echo json_encode($res);
	}

	public function getJobDataByPrOrderNo()
	{
		$customerId = $this->input->post('customerId');
		$jobId = $this->input->post('jobId');

		$res = $this->admin_model->getJobDataByPrOrderNo($customerId, $jobId);

		echo json_encode($res);
	}

	public function assignMachineToUser()
	{
		// $userID = $this->input->post('userID : 145236');
		$userName = $this->input->post('userName');
		$accountID = $this->input->post('accountID');
		$machineName = $this->input->post('machineName');
		$appVersion = $this->input->post('versionInfo');
		$res = $this->admin_model->assignMachineToUser($userName, $accountID, $machineName, $appVersion);

		echo json_encode($res);
	}
	public function login()
	{
		$req = array();
		$params = trim(file_get_contents("php://input"));
		$req = json_decode($params, true);

		$login = array();
		$login['username'] = $req['username'];
		$login['password'] = $req['password'];

		$param['logins'] = $this->admin_model->login($login);

		echo json_encode($param['logins']);
	}
	public function setMachineStatus()
	{
		$customerId = $this->input->post('customerId');
		$machineId = $this->input->post('machineId');
		$status = $this->input->post('status');

		$res = $this->admin_model->setMachineStatus($customerId, $machineId, $status);
		echo json_encode($res);
	}
	public function get_table()
	{
		// header('Content-Type: application/json');
		/*       
        $req = array();
        
        $req = json_decode($params, true);

        $table = $req['table'];
        $where = $req['where'];
        // $res = $this->admin_model->get_table($table, $where);
        */
		// $params = trim(file_get_contents("php://input"));
		$req['a'] = "test";
		$req['b'] = "test";

		echo json_encode($req);
	}

	public function getAllMaintenanceInfo()
	{
		$customerId = $this->input->post('customerId');
		$machineId = $this->input->post('machineId');

		$res = $this->admin_model->getAllMaintenanceInfo($customerId, $machineId);
		echo json_encode($res);
	}
	public function enterInMaintenance()
	{
		$customerId = $this->input->post('customerId');
		$machineId = $this->input->post('machineId');
		$taskId = $this->input->post('taskId');
		$start = $this->input->post('start');

		$res = $this->admin_model->enterInMaintenance($customerId, $machineId, $taskId, $start);
		echo json_encode($res);
	}

	public function completeMaintenanceTask()
	{
		$customerId = $this->input->post('customerId');
		$machineId = $this->input->post('machineId');
		$taskId = $this->input->post('taskId');
		$completedTime = $this->input->post('completedTime');
		try {
			$startTime = $this->input->post('startTime');
		} catch (Exception $err) {
		}
		if ($startTime == null) {
			date_default_timezone_set("GMT");
			$startTime = time();
		}
		$note = $this->input->post('note');

		$res = $this->admin_model->completeMaintenanceTask($customerId, $machineId, $taskId, $completedTime, $note, $startTime);
		echo json_encode($res);
	}

	public function getMachineCalculation()
	{
		$factory_id = $this->input->post('factory_id');
		$machine_id = $this->input->post('machine_id');
		$operator = $this->input->post('operator');
		$jobID = $this->input->post('jobID');
		$date = $this->input->post('date');
		$res = $this->admin_model->getMachineCalculation($factory_id, $machine_id, $operator, $jobID, $date);
		echo json_encode($res);
	}

	public function getAppSettingFormulas()
	{
		$res = $this->admin_model->getAppSettingFormulas();
		echo json_encode($res);
	}

	public function getAppSetting()
	{
		$factory_id = $this->input->post('factory_id');
		$machine_id = $this->input->post('machine_id');
		$res = $this->admin_model->getAppSetting($factory_id, $machine_id);
		$machineDetails = $this->admin_model->get_mechine_details($factory_id, $machine_id);
		try {
			$res["data"]["machine_picture_url"] = $machineDetails["machine_details"]["machine_picture_url"];
		} catch (Exception $e) {
			$res["data"]["machine_picture_url"] = "";
		}
		echo json_encode($res);
	}

	public function updateAppSetting()
	{
		$res = array(
			"status" => true,
			"message" => "success",
			"data" => ""
		);

		try {
			$factory_id = $this->input->post('factory_id');
			$machine_id = $this->input->post('machine_id');

			$buffer_gantt = $this->admin_model->getBufferGanttData($factory_id, $machine_id);
			if (count($buffer_gantt) > 0) {
				$details = array(
					'factory_id' => $this->input->post('factory_id'),
					'machine_id' => $this->input->post('machine_id'),
					'cslock_cycle' => $this->input->post('cslock_cycle'),
					'cslock_reverse' => $this->input->post('cslock_reverse'),
				);
				$this->admin_model->updateAppSetting($factory_id, $machine_id, $details);

				$newBufferGantt = array(
					'customer_id' => $this->input->post('factory_id'),
					'machine_id' => $this->input->post('machine_id'),
					'cslock_cycle' => $this->input->post('cslock_cycle'),
					'cslock_reverse' => $this->input->post('cslock_reverse'),
				);
				$this->admin_model->updateBufferGanttData($factory_id, $machine_id, $newBufferGantt);
			} else {
				if ($this->input->post('gantt_chart_display') == 2) {
					$details = array(
						'factory_id' => $this->input->post('factory_id'),
						'machine_id' => $this->input->post('machine_id'),
						'downtime_reason1' => $this->input->post('downtime_reason1'),
						'downtime_reason2' => $this->input->post('downtime_reason2'),
						'downtime_reason3' => $this->input->post('downtime_reason3'),
						'downtime_reason4' => $this->input->post('downtime_reason4'),
						'downtime_reason5' => $this->input->post('downtime_reason5'),
						'downtime_reason6' => $this->input->post('downtime_reason6'),
						'downtime_reason7' => $this->input->post('downtime_reason7'),
						'downtime_reason8' => $this->input->post('downtime_reason8'),
						'cslock_cycle' => $this->input->post('cslock_cycle'),
						'cslock_reverse' => $this->input->post('cslock_reverse'),
						'cslock_guest' => $this->input->post('cslock_guest'),
						'cslock_alert' => $this->input->post('cslock_alert'),
						'time_stop' => $this->input->post('time_stop'),
						'time_production' => $this->input->post('time_production'),
						'cycle_send_alert' => $this->input->post('cycle_send_alert'),
						'cycle_email1' => $this->input->post('cycle_email1'),
						'cycle_email2' => $this->input->post('cycle_email2'),
						'cycle_email3' => $this->input->post('cycle_email3'),
						'automatic_part' => $this->input->post('automatic_part'),
						'automatic_min_time' => $this->input->post('automatic_min_time'),
						'automatic_part_per_cycle' => $this->input->post('automatic_part_per_cycle'),
						'gantt_chart_display' => $this->input->post('gantt_chart_display'),
						'calc_chart_title' => $this->input->post('calc_chart_title'),
						'calc_chart_formula' => $this->input->post('calc_chart_formula'),
						'calc_chart_unit' => $this->input->post('calc_chart_unit'),
						'calc_chart_option' => $this->input->post('calc_chart_option'),
						'calc_chart_disp_mode' => $this->input->post('calc_chart_disp_mode'),
						'calc_chart_max_value' => $this->input->post('calc_chart_max_value')
					);
					$this->admin_model->updateAppSetting($factory_id, $machine_id, $details);
				} else {
					$details = array(
						'factory_id' => $this->input->post('factory_id'),
						'machine_id' => $this->input->post('machine_id'),
						'downtime_reason1' => $this->input->post('downtime_reason1'),
						'downtime_reason2' => $this->input->post('downtime_reason2'),
						'downtime_reason3' => $this->input->post('downtime_reason3'),
						'downtime_reason4' => $this->input->post('downtime_reason4'),
						'downtime_reason5' => $this->input->post('downtime_reason5'),
						'downtime_reason6' => $this->input->post('downtime_reason6'),
						'downtime_reason7' => $this->input->post('downtime_reason7'),
						'downtime_reason8' => $this->input->post('downtime_reason8'),
						'cslock_cycle' => $this->input->post('cslock_cycle'),
						'cslock_reverse' => $this->input->post('cslock_reverse'),
						'cslock_guest' => $this->input->post('cslock_guest'),
						'cslock_alert' => $this->input->post('cslock_alert'),
						'time_stop' => $this->input->post('time_stop'),
						'time_production' => $this->input->post('time_production'),
						'cycle_send_alert' => $this->input->post('cycle_send_alert'),
						'cycle_email1' => $this->input->post('cycle_email1'),
						'cycle_email2' => $this->input->post('cycle_email2'),
						'cycle_email3' => $this->input->post('cycle_email3'),
						'automatic_part' => $this->input->post('automatic_part'),
						'automatic_min_time' => $this->input->post('automatic_min_time'),
						'automatic_part_per_cycle' => $this->input->post('automatic_part_per_cycle'),
						'gantt_chart_display' => $this->input->post('gantt_chart_display'),
						'calc_chart_title' => "",
						'calc_chart_formula' => "",
						'calc_chart_unit' => "",
						'calc_chart_option' => "0",
						'calc_chart_disp_mode' => "0",
						'calc_chart_max_value' => "100"
					);
					$this->admin_model->updateAppSetting($factory_id, $machine_id, $details);
				}
			}
		} catch (Exception $err) {
			$res = array(
				"status" => false,
				"message" => $err->getMessage(),
				"data" => ""
			);
		}
		echo json_encode($res);
	}


	public function getTankTemperature()
	{
		$customerId = $this->input->post('customerId');
		$activeSeconds = 30 * 60;
		if ($this->input->post('activeMin') != null) {
			$activeSeconds = $this->input->post('activeMin') * 60;
		}

		date_default_timezone_set("America/Los_Angeles");
		$currentTime = time();
		$sensors = $this->admin_model->getSensors($customerId);
		for ($i = 0; $i < count($sensors); $i++) {
			$detail = $this->admin_model->getSensorDetail($sensors[$i]['sensor_id'], $currentTime, $activeSeconds);
			if (count($detail) > 0) {
				$sensors[$i]["reading_time"] =  $detail[0]['reading_time'];
				$sensors[$i]["created_at"] =  $detail[0]['created_at'];
				$sensors[$i]["is_active"] = true;
				$sensors[$i]["value1"] = $detail[0]['value1'];
				$sensors[$i]["value2"] =  $detail[0]['value2'];
				$sensors[$i]["value3"] = $detail[0]['value3'];
			} else {
				$sensors[$i]["created_at"] =  $currentTime;
				$sensors[$i]["reading_time"] =  date('Y-m-d H:i:s');
				$sensors[$i]["is_active"] = false;
				$sensors[$i]["value1"] = 0.0;
				$sensors[$i]["value2"] =  0.0;
				$sensors[$i]["value3"] = 0.0;
			}
		}
		$res = array();
		$res['status'] = true;
		$res['sensors'] = $sensors;
		$res["message"] = "Success";
		echo json_encode($res);
	}

	// TankTime
	public function getTankTime()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$tanks = $this->admin_model->getTanks($customer_id, $part_id);
		if (count($tanks) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"tank_time" => $tanks[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	public function postTankTime()
	{
		$data = array(
			'customer_id' => $this->input->post('customer_id'),
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'part_id' => $this->input->post('part_id'),
			'machine_id' => $this->input->post('machine_id'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'ttemp1' => $this->input->post('ttemp1'),
			'ttemp2' => $this->input->post('ttemp2'),
			'ttemp3' => $this->input->post('ttemp3'),
			'ttemp4' => $this->input->post('ttemp4'),
			'ttemp5' => $this->input->post('ttemp5'),
			'ttemp6' => $this->input->post('ttemp6'),
			'ttemp7' => $this->input->post('ttemp7'),
			'ttemp8' => $this->input->post('ttemp8'),
			'ttime1' => $this->input->post('ttime1'),
			'ttime2' => $this->input->post('ttime2'),
			'ttime3' => $this->input->post('ttime3'),
			'ttime4' => $this->input->post('ttime4'),
			'ttime5' => $this->input->post('ttime5'),
			'ttime6' => $this->input->post('ttime6'),
			'ttime7' => $this->input->post('ttime7'),
			'ttime8' => $this->input->post('ttime8'),
			'toven' => $this->input->post('toven'),
			'operator' => $this->input->post('operator'),
			'rm_lot' => $this->input->post('rm_lot') != null ? $this->input->post('rm_lot') : ""
		);

		$tanks = $this->admin_model->getTanks($this->input->post('customer_id'), $this->input->post('part_id'));
		if (count($tanks) > 0) {
			$tankId = $tanks[0]['id'];
			$this->admin_model->updateTank($tankId, $data);
			$res = array(
				"status" => true,
				"message" => "success",
				"tank_time" => $this->admin_model->getTankTime($tankId)
			);
			echo json_encode($res);
		} else {
			$tankTimeId = $this->admin_model->postTankTime($data);
			if ($tankTimeId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"tank_time" => $this->admin_model->getTankTime($tankTimeId)
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	// Shift
	public function get_shift_detail()
	{
		$customerID = $this->input->post('customer_id');
		$res = array(
			"status" => true,
			"shift_info" => $this->admin_model->get_shift_detail($customerID),
			"message" => "Success"
		);
		echo json_encode($res);
	}

	public function update_shift_detail()
	{
		$customerID = $this->input->post('customer_id');
		$data = array(
			'shift1_start' => $this->input->post('shift1_start'),
			'shift1_end' => $this->input->post('shift1_end'),
			'shift2_start' => $this->input->post('shift2_start'),
			'shift2_end' => $this->input->post('shift2_end'),
			'shift3_start' => $this->input->post('shift3_start'),
			'shift3_end' => $this->input->post('shift3_end')
		);
		if ($this->admin_model->update_shift_detail($customerID, $data)) {
			$res = array(
				"status" => true,
				"message" => "Success"
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	public function postShiftData()
	{
		$customer_id = $this->input->post('customer_id');
		$shift_id = $this->input->post('shift_id');
		$time = date('Y-m-d H:i:s');
		$part_id = str_replace(' ', '', $this->input->post('partId'));
		$machine = $this->input->post('machine');
		$userid = $this->input->post('userid');

		$data = array(
			'part_id' => $part_id,
			'jobId' => $this->input->post('jobId'),
			'sequenceNo' => $this->input->post('sequenceNo') !== null ? $this->input->post('sequenceNo') : "",
			'machine' => $machine,
			'operator' => $this->input->post('operator'),
			'userid' => $userid,
			'date' => $this->input->post('date'),
			'startTime' => $this->input->post('startTime'),
			'stopTime' => $this->input->post('stopTime'),
			'oee' => $this->input->post('oee'),
			'availability' => $this->input->post('availability'),
			'quality' => $this->input->post('quality'),
			'performance' => $this->input->post('performance'),
			'goodParts' => $this->input->post('goodParts'),
			'badParts' => $this->input->post('badParts'),
			'inCycle' => $this->input->post('inCycle'),
			'uncat' => $this->input->post('uncat'),
			'offline' => $this->input->post('offline'),
			'r1t' => $this->input->post('r1t'),
			'r2t' => $this->input->post('r2t'),
			'r3t' => $this->input->post('r3t'),
			'r4t' => $this->input->post('r4t'),
			'r5t' => $this->input->post('r5t'),
			'r6t' => $this->input->post('r6t'),
			'r7t' => $this->input->post('r7t'),
			'r8t' => $this->input->post('r8t'),
			'aux1data' => $this->input->post('aux1data'),
			'aux2data' => $this->input->post('aux2data'),
			'aux3data' => $this->input->post('aux3data'),
			'rework' => $this->input->post('rework'),
			'setup' => $this->input->post('setup') !== null ? $this->input->post('setup') : "",
			'shiftTime' => ($this->input->post('shiftTime') !== null) ? $this->input->post('shiftTime') : "",
			'targetCycleTime' => ($this->input->post('targetCycleTime') !== null) ? $this->input->post('targetCycleTime') : "",
			'plannedProductionTime' => ($this->input->post('plannedProductionTime') !== null) ? $this->input->post('plannedProductionTime') : ""
		);
		// file_put_contents("log.txt", json_encode($data) . PHP_EOL, FILE_APPEND);
		if (count($this->admin_model->getShifts($customer_id, $shift_id)) > 0) {
			$this->admin_model->updateShiftData($customer_id, $shift_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"shift_id" => $shift_id
			);
			echo json_encode($res);
		} else {
			$insert_id =  $this->admin_model->postShiftData($customer_id, $data);
			if (!$insert_id) {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => true,
					"message" => "Success",
					"shift_id" => $insert_id
				);
				echo json_encode($res);
			}
		}
		// file_put_contents("log.txt", "Success" . PHP_EOL, FILE_APPEND); 
	}

	public function postUpdateOldShiftInfo()
	{
		$customer_id = $this->input->post('customer_id');
		$machine_prefix = $this->input->post('machine_prefix');
		$part_id = $this->input->post('part_id');

		$this->admin_model->postUpdateOldShiftInfo($customer_id, $machine_prefix, $part_id);
		$res = array(
			"status" => true,
			"message" => "Success",
		);
		echo json_encode($res);
		// file_put_contents("log.txt", "Success" . PHP_EOL, FILE_APPEND); 
	}

	public function postTimeSavings()
	{
		$data = array(
			'customer_id' => $this->input->post('customerId'),
			'machine_id' => $this->input->post('machineId'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'progNum' => $this->input->post('progNum'),
			'tool' => $this->input->post('tool'),
			'section' => $this->input->post('section'),
			'channel' => $this->input->post('channel'),
			'learnedTime' => $this->input->post('learnedTime'),
			'elapsedTime' => $this->input->post('elapsedTime'),
			'timeSavings' => $this->input->post('timeSavings'),
			'timeSavingsPercentage' => $this->input->post('timeSavingsPercentage')
		);

		$insertId = $this->admin_model->postTimeSavings($data);
		if ($insertId) {
			$res = array(
				"status" => true,
				"message" => "success",
				"tank_time" => $this->admin_model->getTimeSavings($insertId)
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	public function postAlarms()
	{
		$data = array(
			'customer_id' => $this->input->post('customerId'),
			'machine_id' => $this->input->post('machineId'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'progNum' => $this->input->post('progNum'),
			'tool' => $this->input->post('tool'),
			'section' => $this->input->post('section'),
			'channel' => $this->input->post('channel'),
			'alarmType' => $this->input->post('alarmType'),
			'elapsedTime' => $this->input->post('elapsedTime')
		);

		$insertId = $this->admin_model->postAlarms($data);
		if ($insertId) {
			$res = array(
				"status" => true,
				"message" => "success",
				"alarm" => $this->admin_model->getAlarm($insertId)
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	//BlastStation

	public function postBlastStation()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'media_wt_before' => $this->input->post('media_wt_before'),
			'media_wt_after' => $this->input->post('media_wt_after'),
			'media_used' => $this->input->post('media_used')
		);

		if (count($this->admin_model->getBlastStation($part_id, $customer_id)) > 0) {
			$this->admin_model->updateBlastStation($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postBlastStation($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getBlastStation()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getBlastStation($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	// CleaningStation

	public function postCleaningStation()
	{

		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'notes' => $this->input->post('notes')
		);

		if (count($this->admin_model->getCleaningStation($part_id, $customer_id)) > 0) {
			$this->admin_model->updateCleaningStation($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postCleaningStation($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getCleaningStation()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getCleaningStation($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	//  PaintStation

	public function postPaintStation()
	{

		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'ambient_temp' => $this->input->post('ambient_temp'),
			'ambient_humidity' => $this->input->post('ambient_humidity'),
			'ambient_dewpoint' => $this->input->post('ambient_dewpoint'),
			'paintbooth_temp ' => $this->input->post('paintbooth_temp'),
			'paintbooth_humidity' => $this->input->post('paintbooth_humidity'),
			'paintbooth_dewpoint' => $this->input->post('paintbooth_dewpoint'),
			'bitu_wt_before' => $this->input->post('bitu_wt_before'),
			'bitu_wt_after' => $this->input->post('bitu_wt_after'),
			'bitu_used' => $this->input->post('bitu_used')
		);

		if (count($this->admin_model->getPaintStation($part_id, $customer_id)) > 0) {
			$this->admin_model->updatePaintStation($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postPaintStation($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getPaintStation()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getPaintStation($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	//  Assembly1Station

	public function postAssembly1Station()
	{

		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'case_number' => $this->input->post('case_number'),
			'serial' => $this->input->post('serial'),
			'shipment ' => $this->input->post('shipment'),
			'empty_wt' => $this->input->post('empty_wt'),
			'center_grav' => $this->input->post('center_grav'),
			'aft_ass' => $this->input->post('aft_ass'),
			'aft_retain_ring' => $this->input->post('aft_retain_ring'),
			'shipping_cover' => $this->input->post('shipping_cover'),
			'shipping_plug ' => $this->input->post('shipping_plug'),
			'end_cap' => $this->input->post('end_cap'),
			'lugs' => $this->input->post('lugs'),
			'retain_ring' => $this->input->post('retain_ring'),
			'adapt_ring' => $this->input->post('adapt_ring'),
			'impact_ring' => $this->input->post('impact_ring'),
			'total_wt' => $this->input->post('total_wt'),
			'ass_center_grav' => $this->input->post('ass_center_grav'),
			'degrease_sol' => $this->input->post('degrease_sol'),
			'corrosin_prev_compound' => $this->input->post('corrosin_prev_compound'),
			'ship_cover_oring' => $this->input->post('ship_cover_oring'),
			'oring_grease' => $this->input->post('oring_grease'),
			'protective_end_cap' => $this->input->post('protective_end_cap'),
			'end_cap_set_screw' => $this->input->post('end_cap_set_screw'),
			'lifting_lug_bolt' => $this->input->post('lifting_lug_bolt'),
			'lifting_lug_washer' => $this->input->post('lifting_lug_washer'),
			'stencil_ink' => $this->input->post('stencil_ink')
		);

		if (count($this->admin_model->getAssembly1Station($part_id, $customer_id)) > 0) {
			$this->admin_model->updateAssembly1Station($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postAssembly1Station($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getAssembly1Station()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getAssembly1Station($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}


	//Phos_test_data
	public function postPhos_test_data()
	{
		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'weight' => $this->input->post('weight'),
			'water_break' => $this->input->post('water_break')
		);

		$insertId =  $this->admin_model->postPhos_test_data($data);
		if ($insertId) {
			$res = array(
				"status" => true,
				"message" => "success",
				"id" => $insertId
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}


	//  Blu136Assembly

	public function postBlu136Assembly()
	{

		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'base_plat11' => $this->input->post('base_plat11'),
			'20199266_fwd_fuze_line' => $this->input->post('20199266_fwd_fuze_line'),
			'20199367_aft_fuz_line' => $this->input->post('20199367_aft_fuz_line'),
			'1265394_fit_chrg_tube' => $this->input->post('1265394_fit_chrg_tube'),
			'1252629_washer_lock_iternal_tooth2' => $this->input->post('1252629_washer_lock_iternal_tooth2'),
			'4902493_retainer_fuz_liner_aft' => $this->input->post('4902493_retainer_fuz_liner_aft'),
			'1123646_nut_fit_charg_tube2' => $this->input->post('1123646_nut_fit_charg_tube2'),
			'20199361_fwd_chrg_tube' => $this->input->post('20199361_fwd_chrg_tube'),
			'20199361_030_aft_charg_tube' => $this->input->post('20199361_030_aft_charg_tube'),
			'4512424_cap_shipping' => $this->input->post('4512424_cap_shipping'),
			'nas1149f0832p_flat_washer8' => $this->input->post('nas1149f0832p_flat_washer8'),
			'nas568_41_hex_head_bolt8' => $this->input->post('nas568_41_hex_head_bolt8'),
			'x20173251_lug_shipping2' => $this->input->post('x20173251_lug_shipping2'),
			'20199362_charge_tube_plug' => $this->input->post('20199362_charge_tube_plug'),
			'nasm90725_31_screw_cap_hex_head2' => $this->input->post('nasm90725_31_screw_cap_hex_head2'),
			'ms35338_45_washer_lock_sprg4' => $this->input->post('ms35338_45_washer_lock_sprg4'),
			'mil_dtl_450_bituminous' => $this->input->post('mil_dtl_450_bituminous'),
			'as3582_236_o_ring_small2' => $this->input->post('as3582_236_o_ring_small2'),
			'923as694_o_ring_rubber' => $this->input->post('923as694_o_ring_rubber'),
			'ms51964_69_set_screw1' => $this->input->post('ms51964_69_set_screw1'),
			'a_a_208_ink_marking_stencil' => $this->input->post('a_a_208_ink_marking_stencil'),
			'mil_prf_63460_gun_oil' => $this->input->post('mil_prf_63460_gun_oil'),
			'mil_prf_16173_corrision_resistant_grease' => $this->input->post('mil_prf_16173_corrision_resistant_grease'),
			'sae_as8660_silicone_lubricant' => $this->input->post('sae_as8660_silicone_lubricant'),
			'mil_prf_680_degreasing_solvent' => $this->input->post('mil_prf_680_degreasing_solvent'),
			'shipping_plugs2' => $this->input->post('shipping_plugs2'),
			'job_at' => $this->input->post('job_at'),
			'screw_lot6' => $this->input->post('screw_lot6'),
			'threadlock_271_lot' => $this->input->post('threadlock_271_lot'),
			'set_screw_lot_6' => $this->input->post('set_screw_lot_6'),
			'ams_s_8802_lot' => $this->input->post('ams_s_8802_lot'),
			'two_part_polysulfie_sealant' => $this->input->post('two_part_polysulfie_sealant')
		);

		if (count($this->admin_model->getBlu136Assembly($part_id, $customer_id)) > 0) {
			$this->admin_model->updateBlu136Assembly($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postBlu136Assembly($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getBlu136Assembly()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getBlu136Assembly($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	//  Assembly3Station

	public function postAssembly3Station()
	{

		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'mil_d_16791_non_ionic_detergent_lot' => $this->input->post('mil_d_16791_non_ionic_detergent_lot'),
			'923as829_drive_lok_pins_lot' => $this->input->post('923as829_drive_lok_pins_lot'),
			'ams_s_8802_polysulfide_lot' => $this->input->post('ams_s_8802_polysulfide_lot'),
			'4512421_base_plate_lt' => $this->input->post('4512421_base_plate_lt'),
			'4512422_abs_insert_lot' => $this->input->post('4512422_abs_insert_lot'),
			'4512423_steel_insert_lot' => $this->input->post('4512423_steel_insert_lot')
		);

		if (count($this->admin_model->getAssembly3Station($part_id, $customer_id)) > 0) {
			$this->admin_model->updateAssembly3Station($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postAssembly3Station($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getAssembly3Station()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getAssembly3Station($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	//  Quality Station
	public function postQualityStation()
	{

		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');

		$data = array(
			'created_at' => $this->input->post('created_at'),
			'timestamp' => $this->input->post('timestamp'),
			'date' => $this->input->post('date'),
			'time' => $this->input->post('time'),
			'customer_id' => $this->input->post('customer_id'),
			'machine_id' => $this->input->post('machine_id'),
			'part_id' => $this->input->post('part_id'),
			'operator' => $this->input->post('operator'),
			'processing_time' => $this->input->post('processing_time'),
			'scrap_code' => $this->input->post('scrap_code'),
			'comments' => $this->input->post('comments')
		);

		if (count($this->admin_model->getQualityStation($part_id, $customer_id)) > 0) {
			$this->admin_model->updateQualityStation($part_id, $customer_id, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
				"part_id" => $part_id,
				"customer_id" => $customer_id
			);
			echo json_encode($res);
		} else {
			$insertId =  $this->admin_model->postQualityStation($data);
			if ($insertId) {
				$res = array(
					"status" => true,
					"message" => "success",
					"part_id" => $part_id,
					"customer_id" => $customer_id
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			}
		}
	}

	public function getQualityStation()
	{
		$customer_id = $this->input->post('customer_id');
		$part_id = $this->input->post('part_id');
		$station = $this->admin_model->getQualityStation($part_id, $customer_id);
		if (count($station) > 0) {
			$res = array(
				"status" => true,
				"message" => "success",
				"station" => $station[0]
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => false,
				"message" => "Fail"
			);
			echo json_encode($res);
		}
	}

	public function postGanttData()
	{

		$action = $this->input->post('action');
		$customer_id = $this->input->post('customer_id');
		$created_at = $this->input->post('created_at');
		$machine_id = $this->input->post('machine_id');

		$Operator = $this->input->post('Operator');
		$status = $this->input->post('status');
		$color = $this->input->post('color');
		$start = $this->input->post('start');
		$end = $this->input->post('end');
		$time_stamp = $this->input->post('time_stamp');
		$time_stamp_ms = $this->input->post('time_stamp_ms');
		$job_id = $this->input->post('job_id');
		$interface = $this->input->post('interface');
		$comment = $this->input->post('comment');

		$ganttData = $this->admin_model->getGanttData($customer_id, $machine_id, $Operator, $start, $end, $status);
		$success = false;
		$message = "";

		if ($action == 1) {
			//Check Cycle Alert
			$this->addCycleAlert($customer_id, $machine_id, $status);

			$data = array(
				'status' => $status,
				'color' => $color,
				'comment' => $comment,
				'machine_id' => $machine_id,
			);
			$success = $this->admin_model->updateGanttData($customer_id,  $start, $end, $machine_id,  $data);
			$message = "gantt updated";
		} else {
			$data = array(
				'created_at' => $created_at,
				'machine_id' => $machine_id,
				'Operator' => $Operator,
				'status' => $status,
				'color' => $color,
				'start' => $start,
				'end' => $end,
				'time_stamp' => $time_stamp,
				'time_stamp_ms' => $time_stamp_ms,
				'job_id' => $job_id,
				'interface' => $interface,
			);
			if (count($ganttData) > 0) {
				$success = true;
				$message = "gantt exist";
			} else {
				//Check Cycle Alert
				$this->addCycleAlert($customer_id, $machine_id, $status);

				$success = $this->admin_model->postGanttData($customer_id, $data);
				$message = "gantt added";
			}
		}

		$res = array(
			"status" => $success,
			"message" => $message
		);
		echo json_encode($res);
	}

	public function postGanttDataFromBuffer()
	{
		$customer_id = $this->input->post('customer_id');
		$created_at = $this->input->post('created_at');
		$machine_id = $this->input->post('machine_id');
		$status = $this->input->post('status');
		$color = $this->input->post('color');
		$start = $this->input->post('start');
		$end = $this->input->post('end');
		$time_stamp = $this->input->post('time_stamp');
		$time_stamp_ms = $this->input->post('time_stamp_ms');
		$job_id = $this->input->post('job_id');
		$interface = $this->input->post('interface');
		$comment = $this->input->post('comment');

		$ganttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
		if (count($ganttData) > 0) {
			$data = array(
				"end" => $start
			);
			$this->admin_model->updateGanttDataEnd($customer_id, $ganttData[0]["id"], $data);
		}

		//Check Cycle Alert
		$this->addCycleAlert($customer_id, $machine_id, $status);

		$success = false;
		$data = array(
			'created_at' => $created_at,
			'machine_id' => $machine_id,
			'status' => $status,
			'color' => $color,
			'start' => $start,
			'end' => $end,
			'time_stamp' => $time_stamp,
			'time_stamp_ms' => $time_stamp_ms,
			'job_id' => $job_id,
			'interface' => $interface,
			'comment' => $comment,
		);
		$success = $this->admin_model->postGanttData($customer_id, $data);
		$res = array(
			"status" => $success,
			"message" => "Success"
		);
		echo json_encode($res);
	}

	public function updateGanttData()
	{
		$customer_id = $this->input->post('customer_id');
		$machine_id = $this->input->post('machine_id');
		date_default_timezone_set('UTC');
		$start = time();
		$ganttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
		if (count($ganttData) > 0) {
			$data = array(
				"end" => $start
			);
			$this->admin_model->updateGanttDataEnd($customer_id, $ganttData[0]["id"], $data);
		}
		$res = array(
			"status" => true,
			"message" => "Success"
		);
		echo json_encode($res);
	}

	public function getBufferGanttData()
	{
		$customer_id = $this->input->post('customer_id');
		$machine_id = $this->input->post('machine_id');
		$ganttData = [];
		if ($customer_id != null && $machine_id != null) {
			$ganttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
		}

		$res = array(
			"status" => true,
			"buffer_data" => $this->admin_model->getBufferGanttData()
		);

		if (count($ganttData) > 0) {
			$res["last_data"] = $ganttData[0];
		}
		echo json_encode($res);
	}

	public function postBufferGanttData()
	{
		$customer_id = $this->input->post('customer_id') != null ? $this->input->post('customer_id') : $this->input->get('customer_id');
		$machine_id = $this->input->post('machine_id') != null ? $this->input->post('machine_id') : $this->input->get('machine_id');
		$signal = $this->input->get('signal') != null ? (int) $this->input->get('signal') : -1;
		switch ($signal) {
			case 0:
				$status = "Idle-Uncategorized";
				$color =  "#ff0000"; // Idle-Uncategorized
				break;
			case 1:
				$status = "In Cycle";
				$color =  "#46c392"; // In Cycle
				break;
			case 2:
				$status = "Idle - Machine Warning";
				$color =  "#ffff00"; // Idle - Machine Warning
				break;
			case 3:
				$status = "Idle - Machine Alarm";
				$color =  "#ff7900"; // Idle - Machine Alarm
				break;
			default:
				$status = $this->input->post('status');
				$color = $this->input->post('color');
				break;
		}

		$job_id = $this->input->post('job_id');
		$comment = $this->input->post('comment');
		$main_program = $this->input->post('main_program');
		$current_program = $this->input->post('current_program');

		date_default_timezone_set('UTC');

		$start = time();
		$end = $start;
		$time_stamp = date("Y-m-d H:i:s");
		$created_at = date("Y-m-d H:i:s");
		$interface = "ESP32(3.9.28)";

		$ganttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
		if (count($ganttData) > 0) {
			$data = array(
				"end" => $start
			);
			$this->admin_model->updateGanttDataEnd($customer_id, $ganttData[0]["id"], $data);
		}
		$newStatus = $status;
		$newColor = $color;
		$currentBufferGanttData = $this->admin_model->getBufferGanttData($customer_id, $machine_id);
		if (count($currentBufferGanttData) > 0 && $status != "In Cycle" && $status != "Offline" && $currentBufferGanttData[0]["status"] != "In Cycle" && $currentBufferGanttData[0]["status"] != "Offline") {
			$newStatus = $currentBufferGanttData[0]["status"];
			$newColor = $currentBufferGanttData[0]["color"];
		}

		$data = array(
			'status' => $newStatus,
			'color' => $newColor,
			'start' => $start,
			'end' => $end,
			'time_stamp' => $time_stamp,
			'created_at' => $created_at,
			'interface' => $interface,
		);
		if ($job_id != null) {
			$data["job_id"] = $job_id;
		}
		if ($comment != null) {
			$data["comment"] = $comment;
		}
		if ($main_program != null) {
			$data["main_program"] = $main_program;
		}
		if ($current_program != null) {
			$data["current_program"] = $current_program;
		}
		$success = $this->admin_model->updateBufferGanttData($customer_id, $machine_id, $data);
		$message = "Success";
		$buffer_ganttdata = $this->admin_model->getBufferGanttData($customer_id, $machine_id);
		$cslock_cycle = 1;
		$cslock_reverse = 0;
		if ($buffer_ganttdata != null && count($buffer_ganttdata) > 0 && $buffer_ganttdata[0]["cslock_cycle"] != null) {
			$cslock_cycle = $buffer_ganttdata[0]["cslock_cycle"];
			$cslock_reverse = $buffer_ganttdata[0]["cslock_reverse"];
		}
		$res = array(
			"status" => $success,
			"message" => $message,
			"cslock_cycle" => $cslock_cycle,
			"cslock_reverse" => $cslock_reverse,
		);
		echo json_encode($res);
	}


	public function postBufferGanttDataV2()
	{
		date_default_timezone_set('UTC');
		$device_id = $this->input->get('device_id');
		$signals = $this->input->get('signals') != null && $this->input->get('signals') != "" ? $this->input->get('signals') : "0,0,0,0,0,0,0,0,0,0,0,0";
		$inputs = explode(",", $signals);
		$this->db->select('*');
		$this->db->from('tbl_app_setting');
		$this->db->where('device_id', $device_id);
		$res = $this->db->get()->result_array();
		if (count($res) > 0) {
			$settings = $res[0];
			$machineStatusSettings = json_decode($settings["machine_status_settings"], true);
			$customer_id = $settings["factory_id"];
			$machine_id = $settings["machine_id"];
			$status = "Idle-Uncategorized";
			$color =  "#ff0000"; // Idle-Uncategorized
			foreach ($machineStatusSettings as $machineStatusSetting) {
				if (((int)$inputs[0]) == $machineStatusSetting["input1"] && ((int)$inputs[1]) == $machineStatusSetting["input2"] && ((int)$inputs[2]) == $machineStatusSetting["input3"] && ((int)$inputs[3]) == $machineStatusSetting["input4"]) {
					$status = $machineStatusSetting["status"];
					$color =  $machineStatusSetting["color"];
				}
			}
			$start = time();
			$end = $start;
			$time_stamp = date("Y-m-d H:i:s");
			$created_at = date("Y-m-d H:i:s");
			$interface = "ESP32(4.8.5)";

			$ganttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
			if (count($ganttData) > 0) {
				$data = array(
					"end" => $start
				);
				$this->admin_model->updateGanttDataEnd($customer_id, $ganttData[0]["id"], $data);
			}

			$newStatus = $status;
			$newColor = $color;
			$currentBufferGanttData = $this->admin_model->getBufferGanttData($customer_id, $machine_id);
			if (count($currentBufferGanttData) > 0 && $status != "In Cycle" && $status != "Offline" && $currentBufferGanttData[0]["status"] != "In Cycle" && $currentBufferGanttData[0]["status"] != "Offline") {
				$newStatus = $currentBufferGanttData[0]["status"];
				$newColor = $currentBufferGanttData[0]["color"];
			}
			$data = array(
				'status' => $newStatus,
				'color' => $newColor,
				'start' => $start,
				'end' => $end,
				'time_stamp' => $time_stamp,
				'created_at' => $created_at,
				'interface' => $interface,
			);
			$this->admin_model->updateBufferGanttData($customer_id, $machine_id, $data);

			$reading_time = date("Y-m-d H:i:s");
			$created_at = time();
			$data = array(
				'sensor_id' => $device_id,
				'value1' => $inputs[0],
				'value2' => $inputs[1],
				'value3' => $inputs[2],
				'value4' => $inputs[3],
				'value5' => $inputs[4],
				'value6' => $inputs[5],
				'value7' => $inputs[6],
				'value8' => $inputs[7],
				'value9' => $inputs[8],
				'value10' => $inputs[9],
				'value11' => $inputs[10],
				'value12' => $inputs[11],
				'reading_time' => $reading_time,
				'created_at' => $created_at,
			);
			$this->admin_model->postIotData($customer_id, $data);
		}
		$res = array(
			"status" => true,
		);
		echo json_encode($res);
	}

	public function postBufferGanttDataV3()
	{
		$customer_id = $this->input->post('customer_id') != null ? $this->input->post('customer_id') : $this->input->get('customer_id');
		$machine_id = $this->input->post('machine_id') != null ? $this->input->post('machine_id') : $this->input->get('machine_id');
		$status = $this->input->post('status');
		$color = $this->input->post('color');

		date_default_timezone_set('UTC');

		$start = time();
		$end = $start;
		$time_stamp = date("Y-m-d H:i:s");
		$created_at = date("Y-m-d H:i:s");

		$ganttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
		if (count($ganttData) > 0) {
			$data = array(
				"end" => $start
			);
			$this->admin_model->updateGanttDataEnd($customer_id, $ganttData[0]["id"], $data);
		}

		$data = array(
			'status' => $status,
			'color' => $color,
			'start' => $start,
			'end' => $end,
			'time_stamp' => $time_stamp,
			'created_at' => $created_at,
		);
		$success = $this->admin_model->updateBufferGanttData($customer_id, $machine_id, $data);
		$message = "Success";
		$res = array(
			"status" => $success,
			"message" => $message,
		);
		echo json_encode($res);
	}

	function addCycleAlert($customer_id, $machine_id, $status){
		$appSetting = $this->admin_model->getAppSetting($customer_id, $machine_id);
		$appSetting = $appSetting["data"];
		if ($appSetting["cycle_send_alert"] == 1) {
			$lastGanttData = $this->admin_model->getLastGanttData($customer_id, $machine_id);
			// file_put_contents("log.txt", print_r($lastGanttData, 1) . PHP_EOL, FILE_APPEND);
			if ($status == "In Cycle") {
				$this->admin_model->removeCycleAlert($customer_id, $machine_id);
			} else {
				if (count($lastGanttData) > 0 && $lastGanttData[0]["status"] == "In Cycle") {
					if ($appSetting["cycle_email1"] != "") {
						date_default_timezone_set('UTC');
						$sendTime = time() + ($appSetting["cycle_email1_time"] * 60);
						$formattedDateTime = date('Y-m-d H:i:s', $sendTime);
						$data = array(
							'customer_id' => $customer_id,
							'machine_id' => $machine_id,
							'time' => $formattedDateTime,
							'email' => $appSetting["cycle_email1"],
						);
						$this->admin_model->addCycleAlert($data);
					}

					if ($appSetting["cycle_email2"] != "") {
						date_default_timezone_set('UTC');
						$sendTime = time() + ($appSetting["cycle_email2_time"] * 60);
						$formattedDateTime = date('Y-m-d H:i:s', $sendTime);
						$data = array(
							'customer_id' => $customer_id,
							'machine_id' => $machine_id,
							'time' => $formattedDateTime,
							'email' => $appSetting["cycle_email2"],
						);
						$this->admin_model->addCycleAlert($data);
					}

					if ($appSetting["cycle_email3"] != "") {
						date_default_timezone_set('UTC');
						$sendTime = time() + ($appSetting["cycle_email3_time"] * 60);
						$formattedDateTime = date('Y-m-d H:i:s', $sendTime);
						$data = array(
							'customer_id' => $customer_id,
							'machine_id' => $machine_id,
							'time' => $formattedDateTime,
							'email' => $appSetting["cycle_email3"],
						);
						$this->admin_model->addCycleAlert($data);
					}
				}
			}
		}
	}

	public function getLastIotData()
	{
		$device_id = $this->input->post('device_id');
		$this->db->select('*');
		$this->db->from('tbl_app_setting');
		$this->db->where('device_id', $device_id);
		$res = $this->db->get()->result_array();
		$iotData = [];
		if (count($res) > 0) {
			$settings = $res[0];
			$customer_id = $settings["factory_id"];
			$iotData = $this->admin_model->getLastIotData($customer_id, $device_id);
		}
		$res = array(
			"status" => true,
			"data" => $iotData
		);
		echo json_encode($res);
	}


	public function getHst()
	{
		$customer_id = $this->input->post('customer_id');
		$date = $this->input->post('date');
		$machine_id = $this->input->post('machine_id');
		$hstData = $this->admin_model->getHstData($customer_id, $date, $machine_id);
		$res = array(
			"status" => true,
			"data" => $hstData
		);
		echo json_encode($res);
	}


	public function postHSTData()
	{
		$customer_id = $this->input->post('customer_id');
		$date = $this->input->post('date');
		$machine_id = $this->input->post('machine_id');
		$Utilization = $this->input->post('Utilization');
		$inCycle = $this->input->post('inCycle');
		$uncat = $this->input->post('uncat');
		$offline = $this->input->post('offline');
		$r1t = $this->input->post('r1t');
		$r2t = $this->input->post('r2t');
		$r3t = $this->input->post('r3t');
		$r4t = $this->input->post('r4t');
		$r5t = $this->input->post('r5t');
		$r6t = $this->input->post('r6t');
		$r7t = $this->input->post('r7t');
		$r8t = $this->input->post('r8t');
		$oee = $this->input->post('oee');
		$availability = $this->input->post('availability');
		$quality = $this->input->post('quality');
		$performance = $this->input->post('performance');
		$goodParts = $this->input->post('goodParts');
		$badParts = $this->input->post('badParts');
		$square_inch = $this->input->post('square_inch');

		$success = false;
		$hstData = $this->admin_model->getHstData($customer_id, $date, $machine_id);

		if (count($hstData) > 0) {
			$data = array(
				'Utilization' => $Utilization,
				'inCycle' => $inCycle,
				'uncat' => $uncat,
				'offline' => $offline,
				'r1t' => $r1t,
				'r2t' => $r2t,
				'r3t' => $r3t,
				'r4t' => $r4t,
				'r5t' => $r5t,
				'r6t' => $r6t,
				'r7t' => $r7t,
				'r8t' => $r8t,
				'oee' => $oee,
				'availability' => $availability,
				'quality' => $quality,
				'performance' => $performance,
				'goodParts' => $goodParts,
				'badParts' => $badParts,
				'square_inch' => $square_inch,
			);
			$success = $this->admin_model->updateHstData($customer_id,  $date, $machine_id,  $data);
		} else {
			$data = array(
				'Utilization' => $Utilization,
				'inCycle' => $inCycle,
				'uncat' => $uncat,
				'offline' => $offline,
				'r1t' => $r1t,
				'r2t' => $r2t,
				'r3t' => $r3t,
				'r4t' => $r4t,
				'r5t' => $r5t,
				'r6t' => $r6t,
				'r7t' => $r7t,
				'r8t' => $r8t,
				'oee' => $oee,
				'availability' => $availability,
				'quality' => $quality,
				'performance' => $performance,
				'date' => $date,
				'machine_id' => $machine_id,
				'goodParts' => $goodParts,
				'badParts' => $badParts,
				'square_inch' => $square_inch,
			);
			$success = $this->admin_model->postHstData($customer_id, $data);
		}

		$res = array(
			"status" => $success
		);
		echo json_encode($res);
	}

	public function postJobData()
	{
		$customer_id = $this->input->post('customer_id');
		$jobID = $this->input->post('jobID');
		$data = array(
			'jobID' => $this->input->post('jobID'),
			'order_type' => $this->input->post('order_type'),
			'seq_no' => $this->input->post('seq_no'),
			'status_type_val' => $this->input->post('status_type_val'),
			'pr_order_no' => $this->input->post('pr_order_no'),
			'customer' => $this->input->post('customer'),
			'partNumber' => $this->input->post('partNumber'),
			'description' => $this->input->post('description'),
			'qtyRequired' => $this->input->post('qtyRequired'),
			'dueDate' => $this->input->post('dueDate'),
			'orderDate' => $this->input->post('orderDate'),
			'aux1data' => $this->input->post('aux1data'),
			'aux2data' => $this->input->post('aux2data'),
			'aux3data' => $this->input->post('aux3data'),
			'pr_center_no' => $this->input->post('pr_center_no'),
			'short_desc' => $this->input->post('short_desc'),
			'bom_item' => $this->input->post('bom_item'),
			'bom_short_cd' => $this->input->post('bom_short_cd'),
			'bom_item_no' => $this->input->post('bom_item_no'),
			'bom_dim1' => $this->input->post('bom_dim1'),
			'bom_dim2' => $this->input->post('bom_dim2'),
			'bom_dim3' => $this->input->post('bom_dim3'),
			'bom_dim4' => $this->input->post('bom_dim4'),
			'bom_dim5' => $this->input->post('bom_dim5'),
			'bom_dim6' => $this->input->post('bom_dim6'),
			'bom_uom' => $this->input->post('bom_uom'),
		);

		$jobData = $this->admin_model->getJobData($customer_id, $jobID);
		if ($jobData["status"]) {
			$this->admin_model->updateJobData($customer_id, $jobID, $data);
			$res = array(
				"status" => true,
				"message" => "Success",
			);
			echo json_encode($res);
		} else {
			$insert_id =  $this->admin_model->postJobData($customer_id, $data);
			if (!$insert_id) {
				$res = array(
					"status" => false,
					"message" => "Fail"
				);
				echo json_encode($res);
			} else {
				$res = array(
					"status" => true,
					"message" => "Success",
				);
				echo json_encode($res);
			}
		}
	}

	public function postJobDataCompletedParts()
	{
		$customer_id = $this->input->post('customer_id');
		$qtyGoodCompleted = $this->input->post('qtyGoodCompleted');
		$qtyBadCompleted = $this->input->post('qtyBadCompleted');
		$jobID = $this->input->post('jobID');
		$data = array(
			'qtyGoodCompleted' => $qtyGoodCompleted,
			'qtyBadCompleted' => $qtyBadCompleted
		);
		$success = $this->admin_model->updateJobData($customer_id,  $jobID,  $data);

		$res = array(
			"status" => $success
		);
		echo json_encode($res);
	}

	public function getCurrentPartsCount()
	{
		$customer_id = $this->input->post('customer_id');
		$operator = $this->input->post('operator');
		$machine = $this->input->post('machine');
		$date = $this->input->post('date');
		$jobID = $this->input->post('jobID');

		$row = $this->admin_model->getCurrentPartsCount($customer_id,  $operator, $machine, $date,  $jobID);

		if ($row) {
			$res = array(
				"status" => true,
				"data" => $row
			);
		} else {
			$res = array(
				"status" => false
			);
		}
		echo json_encode($res);
	}


	public function postStage()
	{
		$data = array(
			'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
			'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
			'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
			'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
			'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
			's1_fa_mls' => $this->input->post('s1_fa_mls') != null ? $this->input->post('s1_fa_mls') : "",
			's1_conc' => $this->input->post('s1_conc') != null ? $this->input->post('s1_conc') : "",
			's1_ta_mls' => $this->input->post('s1_ta_mls') != null ? $this->input->post('s1_ta_mls') : "",
			's1_ta_fa' => $this->input->post('s1_ta_fa') != null ? $this->input->post('s1_ta_fa') : "",
			's1_conductivity' => $this->input->post('s1_conductivity') != null ? $this->input->post('s1_conductivity') : "",
			's1_temp' => $this->input->post('s1_temp') != null ? $this->input->post('s1_temp') : "",
			's1_time' => $this->input->post('s1_time') != null ? $this->input->post('s1_time') : "",
			's1_notes' => $this->input->post('s1_notes') != null ? $this->input->post('s1_notes') : "",
			's2_fa_mls' => $this->input->post('s2_fa_mls') != null ? $this->input->post('s2_fa_mls') : "",
			's2_conc' => $this->input->post('s2_conc') != null ? $this->input->post('s2_conc') : "",
			's2_ta_mls' => $this->input->post('s2_ta_mls') != null ? $this->input->post('s2_ta_mls') : "",
			's2_ta_fa' => $this->input->post('s2_ta_fa') != null ? $this->input->post('s2_ta_fa') : "",
			's2_conductivity' => $this->input->post('s2_conductivity') != null ? $this->input->post('s2_conductivity') : "",
			's2_temp' => $this->input->post('s2_temp') != null ? $this->input->post('s2_temp') : "",
			's2_time' => $this->input->post('s2_time') != null ? $this->input->post('s2_time') : "",
			's2_notes' => $this->input->post('s2_notes') != null ? $this->input->post('s2_notes') : "",
			's3_conductivity' => $this->input->post('s3_conductivity') != null ? $this->input->post('s3_conductivity') : "",
			's3_ph' => $this->input->post('s3_ph') != null ? $this->input->post('s3_ph') : "",
			's3_total_alk' => $this->input->post('s3_total_alk') != null ? $this->input->post('s3_total_alk') : "",
			's3_time' => $this->input->post('s3_time') != null ? $this->input->post('s3_time') : "",
			's3_notes' => $this->input->post('s3_notes') != null ? $this->input->post('s3_notes') : "",
			's4_conductivity' => $this->input->post('s4_conductivity') != null ? $this->input->post('s4_conductivity') : "",
			's4_ph' => $this->input->post('s4_ph') != null ? $this->input->post('s4_ph') : "",
			's4_temp' => $this->input->post('s4_temp') != null ? $this->input->post('s4_temp') : "",
			's4_time' => $this->input->post('s4_time') != null ? $this->input->post('s4_time') : "",
			's4_notes' => $this->input->post('s4_notes') != null ? $this->input->post('s4_notes') : "",
			's5_total_acid' => $this->input->post('s5_total_acid') != null ? $this->input->post('s5_total_acid') : "",
			's5_free_acid' => $this->input->post('s5_free_acid') != null ? $this->input->post('s5_free_acid') : "",
			's5_gas_points' => $this->input->post('s5_gas_points') != null ? $this->input->post('s5_gas_points') : "",
			's5_zinc' => $this->input->post('s5_zinc') != null ? $this->input->post('s5_zinc') : "",
			's5_conductivity' => $this->input->post('s5_conductivity') != null ? $this->input->post('s5_conductivity') : "",
			's5_temp' => $this->input->post('s5_temp') != null ? $this->input->post('s5_temp') : "",
			's5_time' => $this->input->post('s5_time') != null ? $this->input->post('s5_time') : "",
			's5_notes' => $this->input->post('s5_notes') != null ? $this->input->post('s5_notes') : "",
			's6_conductivity' => $this->input->post('s6_conductivity') != null ? $this->input->post('s6_conductivity') : "",
			's6_ph' => $this->input->post('s6_ph') != null ? $this->input->post('s6_ph') : "",
			's6_total_acid' => $this->input->post('s6_total_acid') != null ? $this->input->post('s6_total_acid') : "",
			's6_time' => $this->input->post('s6_time') != null ? $this->input->post('s6_time') : "",
			's6_notes' => $this->input->post('s6_notes') != null ? $this->input->post('s6_notes') : "",
			's7_conductivity' => $this->input->post('s7_conductivity') != null ? $this->input->post('s7_conductivity') : "",
			's7_ph' => $this->input->post('s7_ph') != null ? $this->input->post('s7_ph') : "",
			's7_absorbance' => $this->input->post('s7_absorbance') != null ? $this->input->post('s7_absorbance') : "",
			's7_time' => $this->input->post('s7_time') != null ? $this->input->post('s7_time') : "",
			's7_notes' => $this->input->post('s7_notes') != null ? $this->input->post('s7_notes') : "",
			's8_conductivity' => $this->input->post('s8_conductivity') != null ? $this->input->post('s8_conductivity') : "",
			's8_ph' => $this->input->post('s8_ph') != null ? $this->input->post('s8_ph') : "",
			's8_temp' => $this->input->post('s8_temp') != null ? $this->input->post('s8_temp') : "",
			's8_time' => $this->input->post('s8_time') != null ? $this->input->post('s8_time') : "",
			's8_notes' => $this->input->post('s8_notes') != null ? $this->input->post('s8_notes') : "",
			's9_temp' => $this->input->post('s9_temp') != null ? $this->input->post('s9_temp') : "",
			's9_time' => $this->input->post('s9_time') != null ? $this->input->post('s9_time') : "",
			's9_notes' => $this->input->post('s9_notes') != null ? $this->input->post('s9_notes') : "",
			'p_break_pass_fail' => $this->input->post('p_break_pass_fail') != null ? $this->input->post('p_break_pass_fail') : "",
			'p_grade' => $this->input->post('p_grade') != null ? $this->input->post('p_grade') : "",
			'p_phose1' => $this->input->post('p_phose1') != null ? $this->input->post('p_phose1') : "",
			'p_phose2' => $this->input->post('p_phose2') != null ? $this->input->post('p_phose2') : "",
			'p_phose3' => $this->input->post('p_phose3') != null ? $this->input->post('p_phose3') : "",
			'p_striped1' => $this->input->post('p_striped1') != null ? $this->input->post('p_striped1') : "",
			'p_striped2' => $this->input->post('p_striped2') != null ? $this->input->post('p_striped2') : "",
			'p_striped3' => $this->input->post('p_striped3') != null ? $this->input->post('p_striped3') : "",
			'p_mg1' => $this->input->post('p_mg1') != null ? $this->input->post('p_mg1') : "",
			'p_mg2' => $this->input->post('p_mg2') != null ? $this->input->post('p_mg2') : "",
			'p_mg3' => $this->input->post('p_mg3') != null ? $this->input->post('p_mg3') : "",
			'p_average' => $this->input->post('p_average') != null ? $this->input->post('p_average') : "",
			'p_notes' => $this->input->post('p_notes') != null ? $this->input->post('p_notes') : "",
			'p_date' => $this->input->post('p_date') != null ? $this->input->post('p_date') : "",
			'p_time' => $this->input->post('p_time') != null ? $this->input->post('p_time') : "",
		);

		$insert_id =  $this->admin_model->postStage($data);
		if (!$insert_id) {
			$res = array(
				"status" => false,
			);
			echo json_encode($res);
		} else {
			$res = array(
				"status" => true,
				"id" => $insert_id
			);
			echo json_encode($res);
		}
	}
}
