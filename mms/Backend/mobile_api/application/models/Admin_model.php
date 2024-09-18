<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Admin_model extends CI_Model
{

	public function login($data)
	{
		$this->db->select('*');
		$query = $this->db->get_where('user_login_barcode', array('username' => $data['username'], 'password' => $data['password']));
		return $query->result();
	}
	public function getInfo($prefix)
	{
		$query = $this->db->get($prefix . '_info');
		return $query->result();
	}
	public function setEmailSetting($prefix, $emailString)
	{
		$this->db->set('emails', $emailString);
		$this->db->update($prefix . '_info');
	}

	public function getStatus($prefix)
	{
		$this->db->order_by("id", "asc");
		$query = $this->db->get($prefix . '_status');
		return $query->result();
	}

	public function getMachineDetail($prefix, $machine_id)
	{
		$query = $this->db->get_where($prefix . '_status', array('id' => $machine_id));
		return $query->result();
	}

	function get_mechine_details($factory_id, $machine_id)
	{
		$res = array();

		$tableName = $factory_id . '_status';
		$this->db->where('machine_id', $machine_id);
		$query = $this->db->get($tableName);
		$res['machine_details'] = $query->row_array();

		$tableName = $factory_id . '_machine_image';
		$this->db->where('machine_id', $machine_id);
		$query = $this->db->get($tableName);
		$res['images'] = $query->result_array();

		$res['status'] = true;
		$res["message"] = "Success";
		return $res;
	}

	function upload_mechine_image($factory_id, $data)
	{
		if ($data['image']['name'] != '') {
			$tempFile = $data['image']['tmp_name'];
			$path = $data['image']['name'];
			$ext = pathinfo($path, PATHINFO_EXTENSION);
			$img_name = time();

			$data['image'] = "images/machine/" . $img_name . "." . $ext;
			move_uploaded_file($tempFile, $data['image']);
		}

		$this->db->insert($factory_id . '_machine_image', $data);
		$res = array();
		$res['status'] = true;
		$res['image'] = $data['image'];
		$res["message"] = "Success";
		return $res;
	}

	function edit_machine_image($factory_id, $id, $data)
	{
		if ($data['image']['name'] != '') {
			$tempFile = $data['image']['tmp_name'];
			$path = $data['image']['name'];
			$ext = pathinfo($path, PATHINFO_EXTENSION);
			$img_name = time();

			$data['image'] = "images/machine/" . $img_name . "." . $ext;
			move_uploaded_file($tempFile, $data['image']);
		}
		$this->db->where('id', $id);
		$this->db->update($factory_id . '_machine_image', $data);
		$res = array();
		$res['status'] = true;
		$res['image'] = $data['image'];
		$res["message"] = "Success";
		return $res;
	}

	function delete_machine_image($factory_id, $id)
	{
		$this->db->where('id', $id);
		$this->db->delete($factory_id . '_machine_image');
		$res = array();
		$res['status'] = true;
		$res["message"] = "Success";
		return $res;
	}

	public function getMachineDetail2($prefix, $machine_id)
	{
		$query = $this->db->get_where($prefix . '_status', array('machine_id' => $machine_id));
		return $query->result();
	}


	public function get_machine_detail_data($prefix, $machine_name)
	{
		$query = $this->db->get_where($prefix . '_hst', array('machine_id' => $machine_name));
		return $query->result();
	}
	public function get_allHstData($prefix)
	{
		$query = $this->db->get($prefix . '_hst');
		return $query->result();
	}

	public function get_machine_detail_data_today($prefix, $machine_name, $sel_date)
	{
		//date_default_timezone_set('America/Los_Angeles');
		if ($sel_date == "now") {

			date_default_timezone_set("GMT");
			$second = round(microtime(true));
			$time_diff = $this->db->select('timezone')->get($prefix . '_info')->row()->timezone;
			$time_diff = $time_diff * 3600;
			$second = $second + $time_diff;
			$sel_date = date('m/d/Y', $second);
		}
		//$date = $now->format('m/d/Y');
		$query = $this->db->get_where($prefix . '_hst', array('machine_id' => $machine_name, 'date' => $sel_date));

		return $query->result();
	}

	public function set_machine_install_Config($factory_id, $machine_id, $data)
	{
		$tableName = $factory_id . "_status";
		$this->db->where('machine_id', $machine_id);
		$this->db->update($tableName, $data);

		$res = array();
		$res['status'] = true;
		$res["message"] = "Success";
		return $res;
	}

	public function get_ganttData($prefix, $machine_name, $sel_date)
	{
		date_default_timezone_set("GMT");
		$time_diff = $this->db->select('timezone')->get($prefix . '_info')->row()->timezone;
		$min_diff = $time_diff * 60;
		$time_diff = $time_diff * 3600;
		$this->db->where("machine_id", $machine_name);

		$res = array();

		if ($sel_date == "now") {
			$sel_date = date('m/d/Y', strtotime($min_diff . " minutes"));
		}

		// changing sel_date format.
		$date = new DateTime($sel_date);
		$fromSecond = $date->getTimestamp() - $time_diff;
		$date = $date->add(new DateInterval('P1D'));
		$toSecond = $date->getTimestamp() - $time_diff;


		$this->db->where("start >=", $fromSecond);
		$this->db->where("end <=", $toSecond);
		$query = $this->db->get($prefix . "_ganttdata");

		$res = $query->result();

		for ($i = 0; $i < count($res); $i++) {
			if ($res[$i]->start <= $fromSecond)
				$res[$i]->start = $fromSecond;

			if ($res[$i]->end >= $toSecond)
				$res[$i]->end = $toSecond;
		}



		return $res;
	}

	public function loginWithUserId($userId, $deviceId, $customerId)
	{
		$verify = $this->checkValidStatus($userId, $deviceId);
		if ($verify['status'] == true || $verify['message'] == "No data.") {
			// check if there's user login info.
			$res = $this->getUserInfoFromLoginBarcode($userId);
			if ($customerId != null && !$res) {
				$res = $this->getUserInfoFromUserTable($customerId, $userId);
			}
			if ($res) {
				if ($verify['status'] == false) {
					$param = array(
						"userId" => $userId,
						"deviceId" => $deviceId,
						"valid" => 1
					);
					$this->db->insert('tbl_devices', $param);
				}

				return array(
					"status" => true,
					"message" => "Success",
					"data" => $res[0]
				);
			} else {
				return array(
					"status" => false,
					"message" => "No data.",
					"data" => null
				);
			}
		} else {
			return array(
				"status" => false,
				"message" => "This device isn't allowed.",
				"data" => null
			);
		}
	}

	function getUserInfoFromLoginBarcode($userId)
	{
		$tableName = "user_login_barcode";
		$this->db->where("id", $userId);
		$this->db->from($tableName);
		$count = $this->db->count_all_results();
		if ($count > 0) {
			$this->db->where("id", $userId);
			$query = $this->db->get($tableName);
			$res = $query->result();
			return $res;
		} else {
			return false;
		}
	}

	function getUserInfoFromUserTable($customerId, $userId)
	{
		$tableName = $customerId . "_user";
		$this->db->where("barcode", $userId);
		$this->db->from($tableName);
		$count = $this->db->count_all_results();
		if ($count > 0) {
			$this->db->where("barcode", $userId);
			$query = $this->db->get($tableName);
			$res = $query->result();
			return $res;
		} else {
			return false;
		}
	}

	public function loginWithCustomerId($customerId)
	{
		if (!$this->db->table_exists($customerId . '_info')) {
			return array(
				"status" => false,
				"message" => "There's no customerId table",
				"data" => null
			);
		}

		$query = $this->db->get($customerId . '_info');
		$res = $query->result();
		// result;
		return array(
			"status" => true,
			"message" => "Success",
			"data" => $res[0]
		);
	}


	public function getAppSetting($factory_id, $machine_id)
	{
		$this->db->select('*');
		$this->db->from('tbl_app_setting');
		$this->db->where('factory_id', $factory_id);
		$this->db->where('machine_id', $machine_id);
		$res = $this->db->get()->result_array();
		if (count($res) == 0) {
			return array("status" => false, "message" => "no data", "data" => "");
		}
		$settings = $res[0];

		$this->db->select('*');
		$this->db->from('tbl_app_setting_calc');
		$this->db->where('id', $settings['calc_chart_formula']);
		$formula = $this->db->get()->row_array();
		if ($formula) {
			$settings["formula_name"] = $formula["name"];
			$settings["formula"] = $formula["formula"];
		} else {
			$settings["formula_name"] = "";
			$settings["formula"] = "";
		}

		return array(
			"status" => true,
			"message" => "Success",
			"data" => $settings
		);
	}

	public function getAppSettingFormulas()
	{
		$this->db->select('*');
		$this->db->from('tbl_app_setting_calc');
		$res = $this->db->get()->result_array();
		return array(
			"status" => true,
			"message" => "Success",
			"data" => $res
		);
	}

	public function getMachineCalculation($factory_id, $machine_id, $operator, $jobId, $date)
	{
		$this->db->select('*');
		$this->db->from('tbl_app_setting');
		$this->db->where('factory_id', $factory_id);
		$this->db->where('machine_id', $machine_id);
		$res = $this->db->get()->result_array();
		if (count($res) == 0) {
			return array("status" => false, "message" => "no data1", "data" => "");
		}
		$settings = $res[0];

		$this->db->select('*');
		$this->db->from('tbl_app_setting_calc');
		$this->db->where('id', $settings['calc_chart_formula']);
		$formula = $this->db->get()->row_array();
		if ($formula) {
			$sql = "SELECT " . $formula["formula"] . " FROM " . $factory_id . "_shifts" . " WHERE `machine` = '"
				. $machine_id . "' AND  substring(date, 1, 10) = '" . $date . "'";

			if ($settings["calc_chart_option"] == "1") {
				$sql = $sql . " AND " . "`operator` = '" . $operator . "'";
			}

			if ($settings["calc_chart_option"] == "2") {
				$sql = $sql . " AND " . "`jobID` = '" . $jobId . "'";
			}

			$query = $this->db->query($sql);
			$calc = $query->row_array();
			if ($calc) {
				$key = array_keys($calc)[0];
				return array(
					"status" => true,
					"message" => "Success",
					"data" => $calc[$key] == null ? "0" : $calc[$key]
				);
			} else {
				return array("status" => false, "message" => "no data2", "data" => "");
			}
		} else {
			return array("status" => false, "message" => "no data3", "data" => "");
		}
	}


	public function updateAppSetting($factory_id, $machine_id, $details)
	{
		$tableName = "tbl_app_setting";
		$this->db->select('*');
		$this->db->from($tableName);
		$this->db->where('factory_id', $factory_id);
		$this->db->where('machine_id', $machine_id);
		$res = $this->db->get()->result_array();
		if (count($res) == 0) {
			$this->db->where('factory_id', $factory_id);
			$this->db->where('machine_id', $machine_id);
			$this->db->delete($tableName);
			$this->db->insert($tableName, $details);
		} else {
			$this->db->where('factory_id', $factory_id);
			$this->db->where('machine_id', $machine_id);
			$this->db->update($tableName, $details);
		}
		return array(
			"status" => true,
			"message" => "Success",
			"data" => ""
		);
	}

	public function checkValidStatus($userId, $deviceId)
	{
		// read info table
		$this->db->select("valid");
		$query = $this->db->get_where('tbl_devices', array('userId' => $userId, 'deviceId' => $deviceId));
		$res = $query->result();
		if (count($res) > 0) {
			$res = $res[0];
			if ($res->valid == '0') {
				return array(
					"status" => false,
					"message" => "This device isn't allowed",
					"data" => null
				);
			} else {
				return array(
					"status" => true,
					"message" => "Success",
					"data" => $res
				);
			}
		} else {
			return array(
				"status" => false,
				"message" => "No data.",
				"data" => null
			);
		}
	}



	public function setMachineStatus($customerId, $machineId, $status)
	{
		$tableName = $customerId . "_status";
		$this->db->set('status', $status);
		$this->db->where('machine_id', $machineId);
		$this->db->update($tableName);

		$res = array();
		$res['status'] = true;
		$res['data'] = $tableName;
		$res["message"] = "Success";
		return $res;
	}
	public function assignMachineToUser($userName, $accountID, $machineName, $appVersion)
	{
		$tableName = $accountID . "_status";
		$this->db->set('Operator', $userName);
		$this->db->set('app_version', $appVersion);
		$this->db->where('machine_id', $machineName);
		$this->db->update($tableName);

		$res = array();
		$res['status'] = true;
		$res['data'] = $tableName;
		$res["message"] = "Success";
		return $res;
	}

	public function getJobData($customerId, $jobId)
	{
		$tableName = $customerId . "_jobdata";

		if (!$this->db->table_exists($tableName)) {
			return array(
				"status" => false,
				"message" => "There's no customerId table",
				"data" => null,
				"response" => $tableName . ", " . $customerId . ", " . $jobId
			);
		}

		$this->db->where('jobID', $jobId);
		$this->db->from($tableName);
		$query = $this->db->get();
		$data = $query->result();

		if (count($data) == 0) {
			return array(
				"status" => false,
				"message" => "There's no data matched by jobId",
				"data" => null,
			);
		}
		$res = array();
		$res['status'] = true;
		$res['data'] = $data;
		$res["message"] = "Success";
		return $res;
	}

	public function getJobDataByPrOrderNo($customerId, $jobId)
	{
		$tableName = $customerId . "_jobdata";

		if (!$this->db->table_exists($tableName)) {
			return array(
				"status" => false,
				"message" => "There's no customerId table",
				"data" => null,
				"response" => $tableName . ", " . $customerId . ", " . $jobId
			);
		}

		$this->db->like('jobID', $jobId);
		$this->db->from($tableName);
		$this->db->limit(10, 0);
		$query = $this->db->get();
		$data = $query->result();
		if (count($data) == 0) {
			return array(
				"status" => false,
				"message" => "There's no data matched by jobId",
				"data" => null,
			);
		}
		$res = array();
		$res['status'] = true;
		$res['data'] = $data;
		$res["message"] = "Success";
		return $res;
	}

	//Post Jop Data
	public function postJobData($customer_id, $data = array())
	{
		if ($data) {
			$insert = $this->db->insert($customer_id . '_jobdata', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	//Update JobData
	public function updateJobData($customer_id, $jobID,  $data)
	{
		if ($data) {
			$this->db->where('jobID', $jobID);
			$this->db->update($customer_id . '_jobdata', $data);
			return true;
		}
	}

	public function get_table($table, $where)
	{
		if ($where != null && $where != "")
			$this->db->where($where);

		// $table = $customerId . "_" . $type;
		$tableName = $table;

		// file_put_contents("test.log", $tableName );
		file_put_contents("test.log", $tableName);
		// $tableName = "strohwig_info";
		$res = array();

		// $query = $this->db->get($tableName);

		$query = $this->db->get($tableName);


		$res['status'] = true;
		$res['table'] = $table;
		$res['con'] = $table == "sm_ks_info";
		// $res['data'] = $query->result();
		$res["message"] = "There's no data matched by jobId";
		return $res;
	}

	public function getAllMaintenanceInfo($customerId, $machineId)
	{
		try {
			$tableName = $customerId . "_maintenance";
			$this->db->where('machine_id', $machineId);
			$this->db->where('is_finished !=', 1);
			$query = $this->db->get($tableName);

			$res = array();
			$res['status'] = true;
			$res['data'] = $query->result();
			$res["message"] = "";
			return $res;
		} catch (Exception $err) {
			$res = array();
			$res['status'] = false;
			$res['data'] = '';
			$res["message"] = "Something went wrong.";
			return $res;
		}
	}
	public function enterInMaintenance($customerId, $machineId, $taskId, $start)
	{
		$tableName = $customerId . "_maintenance";
		$this->db->where('machine_id', $machineId);
		$this->db->where('id', $taskId);
		$this->db->set('is_finished', 0);
		$this->db->set('start', $start);
		$this->db->update($tableName);
		$res = array();
		$res['status'] = true;
		$res['data'] = '';
		$res["message"] = "";
		return $res;
	}

	public function completeMaintenanceTask($customerId, $machineId, $taskId, $completedTime, $note, $startTime)
	{
		// get current situation
		$tableName = $customerId . "_maintenance";
		$this->db->where('machine_id', $machineId);
		$this->db->where('id', $taskId);
		$query = $this->db->get($tableName);

		$orgData = $query->result();
		$orgStart = $startTime;
		if (count($orgData) > 0) {
			$orgData = $orgData[0];
			$orgStart = $orgData->start;
		}


		// update
		$tableName = $customerId . "_maintenance";
		$this->db->where('machine_id', $machineId);
		$this->db->where('id', $taskId);
		$this->db->set('is_finished', 1);
		$this->db->set('end', $completedTime);
		$this->db->set('note', $note);
		$this->db->set('start', $startTime);
		$this->db->update($tableName);
		$res = array();
		$res['status'] = true;
		$res['data'] = '';
		$res["message"] = "";

		// add to history
		if ($orgStart != $startTime) {
			$tableName = $customerId . "_maintenance_history";
			$data = array(
				'machine_id' => $machineId,
				'maintenance_id' => $taskId,
				'start' => $orgStart,
				'end' => $startTime,
				'note' => $note
			);
			$this->db->insert($tableName, $data);
		}
		return $res;
	}


	public function getSensors($customerId)
	{
		$query = $this->db->get_where('tbl_sensors', array('customer_id' => $customerId));
		return $query->result_array();
	}



	public function getSensorDetail($sensorId, $currentTime, $activeSeconds)
	{
		date_default_timezone_set("America/Los_Angeles");
		$sql = "SELECT * FROM  SensorData WHERE sensor_id = $sensorId AND ($currentTime - created_at) < $activeSeconds ORDER BY created_at desc limit 1";
		$query = $this->db->query($sql);
		return $query->result_array();
	}

	public function postTankTime($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('tanktimes', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateTank($tankId, $data = array())
	{
		if ($data) {
			$this->db->where('id', $tankId);
			$this->db->update('tanktimes', $data);
			return true;
		}
	}

	public function getTankTime($id)
	{
		$sql = "SELECT * FROM tanktimes WHERE id = ?";
		$query = $this->db->query($sql, array($id));
		return $query->row_array();
	}

	public function getTanks($customerId, $part_id)
	{
		$query = $this->db->get_where('tanktimes', array('customer_id' => $customerId, 'part_id' => $part_id));
		return $query->result_array();
	}

	public function get_shift_detail($id)
	{
		$sql = "SELECT shift1_start, shift1_end, shift2_start, shift2_end, shift3_start, shift3_end  FROM " . $id . "_info";
		$query = $this->db->query($sql);
		return $query->row_array();
	}

	public function update_shift_detail($id, $data)
	{
		if ($data) {
			$this->db->update($id . "_info", $data);
			return true;
		}
	}

	public function getShifts($customer_id, $id)
	{
		$sql = "SELECT * FROM " . $customer_id . "_shifts WHERE id = ?";
		$query = $this->db->query($sql, array($id));
		return $query->result_array();
	}

	public function postShiftData($customer_id, $data)
	{
		if ($data) {
			$insert = $this->db->insert($customer_id . '_shifts', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateShiftData($customer_id, $shift_id,  $data)
	{
		if ($data) {
			$this->db->where('id', $shift_id);
			$this->db->update($customer_id . '_shifts', $data);
			return true;
		}
	}

	public function postUpdateOldShiftInfo($customer_id, $machine_prefix, $part_id)
	{
		$this->db->set('goodParts', 'goodParts - 1', FALSE);
		$this->db->set('badParts', 'badParts + 1', FALSE);
		$this->db->where("machine LIKE '%$machine_prefix%'");
		$this->db->where("CONCAT(',', part_id, ',') LIKE '%,$part_id,%'");
		$this->db->update($customer_id . '_shifts');
		return true;
	}

	public function postTimeSavings($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('time_savings', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function getTimeSavings($id)
	{
		$sql = "SELECT * FROM time_savings WHERE id = ?";
		$query = $this->db->query($sql, array($id));
		return $query->row_array();
	}

	public function postAlarms($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('alarms', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function getAlarm($id)
	{
		$sql = "SELECT * FROM alarms WHERE id = ?";
		$query = $this->db->query($sql, array($id));
		return $query->row_array();
	}

	//BlastStation

	public function postBlastStation($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('blast_station', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateBlastStation($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('blast_station', $data);
			return true;
		}
	}

	public function getBlastStation($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM blast_station WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM blast_station WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}

	//CleaningStation

	public function postCleaningStation($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('cleaning_station', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateCleaningStation($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('cleaning_station', $data);
			return true;
		}
	}


	public function getCleaningStation($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM cleaning_station WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM cleaning_station WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}

	//PaintStation

	public function postPaintStation($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('paint_station', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updatePaintStation($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('paint_station', $data);
			return true;
		}
	}


	public function getPaintStation($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM paint_station WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM paint_station WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}
	// Assembly1Station

	public function postAssembly1Station($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('assembly_station1', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateAssembly1Station($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('assembly_station1', $data);
			return true;
		}
	}


	public function getAssembly1Station($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM assembly_station1 WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM assembly_station1 WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}

	//Phos_test_data
	public function postPhos_test_data($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('phos_test_data', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}


	public function getPhos_test_data($id)
	{
		$sql = "SELECT * FROM phos_test_data WHERE id = ?";
		$query = $this->db->query($sql, array($id));
		return $query->row_array();
	}


	// Blu136Assembly

	public function postBlu136Assembly($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('blu136_assembly', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateBlu136Assembly($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('blu136_assembly', $data);
			return true;
		}
	}

	public function getBlu136Assembly($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM blu136_assembly WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM blu136_assembly WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}


	// Assembly3Station
	public function postAssembly3Station($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('assembly_station3', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateAssembly3Station($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('assembly_station3', $data);
			return true;
		}
	}


	public function getAssembly3Station($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM assembly_station3 WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM assembly_station3 WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}

	// Quality Station
	public function postQualityStation($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('quality_station', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function updateQualityStation($part_id, $customer_id = null,  $data)
	{
		if ($data) {
			$this->db->where('part_id', $part_id);
			if ($customer_id) {
				$this->db->where('customer_id', $customer_id);
			}
			$this->db->update('quality_station', $data);
			return true;
		}
	}


	public function getQualityStation($part_id, $customer_id = null)
	{
		$params = array();
		$params[] = $part_id;
		if ($customer_id) {
			$params[] = $customer_id;
			$sql = "SELECT * FROM quality_station WHERE part_id = ? AND customer_id = ?";
		} else {
			$sql = "SELECT * FROM quality_station WHERE part_id = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}


	// GanttData
	public function getBufferGanttData($customer_id = null, $machine_id = null)
	{
		$tableName = "buffer_ganttdata";
		$this->db->select("*");
		$this->db->from($tableName);
		if ($customer_id != null && $machine_id != null) {
			$this->db->where('customer_id', $customer_id);
			$this->db->where('machine_id', $machine_id);
		}
		$query = $this->db->get();
		return $query->result_array();
	}

	public function getGanttData($customer_id, $machine_id, $Operator, $start, $end, $status)
	{
		$tableName = $customer_id . "_ganttdata";
		$this->db->select("*");
		$this->db->where('machine_id', $machine_id);
		$this->db->where('Operator', $Operator);
		$this->db->where('start <=', $start);
		$this->db->where('end >=', $end);
		$this->db->where('status', $status);
		$this->db->from($tableName);
		$query = $this->db->get();
		return $query->result_array();
	}

	public function getLastGanttData($customer_id, $machine_id)
	{
		$tableName = $customer_id . "_ganttdata";
		$this->db->select("*");
		$this->db->where('machine_id', $machine_id);
		$this->db->order_by('start', 'desc');
		$this->db->limit(1, 0);
		$this->db->from($tableName);
		$query = $this->db->get();
		return $query->result_array();
	}


	public function getGanttDataMobile($customerId, $machineId, $date)
	{
		$tableName = $customerId . "_ganttdata";

		if (!$this->db->table_exists($tableName)) {
			return array(
				"status" => false,
				"message" => "There's no customerId table",
				"data" => null,
				"response" => $tableName . ", " . $machineId . ", " . $date
			);
		}

		$this->db->select("status, color, start, end");
		$this->db->where('created_at like', $date . '%');
		$this->db->where('machine_id', $machineId);
		$this->db->from($tableName);
		$query = $this->db->get();
		$data = $query->result();
		$res = array();
		$res['status'] = true;
		$res['data'] = $data;
		$res['response'] = $customerId . ", " . $machineId . ", " . $date;
		return $res;
	}

	public function postGanttData($customer_id, $data = array())
	{
		if ($data) {
			$insert = $this->db->insert($customer_id . '_ganttdata', $data);
			if ($insert == true)
				return true;
			else
				return false;
		}
	}

	public function updateGanttData($customer_id,  $start, $end, $machine_id,  $data)
	{
		if ($data) {
			$this->db->where('machine_id', $machine_id);
			$this->db->where('start >=', $start);
			$this->db->where('end <=', $end);
			$this->db->where('status !=', "In Cycle");
			$this->db->update($customer_id . '_ganttdata', $data);
			return true;
		}
	}

	public function updateGanttDataEnd($customer_id, $id, $data)
	{
		if ($data) {
			$this->db->where('id', $id);
			$this->db->update($customer_id . '_ganttdata', $data);
			return true;
		}
	}

	public function updateBufferGanttData($customer_id, $machine_id, $data)
	{
		if ($data) {
			$this->db->where('customer_id', $customer_id);
			$this->db->where('machine_id', $machine_id);
			$this->db->update('buffer_ganttdata', $data);
			return true;
		}
	}

	//HST
	public function getHstData($customer_id, $date, $machine_id = null)
	{
		$params = array();
		$params[] = $date;
		if ($machine_id) {
			$params[] = $machine_id;
			$sql = "SELECT * FROM " . $customer_id . "_hst WHERE date = ? AND machine_id = ?";
		} else {
			$sql = "SELECT * FROM " . $customer_id . "_hst WHERE date = ?";
		}
		$query = $this->db->query($sql, $params);
		return $query->result_array();
	}


	public function updateHstData($customer_id, $date, $machine_id,  $data)
	{
		if ($data) {
			$this->db->where('machine_id', $machine_id);
			$this->db->where('date', $date);
			$this->db->update($customer_id . '_hst', $data);
			return true;
		}
	}

	public function postHstData($customer_id, $data = array())
	{
		if ($data) {
			$insert = $this->db->insert($customer_id . '_hst', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function getCurrentPartsCount($customer_id,  $operator, $machine, $date,  $jobID)
	{
		$sql = "SELECT SUM(goodParts) as goodParts, SUM(badParts) as badParts FROM " .  $customer_id . "_shifts  WHERE operator = ? AND machine = ? AND jobID = ? AND substring(date, 1, 10) = ? GROUP BY substring(date, 1, 10), machine, jobID, operator";
		$query = $this->db->query($sql, array($operator, $machine, $jobID, $date));
		return $query->row_array();
	}

	// Stage
	public function postStage($data = array())
	{
		if ($data) {
			$insert = $this->db->insert("stage", $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	// postHennigData
	public function postHennigData($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('hennig_data', $data);
			return $insert;
		}
	}

	// postHennigData
	public function getLastHennigData($sensor_id)
	{
		$sql = "SELECT * FROM hennig_data  WHERE sensor_id = ? ORDER BY reading_time DESC LIMIT 1";
		$query = $this->db->query($sql, array($sensor_id));
		$res = $query->result_array();
		if ($res && count($res) > 0) {
			return $res[0];
		} else {
			return false;
		}
	}

	// postIotData
	public function postIotData($customer_id, $data = array())
	{
		if ($data) {
			$insert = $this->db->insert($customer_id . '_iotdata', $data);
			return $insert;
		}
	}

	public function getLastIotData($customer_id, $sensor_id)
	{
		$tableName = $customer_id . "_iotdata";
		$sql = "SELECT * FROM $tableName  WHERE sensor_id = ? ORDER BY reading_time DESC LIMIT 1";
		$query = $this->db->query($sql, array($sensor_id));
		$res = $query->result_array();
		if ($res && count($res) > 0) {
			return $res[0];
		} else {
			return false;
		}
	}

	// CycleAlert
	public function addCycleAlert($data = array())
	{
		if ($data) {
			$insert = $this->db->insert("cycle_alert_list", $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function getCycleAlert($customer_id, $machine_id)
	{
		$tableName = "cycle_alert_list";
		$sql = "SELECT * FROM $tableName  WHERE customer_id = ? AND machine_id = ? ORDER BY time_stamp DESC LIMIT 1";
		$query = $this->db->query($sql, array($customer_id, $machine_id));
		$res = $query->result_array();
		if ($res && count($res) > 0) {
			return $res[0];
		} else {
			return false;
		}
	}

	public function getCycleAlerts()
	{
		$tableName = "cycle_alert_list";
		$sql = "SELECT * FROM $tableName ORDER BY time_stamp ASC LIMIT 1";
		$query = $this->db->query($sql);
		$res = $query->result_array();
		if ($res && count($res) > 0) {
			return $res[0];
		} else {
			return false;
		}
	}

	public function removeCycleAlert($customer_id, $machine_id)
	{
		$this->db->where('customer_id', $customer_id);
		$this->db->where('machine_id', $machine_id);
		$this->db->delete('cycle_alert_list');
		return true;
	}
}
