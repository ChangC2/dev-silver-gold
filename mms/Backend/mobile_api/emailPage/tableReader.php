<?php

class tableReader
{
	public function readTable($table_name, $whereString)
	{
		include_once("connection.php");
		$resData = array();
		$db = new dbObj();
		$conn =  $db->getConnstring();
		// Read ColumnNames
		$col_names = array();
		$query = mysqli_query($conn, "SHOW COLUMNS FROM `" . $table_name . "` ");

		if ($query) {
			while ($row = mysqli_fetch_array($query)) {
				$col_names[] = $row['0'];
			}
		}

		$sql = "SELECT * FROM `" . $table_name . "` " . $whereString;
		$query  = mysqli_query($conn, $sql);
		if ($query) {
			$num_column = mysqli_num_fields($query);
			$count_rows = mysqli_num_rows($query);
			for ($i = 0; $i < $count_rows; $i++) {
				$row = mysqli_fetch_row($query);
				$item = array();
				for ($j = 0; $j < $num_column; $j++) {
					$item[$col_names[$j]] = $row[$j];
				}
				$resData[] = $item;
			}
		}
		return $resData;
	}

	public function readInfoTables()
	{
		include_once("connection.php");
		$res = array();
		$db = new dbObj();
		$conn =  $db->getConnstring();

		$sql = "SHOW TABLES FROM " . DB_NAME;
		$query = mysqli_query($conn, $sql);
		if ($query) {
			while ($row = mysqli_fetch_array($query)) {
				if (strpos($row[0], '_info') != false) {
					$res[] = str_replace('_info', '', $row[0]);
				}
			}
		}
		return $res;
	}
}
