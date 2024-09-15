package Mail;

import java.util.ArrayList;

public class GMail extends BaseMail {

    public void setMail() {

        _host = "smtp.gmail.com";
        _port = "465"; // default smtp port
        _sport = "465"; // default socketfactory port

        super.setMail();
    }

    public GMail(String user, String pass, ArrayList<String> to) {

        super(user, pass, to);
    }
}
