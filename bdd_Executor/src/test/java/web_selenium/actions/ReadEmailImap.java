package web_selenium.actions;

import jdk.nashorn.api.scripting.ScriptUtils;
import web_selenium.base.TestAction;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Reads email messages from an IMAP server.
 */
public class ReadEmailImap extends TestAction {

    @Override
    public void run() {
        super.run();

        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String defaultSentAfterTime = LocalDateTime.now().minusDays(365).format(dateTimeFormatter);

        String imapServer = this.readStringArgument("server");
        Integer port = this.readIntArgument("port", null);
        String subjectContains = this.readStringArgument("subjectContains",
                this.readStringArgument("subject", null));
        Pattern subjectRegex = this.readRegexArgument("subjectRegex", null);
        String bodyContains = this.readStringArgument("bodyContains", null);
        Pattern bodyRegex = this.readRegexArgument("bodyRegex", null);
        String fromAddress = this.readStringArgument("from", null);
        String sentAfter = this.readStringArgument("sentAfter", defaultSentAfterTime);
        String userName = this.readStringArgument("userName");
        String password = this.readStringArgument("password");
        Integer maxResults = this.readIntArgument("maxResults", 1);
        Integer minResults = this.readIntArgument("minResults", 1);

        LocalDateTime sentAfterDate = LocalDateTime.parse(
                sentAfter,
                dateTimeFormatter);
        ZonedDateTime sentAfterZonedDate = ZonedDateTime.of(
                sentAfterDate,
                ZoneId.systemDefault());

        Folder inbox = null;
        Store store = null;

        try {
            Session session = Session.getDefaultInstance(System.getProperties());
            store = session.getStore("imaps");
            if (port == null) {
                store.connect(imapServer, userName, password);
            } else {
                store.connect(imapServer, port, userName, password);
            }

            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);

            SentDateTerm sentDateTerm = new SentDateTerm(
                    ComparisonTerm.GE,
                    Date.from(sentAfterZonedDate.toInstant()));
            
            SearchTerm searchTerm = sentDateTerm;
            
            if (subjectContains != null) {
                SubjectTerm subjectTerm = new SubjectTerm(subjectContains);
                searchTerm = new AndTerm(searchTerm, subjectTerm);
            }
            
            if (bodyContains != null) {
                BodyTerm bodyTerm = new BodyTerm(bodyContains);
                searchTerm = new AndTerm(searchTerm, bodyTerm);
            }
            
            if (fromAddress != null) {
                FromStringTerm fromTerm = new FromStringTerm(fromAddress);
                searchTerm = new AndTerm(searchTerm, fromTerm);
            }
            
            List<Message> allMessages = Arrays.asList(inbox.search(searchTerm));

            int resultsIndex = 0;
            ArrayList<HashMap<String, Object>> resultMessages = new ArrayList<>();
            while (resultMessages.size() < maxResults) {
                resultsIndex++;

                if (resultsIndex > allMessages.size()) {
                    break;
                }

                Message currentMessage = allMessages.get(allMessages.size() - resultsIndex);

                // Make sure the sent date and time is after the specified
                // one. Since javax.mail only takes the date into consideration
                // and ignores the time, we might end up with messages sent earlier
                // than the time specified, so we must check this ourselves
                boolean sentDateIsInRange
                        = currentMessage.getSentDate().after(Date.from(sentAfterZonedDate.toInstant()));
                if (!sentDateIsInRange) {
                    continue;
                }

                String subject = currentMessage.getSubject();
                if (subjectRegex != null && !subjectRegex.matcher(subject).find()) {
                    continue;
                }

                String body = this.getTextFromMessage(currentMessage);
                if (bodyRegex != null && !bodyRegex.matcher(body).find()) {
                    continue;
                }

                HashMap<String, Object> resultMessage = new HashMap<>();
                resultMessage.put("subject", subject);
                resultMessage.put("body", body);
                resultMessage.put("from", getEmailAddresses(currentMessage.getFrom()));
                resultMessage.put("to", getEmailAddresses(currentMessage.getAllRecipients()));
                LocalDateTime sentDate = LocalDateTime.ofInstant(currentMessage.getSentDate().toInstant(), ZoneId.systemDefault());
                resultMessage.put("sentDate", sentDate.format(dateTimeFormatter));

                HashMap<String, String> headers = new HashMap<>();
                Enumeration<Header> headersIterator = currentMessage.getAllHeaders();

                while (headersIterator.hasMoreElements()) {
                    Header header = (Header) headersIterator.nextElement();
                    headers.put(header.getName(), header.getValue());
                }
                resultMessage.put("headers", headers);

                resultMessages.add(resultMessage);
            }

            if (resultMessages.size() < minResults) {
                throw new RuntimeException(String.format(
                        "We were expecting to find at least %s email message(s) matching the "
                        + "given criteria, but we found %s. You can set the minimum number "
                        + "of messages expected by using the \"minResults\" argument.",
                        minResults,
                        resultMessages.size()));
            } else {
                log.info(String.format(
                        "We found %s email message(s) matching the given criteria",
                        resultMessages.size()));
            }

            this.writeOutput("emails", ScriptUtils.wrapArray(resultMessages.toArray()));

            // Write the details of the most recent message again, to
            // dedicated output values. These three output values will be
            // deprecated and are only populated for backward compatibility.
            HashMap mostRecentMessage = resultMessages.get(0);
            this.writeOutput("subject", mostRecentMessage.get("subject"));
            this.writeOutput("body", mostRecentMessage.get("body"));
            this.writeOutput("from", mostRecentMessage.get("from"));
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to read emails from server %s", imapServer), ex);
        } finally {
            try {
                if (inbox != null) {
                    inbox.close(true);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException ex) {
                log.error("Failed to close the email store and/or folder", ex);
            }
        }
    }

    private String getEmailAddresses(Address[] addresses) {
        if (addresses == null) {
            return "";
        }

        ArrayList<String> emailAddresses = new ArrayList<>();
        for (Address address : addresses) {
            emailAddresses.add(((InternetAddress) address).getAddress());
        }

        return String.join(";", emailAddresses);
    }

    /**
     * Extracts the text content of an email message with support for multipart
     * messages
     */
    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        } else {
            Object content = message.getContent();
            result = content.toString();
        }

        return result;
    }

    /**
     * Extracts the text content from a multipart email message.
     */
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        ArrayList<String> partStrings = new ArrayList<>();
        
        int partCount = mimeMultipart.getCount();
        for (int i = 0; i < partCount; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                partStrings.add((String)bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                partStrings.add((String)bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                partStrings.add(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        
        return String.join("\n", partStrings);
    }
}
