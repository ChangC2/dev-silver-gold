<?php

class Model_recordings extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function get($id = null, $user = null, $url =null)
	{
		if ($id) {
			$this->db->select('*');
			$this->db->where('id', $id);
			$this->db->from('recordings');
			return $this->db->get()->row_array();
		}
		$this->db->select('recordings.*, accounts.mobile_number as forwarding_number');
		if ($user) {
			$this->db->where('user_id', $user);
		}
		
		if ($url) {
			$this->db->where('url', $url);
		}

		$this->db->order_by('id', 'desc');
		$this->db->from('recordings');
		$this->db->join('accounts', 'accounts.id=recordings.user_id');
		return $this->db->get()->result_array();
	}

	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('recordings', $data);
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
			$update = $this->db->update('recordings', $data);
			return ($update == true) ? true : false;
		}
	}


	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('recordings');
		return ($delete == true) ? true : false;
	}
}
