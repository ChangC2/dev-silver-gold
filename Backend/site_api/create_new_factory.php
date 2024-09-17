<?php
include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

$table = new TableMgr();
$result_theme = new Result_theme();

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$logo = $params['logo'];
$serialKey = $params['serialKey'];
$factoryId = $params['factoryId'];
$factoryName = $params['factoryName'];
$userName = $params['userName'];
$userFullName = $params['userFullName'];
$password = $params['password'];
$password = md5($password);
$userTimezone = $params['timezone'];
$userTimezoneName = $params['timezoneName'];

$timestamp = time();
$created_at = date("Y-m-d h:i:s", $timestamp);

$tableName = "tbl_serialKeyStore";

// check serial key
$res = $table->ReadTable($tableName, "`serialKey`='$serialKey' AND `isValid`=1", "");

if (count($res) == 0 && $serialKey != "12345678") {
  echo $result_theme->MakeResult(false, "", "Serial Key is wrong.");
  exit;
}

// check factory id
$res = $table->ReadTable($tableName, "`factoryId`='$factoryId'", "");
if (count($res) > 0) {
  echo $result_theme->MakeResult(false, "", "Factory Id is already registered");
  exit;
}

// check username
$tableName = "user_login_barcode";
$res = $table->ReadTable($tableName, "`username`='$userName'", "");
if (count($res) > 0) {
  echo $result_theme->MakeResult(false, "", "Username is already registered");
  exit;
}

// set serial number to diable
$tableName = "tbl_serialKeyStore";
$query = "UPDATE `$tableName` SET `isValid`=0, `registeredAt`='$created_at', `factoryId`='$factoryId' WHERE `serialKey`='$serialKey'";
$table->ExecuteQuery_Simple_NoResult($query);

// register user
$date = new DateTime();
$user_id = $date->getTimestamp();

$tableName = "user_login_barcode";

$actual_link = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";
$actual_link = str_replace(basename(__FILE__, '.php') . ".php", "", $actual_link);

$query = "INSERT INTO `$tableName` (`id`,`username`, `password`, `username_full`, `user_picture`, `security_level`, `customer_id`)";
$query .= " VALUES ('$user_id', '$userName', '$password', '$userFullName', '" . $actual_link . "images/photo/blank.jpg', 4, '$factoryId')";
$table->ExecuteQuery_Simple_NoResult($query);
// create tatbles
$userTable = $factoryId . "_user";
$ganttTable = $factoryId . "_ganttdata";
$iotTable = $factoryId . "_iotdata";
$hstTable = $factoryId . "_hst";
$infoTable = $factoryId . "_info";
$jobdataTable = $factoryId . "_jobdata";
$maintenanceTable = $factoryId . "_maintenance";
$historyTable = $factoryId . "_maintenance_history";
$statusTable = $factoryId . "_status";
$groupTable = $factoryId . "_machine_group";
$imageTable = $factoryId . "_machine_image";
$shiftTable = $factoryId . "_shifts";

