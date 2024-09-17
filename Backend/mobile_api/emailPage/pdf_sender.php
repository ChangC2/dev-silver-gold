<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS, DELETE, PUT');
header('Access-Control-Allow-Headers: Origin, Content-Type, Accept, Authorization, X-Request-With, X-CLIENT-ID, X-CLIENT-SECRET');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');

require('fpdf.php');
include_once('mailSender.php');

class PDF extends FPDF
{
    // Page header
    function Header()
    {
        // // Logo
        // $this->Image('3.jpeg', 10, 6, 30);
        // Arial bold 15
        $this->SetFont('Arial', 'B', 15);
        // Move to the right
        // $this->Cell(80);
        // Title
        $this->Cell(70, 10, 'Manual REPORT', 1, 0, 'C');
        // Line break
        $this->Ln(20);
    }

    // Page footer
    function Footer()
    {
        // Position at 1.5 cm from bottom
        $this->SetY(-15);
        // Arial italic 8
        $this->SetFont('Arial', 'I', 8);
        // Page number
        $this->Cell(0, 10, 'Page ' . $this->PageNo() . '/{nb}', 0, 0, 'C');
    }
}

function base64_to_jpeg($base64_string, $output_file)
{
    // open the output file for writing
    $ifp = fopen($output_file, 'wb');

    // split the string on commas
    // $data[ 0 ] == "data:image/png;base64"
    // $data[ 1 ] == <actual base64 string>
    $data = explode(',', $base64_string);

    // we could add validation here with ensuring count( $data ) > 1
    fwrite($ifp, base64_decode($data[1]));

    // clean up the file resource
    fclose($ifp);

    return $output_file;
}


$req = trim(file_get_contents("php://input"));
$params = json_decode($req, true);

$imageData = isset($params['imageData']) != '' ? $params['imageData'] : '';
$machine_id = isset($params['machine_id']) != '' ? $params['machine_id'] : '';
$emailList = isset($params['emailList']) != '' ? $params['emailList'] : '';

$res = [];
// create jpeg from imageData
if ($imageData != '') {

    // Create jpeg
    $date = new DateTime();
    $uniqName = $machine_id . "_" . $date->getTimestamp();
    $imageFile = "pdf_files/" . $uniqName . ".jpeg";
    base64_to_jpeg($imageData, $imageFile);


    // Create PDF
    $pdf = new PDF('P', 'mm', 'A4');
    $pdf->AliasNbPages();
    $pdf->AddPage();
    $pdf->SetFont('Times', '', 12);
    $pdf->Image($imageFile, 5, 30);
    $pdfFile = "pdf_files/" . $uniqName . ".pdf";
    $pdf->Output('F', $pdfFile);


    // Send email
    $mailer = new mailSender();
    for ($k = 0; $k < count($emailList); $k++) {
        $mailer->SendEmail("Manual Report.", $pdfFile, $emailList[$k], "Manual Report");
    }
    $res['emailList'] = $emailList;
    $res['result'] = true;
} else {
    // echo false;
    $res['result'] = false;
}

echo json_encode($res);
