<?php

chdir(__DIR__);


include_once('mailSender.php');

$mailer = new mailSender();

function writeFile($filename, $text, $emailArray)
{
	$html = file_get_contents(getcwd() . "/pageTheme.html");
	file_put_contents(getcwd() . "/HtmlFiles/" . $filename . ".html", $html . $text . "\n", LOCK_EX);
	$mailer = new mailSender();
	// for ($k = 0; $k < count($emailArray); $k++) {
	//     $mailer->SendEmail("New Report. \n" . $filename, getcwd() . "/HtmlFiles/" . $filename . ".html", $emailArray[$k]);
	// }
	// $mailer->SendEmail("New Report. \n" . $filename, getcwd() . "/HtmlFiles/" . $filename . ".html", "chuntianhongsoft@gmail.com");
	$mailer->SendEmail("New Report. \n" . $filename, getcwd() . "/HtmlFiles/" . $filename . ".html", "vientiane2021it@gmail.com");
};

include_once("dataReceiver.php");
$receiver = new dataReceiver();
$values = array();
$values = $receiver->ReadData();
for ($i = 0; $i < count($values); $i++) {
	$filename = $values[$i]['mytime'] . "_" . $values[$i]['info']['name'];
	$content = "<script>\n";

	$content .= "var m_status=" . json_encode($values[$i]['status']) . ";\n";
	$content .= "var m_hst=" . json_encode($values[$i]['hst']) . ";\n";
	$content .= "var m_gantt=" . json_encode($values[$i]['gantt']) . ";\n";
	$content .= "var m_info=" . json_encode($values[$i]['info']) . ";\n";
	// $content .= "var m_userInfo=".json_encode($values[$i]['userInfo']).";\n";
	$content .= "var m_hstList=" . json_encode($values[$i]['hstList']) . ";\n";
	$content .= "</script>\n";


	if (strpos($values[$i]['info']['report_days'], date('w', strtotime(date("Y-m-d")))) !== false) {
		$mailString = $values[$i]['info']['emails'];
		$mailArray = array();
		$mailArray = explode(";", $mailString);
		$emailList = array();

		foreach ($mailArray as $mail) {
			if ($mail == "") continue;
			$tmp = explode(":", $mail);

			if (count($tmp) > 1 && $tmp[1] == '0')
				continue;
			$emailList[] = $tmp[0];
		}
		writeFile($filename, $content, $emailList);
	}
}
