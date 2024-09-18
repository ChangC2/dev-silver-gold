<?php

define('HOSTNAME', 'localhost');
define('DB_USERNAME', 'root');
// define('DB_PASSWORD','k..8A4!UT:N~2X/J');
// define('DB_NAME', 'CloudBoost_MMS');

define('DB_PASSWORD', '');
define('DB_NAME', 'mms');

class dbObj
{
	var $conn;
	function getConnstring()
	{
		$con = mysqli_connect(HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_NAME) or die("Connection failed: " . mysqli_connect_error());
		mysqli_set_charset($con, "utf8");
		/* check connection */
		if (mysqli_connect_errno()) {
			printf("Connect failed: %s\n", mysqli_connect_error());
			exit();
		} else {
			$this->conn = $con;
		}
		return $this->conn;
	}
}
