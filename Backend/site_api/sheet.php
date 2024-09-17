<?php // Crontab Api
require __DIR__ . '/vendor/autoload.php';

include_once("./common/headers.php");
include_once("./common/TableMgr.php");
include_once("./common/Result.php");


// if (php_sapi_name() != 'cli') {
//     throw new Exception('This application must be run on the command line.');
// }

/**
 * Returns an authorized API client.
 * @return Google_Client the authorized client object
 */
function getClient()
{
    $client = new Google_Client();
    $client->setApplicationName('Google Drive API PHP Quickstart');
    $client->setScopes(Google_Service_Drive::DRIVE);
    $client->setAuthConfig('credentials.json');
    $client->setAccessType('offline');
    $client->setPrompt('select_account consent');

    // Load previously authorized token from a file, if it exists.
    // The file token.json stores the user's access and refresh tokens, and is
    // created automatically when the authorization flow completes for the first
    // time.
    $tokenPath = 'token.json';
    if (file_exists($tokenPath)) {
        $accessToken = json_decode(file_get_contents($tokenPath), true);
        $client->setAccessToken($accessToken);
    }

    // If there is no previous token or it's expired.
    if ($client->isAccessTokenExpired()) {
        // Refresh the token if possible, else fetch a new one.
        if ($client->getRefreshToken()) {
            $client->fetchAccessTokenWithRefreshToken($client->getRefreshToken());
        } else {
            // Request authorization from the user.
            $authUrl = $client->createAuthUrl();
            printf("Open the following link in your browser:\n%s\n", $authUrl);
            print 'Enter verification code: ';
            $authCode = trim(fgets(STDIN));

            // Exchange authorization code for an access token.
            $accessToken = $client->fetchAccessTokenWithAuthCode($authCode);
            $client->setAccessToken($accessToken);

            // Check to see if there was an error.
            if (array_key_exists('error', $accessToken)) {
                throw new Exception(join(', ', $accessToken));
            }
        }
        // Save the token to a file.
        if (!file_exists(dirname($tokenPath))) {
            mkdir(dirname($tokenPath), 0700, true);
        }
        file_put_contents($tokenPath, json_encode($client->getAccessToken()));
    }
    return $client;
}


// Get the API client and construct the service object.
$client = getClient();
$service = new Google_Service_Drive($client);
$folderId = "12D404zUFFYHy3fxZoOU_7imELmI0j4Z0";
// Print the names and IDs for up to 10 files.
$optParams = array(
    'pageSize' => 10,
    'fields' => 'nextPageToken, files(id, name)',
    'q' => "'" . $folderId . "' in parents"
);

$results = $service->files->listFiles($optParams);

if (count($results->getFiles()) == 0) {
    print "No files found.\n";
} else {
    print "Files:\n";
    foreach ($results->getFiles() as $file) {
        printf("%s (%s)\n", $file->getName(), $file->getId());
        $content = $service->files->get($file->getId(), array("alt" => "media"));

        file_put_contents($file->getName(), $content->getBody());

        $savedfile = new SplFileObject($file->getName());

        $table = new TableMgr();
        $trucateQuery = "TRUNCATE sm_ks_jobdata";
        $table->ExecuteQuery_Simple_NoResult($trucateQuery);

        $savedfile->fgets();
        $lineNumber = 0;
        $errorCount = 0;
        while (!$savedfile->eof()) {
            $lineNumber = $lineNumber + 1;
            $lineStr =  $savedfile->fgets();
            $lineStr =  str_replace("\'", "", $lineStr);
            $oneLine = explode("\",\"", $lineStr);


            $jobID_long = str_replace("\"", "", $oneLine[0]);
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

            $aux1data = $oneLine[18];
            $aux2data = $oneLine[19];
            $aux3data = $oneLine[12];

            $prCenterNo = $oneLine[25];
            $shortDesc = $oneLine[26];

            $insertQuery = "INSERT INTO " . $customer_id . "_jobdata (`jobId`, `order_type`, `seq_no`, `status_type_val`, `pr_order_no`, `customer`, `partNumber`, `description`, `qtyRequired`, `dueDate`, `aux1data`, `aux2data`, `aux3data`, `pr_center_no`, `short_desc`) VALUES 
                                                                    ('$jobId', '$orderType', '$seqNo', '$statusTypeVal', '$prOrderNo', '$customer', '$partNumber', '$description', '$qtyRequired', '$dueDate', '$aux1data', '$aux2data', '$aux3data', '$prCenterNo', '$shortDesc')";

            $res = $table->ExecuteQuery_Simple_Result($insertQuery);
            if ($res != 1) {
                print "Error in line : $lineNumber#####Sql query: $insertQuery\n";
                $errorCount = $errorCount + 1;
            }
        }
        $savedfile = null;
        break;
    }
}
