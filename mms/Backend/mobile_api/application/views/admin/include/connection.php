<?php

define('HOSTNAME','localhost');
define('DB_USERNAME','root');
define('DB_PASSWORD','4}Uc*tN={=HxC5,e');
define('DB_NAME', 'cheil_trading');

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET, X-Requested-With, X-Auth-Token');

Class dbObj{

	var $conn;
	function getConnstring() {


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

?>
