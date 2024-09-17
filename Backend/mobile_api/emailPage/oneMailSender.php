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
            //$mail->Username   = 'shildra7.dev@gmail.com';                     // SMTP username
            //$mail->Password   = 'dhgidsusDHGIDSUS-3.16';                               // SMTP password
            //$mail->SMTPDebug  = 1;

            // For direct smtp.gmail
            $mail->Host = "smtp.gmail.com";
            $mail->isSMTP();
            $mail->SMTPAuth = true;
            $mail->SMTPSecure = "ssl";
            $mail->Port = "465";
            
            
            // For direct slymms.com
            // $mail->Username = "reports@slymms.com";
            // $mail->Password = "895736def";
            // $mail->isSMTP();
            // $mail->SMTPAuth = true;
            // $mail->Host = "slymms.com";
            // $mail->SMTPSecure = "ssl";
            // $mail->Port = "465";

            // // // For GoDaddy localhost
            // $mail->isSMTP();
            // $mail->Host = 'slymms.com';
            // $mail->Username   = '_mainaccount@slymms.com';                     // SMTP username
            // $mail->Password   = '7Q>EvCF0X';                               // SMTP password            
            // $mail->SMTPAuth = true;
            // //$mail->SMTPSecure = "ssl";
            // //$mail->SMTPAutoTLS = true;
            // $mail->Port = 465;
            
            //Recipients
            $mail->setFrom('reports@slytrackr.com', 'SLYTRACKR');
            $mail->addAddress($destAddr);     // Add a recipient

            //

            // Attachments
            if($attachUrl != "")
                $mail->addAttachment($attachUrl);         // Add attachments
            //$mail->addAttachment('/tmp/image.jpg', 'new.jpg');    // Optional name

            // Content
            $mail->isHTML();                                  // Set email format to HTML
            $mail->Subject = $title;
            $mail->Body    = $mailContent;
            //$mail->AltBody = 'This is the body in plain text for non-HTML mail clients';

            $mail->send();
            echo 'Message has been sent'.PHP_EOL;
        } catch (Exception $e) {
            echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
        }
    }
}
