package Mail;

import org.apache.http.util.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

public class BaseMail extends javax.mail.Authenticator {
    protected String _user = "";
    protected String _pass = "";

    //private String _to = "ezequiel.ribeiro@gmail.com";
    // private String _from = "ezequiel.ribeiro@gmail.com";

    protected ArrayList<String> _to = new ArrayList<>();
    protected String _from = "";

    // Default use Gmail
    protected String _host = "smtp.gmail.com";
    protected String _port = "465";
    protected String _sport = "465";

    protected String _title = "SLYTRACKR";
    protected String _body = "Log";

    protected boolean _auth;

    protected boolean _debuggable;

    protected Multipart _multipart;


    public void setMail() {

        _debuggable = false; // debug mode on or off - default off
        _auth = true; // smtp authentication - default on

        _multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public BaseMail(String user, String pass, ArrayList<String> to) {

        _user = user;
        _from = user;
        _pass = pass;
        _to = to;

        setMail();
    }

    @SuppressWarnings("static-access")
    public boolean send(String title, String texto, String attachment) throws Exception, AddressException, MessagingException {

        if(!TextUtils.isEmpty(attachment)) {
            addAttachment(attachment);
        }

        Properties props = _setProperties();

        if (!_user.equals("") && !_pass.equals("") && !_to.isEmpty() && !_from.equals("")) {
            //Session session = Session.getInstance(props, this);

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(_user, _pass);
                }
            });

            MimeMessage simpleMessage = new MimeMessage(session);

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(texto);
            _multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            simpleMessage.setContent(_multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect();
            // send email

            // Set From Address
            InternetAddress fromAddress = new InternetAddress(_from);
            simpleMessage.setFrom(fromAddress);

            // Set To Addresses
            InternetAddress[] toAddresses = new InternetAddress[_to.size()];
            for (int i = 0; i < _to.size(); i++) {
                toAddresses[i] = new InternetAddress(_to.get(i));
            }
            //InternetAddress toAddress = new InternetAddress("xxxx@mail.com");
            //simpleMessage.setRecipient(RecipientType.TO, toAddress);
            simpleMessage.setRecipients(RecipientType.TO, toAddresses);

            simpleMessage.setSubject(_title);
            //simpleMessage.setText(texto);
            simpleMessage.setContent(_multipart);
            simpleMessage.setSentDate(new Date());

            transport.sendMessage(simpleMessage,
                    simpleMessage.getAllRecipients());

            return true;
        } else {
            return false;
        }
    }

    private void addAttachment(String filePath) throws Exception {

        BodyPart messageBodyPart = new MimeBodyPart();
        FileDataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));

        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        messageBodyPart.setFileName(fileName);

        _multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(_user, _pass);
    }

    protected Properties _setProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.host", _host);

        if (_debuggable) {
            props.put("mail.debug", "true");
        }

        if (_auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", _port);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("SSLContext.TLS",
                "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
        props.put("Alg.Alias.SSLContext.TLSv1", "TLS");
        props.put("KeyManagerFactory.X509",
                "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
        props.put("TrustManagerFactory.X509",
                "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");


        return props;
    }

    // the getters and setters
    public String getBody() {
        return _body;
    }

    public void setBody(String _body) {
        this._body = _body;
    }

    // more of the getters and setters ..

}


