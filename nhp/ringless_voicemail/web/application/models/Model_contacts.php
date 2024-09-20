<?php

class Model_contacts extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function get($id = null, $user =null, $perpage = -1, $startpoint = -1)
	{
		$this->db->select('*');
		if ($id) {
			$this->db->where('id', $id);
			return $this->db->get('contacts')->row_array();
		}
		if ($user) {
			$this->db->where('user', $user);
		}
		$this->db->order_by('id', 'desc');
		if ($perpage > -1 && $startpoint > -1) {
			$this->db->limit($perpage, $startpoint);
		}
		return $this->db->get('contacts')->result_array();
	}

	public function get_farm_contacts($farm_id)
	{
		$this->db->select('*');
		$this->db->where('farm_id', $farm_id);
		$this->db->from('contacts');
		return $this->db->get()->result_array();
	}

	public function create($data = array())
	{
		if ($data) {
			$insert = $this->db->insert('contacts', $data);
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
			$update = $this->db->update('contacts', $data);
			return ($update == true) ? true : false;
		}
	}


	public function delete($id = null, $user = null)
	{
		if ($id){
			$this->db->where('id', $id);
		}
		if ($user){
			$this->db->where('user', $user);
		}
		$delete = $this->db->delete('contacts');
		return ($delete == true) ? true : false;
	}
}
