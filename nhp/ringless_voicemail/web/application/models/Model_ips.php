<?php

class Model_ips extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function get($id = null)
	{
		if ($id) {
			$sql = "SELECT * FROM ips WHERE id = ?";
			$query = $this->db->query($sql, array($id));
			return $query->row_array();
		}

		$sql = "SELECT * FROM ips ORDER BY id DESC";
		$query = $this->db->query($sql);
		
		return $query->result_array();
	}

	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('ips', $data);
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
			$update = $this->db->update('ips', $data);
			return ($update == true) ? true : false;
		}
	}


	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('ips');
		return ($delete == true) ? true : false;
	}
}
