package saaf.common.fmw.message.model.inter.server;

import com.base.sie.common.exception.email.EmailException;
import com.sun.mail.util.MailSSLSocketFactory;

import saaf.common.fmw.message.email.utils.EmailUtils;
import saaf.common.fmw.message.model.entities.MailSenderInfoBean;
import saaf.common.fmw.message.model.entities.MyAuthenticator;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;


/*===========================================================+
  |   Copyright (c) 2012 赛意信息科技有限公司                                         |
+===========================================================+
  |  HISTORY                                                                        |
  | ============ ====== ============  ===========================                   |
  |  Date                     Ver.        Administrator                   Content          |
  | ============ ====== ============  ===========================                   |
  |  Jun 23, 2015            1.0           XXX                      Creation        |
  | 主要是做邮件发送功能的，客户端只需要将需要发送的邮件内容、标题、接收人、抄送人、密送人、附件等信息传递进来即可
 +===========================================================*/
@Component("emailSenderServer")
public class EmailSenderServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderServer.class);

    public Boolean sendEmail(MailSenderInfoBean mailInfo) throws EmailException {
        //, String[] to, String[] cs, String[] ms, String subject, String content, String formEmail, String[] fileList) {
        Boolean sendSuccessFlag = false;
        if (null == mailInfo) {
            throw new EmailException("MailSenderInfo object is null, please init the object");
        }
        String[] to = null;
        if (null != mailInfo.getToList()) {
            to = mailInfo.getToList().toArray(new String[mailInfo.getToList().size()]);
        }
        String[] cs = null;
        if (null != mailInfo.getCsList()) {
            cs = mailInfo.getCsList().toArray(new String[mailInfo.getCsList().size()]);
        }
        String[] ms = null;
        if (null != mailInfo.getMsList()) {
            ms = mailInfo.getMsList().toArray(new String[mailInfo.getMsList().size()]);
        }
        if (null == to && null == cs && null == ms) {
            throw new EmailException("MailSenderInfo object's toList or csList or msList attribute must have value, please update like test@hello.com.");
        }
        String[] fileList = null;
        if (null != mailInfo.getArrArchiveList()) {
            fileList = mailInfo.getArrArchiveList().toArray(new String[mailInfo.getArrArchiveList().size()]);
        }
        String subject = mailInfo.getSubject();
        if (null == subject) {
            throw new EmailException("MailSenderInfo object's subject attribute must have value, please update.");
        }
        String content = mailInfo.getContent();
        if (null == content) {
            throw new EmailException("MailSenderInfo object's content attribute must have value, please update.");
        }
        String fromEmail = null;
        try {
            MyAuthenticator authenticator = null;
            Properties pro = mailInfo.getProperties();
            String userName = pro.getProperty("mail.smtp.userName");
            fromEmail = userName;
            String password = pro.getProperty("mail.smtp.password");
            
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            pro.put("mail.smtp.ssl.enable", "true");
            pro.put("mail.smtp.ssl.socketFactory", sf);
            
            LOGGER.warn("saaf.common.fmw.service.mail.utils.EmailSender-->sendEmail====>userName-->" + userName);
            if (mailInfo.isValidate()) {
                authenticator = new MyAuthenticator(userName, password);
            }
            Session session = Session.getInstance(pro, authenticator);
            // 建立会话
            Message msg = new MimeMessage(session); //建立信息
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            msg.setFrom(new InternetAddress(fromEmail)); // 发件人

            String toList = null;
            String toListcs = null;
            String toListms = null;

            //发送
            if (to != null) {
                toList = EmailUtils.validateEmailList(to);
                InternetAddress[] iaToList = new InternetAddress().parse(toList);
                msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人
            }

            //抄送
            if (cs != null) {
                toListcs = EmailUtils.validateEmailList(cs);
                InternetAddress[] iaToListcs = new InternetAddress().parse(toListcs);
                msg.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
            }

            //密送
            if (ms != null) {
                toListms = EmailUtils.validateEmailList(ms);
                InternetAddress[] iaToListms = new InternetAddress().parse(toListms);
                msg.setRecipients(Message.RecipientType.BCC, iaToListms); // 密送人
            }
            Date sendDate = new Date();
            mailInfo.setSendDate(sendDate);
            msg.setSentDate(sendDate); // 发送日期
            msg.setSubject(subject); // 主题
            msg.setText(content); // 内容
            //显示以html格式的文本内容
            messageBodyPart.setContent(content, "text/html;charset=gbk");
            multipart.addBodyPart(messageBodyPart);
            // 2.保存多个附件
            if (fileList != null) {
                EmailUtils.addTach(fileList, multipart);
            }
            msg.setContent(multipart);
            Transport tran = session.getTransport("smtp");
            tran.send(msg, msg.getAllRecipients());
            LOGGER.warn("sendEmail==> the email send success..........");
            System.out.println("sendEmail==> the email send success..........");
            mailInfo.setSendSuccessFlag(true);
            sendSuccessFlag = true;
            mailInfo.setErrorInfo(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            mailInfo.setSendSuccessFlag(false);
            sendSuccessFlag = false;
            mailInfo.setErrorInfo(e.getMessage());
        }
        return sendSuccessFlag;
    }


    //####################################################################
    /**
     * @param mailInfo
     */
    //    public boolean sendTextMail(MailSenderInfo mailInfo) {
    //        MyAuthenticator authenticator = null;
    //        Properties pro = mailInfo.getProperties();
    //        if (mailInfo.isValidate()) {
    ////            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
    //        }
    //        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
    //        try {
    //            Message mailMessage = new MimeMessage(sendMailSession);
    //            Address from = new InternetAddress(mailInfo.getFromAddress());
    //            mailMessage.setFrom(from);
    //            Address to = new InternetAddress(mailInfo.getToAddress());
    //            mailMessage.setRecipient(Message.RecipientType.TO, to);
    //            mailMessage.setSubject(mailInfo.getSubject());
    //            mailMessage.setSentDate(new Date());
    //            String mailContent = mailInfo.getContent();
    //            mailMessage.setText(mailContent);
    //            Transport.send(mailMessage);
    //            return true;
    //        } catch (MessagingException ex) {
    //            ex.printStackTrace();
    //        }
    //        return false;
    //    }

    /**
     *
     * @param mailInfo
     */
    //    public boolean sendHtmlMail(MailSenderInfo mailInfo) {
    //        MyAuthenticator authenticator = null;
    //        Properties pro = mailInfo.getProperties();
    //        if (mailInfo.isValidate()) {
    ////            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
    //        }
    //        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
    //        try {
    //            Message mailMessage = new MimeMessage(sendMailSession);
    //            Address from = new InternetAddress(mailInfo.getFromAddress());
    //            mailMessage.setFrom(from);
    //            Address to = new InternetAddress(mailInfo.getToAddress());
    //            mailMessage.setRecipient(Message.RecipientType.TO, to);
    //            mailMessage.setSubject(mailInfo.getSubject());
    //            mailMessage.setSentDate(new Date());
    //
    //            Multipart mainPart = new MimeMultipart();
    //            BodyPart html = new MimeBodyPart();
    //            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
    //            mainPart.addBodyPart(html);
    //
    //            html = new MimeBodyPart();
    //            FileDataSource fds = new FileDataSource("D:\\Temp\\DTPConnDBUtil.java");
    //            DataHandler dh = new DataHandler(fds);
    //            html.setFileName("javamail.doc");
    //            html.setDataHandler(dh);
    //            mainPart.addBodyPart(html);
    //
    //            mailMessage.setContent(mainPart);
    //            mailMessage.saveChanges();
    //
    //            Transport.send(mailMessage);
    //            return true;
    //        } catch (MessagingException ex) {
    //            ex.printStackTrace();
    //        }
    //        return false;
    //    }
}
