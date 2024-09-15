package com.cam8.mmsapp.mail;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class MailSender extends AsyncTask<Void, Void, Void> {

    public interface MailSendCallback {
        void onMailFailed(String error);
        void onMailSent();
    }

    String fromEmail;
    String passEmail;
    ArrayList<String> toEmails;
    String subject;
    String contents;
    String attachment;

    MailSendCallback callback;

    boolean result;
    String message;

    public MailSender(String from, String pass, ArrayList<String> to, String subject, String contents, MailSendCallback callback) {
        this.fromEmail = from;
        this.passEmail = pass;
        this.toEmails = to;
        this.subject = subject;
        this.contents = contents;
        this.attachment = attachment;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        result = false;
        message = "";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.e("Mail", "- Start Send -");

        // Choose Mail Sender
        BaseMail mail;
        if (fromEmail.endsWith("gmail.com")) {
            mail = new GMail(fromEmail, passEmail, toEmails);
        } else if (fromEmail.endsWith("hotmail.com")) {
            mail = new HotMail(fromEmail, passEmail, toEmails);
        } else if (fromEmail.endsWith("yahoo.com")) {
            mail = new YahooMail(fromEmail, passEmail, toEmails);
        } else if (fromEmail.endsWith("outlook.com")) {
            mail = new OutlookMail(fromEmail, passEmail, toEmails);
        } else if (fromEmail.endsWith("slymms.com")) {
            mail = new Office365Mail(fromEmail, passEmail, toEmails);
        } else {
            mail = new GMail(fromEmail, passEmail, toEmails);
        }

        try {
            if (mail.send(subject, contents, null)) {
                Log.e("Message", "Email successfully sent!");
                result = true;
            } else {
                Log.e("Message", "Email can not be sent!");
                result = false;
                message = "Email can not be sent!";
            }
        } catch (Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
            result = false;
            message = e.getMessage();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // Send Result
        if (callback != null) {
            if (result) {
                callback.onMailSent();
            } else {
                callback.onMailFailed(message);
            }
        }
    }
}
