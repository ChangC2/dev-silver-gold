<?php

require FCPATH . 'vendor/autoload.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

class Model_common extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
	}

	//Validate Parameters
	function param_validation($paramarray, $data)
	{
		$NovalueParam = [];
		foreach ($paramarray as $val) {
			if (!array_key_exists($val, $data)) {
				$NovalueParam[] = $val;
			}
		}

		if (is_array($NovalueParam) && count($NovalueParam) > 0) {
			$returnArr['error'] = true;
			$returnArr['message'] = 'Sorry, that is not valid input. You missed ' . implode(',', $NovalueParam) . ' parameters';
			return $returnArr;
		} else {
			return false;
		}
	}

	/*=================================================================================
								Get Record	
==================================================================================*/

	function get_all($table_name, $field = '*')
	{
		$this->db->select($field);
		$this->db->from($table_name);
		return $this->db->get();
	}

	function get_record($table_name, $where, $field = '*')
	{
		$this->db->select($field);
		$this->db->from($table_name);

		if (is_array($where)) {
			foreach ($where as $key => $val) {
				$this->db->where($key, $val);
			}
		} else {
			$this->db->where($where);
		}

		return $this->db->get();
	}

	/*=================================================================================
								Execute Query
==================================================================================*/

	function execute_query($sql)
	{
		return $q = $this->db->query($sql);
	}

	/*=================================================================================
								Insert Record	
==================================================================================*/

	function insert_record($table_name, $insert_data)
	{
		$this->db->insert($table_name, $insert_data);
		return $this->db->insert_id();
	}

	/*=================================================================================
								Update Record	
==================================================================================*/

	function update_record($table_name, $where, $update_data)
	{
		if (is_array($where)) {
			foreach ($where as $key => $val) {
				$this->db->where($key, $val);
			}
		} else {
			$this->db->where($where);
		}
		return $this->db->update($table_name, $update_data);
	}

	/*=================================================================================
								Delete Record
==================================================================================*/

	function delete_record($table_name, $where)
	{
		if (is_array($where)) {
			foreach ($where as $key => $val) {
				$this->db->where($key, $val);
			}
		} else {
			$this->db->where($where);
		}

		return $this->db->delete($table_name);
	}
}
