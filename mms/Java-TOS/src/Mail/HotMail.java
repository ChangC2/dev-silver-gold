package Mail;

import java.util.ArrayList;
import java.util.Properties;

public class HotMail extends BaseMail {

    // https://www.lifewire.com/what-are-windows-live-hotmail-smtp-settings-1170861
    public void setMail() {

        _host = "smtp.live.com";
        _port = "587"; // default smtp port
        _sport = "587"; // default socketfactory port

        super.setMail();
    }

    public HotMail(String user, String pass, ArrayList<String> to) {

        super(user, pass, to);
    }

    protected Properties _setProperties() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", _host);
        props.put("mail.smtp.host", _host);
        props.put("mail.smtp.port", _port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return props;
    }
}