try {
  $query = "SET NAMES utf8mb4;
            SET FOREIGN_KEY_CHECKS = 0;
            DROP TABLE IF EXISTS `$userTable`;
            CREATE TABLE `$userTable`  (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `username_full` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `user_picture` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
              `security_level` int NOT NULL DEFAULT 2 COMMENT '4:administrator, 2:client, 0:operator',
              `customer_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `barcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
            DROP TABLE IF EXISTS `$ganttTable`;
            CREATE TABLE `$ganttTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
              `machine_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `Operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `color` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
              `start` int(20) NOT NULL,
              `end` int(20) NOT NULL,
              `time_stamp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `time_stamp_ms` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `job_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `main_program` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `current_program` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `interface` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
            DROP TABLE IF EXISTS `$hstTable`;
            CREATE TABLE `$hstTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
              `Utilization` int(11) NOT NULL DEFAULT 0,
              `inCycle` int(10) NULL DEFAULT NULL,
              `uncat` int(10) NULL DEFAULT NULL,
              `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `offline` int(10) NULL DEFAULT NULL,
              `r1t` int(10) NULL DEFAULT NULL,
              `r2t` int(10) NULL DEFAULT NULL,
              `r3t` int(10) NULL DEFAULT NULL,
              `r4t` int(10) NULL DEFAULT NULL,
              `r5t` int(10) NULL DEFAULT NULL,
              `r6t` int(10) NULL DEFAULT NULL,
              `r7t` int(10) NULL DEFAULT NULL,
              `r8t` int(10) NULL DEFAULT NULL,
              `machine_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux1data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux2data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux3data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `gantt_data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ',
              `time_stamp` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `time_stamp_ms` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `oee` int(11) NOT NULL,
              `availability` int(11) NOT NULL,
              `quality` int(11) NOT NULL,
              `performance` int(11) NOT NULL,
              `goodParts` int(11) UNSIGNED NOT NULL DEFAULT 0,
              `badParts` int(11) UNSIGNED NOT NULL DEFAULT 0,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

            DROP TABLE IF EXISTS `$infoTable`;
            CREATE TABLE `$infoTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `logo` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
              `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `created_at` timestamp(6) NULL DEFAULT NULL,
              `timezone` int(10) NOT NULL DEFAULT 0,
              `timezone_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `emails` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `report_days` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '123456',
              `shift1_start` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `shift1_end` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `shift2_start` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `shift2_end` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `shift3_start` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `shift3_end` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

            DROP TABLE IF EXISTS `$jobdataTable`;
            CREATE TABLE `$jobdataTable`  (
              `Id` int(11) NOT NULL AUTO_INCREMENT,
              `jobID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `order_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `seq_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `status_type_val` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `pr_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `customer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `partNumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `programNumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `partsPerCycle` int(11) NULL DEFAULT NULL,
              `targetCycleTime` int(11) NULL DEFAULT NULL,
              `qtyRequired` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `qtyCompleted` int(11) NULL DEFAULT NULL,
              `orderDate` datetime NULL DEFAULT NULL,
              `dueDate` datetime NULL DEFAULT NULL,
              `qtyGoodCompleted` int(11) NULL DEFAULT NULL,
              `qtyBadCompleted` int(11) NULL DEFAULT NULL,
              `files` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux1data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
              `aux2data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
              `aux3data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
              `pr_center_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `short_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              PRIMARY KEY (`Id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

            DROP TABLE IF EXISTS `$maintenanceTable`;
            CREATE TABLE `$maintenanceTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `machine_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `task_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `task_category` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `picture` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `task_instruction` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
              `frequency` float(11, 3) NULL DEFAULT NULL,
              `interlock` tinyint(1) NULL DEFAULT NULL,
              `created_at` timestamp(6) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
              `start` int(11) NULL DEFAULT NULL,
              `end` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `is_finished` int(1) NULL DEFAULT NULL,
              `note` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `files` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = DYNAMIC;

            DROP TABLE IF EXISTS `$historyTable`;
            CREATE TABLE `$historyTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `machine_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `maintenance_id` int(11) NULL DEFAULT NULL,
              `start` int(11) NULL DEFAULT NULL,
              `end` int(11) NULL DEFAULT NULL,
              `note` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

            DROP TABLE IF EXISTS `$statusTable`;
            CREATE TABLE `$statusTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `machine_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `machine_picture_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `Operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `operator_picture_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `Utilization` double(255, 0) NULL DEFAULT NULL,
              `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `app_version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `created_at` timestamp(6) NULL DEFAULT NULL,
              `r1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r6` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r7` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `r8` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `machine_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `serial_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `cycle_signal` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `cycle_interlock_interface` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `cycle_interlock_on` tinyint(1) NOT NULL DEFAULT 0,
              `cycle_interlock_open` tinyint(1) NOT NULL DEFAULT 0,
              `time_stamp` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
              `time_stamp_ms` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
              `order` int(10) NOT NULL DEFAULT 1,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

            DROP TABLE IF EXISTS `$groupTable`;
            CREATE TABLE `$groupTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `name` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              `machine_list` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

            DROP TABLE IF EXISTS `$imageTable`;
            CREATE TABLE `$imageTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `machine_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

            DROP TABLE IF EXISTS `$shiftTable`;
            CREATE TABLE `$shiftTable`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `jobID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `sequenceNo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `machine` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `userid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
              `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `startTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `stopTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `oee` int(11) NOT NULL,
              `availability` int(11) NOT NULL,
              `quality` int(11) NOT NULL,
              `performance` int(11) NOT NULL,
              `goodParts` int(10) UNSIGNED NOT NULL DEFAULT 0,
              `badParts` int(10) UNSIGNED NOT NULL DEFAULT 0,
              `inCycle` int(11) NOT NULL DEFAULT 0,
              `uncat` int(11) NULL DEFAULT NULL,
              `offline` int(11) NULL DEFAULT NULL,
              `r1t` int(11) NULL DEFAULT NULL,
              `r2t` int(11) NULL DEFAULT NULL,
              `r3t` int(11) NULL DEFAULT NULL,
              `r4t` int(11) NULL DEFAULT NULL,
              `r5t` int(11) NULL DEFAULT NULL,
              `r6t` int(11) NULL DEFAULT NULL,
              `r7t` int(11) NULL DEFAULT NULL,
              `r8t` int(11) NULL DEFAULT NULL,
              `aux1data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux2data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `aux3data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
              `rework` int(2) NOT NULL DEFAULT 0,
              `setup` int(2) NOT NULL DEFAULT 0,
              `shiftTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `targetCycleTime` int(11) NOT NULL DEFAULT 0,
              `plannedProductionTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              `part_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
            
            SET FOREIGN_KEY_CHECKS = 1;";

  $query = $query . "INSERT INTO `$infoTable` (`id`, `logo`, `name`, `timezone`, `timezone_name`) VALUES ('1', '$logo', '$factoryName', $userTimezone, '$userTimezoneName')";
  $table->ExecuteMultiQuery($query);

  $query = "INSERT INTO `$userTable` (`username`, `password`, `username_full`, `user_picture`, `security_level`, `customer_id`, `barcode`)";
  $query .= " VALUES ('$userName', '$password', '$userFullName', '" . $actual_link . "images/photo/blank.jpg', 4, '$factoryId', '$user_id')";
  $table->ExecuteQuery_Simple_NoResult($query);

} catch (Exception $err) {
  echo $result_theme->MakeResult(false, $query, "Error");
}
echo $result_theme->MakeResult(true, $data);
