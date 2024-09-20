<?php

class Model_sms_log extends CI_Model
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
			$this->db->from('sms_log');
			return $this->db->get()->row_array();
		}
		if ($user) {
			$this->db->where('user_id', $user);
		}
		$this->db->order_by('id', 'desc');
		$this->db->from('sms_log');
		return $this->db->get()->result_array();
	}

	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('sms_log', $data);
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
			$update = $this->db->update('sms_log', $data);
			return ($update == true) ? true : false;
		}
	}


	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('sms_log');
		return ($delete == true) ? true : false;
	}
}
