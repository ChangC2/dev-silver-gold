<?php

require_once 'vendor/autoload.php';

class MY_Controller extends CI_Controller
{
	public function __construct()
	{
		parent::__construct();
		date_default_timezone_set('America/Los_Angeles');
		if (empty($this->session->userdata('logged_in'))) {
			$session_data = array('logged_in' => FALSE);
			$this->session->set_userdata($session_data);
		} else {
			$this->data['permission'] = "";
			$this->data['token'] = "";
		}
	}

	public function logged_in()
	{
		$session_data = $this->session->userdata();
		if ($session_data['logged_in'] == TRUE) {
			redirect('maillog', 'refresh');
		}
	}

	public function not_logged_in()
	{
		$session_data = $this->session->userdata();
		if ($session_data['logged_in'] == FALSE) {
			redirect('auth/login', 'refresh');
		}
		$user_id = $this->session->userdata('id');
		$this->load->model('model_signs');
		$sign = $this->model_signs->get($user_id);
		$signed = 0;
		if ($sign != null) {
			$signed = 1;
		}
		$this->data['signed'] = $signed;
	}

	public function render_template($page = null, $data = array())
	{
		$this->load->view('templates/header', $data);
		$this->load->view('templates/main_header', $data);
		$this->load->view('templates/main_sidebar', $data);
		$this->load->view($page, $data);
		$this->load->view('templates/footer', $data);
	}

	public function password_hash($pass = '')
	{
		if ($pass) {
			$password = password_hash($pass, PASSWORD_DEFAULT);
			return $password;
		}
	}

	function encrypt($original_string)
	{
		$ciphering = "BF-CBC";
		$options = 0;
		$encryption_iv = "12345678";
		$encryption_key = openssl_digest(php_uname(), 'MD5', TRUE);
		$encryption = openssl_encrypt(
			$original_string,
			$ciphering,
			$encryption_key,
			$options,
			$encryption_iv
		);
		return $encryption;
	}

	function decrypt($encrypted_string)
	{
		$ciphering = "BF-CBC";
		$options = 0;
		$encryption_iv = "12345678";
		$encryption_key = openssl_digest(php_uname(), 'MD5', TRUE);
		$decryption = openssl_decrypt(
			$encrypted_string,
			$ciphering,
			$encryption_key,
			$options,
			$encryption_iv
		);
		return $decryption;
	}

	function hash_equals_custom($knownString, $userString)
	{
		if (function_exists('mb_strlen')) {
			$kLen = mb_strlen($knownString, '8bit');
			$uLen = mb_strlen($userString, '8bit');
		} else {
			$kLen = strlen($knownString);
			$uLen = strlen($userString);
		}
		if ($kLen !== $uLen) {
			return false;
		}
		$result = 0;
		for ($i = 0; $i < $kLen; $i++) {
			$result |= (ord($knownString[$i]) ^ ord($userString[$i]));
		}
		return 0 === $result;
	}

	public function getUserIP()
	{
		// Get real visitor IP behind CloudFlare network
		return $this->input->ip_address();
	}
	

	public function updateCredits($user_id, $credits, $note)
	{
		try {
			$this->load->model('model_accounts');

			$user_info = $this->model_accounts->get_user_info(null, $user_id);
			$data = array(
				"credits" => $user_info["credits"] + $credits
			);
			$this->model_accounts->update($data, $user_id);

			$soapUrl = "http://ticorfarming.leadmarketer.com/wsFarming.asmx?op=ApplyRinglessCredits";
			$xml_post_string = '<?xml version="1.0" encoding="utf-8"?>
							<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
								<soap:Body>
									<ApplyRinglessCredits xmlns="http://tempuri.org/">
										<APIKey>jsdfnWdf23@4</APIKey>
										<UserID>' . $user_info["request_id"] . '</UserID>
										<Email>' . $user_info["email"] . '</Email>
										<Credits>' . $credits . '</Credits>
										<Notes>Ringless Voicemail' . $note . '</Notes>
									</ApplyRinglessCredits>
								</soap:Body>
							</soap:Envelope>';

			$headers = array(
				"POST /wsFarming.asmx HTTP/1.1",
				"Host: ticorfarming.leadmarketer.com",
				"Content-Type: text/xml; charset=utf-8",
				"Content-Length: " . strlen($xml_post_string)
			);

			$url = $soapUrl;

			$ch = curl_init();
			curl_setopt($ch, CURLOPT_URL, $url);
			curl_setopt($ch, CURLOPT_POST, true);
			curl_setopt($ch, CURLOPT_POSTFIELDS, $xml_post_string);
			curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

			$response = curl_exec($ch);
			curl_close($ch);
			return $response;
		} catch (Exception $e) {
			return $e;
		}
	}

