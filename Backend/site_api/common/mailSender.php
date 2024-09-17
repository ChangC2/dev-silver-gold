<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';

class mailSender
{
    function SendEmail($destAddr, $title, $mailContent)
    {
        $mail = new PHPMailer(true);
        
        try {
            $mail->Username   = 'reports@slytrackr.com';                     // SMTP username
            $mail->Password   = '3039681707Sly';                               // SMTP password            
          
            $mail->Host = "smtp.gmail.com";
            $mail->isSMTP();
            $mail->SMTPAuth = true;
            $mail->SMTPSecure = "ssl";
            $mail->Port = "465";
            
            //Recipients
            $mail->setFrom('reports@slytrackr.com', 'SLYTRACKR');
            $mail->addAddress($destAddr);     // Add a recipient

            $mail->isHTML();                                  // Set email format to HTML
            $mail->Subject = $title;
            $mail->Body    = $mailContent;   //Html msg.
            //$mail->AltBody = $mailContent;  //Text msg.

            $mail->send();
            echo 'Message has been sent'.PHP_EOL;
        } catch (Exception $e) {
            echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
        }
    }
}
