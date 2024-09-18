<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Email extends CI_Controller {

	public function __construct(){
		parent::__construct();
        $this->load->model("admin_model");        
	}
    
    public function getRequestParam()
    {
        $req = array();
        $params = trim(file_get_contents("php://input"));
        $req = json_decode($params,true);
        return $req;
    }

    public function get_content(){
        $req = $this->getRequestParam();
            
        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->getStatus($customerID));
    }

    public function get_customer_detail()
    {
        $req = $this->getRequestParam();

        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->getInfo($customerID));
    }

    public function get_allHstData()
    {
        $req = $this->getRequestParam();

        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->get_allHstData($customerID));
    }

    public function get_machine_detail(){
        $req = $this->getRequestParam();

        if(!isset($this->session->userdata['logged_in'])) $this->signout();
        $machine_id = $req['id'];

        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->getMachineDetail($customerID, $machine_id)[0]);
    }

    public function get_machine_time(){
        $req = $this->getRequestParam();

        $machine_name = $req['machine_id'];
        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->get_machine_detail_data($customerID, $machine_name));
    }

    public function get_machine_detail_data_today()
    {
        $req = $this->getRequestParam();

        $machine_name = $req['machine_id'];
        $sel_date = isset($req['sel_date']) != '' ? $req['sel_date'] : 'now';
        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->get_machine_detail_data_today($customerID, $machine_name, $sel_date));
    }

    public function get_machine_detail_data(){
        $req = $this->getRequestParam();

        $machine_name = $req['machine_id'];
        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->get_machine_detail_data($customerID, $machine_name));
    }

    public function get_ganttData()
    {
        $req = $this->getRequestParam();

        $machine_name = $req['machine_id'];
        $sel_date = isset($req['sel_date']) != '' ? $req['sel_date'] : 'now';

        $customerID = $req['customerID'];
        echo json_encode($this->admin_model->get_ganttData($customerID, $machine_name, $sel_date));
    }
    public function get_timezone(){
        $req = $this->getRequestParam();

        $customerID = $req['customerID'];
        $info = $this->admin_model->getInfo($customerID);
        echo json_encode($info);
    }
}