	public function fileUpload($dir = "files")
	{
		$file_name = '';
		$file_path = $dir . '/';

		if (!file_exists($file_path)) {
			mkdir($file_path, 0777, true);
		}
		if (isset($_FILES["file"]["name"])) {
			$name =  $_FILES["file"]["name"];
			$tmp_name = $_FILES['file']['tmp_name'];
			if (!empty($name)) {
				$ext = pathinfo($name, PATHINFO_EXTENSION);
				$file_name = date("YmdHsi") . "." . $ext;
				if (move_uploaded_file($tmp_name, $file_path . $file_name)) {
					if ($ext == "wav") {
						$mp3File = $file_path . date("YmdHsi") . ".mp3";
						$this->convert_to_mp3($file_path . $file_name, $mp3File);
						return $mp3File;
					}
					return $file_path . $file_name;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public function audioUpload()
	{
		if (isset($_FILES['audio'])) { // Upload the audio recorded on site.
			$audio = $_FILES['audio'];

			if ($audio['error'] === UPLOAD_ERR_OK) {
				// Get the temporary location of the uploaded file
				$tempFile = $audio['tmp_name'];

				// Create a unique name for the audio file
				$fileName = date("YmdHsi") . "." . pathinfo($audio['name'], PATHINFO_EXTENSION);

				// Set the destination directory for the uploaded audio files
				$uploadDirectory = 'audios/';
				$destination = $uploadDirectory . $fileName;

				// Check if the destination directory exists, create it if necessary
				if (!is_dir($uploadDirectory)) {
					mkdir($uploadDirectory, 0777, true);
				}
				// Move the uploaded file to the destination directory
				if (move_uploaded_file($tempFile, $destination)) {
					$mp3File =  $uploadDirectory . date("YmdHsi") . ".mp3";
					$this->convert_to_mp3($destination, $mp3File);
					$data = array(
						"status" => true,
						"url" => $mp3File
					);
					echo (json_encode($data));
					return;
				} else {
					$data = array(
						"status" => false
					);
					echo (json_encode($data));
					return;
				}
			} else {
				$data = array(
					"status" => false
				);
				echo (json_encode($data));
				return;
			}
		} else {
			$file_name = '';
			$file_path = 'audios/';

			if (!file_exists($file_path)) {
				mkdir($file_path, 0777, true);
			}
			if (isset($_FILES["file"]["name"])) {
				$name =  $_FILES["file"]["name"];
				$tmp_name = $_FILES['file']['tmp_name'];
				if (!empty($name)) {
					$file_name = date("YmdHsi") . "." . pathinfo($name, PATHINFO_EXTENSION);
					if (move_uploaded_file($tmp_name, $file_path . $file_name)) {
						$ext = pathinfo($name, PATHINFO_EXTENSION);
						if ($ext != "mp3") {
							$mp3File =  $file_path . date("YmdHsi") . ".mp3";
							$this->convert_to_mp3($file_path . $file_name, $mp3File);
							echo $mp3File;
						} else {
							echo $file_path . $file_name;
						}
					}
				} else {
					echo false;
				}
			} else {
				echo false;
			}
		}
	}

	public function convert_to_mp3($inputFilename, $outputFilename)
	{
		$apiUrl = "https://api.convertio.co/convert";
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POST, 1);
		curl_setopt($ch, CURLOPT_URL, $apiUrl);
		$headers  = [
			'Content-Type: application/json'
		];
		$postData = [
			'apikey' => 'eabc8ebe0c8368706b74214b5b783707',
			'file' => base_url() . $inputFilename,
			'outputformat' => "mp3"
		];
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($postData));
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		$json = curl_exec($ch);
		$res1 = json_decode($json);
		curl_close($ch);
		try {
			if ($res1->code == 200) {
				$id = $res1->data->id;
				$step = "";
				while ($step != "finish") {
					$apiUrl = "https://api.convertio.co/convert/$id/status";
					$ch = curl_init();
					// Will return the response, if false it print the response
					curl_setopt($ch, CURLOPT_URL, $apiUrl);
					curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
					curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
					$headers  = [
						'Content-Type: application/json'
					];
					curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
					$json = curl_exec($ch);
					$res2 = json_decode($json);
					$step = $res2->data->step;
					if ($step == "finish") {
						$remoteFileUrl = $res2->data->output->url;
						$chDownload = curl_init();
						curl_setopt($chDownload, CURLOPT_URL, $remoteFileUrl);
						curl_setopt($chDownload, CURLOPT_RETURNTRANSFER, 1); // Return the transfer as a string
						curl_setopt($chDownload, CURLOPT_HEADER, 0);
						$fileContent = curl_exec($chDownload);
						$httpStatusCode = curl_getinfo($chDownload, CURLINFO_HTTP_CODE);
						if (curl_errno($chDownload) || $httpStatusCode != 200) {
							echo "Error: Failed to download the file. HTTP status code: $httpStatusCode\n";
						} else {
							$fp = fopen($outputFilename, 'w+');
							if ($fp === false) {
								echo "Error: Unable to open the local file for writing.\n";
							} else {
								fwrite($fp, $fileContent);
								fclose($fp);
							}
						}
						curl_close($chDownload);
					}
					curl_close($ch);
				}
			}
		} catch (Exception $e) {
		}
	}

	/*=================================================================================
								send smtp
==================================================================================*/

	function send_smtp($toEmail = null, $subject, $mailBody)
	{
		$Host = "Mail.allaboutleads.com";
		$Username = "Lead@allaboutleads.com";
		$Password = "cP[s\$Ou6\$t+;";

		$Port = 25;

		try {
			$mail = new PHPMailer\PHPMailer\PHPMailer(true);

			$mail->isSMTP();
			$mail->Mailer = "smtp";
			$mail->CharSet = 'UTF-8';

			$mail->Host       = $Host;
			$mail->SMTPAuth   = true;                  // enable SMTP authentication
			$mail->SMTPAutoTLS = false;
			$mail->Username   = $Username;
			$mail->Password   = $Password;

			$mail->SMTPDebug  = 0;                     // enables SMTP debug information (for testing)
			$mail->Port       = $Port;                    // set the SMTP port for the GMAIL server
			if ($toEmail != null) {
				$mail->AddAddress($toEmail);
			} else {
				$mail->AddAddress("sales@newhomepage.com");
			}
			$mail->SetFrom($Username, "NewHomePage LLC");
			$mail->addBcc("sales@newhomepage.com");
			$mail->addBcc("Zho@newhomepage.com");

			$mail->isHTML(true);
			$mail->Subject = $subject;
			$mail->Body = $mailBody;

			$res = $mail->send();
			return $res;
		} catch (PHPMailer\PHPMailer\Exception $e) {
			return false;
		}
	}

	function decryptString($value)
	{
		$xorConstant = 0x53;
		$data = base64_decode($value);

		$result = '';
		for ($i = 0; $i < strlen($data); $i++) {
			$result .= chr(ord($data[$i]) ^ $xorConstant);
		}

		// Assuming the result is in valid UTF-8, no conversion is necessary.
		return $result;
	}

	function encryptString($value)
	{
		$ret = "";
		$xorConstant = 0x53;
		try {
			$data = mb_convert_encoding($value, "UTF-8", "auto");
			for ($i = 0; $i < mb_strlen($data, "UTF-8"); $i++) {
				$data[$i] = chr(ord($data[$i]) ^ $xorConstant);
			}
			$ret = base64_encode($data);
		} catch (Exception $e) {
			error_log($e->getMessage());
		}
		return $ret;
	}
}
