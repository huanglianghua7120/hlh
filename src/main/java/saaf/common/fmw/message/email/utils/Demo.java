package saaf.common.fmw.message.email.utils;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);
    public Demo() {
        super();
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.readMail();
    }

    public void readMail() {
        try {
            Properties prop = System.getProperties();
            prop.put("mail.store.protocol", "imap");
            prop.put("mail.imap.host", "imap.exmail.qq.com"); //RmdeskConfig.mailimaphost

            Session session = Session.getInstance(prop);
            IMAPStore store = (IMAPStore)session.getStore("imap"); // 使用imap会话机制，连接服务器
            store.connect("yuanhaigang@chinasie.com", "Gavin819038939");
            IMAPFolder folder = (IMAPFolder)store.getFolder("INBOX"); // 收件箱
            folder.open(Folder.READ_ONLY); //.READ_WRITE
            //获取未读邮件
            Message[] messages = folder.getMessages(folder.getMessageCount() - folder.getUnreadMessageCount() + 1, folder.getMessageCount());
            parseMessage(messages); //解析邮件
            //释放资源
            if (folder != null)
                folder.close(true);
            if (store != null)
                store.close();
            LOGGER.info("读取成功。。。。。。。。。。。。");
        } catch (Exception e) {
            // TODO: handle exception
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 解析邮件
     * @param messages 要解析的邮件列表
     */
    private void parseMessage(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            MimeMessage msg = (MimeMessage)messages[i];
            InternetAddress address = getFrom(msg);
            LOGGER.info(address.getPersonal() + "\t" + address.getAddress() + "\t" + decodeText(msg.getSubject()));
            msg.setFlag(Flags.Flag.SEEN, true);

            //                //存储邮件信息
            //                EmailInfo emaininfo = new EmailInfo();
            //                emaininfo.setEmailcode(msg.getMessageID()); //ID
            //                InternetAddress address = getFrom(msg);
            //                emaininfo.setSender(address.getPersonal()+"<" + address.getAddress() + ">");//张三<zhangsan@163.com>
            //                emaininfo.setTitle(decodeText(msg.getSubject()));//转码后的标题
            //                emaininfo.setReceiver(getReceiveAddress(msg, null));//收件人
            //                emaininfo.setAccepttime(msg.getSentDate());//收件日期
            //                emaininfo.setCheckstatus(Constant.CHECK_STATUS_NO);
            //                try {
            //                    emailInfoMapper.insert(emaininfo);
            //                } catch (Exception e) {
            //                    // TODO: handle exception
            //                    LOGGER.error(e.getMessage(), e);
            //                    LOGGER.info("----邮件信息存入数据库失败了。。。。。");
            //                }
            //
            //                if (isContainAttachment(msg)) {//保存附件
            //                    saveAttachment(msg, RmdeskConfig.filepath,address.getAddress(),address.getPersonal());
            //                }
        }
    }


    /**
     * 获得邮件发件人
     * @param msg 邮件内容
     * @return 地址
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private InternetAddress getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        return (InternetAddress)froms[0];
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     * @param msg 邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    private String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress)address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length() - 1); //删除最后一个逗号

        return receiveAddress.toString();
    }

    /**
     * 判断邮件中是否包含附件
     * @param msg 邮件内容
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */
    private boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart)part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag)
                    break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part)part.getContent());
        }
        return flag;
    }

    /**
     * 保存附件
     * @param part 邮件中多个组合体中的其中一个组合体
     * @param destDir  附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void saveAttachment(Part part, String destDir, String email, String sendName) throws UnsupportedEncodingException, MessagingException, FileNotFoundException,
                                                                                                 IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent(); //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    //                        this.saveFile(is, destDir, decodeText(bodyPart.getFileName()),email,sendName);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir, email, sendName);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        //                            this.saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()),email,sendName);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part)part.getContent(), destDir, email, sendName);
        }
    }

    /**
         * 读取输入流中的数据保存至指定目录
         * @param is 输入流
         * @param fileName 文件名
         * @param destDir 文件存储目录
         * @throws FileNotFoundException
         * @throws IOException
         */
    //        private void saveFile(InputStream is, String destDir, String fileName,String email,String sendName)
    //                throws FileNotFoundException, IOException {
    //            //附件格式过滤
    //            if(!TypeCastUtil.equals(RmdeskConfig.extname, TypeCastUtil.getFileDot(fileName))){
    //                return;
    //            }
    //
    //            DocInfo doc = new DocInfo();
    //            doc.setDocName(fileName);
    //            String uuidFilename = TypeCastUtil.getUUIDFileName(fileName);
    //            doc.setUrl(uuidFilename);
    //            doc.setBusinessLine("测试");
    //            doc.setReceivedMode(Constant.OWNER_TYPE_EMAIL);
    //            doc.setReceivedTime(new Date());
    //            doc.setOwnerEmail(email);
    //            doc.setOwnerName(sendName);
    //            //TODO:入库
    //            try {
    //                infoMapper.insert(doc);
    //            } catch (Exception e) {
    //                LOGGER.error(e.getMessage(), e);
    //                LOGGER.info("----附件存入数据库失败了。。。。。");
    //            }
    //            BufferedInputStream bis = new BufferedInputStream(is);
    //            BufferedOutputStream bos = new BufferedOutputStream(
    //                    new FileOutputStream(new File(destDir + uuidFilename)));
    //            int len = -1;
    //            while ((len = bis.read()) != -1) {
    //                bos.write(len);
    //                bos.flush();
    //            }
    //            bos.close();
    //            bis.close();
    //        }

    /**
     * 文本解码
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    private String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }
}
