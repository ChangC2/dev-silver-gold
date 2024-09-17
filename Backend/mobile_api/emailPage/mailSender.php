<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';


class mailSender
{
    function SendEmail($mailContent, $attachUrl, $destAddr, $title="24 Hour Machine Report")
    {
        $mail = new PHPMailer(true);
        try {
            $mail->Username   = 'reports@slytrackr.com';                     // SMTP username
            $mail->Password   = '3039681707Sly';                               // SMTP password            
            // For direct smtp.gmail
            $mail->Host = "smtp.gmail.com";
            $mail->isSMTP();
            $mail->SMTPAuth = true;
            $mail->SMTPSecure = "ssl";
            $mail->Port = "465";
            //Recipients
            $mail->setFrom('reports@slytrackr.com', 'SLYTRACKR');
            $mail->addAddress($destAddr);     // Add a recipient
            // Attachments
            if($attachUrl != "")
                $mail->addAttachment($attachUrl);         // Add attachments
            // Content
            $mail->isHTML();                                  // Set email format to HTML
            $mail->Subject = $title;
            $mail->Body    = $mailContent;
            $mail->send();
        } catch (Exception $e) {
            echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
        }
    }
}
