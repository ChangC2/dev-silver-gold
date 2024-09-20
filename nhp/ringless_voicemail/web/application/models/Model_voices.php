<?php

class Model_voices extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function get($id = null, $user = null, $status = null)
	{
		if ($id) {
			$this->db->select('*');
			$this->db->where('id', $id);
			$this->db->from('voices');
			return $this->db->get()->row_array();
		}
		$this->db->select('voices.*, accounts.mobile_number as forwarding_number');
		if ($user) {
			$this->db->where('user_id', $user); // For selected customer
		} else {
			$this->db->where('permission<>', 0); // For all customers
		}

		if ($status == 1) {
			$this->db->where('voice_id<>', "");
			$this->db->where('eden_id<>', "");
			$this->db->where('active', 1);
		}

		if ($status == 2) {
			$this->db->group_start();
			$this->db->where('voice_id=', "");
			$this->db->or_where('eden_id=', "");
			$this->db->or_where('active', 0);
			$this->db->group_end();
		}

		$this->db->order_by('id', 'desc');
		$this->db->from('voices');
		$this->db->join('accounts', 'accounts.id=voices.user_id');
		return $this->db->get()->result_array();
	}

	public function get_available_voices($user)
	{
		$sql = "SELECT * FROM voices WHERE user_id = ? AND active = 1 ORDER BY name ASC";
		$query = $this->db->query($sql, array($user));
		return $query->result_array();
	}

	public function get_pro_voices($is_all = false)
	{
		if ($is_all) {
			$sql = "SELECT * FROM voices WHERE permission = '0' ORDER BY name ASC";
			$query = $this->db->query($sql);
			return $query->result_array();
		} else {
			$sql = "SELECT * FROM voices WHERE permission = '0' AND  active = 1 ORDER BY name ASC";
			$query = $this->db->query($sql);
			return $query->result_array();
		}
	}


	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('voices', $data);
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
			$update = $this->db->update('voices', $data);
			return ($update == true) ? true : false;
		}
	}

	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('voices');
		return ($delete == true) ? true : false;
	}
}
