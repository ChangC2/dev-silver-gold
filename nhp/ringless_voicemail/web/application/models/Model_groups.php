<?php 

class Model_groups extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function getGroupData($groupId = null) 
	{
		if($groupId) {
			$sql = "SELECT * FROM groups WHERE id = ?";
			$query = $this->db->query($sql, array($groupId));
			return $query->row_array();
		}

		$sql = "SELECT * FROM groups WHERE id != ? ORDER BY id DESC";
		$query = $this->db->query($sql, array(1));
		return $query->result_array();
	}

	public function create($data = '')
	{
		$create = $this->db->insert('groups', $data);
		return ($create == true) ? true : false;
	}

	public function edit($data, $id)
	{
		$this->db->where('id', $id);
		$update = $this->db->update('groups', $data);
		return ($update == true) ? true : false;	
	}

	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete = $this->db->delete('groups');
		return ($delete == true) ? true : false;
	}

	public function existInAccountGroup($id)
	{
		$sql = "SELECT * FROM account_group WHERE group_id = ?";
		$query = $this->db->query($sql, array($id));
		//echo $this->db->last_query();
		return ($query->num_rows() == 1) ? true : false;
	}

	public function getAccountGroupByAccountId($account_id) 
	{
		$sql = "SELECT * FROM account_group 
		INNER JOIN groups ON groups.id = account_group.group_id 
		WHERE account_group.account_id = ?";
		$query = $this->db->query($sql, array($account_id));
		$result = $query->row_array();
		return $result;

	}
}