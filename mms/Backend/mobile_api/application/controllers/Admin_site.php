<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Admin_site extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->model("admin_model");
        //$this->load->model("userauth_model");
    }


    public function login()
    {
        $response = array();
        $response['username'] = '';

        $this->session->unset_userdata('logged_in');
        $this->load->view('admin/login');    // header section
    }

    public function login_auth()
    {
        $req = array();
        $req['username'] = $this->input->post('username');
        $req['password'] = $this->input->post('password');

        $param['logins'] = $this->admin_model->login($req);
        print_r(json_encode($param['logins']));
        $response = array();
        if (count($param['logins']) > 0) {

            $response['username'] = $param['logins'][0]->username;
            $response['customer_id_list'] = array();
            $response['customer_id_list'] = explode(',', $param['logins'][0]->customer_id);
            $response['customer_id'] = $response['customer_id_list'][0];

            print_r($response['customer_id_list']);

            /*
            if(count($param['info']) > 0){
                $response['fullname'] = $param['info'][0]->name;
                $response['logo'] = $param['info'][0]->logo;
            }
            */

            $this->session->set_userdata('logged_in', $response);
            redirect(base_url() . "plant_page");
        } else {
            $this->signout();
            return;
        }
    }

    public function signout()
    {
        $response['username'] = "";
        $this->session->unset_userdata('logged_in');
        redirect(base_url() . "login");
    }

    public function plant_page($factoryNo = 0, $collapsed = 1)
    {
        if (!isset($this->session->userdata['logged_in'])) $this->signout();

        // Change Session Data
        $sessionData = array();
        $sessionData = $this->session->userdata['logged_in'];
        $sessionData['customer_id'] = $sessionData['customer_id_list'][$factoryNo];
        $this->session->set_userdata('logged_in', $sessionData);


        // Get user information.

        $customerID = $this->session->userdata['logged_in']['customer_id'];
        $param = array();
        $param['userdata'] = $this->session->userdata['logged_in'];

        $userInfo = $this->admin_model->getInfo($customerID);

        $param['userdata']['fullname'] = $userInfo[0]->name;
        $param['userdata']['logo'] = $userInfo[0]->logo;

        $param['userdata']['machines'] = array();

        // print_r($sessionData['customer_id_list']);
        foreach ($sessionData['customer_id_list'] as $custom) {
            // print_r($custom);
            $info = $this->admin_model->getInfo($custom);
            $param['userdata']['machines'][] = $info[0]->name;
        }

        $param['title'] = "Plant Dashboard";
        $param['collapsed'] = $collapsed;
        $this->load->view('admin/include/header', $param);     // header section        

        $data = array();
        $data['status_list'] = $this->admin_model->getStatus($customerID);
        // print_r(json_encode($data['status_list']));
        //$data['gantt_list'] = $this->userauth_model->getFile($customerID);
        $data['credential']['apiKey'] = "4adee18e-3f01-4ad0-a408-06c958375c38";
        $data['credential']['appId'] = "nrbgoloxzamv";
        $this->load->view('admin/contents/plantPage', $data);    // body section
        $this->load->view('admin/include/footer');     // footer section
    }

    public function machine_page($collapsed = 0)
    {
        if (!isset($this->session->userdata['logged_in'])) $this->signout();
        $param = array();
        $param['userdata'] = $this->session->userdata['logged_in'];
        $param['title'] = "Plant Dashboard";
        $param['collapsed'] = $collapsed;

        $param['title'] = "Machine Utilization";
        $this->load->view('admin/include/header', $param);     // header section

        $this->load->view('admin/contents/machinePage');    // body section
        $this->load->view('admin/include/footer');     // footer section
    }

    public function machineDetail($id)
    {
        if (!isset($this->session->userdata['logged_in'])) $this->signout();
        $machine_id = $id;

        $customerID = $this->session->userdata['logged_in']['customer_id'];
        $data = $this->admin_model->getMachineDetail($customerID, $machine_id)[0];

        $param = array();
        $param['title'] = $data->machine_id;
        $param['userdata'] = $this->session->userdata['logged_in'];
        $userInfo = $this->admin_model->getInfo($customerID);
        $param['userdata']['fullname'] = $userInfo[0]->name;
        $param['userdata']['logo'] = $userInfo[0]->logo;

        $param['status_data'] = $data;
        $param['machine_id'] = $machine_id;
        $this->load->view('machineDetail/index', $param);
    }

    public function ganttDetail($id, $sel_date = "")
    {
        if (!isset($this->session->userdata['logged_in'])) $this->signout();
        $machine_id = $id;

        $customerID = $this->session->userdata['logged_in']['customer_id'];

        $data = $this->admin_model->getMachineDetail($customerID, $machine_id)[0];
        $sel_date = str_replace("_", "/", $sel_date);
        $param = array();
        $param['title'] = $data->machine_id;
        $param['userdata'] = $this->session->userdata['logged_in'];
        $param['data'] = $data;
        $param['sel_date'] = $sel_date;
        $param['machine_id'] = $machine_id;

        $this->load->view('machineDetail/ganttDetail', $param);     // footer section
    }


    public function ganttDetail2($prefix, $machine_name, $sel_date = "")        // Gantt Detail without authentication.
    {

        $machine_name = str_replace("%20", " ", $machine_name);

        $data = $this->admin_model->getMachineDetail2($prefix, $machine_name)[0];

        $sel_date = str_replace("_", "/", $sel_date);
        $param = array();
        $param['title'] = $data->machine_id;
        $param['data'] = $data;
        $param['sel_date'] = $sel_date;
        $param['machine_name'] = $machine_name;
        $param['prefix'] = $prefix;
        //$param['ganttData'] = $this->admin_model->get_ganttData2($prefix, $machine_name, $sel_date);

        $this->load->view('machineDetail/ganttDetail2', $param);     // footer section
    }
    public function ganttDetail3()        // Gantt Detail without authentication.
    {
        $customerId = $this->input->get('customerId');
        $machineId = $this->input->get('machineId');
        $date = $this->input->get("date");
        

        $data = $this->admin_model->getMachineDetail2($customerId, $machineId)[0];

        $sel_date = str_replace("_", "/", $date);
        $param = array();
        $param['title'] = $data->machine_id;
        $param['data'] = $data;
        $param['sel_date'] = $sel_date;
        $param['machine_name'] = $machineId;
        $param['prefix'] = $customerId;

        $this->load->view('machineDetail/ganttDetail2', $param);     // footer section
    }

    public function emailPage($id)
    {
        if (!isset($this->session->userdata['logged_in'])) $this->signout();
        $machine_id = $id;

        $customerID = $this->session->userdata['logged_in']['customer_id'];
        $data = $this->admin_model->getMachineDetail($customerID, $machine_id)[0];
        //print_r($data);
        $param = array();
        $param['title'] = $data->machine_id;
        $param['userdata'] = $this->session->userdata['logged_in'];
        $param['status_data'] = $data;
        $param['machine_id'] = $machine_id;
        $this->load->view('machineDetail/emailPage', $param);
    }
}
