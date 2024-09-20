<?php

class Model_signs extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function get($user)
	{
		$sql = "SELECT * FROM signs WHERE user_id = ?";
		$query = $this->db->query($sql, array($user));
		return $query->row_array();
	}

	public function save($data = array())
	{

		$sql = "SELECT * FROM signs WHERE user_id = ?";
		$query = $this->db->query($sql, array($data["user_id"]));
		$rec = $query->result_array();


		if (count($rec)>0){
			$this->db->where('user_id', $data["user_id"]);
			return $this->db->update('signs', $data);
		}else{
			return $this->db->insert('signs', $data);
		}
	}

	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('signs');
		return ($delete == true) ? true : false;
	}
}
