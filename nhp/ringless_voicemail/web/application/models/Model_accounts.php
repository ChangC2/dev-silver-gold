<?php 

class Model_accounts extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	public function getAccountData($accountId = null) 
	{
		if($accountId) {
			$sql = "SELECT * FROM accounts WHERE id = ?";
			$query = $this->db->query($sql, array($accountId));
			return $query->row_array();
		}

		$sql = "SELECT * FROM accounts WHERE id != ? ORDER BY id DESC";
		$query = $this->db->query($sql, array(1));
		return $query->result_array();
	}

	public function getUsers()
	{
		$this->db->select('accounts.*, account_group.group_id');
		$this->db->where('group_id', 3);
		$this->db->order_by('id', 'desc');
		$this->db->from('accounts');
		$this->db->join('account_group', 'accounts.id=account_group.account_id');
		return $this->db->get()->result_array();
	}


	public function getAccountGroup($accountId = null) 
	{
		if($accountId) {
			$sql = "SELECT * FROM account_group WHERE account_id = ?";
			$query = $this->db->query($sql, array($accountId));
			$result = $query->row_array();

			$group_id = $result['group_id'];
			$g_sql = "SELECT * FROM groups WHERE id = ?";
			$g_query = $this->db->query($g_sql, array($group_id));
			$q_result = $g_query->row_array();
			return $q_result;
		}
	}

	public function create($data = '', $group_id = null)
	{
		if($data && $group_id) {
			$create = $this->db->insert('accounts', $data);
			$account_id = $this->db->insert_id();
			$group_data = array(
				'account_id' => $account_id,
				'group_id' => $group_id
			);
			$group_data = $this->db->insert('account_group', $group_data);
			return ($create == true && $group_data) ? $account_id : false;
		}
	}

	public function update($data = array(), $id = null, $group_id = null)
	{
		$this->db->where('id', $id);
		$update = $this->db->update('accounts', $data);

		if ($group_id) {
			$update_account_group = array('group_id' => $group_id);
			$this->db->where('account_id', $id);
			$account_group = $this->db->update('account_group', $update_account_group);
			return ($update == true && $account_group == true) ? true : false;
		}
		return ($update == true) ? true : false;
	}

	public function delete($id)
	{
		$this->db->where('id', $id);
		$delete1 = $this->db->delete('accounts');

		$this->db->where('account_id', $id);
		$delete2 = $this->db->delete('account_group');

		return ($delete1 == true && $delete2 == true) ? true : false;
	}

	public function countTotalAccounts()
	{
		$sql = "SELECT * FROM accounts WHERE id != ?";
		$query = $this->db->query($sql, array(1));
		return $query->num_rows();
	}

	public function check_email($email)
	{
		if ($email) {
			$sql = "SELECT * FROM accounts WHERE `email` = '$email'";
			$query = $this->db->query($sql);
			$result = $query->num_rows();
			return ($result > 0) ? true : false;
		}
		return false;
	}

	public function verify($user_id, $verification_code)
	{
		$sql = 'SELECT * FROM accounts WHERE id = ? AND verification_code = ?';
		$query = $this->db->query($sql, array($user_id, $verification_code));
		$result = $query->num_rows();
		if ($result == 1) {
			$data = array(
				'status' => 1,
				'verification_code' => ""
			);
			$this->db->where('id', $user_id);
			$this->db->update('accounts', $data);
			return true;
		} else {
			return false;
		}
	}

	/* 
		This function checks if the email and password matches with the database
	*/
	public function login($email, $password)
	{
		if ($email && $password) {
			$sql = "SELECT * FROM accounts WHERE email = ?";
			$query = $this->db->query($sql, array($email));

			if ($query->num_rows() == 1) {
				$result = $query->row_array();
				$hash_password = password_verify($password, $result['password']);
				if ($hash_password === true) {
					$sql1 = "SELECT * FROM account_group 
							INNER JOIN groups ON groups.id = account_group.group_id 
							WHERE account_group.account_id = ?";
					$query1 = $this->db->query($sql1, array($result['id']));
					$group_data = $query1->row_array();
					$result['permission'] = $group_data['permission'];
					return $result;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public function get_user_info($email = null, $id = null, $request_id = null)
	{
		if ($email) {
			$sql = "SELECT * FROM accounts WHERE email = ?";
			$query = $this->db->query($sql, array($email));
		} else if ($id) {
			$sql = "SELECT * FROM accounts WHERE id = ?";
			$query = $this->db->query($sql, array($id));
		} else if ($request_id) {
			$sql = "SELECT * FROM accounts WHERE request_id = ?";
			$query = $this->db->query($sql, array($request_id));
		} else {
			return false;
		}
		$result = $query->row_array();
		if ($result) {
			$sql1 = "SELECT * FROM account_group 
							INNER JOIN groups ON groups.id = account_group.group_id 
							WHERE account_group.account_id = ?";
			$query1 = $this->db->query($sql1, array($result['id']));
			$group_data = $query1->row_array();

			$result['permission'] = $group_data['permission'];
			return $result;
		} else {
			return false;
		}
	}

	public function get_ip_user($ip = null)
	{
		if ($ip) {
			$sql = "SELECT * FROM accounts WHERE ip_address = ?";
			$query = $this->db->query($sql, array($ip));
			return $query->row_array();
		}
	}
}