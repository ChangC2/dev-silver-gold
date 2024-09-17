<?php // Desktop Api
require __DIR__ . '/vendor/autoload.php';

include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');

$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);
$customer_id = isset($_POST['customerID']) != '' ? $_POST['customerID'] : '';

if (isset($_FILES['file'])) {
    $file_name = $_FILES['file']['name'];
    $file_size = $_FILES['file']['size'];
    $file_tmp = $_FILES['file']['tmp_name'];
    $file_type = $_FILES['file']['type'];

    $myResult['file_tmp'] = $file_tmp;

    $time = date("YmdHsi");

    try {
        // $fName = $time . "_" . $file_name;
        $url = "files/" . $file_name;

        move_uploaded_file($file_tmp, $url);

        $savedfile = new SplFileObject($url);

        $table = new TableMgr();
        $trucateQuery = "TRUNCATE " . $customer_id . "_jobdata";
        $table->ExecuteQuery_Simple_NoResult($trucateQuery);

        $savedfile->fgets();
        $lineNumber = 0;
        $errorCount = 0;
        $errorStr = array();
        while (!$savedfile->eof()) {
            $lineNumber = $lineNumber + 1;
            $lineStr =  $savedfile->fgets();
            $lineStr =  str_replace("\'", "", $lineStr);
            $oneLine = explode("\",\"", $lineStr);

            $jobID_long = str_replace("\"", "", $oneLine[0]);
            //$jobId = str_replace("\"", "", $oneLine[0]);
            $orderType = $oneLine[1];
            $seqNo = $oneLine[2];
            $statusTypeVal = $oneLine[3];

            $jobId = $oneLine[4];
            //$prOrderNo = $oneLine[4];

            $customer = $oneLine[6];
            $partNumber = $oneLine[5];
            $description = $oneLine[7];
            $qtyRequired = $oneLine[11];
            $dueDateOld = $oneLine[8];
            $dateArry = explode("/", $dueDateOld);
            $dueDate = "20$dateArry[2]-$dateArry[0]-$dateArry[1]";

            $aux1data = "";
            $aux2data = "";
            $aux3data = "";
            if ($customer_id == "sm_ks") {
                $aux1data = $oneLine[18];
                $aux2data = $oneLine[19];
                $aux3data = $oneLine[12];
            }

            $prCenterNo = $oneLine[25];
            $shortDesc = $oneLine[26];

            $insertQuery = "INSERT INTO " . $customer_id . "_jobdata (`jobId`, `order_type`, `seq_no`, `status_type_val`, `pr_order_no`, `customer`, `partNumber`, `description`, `qtyRequired`, `dueDate`, `aux1data`, `aux2data`, `aux3data`, `pr_center_no`, `short_desc`) VALUES 
                                                                    ('$jobId', '$orderType', '$seqNo', '$statusTypeVal', '$prOrderNo', '$customer', '$partNumber', '$description', '$qtyRequired', '$dueDate', '$aux1data', '$aux2data', '$aux3data', '$prCenterNo', '$shortDesc')";

            $res = $table->ExecuteQuery_Simple_Result($insertQuery);

            if ($res != 1) {
                $errorStr[] =  $insertQuery;
            }

            $myResult['error'] = $errorStr;
            $myResult['url'] = $url;
            $myResult['status'] = true;
            $myResult['message'] = "Upload success";
        }
    } catch (Exception $err) {
        $myResult['message'] = "$err";
    }
} else {
    $myResult['status'] = false;
    $myResult['message'] = "Not found file.";
}

echo json_encode($myResult);
