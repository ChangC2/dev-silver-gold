package com.cam8.mmsapp.mail;

import java.util.ArrayList;

public class YahooMail extends BaseMail {

    // https://www.lifewire.com/what-are-yahoo-smtp-settings-for-email-1170875

    public void setMail() {

        _host = "smtp.mail.yahoo.com";
        _port = "465"; // default smtp port, 587 is an alternative
        _sport = "465"; // default socketfactory port

        super.setMail();
    }

    public YahooMail(String user, String pass, ArrayList<String> to) {
        super(user, pass, to);
    }
}


