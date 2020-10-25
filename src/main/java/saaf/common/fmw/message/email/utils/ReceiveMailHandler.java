package saaf.common.fmw.message.email.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.text.SimpleDateFormat;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.sun.mail.pop3.POP3Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件的收取类
 */
public class ReceiveMailHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveMailHandler.class);
    public static String MAIL_HOST = "imap.exmail.qq.com"; //"pop3.163.com"; //服务器ip

    public static int MAIL_PORT = 993; //110; //端口

    public static String MAIL_TYPE = "imap"; //服务类型

    public static String MAIL_AUTH = "true";

    public static String MAIL_ATTACH_PATH = "upload/recMail/"; //附件存放目录

    /**
     * 获取session会话的方法
     * @return
     * @throws Exception
     */
    private Session getSessionMail() throws Exception {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", MAIL_HOST);
        properties.put("mail.smtp.auth", MAIL_AUTH);
        Session sessionMail = Session.getDefaultInstance(properties, null);
        return sessionMail;
    }

    /**
     * 接收邮件
     * @param 邮箱的用户名和密码
     * @return 无
     */
    public void receiveMail(String userName, String passWord) {
        Store store = null;
        Folder folder = null;
        int messageCount = 0;
        URLName urln = null;
        try {
            //进行用户邮箱连接
            urln = new URLName(MAIL_TYPE, MAIL_HOST, MAIL_PORT, "SSL", userName, passWord);
            store = getSessionMail().getStore(urln);
            store.connect();
            //获得邮箱内的邮件夹Folder对象，以"只读"打开
            folder = store.getFolder("INBOX"); //打开收件箱
            folder.open(Folder.READ_ONLY); //设置只读
            //获得邮件夹Folder内的所有邮件个数
            messageCount = folder.getMessageCount(); // 获取所有邮件个数
            //获取新邮件处理
            LOGGER.info("============>>邮件总数：" + messageCount);
            if (messageCount > 0) {
                Message[] messages = folder.getMessages(messageCount, messageCount); //读取最近的一封邮件
                for (int i = 0; i < messages.length; i++) {
                    String content = getMailContent((Part)messages[i]); //获取内容
                    if (isContainAttach((Part)messages[i])) {
                        saveAttachMent((Part)messages[i], MAIL_ATTACH_PATH);
                    }
                    LOGGER.info("=====================>>开始显示邮件内容<<=====================");
                    LOGGER.info("发送人: " + getFrom(messages[i]));
                    LOGGER.info("主题: " + getSubject(messages[i]));
                    LOGGER.info("内容: " + content);
                    LOGGER.info("发送时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((MimeMessage)messages[i]).getSentDate()));
                    LOGGER.info("是否有附件: " + (isContainAttach((Part)messages[i]) ? "有附件" : "无附件"));
                    LOGGER.info("=====================>>结束显示邮件内容<<=====================");
                    ((POP3Message)messages[i]).invalidate(true);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (folder != null && folder.isOpen()) {
                try {
                    folder.close(true);
                } catch (MessagingException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            if (store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 获得发件人的地址
     * @param message：Message
     * @return 发件人的地址
     */
    private String getFrom(Message message) throws Exception {
        InternetAddress[] address = (InternetAddress[])((MimeMessage)message).getFrom();
        String from = address[0].getAddress();
        if (from == null) {
            from = "";
        }
        return from;
    }

    /**
     * 获得邮件主题
     * @param message：Message
     * @return 邮件主题
     */
    private String getSubject(Message message) throws Exception {
        String subject = "";
        if (((MimeMessage)message).getSubject() != null) {
            subject = MimeUtility.decodeText(((MimeMessage)message).getSubject()); // 将邮件主题解码
        }
        return subject;
    }

    /**
     * 获取邮件内容
     * @param part：Part
     */
    private String getMailContent(Part part) throws Exception {
        StringBuffer bodytext = new StringBuffer(); //存放邮件内容
        //判断邮件类型,不同类型操作不同
        if (part.isMimeType("text/plain")) {
            bodytext.append((String)part.getContent());
        } else if (part.isMimeType("text/html")) {
            bodytext.append((String)part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part)part.getContent());
        } else {
        }
        return bodytext.toString();
    }

    /**
     * 判断此邮件是否包含附件
     * @param part：Part
     * @return 是否包含附件
     */
    private boolean isContainAttach(Part part) throws Exception {
        boolean attachflag = false;
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttach((Part)mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1)
                        attachflag = true;
                    if (contype.toLowerCase().indexOf("name") != -1)
                        attachflag = true;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttach((Part)part.getContent());
        }
        return attachflag;
    }

    /**
     * 保存附件
     * @param part：Part
     * @param filePath：邮件附件存放路径
     */
    private void saveAttachMent(Part part, String filePath) throws Exception {
        String fileName = "";
        //保存附件到服务器本地
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    if (fileName != null) {
                        fileName = MimeUtility.decodeText(fileName);
                        saveFile(fileName, mpart.getInputStream(), filePath);
                    }
                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttachMent(mpart, filePath);
                } else {
                    fileName = mpart.getFileName();
                    if (fileName != null) {
                        fileName = MimeUtility.decodeText(fileName);
                        saveFile(fileName, mpart.getInputStream(), filePath);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part)part.getContent(), filePath);
        }

    }

    /**
     * 保存附件到指定目录里
     * @param fileName：附件名称
     * @param in：文件输入流
     * @param filePath：邮件附件存放基路径
     */
    private void saveFile(String fileName, InputStream in, String filePath) throws Exception {
        File storefile = new File(filePath);
        if (!storefile.exists()) {
            storefile.mkdirs();
        }
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath + "\\" + fileName));
            bis = new BufferedInputStream(in);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (bis != null) {
                bis.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new ReceiveMailHandler().receiveMail("yuanhaigang@chinasie.com", "Gavin819038939");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        }
    }
}
