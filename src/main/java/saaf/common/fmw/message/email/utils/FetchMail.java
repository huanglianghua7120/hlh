package saaf.common.fmw.message.email.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchMail {
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchMail.class);
    public static void main(String[] args) {
        String protocol = "imap";
        boolean isSSL = true;
        String host = "imap.exmail.qq.com"; //"pop.163.com";
        int port = 993;
        String username = "yuanhaigang@chinasie.com"; //"rose@163.com";
        String password = "Gavin819038993";

        Properties props = new Properties();
        props.put("mail.imap.ssl.enable", isSSL);
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", port);

        Session session = Session.getDefaultInstance(props);

        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore(protocol);
            store.connect(username, password);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            int size = folder.getMessageCount();
            Message message = folder.getMessage(size);

            String from = message.getFrom()[0].toString();
            String subject = message.getSubject();
            Date date = message.getSentDate();

            LOGGER.info("From: " + from);
            LOGGER.info("Subject: " + subject);
            LOGGER.info("Date: " + date);
        } catch (NoSuchProviderException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        LOGGER.info("接收完毕！");
    }
}
