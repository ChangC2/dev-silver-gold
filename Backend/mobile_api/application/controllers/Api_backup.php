<?php
defined('BASEPATH') or exit('No direct script access allowed');
// header('Content-Type: text/html; charset=UTF-8');
// header('Access-Control-Allow-Origin: *');
// header('Access-Control-Allow-Credentials: true');
// header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
// header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
// header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');
class Api_backup extends CI_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this->load->model("admin_model");
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
        $userId = $this->input->post('userId');
        $deviceId = $this->input->post('deviceId');


        $res = $this->admin_model->loginWithUserId($userId, $deviceId);

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
            'operator' => $this->input->post('operator')
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
            "message" => "True"
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
                "message" => "True"
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

        $data = array(
            'jobId' => $this->input->post('jobId'),
            'sequenceNo' => $this->input->post('sequenceNo') !== null ? $this->input->post('sequenceNo') : "",
            'machine' => $this->input->post('machine'),
            'operator' => $this->input->post('operator'),
            'userid' => $this->input->post('userid'),
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

        if (count($this->admin_model->getShifts($customer_id, $shift_id)) > 0) {
            $this->admin_model->updateShiftData($customer_id, $shift_id, $data);
            $res = array(
                "status" => true,
                "message" => "True",
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
                    "message" => "True",
                    "shift_id" => $insert_id
                );
                echo json_encode($res);
            }
        }
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
                "message" => "True",
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
                "message" => "True",
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
                "message" => "True",
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
                "message" => "True",
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
                "message" => "True",
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
                "message" => "True",
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
                "message" => "True",
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

        $success = false;

        if ($action == 1) {
            $data = array(
                'status' => $status,
                'color' => $color,
                'comment' => $comment,
                'machine_id' => $machine_id,
            );
            $success = $this->admin_model->updateGanttData($customer_id,  $start, $end, $machine_id,  $data);
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
            $success = $this->admin_model->postGanttData($customer_id, $data);
        }

        $res = array(
            "status" => $success
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



    public function postStage1()
    {
        $data = array(
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'fa_mls' => $this->input->post('fa_mls') != null ? $this->input->post('fa_mls') : "",
            'conc' => $this->input->post('conc') != null ? $this->input->post('conc') : "",
            'ta_mls' => $this->input->post('ta_mls') != null ? $this->input->post('ta_mls') : "",
            'ta_fa' => $this->input->post('ta_fa') != null ? $this->input->post('ta_fa') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'temp' => $this->input->post('temp') != null ? $this->input->post('temp') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage1", $data);
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

    public function postStage2()
    {
        $data = array(
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'fa_mls' => $this->input->post('fa_mls') != null ? $this->input->post('fa_mls') : "",
            'conc' => $this->input->post('conc') != null ? $this->input->post('conc') : "",
            'ta_mls' => $this->input->post('ta_mls') != null ? $this->input->post('ta_mls') : "",
            'ta_fa' => $this->input->post('ta_fa') != null ? $this->input->post('ta_fa') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'temp' => $this->input->post('temp') != null ? $this->input->post('temp') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage2", $data);
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

    public function postStage3()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'ph' => $this->input->post('ph') != null ? $this->input->post('ph') : "",
            'total_alk' => $this->input->post('total_alk') != null ? $this->input->post('total_alk') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage3", $data);
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

    public function postStage4()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'ph' => $this->input->post('ph') != null ? $this->input->post('ph') : "",
            'temp' => $this->input->post('temp') != null ? $this->input->post('temp') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage4", $data);
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

    public function postStage5()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'total_acid' => $this->input->post('total_acid') != null ? $this->input->post('total_acid') : "",
            'free_acid' => $this->input->post('free_acid') != null ? $this->input->post('free_acid') : "",
            'gas_points' => $this->input->post('gas_points') != null ? $this->input->post('gas_points') : "",
            'zinc' => $this->input->post('zinc') != null ? $this->input->post('zinc') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'temp' => $this->input->post('temp') != null ? $this->input->post('temp') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage5", $data);
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


    public function postStage6()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'ph' => $this->input->post('ph') != null ? $this->input->post('ph') : "",
            'total_acid' => $this->input->post('total_acid') != null ? $this->input->post('total_acid') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage6", $data);
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

    public function postStage7()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'ph' => $this->input->post('ph') != null ? $this->input->post('ph') : "",
            'absorbance' => $this->input->post('absorbance') != null ? $this->input->post('absorbance') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage7", $data);
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

    public function postStage8()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'conductivity' => $this->input->post('conductivity') != null ? $this->input->post('conductivity') : "",
            'ph' => $this->input->post('ph') != null ? $this->input->post('ph') : "",
            'temp' => $this->input->post('temp') != null ? $this->input->post('temp') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage8", $data);
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

    public function postStage9()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'temp' => $this->input->post('temp') != null ? $this->input->post('temp') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postStage("stage9", $data);
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


    public function postPlanDataLog()
    {
        $data = array(
            'datetime' => $this->input->post('datetime') != null ? $this->input->post('datetime') : "",
            'date' => $this->input->post('date') != null ? $this->input->post('date') : "",
            'time' => $this->input->post('time') != null ? $this->input->post('time') : "",
            'customer_id' => $this->input->post('customer_id') != null ? $this->input->post('customer_id') : "",
            'machine_id' => $this->input->post('machine_id') != null ? $this->input->post('machine_id') : "",
            'operator' => $this->input->post('operator') != null ? $this->input->post('operator') : "",
            'timestamp' => $this->input->post('timestamp') != null ? $this->input->post('timestamp') : "",
            'break_pass_fail' => $this->input->post('break_pass_fail') != null ? $this->input->post('break_pass_fail') : "",
            'grade' => $this->input->post('grade') != null ? $this->input->post('grade') : "",
            'phose1' => $this->input->post('phose1') != null ? $this->input->post('phose1') : "",
            'phose2' => $this->input->post('phose2') != null ? $this->input->post('phose2') : "",
            'phose3' => $this->input->post('phose3') != null ? $this->input->post('phose3') : "",
            'striped1' => $this->input->post('striped1') != null ? $this->input->post('striped1') : "",
            'striped2' => $this->input->post('striped2') != null ? $this->input->post('striped2') : "",
            'striped3' => $this->input->post('striped3') != null ? $this->input->post('striped3') : "",
            'mg1' => $this->input->post('mg1') != null ? $this->input->post('mg1') : "",
            'mg2' => $this->input->post('mg2') != null ? $this->input->post('mg2') : "",
            'mg3' => $this->input->post('mg3') != null ? $this->input->post('mg3') : "",
            'average' => $this->input->post('average') != null ? $this->input->post('average') : "",
            'notes' => $this->input->post('notes') != null ? $this->input->post('notes') : "",
        );

        $insert_id =  $this->admin_model->postPlanDataLog($data);
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
