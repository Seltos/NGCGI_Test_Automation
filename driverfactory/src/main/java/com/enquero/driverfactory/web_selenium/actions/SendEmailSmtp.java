package com.enquero.driverfactory.web_selenium.actions;

import com.enquero.driverfactory.web_selenium.base.TestAction;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Sends an email message via an SMTP server.
 */
public class SendEmailSmtp extends TestAction {

    @Override
    public void run() {
        String server = this.readStringArgument("server");
        String subject = this.readStringArgument("subject");
        String body = this.readStringArgument("body");
        String userName = this.readStringArgument("userName");
        String password = this.readStringArgument("password");
        String to = this.readStringArgument("to");
        Integer port = this.readIntArgument("port", 25);
        Boolean useTls = this.readBooleanArgument("useTls", true);
        String cc = this.readStringArgument("cc", null);
        String from = this.readStringArgument("from", "sendemail@getopentest.com");

        try {
            Properties prop = System.getProperties();
            prop.put("mail.smtp.host", server);
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.port", port.toString());

            Session session = Session.getInstance(prop, null);
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
            }

            msg.setSubject(subject);
            msg.setText(body);
            msg.setSentDate(new Date());

            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            transport.setStartTLS(useTls);
            transport.connect(server, userName, password);
            transport.sendMessage(msg, msg.getAllRecipients());

            transport.close();
        } catch (Exception exc) {
            throw new RuntimeException("Failed to send email", exc);
        }
    }
}