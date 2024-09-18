package com.cam8.mmsapp.mail;

import java.util.ArrayList;
import java.util.Properties;

public class OutlookMail extends BaseMail {

    // https://www.lifewire.com/what-are-the-outlook-com-smtp-server-settings-1170671
    public void setMail() {

        _host = "smtp-mail.outlook.com";
        _port = "587";
        _sport = "587";

        super.setMail();
    }

    public OutlookMail(String user, String pass, ArrayList<String> to) {

        super(user, pass, to);
    }

    protected  Properties _setProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", _host);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", _port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return props;
    }
}


