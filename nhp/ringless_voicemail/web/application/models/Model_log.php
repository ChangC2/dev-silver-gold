<?php

class Model_log extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function get($id = null, $user = null)
	{
		$this->db->select('*');
		if ($id) {
			$this->db->where('id', $id);
			$this->db->from('mail_log');
			return $this->db->get()->row_array();
		}
		if ($user) {
			$this->db->where('user_id', $user);
		}
		$this->db->order_by('id', 'desc');
		$this->db->from('mail_log');
		return $this->db->get()->result_array();
	}

	public function get_for_order($order_id, $perpage = -1, $startpoint = -1)
	{
		$this->db->select('*');
		$this->db->where('order_id', $order_id);
		$this->db->order_by('id', 'desc');
		$this->db->from('mail_log');
		if ($perpage > -1 && $startpoint > -1) {
			$this->db->limit($perpage, $startpoint);
		}
		return $this->db->get()->result_array();
	}

	public function get_success($order_id)
	{
		$this->db->select('count(*) as count');
		$this->db->where('order_id', $order_id);
		$this->db->where('status', "success");
		$this->db->from('mail_log');
		$res =  $this->db->get()->row_array();
		if ($res){
			return $res["count"];
		}else{
			return 0;
		}
	}

	public function get_fail($order_id)
	{
		$this->db->select('count(*) as count');
		$this->db->where('order_id', $order_id);
		$this->db->where('status', "failure");
		$this->db->from('mail_log');
		$res =  $this->db->get()->row_array();
		if ($res) {
			return $res["count"];
		} else {
			return 0;
		}
	}

	public function resend_failed($order_id)
	{
		$this->db->where('order_id', $order_id);
		$this->db->where('status', "failure");
		$data = array(
			'status' => "Pending in Queue",
		);
		$update = $this->db->update('mail_log', $data);
		return ($update == true) ? true : false;
	}

	public function get_delivered_count($order_id)
	{
		$this->db->select('count(*) as count');
		$this->db->where('order_id', $order_id);
		$this->db->where('status !=', "Pending in Queue");
		$this->db->where('status !=', "Processing");
		$this->db->from('mail_log');
		$res =  $this->db->get()->row_array();
		if ($res) {
			return $res["count"];
		} else {
			return 0;
		}
	}

	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('mail_log', $data);
			if ($insert == true)
				return $this->db->insert_id();
			else
				return false;
		}
	}

	public function update($id = null, $data = array())
	{
		if ($id && $data) {
			$this->db->where('id', $id);
			$update = $this->db->update('mail_log', $data);
			return ($update == true) ? true : false;
		}
	}


	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('mail_log');
		return ($delete == true) ? true : false;
	}
}
