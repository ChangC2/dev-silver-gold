<?php

class Model_orders extends CI_Model
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
			$this->db->from('orders');
			return $this->db->get()->row_array();
		}
		if ($user && $user != "1") {
			$this->db->where('user_id', $user);
		}
		$this->db->order_by('id', 'desc');
		$this->db->from('orders');
		$this->db->limit(300, 0);
		return $this->db->get()->result_array();
	}

	public function get_pendings($user = null, $start_date = null, $end_date = null, $search_query = null)
	{
		$this->db->select('*');
		$this->db->where('status', 0);
		$this->db->where('send_time', "");
		if ($user) {
			$this->db->where('user_id', $user);
		}
		if ($start_date) {
			$this->db->where('created_at >=', $start_date . " 00:00:00");
		}
		if ($end_date) {
			$this->db->where('created_at <=', $end_date . " 23:59:59");
		}
		if ($search_query) {
			$this->db->group_start();
			$this->db->like('user_email', $search_query);
			$this->db->or_like('record_name', $search_query);
			$this->db->or_like('user_name', $search_query);
			$this->db->group_end();
		}
		$this->db->order_by('created_at', 'desc');
		$this->db->from('orders');
		$this->db->limit(300, 0);
		return $this->db->get()->result_array();
	}

	public function get_scheduled($user = null, $start_date = null, $end_date = null, $search_query = null)
	{
		$this->db->select('*');
		$this->db->where('status', 0);
		$this->db->where('send_time<>', "");
		if ($user) {
			$this->db->where('user_id', $user);
		}
		if ($start_date) {
			$this->db->where('created_at >=', $start_date . " 00:00:00");
		}
		if ($end_date) {
			$this->db->where('created_at <=', $end_date . " 23:59:59");
		}
		if ($search_query) {
			$this->db->group_start();
			$this->db->like('user_email', $search_query);
			$this->db->or_like('record_name', $search_query);
			$this->db->or_like('user_name', $search_query);
			$this->db->group_end();
		}
		$this->db->order_by('send_time', 'desc');
		$this->db->from('orders');
		$this->db->limit(300, 0);
		return $this->db->get()->result_array();
	}

	public function get_delivered($user = null, $start_date = null, $end_date = null, $search_query = null)
	{
		$this->db->select('*');
		$this->db->where('status', 1);
		if ($user) {
			$this->db->where('user_id', $user);
		}
		if ($start_date) {
			$this->db->where('created_at >=', $start_date . " 00:00:00");
		}
		if ($end_date) {
			$this->db->where('created_at <=', $end_date . " 23:59:59");
		}
		if ($search_query) {
			$this->db->group_start();
			$this->db->like('user_email', $search_query);
			$this->db->or_like('record_name', $search_query);
			$this->db->or_like('user_name', $search_query);
			$this->db->group_end();
		}
		$this->db->order_by('created_at', 'desc');
		$this->db->from('orders');
		$this->db->limit(300, 0);
		return $this->db->get()->result_array();
	}


	public function existedInOrder($user_id, $audioUrl)
	{
		$this->db->select('*');
		$this->db->where('user_id', $user_id);
		$this->db->where('url', $audioUrl);
		$this->db->where('created_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR)');
		$this->db->order_by('id', 'desc');
		$this->db->from('orders');
		$orders =  $this->db->get()->result_array();
		if ($orders != null && count($orders) > 0){
			return true;
		}else{
			return false;
		}
	}

	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('orders', $data);
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
			$update = $this->db->update('orders', $data);
			return ($update == true) ? true : false;
		}
	}


	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete1 = $this->db->delete('orders');

		$this->db->where('order_id', $id);
		$delete2 = $this->db->delete('mail_log');

		return ($delete1 == true && $delete2 == true) ? true : false;
	}
}
